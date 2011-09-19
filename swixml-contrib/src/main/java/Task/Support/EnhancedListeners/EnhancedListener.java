package Task.Support.EnhancedListeners;

import java.util.ArrayList;
import java.util.List;

import org.swixml.contrib.gmap.Validate;

/**
 * EnhancedListener is a base class that makes it easy to create event listeners that have an "autoremove"
 * flag set on them. This can be used to auto-prune listeners, think "soft listener registration", like "soft
 * reference".
 * <p/>
 * Also, an enhanced listener can request to be executed in the EDT or a background thread. It works with
 * {@link EnhancedListenerUpdater} to make this happen. The updater respects a listener's requirement to run
 * in the EDT or NOT.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Feb 26, 2008, 2:00:33 PM
 */
public class EnhancedListener {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// run in edt policy support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private EDTPolicy _runInEDTPolicy;

public enum EDTPolicy {
  RunInEDT(true), RunInBackgroundThread(false);

  private boolean _mustRunInEDT;

  EDTPolicy(boolean mustRunInEDT) {
    _mustRunInEDT = mustRunInEDT;
  }

  public boolean getMustRunInEDT() {
    return _mustRunInEDT;
  }
}

public void setRunInEDTPolicy(EDTPolicy policy) throws IllegalArgumentException {
  Validate.notNull(policy, "policy can not be null");

  _runInEDTPolicy = policy;
}

public boolean mustRunInEDT() {
  return _runInEDTPolicy.getMustRunInEDT();
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// auto remove support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/**
 * if true, then this is a soft listener, which means that it should be unregistered automatically by the
 * component that this is registered with.
 */
private boolean _isSoftListener = false;

public boolean isSoftListener() {
  return _isSoftListener;
}

public void setSoftListener(boolean flag) {
  _isSoftListener = flag;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// soft listener pruning utility
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * a simple utility method that removes all the listeners that have {@link #isSoftListener()} set to true.
 *
 * @param list this list is mutated if any of the listeners have {@link # isSoftListener ()} set to true.
 */
public static final void pruneSoftListeners(List<? extends EnhancedListener> list) {
  ArrayList<EnhancedListener> _purgeList = new ArrayList<EnhancedListener>();

  for (EnhancedListener listener : list) {
    if (listener.isSoftListener()) {
      _purgeList.add(listener);
    }
  }

  list.removeAll(_purgeList);

}

}//end class EnhancedListener