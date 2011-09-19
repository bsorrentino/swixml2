package examples.explorer;

import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.swixml.jsr296.SwingApplication;

public class LoginExample extends SwingApplication {

	@Override
	protected void startup() {
		
		try {
			LoginDialog dialog = super.render(new LoginDialog(), "examples/explorer/LoginDialog.xml");

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
