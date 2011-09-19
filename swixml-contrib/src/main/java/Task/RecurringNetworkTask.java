package Task;

import Task.Manager.*;

import java.util.concurrent.*;
import java.util.logging.*;

/**
 * This task is a subclass of {@link NetworkTask}.
 * It adds the abillty to perform recurring tasks on top of the implementation of
 * {@link NetworkTask}.
 * <p/>
 * You can specify a refresh delay to define the schedule that will be used to run the
 * TaskExecutor. You can change this at any time as well.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since May 30, 2007, 1:33:23 PM
 */
public class RecurringNetworkTask<ReturnValueType> extends NetworkTask<ReturnValueType> {
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// type
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public Type getType() {
  return Type.RecurringNetworkTask;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructor
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public RecurringNetworkTask(TaskManager container,
                            TaskExecutorIF<ReturnValueType> exec,
                            String name, String descr,
                            AutoShutdownSignals... policy) throws IllegalArgumentException
{
  super(container, exec, name, descr, policy);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// auto stop support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * subclass should implement this method as is appropriate for them. this is called when the task is stopped,
 * based on the {@link AutoShutdownSignals} policy
 */
@Override public void doShutdown() {
  stopRecurring();
  _taskHandler.shutdownCalled(this);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// test mode
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public RecurringNetworkTask(TaskExecutorIF<ReturnValueType> exec) {
  super(exec);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// task execution
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
protected int _refreshDelayInSec = 30; //default is 30 sec
protected ScheduledExecutorService _execService;
protected ScheduledFuture _scheduledFuture;

/** Start the recurring network task... it will periodically run the TaskExecutorIF in the background. */
public void start(int refreshDelayInSec) throws TaskException {
  setRefreshDelay(refreshDelayInSec);
  _initScheduledFuture();
}

/** Start the recurring network task... it will use the default refresh delay... */
public void start() throws TaskException {
  _initScheduledFuture();
}

/** After {@link #start} is called, this method can be used to change the refreshDelayInSec ... */
public void restartWithNewDelay(int refreshDelayInSec) throws TaskException {
  setRefreshDelay(refreshDelayInSec);
  _cancelScheduledFuture();
  _initScheduledFuture();
}

/**
 * This method actually attaches a Runnable object + delay with the _execService.
 * <p/>
 * The way this scheduler works is that it creates a new task at a delay. As long as a task
 * is running, it will not create another one. So, the {@link #executeAndWait()} method just
 * creates a new SwingWorker, and then waits for it to complete. The reason not to use {@link #execute()}
 * is simply to keep new tasks from getting created until the first one finishes. That's why there is a
 * gate to block the currently executing Runnable below until the underlying SwingWorker is complete!
 * <p/>
 * So really, the total delay between these SwingWorkers being spawned is:
 * Time required by the SwingWorker to execute to completion (err, ok, interrupted) + delay
 */
protected void _initScheduledFuture() throws TaskException {
  _assertIsNotShutdown();

  if (_scheduledFuture != null) return;

  _execService = Executors.newScheduledThreadPool(1);
  // System.out.println("executor service created");

  // start up the scheduled executor, and start creating new scheduled tasks...
  _scheduledFuture = _execService.scheduleWithFixedDelay(
      new Runnable() {
        public void run() {
          try {
            executeAndWait(); // spawns a new SwingWorker and executes it...
          }
          catch (TaskException e) {
            _taskManager.writeExceptionToLog(Level.WARNING, getName() + "shutting down", e);
            shutdown();
          }
        }
      },
      0,
      _refreshDelayInSec,
      TimeUnit.SECONDS
  );
}

/**
 * This will cancel any Runnables that are being scheduled by the executor service.
 * Additionally, if any SwingWorkers are currently running, they are cancelled as well.
 *
 * @see #cancel
 * @see #executeAndWait()
 */
protected void _cancelScheduledFuture() {
  if (_scheduledFuture == null) return;
  _scheduledFuture.cancel(true);
  cancel();
  _scheduledFuture = null;
}

/**
 * Stop the recurring network task... this will stop the network task from running TaskExecutorIF
 * in the background. Also, stop any currently executing tasks:
 * <ol>
 * <li>Thread: {@link #getSwingWorker()} - Method to kill it:{@link #cancel()}
 * <li>Thread: {@link #_scheduledFuture} - Method to kill it:{@link #_cancelScheduledFuture()}
 * </ol>
 */
public void stopRecurring() {
  if (_execService != null) {
    _execService.shutdown();
    // System.out.println("executor service shutdown");
    _execService = null;
  }

  _cancelScheduledFuture();
}

/**
 * Set the time delay between running TaskExecutorIF continually.
 *
 * @param refreshDelayInSec the time delay is in {@link TimeUnit#SECONDS}
 */
public void setRefreshDelay(int refreshDelayInSec) {
  _refreshDelayInSec = refreshDelayInSec;
}

}//end class TaskRoot