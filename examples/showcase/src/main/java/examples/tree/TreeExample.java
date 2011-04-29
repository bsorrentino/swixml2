package examples.tree;


import javax.swing.JDialog;

import org.swixml.jsr296.SwingApplication;

public class TreeExample extends SwingApplication {

	
	@Override
	protected void startup() {
		try {
			
			JDialog dialog = render( new TreeDialog() ); 
			
			show( dialog );

			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}

	}

	public static void main(String args []) {
		SwingApplication.launch(TreeExample.class, args);
	}
	
}
