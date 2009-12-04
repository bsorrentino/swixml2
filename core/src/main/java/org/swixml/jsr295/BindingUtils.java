/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr295;

import static org.swixml.LogUtil.logger;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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

/**
 *
 * @author sorrentino
 */
public class BindingUtils  {
    public  static final String TABLE_COLUMN_EDITABLE = "column.editable";
	public static final String TABLE_COLUMN_IS_BOUND = "bind";
	public static final String TABLE_COLUMN_INDEX = "column.index";
	public static final String TABLE_COLUMN_RENDERER = "column.renderer";
	
	
	private static final String pattern =  "[$][{](.*)[}]";

    private BindingUtils() {}

    @SuppressWarnings("serial")
	static public class Column extends TableColumn {
    	private /*static*/ int modelIndex = 0;
    	
    	boolean editable = false;
    	String type = null;
    	
		public Column() {
			super();
			setModelIndex(modelIndex++);
		}
		
		public final String getBindWith() {
			return (String)super.getIdentifier();
		}

		public final void setBindWith(String bindWith) {
			super.setIdentifier(bindWith);
		}

		public final boolean isEditable() {
			return editable;
		}

		public final void setEditable(boolean editable) {
			this.editable = editable;
		}

		public final String getType() {
			return type;
		}

		public final void setType(String type) {
			this.type = type;
		}

		
	}

    public static void setBound( JComponent comp, boolean value ) {
    	if( comp==null) throw new IllegalArgumentException( "comp argument is null!");
    	
    	final String name = comp.getClass().getName().concat(".bound");
    	comp.putClientProperty( name , value);
    }
    
    public static boolean isBound( JComponent comp ) {
    	if( comp==null) throw new IllegalArgumentException( "comp argument is null!");

    	final String name = comp.getClass().getName().concat(".bound");
    	
    	return Boolean.TRUE.equals( comp.getClientProperty(name) );
    }
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
    
    /**
     * 
     * @param beanClass
     * @return
     */
    public static Map<String,PropertyDescriptor> getPropertyMap( Class<?> beanClass ) {
    	PropertyDescriptor pp[] = PropertyUtils.getPropertyDescriptors(beanClass);
    	
		Map<String,PropertyDescriptor> map = new HashMap<String,PropertyDescriptor>(pp.length);
		for( PropertyDescriptor pd : pp ) {
			map.put( pd.getName(), pd);
		}
		
		return map;

    }
    
    public static void setTableColumnRenderer( PropertyDescriptor pd, TableCellRenderer renderer ) {
    	if( pd==null ) throw new IllegalArgumentException("parameter pd is null!");
    	
    	pd.setValue(BindingUtils.TABLE_COLUMN_RENDERER, renderer);
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
    public static AutoBinding parseBind( JComponent owner, String property, String bindProperty ) {
    
        Object client = owner.getClientProperty( SwingEngine.CLIENT_PROPERTY );

        return addAutoBinding( null, UpdateStrategy.READ_WRITE, client, bindProperty, owner, property );
        
    }
    
    
    /**
     * parse bind for UpdateStrategy.READ
     * 
     * @param owner
     * @param bind
     */
    public static AutoBinding parseBindR( JComponent owner, String property, String bindProperty ) {
        
        Object client = owner.getClientProperty( SwingEngine.CLIENT_PROPERTY );

        return addAutoBinding( null, UpdateStrategy.READ, client, bindProperty, owner, property );
        
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
	public static AutoBinding addAutoBinding( BindingGroup bindingGroup, UpdateStrategy strategy, Object source, String beanProperty, Object target, String targetProperty  ) {
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

                return binding;
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
	public static JTableBinding initTableBindingFromTableColumns( BindingGroup group, UpdateStrategy startegy, JTable table, List<?> beanList ) {
    		if( null==table )		throw new IllegalArgumentException( "table argument is null!");
    		if( null==beanList )	throw new IllegalArgumentException( "beanList argument is null!");
 
    		
    		
    		final TableColumnModel columnModel = table.getColumnModel();

    		if( null==columnModel ) throw new IllegalStateException( "columnModel is not set!" );
    		
            Enumeration<TableColumn> tableColumns = columnModel.getColumns();

            if( null==tableColumns ) throw new IllegalStateException( "columnModel hasn't not tableColumns!" );
    		
            JTableBinding binding = SwingBindings.createJTableBinding( startegy, beanList, table);
            
            while( tableColumns.hasMoreElements() ) {
            	
            	TableColumn tc = tableColumns.nextElement();
            	
            	if( !(tc instanceof Column) ) {
            		
            		logger.warning( String.format("column [%s] is not valid. It will be ignored in binding!", tc.getIdentifier()));
            		continue;
            	}
            	
            	Column c = (Column) tc;
   
          	  
          	  logger.info( String.format("column [%s] header=[%s] modelIndex=[%d] resizable=[%b] minWidth=[%s] maxWidth=[%d] preferredWidth=[%d]\n", 
          			  tc.getIdentifier(),
          			  tc.getHeaderValue(),
          			  tc.getModelIndex(),
          			  tc.getResizable(),
          			  tc.getMinWidth(),
          			  tc.getMaxWidth(),
          			  tc.getPreferredWidth()
          			  ));
            	
                final String propertyName = c.getBindWith();
                
                if( null==propertyName ) {
            		logger.warning( String.format("column [%s] has not set bindWith property. It will be ignored in binding!", tc.getIdentifier()));
            		continue;
                	
                }
                
                //
                // Property Binding
                //
                Property bp = BeanProperty.create(propertyName);

                ColumnBinding cb = binding.addColumnBinding( bp) ;
                
                //
                // set Header
                //
               
                
                final Object headerValue = c.getHeaderValue();
               
                if( null==headerValue ) {
                	c.setHeaderValue(propertyName);
                }
                cb.setColumnName( c.getHeaderValue().toString());
                               
                //
                // set Property type
                //
                final String typeName = c.getType();
                
                if( null!=typeName ) {
                	
                    Class<?> typeClass = null;
                    try {
                            typeClass = Class.forName(typeName);

                            cb.setColumnClass(typeClass);
                    } catch (ClassNotFoundException e) {
                        logger.warning( String.format("column type [%s] is not valid java type. It will be ignored in binding!", typeName));
                    }
                                 	
                }
              

                //
                // set Editable
                //
                cb.setEditable( c.isEditable() ) ;
                
                
            }

            
            if( null!=group ) {
            	group.addBinding(binding);
            }
            else {
            	binding.bind();
            }

            return binding;
            
    }

    /**
     * 
     * @param table
     * @param beanList
     */
    @SuppressWarnings("unchecked")
	public static JTableBinding initTableBindingFromBeanInfo( BindingGroup group, UpdateStrategy startegy, JTable table, List<?> beanList, Class<?> beanClass, boolean isAllPropertiesBound ) {
    		if( null==table )		throw new IllegalArgumentException( "table argument is null!");
    		if( null==beanList )	throw new IllegalArgumentException( "beanList argument is null!");
    		if( null==beanClass )	throw new IllegalArgumentException( "beanClass argument is null!");
            
    		PropertyDescriptor[] pp = PropertyUtils.getPropertyDescriptors(beanClass);
        
            JTableBinding binding = SwingBindings.createJTableBinding( startegy, beanList, table);
            
            if( null==pp ) {
                logger.warning("getPropertyDescriptors has returned null!");
                return null;
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
                if( null==isBinded && isAllPropertiesBound==false) {
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

            return binding;
            
    }
    
    /**
     * 
     * @param group
     * @param startegy
     * @param combo
     * @param beanList
     */
	@SuppressWarnings("unchecked")
	public static JComboBoxBinding initComboBinding( BindingGroup group, UpdateStrategy strategy, JComboBox combo, List<?> beanList ) {
		if( null==combo )		throw new IllegalArgumentException( "combo argument is null!");
		if( null==beanList )	throw new IllegalArgumentException( "beanList argument is null!");
        
    
		JComboBoxBinding binding = SwingBindings.createJComboBoxBinding(strategy, beanList, combo);
        

                if( null!=group ) {
                        group.addBinding(binding);
                }
                else {
                        binding.bind();
                }

                return binding;
        
	}
           
	/**
	 * 
	 * @param group
	 * @param strategy
	 * @param list
	 * @param beanList
	 */
	@SuppressWarnings("unchecked")
	public static JListBinding initListBinding( BindingGroup group, UpdateStrategy strategy, JList list, List<?> beanList ) {
		if( null==list )		throw new IllegalArgumentException( "list argument is null!");
		if( null==beanList )	throw new IllegalArgumentException( "beanList argument is null!");
        
    
		JListBinding binding = SwingBindings.createJListBinding(strategy, beanList, list);
        

                if( null!=group ) {
                        group.addBinding(binding);
                }
                else {
                        binding.bind();
                }

                return binding;
	}
}
