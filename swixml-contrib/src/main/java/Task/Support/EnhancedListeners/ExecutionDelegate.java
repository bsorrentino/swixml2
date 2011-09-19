package Task.Support.EnhancedListeners;

/**
 * ExecutionDelegate
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Mar 11, 2008, 6:39:57 PM
 */
public interface ExecutionDelegate<EventType, EnhancedListenerType> {
  /**
   * this is a call back method. in your implementation, actually perform the tasks that you need in order to fire
   * the event on the listener.
   */
  public void doFireEvent(EnhancedListenerType listener, EventType event);
}
