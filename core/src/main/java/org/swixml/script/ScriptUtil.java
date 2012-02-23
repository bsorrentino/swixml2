/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.script;

import java.util.regex.Matcher;
import org.swixml.LogAware;
import org.swixml.SwingEngine;
import org.swixml.dom.Attribute;
import org.swixml.jsr295.BindingUtils;

/**
 *
 * @author softphone
 */
public class ScriptUtil implements LogAware {

    public final static String PREFIX = "script";


    protected ScriptUtil() {
    }
    
    
    /**
     * 
     * @param attr
     * @return if attr is a script attribute
     */
    public static boolean isScriptAttribute( Attribute attr ) {
    
        return PREFIX.equalsIgnoreCase(attr.getPrefix());
 
    }
    
   /**
     * 
     * @param attr
     * @return if attr is a script attribute
     */
    public static Object evaluateAttribute( Attribute attr, SwingEngine<?> engine ) {
    
        if( ScriptUtil.isScriptAttribute(attr) ) {
            
            ScriptService service = engine.getScript();
            if( service == null ) {
                logger.severe("script service is null. script evaluation has ignored!");
                return null;
            }
            
            Object result;
            
            Matcher m = BindingUtils.getVariableMatcher(attr.getValue());
            if( m.matches() ) {
                
                result = service.evalSafe( m.group(1) );
            }
            else {
                
                result = service.invokeFunctionSafe(attr.getValue());
            }
            
            return result;
            
        }
        return attr.getValue();
 
    }
    
}
