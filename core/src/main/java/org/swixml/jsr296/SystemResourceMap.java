/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.jsr296;

import java.util.Set;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author softphone
 */
public class SystemResourceMap extends ResourceMap {

    public SystemResourceMap() {
        super((ResourceMap)null, ClassLoader.getSystemClassLoader(), "system");
    }

    @Override
    protected Set<String> getResourceKeySet() {
        java.util.Set result = System.getProperties().keySet();
        return result;
    }

    @Override
    protected boolean containsResourceKey(String key) {
        return System.getProperties().containsKey(key);
    }

    @Override
    protected Object getResource(String key) {
        return System.getProperty(key);
    }

    @Override
    protected void putResource(String key, Object value) {
        // Read Only
    }

}
