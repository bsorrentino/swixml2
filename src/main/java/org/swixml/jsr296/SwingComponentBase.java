package org.swixml.jsr296;

import java.awt.Container;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.beansbinding.BindingGroup;
import org.swixml.SwingEngine;

public class SwingComponentBase implements SwingComponent {

	protected final SwingApplication application;
	
	final BindingGroup bindingGroup = new BindingGroup();
	final SwingEngine engine = new SwingEngine(this);
	
	public SwingComponentBase( SwingApplication application ) {
		this.application = application;
		
		application.getContext().getResourceMap().injectFields(this);
		
		engine.setClassLoader( getClass().getClassLoader() );

	}

	public BindingGroup getBindingGroup() {
		return bindingGroup;
	}

	public Action getComponentAction(String name) {
		final ApplicationActionMap actionMap = application.getContext().getActionMap(this);
		
		return actionMap.get(name);
	}

	public final <T extends Container> T render( Class<T> resultClass, String resource) throws Exception {	
		return SwingApplication.render( resultClass, engine, resource);
	}
	
	protected final JFrame renderFrame(String resource) throws Exception {
		return render( JFrame.class, resource);
	}
	
	protected final JDialog renderDialog(String resource) throws Exception {
		return render( JDialog.class,resource);
	}

}
