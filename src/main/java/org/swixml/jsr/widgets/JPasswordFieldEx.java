package org.swixml.jsr.widgets;

import javax.swing.JPasswordField;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Converter;
import org.swixml.jsr295.BindingUtils;
import static org.swixml.SwingEngine.isDesignTime;

@SuppressWarnings("serial")
public class JPasswordFieldEx extends JPasswordField implements BindableBasicWidget {
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

        if( !isDesignTime() && null!=bindWith && !bindWith.isEmpty() ){

            AutoBinding binding = BindingUtils.parseBind( this, "text", bindWith );

            if( getConverter()!=null ) {
                binding.setConverter( getConverter() );
            }

        }

        super.addNotify();
    }
}
