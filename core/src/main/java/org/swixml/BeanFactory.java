/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml;

import java.util.List;
import static org.swixml.LogUtil.logger;

import java.awt.LayoutManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.logging.Level;
import org.apache.commons.beanutils.BeanUtils;


import org.apache.commons.beanutils.ConstructorUtils;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.PropertyResolutionException;
import org.jdom.Attribute;
import org.jdom.Element;
import org.swixml.jsr295.BindingUtils;
import org.swixml.processor.ButtonGroupTagProcessor;
import org.swixml.processor.ConstraintsTagProcessor;
import org.swixml.processor.TagProcessor;
import sun.reflect.misc.MethodUtil;

/**
 *
 * @author sorrentino
 */
public class BeanFactory implements Factory {

    private class MethodMap {

        final java.util.Map<String, java.util.List<Method>> nameMap;

        public MethodMap(int capacity) {
            nameMap = new java.util.HashMap<String, java.util.List<Method>>(capacity);
        }

        public void put(String name, Method m) {

            java.util.List<Method> methods = nameMap.get(name);

            if (methods == null) {
                methods = new java.util.ArrayList<Method>(2);
                nameMap.put(name, methods);
            }
            methods.add(m);
        }

        public java.util.List<Method> get(String name) {
            return nameMap.get(name);
        }

        public Method get(String name, Class<?> type) {

            java.util.List<Method> methods = nameMap.get(name);

            if (methods == null || methods.isEmpty()) {
                return null;
            }

            //if( methods.size()==1 ) return methods.get(0);

            for (Method m : methods) {

                if (m.getParameterTypes()[0].equals(type)) {
                    return m;
                }
            }

            return null;
        }

        public Collection<Method> values() {

            java.util.List<Method> result = new java.util.ArrayList<Method>();

            for (java.util.List<Method> lm : nameMap.values()) {

                result.addAll(lm);
            }

            return result;
        }
    }
    final MethodMap nameMap;
    final Class<?> template;
    private final TagProcessor processor;

    public BeanFactory(Class<?> beanClass) {
        this(beanClass, null);
    }

    public BeanFactory(Class<?> beanClass, TagProcessor processor) {
        if (null == beanClass) {
            throw new IllegalArgumentException("beanClass is null!");
        }
        this.template = beanClass;
        this.processor = processor;

        Method[] mm = beanClass.getMethods();

        nameMap = new MethodMap(mm.length);

        for (Method m : mm) {

            int modifier = m.getModifiers();
            String name = m.getName();

            if (Modifier.isPublic(modifier) && !Modifier.isAbstract(modifier)
                    && name.startsWith("set")
                    && m.getParameterTypes().length == 1) {
                nameMap.put(name.substring(3).toLowerCase(), m);
            }
        }
    }

    public Class<?> getTemplate() {
        return template;
    }

    public Object create(Object owner, Element element) throws Exception {
        return template.newInstance();
    }

    /**
     * 
     */
    public Object newInstance(Object... parameter) throws InstantiationException, IllegalAccessException, InvocationTargetException {

        /*
        Class<?> types[] = new Class<?>[ parameter.length ];
        int i=0;
        for( Object p : parameter ) {
        types[i++] = p.getClass();
        }
         */
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

    public Class<?>[] getPropertyType(Object bean, String name) {
        java.util.List<Method> methods = nameMap.get(name.toLowerCase());

        if (null == methods || methods.isEmpty()) {
            return null;
        }

        Class<?> result[] = new Class<?>[methods.size()];

        int i = 0;
        for( Method m : methods ) {
            result[i++] = m.getParameterTypes()[0];
        }
        
        return result;

    }

    public void setProperty(Object bean, Attribute attr, Object value, Class<?> type) throws Exception {
        if (null == attr) {
            throw new IllegalArgumentException("attr is null!");
        }
        if (null == bean) {
            throw new IllegalArgumentException("bean is null!");
        }
        if (null == value) {
            return;
        }

        final String name = attr.getName();
        
        Method m = nameMap.get(name.toLowerCase(), type);

        if (null == m) {
            BeanUtils.setProperty(bean, name, value);
            //throw new NoSuchMethodException(name);
        }
        else
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

        if (null != processor) {
            result = processor.process(p, parent, child, layoutMgr);
        }

        if (!result) {
            result = ButtonGroupTagProcessor.instance.process(p, parent, child, layoutMgr);

            if (!result) {
                result = ConstraintsTagProcessor.instance.process(p, parent, child, layoutMgr);
            }
        }

        return result;
    }

    public final Object getAttributeValue(Object owner, Attribute attr) {
        final boolean isVariable = BindingUtils.isVariablePattern(attr.getValue());

        if (!isVariable) {
            return attr.getValue();
        }

        Object result = null;
        try {

            ELProperty<Object, Object> p = ELProperty.create(attr.getValue());

            if (!p.isReadable(owner)) {
                logger.warning("property " + attr.getValue() + " is not readable!");
                return result;
            }

            result = p.getValue(owner);

        } catch (PropertyResolutionException ex) {
            logger.log(Level.WARNING, "variable " + attr.getValue() + " doesn't exist!", ex);
        }

        return result;
    }
}
