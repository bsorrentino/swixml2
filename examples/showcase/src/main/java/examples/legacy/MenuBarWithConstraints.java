package examples.legacy;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.swixml.SwingEngine;


// $Id: MenuBarWithConstraints.java,v 1.1 2005/08/22 21:20:01 tichy Exp $
/**
 * Sample program to show a menubar with constraints attribute in it.
 * 
 */
public class MenuBarWithConstraints extends JFrame {

	public Action exitAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}		
	};
	
	/**
	 * @param args
	 * @throws Exception if something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		SwingEngine se = new SwingEngine( new MenuBarWithConstraints() );
		Container container = se.render("xml/menu-bar.xml");
		container.setVisible(true);
	}

}
