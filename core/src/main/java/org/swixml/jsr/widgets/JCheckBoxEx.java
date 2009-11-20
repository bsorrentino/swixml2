package org.swixml.jsr.widgets;

import javax.swing.JCheckBox;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Converter;
import org.swixml.jsr295.BindingUtils;

/**
 * 
 * @author sorrentino
 *
 */
@SuppressWarnings("serial")
public class JCheckBoxEx extends JCheckBox implements BindableBasicWidget{
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

        if( null!=bindWith && bindWith.length()>0 && bindWith.trim().length()>0){

            AutoBinding binding = BindingUtils.parseBind( this, "selected", bindWith );

            if( getConverter()!=null ) {
                binding.setConverter( getConverter() );
            }

        }

        super.addNotify();
    }

}
