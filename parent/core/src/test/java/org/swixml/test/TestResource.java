package org.swixml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.junit.Test;
import org.swixml.test.data.SimpleApplication;
import org.swixml.test.data.SimpleDialog;
import org.swixml.test.data.SimpleDialog1;

//org.junit.Ignore
public class TestResource {

	private static void print( String msg, ResourceMap rm ) {
		System.out.printf( "[%s] resource map  {\n", msg);
		for( String r : rm.keySet()) {
			System.out.printf("\t[%s]\n", r);
		}
		System.out.println("}");
	}

	private void loadResourceTask()  {
		SimpleApplication app = Application.getInstance(SimpleApplication.class);
	
		
		assertNotNull( app );

		ApplicationContext ctx = app.getContext();
		
		assertNotNull( ctx );

		ResourceMap mainResMap = ctx.getResourceMap();

		print( "main", mainResMap );
		
		assertNotNull( mainResMap );			
		assertNotNull(app.getEntry());
		assertEquals( "test", app.getEntry());
		
		{
			SimpleDialog dlg = new SimpleDialog();
			
			mainResMap.injectFields(dlg);
			
			assertNotNull( dlg.entry1 );	
			
			ResourceMap rm = app.getContext().getResourceMap(SimpleDialog.class);
	
			print( "SimpleDialog", mainResMap );
	
			rm.injectComponents(dlg);
			
			assertEquals( "test", dlg.myLabel.getText() );
		}
		
		{
			SimpleDialog1 dlg = new SimpleDialog1();
			
			mainResMap.injectFields(dlg);
			
			assertNotNull( dlg.entry1 );	
			
			ResourceMap rm = app.getContext().getResourceMap(SimpleDialog1.class);

			rm.injectComponents(dlg);
			
			assertEquals( "test1", dlg.myLabel.getText() );
		}
		
	}
	
	@Test
	public void loadResource() throws Exception {
		System.setProperty( Application.AUTO_INJECTFIELD, "true" );
		
		Application.launch(SimpleApplication.class, new String[0]);
		
		SwingUtilities.invokeAndWait( new Runnable() {

			public void run() {
				loadResourceTask();
				
			}
			
		});

	}
	
	
}
