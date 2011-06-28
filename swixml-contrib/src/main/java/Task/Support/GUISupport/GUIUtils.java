package Task.Support.GUISupport;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import org.swixml.contrib.gmap.Validate;

/**
 * GUIUtils is a utility class with frequently used functions to make writing Swing GUI apps easier
 *
 * @author Nazmul Idris
 * @since Jan 6, 2007, 7:29:25 PM
 */
public class GUIUtils {

private static Logger LOG = Logger.getLogger(GUIUtils.class.getName());

//no constructor
private GUIUtils() {}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// button and label HTML text generators
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * Converts the KeyEvent int to a String that can be displayed in a UI. Used to generate Alt + "?"
 * in labels. Swing components store the keyboard accelerators as ints (KeyEvent.VK_XXX).
 *
 * @see javax.swing.AbstractButton#setMnemonic(char)
 */
public static char mnemonicToString(int mnemonic) throws IllegalArgumentException {
  char character = (char) (mnemonic + ('a' - 'A'));
  if (character >= 'a' && character <= 'z') {
    return Character.toUpperCase(character);
  }
  else {
    throw new IllegalArgumentException("mnemonic is not between 'a' and 'z'");
  }
}

public static String formatForLabel(String msg) {
  String formattedMsg = encloseWithTag(
      applyCenterTag(applyFontColorTag(msg)),
      "html");
  return formattedMsg;
}

public static String makeLabelWithShortcut(String labelText, int keybAccel) {
  Validate.notEmpty(labelText, "label can not be empty or null");

  // generate the html label
  final StringBuilder sb = new StringBuilder();
  sb.append(labelText);

  if (keybAccel != 0) {
    sb.append("<br><br>(<i>").append("Alt+").append(mnemonicToString(keybAccel)).append("</i>)");
  }

  return encloseWithTag(applyFontColorTag(sb.toString()), "html");
}

public static String encloseWithTag(String msg, String tag) {
  StringBuilder sb = new StringBuilder();

  sb
      .append("<").append(tag).append(">")
      .append(msg)
      .append("</").append(tag).append(">");

  return sb.toString();
}

public static String encloseWithTag(String startTag, String msg, String endTag) {
  StringBuilder sb = new StringBuilder();

  sb
      .append(startTag)
      .append(msg)
      .append(endTag);

  return sb.toString();
}

public static String applyFontColorTag(String msg, Colors color) {
  StringBuilder sb = new StringBuilder();

  sb
      .append("<font color=").append(color.toHexString()).append(">")
      .append(msg)
      .append("</font>");

  return sb.toString();
}

public static String applyFontColorTag(String msg) {
  StringBuilder sb = new StringBuilder();

  sb
      .append("<font color=").append(Colors.DullBlue.toHexString()).append(">")
      .append(msg)
      .append("</font>");

  return sb.toString();
}

public static String applyCenterTag(String msg) {
  StringBuilder sb = new StringBuilder();

  sb
      .append("<center>").append(msg).append("</center>");

  return sb.toString();
}

public static String applyItalicTag(String msg) {
  StringBuilder sb = new StringBuilder();

  sb
      .append("<i>").append(msg).append("</i>");

  return sb.toString();
}

public static String applyBoldTag(String msg) {
  StringBuilder sb = new StringBuilder();

  sb
      .append("<b>").append(msg).append("</b>");

  return sb.toString();
}

public static String applyUnderlineTag(String msg) {
  StringBuilder sb = new StringBuilder();

  sb
      .append("<u>").append(msg).append("</u>");

  return sb.toString();
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// Tootip helpers
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * Return an HTML formatted tooltip string similar to what Office 2007 has. Here's a sample:
 * <pre>
 *   Paste (Ctrl+V)         <- Bold + Black
 *   Pastes the contents of <- Dark Gray & multi-line
 *   the Clipboard
 * </pre>
 */
public static String getFormattedTooltip(String header, String description) {
  StringBuilder sb = new StringBuilder();
  //todo: implement the Office2007 tooltip formatting
  return sb.toString();
}

//:::::::::::::::::::::::::::::::::::::::::::::::
// LNF stuff
//:::::::::::::::::::::::::::::::::::::::::::::::
/** uses UIDefault to replace a font property */
public static void setUIDefaultFontProperty(String fontProp, Font font) {
  UIDefaults defaults = UIManager.getLookAndFeelDefaults();
  defaults.put(fontProp, new FontUIResource(font));
}

/** uses UIDefault to replace a color property */
public static void setUIDefaultColorProperty(String colorProp, Color color) {
  UIDefaults defaults = UIManager.getLookAndFeelDefaults();
  defaults.put(colorProp, new ColorUIResource(color));
}

public final static void setSystemLookAndFeelBeforeUIStarted() {
  try {
    // Set System L&F
    UIManager.setLookAndFeel(
        UIManager.getSystemLookAndFeelClassName());
  }
  catch (Exception e) {
    // handle exception
    LOG.log(Level.WARNING, "Problem setting native L&F at startup", e);
  }
}


public final static void setNimbusLookAndFeelBeforeUIStarted() {
  try {
    // Set Nimbus LnF
    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
  }
  catch (Exception e) {
    // handle exception
    LOG.log(Level.WARNING, "Problem setting Mimbus L&F at startup", e);
  }
}


/** @see <a href="https://looks.dev.java.net/faq.html">Looks FAQ</a> */
/*
public final static void setJGoodiesLooksLookAndFeelBeforeUIStarted() {
  try {

    PlasticLookAndFeel.setPlasticTheme(new LightGray());
    //PlasticLookAndFeel.setPlasticTheme(new SkyBluer());

    // Set JGoodies LnF
    UIManager.setLookAndFeel(
        //new Plastic3DLookAndFeel()
        //new PlasticLookAndFeel()
        new PlasticXPLookAndFeel()
    );
  }
  catch (Exception e) {
    // handle exception
    LOG.log(Level.WARNING, "Problem setting Mimbus L&F at startup", e);
  }
}
*/

public final static void setSystemLookAndFeel(JFrame frame) {
  try {
    // Set System L&F
    UIManager.setLookAndFeel(
        UIManager.getSystemLookAndFeelClassName());
    SwingUtilities.updateComponentTreeUI(frame);
  }
  catch (Exception e) {
    // handle exception
    LOG.log(Level.WARNING, "Problem setting native L&F after startup", e);
  }
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// dynamic layout enable
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static void enableDynamicLayout() {
  try {
    Toolkit.getDefaultToolkit().setDynamicLayout(true);
  }
  catch (Exception e) {
    LOG.log(Level.WARNING, "Problem setting dynamic layout", e);
  }
}

//:::::::::::::::::::::::::::::::::::::::::::::::
// parameter validation for UIs
//:::::::::::::::::::::::::::::::::::::::::::::::
public final static boolean isEmptyOrNull(String msg) {
  if (msg == null) {
    return true;
  }
  if (msg.trim().equals("")) {
    return true;
  }
  return false;
}

//:::::::::::::::::::::::::::::::::::::::::::::::
// modal dialog display methods
//:::::::::::::::::::::::::::::::::::::::::::::::
public final static void showErrorDialog(Component parentComponent, String msg) {
  if (isEmptyOrNull(msg)) {
    return;
  }
  StringBuilder sb = new StringBuilder();
  sb.append("<html><font color=red>");
  sb.append(msg);
  sb.append("</font></html>");
  JOptionPane.showInternalMessageDialog(parentComponent, sb, "Exception", JOptionPane.WARNING_MESSAGE);
}

public final static void showErrorDialog(Component parentComponent, Throwable ex) {
  String exClassName, exToString;
  if (ex == null) {
    return;
  }
  else {
    exClassName = ex.getClass().getName();
    exToString = "";//ToStringBuilder.reflectionToString(ex, ToStringStyle.MULTI_LINE_STYLE);
  }

  JOptionPane.showInternalMessageDialog(parentComponent, exToString, exClassName, JOptionPane.WARNING_MESSAGE);
}

public final static void showErrorDialog(Component parentComponent, String msg, Throwable ex) {
  if (isEmptyOrNull(msg)) {
    return;
  }
  String exClassName, exToString;
  if (ex == null) {
    exClassName = "null";
    exToString = "";
  }
  else {
    exClassName = ex.getClass().getName();
    exToString = ""; // ToStringBuilder.reflectionToString(ex, ToStringStyle.MULTI_LINE_STYLE);
  }
  StringBuilder sb = new StringBuilder();
  sb.append("<html><font color=red>");
  sb.append(msg);
  sb.append("</font><br></html>");
  sb.append(exToString);
  JOptionPane.showInternalMessageDialog(parentComponent, sb, exToString, JOptionPane.WARNING_MESSAGE);
}

//:::::::::::::::::::::::::::::::::::::::::::::::
// frame utility methods
//:::::::::::::::::::::::::::::::::::::::::::::::
public final static void centerOnScreen(JFrame frame) {
  frame.setLocationRelativeTo(null);
}

public final static void setMinimumWindowSize(JFrame frame, int w, int h) {
  frame.setMinimumSize(new Dimension(w, h));
}

public final static void setMinimumWindowSize(JFrame frame, Dimension d) {
  frame.setMinimumSize(d);
}

public final static void setAppIcon(JFrame frame, String image) {
  try {
    frame.setIconImage(ImageUtils.loadBufferedImage(image, false, 1.0f));
  }
  catch (ClassNotFoundException e) {
    //do nothing
    LOG.log(Level.SEVERE, image + ".png - icon image for main frame not found in classpath", e);
  }
}

public final static void setAppIcon(JFrame frame, Image image) {
  frame.setIconImage(image);
}

//:::::::::::::::::::::::::::::::::::::::::::::::
// system tray functionality
// See for more details: http://java.sun.com/developer/technicalArticles/J2SE/Desktop/javase6/systemtray/
//:::::::::::::::::::::::::::::::::::::::::::::::
public static final boolean isSystemTrayAvailable() {
  return SystemTray.isSupported();
}

public static final void addToSystemTray(TrayIcon icon) {
  try {
    SystemTray tray = SystemTray.getSystemTray();
    tray.add(icon);
  }
  catch (Exception e) {
    LOG.log(Level.SEVERE, "problem adding tray icon to the system tray", e);
  }
}

/**
 * <b>Warning</b>: Don't call this method from a VM shutdown hook thread. it causes deadlock for
 * some reason?!
 */
public static final void removeFromSystemTray(TrayIcon icon) {
  try {
    SystemTray tray = SystemTray.getSystemTray();
    tray.remove(icon);
  }
  catch (Exception e) {
    LOG.log(Level.SEVERE, "problem adding tray icon to the system tray", e);
  }
}

/**
 * create a trayicon with the provided image
 *
 * @param iconName this is the name of an image file that will be loaded with ImageUtils (from the
 *                 classpath)
 *
 * @throws UnsupportedOperationException - if SystemTrays are not supported on this platform...
 */
public static final TrayIcon getSystemTrayIcon(String iconName) throws UnsupportedOperationException {
  BufferedImage img = null;

  try {
    img = ImageUtils.loadBufferedImage(iconName, false, 1.0f);
  }
  catch (ClassNotFoundException e) {
    LOG.log(Level.SEVERE, "Could not find image file:" + iconName, e);
    img = createFDImage();
  }

  return getSystemTrayIcon(img);
}

public static final TrayIcon getSystemTrayIcon(BufferedImage img) throws UnsupportedOperationException {
  TrayIcon trayIcon = new TrayIcon(img);
  trayIcon.setImageAutoSize(true);
  return trayIcon;
}

//Creates an icon-worthy Image from scratch.
protected static BufferedImage createFDImage() {
  //Create a 16x16 pixel image.
  BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);

  //Draw into it.
  Graphics g = bi.getGraphics();
  g.setColor(Color.BLACK);
  g.fillRect(0, 0, 15, 15);
  g.setColor(Color.RED);
  g.fillOval(5, 3, 6, 6);

  //Clean up.
  g.dispose();

  //Return it.
  return bi;
}

//-------------------------------------------------------------------------------------------------------------------
// Launch external browser
// for more info - http://java.sun.com/developer/technicalArticles/J2SE/Desktop/javase6/desktop_api/
//-------------------------------------------------------------------------------------------------------------------
public static void launchExternalBrowser(String url) {
  if (Desktop.isDesktopSupported()) {
    Desktop desktop = Desktop.getDesktop();

    if (desktop.isSupported(Desktop.Action.BROWSE)) {

      try {
        URI uri = new URI(url);
        desktop.browse(uri);

      }
      catch (Exception e) {
        LOG.log(Level.SEVERE, "encountered problem while trying to launch external browser", e);
      }

    }
    else {
      LOG.log(Level.SEVERE,
              "the underlying platform does not support the BROWSE Action on java.awt.Desktop, " +
              "can't launch external browser for URL=" + url);
    }

  }
  else {
    LOG.log(Level.SEVERE,
            "the underlying platform does not support java.awt.Desktop API, can't launch external browser for URL=" +
            url);
  }
}

//-------------------------------------------------------------------------------------------------------------------
// Registry prefs methods
//-------------------------------------------------------------------------------------------------------------------
public static void putWindowBoundsToPersistence(Class myclass, Rectangle bounds) {
  try {
    //save this data to the registry via preferences api
    Preferences prefs = Preferences.userNodeForPackage(myclass);

    Double width = bounds.getWidth(), height = bounds.getHeight(), x = bounds.getX(), y = bounds.getY();

    prefs.putInt("height", height.intValue());
    prefs.putInt("width", width.intValue());
    prefs.putInt("x", x.intValue());
    prefs.putInt("y", y.intValue());

    prefs.flush();
    //System.out.println("----SAVED PREFS----");
  }
  catch (Exception e) {
    LOG.log(Level.SEVERE, "problem occured while saving window locations to preferences", e);
  }

}

/**
 * the first time this method is called, and there are no prefs stored in the registry, it will
 * return null
 */
public static Rectangle getWindowBoundsFromPersistence(Class myclass) {
  //try and get the data from the registry via preferences api, else return null
  try {
    Preferences prefs = Preferences.userNodeForPackage(myclass);
    int height, width, x, y;

    height = prefs.getInt("height", -1);
    width = prefs.getInt("width", -1);
    x = prefs.getInt("x", -1);
    y = prefs.getInt("y", -1);

    if (x == -1 || y == -1) {
      //System.out.println("-----NO PREFS TO LOAD-----");
      return null;
    }

    //System.out.println("-----LOADED PREFS-----");
    return new Rectangle(x, y, width, height);

  }
  catch (Exception e) {
    LOG.log(Level.SEVERE, "problem occured while loading window locations from preferences", e);
    return null;
  }
}

}//end of class GUIUtils
