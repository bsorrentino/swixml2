/*
 * @(#)JMenuButton.java.java
 *
 * Copyright 2003 Richard Bair. All rights reserved.
 * SHOPLOGIC PROPRIETARY/CONFIDENTIAL. Unauthorized use is prohibited.
 * All authorizations for use must be in written form.
 */
package org.swixml.contrib;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


/**
 * A JMenuButton is a button that is split up into two parts, both of which are buttons in their own right. The first button is
 * the normal button. It will always execute whatever action is the first action in the associated drop down list. The second button
 * is a down arrow button, and it will bring up the associated JMenu. The menu contains one or more actions.
 * @author Richard Bair
 */
public class JSimpleMenuButton extends JButton {

    class RegularArrow {
        // to draw a nice curved arrow, fill a V shape rather than stroking it with lines

        
        public void draw(Graphics g) {
            // as we're filling rather than stroking, control point is at the apex,

            Rectangle bounds = getBounds();
            
            int y = bounds.y+(bounds.height/2);
            
            int size = 5;
 
            g.translate( (bounds.x + bounds.width), y  );
            g.setColor(Color.BLACK);

            int ox = -size*3;
            int oy = -size-1;

            g.fillPolygon(new int[]{ox, ox + size, ox}, new int[]{oy, oy + size, oy + size * 2}, 3);

        }
    }
    private RegularArrow arrow = new RegularArrow();
    
    private JPopupMenu popupMenu;

    public JSimpleMenuButton() {
    }

    @Override
    public void addNotify() {
        super.addNotify();
    }

    @Override
    public Component add(Component c) {
        if (c instanceof JPopupMenu) {
            this.popupMenu = (JPopupMenu) c;
            return c;
        }
        return super.add(c);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        
        arrow.draw(g);


    }

    public void showPopup() {

        Rectangle bounds = getBounds(new Rectangle());
        
        Point p = new Point((bounds.x + bounds.width), bounds.y+(bounds.height/2));

        SingleFrameApplication app = Application.getInstance(SingleFrameApplication.class);
        //Not sure if this next line is doing anything...weird
        SwingUtilities.convertPoint(this, p, app.getMainFrame());

        popupMenu.show(this, p.x, p.y);
    }
}
