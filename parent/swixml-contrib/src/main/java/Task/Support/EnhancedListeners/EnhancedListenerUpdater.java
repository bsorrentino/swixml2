package Task.Support.EnhancedListeners;

import Task.Support.GUISupport.*;

import java.util.*;

/**
 * EnhancedListenerUpdater<T,V> takes care of firing events to listeners in a way that's compatible with the listener's
 * preference for running in the EDT or background thread. Here's a description of what T and V are:
 * <ol>
 * <li>EventType - this is the event type, that must be fired to the listeners
 * <li>EnhancedListenerType - this is a subclass of {@link EnhancedListener} that the {@link ExecutionDelegate} will operate on
 * </ol>
 * The {@link ExecutionDelegate} is simply a callback that allows you to bind whatever action you want to happen
 * in order for the event to actually get fired.
 * <p/>
 * You would use this class over manually creating an even propagator that respects the EDT execution policy of a
 * listener ({@link EnhancedListener}).
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Feb 26, 2008, 4:53:41 PM
 */
class EnhancedListenerUpdater<EventType, EnhancedListenerType extends EnhancedListener> {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// functor constructors
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

EnhancedListenerUpdater(final List<EnhancedListenerType> listenerList,
                        final ExecutionDelegate<EventType, EnhancedListenerType> delegate,
                        final EventType event)
{
  ArrayList<EventType> eventlist = new ArrayList<EventType>();
  eventlist.add(event);
  _fireUpdate(listenerList, delegate, eventlist);
}

EnhancedListenerUpdater(final List<EnhancedListenerType> listenerList,
                        final ExecutionDelegate<EventType, EnhancedListenerType> delegate,
                        final List<EventType> event)
{
  _fireUpdate(listenerList, delegate, event);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// actual business logic
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

private void _fireUpdate(List<EnhancedListenerType> listenerList,
                         final ExecutionDelegate<EventType, EnhancedListenerType> delegate,
                         final List<EventType> eventList)
{
  final ArrayList<EnhancedListenerType> _edtList = new ArrayList<EnhancedListenerType>();
  final ArrayList<EnhancedListenerType> _nonEdtList = new ArrayList<EnhancedListenerType>();

  // separate the listeners out into EDT and non-EDT groups
  for (EnhancedListenerType listener : listenerList) {
    if (listener.mustRunInEDT()) {
      _edtList.add(listener);
    }
    else {
      _nonEdtList.add(listener);
    }
  }

  if (ThreadUtils.isInEDT()) {
    // if the current thread is the EDT, then fire all the events on this thread
    for (EnhancedListenerType listener : listenerList) {
      _doFireEvent(delegate, eventList, listener);
      //System.out.println("firing eventList in edt (inEDT=" + ThreadUtils.isInEDT() + "):" + eventList);
    }
  }
  else {
    // if the current thread is not the EDT, then fire nonEDT listeners on this bg thread
    for (EnhancedListenerType listener : _nonEdtList) {
      _doFireEvent(delegate, eventList, listener);
      //System.out.println("firing eventList in bg-thread (inEDT=" + ThreadUtils.isInEDT() + "):" + eventList);
    }

    // fire the EDT listeners on the EDT
    ThreadUtils.executeLaterInEDT(new Runnable() {
      public void run() {
        for (EnhancedListenerType listener : _edtList) {
          _doFireEvent(delegate, eventList, listener);
          //System.out.println("firing eventList in edt (inEDT=" + ThreadUtils.isInEDT() + "):" + eventList);
        }
      }
    });
  }
}

private void _doFireEvent(ExecutionDelegate<EventType, EnhancedListenerType> delegate,
                          List<EventType> eventList,
                          EnhancedListenerType listener)
{
  for (EventType event : eventList) delegate.doFireEvent(listener, event);
}

}//end class EnhancedListenerUpdater
