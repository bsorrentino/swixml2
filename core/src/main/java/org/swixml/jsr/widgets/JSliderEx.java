/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.beansbinding.Converter;
import org.swixml.jsr295.BindingUtils;

/**
 *
 * @author softphone
 */
public class JSliderEx extends JSlider implements BindableBasicWidget {
    
	private Action action;
	
	private ChangeListener listener = new ChangeListener() {

		public void stateChanged(ChangeEvent e) {

			if( action!=null )
				action.actionPerformed( new ActionEvent(  e, 0, "changed") );
		}
		
	};

    public final Action getAction() {
		return action;
	}

	public final void setAction(Action action) {
		this.action = action;
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

        if( null!=bindWith && !bindWith.isEmpty() ){

            BindingUtils.parseBind( this, "value", bindWith, getConverter() );

        }

        super.addChangeListener( listener );
        super.addNotify();
    }

    @Override
    public void removeNotify() {    	
    	super.removeNotify();
    	
        super.removeChangeListener( listener );
    }
}
