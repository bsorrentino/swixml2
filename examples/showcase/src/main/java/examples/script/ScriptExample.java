package examples.script;

import javax.swing.JDialog;

import org.swixml.jsr296.SwingApplication;

public class ScriptExample extends SwingApplication {
	@Override
	protected void startup() {
		
		try {

			JDialog dialog = render( new ScriptDialog() ); 
			
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

		SwingApplication.launch(ScriptExample.class, args);
	}

}
