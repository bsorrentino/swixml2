package Task;
/**
 * NetworkTaskHandler extends {@link SimpleTaskHandler}, and adds support for {@link #notOnline(AbstractTask)}.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Oct 5, 2007, 10:16:26 AM
 */
public class NetworkTaskHandler<ReturnValueType> extends SimpleTaskHandler<ReturnValueType> implements NetworkTaskHandlerIF<ReturnValueType> {

/** {@inheritDoc}
 * @param task*/
public void notOnline(AbstractTask task) {}

}//end class NetworkTaskHandler
