package examples.combo;

import javax.swing.JDialog;

import org.swixml.jsr296.SwingApplication;


public class ComboExample extends SwingApplication {


	public static void main(String args []) {
		SwingApplication.launch(ComboExample.class, args);
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
