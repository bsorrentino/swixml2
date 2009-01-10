package org.swixml.jsr.widgets;

import javax.swing.JCheckBox;

import org.swixml.jsr295.BindingUtils;

/**
 * 
 * @author sorrentino
 *
 */
@SuppressWarnings("serial")
public class JCheckBoxEx extends JCheckBox {

    String bindWith;

    public String getBindWith() {
        return bindWith;
    }

    public void setBindWith(String bindWith) {
        this.bindWith = bindWith;
        if( null!=bindWith) {
            BindingUtils.parseBind( this, "selected", bindWith );
        }
    }
	
}
