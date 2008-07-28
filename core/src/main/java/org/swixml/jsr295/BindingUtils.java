/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr295;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import org.jdesktop.application.Application;
import javax.swing.JTable;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.swixml.LogUtil;
import org.swixml.SwingEngine;
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author sorrentino
 */
public class BindingUtils extends LogUtil  {
    private static final String pattern =  "[$][{](.*)[}]";

    private BindingUtils() {}

    /**
     * 
     * @param value
     * @return
     */
    public static boolean isVariablePattern( String value ) {
        if( null==value ) return false;
        return Pattern.matches(pattern, value);
    }
    

    /**
     * 
     * @param owner
     * @param bind
     */
    public static void parseBind( Object owner, String property, String bindProperty ) {    
    
        Application app = Application.getInstance();
        
        if( app instanceof SwingApplication ) {
            SwingApplication swapp = (SwingApplication) app;
            
            BindingGroup group = swapp.getBindingGroup();
            
            addAutoBinding( group, UpdateStrategy.READ_WRITE, swapp, bindProperty, owner, property);
        }
        else {
            logger.warning( "application instance is not a SwingApplication instance");
            
        }
        
        
    }
    
    /**
     * 
     * @param bindingGroup
     * @param strategy
     * @param source
     * @param beanProperty
     * @param target
     * @param targetProperty
     */
    public static void addAutoBinding( BindingGroup bindingGroup, UpdateStrategy strategy, Object source, String beanProperty, Object target, String targetProperty  ) {
      if( null==bindingGroup ) throw new IllegalArgumentException( "binding group argument is null!");
      if( null==source ) throw new IllegalArgumentException( "bean argument is null!");
      if( null==target ) throw new IllegalArgumentException( "target argument is null!");
      if( null==targetProperty ) throw new IllegalArgumentException( "targetProperty argument is null!");

      Property tp = ( targetProperty.startsWith("$") ) 
              ? ELProperty.create(targetProperty) 
              : BeanProperty.create(targetProperty);

      AutoBinding binding = Bindings.createAutoBinding(
              strategy, 
              source, 
              BeanProperty.create(beanProperty), 
              target, 
              tp);

      bindingGroup.addBinding(binding);

    }

    /**
     * 
     * @param p
     * @return
     */
    private static  int getColumnIndex( PropertyDescriptor p ) {
        final Object i = p.getValue("column.index");
        return (i instanceof Integer ) ? (Integer)i : Integer.MAX_VALUE;
    }
    

    /**
     * 
     * @param table
     * @param beanList
     */
    public static void initTableBinding( UpdateStrategy startegy, JTable table, List<?> beanList, Class<?> beanClass ) {
            PropertyDescriptor[] pp = PropertyUtils.getPropertyDescriptors(beanClass);
        
            JTableBinding tb = SwingBindings.createJTableBinding( startegy, beanList, table);
            
            if( null==pp ) {
                SwingEngine.logger.warning("getPropertyDescriptors has returned null!");
                return;
            }
            
            Arrays.sort( pp, new Comparator<PropertyDescriptor>() {

                public int compare(PropertyDescriptor p1, PropertyDescriptor p2) {
    
                    final int i1 = getColumnIndex(p1);
                    final int i2 = getColumnIndex(p2);
                    
                    return ( i1-i2 );                   
                }
            });
            for( PropertyDescriptor p : pp ) {
                
                Boolean isBinded = (Boolean) p.getValue("bind");
                if( null!=isBinded && Boolean.FALSE.equals(isBinded)) {
                    continue; // skip property
                }
                
                final String name = p.getName();
                
                if( "class".equals(name)) {
                    continue;
                }
                
                Property bp = BeanProperty.create(name);

                ColumnBinding cb = tb.addColumnBinding( bp) ;
                
                final String displayName = p.getDisplayName();
                
                cb.setColumnName( (null==displayName) ? name : displayName);
                
                final Class<?> type = p.getPropertyType();
                
                if( type.isPrimitive()) {
                    cb.setColumnClass( MethodUtils.toNonPrimitiveClass(type) );
                    
                } 
                else {    
                    cb.setColumnClass(type);
                }
                

                Boolean isEditable = (Boolean) p.getValue("column.editable");
                
                cb.setEditable( (null!=isEditable && Boolean.TRUE.equals(isEditable)) ) ;
                
                
            }

            tb.bind();
            
    }
    
            
}
