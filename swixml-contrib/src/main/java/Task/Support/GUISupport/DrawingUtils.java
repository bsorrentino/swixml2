package Task.Support.GUISupport;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.graphics.ReflectionRenderer;
import org.jdesktop.swingx.painter.AbstractAreaPainter;
import org.jdesktop.swingx.painter.AbstractLayoutPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.PinstripePainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.swixml.contrib.gmap.Validate;

import com.sun.java.swing.Painter;

/**
 * DrawingUtils is a utility class that helps with drawing operations - reflections and other effects.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Apr 20, 2007, 2:56:50 PM
 */
public class DrawingUtils {

private static Logger LOG = Logger.getLogger(DrawingUtils.class.getName());

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// font metrics stuff
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static final Rectangle2D getTextBounds(Graphics2D g2, Font font, String str)
    throws IllegalArgumentException
{
  Validate.notNull(g2, "g2 can not be null");
  Validate.notNull(font, "font can not be null");
  Validate.notNull(str, "str can not be null");

  FontMetrics fm = g2.getFontMetrics(font);
  return fm.getStringBounds(str, g2);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// AA rendering hints
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * Applies the Desktop Anti Aliasing settings to the given Graphics2D object. If you do not want
 * this setting propagated, be sure to dispose() your graphics context after using it.
 */
public static Graphics2D applyDesktopAASettings(Graphics2D g) {
  // use desktop text AA settings
  Toolkit tk = Toolkit.getDefaultToolkit();
  Map map = (Map) (tk.getDesktopProperty("awt.font.desktophints"));
  if (map != null) {
    g.addRenderingHints(map);
  }
  return g;
}

/**
 * Applies AA settings to the Graphics2D context for smoothing out drawing. Use this if you
 * do not want jagged edges on things that you draw on the screen. Call dispose() on your
 * Graphics2D object if you do not want these settings to propagate.
 */
public static Graphics2D applySmoothDrawingSettings(Graphics2D g) {
  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
  g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
  g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
  return g;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// border drawing
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static Border getLightBorder() {
  return BorderFactory.createCompoundBorder(
      BorderFactory.createEtchedBorder(
          EtchedBorder.RAISED, Colors.LightFrost.alpha(0.1f), Colors.DarkFrost.alpha(0.1f)),
      BorderFactory.createEmptyBorder(1, 1, 1, 1)
  );
}

public static Border getDarkBorder() {
  return BorderFactory.createCompoundBorder(
      BorderFactory.createEtchedBorder(
          EtchedBorder.RAISED, Colors.LightFrost.alpha(0.3f), Colors.DarkFrost.alpha(0.3f)),
      BorderFactory.createEmptyBorder(1, 1, 1, 1)
  );
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// reflection effects
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public static BufferedImage getReflection(BufferedImage img, float length, float opacity, boolean blur) {
  ReflectionRenderer renderer = new ReflectionRenderer();
  renderer.setOpacity(opacity);
  renderer.setLength(length);
  renderer.setBlurEnabled(blur);
  BufferedImage reflection = renderer.appendReflection(img);
  return reflection;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// image effects
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public static BufferedImage getGlossyImage(BufferedImage image) {
  return getGlossyImage(image,
                        new Color(1.0f, 1.0f, 1.0f, 0.2f),
                        GlossPainter.GlossPosition.TOP);
}

/**
 * given a bufferedimage, returns a glossy version of the image, with the given color and gloss
 * position applied
 */
public static BufferedImage getGlossyImage(BufferedImage image, Color color, GlossPainter.GlossPosition position) {
  Paint paint = color;
  int width = image.getWidth();
  int height = image.getHeight();

//  Graphics2D g2 = (Graphics2D) image.getGraphics();
  Graphics2D g2 = ImageUtils.g2(image);

  Ellipse2D ellipse = new Ellipse2D.Double(-width / 2.0,
                                           height / 2.7, width * 2.0,
                                           height * 2.0);

  Area gloss = new Area(ellipse);
  if (position == GlossPainter.GlossPosition.TOP) {
    Area area = new Area(new Rectangle(0, 0,
                                       width, height));
    area.subtract(new Area(ellipse));
    gloss = area;
  }
  /*
  if(getClip() != null) {
      gloss.intersect(new Area(getClip()));
  }*/
  g2.setPaint(paint);
  g2.fill(gloss);

  g2.dispose();

  return image;

}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// SwingX painter helpers
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static RectanglePainter getRoundedRectanglePainter(Color fillColor,
                                                          Color borderColor, int borderWidth,
                                                          AbstractAreaPainter.Style style,
                                                          int roundedWidth)
{
  RectanglePainter _rectPainter = new RectanglePainter(
      fillColor,
      borderColor,
      (float) borderWidth,
      style);

  _rectPainter.setRounded(true);
  _rectPainter.setRoundWidth(roundedWidth);
  _rectPainter.setRoundHeight(roundedWidth);
  return _rectPainter;
}

public static Painter getDropTargetLabelBgPainter() {
  GradientPainter gradientPainter = new GradientPainter(
      Colors.White.alpha(0.3f),
      Colors.LightGray.alpha(0.1f),
      true
  );
  return gradientPainter;
}

public static Painter getPanelBgPainter() {
  GradientPainter gradientPainter = new GradientPainter(
      Colors.LightGray.alpha(0.3f),
      Colors.LightGray.alpha(0.7f),
      false
  );
  return gradientPainter;
}

public static Painter getStatusUpdaterBgPainter() {
	throw new UnsupportedOperationException();
/*	
  GlossPainter glossPainter = new GlossPainter(Colors.White.alpha(0.3f));
  GradientPainter gradientPainter = new GradientPainter(
      Colors.LightestGray.alpha(0.3f), Colors.MediumGray.alpha(0.3f), true
  );
  MattePainter mattePainter = new MattePainter(Colors.MediumGray.alpha(0.3f));
  //MattePainter mattePainter = new MattePainter(Colors.LightGray.alpha(0.3f));
  return new CompoundPainter(mattePainter, gradientPainter, glossPainter);
*/  
}

public static Painter getInfoLabelBgPainter() {
	throw new UnsupportedOperationException();
/*	
  GlossPainter glossPainter = new GlossPainter(Colors.White.alpha(0.1f));
  GradientPainter gradientPainter = new GradientPainter(
      Colors.LightestGray.alpha(0.3f), Colors.MediumGray.alpha(0.3f), true
  );
  PinstripePainter pinstripePainter = new PinstripePainter(Colors.LightGray.alpha(0.1f), 45, 2, 5);
  MattePainter mattePainter = new MattePainter(Colors.LightGray.alpha(0.3f));
  return new CompoundPainter(mattePainter, gradientPainter, pinstripePainter, glossPainter);
*/  
}

public static Painter getContentPanelBgPainter() {

  //MattePainter mp = new MattePainter(Colors.LightestGray.color());
  GradientPainter gp = new GradientPainter(Colors.LightGray.alpha(0.3f), Colors.Gray.alpha(0.3f), true);
  //PinstripePainter pp = new PinstripePainter(Colors.LightGray.alpha(0.3f));

  return gp;
  //return new CompoundPainter(mp, gp, pp);
  //return new CompoundPainter(mp, gp);
  //return new new CompoundPainter(pp, gp);
}

public static Painter getHeaderPanelBgPainter() {
	throw new UnsupportedOperationException();
/*	
  // set the background painter
  MattePainter mp = new MattePainter(Colors.LightestGray.alpha(0.5f));
  GlossPainter gp = new GlossPainter(Colors.White.alpha(0.6f),
                                     GlossPainter.GlossPosition.TOP);
  PinstripePainter pp = new PinstripePainter(Colors.MediumGray.alpha(0.3f),
                                             45d);
  return (new CompoundPainter(mp, gp, pp));
*/  
}

public static Painter getGradientFillBgPainter(int width, int height, Color color1, Color color2) {
  LinearGradientPaint gradientPaint =
      new LinearGradientPaint(0.0f, 0.0f, width, height,
                              new float[]{0.0f, 1.0f},
                              new Color[]{color1, color2});
  MattePainter mattePainter = new MattePainter(gradientPaint);
  return (Painter) mattePainter;
}

private static Painter DISABLED_GLASS_PANE_BG_PAINTER;

public static Painter getDisabledGlassPaneBgPainter(int width, int height) {

  if (DISABLED_GLASS_PANE_BG_PAINTER == null) {

    /*
    LinearGradientPaint gradientPaint =
        new LinearGradientPaint(0.0f, 0.0f, 0f, height,
                                new float[]{0.0f, 1.0f},
                                new Color[]{Colors.Black.alpha(0.3f), Colors.MediumGray.alpha(0.3f)});
    MattePainter mattePainter = new MattePainter(gradientPaint);
    */

    ImagePainter imgPainter = null;

    try {
      BufferedImage img = ImageUtils.loadBufferedImage("images/splashscreen.png", true, 0.05f);
      int imgW = img.getWidth(),
          imgH = img.getHeight();
      BufferedImage scaledImg = ImageUtils.getScaledBufferedImage(img, (int) (imgW * 0.8), (int) (imgH * 0.8));

      ReflectionRenderer renderer = new ReflectionRenderer();
      renderer.setOpacity(0.8f);
      renderer.setLength(1.0f);
      renderer.setBlurEnabled(true);
      BufferedImage reflection = renderer.appendReflection(scaledImg);

      imgPainter = new ImagePainter(
          reflection,
          AbstractLayoutPainter.HorizontalAlignment.CENTER,
          AbstractLayoutPainter.VerticalAlignment.CENTER
      );
    }
    catch (Exception e) {
      LOG.log(Level.WARNING,
              "splashscreen.png was not found in the classpath. Could not paint a translucent background image",
              e);
    }

    PinstripePainter pinstripePainter = new PinstripePainter(Colors.LightFrost.alpha(0.3f), 45);

    /*
CompoundPainter cp = (imgPainter == null) ?
    new CompoundPainter(mattePainter, pinstripePainter) :
    new CompoundPainter(mattePainter, pinstripePainter, imgPainter);
    */

    GradientPainter gp = new GradientPainter(width, height,
                                             Colors.Black.alpha(0.3f), Colors.MediumGray.alpha(0.3f),
                                             true);
	throw new UnsupportedOperationException();
/*
    DISABLED_GLASS_PANE_BG_PAINTER = new CompoundPainter(gp,
                                                         imgPainter,
                                                         new GlossPainter(Colors.White.alpha(0.1f))
                                                         //,pinstripePainter
    );
*/
    /*
    DISABLED_GLASS_PANE_BG_PAINTER = new CompoundPainter(pinstripePainter, mattePainter, imgPainter,
                                                         new GlossPainter(Colors.White.alpha(0.1f))
    );
    */

    //DISABLED_GLASS_PANE_BG_PAINTER = cp;
    //DISABLED_GLASS_PANE_BG_PAINTER = mattePainter;
    //DISABLED_GLASS_PANE_BG_PAINTER = pinstripePainter;
    /*
    DISABLED_GLASS_PANE_BG_PAINTER = new CompoundPainter(
        imgPainter,
        new MattePainter(Colors.TranslucentBlack.color()),
        new GlossPainter(Colors.White.alpha(0.1f), GlossPainter.GlossPosition.TOP)
    );
    */

    /*
    DISABLED_GLASS_PANE_BG_PAINTER = new CompoundPainter(
        pinstripePainter,
        imgPainter,
        new MattePainter(Colors.DarkFadedBlue.alpha(0.3f))
    );
    */

    //DISABLED_GLASS_PANE_BG_PAINTER = new MattePainter(Colors.TranslucentBlack.color());
  }

  return DISABLED_GLASS_PANE_BG_PAINTER;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// themes for gradient fills
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public static Color[] WHITE_2GRAD = new Color[]{Color.LIGHT_GRAY, Color.WHITE};

public static Color[] GRAY_4GRAD = new Color[]{Color.WHITE, Color.GRAY, Color.BLACK, Color.DARK_GRAY};
public static Color[] REV_GRAY_4GRAD = new Color[]{Color.DARK_GRAY, Color.BLACK, Color.GRAY, Color.WHITE};

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods to help you paint things
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/**
 * Given a BufferedImage, this method returns a blurred version of the image as a new
 * BufferedImage.
 */
public static BufferedImage getBlurredImage(BufferedImage buf) {
  float[] my_kernel = {0.1f, 0.1f, 0.1f,
                       0.1f, 0.2f, 0.1f,
                       0.1f, 0.1f, 0.1f};
  ConvolveOp op = new ConvolveOp(new Kernel(3, 3, my_kernel));
  return op.filter(buf, null);
}

/**
 * Given a BufferedImage, this method returns a translucent version of this image as a new
 * BufferedImage.
 *
 * @param translucency between 0f and 1f.
 */
public static BufferedImage getTranslucentImage(BufferedImage buf, float translucency) {
  BufferedImage bufNew = ImageUtils.createCompatibleImage(buf.getWidth(), buf.getHeight(), false);

//  Graphics2D g2 = (Graphics2D) bufNew.getGraphics();
  Graphics2D g2 = ImageUtils.g2(bufNew);

  AlphaComposite c = AlphaComposite.SrcOver.derive(translucency);
  g2.setComposite(c);

  g2.drawImage(buf, 0, 0, null);

  g2.dispose();

  return bufNew;

  /*
  BufferedImage bufCopy = ImageUtils.getCopyOfBufferedImage(buf);

  Graphics2D g2 = (Graphics2D) bufCopy.getGraphics();

  AlphaComposite c = AlphaComposite.DstIn.derive(translucency);
  g2.setComposite(c);

  g2.fillRect(0, 0, bufCopy.getWidth(), bufCopy.getHeight());

  g2.dispose();

  return bufCopy;
  */
}

/**
 * Given a BufferedImage, this method returns a rounded, gradient, translucency effect on top of it,
 * and retursn it as a new BufferedImage.
 */
public static void drawBorderedTranslucentImage(Graphics g,
                                                int width,
                                                int height,
                                                boolean enabled,
                                                Color[] fourGradientTheme)
{
  Graphics2D g2 = ImageUtils.g2(g);

//  applySmoothDrawingSettings(g2);

  LinearGradientPaint p;


  p = new LinearGradientPaint(0.0f, 0.0f, 0, height,
                              new float[]{0.0f, 0.499f, 0.50f, 1.0f},
                              fourGradientTheme);

  AlphaComposite c = AlphaComposite.SrcOver.derive(0.20f);
  g2.setComposite(c);

  g2.setPaint(p);

  // create a round rectangle
  int offset = 2;
  Shape round = new RoundRectangle2D.Float(offset, offset,
                                           width - offset * 2, height - offset * 2,
                                           10, 10);

  //fill the rectangle with the translucent effect
  g2.fill(round);

  // draw the gray border
  if (enabled) {
    g2.setColor(Color.black);
  }
  else {
    g2.setColor(Color.gray);
  }
  g2.setStroke(new BasicStroke(offset));
  g2.draw(round);

  g2.dispose();
}

public static void drawSemiTransparentShade(Graphics g, int width, int height, Color[] twoGradientTheme) {
  Graphics2D g2 = ImageUtils.g2(g);

//  applySmoothDrawingSettings(g2);

  AlphaComposite c = AlphaComposite.SrcOver.derive(0.3f);
  g2.setComposite(c);

  LinearGradientPaint p;

  p = new LinearGradientPaint(0.0f, 0.0f, 0, height,
                              new float[]{0.0f, 1.0f},
                              twoGradientTheme);

  g2.setPaint(p);

  // create a round rectangle
  int offset = 2;
  Shape round = new RoundRectangle2D.Float(offset, offset,
                                           width - offset * 2, height - offset * 2,
                                           10, 10);

  //fill the rectangle with the translucent effect
  g2.fill(round);

  g2.dispose();
}

public static void drawShadeNormal(Graphics g, int width, int height, Color[] twoGradientTheme, float alpha) {
  Graphics2D g2 = ImageUtils.g2(g);

//  applySmoothDrawingSettings(g2);

  AlphaComposite c = AlphaComposite.SrcOver.derive(alpha);
  g2.setComposite(c);

  LinearGradientPaint p;


  p = new LinearGradientPaint(0.0f, 0.0f, 0, height,
                              new float[]{0.0f, 1.0f},
                              twoGradientTheme);

  g2.setPaint(p);

  // create a rect rectangle
  Shape rect = new Rectangle2D.Float(0, 0, width, height);

  //fill the rectangle with the translucent effect
  g2.fill(rect);

  g2.dispose();
}

/** @param alpha this is ignored... kept to have same method signature as {@link #drawShadeNormal} */
public static void drawShadeFast(Graphics g, int width, int height, Color[] twoGradientTheme, float alpha) {
	throw new UnsupportedOperationException();
/*	
  Graphics2D g2 = ImageUtils.g2(g);

  AlphaComposite c = AlphaComposite.SrcOver.derive(alpha);
  g2.setComposite(c);

  // create a rect rectangle
  Shape rect = new Rectangle2D.Float(0, 0, width, height);

  //fill the rectangle with the translucent effect
  JideSwingUtilities.fillGradient(g2, rect, twoGradientTheme[0], twoGradientTheme[1], true);

  g2.dispose();
*/  
}

public static void drawShade4Stops(Graphics g, int width, int height, Color[] fourGradientTheme, float alpha) {
  Graphics2D g2 = ImageUtils.g2(g);

//  applySmoothDrawingSettings(g2);

  AlphaComposite c = AlphaComposite.SrcOver.derive(alpha);
  g2.setComposite(c);

  LinearGradientPaint p;

  p = new LinearGradientPaint(0.0f, 0.0f, 0, height,
                              new float[]{0.0f, 0.49f, 0.5f, 1.0f},
                              fourGradientTheme);

  g2.setPaint(p);

  // create a rectangle
  Shape rect = new Rectangle2D.Float(0, 0, width, height);

  //fill the rectangle with the translucent effect
  g2.fill(rect);

  g2.dispose();
}

}//end class DrawingUtils
