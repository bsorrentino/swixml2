package org.swixml.jsr.widgets;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.swixml.jsr295.BindingUtils;

@SuppressWarnings("serial")
public class JListEx extends JList {

	private List<?> beanList;
	private javax.swing.Action action;
	
	public JListEx() {
		super.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
                if( e.getValueIsAdjusting() ) return;
                if( getSelectedIndex()==-1 ) return;
                
				Action a = getAction();

                if( null==a ) return;

                ActionEvent ev = new ActionEvent( e, 0, null );

                a.actionPerformed(ev);
				
			}
			
		});
	
	}

	/**
     * 
     * @return
     */
    public javax.swing.Action getAction() {
        return action;
    }

    /**
     * 
     * @param action
     */
    public void setAction(javax.swing.Action action) {
        this.action = action;
    }
	
	public final List<?> getBindList() {
		return beanList;
	}

	public final void setBindList(List<?> beanList) {
		this.beanList = beanList;
	}

	@Override
	public void addNotify() {
        if( beanList!=null && !BindingUtils.isBound(this)  ) {
        	
            BindingUtils.initListBinding( null, UpdateStrategy.READ_WRITE, this, beanList );
            BindingUtils.setBound(this, true);

        }

		super.addNotify();
	}

}
