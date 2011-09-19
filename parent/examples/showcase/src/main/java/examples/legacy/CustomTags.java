package examples.legacy;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.swixml.SwingEngine;

import com.toedter.calendar.JCalendar;


public class CustomTags extends JFrame {


  public CustomTags() throws Exception {
    SwingEngine swix = new SwingEngine(this);
    swix.getTaglib().registerTag("Calendar", JCalendar.class);
    swix.render("xml/customtags.xml").setVisible(true);
  }

  /**
   * Invoked when a window is in the process of being closed.
   * The close operation can be overridden at this point.
   */
  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }

  //
  //  Make the class bootable
  //
  public static void main(String[] args) throws Exception {
    new CustomTags();
  }
}
