package org.swixml.jsr.widgets;

import java.util.List;

import javax.swing.JComboBox;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.swixml.jsr295.BindingUtils;

@SuppressWarnings("serial")
public class JComboBoxEx extends JComboBox {

	private List<?> beanList;

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
