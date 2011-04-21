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
    public void testTitledBorder() {

        Pattern p = Pattern.compile( "(\\w*)[(](.+)*[)]");


        String input = "TitledBorder( Hello ,CENTER ,RIGHT )";

        Matcher m = p.matcher(input);

        Assert.assertTrue(m.matches() );

        int groupcount = m.groupCount();
        System.out.println("Found "+groupcount+" groups");
 
        for (int i = 0; i <= groupcount; i++) {
            System.out.println("Match "+i+": "+m.group(i));
        }

        String [] tt = m.group(2).split(",");

        for( String t : tt ) {
            System.out.println( "param " + t );
        }

        Assert.assertTrue( tt.length > 0);
        
        Border b;
        switch( tt.length ) {
            case 1:
            b = new TitledBorder( tt[0] );
            break;
            case 2:
            b = new TitledBorder( (Border)null, tt[0], getConstantValue(TitledBorder.class, tt[1], TitledBorder.DEFAULT_JUSTIFICATION), TitledBorder.DEFAULT_POSITION );
            break;
            default:
            {
            int titleJustification =   getConstantValue(TitledBorder.class, tt[1], TitledBorder.DEFAULT_JUSTIFICATION);
            int textPosition = getConstantValue(TitledBorder.class, tt[2], TitledBorder.DEFAULT_POSITION );
            b = new TitledBorder( (Border)null, tt[0],titleJustification, textPosition);
            }
        }

/*
        BorderConverter c = new BorderConverter();

        org.jdom.Attribute a = new org.jdom.Attribute("border", input);

        Border b = (Border) c.convert(Border.class, a, null);

        Assert.assertNotNull(b);
*/
    }

    //@Test
    public void testTitledBorder2() {

        Pattern p = Pattern.compile( "TitledBorder[(][\\s]*([\\w]+)[\\s]*((?:(?:[,][\\s]*)[\\w]+(?:[\\s]*))*)[)]");


        String inputSeq = "TitledBorder( Hello ,CENTER ,RIGHT ,LEFT )";

        Matcher m = p.matcher(inputSeq);

        Assert.assertTrue(m.matches() );

        int index = 0;
        int curPos = 0;
                 while (m.find()) {
                       System.out.println(inputSeq.subSequence(curPos, m.start()).toString());
                       curPos = m.end();
                       index++;
                 }

                 System.out.println(inputSeq.subSequence(curPos, inputSeq.length()).toString());
                 index++;

    }


}
