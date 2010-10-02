/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml;

import java.awt.LayoutManager;
import org.jdom.Element;
import static org.swixml.LogUtil.logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.beanutils.ConstructorUtils;
import org.jdom.Attribute;
import org.swixml.processor.ButtonGroupTagProcessor;
import org.swixml.processor.ConstraintsTagProcessor;
import org.swixml.processor.TagProcessor;

/**
 *
 * @author sorrentino
 */
public class BeanFactory implements Factory {

    final Map<String,Method> nameMap;
    final Class<?> template;
    private final TagProcessor processor;
    
    public BeanFactory( Class<?> beanClass ) {
       this( beanClass, null );
    }
    
    public BeanFactory( Class<?> beanClass, TagProcessor processor ) {
		if( null==beanClass) throw new IllegalArgumentException( "beanClass is null!");
        this.template = beanClass;
        this.processor = processor;
        
        Method[] mm = beanClass.getMethods();
        
        nameMap = new HashMap<String, Method>(mm.length);
        
        for( Method m : mm ) {
            
            int modifier = m.getModifiers();
            String name = m.getName();
            
            if( Modifier.isPublic(modifier) && !Modifier.isAbstract(modifier) &&
                name.startsWith("set") && 
                m.getParameterTypes().length==1 ) 
            {
                nameMap.put( name.substring(3).toLowerCase(), m);
            }
        }
    }

    public Class<?> getTemplate() {
        return template;
    }

    public Object newInstance( List<Attribute> attributes ) throws Exception {
        return template.newInstance();
    }

    /**
     * 
     */
    public Object newInstance(Object... parameter) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> types[] = new Class<?>[ parameter.length ];
        int i=0;
        for( Object p : parameter ) {
            types[i++] = p.getClass();
        }
        try {
            // get runtime class of the parameter
            //return template.getConstructor(types).newInstance(parameter);
        	return ConstructorUtils.invokeConstructor(template, parameter);
        } catch (NoSuchMethodException ex) {
            logger.log(Level.SEVERE, "newInstance", ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "newInstance", ex);
        }
        
        return null;
    }


    public Collection<Method> getSetters() {
        
        return nameMap.values();
    }

    private Method getSetter(String name) {
        if( null==name ) throw new IllegalArgumentException("name is null!");
        return nameMap.get( name.toLowerCase());
    }

    public Class<?> getPropertyType( Object bean, String name )  {
        Method m = getSetter(name);
        
        if( null==m ) return null;

        return m.getParameterTypes()[0];
        
    }
    
    public void setProperty( Object bean, String name, Object value ) throws Exception {
        if( null==name ) throw new IllegalArgumentException("name is null!");
        if( null==bean ) throw new IllegalArgumentException("bean is null!");
        if( null==value ) return;
        
        Method m = getSetter(name);        
        if( null==m ) throw new NoSuchMethodException(name);
        
        m.invoke(bean, value);
        
        
                
    }


    /**
     * 
     * @param p
     * @param parent
     * @param child
     * @return
     * @throws Exception
     */
    public boolean process(Parser p, Object parent, Element child, LayoutManager layoutMgr) throws Exception {
        boolean result = false;
        
        if(null != processor)
            result = processor.process(p, parent, child, layoutMgr);

        if (!result) {
            result = ButtonGroupTagProcessor.instance.process(p, parent, child, layoutMgr);

            if (!result) {
                result = ConstraintsTagProcessor.instance.process(p, parent, child, layoutMgr);
            }
        }

        return result;
    }




}
