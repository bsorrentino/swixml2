package org.swixml.processor;

import java.awt.LayoutManager;

import org.jdom.Element;
import org.swixml.LogAware;
import org.swixml.Parser;
import org.swixml.script.ScriptService;

public class ScriptTagProcessor implements TagProcessor, LogAware {

    public static final TagProcessor instance = new ScriptTagProcessor();

    @Override
	public boolean process(Parser p, Object obj, Element child,	LayoutManager layoutMgr) throws Exception {
        if( !Parser.TAG_SCRIPT.equalsIgnoreCase(child.getName())) return false;

        logger.info( String.format("script processor [%s]", child.getText()));
        
        ScriptService s = p.engine.getScript();
        
        if( s!=null ) {
        	
        	s.eval(child.getText());
        }
        
		return true;
	}

}
