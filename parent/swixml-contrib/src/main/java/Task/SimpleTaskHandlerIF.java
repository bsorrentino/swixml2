package Task;

/**
 * TaskHandlerIF is an interface that encapsulates the various lifecyle stages that a task
 * will go through. This allows task writers to add event handling code as a task progresses through
 * various stages.
 * <p/>
 * Here's a quick rundown of the various paths that can be taken:
 * <ol>
 * <li>beforeStart -> started -> stopped -> ok
 * <li>beforeStart -> started -> stopped -> error
 * <li>beforeStart -> started -> stopped -> interrupted
 * <li>beforeStart -> started -> stopped -> cancelled
 * </ol>
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Oct 5, 2007, 9:59:16 AM
 */
public interface SimpleTaskHandlerIF<ReturnValueType> {

/**
 * this method is called before the background thread is started. good place to do
 * prep work if any needs to be done.
 * this may not run in the EDT.
 */
public void beforeStart(AbstractTask task);

/**
 * this is called after the task is started, it's not running in the background at this point,
 * but is just about to. all the states have been setup (in the task) and updates sent out.
 * this may not run in the EDT.
 */
public void started(AbstractTask task);

/**
 * this is called after the task has ended normally (not interrupted). ok or error may be called
 * after this.
 * this runs in the EDT.
 */
public void stopped(long time, AbstractTask task);

/**
 * this is called after the task has been interrupted. ok or error may not be called after this.
 * this is caused by underlying IOException, InterruptedIOException, or InterruptedException.
 * this runs in the EDT.
 *
 * @param e this holds the underlying exception that holds more information on the
 */
public void interrupted(Throwable e, AbstractTask task);

/**
 * this is called after stopped(). it signifies successful task completion.
 * this runs in the EDT.
 *
 * @param value this is optional. the task may want to pass an object or objects to the task
 */
public void ok(ReturnValueType value, long time, AbstractTask task);

/**
 * this is called after stopped(). it signifies failure of task execution.
 * this runs in the EDT.
 *
 * @param e this is used to pass the exception that caused the task to stop to be reported to the
 */
public void error(Throwable e, long time, AbstractTask task);

/**
 * This is called after started(). it signifies that the task was cancelled by cancel() being called
 * on it's SwingWorker thread. This is not the same as InterruptedIOException from the IO layer,
 * which results in an Err.
 * Cancel trumps: {@link #stopped(long, AbstractTask)}, {@link #error(Throwable,long, AbstractTask)},
 * {@link #ok}, and {@link #interrupted(Throwable, AbstractTask)}.
 * <p/>
 * In your handler implementation, just throw the results away, and assume everything has stopped
 * and terminated.
 * this runs in the EDT.
 */
public void cancelled(long time, AbstractTask task);

/**
 * This method is called on the task handler when {@link AbstractTask#shutdown()} is called. It signifies
 * that the task is going to stop.
 */
public void shutdownCalled(AbstractTask task);

}//end interface SimpleTaskHandlerIF
