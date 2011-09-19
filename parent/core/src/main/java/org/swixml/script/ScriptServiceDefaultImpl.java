package org.swixml.script;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.swixml.LogAware;

public class ScriptServiceDefaultImpl implements ScriptService, LogAware {

	public static final String COMMON_SCRIPT_CONFIG = "common";

	private final ScriptEngine engine;
	
	
	/**
	 * The configuration entry should be:<br/>
	 * <pre>
	 * script.engine (default 'JavaScript')
	 * script.common (default 'scripting/common.js')
	 * script.main (default 'scripting/main.js')
	 * script.development (default '${script.main}')
	 * 
	 * </pre>
	 * 
	 * @param scriptConf configuration instance
	 * @throws ScriptException
	 */
	public ScriptServiceDefaultImpl() throws  ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		
		final String script_engine =  "JavaScript";
		
		engine = manager.getEngineByName(script_engine);
		if( null==engine ) throw new IllegalStateException( "impossible initialize script engine!");
		
		final String common_script =  "scripting/commons.js";
		
		loadScripts(common_script);
		
	}

	/**
	 * 
	 * @param scriptResource
	 * @throws IOException 
	 * @throws ScriptException 
	 * @throws Exception
	 */
	private void loadScripts( String scriptResource )  {
		
		java.util.Enumeration<java.net.URL> resources = null;;
		try {
			resources = getClass().getClassLoader().getResources(scriptResource);
		} catch (IOException e1) {
			logger.log( Level.WARNING, String.format( "error loading resources [%s]", scriptResource), e1);				
		}
		
		while ( resources.hasMoreElements() ) {
			
			java.net.URL resourceURL = resources.nextElement();

			java.io.InputStream is;
			try {
				is = resourceURL.openStream();
				if( is!=null )	{
					engine.eval( new java.io.InputStreamReader( is ) );
				}
				else {
					logger.warning( String.format( "script resource [%s] not found!", scriptResource));			
				}
			} catch (IOException e) {
				logger.log( Level.WARNING, String.format( "error loading resource [%s]", resourceURL), e);				
			}
			catch( ScriptException ex ) {
				logger.log( Level.WARNING, String.format( "error evaluating resource [%s]", resourceURL), ex);				
				
			}
			
		}
	}
	
	/**
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	public Object invokeFunctionSafe( String method, Object... args )  {
		
		try {
			
			Invocable inv = (Invocable) engine;

			return inv.invokeFunction(method, (Object[])args);
					
		} catch (Exception e) {
			
			logger.log( Level.WARNING, String.format( "invocation of method [%s] has failed!", method), e);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param method
	 * @param args
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws ScriptException 
	 */
	public Object invokeFunction( String method, Object... args ) throws ScriptException, NoSuchMethodException  {
		
		Object result = null;

		Invocable inv = (Invocable) engine;

		result = inv.invokeFunction(method, (Object[])args);
					
		return result;
	}


	public Bindings createBindings() {
		return engine.createBindings();
	}


	public Object eval(Reader reader, Bindings n) throws ScriptException {
		return engine.eval(reader, n);
	}


	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {
		return engine.eval(reader, context);
	}


	public Object eval(Reader reader) throws ScriptException {
		return engine.eval(reader);
	}


	public Object eval(String script, Bindings n) throws ScriptException {
		return engine.eval(script, n);
	}


	public Object eval(String script, ScriptContext context)
			throws ScriptException {
		return engine.eval(script, context);
	}


	public Object eval(String script) throws ScriptException {
		return engine.eval(script);
	}


	public Object get(String key) {
		return engine.get(key);
	}


	public Bindings getBindings(int scope) {
		return engine.getBindings(scope);
	}


	public ScriptContext getContext() {
		return engine.getContext();
	}


	public ScriptEngineFactory getFactory() {
		return engine.getFactory();
	}


	public void put(String key, Object value) {
		engine.put(key, value);
	}


	public void setBindings(Bindings bindings, int scope) {
		engine.setBindings(bindings, scope);
	}


	public void setContext(ScriptContext context) {
		engine.setContext(context);
	}


	
}
