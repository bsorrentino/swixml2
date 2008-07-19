/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.variables;

import java.awt.Dimension;
import org.junit.Assert;
import org.junit.Test;
import org.swixml.VariableLibrary;

/**
 *
 * @author sorrentino
 */
public class VariablesTest {

    
    @Test
    public void getVariable() {
        
        VariableLibrary vars = VariableLibrary.getInstance();
        
        vars.putVariable( "size", new Dimension(100,100));
        
        Assert.assertTrue( "isVariablePattern", vars.isVariablePattern("${size}") );
       
        Object size = vars.getVariable("${size}");
        
        Assert.assertTrue(  size instanceof Dimension );
    }
}
