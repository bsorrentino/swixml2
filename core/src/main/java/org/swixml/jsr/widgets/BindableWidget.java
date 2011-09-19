/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

/**
 *
 * @author softphone
 */
public interface BindableWidget {

    String CONVERTER_PROPERTY = "org.swixml.jsr.widgets.Converter";
    
    void setConverter( org.jdesktop.beansbinding.Converter<?,?> converter );
    org.jdesktop.beansbinding.Converter<?,?> getConverter();


}
