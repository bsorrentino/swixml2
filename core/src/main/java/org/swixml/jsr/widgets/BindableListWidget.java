/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

/**
 *
 * @author softphone
 */
public interface BindableListWidget extends BindableWidget{
    
    java.util.List<?> getBindList();
    void setBindList(java.util.List<?> beanList);

}
