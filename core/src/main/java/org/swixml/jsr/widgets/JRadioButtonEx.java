/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import javax.swing.JRadioButton;
import org.jdesktop.beansbinding.Converter;
import org.swixml.jsr295.BindingUtils;
import static org.swixml.SwingEngine.isDesignTime;

/**
 *
 * @author sorrentino
 */
public class JRadioButtonEx extends JRadioButton implements BindableBasicWidget{
    public String getBindWith() {
        return (String) getClientProperty(BINDWITH_PROPERTY);
    }

    public void setBindWith(String bindWith) {
        putClientProperty(BINDWITH_PROPERTY, bindWith);
    }

    public void setConverter(Converter<?, ?> converter) {
        putClientProperty(CONVERTER_PROPERTY, converter);
    }

    public Converter<?, ?> getConverter() {
        return (Converter<?, ?>) getClientProperty(CONVERTER_PROPERTY);
    }

    @Override
    public void addNotify() {

        final String bindWith = getBindWith();

        if( null!=bindWith && !bindWith.isEmpty() ){

            BindingUtils.parseBind( this, "selected", bindWith, getConverter() );

        }

        super.addNotify();
    }

}
