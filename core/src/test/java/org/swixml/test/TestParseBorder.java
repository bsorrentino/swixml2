/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import junit.framework.Assert;

import org.junit.Test;
import static org.swixml.converters.PrimitiveConverter.getConstantValue;



/**
 *
 * @author sorrentino
 */
public class TestParseBorder {

/*
  public static void main( String args[] ) {
    System.out.println(
      getConstantValue( TitledBorder.class, "TitledBorder.CENTER", TitledBorder.DEFAULT_POSITION ));

    System.out.println(
      getConstantValue( BorderLayout.class, "CENTER", BorderLayout.CENTER));
  }
*/
	
	
    @Test
    public void testCompundBorder() {
        String input = "CompoundBorder( EmptyBorder(2,2,2,2) , MatterBorder(1,1,1,1,blue) )";

        Pattern p = Pattern.compile( "CompoundBorder[(][\\s]*(.*)[\\s]*[,][\\s]+(.*)[\\s]*[)]");
        
        Matcher m = p.matcher(input);
        
        if( m.matches() ) {
            
            System.out.printf( "CompoundBorder - boder1 = %s border2=%s\n", m.group(1), m.group(2));
        }
    }

    @Test
    public void testBorders() {
        Pattern p = Pattern.compile( "(\\w*)(?:[(](.+)*[)])?");

        {
	        String input = "EtchedBorder";
	
	        Matcher m = p.matcher(input);
	
	        Assert.assertTrue(m.matches() );
	        Assert.assertEquals( "EtchedBorder", m.group(1) );
        }

        {
            String input = "EtchedBorder()";

            Matcher m = p.matcher(input);

            Assert.assertTrue(m.matches() );
            Assert.assertEquals( "EtchedBorder", m.group(1) );
            }
    }
    
    @Test
    public void testTitledBorder() {

        Pattern p = Pattern.compile( "(\\w*)(?:[(](.+)*[)])?");


        String input = "TitledBorder( Hello ,CENTER ,RIGHT )";

        Matcher m = p.matcher(input);

        Assert.assertTrue(m.matches() );

        int groupcount = m.groupCount();
        Assert.assertEquals(2, groupcount);
        Assert.assertEquals("TitledBorder", m.group(1));
         

        String [] tt = m.group(2).split(",");

        Assert.assertEquals(3, tt.length);
        Assert.assertEquals("Hello", tt[0].trim());
        Assert.assertEquals("CENTER", tt[1].trim());
        Assert.assertEquals("RIGHT", tt[2].trim());

        
        Assert.assertTrue( tt.length > 0);
        
/*
        BorderConverter c = new BorderConverter();

        org.jdom.Attribute a = new org.jdom.Attribute("border", input);

        Border b = (Border) c.convert(Border.class, a, null);

        Assert.assertNotNull(b);
*/
    }


}
