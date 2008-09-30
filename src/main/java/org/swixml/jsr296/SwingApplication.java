/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.BindingGroup;
import org.swixml.BoxFactory;
import org.swixml.SwingEngine;
import org.swixml.jsr.widgets.Label2;
import org.swixml.jsr.widgets.Table2;
import org.swixml.jsr.widgets.TextArea2;
import org.swixml.jsr.widgets.TextField2;
import org.swixml.jsr.widgets.Tree2;

/**
 *
 * @author Sorrentino
 */
public abstract class SwingApplication extends SingleFrameApplication  {
  protected final SwingEngine swix;
  protected final java.util.Map<String,SwingComponent> componentMap;
  
  private BindingGroup bindingGroup = new BindingGroup();

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

      swix = new SwingEngine( this );   
      componentMap = new java.util.HashMap<String,SwingComponent>(10);
      
      TextArea2.register(swix);
      TextField2.register(swix);
      Label2.register(swix);
      Tree2.register(swix);
      Table2.register(swix);
      BoxFactory.register(swix);

    }


    public void insertComponent( SwingComponent comp, Container c ) throws Exception {
        if( null==comp) throw new IllegalArgumentException("comp parameter is null!");
        String name = comp.getName();
        if( null==name) throw new IllegalArgumentException("component name is is null!");
     
        comp.setApplication(this);
        
        componentMap.put(name, comp);

        swix.insert( comp.getContentToRender(), c);
        
    }
    
    public final javax.swing.Action getAction( String name ) {
        
        javax.swing.Action result = getContext().getActionMap().get(name);
        if( null==result ) {
            for( SwingComponent sc : componentMap.values() ) {
                result = getContext().getActionMap(sc.getClass(), sc).get(name);        

                if( null!=result ) break;    
            }
        }
        return result;
    }

    public final void setTaskChangeListener( Task<?,?> t ) {
        t.addPropertyChangeListener( taskChangeListener );
    }
    
    protected void progressMessage( final String message ) {
        
    }
    
    public final SwingEngine getSwix() {
        return swix;
    }

    public BindingGroup getBindingGroup() {
        return bindingGroup;
    }

    @Override
    protected void shutdown() {
        componentMap.clear();
        bindingGroup.unbind();
        super.shutdown();
    }

	@Override
	protected final void show(JComponent c) {
		bindingGroup.bind();
		super.show(c);
	}

	@Override
	public final void show(JDialog c) {
		bindingGroup.bind();
		super.show(c);
	}

	@Override
	public final void show(JFrame c) {
		bindingGroup.bind();
		super.show(c);
	}

}
