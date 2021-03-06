package examples;

import javax.swing.JFrame;

import org.swixml.jsr296.SWIXMLApplication;


public class BindingExamplesApplication extends SWIXMLApplication {

	public static void main(String args []) {
		SWIXMLApplication.launch(BindingExamplesApplication.class, args);
	}
	
	
	@Override
	protected void startup() {

		try {
/*			
			JDialog dialog = render( new LoginDialog(), "examples/SimpleDialog.xml"); 
			
			show( dialog );
*/
			JFrame frame = render( new BindingExamplesFrame(), "examples/BindingExamples.xml" );

			show( frame );
			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}
