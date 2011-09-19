package Task.Support.GUISupport;

import java.awt.Font;
import java.util.concurrent.ConcurrentHashMap;

import org.swixml.contrib.gmap.Validate;

/**
 * FontUtils is a convenience class that allows pre defined font sizes and styles to be accessed easily.
 * These fonts are cached, once created, to enhance performance.
 * 
 * @author Nazmul Idris
 * @version 1.0
 * @since May 6, 2007, 1:33:40 PM
 */
public class FontUtils {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// pre-defined font styles
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public enum FontStyles {

  FixedWidthXSmall(FontNames.Consolas, Font.PLAIN, 9f),
  FixedWidthSmall(FontNames.Consolas, Font.PLAIN, 11f),
  FixedWidthNormal(FontNames.Consolas, Font.PLAIN, 13f),
  FixedWidthLarge(FontNames.Consolas, Font.PLAIN, 18f),

  VariableWidthXXSmall(FontNames.SegoeUI, Font.PLAIN, 8f),
  VariableWidthXSmall(FontNames.SegoeUI, Font.PLAIN, 9f),
  VariableWidthSmall(FontNames.SegoeUI, Font.PLAIN, 13f),
  VariableWidthNormal(FontNames.SegoeUI, Font.PLAIN, 14f),
  VariableWidthLarge(FontNames.LucidaGrande, Font.PLAIN, 16f),
  VariableWidthXLarge(FontNames.LucidaGrande, Font.BOLD, 18f),
  VariableWidthXXLarge(FontNames.LucidaGrande, Font.BOLD, 24f);

  FontNames _face;
  int _style;
  float _size;

  FontStyles(FontNames face, int style, float size) throws IllegalArgumentException {
    Validate.notNull(face, "face can not be empty or null");

    _face = face;
    _style = style;
    _size = size;
  }

  public Font font() {
    Font f = create(_face.getName(), _style, _size);
    //System.out.printf("font()=%s,%s,%s,%s%n", f, _face, _style, _size );
    return f;
  }

  public Font font(int style) {
    Font f = create(_face.getName(), style, _size);
    return f;
  }

  public Font font(float size) {
    Font f = create(_face.getName(), _style, size);
    return f;
  }

  public String toString() {
    return fontToString(_face.getName(), _style, _size);
  }

}//end enum FontStyles

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// fonts to load from JAR...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public enum FontNames {

  LucidaGrande("Lucida Grande"),
  Consolas("Consolas"),
  SegoeUI("Segoe UI"),
  Calibri("Calibri");

  public String _name;

  FontNames(String name) {_name = name;}

  public String getName() {return toString();}

  public String toString() {return _name;}

}//end enum FontNames

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// font cache
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public static final ConcurrentHashMap<String, Font> FontCache = new ConcurrentHashMap<String, Font>(20);

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/** load all the fonts from the JAR file now */
public static void setFonts() {

  // replace some font related defaults in UIDefautls
  GUIUtils.setUIDefaultFontProperty("Button.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("Label.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("CheckBox.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("ProgressBar.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("TextArea.font", FontUtils.FontStyles.FixedWidthNormal.font());
  GUIUtils.setUIDefaultFontProperty("Table.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("Tree.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("TableHeader.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("List.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("ComboBox.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("FormattedTextField.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("TextField.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("PasswordField.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("JideTabbedPane.font", FontUtils.FontStyles.VariableWidthSmall.font());
  GUIUtils.setUIDefaultFontProperty("JideTabbedPane.selectedTabFont", FontUtils.FontStyles.VariableWidthSmall.font());

  // replace some color related defaults in UIDefaults

  GUIUtils.setUIDefaultColorProperty("ProgressBar.foreground", Colors.SmurfBlue.color());
  GUIUtils.setUIDefaultColorProperty("Button.foreground", Colors.LightBlack.color());
  GUIUtils.setUIDefaultColorProperty("Label.foreground", Colors.LightBlack.color());
  GUIUtils.setUIDefaultColorProperty("TextArea.foreground", Colors.LightBlack.color());
  GUIUtils.setUIDefaultColorProperty("TextField.foreground", Colors.LightBlack.color());
  GUIUtils.setUIDefaultColorProperty("PasswordField.foreground", Colors.LightBlack.color());
}

/**
 * check to see if the entire name + style + size is in the cache, if not: 1. see if you have to
 * load the name + style from the JAR file (cache this in FONT_FILES_IN_JAR) 2. derive this loaded
 * font, and save that to the FONT_CACHE
 */
public static Font create(String name, int style, float size) throws IllegalArgumentException {
  Validate.notEmpty(name, "name can not be empty or null");

  String key_NAME_STYLE = fontToString(name, style);
  String key_NAME_STYLE_SIZE = fontToString(name, style, size);

  Font font;

  // cache contains name, style, size
  if (FontCache.containsKey(key_NAME_STYLE_SIZE)) {
    font = FontCache.get(key_NAME_STYLE_SIZE);
  }
  // cache contains name, style
  else if (FontCache.containsKey(key_NAME_STYLE)) {
    font = FontCache.get(key_NAME_STYLE).deriveFont(size);
    FontCache.put(key_NAME_STYLE_SIZE, font);
  }
  else {
    // cache doesnt contain anything
    font = new Font(name, style, (int) size);
    FontCache.put(key_NAME_STYLE_SIZE, font);
  }

  return font;
}

/** turns the entire name, style, size into a String */
private static final String fontToString(String name, int style, float size) throws IllegalArgumentException {
  Validate.notEmpty(name, "name can not be empty or null");

  StringBuilder sb = new StringBuilder();

  sb.
      append(name).append(",").
      append(style).append(",").
      append(size);

  return sb.toString();
}

/** turns the font face + style into a String */
private static final String fontToString(String name, int style) throws IllegalArgumentException {
  Validate.notEmpty(name, "name can not be empty or null");

  StringBuilder sb = new StringBuilder();

  sb.
      append(name).append(",").
      append(style);

  return sb.toString();
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// toString - debug
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static String dumpFontCache() {
  StringBuilder sb = new StringBuilder();

  sb.append("FontUtils.FontCache:\n");

  for (String key : FontCache.keySet()) {
    sb.
        append("key=[").append(key).append("], ").
        append("font=[").append(FontCache.get(key).getName()).append("]\n");
  }

  return sb.toString();
}

}//end class FontUtils