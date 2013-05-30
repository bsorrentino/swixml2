package org.swixml.test.render;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.junit.Test;

import org.swixml.jsr296.SWIXMLApplication;
import org.junit.Assert;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestRender {
	private static final int TEST_WIDTH = 300;
	private static final int TEST_HEIGTH = 350;

	static class MyApp extends SWIXMLApplication {

		@Override
		protected void startup() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Test public void renderSize() throws Exception {
		
		SWIXMLApplication app = new MyApp();
		
		MyFrame f = app.render( new MyFrame() );

		Dimension d = new Dimension();
		
		f.getSize(d);
		
		Assert.assertThat(TEST_HEIGTH, equalTo(d.height));
		Assert.assertThat(TEST_WIDTH, equalTo(d.width));
	}
	
	@Test public void renderSize2() throws Exception {
		
		Application.launch(MyApp.class, new String[0]);

		SwingUtilities.invokeAndWait( new Runnable() {
			public void run() {
				MyApp app = Application.getInstance(MyApp.class);
			
				
				try {
					final MyFrame f = app.render( new MyFrame() );
					Dimension d = new Dimension();
					
					f.getSize(d);
					
					Assert.assertThat(TEST_HEIGTH, equalTo(d.height));
					Assert.assertThat(TEST_WIDTH, equalTo(d.width));
					
					//f.setVisible(true);
					
					app.show(f);
					
				} catch (Exception e) {
					Assert.fail( e.getMessage() );
				}
		
			}
		});
		Thread.sleep( 2*1000 );
	}
}
