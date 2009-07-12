package org.swixml.test;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.junit.Test;


public class BindingTest {

	private String testValue = "TEST";
	
	
	public final String getTestValue() {
		return testValue;
	}


	public final void setTestValue(String testValue) {
		this.testValue = testValue;
		System.out.printf( "setTestValue(%s)\n", testValue);
	}


	@Test
	public void dummy() {
		
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
