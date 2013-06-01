/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swixml.tool;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import org.jdesktop.observablecollections.ObservableCollections;

/**
 *
 * @author bsorrentino
 */
public class ClassloaderBuilder {
   
    private java.util.List<URL> urlList = ObservableCollections.observableList(new java.util.ArrayList<URL>());

    public ClassLoader build() {
        
        if( urlList.isEmpty() ) {
            return Thread.currentThread().getContextClassLoader();
        }
        
        final URL urls[] = new URL[ urlList.size() ];
        final ClassLoader loader = new URLClassLoader( urlList.toArray(urls) );
    
        urlList.clear();
        
        return loader;
    }
    
    public final void add( File file ) throws MalformedURLException {
        this.add( file.toURI().toURL() );
    }
    
    public final void add( URL uri ) {
        if( !urlList.contains(uri)) {
            urlList.add(uri);
        }
    }
    
    public List<URL> getUrls() {
        return urlList;
    }
    
}
