package Task.Support.GUISupport;

import java.awt.*;

/**
 * Colors is a utility class that makes it easier to work with colors. Methods are provided for
 * conversion to hex strings, and for getting alpha channel colors.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Apr 21, 2007, 12:55:24 PM
 */
public enum Colors {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// various colors in the pallete
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  LightGrad_DarkStop(200, 200, 200),
  LightGrad_LightStop(240, 240, 240),
  SmurfBlue(84, 141, 212),
  LightPaleBlue(225, 240, 255),
  LightestBrightBlue(198, 230, 244),
  LightBrightBlue(129, 200, 231),
  BrightBlue(87, 182, 223),
  Pink(255, 175, 175),
  Green(159, 205, 20),
  Orange(213, 113, 13),
  Yellow(Color.yellow),
  Red(189, 67, 67),
  LightBlue(208, 223, 245),
  DarkBlue(45, 45, 107),
  DullBlue(82, 109, 165),
  DullBlueDark(55, 76, 120),
  Blue(Color.blue),
  BlueGray(72, 72, 107),
  Black(0, 0, 0),
  MediumBlack(25, 25, 25),
  LightBlack(50, 50, 50),
  LightestBlack(75, 75, 75),
  White(255, 255, 255),
  Gray(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue()),
  LightestGray(239, 239, 239),
  LightGray(233, 233, 233),
  MediumGray(191, 191, 191),
  DarkGray(111, 111, 111),
  DarkestGray(128, 128, 128),
  LightFrost(195, 203, 223),
  DarkFrost(97, 101, 111),
  DarkFadedBlue(51, 44, 79),
  TranslucentBlue(150, 190, 255, 200),
  TranslucentTitleGray(Colors.DarkGray.alpha(0.7f)),
  TranslucentWhite(1.0f, 1.0f, 1.0f, 0.5f),
  TranslucentBlack(0f, 0f, 0f, 0.5f),
  TranslucentGray(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), (int) (255 * .5));

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// Color support
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructors
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

Colors(Color c) {
  _myColor = c;
}

Colors(int r, int g, int b) {
  _myColor = new Color(r, g, b);
}

Colors(int r, int g, int b, int alpha) {
  _myColor = new Color(r, g, b, alpha);
}

Colors(float r, float g, float b, float alpha) {
  _myColor = new Color(r, g, b, alpha);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

private Color _myColor;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public Color alpha(float t) {
  return new Color(_myColor.getRed(), _myColor.getGreen(), _myColor.getBlue(), (int) (t * 255f));
}

public static Color alpha(Color c, float t) {
  return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (t * 255f));
}

public Color color() { return _myColor; }

public Color color(float f) {
  return alpha(f);
}

public String toString() {
  StringBuilder sb = new StringBuilder();
  sb.append("r=")
      .append(_myColor.getRed())
      .append(", g=")
      .append(_myColor.getGreen())
      .append(", b=")
      .append(_myColor.getBlue())
      .append("\n");
  return sb.toString();
}

public String toHexString() {
  Color c = _myColor;
  StringBuilder sb = new StringBuilder();
  sb.append("#");
  sb.append(Integer.toHexString(_myColor.getRed()));
  sb.append(Integer.toHexString(_myColor.getGreen()));
  sb.append(Integer.toHexString(_myColor.getBlue()));
  return sb.toString();
}

}//end enum Colors
