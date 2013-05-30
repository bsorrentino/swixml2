package examples.combo;

import javax.swing.JDialog;

import org.swixml.jsr296.SWIXMLApplication;


public class ComboExample extends SWIXMLApplication {


	public static void main(String args []) {
		SWIXMLApplication.launch(ComboExample.class, args);
	}
	
	
	@Override
	protected void startup() {

		try {
			
			JDialog dialog = render( new ComboDialog() ); 
			
			show( dialog );

			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}
