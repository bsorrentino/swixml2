package org.swixml;

import org.jdom.Attribute;

/**
 * call to adapt old converter stategy to the new one
 * 
 * @author softphone
 *
 */
@Deprecated
public abstract class ConverterAdapter implements Converter<Object> {

	@Override
	public Object convert(Class<Object> type, Attribute attr, SwingEngine<?> engine) throws Exception {
		return convert(type, attr, (engine==null) ? (Localizer)null : engine.getLocalizer());
	}

}
