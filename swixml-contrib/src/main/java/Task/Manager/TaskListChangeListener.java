package Task.Manager;

import Task.Support.EnhancedListeners.*;

/**
 * TaskListChangeListener is an adapter class that you have to extend in order to respond to the container's
 * task list changes, {@link TaskManager}.
 * <p/>
 * If you attach a "soft listener", then it will be purged under the following conditions:
 * {@link TaskManager#shutdownAll()}.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Feb 26, 2008, 1:57:33 PM
 */
public abstract class TaskListChangeListener extends EnhancedListener {

public TaskListChangeListener(boolean isSoftListener) {
  setSoftListener(isSoftListener);
}

public TaskListChangeListener(boolean isSoftListener, EDTPolicy mustRunInEDT) {
  setSoftListener(isSoftListener);
  setRunInEDTPolicy(mustRunInEDT);
}

/** subclass must override this method */
public abstract void taskListChanged(TaskListChangeEvent evt);

}//end class TaskListChangeListener
