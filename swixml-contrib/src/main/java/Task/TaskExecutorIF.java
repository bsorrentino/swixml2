package Task;
import Task.ProgressMonitor.*;

import java.util.concurrent.*;

/**
 * TaskExecutorIF is a simple interface that encapsulates the method needed by a functor
 * which executes in the background - not on the EDT thread.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since May 30, 2007, 1:35:53 PM
 */
public interface TaskExecutorIF<ReturnValueType> {

/**
 * This method is executed in the background thread. The actual execution is performed in a background
 * thread provided by {@link javax.swing.SwingWorker}. The swingWorker object can be queried to find out
 * if it's been cancelled or not.
 *
 * @param swingWorker useful to determine if the SwingWorker has been cancelled (interrupted or whatever)
 * @param hook
 */
public ReturnValueType doInBackground(Future<ReturnValueType> swingWorker, SwingUIHookAdapter hook) throws Exception;

public String getStartMessage();

public String getInterruptedMessage();

public String getCancelledMessage();

public String getSuccessMessage();

public String getRetryMessage();

public String getNotOnlineMessage();

}//end interface TaskExecutorIF
