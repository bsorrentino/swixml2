package Task.Support.GUISupport;

import javax.swing.*;

/**
 * ThreadUtils contains functions to help you ensure that code runs on the EDT
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Apr 26, 2007, 6:35:49 PM
 */
public class ThreadUtils {

public final static void assertInEDT() throws IllegalStateException {
  if (!SwingUtilities.isEventDispatchThread()) {
    throw new IllegalStateException("This code must run in the EDT");
  }
}

public final static boolean isInEDT() {
  return SwingUtilities.isEventDispatchThread();
}

public final static void executeLaterInEDT(Runnable functor) {
  if (functor == null) return;

  if (isInEDT()) {
    functor.run();
  }
  else {
    SwingUtilities.invokeLater(functor);
  }
}

}//end class ThreadUtils
