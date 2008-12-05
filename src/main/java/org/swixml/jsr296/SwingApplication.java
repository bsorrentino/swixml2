/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.swixml.SwingEngine;

/**
 *
 * @author Sorrentino
 */
public abstract class SwingApplication extends SingleFrameApplication {

	private PropertyChangeListener taskChangeListener = new PropertyChangeListener() {
	
	        public void propertyChange(PropertyChangeEvent e) {
		    String propertyName = e.getPropertyName();
		    if ("message".equals(propertyName)) {
	            Object value = e.getNewValue();
	            if( null!=value )
	                progressMessage( e.getNewValue().toString() );
		    }
	    }
	};
  
	protected SwingApplication() {
	}
	
    public final void setTaskChangeListener( Task<?,?> t ) {
        t.addPropertyChangeListener( taskChangeListener );
    }
    
    protected void progressMessage( final String message ) {
        
    }
        
    public final <T extends Container> T render( T container, String resource) throws Exception {
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

		getContext().getResourceMap().injectFields(container);

		return engine.render(resource);
	}
    
}
