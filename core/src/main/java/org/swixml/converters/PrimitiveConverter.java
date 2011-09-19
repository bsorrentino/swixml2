/*--
 $Id: PrimitiveConverter.java,v 1.1 2004/03/01 07:56:02 wolfpaulus Exp $

 Copyright (C) 2003-2007 Wolf Paulus.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions, and the disclaimer that follows
 these conditions in the documentation and/or other materials provided
 with the distribution.

 3. The end-user documentation included with the redistribution,
 if any, must include the following acknowledgment:
        "This product includes software developed by the
         SWIXML Project (http://www.swixml.org/)."
 Alternately, this acknowledgment may appear in the software itself,
 if and wherever such third-party acknowledgments normally appear.

 4. The name "Swixml" must not be used to endorse or promote products
 derived from this software without prior written permission. For
 written permission, please contact <info_AT_swixml_DOT_org>

 5. Products derived from this software may not be called "Swixml",
 nor may "Swixml" appear in their name, without prior written
 permission from the Swixml Project Management.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE SWIXML PROJECT OR ITS
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 ====================================================================

 This software consists of voluntary contributions made by many
 individuals on behalf of the Swixml Project and was originally
 created by Wolf Paulus <wolf_AT_swixml_DOT_org>. For more information
 on the Swixml Project, please see <http://www.swixml.org/>.
*/

package org.swixml.converters;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreeSelectionModel;

import org.swixml.ConverterAdapter;
import org.swixml.Localizer;
import org.swixml.LogUtil;
import org.swixml.Parser;
import org.swixml.dom.Attribute;

/**
 * The <code>PrimitiveConverter</code> class defines a converter that creates primitive objects (wrapper),
 * based on a provided input Class and String.
 *
 * @author <a href="mailto:wolf@wolfpaulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 * @see org.swixml.ConverterLibrary
 */

public class PrimitiveConverter extends ConverterAdapter implements SwingConstants, ScrollPaneConstants, KeyEvent, InputEvent {

  public static final PrimitiveConverter instance = new PrimitiveConverter();
  
  /** converter's return type */
  public static final Class<?> TEMPLATE = Object.class;
  /** map contains all constant provider types */
  private static Map<String, Class<?>> dictionaries = new HashMap<String, Class<?>>();
  /**
   * Static Initializer, setting up the initial constant providers
   */
  static {
    PrimitiveConverter.addConstantProvider( JTabbedPane.class );
    PrimitiveConverter.addConstantProvider( JScrollPane.class );
    PrimitiveConverter.addConstantProvider( JSplitPane.class );
    PrimitiveConverter.addConstantProvider( GridBagConstraints.class );
    PrimitiveConverter.addConstantProvider( FlowLayout.class );
    PrimitiveConverter.addConstantProvider( ListSelectionModel.class );
    PrimitiveConverter.addConstantProvider( TreeSelectionModel.class );
    PrimitiveConverter.addConstantProvider( JDialog.class );
    PrimitiveConverter.addConstantProvider( JFrame.class );
    PrimitiveConverter.addConstantProvider( TitledBorder.class );
    PrimitiveConverter.addConstantProvider( JComponent.class );
    
  }
  /**
   * Converts String into java primitive type
   * @param type <code>Class</code> target type
   * @param attr <code>Attribute</code> value field needs to provide convertable String
   * @param localizer <code>Localizer</code>
   * @return <code>Object</code> primitive wrapped into wrapper object
   * @throws NoSuchFieldException in case no class a field matching field name had been regsitered with this converter
   * @throws IllegalAccessException if a matching field can not be accessed
   */
  public static Object conv( final Class<?> type, final Attribute attr,  final Localizer localizer ) throws NoSuchFieldException, IllegalAccessException {
    Attribute a = (Attribute) attr.clone();
    Object obj = null;
    if ( Parser.LOCALIZED_ATTRIBUTES.contains( a.getLocalName().toLowerCase() ))
         a.setValue( localizer.getString( a.getValue() ));

    try {
      if( type.isPrimitive() ) {
          if (Boolean.TYPE.equals( type )) { obj = Boolean.valueOf(a.getBooleanValue()); }
          else if (Integer.TYPE.equals( type )) { obj = Integer.valueOf( a.getIntValue() ); } 
          else if (Long.TYPE.equals( type )) { obj = Long.valueOf( a.getLongValue() ); } 
          else if (Float.TYPE.equals( type )) { obj = Float.valueOf( a.getFloatValue() ); } 
          else if (Double.TYPE.equals( type )) { obj = Double.valueOf( a.getDoubleValue() ); }   	  
      }
      else {
          if (Boolean.class.equals( type )) { obj = Boolean.valueOf(a.getBooleanValue()); }
          else if (Integer.class.equals( type )) { obj = Integer.valueOf( a.getIntValue() ); } 
          else if (Long.class.equals( type )) { obj = Long.valueOf( a.getLongValue() ); } 
          else if (Float.class.equals( type )) { obj = Float.valueOf( a.getFloatValue() ); } 
          else if (Double.class.equals( type )) { obj = Double.valueOf( a.getDoubleValue() ); }   	  
    	  
      }
    } catch (NumberFormatException e) { 
      // intent. empty
    } finally {
      if (obj==null) {
        try {
          String s = a.getValue();
          int k = s.indexOf( '.' );
          Class<?> pp = dictionaries.get( s.substring( 0, k ) );
          obj = pp.getField( s.substring( k + 1 ) ).get( pp );
        } catch (Exception ex) {
          //
          //  Try to find the given value as a Constant in SwingConstants
          //
          obj = PrimitiveConverter.class.getField( a.getValue() ).get( PrimitiveConverter.class );
        }
      }
    }

    return obj;
  }
  /**
   * retrieve a constant field from class
   *
   * @param clazz   Constant Provider
   * @param value   Constant name
   * @param def     default value
   * @return
   */
  public static <T> T getConstantValue( Class<?> clazz, String value, T def ) {
      if( clazz == null ) throw new IllegalArgumentException( "class is null!");
      if( value == null ) throw new IllegalArgumentException( "value is null!");

      Matcher m = p.matcher(value.trim());

      if( !m.matches() ) {
          LogUtil.logger.warning(  String.format( "value [%s] is not valid constant expression. Default is returned!", value ));
          return def;
      }

      String cn =  m.group(1);

      if( cn!=null && !clazz.getSimpleName().equalsIgnoreCase(cn) ) {
          LogUtil.logger.warning(  String.format( "class defined in constant expression [%s] doesn't match with given class [%s]. Ignored!", cn, clazz.getSimpleName() ));

      }

      String cv = m.group(2);
      try {
          Field f = clazz.getField(cv);

          Object vv = f.get(null);

          if( vv==null ) {
              LogUtil.logger.warning(String.format("constant field [%s] in class [%s] is null. Default is returned!", cv, clazz.getSimpleName()));
              return def;
          }

          return (T) vv;

      } catch (NoSuchFieldException ex) {
          LogUtil.logger.warning(String.format("constant field [%s] not found in class [%s] Default is returned!", cv, clazz.getSimpleName()));
          return def;
      } catch (Exception ex) {
          LogUtil.logger.log(Level.WARNING, String.format("exception reading contant field [%s] in class [%s]. Default is returned!", cv, clazz.getSimpleName()), ex);
          return def;
      }

  }

  protected PrimitiveConverter() {
  }

/**
   * Converts String into java primitive type
   * @param type <code>Class</code> target type
   * @param attr <code>Attribute</code> value field needs to provide convertable String
   * @return <code>Object</code> primitive wrapped into wrapper object
   * @throws Exception
   */
  public Object convert( final Class<?> type, final Attribute attr, final Localizer localizer ) throws Exception {
    return PrimitiveConverter.conv( type, attr, localizer );
  }

  /**
   * A <code>Converters</code> conversTo method informs about the Class type the converter
   * is returning when its <code>convert</code> method is called
   * @return <code>Class</code> - the Class the converter is returning when its convert method is called
   */
  public Class<?> convertsTo() {
    return TEMPLATE;
  }

  /**
   * Adds a new class or interface to the dictionary of primitive providers.
   * @param clazz <code>Class</code> providing primitive constants / public (final) fields
   */
  public static void addConstantProvider(final Class<?> clazz) {
    dictionaries.put( clazz.getSimpleName(), clazz );
  }

  private static Pattern p = Pattern.compile( "(?:(\\w+)[.])?(\\w+)");




}
