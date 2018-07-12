package examples.text;

import javax.swing.JDialog;

import org.swixml.ApplicationPropertiesEnum;
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
		
	    ApplicationPropertiesEnum.IGNORE_RESOURCES_PREFIX.set(true);
	    ApplicationPropertiesEnum.AUTO_INJECTFIELD.set(true);

		SWIXMLApplication.launch(TextExample.class, args);
	}

}
