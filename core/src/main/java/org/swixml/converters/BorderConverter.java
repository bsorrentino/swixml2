/*--
 $Id: BorderConverter.java,v 1.1 2004/03/01 07:55:58 wolfpaulus Exp $

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

import static org.swixml.converters.PrimitiveConverter.getConstantValue;

import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.swixml.*;
import org.swixml.dom.Attribute;

/**
 * The <code>BorderConverter</code> class defines a converter that creates Border objects based on a provided String.
 * The BorderConverter internally uses the <code>javax.swing.BorderFactory</code> and its static <i>create</i>.. methods
 * to instatiate differnt kinds of borders, based on the given String.<br>
 * Additional parameters to create a border need to be comma separated and enclosed in parentheses.<br><br>
 * Parameter types get converted through the <code>ConverterLibrary</code>. Therefore, only parameter classes are supported that
 * have registered  converters in the ConverLibrary.
 * <h3>Examples for Valid XML attribute notations:</h3>
 * <pre>
 * <ul>
 * <li>border="MatteBorder(4,4,4,4,red)"</li>
 * <li>border="EmptyBorder(5,5,5,5)"</li>
 * <li>border="TitledBorder(My Title)"</li>
 * <li>border="RaisedBevelBorder"</li>
 * <li>border="TitledBorder(TitledBorder, myTitle, TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM, VERDANA-BOLD-18, blue)"</li>
 * </ul>
 * </pre>
 *
 * @author <a href="mailto:wolf@wolfpaulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 * @see javax.swing.BorderFactory
 * @see javax.swing.border.AbstractBorder
 * @see org.swixml.ConverterLibrary
 */
public class BorderConverter extends AbstractConverter<Border> {
    
    /**
     * converter's return type
     */
    public static final Class<?> TEMPLATE = Border.class;
    /**
     * all methods the BorderFactory provides
     */
    private static final Method[] METHODS = BorderFactory.class.getMethods();
    private static final Pattern compoundBorderPattern = 
                Pattern.compile("CompoundBorder[(][\\s]*(.*)[\\s]*[,][\\s]+(.*)[\\s]*[)]");
    private static final Pattern borderPattern = 
                Pattern.compile( "(\\w+)(?:[(]([\\w, -]+)*[)])?");
    //private Pattern borderPattern = Pattern.compile("(\\w*)(?:[(](.+)*[)])?");

    /**
     * Returns a
     * <code>javax.swing Border</code>
     *
     * @param type
     * <code>Class</code> not used
     * @param attr
     * <code>Attribute</code> value needs to provide Border type name and
     * optional parameter
     * @return
     * <code>Object</code> runtime type is subclass of
     * <code>AbstractBorder</code>
     */
    @Override
    public Border convert(String value, Class<?> type, Attribute attr, SwingEngine<?> engine) throws Exception {

        Matcher m = compoundBorderPattern.matcher(value);

        if (m.matches()) {

            Border outside = convert(type, m.group(1), engine);

            Border inside = convert(type, m.group(2), engine);

            return BorderFactory.createCompoundBorder(outside, inside);
        }

        return convert(type, value, engine);
    }

    /**
     *
     * @param type
     * @param borderString
     * @param engine
     * @return
     */
    protected Border convert(final Class<?> type, final String borderString, SwingEngine<?> engine) {

        Matcher m = borderPattern.matcher(borderString);

        if (!m.matches()) {
            return null;
        }

        int groupCount = m.groupCount();

        String borderType = m.group(1);

        String[] params = new String[0];

        if (groupCount > 1) {
            String p = m.group(2);
            if (p != null) {
                params = p.split(",");
            }
        }

        ConverterLibrary cvtlib = ConverterLibrary.getInstance();

        try {

            if ("TitledBorder".equalsIgnoreCase(borderType)) {
                return convertTitledBorder(cvtlib, engine, params);
            }

            return convertBorder(cvtlib, engine, borderType, params);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't create border, " + borderString, e);
        }
        return null;

    }

    /**
     * A
     * <code>Converters</code> conversTo method informs about the Class type the
     * converter is returning when its
     * <code>convert</code> method is called
     *
     * @return
     * <code>Class</code> - the Class the converter is returning when its
     * convert method is called
     */
    public Class<?> convertsTo() {
        return TEMPLATE;
    }

    /**
     *
     * @param cvtlib
     * @param localizer
     * @param borderType
     * @param params
     * @return
     * @throws Exception
     */
    private Border convertBorder(ConverterLibrary cvtlib, SwingEngine<?> engine, String borderType, String[] params) throws Exception {
        Border border = null;

        Method method = null;


        int pLen = params.length;

        //
        // Special case for single parameter construction, give priority to String Type
        //
        if (pLen == 0) {
            try {

                method = BorderFactory.class.getMethod("create" + borderType);

            } catch (NoSuchMethodException e) {
                // intent. empty
            }

            //if (method == null) pLen = 1 ; // try with empty string
            if (method == null) {
                return null;
            }

        }

        /*
         * if (pLen == 1) { try { method =
         * BorderFactory.class.getMethod("create" + borderType, new
         * Class[]{String.class}); } catch (NoSuchMethodException e) { // no
         * need to do anything here. } }
         */

        for (int i = 0; method == null && i < METHODS.length; i++) {
            if (METHODS[i].getParameterTypes().length == pLen && METHODS[i].getName().endsWith(borderType)) {
                method = METHODS[i];

                for (int j = 0; j < method.getParameterTypes().length; j++) {
                    if (String.class.equals(method.getParameterTypes()[j])) {
                        continue;
                    }
                    if (null == cvtlib.getConverter(method.getParameterTypes()[j])) {
                        method = null;
                        break;
                    }
                }
            }
        }

        if (method != null) {

            Object[] args = new Object[pLen];

            for (int i = 0; i < pLen; ++i) { // fill argument array

                final Converter<?> converter = cvtlib.getConverter(method.getParameterTypes()[i]);

                final String value = params[i].trim();

                if (converter != null) {
                    final Attribute attrib = new Attribute(String.class.equals(converter.convertsTo()) ? "title" : "NA", value, Attribute.CDATA_TYPE);
                    args[i] = converter.convert(method.getParameterTypes()[i], attrib, engine);
                } else {
                    args[i] = value;
                }
            }


            border = (Border) method.invoke(null, args);
        }

        return border;

    }

    private TitledBorder convertTitledBorder(ConverterLibrary cvtlib, SwingEngine<?> engine, String[] params) throws Exception {

        if (params == null || params.length == 0) {
            return new TitledBorder((Border) null);
        }

        Attribute attrib = new Attribute("title", params[0].trim(), Attribute.CDATA_TYPE);

        String title = (String) cvtlib.getConverter(String.class).convert(String.class, attrib, engine);

        switch (params.length) {
            case 1:
                return new TitledBorder(title);
            case 2: {
                int titleJustification = getConstantValue(TitledBorder.class, params[1], TitledBorder.DEFAULT_JUSTIFICATION);
                return new TitledBorder((Border) null, title, titleJustification, TitledBorder.DEFAULT_POSITION);
            }
            case 3: 
            {
                int titleJustification = getConstantValue(TitledBorder.class, params[1], TitledBorder.DEFAULT_JUSTIFICATION);
                int textPosition = getConstantValue(TitledBorder.class, params[2], TitledBorder.DEFAULT_POSITION);
                return new TitledBorder((Border) null, title, titleJustification, textPosition);
            }
            default: {

                int titleJustification = getConstantValue(TitledBorder.class, params[1], TitledBorder.DEFAULT_JUSTIFICATION);
                int textPosition = getConstantValue(TitledBorder.class, params[2], TitledBorder.DEFAULT_POSITION);
                return new TitledBorder((Border) null, title, titleJustification, textPosition, java.awt.Font.decode(params[3]));
                
            }
        }
    }

    /**
     *
     *
     */
    @Deprecated
    protected Border _convert_old(final Class type, final String borderString, SwingEngine<?> engine) {

        Border border = null;

        StringTokenizer st = new StringTokenizer(borderString, "(,)"); // border type + parameters

        int n = st.countTokens() - 1; // number of parameter to create a border

        String borderType = st.nextToken().trim();

        Method method = null;

        ConverterLibrary cvtlib = ConverterLibrary.getInstance();
        //
        // Special case for single parameter construction, give priority to String Type
        //
        if (n == 0) {
            try {
                method = BorderFactory.class.getMethod("create" + borderType);

            } catch (NoSuchMethodException e) {
                // intent. empty
            }
            if (method == null) { // try with empty string
                n = 1;
                st = new StringTokenizer(" ", "(,)");
            }
        }
        if (n == 1) {
            try {
                method = BorderFactory.class.getMethod("create" + borderType, new Class[]{String.class});
            } catch (NoSuchMethodException e) {
                //  no need to do anything here.
            }
        }
        for (int i = 0; method == null && i < METHODS.length; i++) {
            if (METHODS[i].getParameterTypes().length == n && METHODS[i].getName().endsWith(borderType)) {
                method = METHODS[i];

                for (int j = 0; j < method.getParameterTypes().length; j++) {
                    if (String.class.equals(method.getParameterTypes()[j])) {
                        continue;
                    }
                    if (null == cvtlib.getConverter(method.getParameterTypes()[j])) {
                        method = null;
                        break;
                    }
                }
            }
        }
        try {
            Object[] args = new Object[n];
            for (int i = 0; i < n; i++) { // fill argument array
                Converter<?> converter = cvtlib.getConverter(method.getParameterTypes()[i]);

                final String value = st.nextToken().trim();
                if (converter != null) {
                    Attribute attrib = new Attribute(String.class.equals(converter.convertsTo()) ? "title" : "NA", value, Attribute.CDATA_TYPE);
                    args[i] = converter.convert(method.getParameterTypes()[i], attrib, engine);
                } else {
                    args[i] = value;
                }
            }
            border = (Border) method.invoke(null, args);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't create border, " + borderString, e);
        }
        return border;
    }

}
