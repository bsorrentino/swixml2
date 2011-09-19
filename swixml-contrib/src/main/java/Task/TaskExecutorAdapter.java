package Task;
/**
 * TaskExecutorAdapter is a helper class, that has empty implementations of all the methods in
 * TaskExecutorIF. Just override this class and provide whatever implementations are relevant to your
 * task.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Jun 22, 2007, 10:38:24 AM
 */
public abstract class TaskExecutorAdapter<ReturnValueType> implements TaskExecutorIF<ReturnValueType> {

public String getName() {
  return "TaskRoot";
}

public String getStartMessage() {
  return getName() + " started.";
}

public String getInterruptedMessage() {
  return getName() + " was interrupted.";
}

public String getCancelledMessage() {
  return getName() + " was cancelled.";
}

public String getSuccessMessage() {
  return getName() + " completed successfully.";
}

public String getRetryMessage() {
  return "Please try again.";
}

public String getNotOnlineMessage() {
  return "Application is not online, did not run " + getName() + ".";
}

}//end class TaskExecutorAdapter
