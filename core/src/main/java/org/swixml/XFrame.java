/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author softphone
 */
public class XFrame extends JFrame {

    Dimension _size;

    @Override
    public void setSize(Dimension value) {

        _size = new Dimension(value.width, value.height);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        if( _size!=null ) {
            super.setSize(_size);
        }
    }


}
