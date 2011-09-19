/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.factory;

import org.jdom.Attribute;
import org.swixml.XSplitPane;
import org.swixml.processor.TagProcessor;

/**
 *
 * @author softphone
 */
public class SplitPaneFactory extends BeanFactory {

    public SplitPaneFactory( TagProcessor processor) {
        super( XSplitPane.class, processor );
    }

    public SplitPaneFactory() {
        super( XSplitPane.class );
    }

    @Override
    public void setProperty(Object bean, Attribute attr, Object value, Class<?> type) throws Exception {

        final String name = attr.getName();
        
        if( "dividerlocation".equalsIgnoreCase(name)) {

            if( attr.getValue().contains(".") )
                super.setProperty(bean, attr, attr.getDoubleValue(), Double.TYPE);
            else
                super.setProperty(bean, attr, attr.getIntValue(), Integer.TYPE);

            return;
        }

        super.setProperty(bean, attr, value, type);
    }


}
