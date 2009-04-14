/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr295;

import static org.swixml.LogUtil.logger;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.swixml.SwingEngine;
import org.swixml.jsr.widgets.JTableEx;

/**
 *
 * @author sorrentino
 */
public class BindingUtils  {
    public  static final String TABLE_COLUMN_EDITABLE = "column.editable";
	public static final String TABLE_COLUMN_IS_BOUND = "bind";
	public static final String TABLE_COLUMN_INDEX = "column.index";
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
     * @param beanClass
     * @param propertyName
     * @return
     */
    public static PropertyDescriptor findProperty( Class<?> beanClass, String propertyName ) {

    	if( null==propertyName ) throw new IllegalArgumentException( "propertyName param is null!");
    	PropertyDescriptor[] pp = PropertyUtils.getPropertyDescriptors(beanClass);
    	for( PropertyDescriptor pd : pp ) {
    		if( propertyName.equalsIgnoreCase(pd.getName()) ) {
    	    	return pd;
    		}
    	}
    	return null;
    }
    
    
    public static void setTableColumnIndex( PropertyDescriptor pd, int index ) {
    	if( pd==null ) throw new IllegalArgumentException("parameter pd is null!");
    	
    	pd.setValue(BindingUtils.TABLE_COLUMN_INDEX, index);
    }
    
    public static void setTableColumnIsBound( PropertyDescriptor pd, boolean bind ) {
    	if( pd==null ) throw new IllegalArgumentException("parameter pd is null!");
    	
    	pd.setValue(BindingUtils.TABLE_COLUMN_IS_BOUND, bind);
    }
    
    public static void setTableColumnEditable( PropertyDescriptor pd, boolean editable ) {
    	if( pd==null ) throw new IllegalArgumentException("parameter pd is null!");
    	
    	pd.setValue(BindingUtils.TABLE_COLUMN_EDITABLE, editable);
    }

    /**
     * parse bind for UpdateStrategy.READ_WRITE
     * 
     * @param owner
     * @param bind
     */
    public static void parseBind( JComponent owner, String property, String bindProperty ) {    
    
        Object client = owner.getClientProperty( SwingEngine.CLIENT_PROPERTY );

        addAutoBinding( null, UpdateStrategy.READ_WRITE, client, bindProperty, owner, property );
        
    }
    
    
    /**
     * parse bind for UpdateStrategy.READ
     * 
     * @param owner
     * @param bind
     */
    public static void parseBindR( JComponent owner, String property, String bindProperty ) {    
        
        Object client = owner.getClientProperty( SwingEngine.CLIENT_PROPERTY );

        addAutoBinding( null, UpdateStrategy.READ, client, bindProperty, owner, property );
        
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
	@SuppressWarnings("unchecked")
	public static void addAutoBinding( BindingGroup bindingGroup, UpdateStrategy strategy, Object source, String beanProperty, Object target, String targetProperty  ) {
		if( null==source )			throw new IllegalArgumentException( "bean argument is null!");
		if( null==target )			throw new IllegalArgumentException( "target argument is null!");
		if( null==targetProperty )	throw new IllegalArgumentException( "targetProperty argument is null!");
	
		final Property tp = ( targetProperty.startsWith("$") ) 
		      ? ELProperty.create(targetProperty) 
		      : BeanProperty.create(targetProperty);
		
		final AutoBinding binding = Bindings.createAutoBinding(
		      strategy, 
		      source, 
		      BeanProperty.create(beanProperty), 
		      target, 
		      tp);
	  
		if( null!=bindingGroup ) {
			bindingGroup.addBinding(binding);
		} else {
			binding.bind();    	  
		}  
	}

    /**
     * 
     * @param p
     * @return
     */
    private static  int getColumnIndex( PropertyDescriptor p ) {
        final Object i = p.getValue(TABLE_COLUMN_INDEX);
        return (i instanceof Integer ) ? (Integer)i : Integer.MAX_VALUE;
    }
    

    /**
     * 
     * @param table
     * @param beanList
     */
    @SuppressWarnings("unchecked")
	public static void initTableBinding( BindingGroup group, UpdateStrategy startegy, JTableEx table ) {
    		if( null==table )		throw new IllegalArgumentException( "table argument is null!");

    		Class<?> beanClass = table.getBindClass();
    		List<?> beanList = table.getBindList();
    		
    		if( null==beanList )	throw new IllegalStateException( "beanList argument is null!");
    		if( null==beanClass )	throw new IllegalStateException( "beanClass argument is null!");
            
    		PropertyDescriptor[] pp = PropertyUtils.getPropertyDescriptors(beanClass);
        
            JTableBinding binding = SwingBindings.createJTableBinding( startegy, beanList, table);
            
            if( null==pp ) {
                logger.warning("getPropertyDescriptors has returned null!");
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
                
                Boolean isBinded = (Boolean) p.getValue(TABLE_COLUMN_IS_BOUND);
                if( null==isBinded && table.isAllPropertyBound()==false) {
                	continue;
                }
                if( (null!=isBinded && Boolean.FALSE.equals(isBinded)) ) {
                    continue; // skip property
                }
                	
                final String name = p.getName();
                
                if( "class".equals(name)) {
                    continue;
                }
                
                Property bp = BeanProperty.create(name);

                ColumnBinding cb = binding.addColumnBinding( bp) ;
                
                final String displayName = p.getDisplayName();
                
                cb.setColumnName( (null==displayName) ? name : displayName);
                
                final Class<?> type = p.getPropertyType();
                
                if( type.isPrimitive()) {
                    cb.setColumnClass( MethodUtils.toNonPrimitiveClass(type) );
                    
                } 
                else {    
                    cb.setColumnClass(type);
                }
                

                Boolean isEditable = (Boolean) p.getValue(TABLE_COLUMN_EDITABLE);
                
                cb.setEditable( (null!=isEditable && Boolean.TRUE.equals(isEditable)) ) ;
                
                
            }

            if( null!=group ) {
            	group.addBinding(binding);
            }
            else {
            	binding.bind();
            }
            
    }
    
    /**
     * 
     * @param group
     * @param startegy
     * @param combo
     * @param beanList
     */
	@SuppressWarnings("unchecked")
	public static void initComboBinding( BindingGroup group, UpdateStrategy strategy, JComboBox combo, List<?> beanList ) {
		if( null==combo )		throw new IllegalArgumentException( "combo argument is null!");
		if( null==beanList )	throw new IllegalArgumentException( "beanList argument is null!");
        
    
		JComboBoxBinding binding = SwingBindings.createJComboBoxBinding(strategy, beanList, combo);
        

        if( null!=group ) {
        	group.addBinding(binding);
        }
        else {
        	binding.bind();
        }
        
	}
           
	/**
	 * 
	 * @param group
	 * @param strategy
	 * @param list
	 * @param beanList
	 */
	@SuppressWarnings("unchecked")
	public static void initListBinding( BindingGroup group, UpdateStrategy strategy, JList list, List<?> beanList ) {
		if( null==list )		throw new IllegalArgumentException( "list argument is null!");
		if( null==beanList )	throw new IllegalArgumentException( "beanList argument is null!");
        
    
		JListBinding binding = SwingBindings.createJListBinding(strategy, beanList, list);
        

        if( null!=group ) {
        	group.addBinding(binding);
        }
        else {
        	binding.bind();
        }
        
	}
}
