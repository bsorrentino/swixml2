package Task.Support.GUISupport;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.swixml.contrib.gmap.Validate;

import Task.Support.CoreSupport.ByteBuffer;
import Task.Support.CoreSupport.ZipObject;
import Task.Support.CoreSupport.ZipObjectException;

/**
 * ImageUtils allows icons stored in jar files on the classpaths to be loaded as ImageIcon objects
 * and caches them
 *
 * @author Nazmul Idris
 * @since Jan 3, 2007, 2:06:54 PM
 */
public class ImageUtils {
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constants
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public static HashMap<String, BufferedImage> bufferedImageCache = new HashMap<String, BufferedImage>();
public static ReadWriteLock bufferedImageCacheLock = new ReentrantReadWriteLock();

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// Graphics manipulation
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static Graphics2D g2(Graphics g) {
  return DrawingUtils.applySmoothDrawingSettings((Graphics2D) g.create());
}

public static Graphics2D g2plain(Graphics g) {
  return (Graphics2D) g.create();
}

public static Graphics2D g2(BufferedImage img) {
  return DrawingUtils.applySmoothDrawingSettings(img.createGraphics());
}

public static Graphics2D g2(SplashScreen img) {
  return DrawingUtils.applySmoothDrawingSettings(img.createGraphics());
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// saving and loading images from ByteBuffers
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * Encodes the BufferedImage into a PNG file and writes that to a ByteBuffer.
 *
 * @param img source bufferedimage
 *
 * @return PNG encoded bytebuffer
 *
 * @throws IOException if there is a problem with encoding the image to PNG, this exception is
 *                     thrown
 * @see #decodeBufferedImage(ZipObject)
 */
public static ZipObject<ByteBuffer> encodeBufferedImage(BufferedImage img)
    throws IllegalArgumentException, IOException, ZipObjectException
{
  Validate.notNull(img, "BufferedImage can not be null");

  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  ImageIO.write(img, "png", baos);
  return new ZipObject<ByteBuffer>(new ByteBuffer(baos.toByteArray()));
}

/**
 * Decodes the PNG-encoded-ByteBuffer into a BufferedImage.
 *
 * @param zipObject PNG encoded bytebuffer
 *
 * @return BufferedImage
 *
 * @throws IOException if there is a problem with decoding the image from PNG, this exception is
 *                     thrown
 * @see #encodeBufferedImage(BufferedImage)
 */
public static BufferedImage decodeBufferedImage(ZipObject<ByteBuffer> zipObject)
    throws IllegalArgumentException, IOException, ZipObjectException
{
  Validate.notNull(zipObject, "ZipObject can not be null");

  ByteBuffer bb = zipObject.getObject();
  return ImageIO.read(bb.getInputStream());
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// scaling math...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/**
 * This method gets a scaling factor that you can apply to a source image to make it fit the bounds
 * of the target W/H, while maintaining the aspect ratio. If you do not need to maintain aspect
 * ratio, then just scale the image to the target W/H, you do not need to use this method!
 */
public static float scaleToFit(int sourceW, int sourceH,
                               int targetW, int targetH,
                               boolean respectSourceAspectRatio)
{
  if (respectSourceAspectRatio) {
    float scaleX = (targetW * 1.0f) / (sourceW * 1.0f);
    float scaleY = (targetH * 1.0f) / (sourceH * 1.0f);
    return (scaleX < scaleY)
           ? scaleX
           : scaleY;
  }
  else {
    //since the image is going to be clobbered, does not matter if you pick W or H
    return (sourceW * 1.0f) / (targetW * 1.0f);
  }
}

/**
 * uses {@link #scaleToFit(int,int,int,int,boolean)} to figure out the final width/height of the
 * source image that should be fit in the target width/height provided, while maintaining the aspect
 * ration of the source image.
 */
public static Dimension scaleToFit(int sourceW, int sourceH,
                                   int targetW, int targetH)
{
  float scaleToFitFactor = scaleToFit(sourceW, sourceH,
                                      targetW, targetH, true);
  return new Dimension(
      (int) (scaleToFitFactor * sourceW * 1.0f),
      (int) (scaleToFitFactor * sourceH * 1.0f)
  );
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// component imaging utilities...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static BufferedImage getImageOfComponent(JComponent comp) {
  BufferedImage newImg = createCompatibleImage(comp.getWidth(), comp.getHeight(), false);

//  Graphics2D g2 = (Graphics2D) newImg.getGraphics();
  Graphics2D g2 = g2(newImg);

  comp.paint(g2);

  g2.dispose();

  return newImg;
}

public static BufferedImage getImageOfComponent(JComponent comp, boolean opaque) {
  BufferedImage newImg = createCompatibleImage(comp.getWidth(), comp.getHeight(), opaque);

//  Graphics2D g2 = (Graphics2D) newImg.getGraphics();
  Graphics2D g2 = g2(newImg);

  comp.paint(g2);

  g2.dispose();

  return newImg;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// Compatible BufferedImage creation convenience methods...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/** returns a copy of a non-compatible BufferedImage as a compatible one */
public static BufferedImage toCompatibleImage(BufferedImage image) {
  GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
  GraphicsDevice d = e.getDefaultScreenDevice();
  GraphicsConfiguration c = d.getDefaultConfiguration();
  BufferedImage compatibleImage = c.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
//  Graphics gImg = compatibleImage.getGraphics();
  Graphics gImg = g2(compatibleImage);
  gImg.drawImage(image, 0, 0, null);
  gImg.dispose();
  return compatibleImage;
}

/**
 * convenience method for creating an opaque or transparent compatible BufferedImage
 *
 * @param opaque true = opaque, false = transparent
 */
public static BufferedImage createCompatibleImage(int width, int height, boolean opaque) {
  GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
  GraphicsDevice d = e.getDefaultScreenDevice();
  GraphicsConfiguration c = d.getDefaultConfiguration();
  BufferedImage compatibleImage = c.createCompatibleImage(width,
                                                          height,
                                                          opaque
                                                          ? Transparency.OPAQUE
                                                          : Transparency.TRANSLUCENT);
  return compatibleImage;
}

/** convenience constructor for creating a compatible BufferedImage given the Transparency setting */
public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
  GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
  GraphicsDevice d = e.getDefaultScreenDevice();
  GraphicsConfiguration c = d.getDefaultConfiguration();
  BufferedImage compatibleImage = c.createCompatibleImage(width, height, transparency);
  return compatibleImage;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// BufferedImage manipulation convenience methods...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/**
 * returns a copy of the given buffered image with preferred transparency setting (regardless of
 * whether the orginal BufferedImage has any transparency in it or not.
 */
public static BufferedImage getCopyOfBufferedImage(BufferedImage img, boolean opaque) {
  BufferedImage newImg = createCompatibleImage(img.getWidth(), img.getHeight(),
                                               opaque
                                               ? Transparency.OPAQUE
                                               : Transparency.TRANSLUCENT);
//  Graphics2D g2 = (Graphics2D) newImg.getGraphics();
  Graphics2D g2 = g2(newImg);

  g2.drawImage(img, 0, 0, null);
  g2.dispose();

  return newImg;
}

/** simply returns a copy of the given BufferedImage */
public static BufferedImage getCopyOfBufferedImage(BufferedImage img) {
  int type = (img.getTransparency() == Transparency.OPAQUE)
             ?
             BufferedImage.TYPE_INT_RGB
             : BufferedImage.TYPE_INT_ARGB;
  BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), type);
//  Graphics2D g2 = (Graphics2D) newImg.getGraphics();
  Graphics2D g2 = g2(newImg);

  g2.drawImage(img, 0, 0, null);
  g2.dispose();

  return newImg;
}

/** Utility method provided to convert Images to BufferedImages. */
public BufferedImage createBufferedImage(Image image) {
  if (image instanceof BufferedImage) {
    return (BufferedImage) image;
  }
  BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                                                  BufferedImage.TYPE_INT_ARGB); // ARGB to support transparency if in original image
//  Graphics2D gImg = bufferedImage.createGraphics();
  Graphics2D gImg = g2(bufferedImage);

  gImg.drawImage(image, 0, 0, null);
  gImg.dispose();

  return bufferedImage;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// Image loading methods (from classpath/JAR file)...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

/**
 * Tries to load the <imageName>.png file from the classpath (should be in the JAR files for the
 * app) or the cache (if it's avaialble in there). Also, this method adds the image to the cache (if
 * it has to be loaded from the classpath).
 *
 * @param imageName name of image file.
 *
 * @throws ClassNotFoundException   if the imageName.png file could not be found
 * @throws IllegalArgumentException if the imageName is empty or null
 */
public static BufferedImage loadBufferedImage(String imageName, boolean blur, float translucency)
    throws ClassNotFoundException, IllegalArgumentException
{
  Validate.notEmpty(imageName, "imageName can not be null or empty");

  if (translucency < 0.0f || translucency > 1.0f) {
    throw new IllegalArgumentException("transluceny is invalid");
  }

  BufferedImage bufImg = null;

  //see if the image exists in the cache
  Lock readLock = bufferedImageCacheLock.readLock();
  try {
    readLock.lock();
    if (bufferedImageCache.containsKey(imageName)) {
      bufImg = bufferedImageCache.get(imageName);
    }
  }
  finally {
    readLock.unlock();
  }

  //if not in cache, load it and put it in the cache
  if (bufImg == null) {
    Lock writeLock = bufferedImageCacheLock.writeLock();
    try {
      writeLock.lock();
      bufImg = toCompatibleImage(ImageIO.read(ClassLoader.getSystemResourceAsStream(imageName)));
      bufferedImageCache.put(imageName, bufImg);
    }
    catch (Exception e) {
      throw new ClassNotFoundException("'" + imageName + "' not found in classpath", e);
    }
    finally {
      writeLock.unlock();
    }
  }

  // apply blur and translucency
  bufImg = blur
           ? DrawingUtils.getBlurredImage(bufImg)
           : bufImg;
  bufImg = (translucency == 1.0f)
           ? bufImg
           : DrawingUtils.getTranslucentImage(bufImg, translucency);
  return bufImg;
}

/**
 * This returns a scaled version of the original icon image. Some source icons are quite large. This
 * method scales them to the appropriate width and height that is specified in the input params.
 */
public static BufferedImage getScaledBufferedImage(BufferedImage img, int targetWidth, int targetHeight)
    throws IllegalArgumentException
{
  Validate.notNull(img, "BufferedImage can not be null");

  try {
    return createHighResThumbnail(img, targetWidth, targetHeight, true);
  }
  catch (Exception e) {
    return createFastResizedCopy(img, targetWidth, targetHeight, true);
  }
}

/**
 * This returns a scaled version of the original icon image. Some source icons are quite large. This
 * method scales them to the appropriate width and height that is specified in the input params.
 * <p/>
 * You can specify if you want the image blurred and translucent as well.
 */
public static BufferedImage getScaledBufferedImage(BufferedImage img, int targetWidth, int targetHeight,
                                                   boolean blur, float translucency)
    throws IllegalArgumentException
{
  Validate.notNull(img, "BufferedImage can not be null");

  BufferedImage bufImg = getScaledBufferedImage(img, targetWidth, targetHeight);

  bufImg = blur
           ? DrawingUtils.getBlurredImage(bufImg)
           :
           bufImg;

  bufImg = (translucency == 1.0f)
           ? bufImg
           :
           DrawingUtils.getTranslucentImage(bufImg, translucency);

  return bufImg;
}

/**
 * Resizes the originalImage to fit the scaled Width and Height. It preserves the alpha channel
 * (transparency) of the original image. This is a very fast operation, and may not produce the best
 * looking results. Look at {@link #createHighResThumbnail} for details on creating high res
 * thumbnails.
 *
 * @return scaled BufferedImage
 */
public static BufferedImage createFastResizedCopy(BufferedImage originalImage,
                                                  int scaledWidth, int scaledHeight,
                                                  boolean keepAspectRatio)
{
  // scales the image, while respecting the aspect ratio
  if (keepAspectRatio) {
    Dimension d = ImageUtils.scaleToFit(originalImage.getWidth(), originalImage.getHeight(), scaledWidth, scaledHeight);
    scaledWidth = d.width;
    scaledHeight = d.height;
  }

  int type = (originalImage.getTransparency() == Transparency.OPAQUE)
             ?
             BufferedImage.TYPE_INT_RGB
             : BufferedImage.TYPE_INT_ARGB;

  BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, type);
//  Graphics2D g = scaledBI.createGraphics();
  Graphics2D g = g2(scaledBI);

  g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                     RenderingHints.VALUE_INTERPOLATION_BICUBIC);

  if (type == BufferedImage.TYPE_INT_ARGB) {
    g.setComposite(AlphaComposite.Src);
  }

  g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
  g.dispose();
  return scaledBI;
}

/**
 * This creates a high quality thumbnail of the given image. The newWidth and newHeight must be
 * smaller than the image's width and height, otherwise an exception is thrown.
 */
public static BufferedImage createHighResThumbnail(BufferedImage image,
                                                   int newWidth, int newHeight,
                                                   boolean keepAspectRatio)
{
  int width = image.getWidth();
  int height = image.getHeight();

  // scales the image, while respecting the aspect ratio
  if (keepAspectRatio) {
    Dimension d = ImageUtils.scaleToFit(width, height, newWidth, newHeight);
    newWidth = d.width;
    newHeight = d.height;
  }

  boolean isTranslucent = image.getTransparency() != Transparency.OPAQUE;

  if (newWidth >= width || newHeight >= height) {
    throw new IllegalArgumentException("newWidth and newHeight cannot" +
                                       " be greater than the image" +
                                       " dimensions");
  }
  else if (newWidth <= 0 || newHeight <= 0) {
    throw new IllegalArgumentException("newWidth and newHeight must" +
                                       " be greater than 0");
  }

  BufferedImage thumb = image;
  BufferedImage temp = null;

  Graphics2D g2 = null;

  int previousWidth = width;
  int previousHeight = height;

  do {
    if (width > newWidth) {
      width /= 2;
      if (width < newWidth) {
        width = newWidth;
      }
    }

    if (height > newHeight) {
      height /= 2;
      if (height < newHeight) {
        height = newHeight;
      }
    }

    if (temp == null || isTranslucent) {
      temp = createCompatibleImage(width, height, !isTranslucent);
//      g2 = temp.createGraphics();
      g2 = g2(temp);
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }
    g2.drawImage(thumb, 0, 0, width, height,
                 0, 0, previousWidth, previousHeight, null);

    previousWidth = width;
    previousHeight = height;

    thumb = temp;
  }
  while (width != newWidth || height != newHeight);

  g2.dispose();

  if (width != thumb.getWidth() || height != thumb.getHeight()) {
    temp = createCompatibleImage(width, height, !isTranslucent);
//    g2 = temp.createGraphics();
    g2 = g2(temp);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(thumb, 0, 0, null);
    g2.dispose();
    thumb = temp;
  }

  return thumb;
}

public static Icon loadScaledBufferedIcon(String icon, int width, int height, boolean blur, float transparency)
    throws ClassNotFoundException
{
  BufferedImage img = loadBufferedImage(icon, blur, transparency);
  img = getScaledBufferedImage(img, width, height);
  return new ImageIcon(img);
}
}//end ImageUtils
