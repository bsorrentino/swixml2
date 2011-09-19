package Task;

/**
 * identifies the AutoShutdown policy for a task. Feel free to extend this class and add your own as you see fit
 * for your applications.
 */
public enum AutoShutdownSignals {
  /** stop a task before the app shuts down. this is the default autoshutdown policy for a task. */
  AppShutdown,
  /** long running task that should probably be around between user login sessions, etc. */
  Daemon,
  /** stop the task when a logout occurs */
  Logout

}
