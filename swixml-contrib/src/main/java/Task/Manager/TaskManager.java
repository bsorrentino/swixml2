package Task.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.swixml.contrib.gmap.Validate;

import Task.AbstractTask;
import Task.AutoShutdownSignals;
import Task.TaskException;
import Task.Support.EnhancedListeners.EnhancedListenerManager;
import Task.Support.EnhancedListeners.ExecutionDelegate;

/**
 * TaskManager is a class that makes it easier for tasks to be created and a history of running
 * and dead tasks maintained in an application.
 * <p/>
 * All the tasks stopped and purged, as is determined by their {@link AutoShutdownSignals}
 * policy (that's defined in the task itself). You have to call {@link #autoShutdownOn(AutoShutdownSignals)}
 * and pass it the appropriate
 * {@link AutoShutdownSignals} enumeration to perform this auto shutdown... you can extend the enumeration
 * to add different signals that are more appropriate to your application. If you want to kill all running tasks
 * then issue a {@link #shutdownAll()}.
 * <p/>
 * Tasks are not automatically started. This has to be done explicitly by you/your app.
 * <p/>
 * Regardless of whether a task has been auto stopped (by a call to {@link #autoShutdownOn(AutoShutdownSignals)} or
 * manually stopped, the list of tasks is pruned by this manager, it's checked for tasks that have not been stopped,
 * and these are NOT removed!
 * <p/>
 * When the task list changes, {@link TaskListChangeEvent}s are fired to any registered
 * {@link TaskListChangeListener}s. If you register a soft-listener, then it will be pruned/purged when a
 * {@link #shutdownAll()} is invoked.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Jun 29, 2007, 1:00:34 PM
 */
public class TaskManager extends EnhancedListenerManager<TaskListChangeListener> {
private static Logger LOG = Logger.getLogger(TaskManager.class.getName());

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private CopyOnWriteArrayList<AbstractTask> _taskList = new CopyOnWriteArrayList<AbstractTask>();

/** holds flag that determines if SwingNetworkTasks should run */
private boolean _isOnline = true;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constuctor
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** default constructor */
public TaskManager() {}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// online/offline methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public void offline() {
  _isOnline = false;
}

public void online() {
  _isOnline = true;
}

public boolean isOnline() {
  return _isOnline;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/**
 * stops ALL tasks that are registered with this manager. this includes {@link AutoShutdownSignals#Daemon} tasks
 * as well.
 */
public void shutdownAll() {
  for (AbstractTask task : _taskList) {
    task.shutdown();
    _fireTaskListChange(new TaskListChangeEvent(TaskListChangeEvent.Type.Shutdown, task));
  }

  _removeStoppedTasksFromList();

  // remove all soft TaskListChangeListeners from the TaskManager...
  pruneSoftListeners();
}

/**
 * stops any registered tasks that have an {@link AutoShutdownSignals} policy that matches what's in shutdown.
 *
 * @param stopOnThis match all the registered tasks's autostop policy with this
 */
public void autoShutdownOn(AutoShutdownSignals stopOnThis) {

  for (AbstractTask task : _taskList) {
    if (task.hasAutoShutdownPolicy(stopOnThis)) {
      task.shutdown();
      _fireTaskListChange(new TaskListChangeEvent(TaskListChangeEvent.Type.Shutdown, task));
    }
  }

  _removeStoppedTasksFromList();

}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// helpers to create tasks
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * Registers the given task with the container.
 *
 * @param task task to register with the container
 */
public void registerTask(AbstractTask task) throws IllegalArgumentException {
  Validate.notNull(task, "TaskRoot can not be null");

  _taskList.add(task);
  _fireTaskListChange(new TaskListChangeEvent(TaskListChangeEvent.Type.Register, task));
}

/** Unregisters the task, whether it's a daemon task or not. */
public void unregisterTask(AbstractTask task) throws IllegalArgumentException {
  Validate.notNull(task, "TaskRoot can not be null");

  _taskList.remove(task);
  _fireTaskListChange(new TaskListChangeEvent(TaskListChangeEvent.Type.Unregister, task));
}

/** Clears out all the tasks that have been stopped */
private void _removeStoppedTasksFromList() {
  ArrayList<AbstractTask> isStoppedList = new ArrayList<AbstractTask>();

  for (AbstractTask task : _taskList) {
    if (task.isShutdown()) {
      isStoppedList.add(task);
      _fireTaskListChange(new TaskListChangeEvent(TaskListChangeEvent.Type.Unregister, task));
    }
  }

  _taskList.removeAll(isStoppedList);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// reporting support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** returns a copy of the task list */
public List<AbstractTask> getCopyOfList() {
  return new ArrayList<AbstractTask>(_taskList);
}

/**
 * This method ensures that the state change events are fired on the EDT or not, depending on the listener
 * preference. This method is called after the container's state change transition is complete.
 */
private void _fireTaskListChange(final TaskListChangeEvent evt) {

  fireUpdateToListeners(
      evt,
      new ExecutionDelegate<TaskListChangeEvent, TaskListChangeListener>() {
        public void doFireEvent(TaskListChangeListener listener, TaskListChangeEvent event) {
          listener.taskListChanged(event);
        }
      }
  );

}

public void writeExceptionToLog(Level warning, String s, TaskException e) {
  LOG.log(warning, s, e);
}

}//end class TaskManager
