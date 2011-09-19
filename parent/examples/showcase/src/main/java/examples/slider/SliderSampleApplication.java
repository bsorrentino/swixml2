package examples.slider;

import javax.swing.JDialog;

import org.swixml.jsr296.SwingApplication;

public class SliderSampleApplication extends SwingApplication {

	public static void main(String args []) {
		SwingApplication.launch(SliderSampleApplication.class, args);
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
