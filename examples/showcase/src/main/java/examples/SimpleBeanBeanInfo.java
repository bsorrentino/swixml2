/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.swixml.jsr295.BindingUtils;


/**
 * @author sorrentino
 */
public class SimpleBeanBeanInfo extends SimpleBeanInfo {

    private static final int PROPERTY_age = 0;
    private static final int PROPERTY_name = 1;
    private static final int PROPERTY_field3 = 2;
    private static final int PROPERTY_field4 = 3;

    public PropertyDescriptor[] getPropertyDescriptors(){
        try {
	        PropertyDescriptor[] properties = new PropertyDescriptor[] {
	            new PropertyDescriptor ( "age", examples.SimpleBean.class, "getAge", "setAge" ), 
	            new PropertyDescriptor ( "name", examples.SimpleBean.class, "getName", "setName" ), 
	            new PropertyDescriptor ( "field3", examples.SimpleBean.class, "getField3", "setField3" ), 
	            new PropertyDescriptor ( "field4", examples.SimpleBean.class, "getField4", "setField4" ), 
	        };

	        // SWIXML2 extension
            BindingUtils.setTableColumnIndex(properties[PROPERTY_age], 2);
            BindingUtils.setTableColumnIndex(properties[PROPERTY_name], 1);
            BindingUtils.setTableColumnEditable(properties[PROPERTY_name], true);
            
            properties[PROPERTY_field3].setDisplayName("3rd field" );
            properties[PROPERTY_field4].setDisplayName("4th field" );

            return properties;     
           
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }
        
        return null;
    }


    public BeanDescriptor getBeanDescriptor() {
    	return new BeanDescriptor  ( examples.SimpleBean.class , null );
    }


}

