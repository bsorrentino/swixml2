package examples.button;

import javax.swing.JDialog;

import org.jdesktop.application.ResourceMap;
import org.swixml.SwingTagLibrary;
import org.swixml.jsr296.SWIXMLApplication;

public class ButtonExample extends SWIXMLApplication {
	@Override
	protected void startup() {
		
		SwingTagLibrary.getInstance().registerTag("toggleButton", JToggleButtonEx.class);
		ResourceMap rMap = getContext().getResourceManager().getResourceMap(ButtonDialog.class);
		
		String tbText = rMap.getString("tb.text");
		
		System.out.println( "==> " + tbText );
		
		try {
			
			JDialog dialog = render( new ButtonDialog() ); 
			
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

		SWIXMLApplication.launch(ButtonExample.class, args);
	}

}
