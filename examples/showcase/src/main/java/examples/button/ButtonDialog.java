package examples.button;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JToggleButton;

import org.jdesktop.application.Action;

@SuppressWarnings("serial")
public class ButtonDialog extends JDialog {
	JToggleButton toggleButton;

	@Action
	public void onCLick( ActionEvent e ) {
	
		System.out.println( e );
	}

	@Action
	public void close() {
	
		System.out.println( "close" );
	}
}
