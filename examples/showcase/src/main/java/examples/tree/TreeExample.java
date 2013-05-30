package examples.tree;


import javax.swing.JDialog;

import org.swixml.jsr296.SWIXMLApplication;

public class TreeExample extends SWIXMLApplication {

	
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
		SWIXMLApplication.launch(TreeExample.class, args);
	}
	
}
