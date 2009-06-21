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

    public PropertyDescriptor[] getPropertyDescriptors(){
        PropertyDescriptor[] properties = new PropertyDescriptor[2];
    
        try {
            properties[PROPERTY_age] = new PropertyDescriptor ( "age", examples.SimpleBean.class, "getAge", "setAge" ); // NOI18N
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", examples.SimpleBean.class, "getName", "setName" ); // NOI18N

            // SWIXML2 extension
            BindingUtils.setTableColumnIndex(properties[PROPERTY_age], 2);
            BindingUtils.setTableColumnIndex(properties[PROPERTY_name], 1);
            BindingUtils.setTableColumnEditable(properties[PROPERTY_name], true);
            
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }
        
        return properties;     
    }


    public BeanDescriptor getBeanDescriptor() {
    	return new BeanDescriptor  ( examples.SimpleBean.class , null );
    }


}

