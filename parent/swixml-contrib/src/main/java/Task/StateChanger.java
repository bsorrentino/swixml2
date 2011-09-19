package Task;
import java.util.concurrent.*;

/**
 * StateChanger is a functor that orchestrates the business logic required to have
 * state changes take place in the Task API.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Oct 5, 2007, 3:55:38 PM
 */
public class StateChanger {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods for SimpleTaskHandlerIF and NetworkTaskHandlerIF
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static void signalInterrupted(AbstractTask task,
                                     SimpleTaskHandlerIF taskHandler,
                                     TaskExecutorIF exec,
                                     InterruptedException ex, long time) {
  task.setState(AbstractTask.State.Interrupted);
  taskHandler.interrupted(ex, task);
  task.setStatus(appendRetryMessage(exec.getInterruptedMessage(), exec));
}

public static void signalErr(AbstractTask task,
                             SimpleTaskHandlerIF taskHandler,
                             TaskExecutorIF exec,
                             ExecutionException ex,
                             long time) {
  task.setState(AbstractTask.State.Err);
  taskHandler.error(ex.getCause(), time, task);
  task.setStatus(appendRetryMessage(ex.getCause().getMessage(), exec));
}

public static void signalBeforeStart(SimpleTaskHandlerIF taskHandler, AbstractTask task) {
  taskHandler.beforeStart(task);
}

public static <T> void signalOK(AbstractTask task,
                                SimpleTaskHandlerIF<T> taskHandler,
                                TaskExecutorIF<T> exec,
                                T retVal,
                                long time) {
  task.setState(AbstractTask.State.OK);
  taskHandler.ok(retVal, time, task);
  task.setStatus(exec.getSuccessMessage());
}

public static void signalStart(AbstractTask task,
                               SimpleTaskHandlerIF taskHandler,
                               TaskExecutorIF exec) {
  task.setState(AbstractTask.State.Started);
  taskHandler.started(task);
  task.setStatus(exec.getStartMessage());
}

public static void signalStopped(AbstractTask task,
                                 SimpleTaskHandlerIF taskHandler,
                                 long time) {
  task.setState(AbstractTask.State.Stopped);
  taskHandler.stopped(time, task);
  // no status msg to set
}

public static void signalCancelled(AbstractTask task,
                                   SimpleTaskHandlerIF taskHandler,
                                   TaskExecutorIF exec,
                                   long time) {
  task.setState(AbstractTask.State.Cancelled);
  taskHandler.cancelled(time, task);
  task.setStatus(exec.getCancelledMessage());
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods for NetworkTaskHandlerIF only
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static void signalNotOnline(AbstractTask task,
                                   NetworkTaskHandlerIF taskHandler,
                                   TaskExecutorIF exec) {
  task.setState(AbstractTask.State.NotOnline);
  taskHandler.notOnline(task);
  task.setStatus(appendRetryMessage(exec.getNotOnlineMessage(), exec));
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// internal helper methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private static String appendRetryMessage(String msg, TaskExecutorIF exec) {
  return msg += " " + exec.getRetryMessage();
}

}//end class StateChanger
