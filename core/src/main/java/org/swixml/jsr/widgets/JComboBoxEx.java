package org.swixml.jsr.widgets;

import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.swixml.jsr295.BindingUtils;

@SuppressWarnings("serial")
public class JComboBoxEx extends JComboBox {

	private List<?> beanList;

	public JComboBoxEx() {
		super();
	}

	public JComboBoxEx(ComboBoxModel model) {
		super(model);
	}

	public JComboBoxEx(Object[] items) {
		super(items);
	}

	public JComboBoxEx(Vector<?> items) {
		super(items);
	}

	public final List<?> getBindList() {
		return beanList;
	}

	public final void setBindList(List<?> beanList) {
		this.beanList = beanList;
	}

	@Override
	public void addNotify() {
        if( beanList!=null ) {
        	
            BindingUtils.initComboBinding( null, UpdateStrategy.READ_WRITE, this, beanList );
        }

		super.addNotify();
	}

}
