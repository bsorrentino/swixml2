package Task.Support.EnhancedListeners;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.swixml.contrib.gmap.Validate;

/**
 * AutoPruningListenerManager<T>, where ListenerType is the type of listener that this manager handles. This
 * class takes care of managing the {@link EnhancedListener}s that can be associated with this manager:
 * <ol>
 * <li>listeners can be added or removed.
 * <li>soft listeners are automatically pruned when {@link #pruneSoftListeners()} is called.
 * <li>events can be fired to all the listeners managed by this class.
 * </ol>
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Mar 7, 2008, 4:00:16 PM
 */
public class EnhancedListenerManager<ListenerType extends EnhancedListener> {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
protected CopyOnWriteArrayList<ListenerType> _listeners = new CopyOnWriteArrayList<ListenerType>();

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructor
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public EnhancedListenerManager() {}


/** actually performs the pruning of listeners */
public void pruneSoftListeners()
{
  EnhancedListener.pruneSoftListeners(_listeners);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public void addListener(ListenerType l) {
  Validate.notNull(l, l.getClass() + " can not be null");
  _listeners.add(l);
}

public void removeListener(ListenerType l) {
  Validate.notNull(l, l.getClass() + " can not be null");
  _listeners.remove(l);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// updating listeners - convenience methods to clean up syntax....
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public <EventType> void fireUpdateToListeners(EventType event,
                                              ExecutionDelegate<EventType, ListenerType> delegate)
{
  new EnhancedListenerUpdater<EventType, ListenerType>(_listeners, delegate, event);
}

public <EventType> void fireUpdatesToListeners(List<EventType> eventList,
                                               ExecutionDelegate<EventType, ListenerType> delegate)
{
  new EnhancedListenerUpdater<EventType, ListenerType>(_listeners, delegate, eventList);
}

}//end class EnhancedListenerManager
