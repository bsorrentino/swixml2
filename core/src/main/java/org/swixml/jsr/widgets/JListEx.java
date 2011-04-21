package org.swixml.jsr.widgets;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Converter;
import org.swixml.SwingEngine;
import org.swixml.jsr295.BindingUtils;

@SuppressWarnings("serial")
public class JListEx extends JList implements BindableListWidget, BindableBasicWidget {

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

    @Deprecated
    public final List<?> getBindList() {
            return beanList;
    }

    @Deprecated
    public final void setBindList(List<?> beanList) {
            this.beanList = beanList;
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
        if( beanList == null ) {

            if( getBindWith()!=null ) {
                Object client = getClientProperty( SwingEngine.CLIENT_PROPERTY );

                BeanProperty<Object, List<?>> p = BeanProperty.create( getBindWith() );

                beanList = p.getValue(client);

            }

        }

        if( beanList!=null ) {

            BindingUtils.initListBinding( null, UpdateStrategy.READ_WRITE, this, beanList, getConverter() );

        }

        super.addNotify();
    }

}
