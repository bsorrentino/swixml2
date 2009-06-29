package examples.explorer;

import org.jdesktop.application.Application;
import org.swixml.jsr296.SwingApplication;

public class LoginExample extends SwingApplication {

	@Override
	protected void startup() {
		
		try {
			LoginDialog dialog = super.render(new LoginDialog(), "examples/explorer/loginDialog.xml");

			super.show(dialog);
			
		} catch (Exception e) {
			exit();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Application.launch(LoginExample.class, args);

	}

}
