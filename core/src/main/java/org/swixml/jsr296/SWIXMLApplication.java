/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import static org.swixml.LogUtil.logger;

import java.awt.Container;
import java.io.File;
import java.io.Reader;

import org.jdesktop.application.SingleFrameApplication;
import org.swixml.SwingEngine;
import org.swixml.script.ScriptService;

/**
 *
 * @author Sorrentino
 */
public abstract class SWIXMLApplication extends SingleFrameApplication {

        
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
	protected SWIXMLApplication() {
	}
	
/*	
    public final void setTaskChangeListener( Task<?,?> t ) {
        t.addPropertyChangeListener( taskChangeListener );
    }
    
    protected void progressMessage( final String message ) {
        
    }
*/   
	/**
	 * render using a resource 
	 * 
	 * <pre>
	 * 
	 * the resource is located using the following convention
	 * 
	 * container class a.b.c.Name ==> a/b/c/Name.xml
	 * 
	 * </pre>
	 * 
	 * @param container target container
	 */
    public final <T extends Container> T render( T container ) throws Exception {
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

        //if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);
        if( getBooleanProperty(AUTO_INJECTFIELD) ) getContext().getResourceMap().injectFields(container);

        String resource = container.getClass().getName().replace('.', '/').concat(".xml");
        
        logger.info( String.format("render resource [%s]", resource));
        
        ScriptService service = engine.getScript();
        if( service != null ) {
        	service.put("application", this);
        }
        
		return engine.render(resource);
	}
	
    /**
     * 
     * @param container
     * @param resource
     * @return
     * @throws Exception
     */
    public final <T extends Container> T render( T container, String resource) throws Exception {
    	if( null==resource ) throw new IllegalArgumentException( "resource is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

        //if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);
        if( getBooleanProperty(AUTO_INJECTFIELD) ) getContext().getResourceMap().injectFields(container);

        ScriptService service = engine.getScript();
        if( service != null ) {
        	service.put("application", this);
        }

        return engine.render(resource);
	}

    /**
     * 
     * @param container
     * @param reader
     * @return
     * @throws Exception
     */
    public final <T extends Container> T render( T container, Reader reader) throws Exception {
    	if( null==reader ) throw new IllegalArgumentException( "reader is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

        //if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);
        if( getBooleanProperty(AUTO_INJECTFIELD) ) getContext().getResourceMap().injectFields(container);

        ScriptService service = engine.getScript();
        if( service != null ) {
        	service.put("application", this);
        }
		return engine.render(reader);
	}

    /**
     * 
     * @param container
     * @param xmlFile
     * @return
     * @throws Exception
     */
    public final <T extends Container> T render( T container, File xmlFile) throws Exception {
    	if( null==xmlFile ) throw new IllegalArgumentException( "xmlFile is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

        //if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);
        if( getBooleanProperty(AUTO_INJECTFIELD) ) getContext().getResourceMap().injectFields(container);

        ScriptService service = engine.getScript();
        if( service != null ) {
        	service.put("application", this);
        }

        return engine.render(xmlFile);
	}

    /**
     * 
     * @param container
     * @param url
     * @return
     * @throws Exception
     */
    public final <T extends Container> T render( T container, java.net.URL url) throws Exception {
    	if( null==url ) throw new IllegalArgumentException( "url is null!");
    	final SwingEngine<T> engine = new SwingEngine<T>( container );
		engine.setClassLoader( getClass().getClassLoader() );

        //if( Boolean.getBoolean(AUTO_INJECTFIELD)) getContext().getResourceMap().injectFields(container);
        if( getBooleanProperty(AUTO_INJECTFIELD) ) getContext().getResourceMap().injectFields(container);

        ScriptService service = engine.getScript();
        if( service != null ) {
        	service.put("application", this);
        }

        return engine.render(url);
	}
    
}
