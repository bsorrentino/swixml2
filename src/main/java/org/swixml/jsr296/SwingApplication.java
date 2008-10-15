/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.swixml.SwingEngine;

/**
 *
 * @author Sorrentino
 */
public abstract class SwingApplication extends SingleFrameApplication implements SwingComponent {

	private SwingEngine engine = new SwingEngine(this);
	
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
		engine.setClassLoader( getClass().getClassLoader() );
	}
	public Action getComponentAction(String name) {
		return getContext().getActionMap().get(name);
	}

    public final void setTaskChangeListener( Task<?,?> t ) {
        t.addPropertyChangeListener( taskChangeListener );
    }
    
    protected void progressMessage( final String message ) {
        
    }
        
    public final <T extends Container> T render( Class<T> resultClass, String resource) throws Exception {
		return render( resultClass, engine, resource);
	}
    
    protected final JFrame renderFrame( String resource ) throws Exception {
    	return render( JFrame.class, resource );
    }

    protected final JDialog renderDialog( String resource ) throws Exception {
    	return render( JDialog.class, resource );
    }
    
    
	@SuppressWarnings("unchecked")
	public static final <T extends Container> T render( final Class<T> resultClass, final SwingEngine swix, final String resource ) throws Exception {
       
        Object o = swix.getClient();
        
        if( !(o instanceof SwingComponent) ) {
        	throw new IllegalArgumentException( "client object is no a SwingComponent ");
        }
        
        //final SwingComponent client = (SwingComponent)o;
        
        final Container c = swix.render(resource);

        return (T)c;
    }
	
	
    

}
