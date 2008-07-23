package org.swixml;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.WARNING;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {

	public final static Logger logger = Logger.getLogger("SWIXML");
	
	static {
		initLog();
	}

	private static void initLog() {
		logger.setLevel(ALL);
		
		try {
			java.util.logging.FileHandler h = new java.util.logging.FileHandler( "swixml.log" );
			h.setFormatter( new SimpleFormatter() );
			logger.addHandler(h);
		} catch (Exception e) {
			logger.log( WARNING, "file handler error", e  );	
		}
		
	}


}
