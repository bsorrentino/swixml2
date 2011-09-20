package examples.layout;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.swixml.jsr296.SwingApplication;

public class LayoutExample extends SwingApplication {

    @Override
    protected void startup() {

		try {
			JDialog frame = super.render(new JDialog(), "examples/layout/LayoutFrame.xml");

			super.show(frame);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "error on startup " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			
			// Exit to application
			//exit();
		}
    	
    }

    public static void main( String[] args ) {
        
    	Application.launch(LayoutExample.class, args);
    }

}
