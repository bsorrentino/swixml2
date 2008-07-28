/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml;

import java.awt.Dialog;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.beanutils.PropertyUtils;
import static org.swixml.SwingEngine.logger;

/**
 *
 * @author sorrentino
 */
public class BeanFactory implements Factory {

    final Map<String,String> nameMap;
    final Class<?> template;
    
    public BeanFactory( Class<?> beanClass ) {
        this.template = beanClass;
        
        PropertyDescriptor[] pp = PropertyUtils.getPropertyDescriptors(beanClass);
    
        nameMap = new HashMap<String, String>( pp.length );
        for( PropertyDescriptor p : pp ) {
            String s = p.getName();
            
            if(Dialog.class.isAssignableFrom(template) ) {
                
                if( "size".equals(s) ) { // Bux Fix
                    nameMap.put( s, "minimumSize");
                    continue;
                }
                    
            }
            nameMap.put( s.toLowerCase() , s);
        }
    }

    public Class getTemplate() {
        return template;
    }

    public String getOriginalName( String name ) {
        if( null==name ) throw new IllegalArgumentException( "name is null!");
        return nameMap.get( name.toLowerCase() );
    }
    
    public Object newInstance() throws Exception {
        return template.newInstance();
    }

    public Object newInstance(Object parameter) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object newInstance(Object[] parameter) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> types[] = new Class<?>[ parameter.length ];
        int i=0;
        for( Object p : parameter ) {
            types[i++] = p.getClass();
        }
        try {
            // get runtime class of the parameter
            return template.getConstructor(types).newInstance(parameter);
        } catch (NoSuchMethodException ex) {
            logger.log(Level.SEVERE, "newInstance", ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "newInstance", ex);
        }
        
        return null;
    }


    public Collection getSetters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Method getSetter(Class template) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Method getSetter(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Method guessSetter(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Class<?> getPropertyType( Object bean, String name )  {
        if( null==name ) throw new IllegalArgumentException("name is null!");
        
        final String originalName = getOriginalName(name);
        
        if( null==originalName ) return null;

        Class<?> result = null;
        try {
            return PropertyUtils.getPropertyType(bean, originalName);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "getPropertyType", ex);
        }
        
        return result;
    }
    
    public void setProperty( Object bean, String name, Object value ) throws Exception {
        if( null==name ) throw new IllegalArgumentException("name is null!");
        if( null==bean ) throw new IllegalArgumentException("bean is null!");
        if( null==value ) return;
        
        final String originalName = getOriginalName(name);
        if( null==originalName ) throw new NoSuchMethodException(name);
        
        PropertyUtils.setSimpleProperty(bean, originalName, value);
        
    }

}
