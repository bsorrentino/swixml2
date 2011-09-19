package Task.Manager;

import org.swixml.contrib.gmap.Validate;

import Task.AbstractTask;

/**
 * TaskListChangeEvent encapsulates the change that's occured to the task list in {@link TaskManager}.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Feb 26, 2008, 2:10:35 PM
 */
public class TaskListChangeEvent {

private Type _type;
private AbstractTask _task;

/** different types of task list change events... */
public enum Type {
  Register, Unregister, Shutdown
}

public TaskListChangeEvent(Type type, AbstractTask task) {
  Validate.notNull(type, "type can not be null");
  Validate.notNull(task, "task can not be null");

  _type = type;
  _task = task;
}

public Type getType() {return _type;}
public AbstractTask getTask() {return _task;}

public String toString() {
  StringBuilder sb = new StringBuilder();

  sb
      .append("AuthTokenChangeEvent=\n")
      .append("\tType=").append(_type).append("\n")
      .append("\tTaskRoot=").append(_task).append("\n");

  return sb.toString();
}

}//end class TaskListChangeEvent
