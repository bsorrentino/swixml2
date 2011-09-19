package Task.Support.GUISupport;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.swixml.contrib.gmap.Validate;

import com.sun.java.swing.Painter;

/**
 * GradientPainter is a simple JPanel extension that paints a FAST gradient (using JIDE OSS library).
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since May 11, 2007, 12:14:46 PM
 */
public class GradientPainter implements Painter {

public GradientPainter() {
  _color1 = Colors.LightGray.alpha(0.3f);
  _color2 = Colors.MediumGray.alpha(0.3f);
  _vertical = true;
}

public GradientPainter(Color c1, Color c2, boolean vertical) throws IllegalArgumentException {
  Validate.notNull(c1, "Color1 can not be null");
  Validate.notNull(c2, "Color2 can not be null");

  _color1 = c1;
  _color2 = c2;
  _vertical = vertical;
}

public GradientPainter(int width, int height, Color c1, Color c2, boolean vertical) {
  Validate.notNull(c1, "Color1 can not be null");
  Validate.notNull(c2, "Color2 can not be null");

  _vertical = vertical;
  _color1 = c1;
  _color2 = c2;
  _bounds = new Rectangle(0, 0, width, height);
}

private Color _color1, _color2;
private Rectangle _bounds;
private boolean _vertical;

public void paint(Graphics2D g, Object object, int width, int height) {
	throw new UnsupportedOperationException();
/*
	if (_bounds == null || _bounds.width != width || _bounds.height != height) {
    _bounds = new Rectangle(0, 0, width, height);
  }

  JideSwingUtilities.fillGradient(g,
                                  _bounds,
                                  _color1,
                                  _color2,
                                  _vertical);
*/                                  
}

}//end class GradientPainter
