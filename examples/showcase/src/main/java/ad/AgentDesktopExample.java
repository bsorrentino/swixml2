package ad;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.swixml.jsr296.SWIXMLApplication;

public class AgentDesktopExample extends SWIXMLApplication {

    @Override
    protected void startup() {

		try {
			JDialog dialog = super.render(new JDialog(), "ad/AgentDesktop.xml");

			super.show(dialog);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "error on startup " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			
			// Exit to application
			//exit();
		}
    	
    }

    public static void main( String[] args ) {
        
    	Application.launch(AgentDesktopExample.class, args);
    }

}
