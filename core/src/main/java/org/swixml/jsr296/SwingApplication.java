/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.Reader;

import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.swixml.SwingEngine;
import static org.swixml.LogUtil.logger;

/**
 *
 * @author Sorrentino
 */
public abstract class SwingApplication extends SingleFrameApplication {

        
/*
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
*/
	protected SwingApplication() {
	}
	
/*	
    public final void setTaskChangeListener( Task<?,?> t ) {
        t.addPropertyChangeListener( taskChangeListener );
    }
    
    protected void progressMessage( final String message ) {
        
    }
*/   
    public final <T extends Container> T render( T container ) throws Exception {
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

        if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);

        String resource = container.getClass().getName().replace('.', '/').concat(".xml");
        
        logger.info( String.format("render resource [%s]", resource));
        
		return engine.render(resource);
	}
	
    public final <T extends Container> T render( T container, String resource) throws Exception {
    	if( null==resource ) throw new IllegalArgumentException( "resource is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

                if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);

		return engine.render(resource);
	}

    public final <T extends Container> T render( T container, Reader reader) throws Exception {
    	if( null==reader ) throw new IllegalArgumentException( "reader is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

                if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);

		return engine.render(reader);
	}

    public final <T extends Container> T render( T container, File xmlFile) throws Exception {
    	if( null==xmlFile ) throw new IllegalArgumentException( "xmlFile is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

                if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);

		return engine.render(xmlFile);
	}

    public final <T extends Container> T render( T container, java.net.URL url) throws Exception {
    	if( null==url ) throw new IllegalArgumentException( "url is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

                if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);

		return engine.render(url);
	}
    
}
