package examples.text;

import javax.swing.JDialog;

import org.swixml.jsr296.SWIXMLApplication;

public class TextExample extends SWIXMLApplication {
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

		SWIXMLApplication.launch(TextExample.class, args);
	}

}
