package org.swixml;

import javax.swing.JFrame;

import org.swixml.jsr296.SwingApplication;

public class SimpleApplication extends SwingApplication {

	public static void main(String args []) {
		SwingApplication.launch(SimpleApplication.class, args);
	}
	
	
	@Override
	protected void startup() {

		try {
			JFrame frame = render( new BindingExamplesFrame() );

			show( frame );
			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}
