package examples.text;

import javax.swing.JDialog;

import org.swixml.jsr296.SwingApplication;

public class TextExample extends SwingApplication {
	@Override
	protected void startup() {
		
		try {
			
			JDialog dialog = render( new TextDialog() ); 
			
			show( dialog );

			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.setProperty( IGNORE_RESOURCES_PREFIX, "true");
		System.setProperty( AUTO_INJECTFIELD, "true");

		SwingApplication.launch(TextExample.class, args);
	}

}
