/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.io.Reader;

/**
 *
 * @author Sorrentino
 */
public interface SwingComponent {

    public void setApplication(SwingApplication application);

    public String getName();
    
    public Reader getContentToRender() throws Exception;

}
