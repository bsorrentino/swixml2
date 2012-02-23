/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.script;

import org.swixml.dom.Attribute;

/**
 *
 * @author softphone
 */
public class ScriptUtil {

    public final static String PREFIX = "script";


    protected ScriptUtil() {
    }
    
    
    /**
     * 
     * @param attr
     * @return if attr is a script attribute
     */
    public static boolean isScriptAttribute( Attribute attr ) {
    
        return PREFIX.equalsIgnoreCase(attr.getPrefix());
 
    }
    
}
