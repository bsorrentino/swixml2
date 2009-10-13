/*
 * @(#)JMenuButton.java.java
 *
 * Copyright 2003 Richard Bair. All rights reserved.
 * SHOPLOGIC PROPRIETARY/CONFIDENTIAL. Unauthorized use is prohibited.
 * All authorizations for use must be in written form.
 */
package org.swixml.contrib;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


/**
 * A JMenuButton is a button that is split up into two parts, both of which are buttons in their own right. The first button is
 * the normal button. It will always execute whatever action is the first action in the associated drop down list. The second button
 * is a down arrow button, and it will bring up the associated JMenu. The menu contains one or more actions.
 * @author Richard Bair
 */
public class JSimpleMenuButton extends JButton {
	private JPopupMenu popupMenu;
        
        public JSimpleMenuButton() {
        }


        @Override
        public void addNotify() {
            super.addNotify();
        }

        @Override
        public Component add(Component c) {
            if( c instanceof JPopupMenu ) {
                this.popupMenu = (JPopupMenu) c;
                return c;
            }
            return super.add(c);
        }

        public void showPopup() {

            Point p = new Point(getLocation());

            SingleFrameApplication app = Application.getInstance(SingleFrameApplication.class);
            //Not sure if this next line is doing anything...weird
            SwingUtilities.convertPoint(this, p, app.getMainFrame());

            popupMenu.show(this, p.x + this.getWidth(), p.y);
        }


 }
