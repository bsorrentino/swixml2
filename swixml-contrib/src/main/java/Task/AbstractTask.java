package Task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingWorker;

import org.swixml.contrib.gmap.Validate;

import Task.Manager.TaskManager;
import Task.ProgressMonitor.SwingUIHookAdapter;
import Task.ProgressMonitor.UIHookIF;

/**
 * AbstractTask is a super class that encapsulates some common functionality between various types of tasks:
 * <ol>
 * <li>{@link SimpleTask}
 * <li>{@link NetworkTask}
 * <li>{@link RecurringNetworkTask}
 * </ol>
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Jun 29, 2007, 1:10:10 PM
 */
public abstract class AbstractTask {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// enums
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** identifies the type of the task */
public enum Type {
  /** this identfies a non recurring task that doesn't perform any network ops */
  NonRecurringTask,
  /** this identifies a non recurring task that performs network ops */
  NonRecurringNetworkTask,
  /** this identifies a non recurring task that performs network ops */
  RecurringNetworkTask
}

/**
 * List of all the possible states that any kind of Task (Network, non network, etc) may have. The lifecycle
 * begins with Started, and ends in Stopped (regardless of the outcome). Once, Stopped, it may transition to
 * OK, Interrupted, Err, Cancelled. For networked tasks, that are not online, it goes from Started to Stopped to
 * NotOnline. Bascially, Stopped is called pretty much any time after Started - you can put code in there to
 * turn off busy label, etc or something like that.
 */
public enum State {
  /** task is started */
  Started,
  /** task is stopped - regardless of Err, OK, or NotOnline */
  Stopped,
  /**
   * Task is interrupted. Stopped, OK, or Err never get called. Cancel trumps Interrupted - the task may be interrupted
   * by someone calling cancel on the SwingWorker ({@link AbstractTask#getSwingWorker()}. There are a very few conditions
   * in which the SwingWorker can be interrupted - if it's wait()ing for example, or blocking on some other operation.
   */
  Interrupted,
  /** after task is Stopped, result is good - OK */
  OK,
  /** after task is Stopped, result is bad - Err. If Err is an AuthToken violation OFFLINE mode is activated */
  Err,
  /**
   * After task is Started, if {@link AbstractTask#getSwingWorker()}.cancel() is called, this state is reached as soon
   * as the SwingWorker's (and {@link TaskExecutorIF}'s) doInBackground method completes (is done()). This is <b>different</b> than
   * when IO operations are interrupted, which is supported by {@link UIHookIF}. The IO layer is supposed to generate
   * an InterruptedIOException, which gets thrown as an MBException, which the task catches as an ExecutionException, and
   * reports to the {@link SimpleTaskHandlerIF} as an Err.
   * <p/>
   * Cancel is for the case where task is simply unaware that cancel() was called on it's SwingWorker, while it was
   * running the executor's doInBackground method. This is needed since Java does not support
   * pre-empting running threads. They pretty much have to complete, then realize that they were cancelled, and then
   * the results have to be thrown away (and this has to be reported to the TaskHandler).
   * <p/>
   * This is different from Interruped, which can be reached only if the tread is in a state where InterruptedException
   * can be thrown on the SwingWorker (which might be the result of cancel(true) being called on it). The SwingWorker
   * will be interruptible only under a few circumstances (waiting, etc). So Cancel trumps Interrupted.
   */
  Cancelled,
  /** after task is Stopped, if container is not in ONLINE mode. TaskExecutorIF was not run. */
  NotOnline
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public abstract Type getType();

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// UIHookIF support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private SwingUIHookAdapter _uihook = new SwingUIHookAdapter();
public SwingUIHookAdapter getUIHook() {
  return _uihook;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// autostop support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** this holds all the {@link AutoShutdownSignals} types that this task will respond to */
protected AutoShutdownSignals[] _autoShutdownRay = {AutoShutdownSignals.AppShutdown};
protected boolean _shutdownCalled = false;

public AutoShutdownSignals[] getAutoShutdownPolicy() {
  return _autoShutdownRay;
}

public void setAutoShutdownPolicy(AutoShutdownSignals... policy) {
  Validate.notNull(policy, "policy can not be null");
  _autoShutdownRay = policy;
}

public boolean isShutdown() {
  return _shutdownCalled;
}

public void shutdown() {
  _shutdownCalled = true;
  clearAllStatusListeners();

  // sublclass performs shutdown stuff...
  doShutdown();

  // clear out the swingworker reference with uihook and unregister all listeners.
  getUIHook().clearAllStatusListeners();
  getUIHook().setSwingWorker(null);
}

/** returns true if this task has an autostop policy that matches the param */
public boolean hasAutoShutdownPolicy(AutoShutdownSignals policy) throws IllegalArgumentException {
  Validate.notNull(policy, "policy can not be null");

  for (AutoShutdownSignals p : _autoShutdownRay) {
    if (p.equals(policy)) return true;
  }

  return false;
}

/**
 * subclass should implement this method as is appropriate for them. this is called when the task is stopped,
 * based on the {@link AutoShutdownSignals} policy
 */
public abstract void doShutdown();

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// enclosing container support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
protected TaskManager _taskManager;

protected TaskManager getTaskManager() {
  return _taskManager;
}

protected void setTaskManager(TaskManager container) {
  _taskManager = container;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// property change support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
protected Lock _boundPropertiesLock = new ReentrantLock();
protected PropertyChangeSupport _boundProperties = new PropertyChangeSupport(this);
protected String _status;
protected State _state;

public String getStatus() {
  return _status;
}

public Object getState() {
  return _state;
}

protected PropertyChangeSupport getBoundProperties() {
  return _boundProperties;
}

/**
 * enumeration that holds list of properties that you can attach property change listeners for, right now there's only
 * one, but you can add more in the future.
 */
public enum PropertyNames {
  Status
}

public void setStatus(String s) {
  if (s == null) {
    // dont fire anything...
  }
  else {
    _boundProperties.firePropertyChange(PropertyNames.Status.toString(),
                                        null,
                                        s
    );
  }

  _status = s;
}

public void setState(State stateValue) {
  _state = stateValue;
}

/**
 * PropertyChangeEvent.getNewValue() contains the status message. Here's an example:
 * <pre>
 * public void propertyChange(PropertyChangeEvent evt) {
 *   _setStatus(evt.getNewValue().toString());
 * }
 * </pre>
 */
public void addStatusListener(PropertyChangeListener l) {
  Validate.notNull(l, "PropertyChangeListener for Status can not be null");
  try {
    _boundPropertiesLock.lock();

    _boundProperties.addPropertyChangeListener(PropertyNames.Status.toString(), l);
  }
  finally {
    _boundPropertiesLock.unlock();
  }
}

public void clearAllStatusListeners() {

  try {
    _boundPropertiesLock.lock();

    PropertyChangeListener[] list = _boundProperties.getPropertyChangeListeners(PropertyNames.Status.toString());
    for (PropertyChangeListener l : list) {
      _boundProperties.removePropertyChangeListener(PropertyNames.Status.toString(), l);
    }

  }
  finally {
    _boundPropertiesLock.unlock();
  }

}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// access to underlying SwingWorker (to get liveness, other state info, and to cancel execution)
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public abstract SwingWorker getSwingWorker();

public abstract void execute() throws TaskException;

/**
 * This method causes the cancel() method to be called on the underlying SwingWorker object (and thread).
 * The implementation of the TaskExecutorIF needs to check {@link javax.swing.SwingWorker#isCancelled()} in
 * order for this to work. The InterruptedException will be thrown when this method is called, if need
 * be.
 */
public void cancel() {
  Future future = getSwingWorker();
  if (future != null) future.cancel(true);
}

/**
 * makes sure that an existing SwingWorker is not already in flight. also if cleared for execution (SwingWorker
 * creation).
 * <p/>
 * the cancel flag is cleared on the {@link SwingUIHookAdapter#resetCancelFlag()}. if a task was cancelled previously,
 * then that cancellation is not longer active, since the {@link #getUIHook()} is about to be bound to a new
 * execution environment (thread/swingworker).
 *
 * @throws TaskException {@link TaskException.Type#ExecutionAttemptAfterTaskIsShutdown}
 */
protected void _assertNoSwingWorkerCurrentlyActive() throws TaskException {
  Future worker = getSwingWorker();

  if (worker == null || worker.isDone() || worker.isCancelled()) {
    getUIHook().resetCancelFlag();
  }
  else {
    throw TaskException.newConcurrentSwingWorkerCreationAttempt(
        "This task already has one underlying SwingWorker executing. It can't have more than one active concurrently.",
        this);
  }

}

/**
 * makes sure that {@link #shutdown()} has not been called yet.
 *
 * @throws TaskException {@link TaskException.Type#ExecutionAttemptAfterTaskIsShutdown}
 */
protected void _assertIsNotShutdown() throws TaskException {
  if (isShutdown()) {
    throw TaskException.executionAttemptAfterShutdown(
        "This task can not be executed once it has been shutdown.", this);
  }
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// name, description
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private String _name = "AbstractTask", _descr = "DescriptionSet";

public String getName() {return _name;}

public String getDescription() {return _descr;}

public void setName(String name) {_name = name;}

public void setDescription(String descr) {_descr = descr;}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// debug - pretty print
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public String formattedString() {
  StringBuilder sb = new StringBuilder();

  String name = getName() == null
                ? "n/a"
                : getName();
  String descr = getDescription() == null
                 ? "n/a"
                 : getDescription();
  String autostop = _autoShutdownRay == null
                    ? "n/a"
                    : getAutoStopPolicyString();
  String state = getState() == null
                 ? "n/a"
                 : getState().toString();
  String status = getStatus() == null
                  ? "n/a"
                  : getStatus();
  String tState = getSwingWorker() == null
                  ? "n/a"
                  : getSwingWorker().getState().toString();
  String tProgress = getSwingWorker() == null
                     ? "n/a"
                     : Integer.toString(getSwingWorker().getProgress());
  String isCancelled = getSwingWorker() == null
                       ? "n/a"
                       : Boolean.toString(getSwingWorker().isCancelled());

  sb.append("Name=").append(name).append(", DescriptionSet=").append(descr).append("\n");
  sb.append("AutoStop Policy=").append(autostop).append("\n");
  sb.append("State=").append(state).append(", Status=").append(status).append("\n");
  sb.append("IsCancelled?=").append(isCancelled).append("\n");
  sb.append("ThreadState=").append(tState).append(", ThreadStatus=").append(tProgress);

  return sb.toString();
}

private String getAutoStopPolicyString() {

  StringBuilder sb = new StringBuilder();

  sb.append("{");

  int i = 0;

  for (AutoShutdownSignals as : _autoShutdownRay) {
    i++;
    sb.append(as.toString());
    if (i != _autoShutdownRay.length) sb.append(", ");
  }

  sb.append("}");

  return sb.toString();

}

}//end class AbstractTask
