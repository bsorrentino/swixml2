package org.swixml;

import javax.swing.JFrame;

import org.swixml.jsr296.SWIXMLApplication;

public class SimpleApplication extends SWIXMLApplication {

	public static void main(String args []) {
		SWIXMLApplication.launch(SimpleApplication.class, args);
	}
	
	
	@Override
	protected void startup() {

		try {
			JFrame frame = render( new ExplorerFrame() );

			show( frame );
			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}
