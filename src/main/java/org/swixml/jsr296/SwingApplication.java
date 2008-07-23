/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.awt.Container;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JTable;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.swixml.SwingEngine;
import org.swixml.jsr295.BeanRegistry;
import org.swixml.jsr.widgets.BBLabel;
import org.swixml.jsr.widgets.BBTextField;
import org.swixml.jsr.widgets.Table2;
import org.swixml.jsr.widgets.Tree2;

/**
 *
 * @author Sorrentino
 */
public abstract class SwingApplication extends Application  {
  protected final SwingEngine swix;
  protected final java.util.Map<String,SwingComponent> componentMap;
  
  private final BeanRegistry beanRegistry;
  
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
      beanRegistry = BeanRegistry.createInstance();
      
      beanRegistry.registerBean( "app", this);
      
      BBTextField.register(this);
      BBLabel.register(this);
      Tree2.register(this);
      Table2.register(this);

    }

    public final BeanRegistry getBeanRegistry() {
        return beanRegistry;
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

    public final void setTaskChangeListener( Task t ) {
        t.addPropertyChangeListener( taskChangeListener );
    }
    
    protected void progressMessage( final String message ) {
        
    }
    
    public final SwingEngine getSwix() {
    return swix;
    }

    @Override
    protected void shutdown() {
        componentMap.clear();
        beanRegistry.shutdown();
        super.shutdown();
    }

}
