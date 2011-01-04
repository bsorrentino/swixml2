/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;

/**
 *
 * @author softphone
 */
public class JDialogEx extends JDialog {

    protected JButton defaultButton;

    public JDialogEx(Window window, String string, ModalityType mt, GraphicsConfiguration gc) {
    }

    public JDialogEx(Window window, String string, ModalityType mt) {
    }

    public JDialogEx(Window window, String string) {
    }

    public JDialogEx(Window window, ModalityType mt) {
    }

    public JDialogEx(Window window) {
    }

    public JDialogEx(Dialog dialog, String string, boolean bln, GraphicsConfiguration gc) {
    }

    public JDialogEx(Dialog dialog, String string, boolean bln) {
    }

    public JDialogEx(Dialog dialog, String string) {
    }

    public JDialogEx(Dialog dialog, boolean bln) {
    }

    public JDialogEx(Dialog dialog) {
    }

    public JDialogEx(Frame frame, String string, boolean bln, GraphicsConfiguration gc) {
    }

    public JDialogEx(Frame frame, String string, boolean bln) {
    }

    public JDialogEx(Frame frame, String string) {
    }

    public JDialogEx(Frame frame, boolean bln) {
    }

    public JDialogEx(Frame frame) {
    }

    public JDialogEx() {
    }


	@Override
	public void addNotify() {
		
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

        InputMap im = rootPane.getInputMap();
        im.put(enter, "enterAction");
        im.put(escape, "escapeAction");


        if( defaultButton != null )
            rootPane.setDefaultButton(defaultButton);

        ActionMap am = rootPane.getActionMap();

        ApplicationActionMap aam = Application.getInstance().getContext().getActionMap(this);
        
        Action escapeAction = aam.get("escapeAction");
        if( escapeAction != null )  {
            am.put( "escapeAction", escapeAction);
        }
        
        Action enterAction = aam.get("enterAction");
        if( enterAction != null )  {
            am.put( "enterAction", enterAction);
        }
		super.addNotify();
	}


}
