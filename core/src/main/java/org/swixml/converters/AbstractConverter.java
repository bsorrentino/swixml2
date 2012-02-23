/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.converters;

import java.util.regex.Matcher;
import org.swixml.Converter;
import org.swixml.Localizer;
import org.swixml.LogAware;
import org.swixml.SwingEngine;
import org.swixml.dom.Attribute;
import org.swixml.jsr295.BindingUtils;
import org.swixml.script.ScriptService;
import org.swixml.script.ScriptUtil;

/**
 *
 * @author softphone
 */
public abstract class AbstractConverter<T> implements Converter<T>, LogAware {
    
    
    public Localizer getLocalizer( SwingEngine<?> engine ) {
        return (engine==null) ? (Localizer)null : engine.getLocalizer();
    }
    
    /**
     * 
     * @param attr
     * @return if attr is a script attribute
     */
    public boolean isScriptAttribute( Attribute attr ) {
    
        return ScriptUtil.isScriptAttribute(attr);
 
    }

    /**
     * 
     * @param attr
     * @return if attr is a script attribute
     */
    public Object evaluateAttribute( Attribute attr, SwingEngine<?> engine ) {
    
        if( isScriptAttribute( attr ) ) {
            
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
