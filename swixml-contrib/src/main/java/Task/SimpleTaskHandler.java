package Task;
/**
 * TaskHandler is a convenience class that you can extend in order to override
 * only the methods that you want to override for your task handler.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @see SimpleTaskHandlerIF more information  on the TaskHandlerIF
 * @since Oct 5, 2007, 10:12:06 AM
 */
public class SimpleTaskHandler<ReturnValueType> implements SimpleTaskHandlerIF<ReturnValueType> {

/** {@inheritDoc}
 * @param task*/
public void beforeStart(AbstractTask task) {}

/** {@inheritDoc}
 * @param task*/
public void started(AbstractTask task) {}

/** {@inheritDoc} */
public void stopped(long time, AbstractTask task) {}

/** {@inheritDoc} */
public void interrupted(Throwable e, AbstractTask task) {}

/** {@inheritDoc} */
public void ok(ReturnValueType value, long time, AbstractTask task) {}

/** {@inheritDoc} */
public void error(Throwable e, long time, AbstractTask task) {}

/** {@inheritDoc} */
public void cancelled(long time, AbstractTask task) {}

/** {@inheritDoc}
 * @param task*/
public void shutdownCalled(AbstractTask task) {}

}//end class SimpleTaskHandler
