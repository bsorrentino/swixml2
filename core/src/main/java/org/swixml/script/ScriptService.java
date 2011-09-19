package org.swixml.script;

import javax.script.ScriptException;

/**
 * Script service interface
 * 
 * @author softphone
 *
 */
public interface ScriptService extends javax.script.ScriptEngine {

	/**
	 * script method invocation ignoring exception
	 * 
	 * @param method method name
	 * @param args method arguments
	 * @return function return 
	 */
	 Object invokeFunctionSafe( String method, Object... args ) ;

	 /**
	  * script method invocation
	  * 
	  * @param method  method name
	  * @param args method arguments
	  * @return
	  * @throws ScriptException
	  * @throws NoSuchMethodException
	  */
	 Object invokeFunction( String method, Object... args ) throws ScriptException, NoSuchMethodException  ;
	 
}