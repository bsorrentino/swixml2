/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.contrib;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import org.jdesktop.application.Application;
import org.junit.Test;
import org.swixml.jsr.widgets.JDialogEx;
import org.swixml.jsr296.SwingApplication;

public class GradientApp extends SwingApplication{

    public static class Dialog extends JDialogEx {
	
    
	public Dialog(){
		 JPanel container=new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -7419521311425402231L;

			@Override
			protected void paintComponent( Graphics grphcs ) 
			{
			    Graphics2D g2d = (Graphics2D) grphcs;
		        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                RenderingHints.VALUE_ANTIALIAS_ON);

		        GradientPaint gp = new GradientPaint(0, 0, 
		                getBackground().brighter().brighter(), 0, getHeight(),
		                getBackground().darker().darker());
		        
		        g2d.setPaint(gp);
		        g2d.fillRect(0, 0, getWidth(), getHeight());
		        
		       
			 
		        this.getRootPane().setOpaque( true );
		        super.paintComponents(grphcs);
			   
			}
		};
		
		
		this.setContentPane(container);
		
	}
    }

        @Override
    protected void startup() {
         

        try {
            
            System.setProperty(Application.AUTO_INJECTFIELD, String.valueOf(true));
            
            Dialog dlg = super.render(new Dialog());
            
            show(dlg);


        } catch (Exception ex) {
            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        Application.launch(GradientApp.class, args);

    }

    @Test
    public void dummy() {
    }
    

}
