package org.swixml.jsr.widgets;

import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.swixml.SwingEngine;
import org.swixml.jsr295.BindingUtils;
import static org.swixml.SwingEngine.isDesignTime;

@SuppressWarnings("serial")
public class JComboBoxEx extends JComboBox implements BindableListWidget, BindableBasicWidget {

	private List<?> beanList;

        public JComboBoxEx() {
		super();
		
	}

	public JComboBoxEx(ComboBoxModel model) {
		super(model);
	}

	public JComboBoxEx(Object[] items) {
		super(items);
	}

	public JComboBoxEx(Vector<?> items) {
		super(items);
	}

	public final List<?> getBindList() {
		return beanList;
	}

	public final void setBindList(List<?> beanList) {
		this.beanList = beanList;
	}

        public void setConverter(Converter<?, ?> converter) {
            putClientProperty(CONVERTER_PROPERTY, converter);
        }

        public Converter<?, ?> getConverter() {
            return (Converter<?, ?>) getClientProperty(CONVERTER_PROPERTY);
        }

        public String getBindWith() {
            return (String) getClientProperty(BINDWITH_PROPERTY);
        }

        public void setBindWith(String bindWith) {
            putClientProperty(BINDWITH_PROPERTY, bindWith);
        }

	
	@Override
	public void addNotify() {
	
            if( getBindList()!=null && !BindingUtils.isBound(this) ) {

                BindingGroup context = new BindingGroup();

                BindingUtils.initComboBinding( context, UpdateStrategy.READ_WRITE, this, getBindList(), getConverter() );

                if( getBindWith()!=null ) {
                    
                    Object client = getClientProperty( SwingEngine.CLIENT_PROPERTY );

                    Binding b = Bindings.createAutoBinding( UpdateStrategy.READ_WRITE,
                        client,
                        //ELProperty.create( "${testValue}"),
                        BeanProperty.create( getBindWith() ),
                        getEditor().getEditorComponent(),
                        BeanProperty.create( "text")
                        );
                    if( getConverter()!=null ) {
                        b.setConverter( getConverter() );
                    }

                    context.addBinding(b);
                }

                context.bind();
            }

            super.addNotify();
	}

}
