package examples.slider;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

public class SliderDialog extends JDialog {

	 int transparency;
	 
	 public Action transparencyAction = new AbstractAction() {
	       public void actionPerformed(ActionEvent e) {

	           // Read the transparency to use
	           //
	           System.out.println("DEBUG: This event is not called. It is a swixml bug. Open a ticket");
	       }
	   };

	public final int getTransparency() {
		return transparency;
	}

	public final void setTransparency(int value) {
		
        System.out.println("DEBUG: This setter is automatically called. " + value );
		
		this.transparency = value;
	}
	   
	   
}
