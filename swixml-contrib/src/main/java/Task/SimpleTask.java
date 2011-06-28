package Task;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.swixml.contrib.gmap.Validate;

import Task.Manager.TaskManager;

/**
 * Task is a SwingWorker that's optimzed for calling services in the platform
 * and handling mundane error processing, etc.
 * <p/>
 * When using this class, you can bind your UI to the following properties:
 * <dl>
 * <dt> <b>PropertyNames.Status</b>
 * <dd> A string message will be sent as status changes happen
 * <dt> <b>PropertyNames.State</b>
 * <dd> A State change will be issued when the background thread is started/stopped. Also,
 * upon completion of the task, a OK or Err property will be fired. In the OK property,
 * change event, the first parameter (old value) is the return value from the TaskExecutorIF,
 * and the 2nd parameter (new value) is the State enum.<br>
 * Similarly, for the Err property, the first value is the Throwable, and the 2nd is the
 * State enum.
 * </dl>
 * <b>Registering tasks with container</b><br>
 * When an instance of this class is created, it's registered with the container, using
 * the container's ContainerTaskManager object. This provides the container with the
 * monitor running/dead tasks.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since May 30, 2007, 1:33:23 PM
 */
public class SimpleTask<ReturnValueType> extends AbstractTask {
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// type
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public Type getType() {
  return Type.NonRecurringTask;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructor
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** use this constructor if you want the tasks to show up in the DesktopAppContainerIF */
public SimpleTask(TaskManager taskManager,
                 TaskExecutorIF<ReturnValueType> exec,
                 String name, String descr,
                 AutoShutdownSignals... autoShutdownPolicy) throws IllegalArgumentException
{
  Validate.notNull(exec, "TaskExector<T> can not be null");
  Validate.notEmpty(name, "TaskRoot name can not be empty or null");
  Validate.notEmpty(descr, "TaskRoot description can not be empty or null");
  Validate.notNull(taskManager, "DesktopAppContainerIF can not be null");
  Validate.notNull(autoShutdownPolicy, "AutoStop can not be null");

  setTaskManager(taskManager);

  _exec = exec;

  setName(name);
  setDescription(descr);

  setAutoShutdownPolicy(autoShutdownPolicy);

  taskManager.registerTask(this);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// autostop policy support
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

/** use this constructor if you don't want to deal with the DesktopAppContainerIF hooks */
SimpleTask(TaskExecutorIF<ReturnValueType> exec) {
  Validate.notNull(exec, "exec can not be null");

  _exec = exec;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// task handler support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
SimpleTaskHandlerIF<ReturnValueType> _taskHandler =
    new SimpleTaskHandler<ReturnValueType>(); // empty implementation, to avoid nullpointerexceptions

public void setTaskHandler(SimpleTaskHandlerIF<ReturnValueType> handler) throws IllegalArgumentException {
  Validate.notNull(handler, "SimpleTaskHandler can not be null");

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
 */
public void execute() throws TaskException {
  _assertIsNotShutdown();
  _assertNoSwingWorkerCurrentlyActive();

  // << BEFORE START >>
  StateChanger.signalBeforeStart(_taskHandler, this);

  // << START >>
  StateChanger.signalStart(this, _taskHandler, _exec);

  // << actually start the underlying SwingWorker thread... >>
  // create the underlying SwingWorker
  _createSwingWorker();
  // bind the UIHook to the SwingWorker
  getUIHook().setSwingWorker(_swingWorker);
  // run the SwingWorker in it's own background/non-EDT thread
  _swingWorker.execute();
}

/** instantiates the underlying swing worker implementation... */
protected void _createSwingWorker() {
  _swingWorker = new SwingWorker<ReturnValueType, Void>() {

    long startTime;
    long endTime;

    /** runs in background thread, not EDT */
    protected ReturnValueType doInBackground() throws Exception {
      startTime = System.currentTimeMillis();
      return _exec.doInBackground(this, getUIHook());
    }// doInBackground

    /** runs in EDT */
    @Override protected void done() {
      try {
        //System.out.println("SwingWorker - done()!!!");

        // background thread is done... try and get return value
        ReturnValueType retVal = get();

        endTime = System.currentTimeMillis();

        // << STOPPED >>
        StateChanger.signalStopped(SimpleTask.this, _taskHandler, endTime - startTime);

        // << OK >>
        StateChanger.signalOK(SimpleTask.this, _taskHandler, _exec, retVal, endTime - startTime);
      }
      catch (ExecutionException ex) {
        endTime = System.currentTimeMillis();

        // << STOPPED >>
        StateChanger.signalStopped(SimpleTask.this, _taskHandler, endTime - startTime);

        // << ERROR >>
        StateChanger.signalErr(SimpleTask.this, _taskHandler, _exec, ex, endTime - startTime);
      }
      catch (InterruptedException ex) {
        endTime = System.currentTimeMillis();

        // << STOPPED >>
        StateChanger.signalStopped(SimpleTask.this, _taskHandler, endTime - startTime);

        // << INTERRUPTED >>
        StateChanger.signalInterrupted(SimpleTask.this, _taskHandler, _exec, ex, endTime - startTime);
      }
      catch (CancellationException ex) {
        endTime = System.currentTimeMillis();

        // << STOPPED >>
        StateChanger.signalStopped(SimpleTask.this, _taskHandler, endTime - startTime);

        // << CANCELLED >>
        StateChanger.signalCancelled(SimpleTask.this, _taskHandler, _exec, endTime - startTime);
      }
    }// done
  };
}// end _createSwingWorker


}//end class TaskRoot
