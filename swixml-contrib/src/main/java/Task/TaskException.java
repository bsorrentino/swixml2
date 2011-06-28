package Task;

import org.swixml.contrib.gmap.Validate;

/**
 * TaskException is thrown when at attempt is made to create & execute more than one underlying SwingWorker thread.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Mar 4, 2008, 2:55:17 PM
 */
public class TaskException extends Throwable {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private Type _type;
private static AbstractTask _task;

/** enumeration of all the different types of TaskException that can be thrown */
public enum Type {
  ExecutionAttemptAfterTaskIsShutdown, ConcurrentSwingWorkerCreationAttempt
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructor
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public TaskException(Type t, String s) {
  super(s);
  _type = t;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// factory
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public static TaskException newConcurrentSwingWorkerCreationAttempt(String msg, AbstractTask task)
    throws IllegalArgumentException
{
  Validate.notNull(task, "task can not be null");

  _task = task;
  return new TaskException(Type.ConcurrentSwingWorkerCreationAttempt, msg);
}

@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public static TaskException executionAttemptAfterShutdown(String msg, AbstractTask task)
    throws IllegalArgumentException
{
  Validate.notNull(task, "task can not be null");

  _task = task;
  return new TaskException(Type.ExecutionAttemptAfterTaskIsShutdown, msg);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public Type getType() {
  return _type;
}

public AbstractTask getTask() {
  return _task;
}

public String getName() {
  return _task.getName();
}

public String toString() {
  StringBuilder sb = new StringBuilder();

  sb.
      append("TaskException, type=").append(_type.toString()).
      append(", name=").append(getName()).
      append(", msg=").append(getMessage()).
      append("\n, task=").append(getTask());

  return sb.toString();
}

}//end class TaskException
