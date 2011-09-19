package org.swixml.script;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.swixml.LogAware;
import org.swixml.SwingEngine;

@SuppressWarnings("serial")
public class ScriptAction extends AbstractAction implements LogAware {

	final ScriptService service;
	final String methodName;
	
	public ScriptAction( SwingEngine<?> engine, String methodName) throws NoSuchMethodException {
		this.methodName = methodName;
		
		service = engine.getScript();
		if( service == null ) {
			logger.warning("script service is null. script action ignored!");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		if( service == null ) {
			return;
		}
		
		service.invokeFunctionSafe(methodName, e);
	}
}
