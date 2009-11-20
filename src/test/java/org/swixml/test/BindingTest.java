package org.swixml.test;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import junit.framework.Assert;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Converter;
import org.junit.Test;


public class BindingTest {

	private String testValue = "TEST";
	private Map<String,Object> map = new  HashMap<String,Object>();
	
	
	public final Map<String, Object> getMap() {
		return map;
	}


	public final String getTestValue() {
		return testValue;
	}


	public final void setTestValue(String testValue) {
		this.testValue = testValue;
		System.out.printf( "setTestValue(%s)\n", testValue);
	}


	@Test
	public void elProperty() {
		
		ELProperty<Object,String> p = ELProperty.create("${testValue}");
		
		String value =  p.getValue( this );
		
		Assert.assertEquals(testValue, value);
	}

	@Test
	public void elLocalMap() {
		
		Map<String,Object> localMap = new HashMap<String,Object>();
		
		localMap.put("testValue", testValue);
		localMap.put("testValue2", "xxx");
		
		ELProperty<Object,String> p = ELProperty.create("${testValue2}");
		
		String value =  p.getValue( localMap );
		
		Assert.assertEquals("xxx", value);
	}
	
	@Test
	public void elPropertyMap() {
		
		map.put("testValue", testValue);
		map.put("testValue2", "xxx");
		
		ELProperty<Object,String> p = ELProperty.create("${map.testValue2}");
		
		String value =  p.getValue( this );
		
		Assert.assertEquals("xxx", value);
	}

        public static class MyDialog extends JDialog {

            private long time = 0;

            JTextField textField = new JTextField();

            public MyDialog() {
 		add(textField);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
           }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }


        }

        @Test
	public void converter() {

		MyDialog dialog = new MyDialog();



		{
		Binding b = Bindings.createAutoBinding( UpdateStrategy.READ_WRITE,
					dialog,
					//ELProperty.create( "${testValue}"),
					BeanProperty.create( "time"),
					dialog.textField,
					BeanProperty.create( "text")
					);

                b.setConverter( new Converter() {

                    @Override
                    public Object convertForward(Object value) {
                        System.out.println( "convertForward " + value );
                        return value.toString();
                    }

                    @Override
                    public Object convertReverse(Object value) {
                        System.out.println( "convertReverse " + value );
                        return Long.parseLong(value.toString());
                    }
                });

                b.bind();
		}


		dialog.setSize( 100, 100 );
		dialog.pack();

		dialog.setVisible(true);

                dialog.textField.setText("1024");
	}
	
	public static void main(String[] args) {

		BindingTest bindTest = new BindingTest();
		
		
		JDialog dialog = new JDialog();
		
		JTextField textField = new JTextField();
		JButton button = new JButton("close");
		
		textField.setColumns(10);
		

		{
		Binding b = Bindings.createAutoBinding( UpdateStrategy.READ_WRITE, 
					bindTest, 
					//ELProperty.create( "${testValue}"),
					BeanProperty.create( "testValue"),
					textField, 
					BeanProperty.create( "text")
					);
		b.bind();
		}
		
		
		dialog.setLayout( new GridLayout(2,1) );
		dialog.add(textField);
		dialog.add(button);
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setSize( 100, 100 );
		dialog.pack();
		
		dialog.setVisible(true);
		
	}

}
