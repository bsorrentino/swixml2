package examples.script;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JToggleButton;

import org.jdesktop.application.Action;

@SuppressWarnings("serial")
public class ScriptDialog extends JDialog {
	JToggleButton toggleButton;

	@Action
	public void onCLick( ActionEvent e ) {
	
		System.out.println( e );
	}

}
