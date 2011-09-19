package Task.ProgressMonitor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import java.util.concurrent.Future;

import org.swixml.contrib.gmap.Validate;

import Task.AbstractTask;
import Task.Support.CoreSupport.BitRegister;
import Task.Support.GUISupport.ThreadUtils;

/**
 * SwingUIHookAdapter is an EDT friendly adapter that uses the {@link UIHookIF} facility of the
 * networking layer to provide feedback to the UI, and get cancel-signal from the UI as well.
 * <p/>
 * This class can also function as a hybrid adapter - The whole idea being that you can cancel a task by
 * invoking cancel() on
 * this object, <b>OR</b> the provided SwingWorker/Future can be cancelled as well, and this will
 * interrupt the underlying IO, and any task that is checking {@link #isCancelled}. Note that when
 * used in this manner, resetting the cancel flag only resets the flag controlled by this class and
 * NOT the isCancelled method on the SwingWorker/Future.
 * <p/>
 * The following abstract methods that you have to implement to tie this into your UI: <ol>
 * <li>{@link #updateRecieveStatusInUI(int,int)}
 * <li>{@link #updateSendStatusInUI(int,int)}
 * <li>{@link #closeInUI()} </ol>
 * <p/>
 * In your GUI, if you want to interrupt the IO operation of the underlying HTTP client, then just
 * call {@link #cancel}. This will interrupt the underlying IO stream. Please note that the cancel
 * flag will cause any subsequent IO operations with this SwingUIHookAdapter to cancel as well.
 * If you do not want this to happen, then call {@link #resetCancelFlag}. {@link AbstractTask} automatically
 * does this for you, so you don't have to do any of this yourself.
 * <p/>
 * This ProgressMonitor may be used by more than 1 objects. So be sure to {@link
 * #clearAllStatusListeners()} once you are done using this ProgressMonitor, so that there are no
 * ties to the UI once you are done using this monitor.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Oct 1, 2007, 2:57:56 PM
 */
public class SwingUIHookAdapter extends UIHookAdapter {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * underlying SwingWorker providing context of execution (thread)... used to determine cancellation,
 * may be null
 */
private Future<?> _swingWorker;

/** used to determine if SEND status updates should be delivered to the UI, true by default */
private boolean _fireSENDStatusUpdates = true;

/** used to determine if RECIEVE status updates should be delivered to the UI, true by default */
private boolean _fireRECIEVEStatusUpdates = true;

private boolean _savedfireSENDStatusUpdatesFlag = true;
private boolean _savedfireRECIEVEStatusUpdatesFlag = true;

/** property change listener support for status property */
protected PropertyChangeSupport _boundProperties = new PropertyChangeSupport(this);

public enum PropertyList {
  /** reports that data has been Sent, progress % and progress message are provided in the event */
  Send,
  /** reports that data has been Recieved, progress % and progress message are provided in the event */
  Recieve,
  /** reports that the underlying IO stream that was being monitored is closed */
  UnderlyingIOStreamClosed,
  /**
   * reports that the underlying IO stream was interrupted due to user cancellation, or SwingWorker cancellation
   * (programatically on a task)}
   */
  UnderlyingIOStreamInterruped
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructors
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * simply creates an adapter class that you have to override with the implementations of the
 * callback methods: <ol> <li>{@link #updateRecieveStatusInUI(int,int)} <li>{@link
 * #updateSendStatusInUI(int,int)} (int, int)} <li>{@link #closeInUI()} </ol>
 */
public SwingUIHookAdapter() {
  _uiHook = new UIHookIFImpl();
}

/**
 * The Future object is used to determine if the cancel has occured.
 *
 * @param future SwingWorker that is provided by the Task API
 */
public SwingUIHookAdapter(Future<?> future) throws IllegalArgumentException {
  this();

  Validate.notNull(future, "SwingWorker can not be null");

  _swingWorker = future;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// helper methods to get SEND and RECIEVE status notifications...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * this runs in the EDT. why? it's common for tasks to release the listeners in the background while
 * the ui hook is still being updated by the edt (an property change event update)... to avoid this
 * loss of status info, it's best to run this in the EDT.
 */
public void clearAllStatusListeners() {
  for (PropertyChangeListener listener : _boundProperties.getPropertyChangeListeners()) {
    //System.out.println("removing listeners...");
    _boundProperties.removePropertyChangeListener(listener);
  }
}

/**
 * The property change event that's fired to the listener has the following parts:
 * <pre>
 * public void propertyChange(PropertyChangeEvent evt) {
 *   String type = evt.getPropertyName();
 *   String progressStr = evt.getOldValue().toString();
 *   int progress = Integer.parseInt(progressStr);
 *   String msg = evt.getNewValue().toString();
 * }
 * </pre>
 *
 * @see PropertyList#Send
 */
public void addSendStatusListener(final PropertyChangeListener l) throws IllegalArgumentException {
  Validate.notNull(l, "PropertyChangeListener can not be null");

  _boundProperties.addPropertyChangeListener(PropertyList.Send.toString(), l);
}

/**
 * The property change event that's fired to the listener has the following parts:
 * <pre>
 * public void propertyChange(PropertyChangeEvent evt) {
 *   String type = evt.getPropertyName();
 *   String progressStr = evt.getOldValue().toString();
 *   int progress = Integer.parseInt(progressStr);
 *   String msg = evt.getNewValue().toString();
 * }
 * </pre>
 *
 * @see PropertyList#Recieve
 */
public void addRecieveStatusListener(final PropertyChangeListener l) throws IllegalArgumentException {
  Validate.notNull(l, "PropertyChangeListener can not be null");

  _boundProperties.addPropertyChangeListener(PropertyList.Recieve.toString(), l);
}

/**
 * The property change event that's fired to the listener has the following parts:
 * <pre>
 * public void propertyChange(PropertyChangeEvent evt) {
 *   String type = evt.getPropertyName();
 * }
 * </pre>
 *
 * @see PropertyList#UnderlyingIOStreamClosed
 * @see PropertyList#UnderlyingIOStreamInterruped
 */
public void addUnderlyingIOStreamInterruptedOrClosed(final PropertyChangeListener l) throws IllegalArgumentException {
  Validate.notNull(l, "PropertyChangeListener can not be null");

  _boundProperties.addPropertyChangeListener(PropertyList.UnderlyingIOStreamClosed.toString(), l);
  _boundProperties.addPropertyChangeListener(PropertyList.UnderlyingIOStreamInterruped.toString(), l);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// these 4 methods are delegated from the inner class ... you can override them in your subclass, if you don't
// want the default behavior of firing a property change when new SEND/RECV status updates are ready.
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/**
 * this is called in EDT, in your code. provides a SEND status update (progress bytes of total
 * bytes)
 */
public void updateSendStatusInUI(int progress, int total) {
  _boundProperties.firePropertyChange(
      PropertyList.Send.toString(),
      Integer.toString(ProgressMonitorUtils.getProgressPercent(progress, total)),
      ProgressMonitorUtils.generateProgressMessage(ProgressMonitorUtils.Type.Send,
                                                   getProgressMessage(),
                                                   progress, total)
  );
}

/**
 * this is called in EDT, in your code. provides a RECV status update (progress bytes of total
 * bytes)
 */
public void updateRecieveStatusInUI(int progress, int total) {
  _boundProperties.firePropertyChange(
      PropertyList.Recieve.toString(),
      Integer.toString(ProgressMonitorUtils.getProgressPercent(progress, total)),
      ProgressMonitorUtils.generateProgressMessage(ProgressMonitorUtils.Type.Recieve,
                                                   getProgressMessage(),
                                                   progress, total)
  );
}

/**
 * this is called in EDT, in your code. this is called if the underlying IO stream was closed for
 * some reason.
 */
public void closeInUI() {
  _boundProperties.firePropertyChange(PropertyList.UnderlyingIOStreamClosed.toString(),
                                      null,
                                      null);
}

/**
 * this is called in EDT, in your code. this is called if the underlying IO stream was interrupted for
 * some reason (user cancellation via {@link UIHookIF}, or SwingWorker (which belongs to a task)
 * cancellation for some reason).
 */

public void interruptedIOInUI() {
  _boundProperties.firePropertyChange(PropertyList.UnderlyingIOStreamInterruped.toString(),
                                      null,
                                      null);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods to enable/disable silent mode for SEND/RECV update notifications
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * you can put this UIHook in silent mode - will not update UI with status updates on SEND
 *
 * @param enabled false activates silent mode. default is true (non silent mode)
 */
public void enableSendStatusNotification(boolean enabled) {
  _fireSENDStatusUpdates = enabled;
}

/**
 * you can put this UIHook in silent mode - will not update UI with status updates on RECV
 *
 * @param enabled false activates silent mode. default is true (non silent mode)
 */
public void enableRecieveStatusNotification(boolean enabled) {
  _fireRECIEVEStatusUpdates = enabled;
}

/**
 * this saves the current state of {@link #enableRecieveStatusNotification(boolean)} and
 * {@link #enableSendStatusNotification(boolean)}
 */
public void saveNotificationSet() {
  _savedfireRECIEVEStatusUpdatesFlag = _fireRECIEVEStatusUpdates;
  _savedfireSENDStatusUpdatesFlag = _fireSENDStatusUpdates;
}

public void restoreNotificationSet() {
  _fireRECIEVEStatusUpdates = _savedfireRECIEVEStatusUpdatesFlag;
  _fireSENDStatusUpdates = _savedfireSENDStatusUpdatesFlag;
}

/**
 * see if silent mode is activated for SEND updates
 *
 * @return false means silent mode is active, true is default
 */
public boolean isSendStatusNotificationEnabled() {
  return _fireSENDStatusUpdates;
}

/**
 * see if silent mode is activated for RECV updates
 *
 * @return false means silent mode is active, true is default
 */
public boolean isRecieveStatusNotificationEnabled() {
  return _fireRECIEVEStatusUpdates;
}

/**
 * Returns a reference to the underlying SwingWorker providing the execution context/thread, if one
 * has been copyFrom
 *
 * @return may return null
 */
public Future<?> getSwingWorker() {
  return _swingWorker;
}

/**
 * sets a reference to the underlying SwingWorker providing the execution context/thread. this is
 * used to evaluate cancellation.
 */
public void setSwingWorker(Future<?> future) {
  _swingWorker = future;
}

/**
 * Call this method to interrupt the underlying IO stream. This will case SEND or RECV to abort with
 * InterruptedIOException. If you want to resume IO operations in the future, be sure to call {@link
 * #resetCancelFlag()}.
 */
public void cancel() {
  _uiHook.cancel();
}

/** This is used to check if {@link #cancel} was called already. */
public boolean isCancelled() {
  return _uiHook.isCancelled();
}

/**
 * If {@link #cancel} is called, then all subsequent IO operations will be cancelled, until this
 * method is called to reset the cancel condition.
 */
public void resetCancelFlag() {
  _uiHook.resetCancelFlag();
}

public void setProgressMessage(String msg) {
  _uiHook.setProgressMessage(msg);
}

public String getProgressMessage() {
  return _uiHook.getProgressMessage();
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// inner class...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class UIHookIFImpl implements UIHookIF {

  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  // data members
  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  private BitRegister cancelFlag = new BitRegister(false);
  private String progressMessage;
  private UpdateInfo _lastUpdateFired = new UpdateInfo();
  private UpdateInfo _currentUpdate = new UpdateInfo();
  private Runner _runner = new Runner();

  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  // UIHookIF impl
  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  public void setProgressMessage(String msg) {
    progressMessage = msg;
  }

  public String getProgressMessage() {
    return progressMessage;
  }

  public void updateSendStatus(final int progress, final int total) {
    if (isSendStatusNotificationEnabled()) {
      _fireUpdate(PropertyList.Send, progress, total);
    }
    else {
      //System.out.println("update send status suppressed");
    }
  }

  public void updateRecieveStatus(final int progress, final int total) {
    if (isRecieveStatusNotificationEnabled()) {
      _fireUpdate(PropertyList.Recieve, progress, total);
    }
    else {
      //System.out.println("update recieve status suppressed");
    }
  }

  private final void _fireUpdate(final PropertyList type, final int progress, final int total) {
    //System.out.println("_fireUpdate=" + type + ", progress=" + progress + ", total=" + total);

    _currentUpdate.set(type, progress, total);

    //System.out.println("_currentUpdate=" + _currentUpdate.toString());

    // filter redundant progress updates (this just bogs the EDT down)
    if (!_currentUpdate.hasChanged(_lastUpdateFired)) {
      //System.out.println("coalesing update... (in EDT?=" + ThreadUtils.isInEDT() + ")\n");
      return;
    }

    _lastUpdateFired.copyFrom(_currentUpdate);

    //System.out.println("firing update... last update fired=" + _lastUpdateFired);

    ThreadUtils.executeLaterInEDT(_runner.display(type, progress, total));
  }

  public void resetCancelFlag() {
    cancelFlag.clear();
  }

  public void cancel() {
    cancelFlag.set();
  }

  public boolean isCancelled() {
    if (getSwingWorker() == null) {
      return cancelFlag.isSet();
    }
    else {
      return (getSwingWorker().isCancelled() || cancelFlag.isSet());
    }
  }

  public void close() {
    ThreadUtils.executeLaterInEDT(new Runnable() {
      public void run() {
        SwingUIHookAdapter.this.closeInUI();
      }
    });
  }

  public void interrupedIO() {
    ThreadUtils.executeLaterInEDT(new Runnable() {
      public void run() {
        SwingUIHookAdapter.this.interruptedIOInUI();
      }
    });
  }

  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  // inner class
  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

  private class UpdateInfo {

    private PropertyList _type;
    private int _total;
    private int _percent;
    private int _progress;

    public UpdateInfo() {
      _type = PropertyList.Recieve;
      _total = -1;
      _percent = -1;
    }

    public UpdateInfo(PropertyList type, int progress, int total) {
      _type = type;
      _total = total;
      _progress = progress;
      _percent = ProgressMonitorUtils.getProgressPercent(progress, total);
    }

    public void set(PropertyList type, int progress, int total) {
      _type = type;
      _total = total;
      _progress = progress;
      _percent = ProgressMonitorUtils.getProgressPercent(progress, total);
    }

    public void copyFrom(UpdateInfo update) {
      _type = update._type;
      _total = update._total;
      _progress = update._progress;
      _percent = update._percent;
    }

    public boolean hasChanged(UpdateInfo rhs) {
      if (rhs == null) return true;

      if (_type != rhs._type ||
          _total != rhs._total ||
          _percent != rhs._percent
          )
      {
        return true;
      }

      return false;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.
          append("type=").append(_type).
          append(", total=").append(_total).
          append(", %=").append(_percent);

      return sb.toString();
    }

  }

  private class Runner implements Runnable {
    private Vector<UpdateInfo> updateList = new Vector<UpdateInfo>();

    public Runnable display(PropertyList type, int progress, int total) {
      updateList.add(new UpdateInfo(type, progress, total));
      return this;
    }//end init

    public void run() {
      while (!updateList.isEmpty()) {
        UpdateInfo info = updateList.remove(0);

        switch (info._type) {
          case Recieve:
            SwingUIHookAdapter.this.updateRecieveStatusInUI(info._progress, info._total);
            break;
          case Send:
            SwingUIHookAdapter.this.updateSendStatusInUI(info._progress, info._total);
            break;
        }
      }
    }//end run

  }


}

}//end class SwingUIHookAdapter