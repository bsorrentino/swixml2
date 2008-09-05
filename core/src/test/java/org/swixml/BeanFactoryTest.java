/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml;

import java.awt.Dimension;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.swing.JDialog;
import javax.swing.JWindow;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
/**
 *
 * @author sorrentino
 */
public class BeanFactoryTest {

    
    @Before
    public void init() {
        
    }
    
    @Test
    public void testJDialog() throws Exception {
      
        
        BeanFactory factory  = new BeanFactory( JDialog.class );
       
        for( Method m : factory.getSetters() ) {
            System.out.printf( "setter %s\n", m);
        }
        
        Method maximumSizeSetter = factory.getSetter("maximumSize");
      
        assertNotNull( maximumSizeSetter );
        
        JDialog dlg = new JDialog();
        
        factory.setProperty(dlg, "maximumSize", new Dimension(100,100));
        
    }
    
}
