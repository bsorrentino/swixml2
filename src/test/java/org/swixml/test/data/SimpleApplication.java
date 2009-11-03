package org.swixml.test.data;

import org.jdesktop.application.Resource;
import org.swixml.jsr296.SwingApplication;
public class SimpleApplication extends SwingApplication {

	@Resource protected String entry;

	
	public final String getEntry() {
		return entry;
	}


	@Override
	protected void startup() {
		// TODO Auto-generated method stub
		
	}

}
