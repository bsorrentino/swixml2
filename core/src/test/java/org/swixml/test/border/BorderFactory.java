/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.test.border;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import org.jdom.Attribute;
import org.swixml.Localizer;



/**
 * <ul>
 *
 * <li>
 * empty: ?????? ?????.<br/>
 * ?????????? ??? ???, ??? 4 ????? ????? (???????). (????, ????, ???, ?????)
 * <p>
 * empty;<br/>
 * empty 12 12 12 12;<br/>
 * </p>
 * </li>
 *
 * <li>
 *    line: ????? ? ???? ?????<br/>
 *    ?????????? ???????:
 *    <ul>
 *       <li>line color</li>
 *       <li>line color thick</li>
 *    </ul>
 *    ???
 *    <ul>
 *       <li>color - ???? ????? ? ??????? ColorFactory.getColor(String)</li>
 *       <li>thick - ??????? ????? ? ????????</li>
 *    </ul>
 *    <p>
 *    ???????:
 *    <ul>
 *       <li>line black</li>
 *       <li>line red 2</li>
 *    </ul>
 *    </p>
 * </li>
 *
 * <li>
 *    etched: ????? ? ???? ??????
 *    ?????????? ???????:
 *    <ul>
 *       <li>etched</li>
 *       <li>etched type</li>
 *       <li>etched highlight_color shadow_color</li>
 *       <li>etched type highlight_color shadow_color</li>
 *    </ul>
 *    ???
 *    <ul>
 *       <li>type - ??? ????? (lowered, raised)</li>
 *       <li>highlight_color - ???? ??????? ????? ? ??????? ColorFactory.getColor(String)</li>
 *       <li>shadow_color - ???? ??????? ????? ? ??????? ColorFactory.getColor(String)</li>
 *    </ul>
 *    <p>
 *    ???????:
 *    <ul>
 *       <li>etched</li>
 *       <li>etched lowered</li>
 *       <li>etched white black</li>
 *       <li>etched lowered white black</li>
 *    </ul>
 *    </p>
 * </li>
 *
 * <li>
 *    compound: ????????? ?????. 2 ?????????: ??????? ????? ? ?????????? ?????.
 *    <p>
 *    compound (outer) (inner)
 *    </p>
 *    <p>
 *    ??????:<br/>
 *    compound (empty 12 12 12 12) (compound (line black) (empty 12 12 12 12))
 *    </p>
 * </li>
 *
 * <li>
 *    titled: ????? ? ??????????.<br/>
 *    ?????????? ???????:
 *    <ul>
 *       <li>titled "caption"</li>
 *       <li>titled "caption" (inner)</li>
 *       <li>titled "caption" (inner) justification position</li>
 *       <li>titled "caption" (inner) justification position (font) color</li>
 *    </ul>
 *    ???
 *    <ul>
 *       <li>caption - ?????????</li>
 *       <li>inner - ?????? ?????</li>
 *       <li>justification - ???????????? ?? ??????????? (left, right, center, leading, trailing)</li>
 *       <li>position - ???????????? ?? ????????? (top, above-top, below-top, bottom, above-bottom, below-bottom)</li>
 *       <li>font - ????? ????????? ? ??????? Font.decode(String)</li>
 *       <li>color - ???? ????????? ? ??????? ColorFactory.getColor(String)</li>
 *    </ul>
 *    <p>
 *    ???????:
 *    <ul>
 *       <li>titled "Foo"</li>
 *       <li>titled "Foo" (empty 12 12 12 12)</li>
 *       <li>titled "Foo" (empty 12 12 12 12) left bottom</li>
 *       <li>titled "Foo" (empty 12 12 12 12) left bottom (Arial) red</li>
 *    </ul>
 *    </p>
 * </li>
 * </ul>
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 15:17:44
 * </pre>
 */
public class BorderFactory {
    
    /*
      public static Object convert(final Class type, final Attribute attr, Localizer localizer) {

          String nsURI = attr.getNamespaceURI();
          if( SWINGHTMLTEMPLATE_NAMESPACE_URI.equals(nsURI)) {

              return parseBorderString( attr.getValue(), localizer);

          }
          return null;
      }
    */
    
    public static String SWINGHTMLTEMPLATE_NAMESPACE_URI = "http://code.google.com/p/swinghtmltemplate/";

    private static Log logger = LogFactory.getLog(BorderFactory.class);

    
    public static boolean isEmpty( String v ) {
        return v.isEmpty();
    } 
    
    public static boolean isNotEmpty( String v ) {
        return !v.isEmpty();
    }
    
    public static boolean isNotBlank( String v ) {
        return v.trim().length()>0;
    }

    public static boolean isBlank( String v ) {
        return v.trim().length()==0;
    }
    
    
    public static Color getColor(String name) {
        if (name.startsWith("#")) {
            return parseHexColor(name);
        }
        else {
            return Color.getColor(name);
        }
    }


    public static Color parseHexColor(String hex) {
        if (hex.startsWith("#")) {
            return Color.decode("0x"+hex.substring(1));
        }
        return null;
    }   
    
    public static int fingMatchingClosingBracket(String text, int openingBracketIndex) {
        int outerEnd = -1;
        int currentOpened = 0;//?????????? ???????? ?????? (??? ????????? ???????)
        for (int i = openingBracketIndex+1; i<text.length() && outerEnd<0; i++) {
            char c = text.charAt(i);
            if (c =='(') {
                currentOpened++;
            }
            else if (c ==')') {
                if (currentOpened>0) {
                    currentOpened--;
                }
                else {
                    outerEnd = i;
                }
            }
        }
        return outerEnd;
    }
    
    public static boolean isNumeric( String v ) {
        
        try {
        Integer.parseInt(v);
        
        return true;
        }
        catch( NumberFormatException e ) {
            return false;
        }
        
    }

  /**
     * ??????????? ?????? ????? ? ?????? ?????.
     * @param text ?????? ?????, ????????, "10 10 12 234"
     * @return ?????? ?????
     */
    public  static int[] parseIntegers(String text) {
        if (isEmpty(text)) {
            return new int[0];
        }
        text = mergeSpaces(text);
        String[] tokens = text.split(" ");
        java.util.List<Integer> sizes = new java.util.ArrayList<Integer>();
        for (String t : tokens) {
            if (!isEmpty(t) && isNumeric(t)) {
                sizes.add(new Integer(t.trim()));
            }
        }
        int[] res = new int[sizes.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = sizes.get(i);
        }
        return res;
    }
    
    public static String mergeSpaces(String text) {
        text = text.replaceAll("\\s", " ");
        while (text.indexOf("  ")>=0) {
            text = text.replaceAll("  ", " ");
        }
        return text.trim();
    }    
    
    public static String[] extractParams(String text) {
        text = mergeSpaces(text);
        return text.split(" ");
    }

    /**
     * ??????????? ??????, ??????????? ?????, ? ?????.
     * @param borderString ??????, ??????????? ?????
     * @return ?????
     */
    public static Border parseBorderString(String borderString, Localizer localizer) {
        borderString = mergeSpaces(borderString);
        int spacePos = borderString.indexOf(" ");
        if (spacePos<0) {
            spacePos = borderString.length();
        }
        String borderType = borderString.substring(0, spacePos);
        String borderParams;
        if (spacePos<borderString.length()) {
            borderParams = borderString.substring(spacePos + 1);
        }
        else {
            borderParams = null;
        }
        Border border = null;

        if ("empty".equals(borderType)) {
            int[] paddings = parseIntegers(borderParams);
            if (paddings.length==0) {
                border = javax.swing.BorderFactory.createEmptyBorder();
            }
            else if (paddings.length!=4) {
                logger.warn("Wrong border format: "+borderString);
            }
            else {
                border = javax.swing.BorderFactory.createEmptyBorder(paddings[0], paddings[1], paddings[2], paddings[3]);
            }
        }
        else if ("etched".equals(borderType)) {
            if (isNotBlank(borderParams)) {
                String[] params = extractParams(borderParams);
                if (params.length==1) {
                    int type = EtchedBorder.LOWERED;
                    if ("raised".equals(params[0])) {
                        type = EtchedBorder.RAISED;
                    }
                    else if ("lowered".equals(params[0])) {
                        type = EtchedBorder.LOWERED;
                    }
                    else {
                        logger.warn("Unknown border type: "+borderString);
                    }
                    border = javax.swing.BorderFactory.createEtchedBorder(type);
                }
                else if (params.length==2) {
                    Color highlight = getColor(params[0]);
                    Color shadow = getColor(params[1]);
                    border = javax.swing.BorderFactory.createEtchedBorder(highlight, shadow);
                }
                else if (params.length==3) {
                    int type = EtchedBorder.LOWERED;
                    if ("raised".equals(params[0])) {
                        type = EtchedBorder.RAISED;
                    }
                    else if ("lowered".equals(params[0])) {
                        type = EtchedBorder.LOWERED;
                    }
                    else {
                        logger.warn("Unknown border type: "+borderString);
                    }
                    Color highlight = getColor(params[1]);
                    Color shadow = getColor(params[2]);
                    border = javax.swing.BorderFactory.createEtchedBorder(type, highlight, shadow);
                }
            }
            else {
                border = javax.swing.BorderFactory.createEtchedBorder();
            }
        }
        else if ("compound".equals(borderType)) {

            //??????: compound (border1) (border2)
            int outerStart = borderString.indexOf("(", spacePos+1)+1;//??????????? ??? ?????? ?????
            int outerEnd = -1;
            outerEnd = fingMatchingClosingBracket(borderString, outerStart-1);
            int innerStart = borderString.indexOf("(", outerEnd+1)+1;
            int innerEnd = fingMatchingClosingBracket(borderString, innerStart-1);

            String inner = borderString.substring(innerStart, innerEnd);
            String outer = borderString.substring(outerStart, outerEnd);

            final Border outerBorder = BorderFactory.parseBorderString(outer, localizer);
            final Border innerBorder = BorderFactory.parseBorderString(inner, localizer);
            border = javax.swing.BorderFactory.createCompoundBorder(outerBorder, innerBorder);
            
        }
        else if ("line".equals(borderType)) {
            int thick = -1;
            Color color = null;
            if (isNotEmpty(borderParams)) {
                String[] tokens = extractParams(borderParams);
                if (tokens.length==1) {
                    color = getColor(tokens[0]);
                    border = javax.swing.BorderFactory.createLineBorder(color);
                }
                else if (tokens.length==2) {
                    color = getColor(tokens[0]);
                    thick = new Integer(tokens[1]);
                    border = javax.swing.BorderFactory.createLineBorder(color, thick);
                }
                else {
                    logger.warn("Wrong border format: "+borderString);
                }
            }
            else {
                logger.warn("Wrong border format: "+borderString);
            }
        }
        else if ("titled".equals(borderType)) {
            if (isNotEmpty(borderParams)) {
                
                int titleStart = borderString.indexOf('"');
                int titleEnd = borderString.indexOf('"', titleStart+1);
                String title = borderString.substring(titleStart+1, titleEnd);

                int innerStart = borderString.indexOf('(');
                if (innerStart >=0) {//???? ? ?????? ??????? ?????????? ?????
                    int innerEnd = fingMatchingClosingBracket(borderString, innerStart);
                    Border inner = parseBorderString(borderString.substring(innerStart+1, innerEnd), localizer);
                    if (isNotBlank(borderString.substring(innerEnd+1))) {
                        String[] params = extractParams(borderString.substring(innerEnd+1));
                        if (params.length==2) {//?????? ?????? titleJustification
                            int titleJustification = convertTitleJustification(params[0]);
                            int titlePosition = convertTitlePosition(params[1]);
                            border = javax.swing.BorderFactory.createTitledBorder(inner, title,titleJustification, titlePosition);
                        }
                        else {

                            int fontStart = borderString.indexOf('(', innerEnd);
                            if (fontStart>=0) {
                                int fontEnd = fingMatchingClosingBracket(borderString, fontStart);
                                String fontStr = borderString.substring(fontStart+1, fontEnd);
                                Font font = Font.decode(fontStr);

                                int titleJustification = convertTitleJustification(params[0]);
                                int titlePosition = convertTitlePosition(params[1]);

                                if (isBlank(borderString.substring(fontEnd+1))) {//???? ?? ?????? ????
                                    border = javax.swing.BorderFactory.createTitledBorder(inner, title,titleJustification, titlePosition, font);
                                }
                                else {
                                    Color color = getColor(mergeSpaces(borderString.substring(fontEnd+1)));
                                    border = javax.swing.BorderFactory.createTitledBorder(inner, title,titleJustification, titlePosition, font, color);
                                }


                            }
                            else {
                                logger.warn("Expected font declaration in border: "+borderString);
                            }


                        }
                    }
                    else {
                        border = javax.swing.BorderFactory.createTitledBorder(inner, title);
                    }
                }
                else {
                    border = javax.swing.BorderFactory.createTitledBorder(title);
                }
            }
            else {
                logger.warn("Wrong border format: "+borderString);
            }
        }
        else {
            logger.warn("Unknown border type: "+borderString);
            border = null;
        }


        return border;
    }

    /**
     * ????????? ?????? ? ????????? ????????? justification ????? TitledBorder ? ????????, ?????????? ??? TitledBorder.
     * @param justif ??????
     * @return ????????
     */
    private static int convertTitleJustification(String justif) {
        int titleJustification = TitledBorder.DEFAULT_JUSTIFICATION;
        if ("left".equals(justif)) {
            titleJustification = TitledBorder.LEFT;
        }
        else if ("right".equals(justif)) {
            titleJustification = TitledBorder.LEFT;
        }
        else if ("center".equals(justif)) {
            titleJustification = TitledBorder.CENTER;
        }
        else if ("leading".equals(justif)) {
            titleJustification = TitledBorder.LEADING;
        }
        else if ("trailing".equals(justif)) {
            titleJustification = TitledBorder.TRAILING;
        }
        else {
            logger.warn("Unknown title justification: "+justif);
        }
        return titleJustification;
    }

    /**
     * ????????? ?????? ? ????????? ????????? position ????? TitledBorder ? ????????, ?????????? ??? TitledBorder.
     * @param pos ??????
     * @return ????????
     */
    private static int convertTitlePosition(String pos) {

        int position = TitledBorder.DEFAULT_POSITION;
        if ("top".equals(pos)) {
            position = TitledBorder.TOP;
        }
        else if ("below-top".equals(pos)) {
            position = TitledBorder.BELOW_TOP;
        }
        else if ("above-top".equals(pos)) {
            position = TitledBorder.ABOVE_TOP;
        }
        else if ("bottom".equals(pos)) {
            position = TitledBorder.BOTTOM;
        }
        else if ("below-bottom".equals(pos)) {
            position = TitledBorder.BELOW_BOTTOM;
        }
        else if ("above-bottom".equals(pos)) {
            position = TitledBorder.ABOVE_BOTTOM;
        }
        else {
            logger.warn("Unknown title position: "+pos);
        }
        return position;
    }

}
