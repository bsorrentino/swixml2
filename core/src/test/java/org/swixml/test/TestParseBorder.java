/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.junit.Test;
import org.hamcrest.core.*;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.swixml.converters.BorderConverter;



/**
 *
 * @author sorrentino
 */
public class TestParseBorder {

    static final Pattern borderPattern = Pattern.compile( "(\\w+)(?:[(]([\\w, -]+)*[)])?");
    static final Pattern compoundBorderPattern = Pattern.compile("CompoundBorder[(][\\s]*(.*)[\\s]*[,][\\s]+(.*)[\\s]*[)]");

    @Test
    public void testConverters() throws Exception {

        {
        String value = "CompoundBorder(EmptyBorder(1,1,1,1), EtchedBorder)";
        
        BorderConverter c = new BorderConverter();

        Border b = (Border) c.convert(value, Border.class, null, null);

        Assert.assertThat( b, IsNull.notNullValue() );
        
        }
        
        {
        String value = "TitledBorder(test,LEFT,TOP,Arial-BOLD-10)";
        
        BorderConverter c = new BorderConverter();

        Border b = (Border) c.convert(value, Border.class, null, null);

        Assert.assertThat( b, IsNull.notNullValue() );
        Assert.assertThat( b, IsInstanceOf.instanceOf(TitledBorder.class) );
        
        TitledBorder tb = (TitledBorder) b;
                
        Assert.assertThat( tb.getTitleFont(), IsNull.notNullValue() );
        Assert.assertThat( tb.getTitleFont().getName().trim(), IsEqual.equalTo("Arial") );
        
        
        }
        
        {
        String value = "CompoundBorder(EmptyBorder(1,1,1,1), TitledBorder(test,LEFT,TOP,Arial-BOLD-10))";
        
        BorderConverter c = new BorderConverter();

        Border b = (Border) c.convert(value, Border.class, null, null);

        Assert.assertThat( b, IsNull.notNullValue() );
        
        }
    }
	
	
    @Test
    public void testCompundBorder() {
        testCompundBorder("CompoundBorder(EmptyBorder(2,2,2,2), MatterBorder(1,1,1,1,blue))");
        testCompundBorder("CompoundBorder(EmptyBorder(1,1,1,1), TitledBorder(test,LEFT,TOP,Arial-BOLD-10))");

    }
    
    private void testCompundBorder( String input ) {

        Matcher m = compoundBorderPattern.matcher(input);
        
        Assert.assertThat( m, IsNull.notNullValue() );
        Assert.assertThat( m.matches(), Is.is(true) );
        Assert.assertThat( m.groupCount(), IsEqual.equalTo(2) );
        
        if( m.matches() ) {
            
            System.out.printf( "CompoundBorder\n\toutside[%s]\n\tinside[%s]\n", m.group(1), m.group(2));
        }
    }

    
    private void testBorder( String input, String borderType ) {
        
        final Matcher m = borderPattern.matcher(input);

        Assert.assertThat(m.matches(), Is.is(true) );
        Assert.assertThat( borderType, IsEqual.equalTo(m.group(1)) );
    }
    
    private void testBorderNotMatch( String input ) {
        
        final Matcher m = borderPattern.matcher(input);

        Assert.assertThat(m.matches(), Is.is(false) );
    }

    @Test
    public void testBorders() {
        testBorder("EtchedBorder()", "EtchedBorder" );
        testBorder("TitledBorder(Hello,CENTER,RIGHT)", "TitledBorder" );
        testBorder("EmptyBoder(1,1,1,1)", "EmptyBoder" );
        testBorderNotMatch("EmptyBorder(1,1,1,1), TitledBorder(test,LEFT,TOP");
        
    }
    
    @Test
    public void testTitledBorder() {

        String input = "TitledBorder(Hello, CENTER,RIGHT)";

        Matcher m = borderPattern.matcher(input);

        Assert.assertTrue(m.matches() );

        int groupcount = m.groupCount();
        Assert.assertThat(groupcount, IsEqual.equalTo(2));
        Assert.assertThat(m.group(1), IsEqual.equalTo("TitledBorder"));
         

        String [] tt = m.group(2).split(",");

        Assert.assertThat(3, IsEqual.equalTo(tt.length));
        Assert.assertThat("Hello", IsEqual.equalTo(tt[0].trim()));
        Assert.assertThat("CENTER", IsEqual.equalTo(tt[1].trim()));
        Assert.assertThat("RIGHT", IsEqual.equalTo(tt[2].trim()));

        
        Assert.assertThat( tt.length > 0, Is.is(true));
        
    }


}
