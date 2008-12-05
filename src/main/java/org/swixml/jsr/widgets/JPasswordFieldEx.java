package org.swixml.jsr.widgets;

import javax.swing.JPasswordField;

import org.swixml.jsr295.BindingUtils;

@SuppressWarnings("serial")
public class JPasswordFieldEx extends JPasswordField {
    String bindWith;

    public String getBindWith() {
        return bindWith;
    }

    public void setBindWith(String bindWith) {
        this.bindWith = bindWith;
        if( null!=bindWith) {
            BindingUtils.parseBind( this, "text", bindWith );
        }
        
    }

}
