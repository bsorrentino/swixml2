/*
 * @(#)JMenuButton.java.java
 *
 * Copyright 2003 Richard Bair. All rights reserved.
 * SHOPLOGIC PROPRIETARY/CONFIDENTIAL. Unauthorized use is prohibited.
 * All authorizations for use must be in written form.
 */
package org.swixml.contrib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.plaf.UIResource;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


/**
 * A JMenuButton is a button that is split up into two parts, both of which are buttons in their own right. The first button is
 * the normal button. It will always execute whatever action is the first action in the associated drop down list. The second button
 * is a down arrow button, and it will bring up the associated JMenu. The menu contains one or more actions.
 * @author Richard Bair
 */
public class JMenuButton extends JPanel {
	private JButton button;
	private JButton arrowButton;
	private JPopupMenu popupMenu;
	private Action[] actions;

        public JMenuButton() {
            actions = new Action[0];
            initGui();
        }

	/**
	 * 
	 */
	public JMenuButton(String text, Action[] menuItems) {
		this(text, null, menuItems);
	}
	
	/**
	 * 
	 */
	public JMenuButton(Icon icon, Action[] menuItems) {
		this(null, icon, menuItems);
	}
	
	/**
	 * 
	 */
	public JMenuButton(String text, Icon icon, Action[] menuItems) {
		actions = menuItems == null ? new Action[0] : menuItems;
		if (text == null && icon != null) {
			button = new JButton(icon);
		} else if (text != null && icon == null) {
			button = new JButton(text);
		} else {
			button = new JButton(text, icon);
		}
		initGui();
	}
	
	private void initGui() {
		//if the button is null, create an empty button
		if (button == null) {
			button = new JButton("");
		}
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setOpaque(isOpaque());
		
		//create the arrowButton
		arrowButton = new JButton(MenuArrowIcon.INSTANCE);
		arrowButton.setMargin(new Insets(0, 1, 0, 1));
		arrowButton.setBorder(BorderFactory.createEmptyBorder());
		arrowButton.setOpaque(isOpaque());
		arrowButton.setPreferredSize(new Dimension(11, 11));
		
		//set a border for this whole panel such as mimicing the buttons
		setBorder(BorderFactory.createRaisedBevelBorder());

                if( actions!=null && actions.length>0 ) {
                    //create the popup menu
                    popupMenu = new JPopupMenu();
                    for (int i=0; i<actions.length; i++) {
                            popupMenu.add(actions[i]);
                    }
		}
		//put them both on the panel
		setLayout(new BorderLayout());
		add(button, BorderLayout.CENTER);
		add(arrowButton, BorderLayout.EAST);
		
		//add a listener to the main button so that it launches the first action when clicked
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//fire the first action in the popup menu
				if (actions.length > 0) {
					actions[0].actionPerformed(e);
				}
			}
		});
		
		//add a listener to the arrow button to show the drop down
		final Component me = this;
		arrowButton.addMouseListener(new MouseAdapter() {

                    public void mousePressed(MouseEvent e) {
                        maybeShowPopup(e);
                    }

                    public void mouseReleased(MouseEvent e) {
                        maybeShowPopup(e);
                    }

                    private void maybeShowPopup(MouseEvent e) {
                        Point p = new Point(getLocation());

                        SingleFrameApplication app = Application.getInstance(SingleFrameApplication.class);
                        //Not sure if this next line is doing anything...weird
                        SwingUtilities.convertPoint(me, p, app.getMainFrame());

                        //Component source = e.getComponent();
                        Component source = arrowButton;

                        popupMenu.show(source, p.x + source.getWidth(), p.y);
                    }
        	  });
	}
	
	private static class MenuArrowIcon implements Icon, UIResource, Serializable  {

            private static final MenuArrowIcon INSTANCE = new MenuArrowIcon();

            private static final int WIDTH  = 4;
            private static final int HEIGHT = 8;

            public void paintIcon( Component c, Graphics g, int x, int y ) {
                    Color origColor = g.getColor();
                    g.setColor(c.getForeground());

                    //draw a "down" triangle by drawing 3 lines -- the first is 5 pixels wide, the second 3, and the last one 1.
                    //disregard clipping regions.
                    //oh, and the y offset of the first line is c.getHeight()/2 - 2;
                    int yoffset = (c.getHeight()/2) - 1;
                    int xoffset = (c.getWidth()/2) - 2;
                    g.drawLine( xoffset, yoffset, xoffset+5, yoffset );
                    g.drawLine( xoffset+1, yoffset+1, xoffset+4, yoffset+1 );
                    g.drawLine( xoffset+2, yoffset+2, xoffset+3, yoffset+2 );
                    g.setColor(origColor);
            }

            public int getIconWidth()	{ return WIDTH; }
            public int getIconHeight() { return HEIGHT; }
      }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
        if( actions!=null && actions.length>0 ) {
            //create the popup menu
            popupMenu = new JPopupMenu();
            for (int i=0; i<actions.length; i++) {
                    popupMenu.add(actions[i]);
            }
        }
    }

    public void addActions( Action... actions ) {
        setActions( (Action[])actions );
    }

    public String getText() {
        return button.getText();
    }

    public void setText(String text) {
        button.setText(text);
    }

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setOpaque(boolean)
	 */
        @Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(isOpaque);
		if (button != null) {
			button.setOpaque(isOpaque);
		}
		
		if (arrowButton != null) {
			arrowButton.setOpaque(isOpaque);
		}
	}
}
