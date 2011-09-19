package org.swixml.factory;

import java.awt.LayoutManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import org.jdom.Attribute;
import org.jdom.Element;
import org.swixml.Factory;
import org.swixml.LogAware;
import org.swixml.Parser;


public class ScriptFactory implements Factory, LogAware{

	@Override
	public Object create(Object owner, Element element) throws Exception {
		return null;
	}

	@Override
	public Object newInstance(Object... parameter) throws InstantiationException, IllegalAccessException,InvocationTargetException {
		return null;
	}

	@Override
	public Class<?> getTemplate() {
		return Void.class;
	}

	@Override
	public Collection<Method> getSetters() {
		return Collections.emptyList();
	}

	@Override
	public Class<?>[] getPropertyType(Object bean, String name) {
		return null;
	}

	@Override
	public void setProperty(Object bean, Attribute attr, Object value,	Class<?> type) throws Exception {
		
	}

	@Override
	public boolean process(Parser p, Object parent, Element child,	LayoutManager layoutMgr) throws Exception {
		return false;
	}

}
