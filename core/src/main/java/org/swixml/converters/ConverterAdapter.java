package org.swixml.converters;

import org.swixml.Converter;
import org.swixml.Localizer;
import org.swixml.SwingEngine;
import org.swixml.dom.Attribute;

/**
 * call to adapt old converter stategy to the new one
 * 
 * @author softphone
 *
 */
public abstract class ConverterAdapter implements Converter<Object> {
    
        @Deprecated	
        public abstract Object convert( final Class<?> type, final Attribute attr, final Localizer localizer ) throws Exception;

	@Override
	public Object convert(Class<?> type, Attribute attr, SwingEngine<?> engine) throws Exception {
		return convert(type, attr, (engine==null) ? (Localizer)null : engine.getLocalizer());
	}

}
