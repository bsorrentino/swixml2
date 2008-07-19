/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sorrentino
 */
public class VariableLibrary {

    private static final String pattern =  "[$][{](.*)[}]";
    
    
    private static VariableLibrary instance = new VariableLibrary();
    private Map<String, Object> variables = new HashMap<String, Object>();
  
    /**
     * Global Variables
     * 
     * @return  The single instance of the VariableLibrary.
     */
    public static synchronized VariableLibrary getInstance() {
        return instance;
    }
    
    protected VariableLibrary() {
    }
    
    public static boolean isVariablePattern( String value ) {
        if( null==value ) return false;
        return Pattern.matches(pattern, value);
    }
    
    public Object getVariable( String name ) {
    
        if( name == null ) return null;
        
        Matcher m = Pattern.compile(pattern).matcher(name);
        
        Object result = null;
        
        
        if( m.matches() && m.groupCount()>0 ) {
            
            String realName = m.group(1);

            result = variables.get(realName);
            
        }
        
        return result;
    }
    
    public final void putVariable( String name, Object value ) {
        variables.put( name, value);
    }

    public final void removeVariable( String name ) {
        variables.remove( name );
    }
    
    public final void sutdown() {
        variables.clear();
    }
}
