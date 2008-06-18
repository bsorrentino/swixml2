/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr295;

import org.jdesktop.application.Application;
import java.util.logging.Logger;
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author sorrentino
 */
public class BindingUtils {
    static final Logger log = Logger.getLogger(BindingUtils.class.getName());

    private BindingUtils() {}
    
    /**
     * 
     * @param owner
     * @param bind
     */
    public static void parseBind( Object owner, String property, String bind ) {    
        
        int index = bind.indexOf(".");
        
        if( index==-1 ) {
            log.warning( "bind property is not valid!");
            return;
        }
        Application app = Application.getInstance();
        
        if( app instanceof SwingApplication ) {
            SwingApplication swapp = (SwingApplication) app;
            
            String bean = bind.substring(0, index++);
            String prop = bind.substring(index);
            swapp.getBeanRegistry().addRWAutoBinding(bean, prop, owner, property);
        }
        else {
            log.warning( "application instance is not a SwingApplication instance");
            
        }
        
        
    }

            
}
