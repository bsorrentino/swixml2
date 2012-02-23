/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.converters;

import org.swixml.Converter;
import org.swixml.Localizer;
import org.swixml.LogAware;
import org.swixml.SwingEngine;
import org.swixml.dom.Attribute;
import org.swixml.script.ScriptUtil;

/**
 *
 * @author softphone
 */
public abstract class AbstractConverter<T> implements Converter<T>, LogAware {
    
    
    public Localizer getLocalizer( SwingEngine<?> engine ) {
        
        return Util.getLocalizer(engine);
    }
    

    /**
     * 
     * @param attr
     * @return if attr is a script attribute
     */
    public Object evaluateAttribute( Attribute attr, SwingEngine<?> engine ) {
        
        return ScriptUtil.evaluateAttribute(attr, engine);
    }

    /**
     * 
     * @param type
     * @param attr
     * @param engine
     * @return
     * @throws Exception 
     */
    public final T convert(Class<?> type, Attribute attr, SwingEngine<?> engine) throws Exception {
        
        if ( attr == null ) {
            logger.warning( "attribute is null!");
            return null;
        }
        
        final Object value = evaluateAttribute(attr, engine);
        
        if( value == null ) return null;
        
        if( convertsTo().isInstance(value)) return (T)value;
        
        return convert( value.toString(), type, attr, engine );
    }
    
    /**
     * 
     * @param value evaluated value as string
     * @param type
     * @param attr
     * @param engine
     * @return
     * @throws Exception 
     */
    public abstract T convert(String value, Class<?> type, Attribute attr, SwingEngine<?> engine) throws Exception;

}
