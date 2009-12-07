/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import javax.swing.JSpinner;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Converter;
import org.swixml.jsr295.BindingUtils;
import static org.swixml.SwingEngine.isDesignTime;

/**
 *
 * @author softphone
 */
public class JSpinnerEx extends JSpinner implements BindableBasicWidget{

    public static class Date extends JSpinnerEx {

        private String dateFormat;

        public Date() {
            super();
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public void addNotify() {

            if( getDateFormat()!=null ) {
                setEditor(new JSpinner.DateEditor(this,getDateFormat()));

            }
            else {
                setEditor(new JSpinner.DateEditor(this));

            }
            super.addNotify();
        }


    }

    public JSpinnerEx() {
        super();
    }
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

            AutoBinding binding = BindingUtils.parseBind( this, "value", bindWith );

            if( getConverter()!=null ) {
                binding.setConverter( getConverter() );
            }

        }

        super.addNotify();
    }

}
