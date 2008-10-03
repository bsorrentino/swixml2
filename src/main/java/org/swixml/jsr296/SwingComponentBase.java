package org.swixml.jsr296;

import java.awt.Container;

import javax.swing.Action;

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
			
	}

	public BindingGroup getBindingGroup() {
		return bindingGroup;
	}

	public Action getComponentAction(String name) {
		final ApplicationActionMap actionMap = application.getContext().getActionMap(this);
		
		return actionMap.get(name);
	}

	public <T extends Container> T render(String resource) throws Exception {
		
		return SwingApplication.render(engine, resource);
	}

}
