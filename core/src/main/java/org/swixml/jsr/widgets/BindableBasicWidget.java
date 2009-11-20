/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

/**
 *
 * @author softphone
 */
public interface BindableBasicWidget extends BindableWidget{
    String BINDWITH_PROPERTY = "org.swixml.jsr.widgets.bindWith";

    String getBindWith();
    void setBindWith(String bindWith);


}
