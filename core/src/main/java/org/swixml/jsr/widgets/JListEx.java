package org.swixml.jsr.widgets;

import java.util.List;

import javax.swing.JList;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.swixml.jsr295.BindingUtils;

@SuppressWarnings("serial")
public class JListEx extends JList {
	private List<?> beanList;

	public final List<?> getBeanList() {
		return beanList;
	}

	public final void setBeanList(List<?> beanList) {
		this.beanList = beanList;
	}

	@Override
	public void addNotify() {
        if( beanList!=null ) {
        	
            BindingUtils.initListBinding( null, UpdateStrategy.READ_WRITE, this, beanList );
        }

		super.addNotify();
	}

}
