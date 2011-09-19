package Task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.swixml.contrib.gmap.Validate;

import Task.Manager.TaskManager;

/**
 * SwingNetworkTask is a SwingWorker that's optimzed for calling services in the platform
 * and handling mundane error processing, etc. The key difference between this
 * class and SwingTask is that this class is aware of the {@link TaskManager} state,
 * and when it is not in ONLINE mode,
 * these tasks do not execute (TaskExecutorIF is not run), they just report they
 * are not in online mode.
 * <p/>
 * When using this class, you can bind your UI to the following properties:
 * <dl>
 * <dt> <b>PropertyNames.Status</b>
 * <dd> A string message will be sent as status changes happen
 * </dl>
 * <b>Registering tasks with TaskManager</b><br>
 * When an instance of this class is created, it's registered with the manager, using
 * which allows it to monitor running/dead tasks.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since May 30, 2007, 1:33:23 PM
 */
public class NetworkTask<ReturnValueType> extends AbstractTask {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// type
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public Type getType() {
  return Type.NonRecurringNetworkTask;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructors
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public NetworkTask(TaskManager container,
                   TaskExecutorIF<ReturnValueType> exec,
                   String name, String descr,
                   AutoShutdownSignals... policy) throws IllegalArgumentException
{
  Validate.notNull(exec, "TaskExecutorIF<T> can not be null");
  Validate.notNull(container, "DesktopAppContainerIF can not be null");
  Validate.notEmpty(name, "name can not be empty or null");
  Validate.notEmpty(descr, "descr can not be empty or null");

  _exec = exec;

  setTaskManager(container);

  Validate.notEmpty(name, "TaskRoot name can not be empty or null");
  Validate.notEmpty(descr, "TaskRoot description can not be empty or null");

  setName(name);
  setDescription(descr);
  setAutoShutdownPolicy(policy);

  container.registerTask(this);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// auto stop support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * subclass should implement this method as is appropriate for them. this is called when the task is stopped,
 * based on the {@link AutoShutdownSignals} policy
 */
public void doShutdown() {
  cancel();
  _taskHandler.shutdownCalled(this);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// testing mode...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
NetworkTask(TaskExecutorIF<ReturnValueType> exec) {
  Validate.notNull(exec, "exec can not be null");
  _exec = exec;
  testMode_Enabled = true;
}

protected boolean testMode_Enabled = false;
protected boolean testMode_isOnline = false;

public void setTestMode_isOnline(boolean flag) {
  testMode_isOnline = flag;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// task handler support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
protected NetworkTaskHandlerIF<ReturnValueType> _taskHandler =
    new NetworkTaskHandler<ReturnValueType>(); // empty impl, to void nullpointerexception

public void setTaskHandler(NetworkTaskHandlerIF<ReturnValueType> handler) throws IllegalArgumentException {
  Validate.notNull(handler, "NetworkTaskHandler can not be null");

  _taskHandler = handler;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// task execution
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
protected TaskExecutorIF<ReturnValueType> _exec;
protected SwingWorker<ReturnValueType, Void> _swingWorker;

public SwingWorker<ReturnValueType, Void> getSwingWorker() {
  return _swingWorker;
}

/**
 * For each invocation, this method creates a new SwingWorker object to perform a background
 * task, and then it uses the provided TasKHandler to notify you of the changes to the task's
 * lifecycle.
 * <p/>
 * If the task has been shutdown then it can't be executed anymore.
 */
public void execute() throws TaskException {
  _assertIsNotShutdown();
  _assertNoSwingWorkerCurrentlyActive();

  // << BEFORE START >>
  StateChanger.signalBeforeStart(_taskHandler, this);

  // << START >>
  StateChanger.signalStart(this, _taskHandler, _exec);

  // << actually start the underlying SwingWorker thread... (not for RecurringNetworkTask) >>
  // create the swingworker, and don't use a blocking Q
  _createSwingWorker(null);
  // update the uihook
  getUIHook().setSwingWorker(_swingWorker);
  // run the SwingWorker in a background/non-edt thread
  _swingWorker.execute();
}

/**
 * Support for {@link RecurringNetworkTask}.
 * <p/>
 * Create a swing worker thread, but wait until the underlying thread is done. This means
 * that the calling thread waits until this underlying swing worker is complete. This is used by
 * {@link RecurringNetworkTask#_initScheduledFuture()}.
 * <p/>
 * What happens when the recurring task is shutdown? Look at {@link RecurringNetworkTask#stopRecurring()}.
 * Basically, the task executor is shutdown, which prevents any future tasks from being spawned. What
 * about the task that's already running? By issing a cancel to the _scheduledFuture, it causes
 * InterruptedException to be thrown on the Runnable which is waiting on this method to complete.
 * Hence, in the section in this code where InterruptedException is thrown, it should be taken as
 * a signal to propagate the cancellation of all SwingWorkers that may be currently running (which is
 * why this method would be blocked at the gate). If it's not blocked at the gate, nothing is currently
 * running, so shutdown is trivial.
 *
 * @see RecurringNetworkTask#start()
 * @see RecurringNetworkTask#_initScheduledFuture()
 */
protected void executeAndWait() throws TaskException {
  _assertIsNotShutdown();

  // << BEFORE START >>
  StateChanger.signalBeforeStart(_taskHandler, this);

  // << START >>
  StateChanger.signalStart(this, _taskHandler, _exec);

  // << actually start the underlying SwingWorker thread... >>
  // create the underlying SwingWorker with blocking Q
  BlockingQueue gate = new ArrayBlockingQueue<String>(1);
  _createSwingWorker(gate);
  // bind the UIHook to the SwingWorker
  getUIHook().setSwingWorker(_swingWorker);
  // run the SwingWorker in it's own background/non-EDT thread
  _swingWorker.execute();

  try {
    //System.out.println("NetworkTask waiting on gate (waiting for spawned SwingWorker to end)");
    // wait on the SwingWorker to complete...
    gate.take();
  }
  catch (InterruptedException e) {
    /**
     * stop waiting and return... this can get called when the recurring task executor is
     * shutdown here - {@link RecurringNetworkTask# stopRecurring}.
     */
    cancel();
  }
  //System.out.println("NetworkTask done waiting on gate (spawned SwingWorker has ended)");
}

/**
 * instantiates the underlying swing worker implementation...
 *
 * @param gate if it is null, then this is just a regular SwingNetworkTask.
 *             if not null, this implies that this is for use with the SwingRecurringNetworkTask, and this
 *             gate is meant to block the caller of {@link #executeAndWait()} blocked until this SwingWorker
 *             completes what it's doing (err, ok, interrupted).
 */
protected void _createSwingWorker(final BlockingQueue gate) {
  _swingWorker = new SwingWorker<ReturnValueType, Void>() {

    boolean isNotOnline = false;

    long startTime;
    long endTime;

    /** runs in background thread, NOT EDT */
    protected ReturnValueType doInBackground() throws Exception {
      startTime = System.currentTimeMillis();

      // check if this object is in test-mode...
      if (testMode_Enabled) { // << in test mode >>
        if (testMode_isOnline) {
          return _exec.doInBackground(this, getUIHook());
        }
        else {
          isNotOnline = true;
          return null;
        }
      }
      else {                 // << not in test mode >>
        // check container state, if not ONLINE, then set isNotOnline
        if (getTaskManager() != null && getTaskManager().isOnline()) {
          return _exec.doInBackground(this, getUIHook());
        }
        else {
          isNotOnline = true;
          return null;
        }
      }

    }// doInBackground

    /** runs in EDT */
    @Override protected void done() {
      if (!isNotOnline) { // is ONLINE
        try {
          // background thread is done... try and get return value
          ReturnValueType retVal = get();

          endTime = System.currentTimeMillis();

          // << STOPPED >>
          StateChanger.signalStopped(NetworkTask.this, _taskHandler, endTime - startTime);

          // << OK >>
          StateChanger.signalOK(NetworkTask.this, _taskHandler, _exec, retVal, endTime - startTime);
        }
        catch (ExecutionException ex) {
          endTime = System.currentTimeMillis();

          // << STOPPED >>
          StateChanger.signalStopped(NetworkTask.this, _taskHandler, endTime - startTime);

          // << ERROR >>
          StateChanger.signalErr(NetworkTask.this, _taskHandler, _exec, ex, endTime - startTime);

        }
        catch (InterruptedException ex) {
          endTime = System.currentTimeMillis();

          // << STOPPED >>
          StateChanger.signalStopped(NetworkTask.this, _taskHandler, endTime - startTime);

          // << INTERRUPTED >>
          StateChanger.signalInterrupted(NetworkTask.this, _taskHandler, _exec, ex, endTime - startTime);
        }
        catch (CancellationException ex) {
          endTime = System.currentTimeMillis();

          // << STOPPED >>
          StateChanger.signalStopped(NetworkTask.this, _taskHandler, endTime - startTime);

          // << CANCELLED >>
          StateChanger.signalCancelled(NetworkTask.this, _taskHandler, _exec, endTime - startTime);
        }
      }
      else { // is NOT ONLINE (BOOT, OFFLINE)
        endTime = System.currentTimeMillis();

        // << STOPPED >>
        StateChanger.signalStopped(NetworkTask.this, _taskHandler, endTime - startTime);

        // << NOT ONLINE >>
        StateChanger.signalNotOnline(NetworkTask.this, _taskHandler, _exec);
      }

      // the following must be called regardless of online/offline, in case executeAndWait
      // is running, release the thread waiting on the gate...
      // it doesnt matter if the swingworker has been cancelled or not... _openGate can be
      // called... chances are that the executeAndWait method has already been interrupted.
      _openGate(gate);

    }// done

  }; // end SwingWorker
}//end _createSwingWorker

/**
 * This method releases the thread that's waiting on the BlockingQueue. This waiting thread is the one
 * that spawned this SwingWorker thread, from a call to {@link #executeAndWait()} from the
 * {@link RecurringNetworkTask} class.
 * <p/>
 * If the gate is null, then do not do anything. This is an invocation of a regular SwingNetworkTask,
 * not a SwingRecurringNetworkTask.
 *
 * @param gate may be null.
 */
@SuppressWarnings("unchecked")
protected void _openGate(final BlockingQueue gate) {
  // mark the end of the method for anyone waiting on gate...
  if (gate != null) {
    try {
      gate.put("");
    }
    catch (InterruptedException e) {
      // should never happen
    }
  }
}

}//end class TaskRoot
