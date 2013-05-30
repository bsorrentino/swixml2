package examples.slider;

import javax.swing.JDialog;

import org.swixml.jsr296.SWIXMLApplication;

public class SliderSampleApplication extends SWIXMLApplication {

	public static void main(String args []) {
		SWIXMLApplication.launch(SliderSampleApplication.class, args);
	}

	@Override
	protected void startup() {

		try {

			JDialog dialog = render( new SliderDialog() );

			show( dialog );

		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
	}

}
