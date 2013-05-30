package examples.dialog;

import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.swixml.jsr296.SWIXMLApplication;

public class LoginExample extends SWIXMLApplication {

	@Override
	protected void startup() {
		
		try {
			LoginDialog dialog = super.render(new LoginDialog());

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

		Application.launch(LoginExample.class, args);

	}

}
