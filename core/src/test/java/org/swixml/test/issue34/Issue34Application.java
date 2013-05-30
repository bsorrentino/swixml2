package org.swixml.test.issue34;

import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.swixml.jsr296.SWIXMLApplication;

public class Issue34Application extends SWIXMLApplication {

	@Override
	protected void startup() {
		
		try {
			Issue34Dialog dialog = super.render(new Issue34Dialog(), "issue34/Issue34Dialog.xml");

			super.show(dialog);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "error on startup " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			
			// Exit to application
			//exit();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Application.launch(Issue34Application.class, args);

	}

}
