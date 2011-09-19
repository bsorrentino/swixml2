package org.swixml;

import org.swixml.dom.Attribute;

/**
 * call to adapt old converter stategy to the new one
 * 
 * @author softphone
 *
 */
public abstract class ConverterAdapter implements Converter<Object> {

	@Override
	public Object convert(Class<?> type, Attribute attr, SwingEngine<?> engine) throws Exception {
		return convert(type, attr, (engine==null) ? (Localizer)null : engine.getLocalizer());
	}

}
