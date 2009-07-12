/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


/**
 *
 * @author sorrentino
 */
public class TestParseBorder {

	
	
	@Test
    public void test1() {
        String input = "CompoundBorder( EmptyBorder(2,2,2,2) , MatterBorder(1,1,1,1,blue) )";
        
        Pattern p = Pattern.compile( "CompoundBorder[(][\\s]*(.*)[\\s]*[,][\\s]+(.*)[\\s]*[)]");
        
        Matcher m = p.matcher(input);
        
        if( m.matches() ) {
            
            System.out.printf( "CompoundBorder - boder1 = %s border2=%s\n", m.group(1), m.group(2));
        }
    }
}
