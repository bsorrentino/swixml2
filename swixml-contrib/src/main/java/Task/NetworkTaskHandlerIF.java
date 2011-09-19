package Task;
/**
 * NetworkTaskHandlerIF is an extension of the lifecycle events that can be passed to a task handler
 * that deals with responding to network-enabled tasks.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @see SimpleTaskHandlerIF more information on the lifecycle methods
 * @since Oct 5, 2007, 9:59:30 AM
 */
public interface NetworkTaskHandlerIF<ReturnValueType> extends SimpleTaskHandlerIF<ReturnValueType> {

/**
 * this is called if the method is trying to run in the background and the container
 * is not online.
 * @param task
 */
public void notOnline(AbstractTask task);

}//end interface NetworkTaskHandlerIF
