package Task.ProgressMonitor;

import java.beans.*;

/**
 * ProgressMonitorUtils allows you to easily work with the progress monitor classes, to generate messages,
 * parse information out of PropertyChangeEvents, etc.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Oct 12, 2007, 8:34:07 PM
 */
public class ProgressMonitorUtils {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// Task.ProgressMonitor helpers
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public enum Type {
  Send, Recieve
}

/**
 * Creates a status message for the progress, not formatted in HTML, just plain text.
 *
 * @return plain text string (no html formatting)
 */
public static final String generateProgressMessage(Type type, String msg, int progress, int total) {

  StringBuilder sb = new StringBuilder();

  // insert the msg
  if (msg != null) {
    sb.append(msg).append(" : ");
  }

  // insert the type (send/recv)
  switch (type) {
    case Recieve:
      sb.append("Downloading : ");
      break;
    case Send:
      sb.append("Uploading : ");
      break;
  }

  // try and insert the percentage
  if (total < 0 || progress < 0 || progress > total) {
    sb.append("please wait...");
  }
  else {
    if (total != 0) {
      sb.append(getProgressPercent(progress, total)).append(" % complete.");
    }
    else {
      sb.append("please wait...");
    }
  }

  return sb.toString();
}

public static final int getProgressPercent(int progress, int total) {
  try {
    return progress * 100 / total;
  }
  catch (Exception e){
    return 0;
  }

}

public static int parsePercentFrom(PropertyChangeEvent evt) {
  int percent = Integer.parseInt(evt.getOldValue().toString());
  return percent;
}

public static String parseMessageFrom(PropertyChangeEvent evt) {
  return evt.getNewValue().toString();
}

public static SwingUIHookAdapter.PropertyList parseTypeFrom(PropertyChangeEvent evt) {
  return SwingUIHookAdapter.PropertyList.valueOf(evt.getPropertyName());
}

public static String parseStatusMessageFrom(PropertyChangeEvent evt) {
  return evt.getNewValue().toString();
}

}//end class ProgressMonitorUtils
