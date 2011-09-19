package org.swixml.processor;

import java.awt.LayoutManager;

import org.swixml.LogAware;
import org.swixml.Parser;
import org.swixml.script.ScriptService;

public class ScriptTagProcessor implements TagProcessor, LogAware {

    public static final TagProcessor instance = new ScriptTagProcessor();

    @Override
	public boolean process(Parser p, Object obj, org.w3c.dom.Element child,	LayoutManager layoutMgr) throws Exception {
        if( !Parser.TAG_SCRIPT.equalsIgnoreCase(child.getLocalName())) return false;

        logger.info( String.format("script processor [%s]", child.getTextContent()));
        
        ScriptService s = p.engine.getScript();
        
        if( s!=null ) {
        	
        	s.eval(child.getTextContent());
        }
        
		return true;
	}

}
