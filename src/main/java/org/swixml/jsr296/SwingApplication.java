/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.BindingGroup;
import org.swixml.BoxFactory;
import org.swixml.SwingEngine;
import org.swixml.jsr.widgets.JLabelEx;
import org.swixml.jsr.widgets.JTableEx;
import org.swixml.jsr.widgets.JTextAreaEx;
import org.swixml.jsr.widgets.JTextFieldEx;
import org.swixml.jsr.widgets.JTreeEx;

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
  
	public Action getComponentAction(String name) {
		return getContext().getActionMap().get(name);
	}

    public final void setTaskChangeListener( Task<?,?> t ) {
        t.addPropertyChangeListener( taskChangeListener );
    }
    
    protected void progressMessage( final String message ) {
        
    }
        
    public final <T extends Container> T render(String resource) throws Exception {
		return render( engine, resource);
	}

	@SuppressWarnings("unchecked")
	public static final <T extends Container> T render( SwingEngine swix, String resource ) throws Exception {
       
        Object o = swix.getClient();
        
        if( !(o instanceof SwingComponent) ) {
        	throw new IllegalArgumentException( "client object is no a SwingComponent ");
        }
        
        final SwingComponent client = (SwingComponent)o;
        
        final Container c = swix.render(resource);

        return (T)c;
    }
    

}
