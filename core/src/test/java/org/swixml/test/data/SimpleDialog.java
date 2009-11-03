package org.swixml.test.data;

import javax.swing.JDialog;
import javax.swing.JLabel;

import org.jdesktop.application.Resource;

@SuppressWarnings("serial")
public class SimpleDialog extends JDialog {

	@Resource 
	public String entry1;

	public final JLabel myLabel = new JLabel();
	
	public SimpleDialog() {
		super();
	
		myLabel.setName("myLabel");
		add( myLabel );
	}
	
	
	
}
