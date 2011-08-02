package org.swixml.contrib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;

public class Toaster
{
  private int toasterWidth = 300;

  private int toasterHeight = 80;

  private int step = 20;

  private int stepTime = 20;

  private int displayTime = 3000;

  private int currentNumberOfToaster = 0;

  private int maxToaster = 0;
  private int maxToasterInSceen;
  private Font font;
  private Color borderColor;
  private Color toasterColor;
  private Color messageColor;
  int margin;
  boolean useAlwaysOnTop = true;
  private static final long serialVersionUID = 1L;

  public Toaster()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 36	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: sipush 300
    //   8: putfield 38	com/nitido/utils/toaster/Toaster:toasterWidth	I
    //   11: aload_0
    //   12: bipush 80
    //   14: putfield 40	com/nitido/utils/toaster/Toaster:toasterHeight	I
    //   17: aload_0
    //   18: bipush 20
    //   20: putfield 42	com/nitido/utils/toaster/Toaster:step	I
    //   23: aload_0
    //   24: bipush 20
    //   26: putfield 44	com/nitido/utils/toaster/Toaster:stepTime	I
    //   29: aload_0
    //   30: sipush 3000
    //   33: putfield 46	com/nitido/utils/toaster/Toaster:displayTime	I
    //   36: aload_0
    //   37: iconst_0
    //   38: putfield 48	com/nitido/utils/toaster/Toaster:currentNumberOfToaster	I
    //   41: aload_0
    //   42: iconst_0
    //   43: putfield 50	com/nitido/utils/toaster/Toaster:maxToaster	I
    //   46: aload_0
    //   47: iconst_1
    //   48: putfield 52	com/nitido/utils/toaster/Toaster:useAlwaysOnTop	Z
    //   51: aload_0
    //   52: new 54	java/awt/Font
    //   55: dup
    //   56: ldc 56
    //   58: iconst_1
    //   59: bipush 12
    //   61: invokespecial 59	java/awt/Font:<init>	(Ljava/lang/String;II)V
    //   64: putfield 61	com/nitido/utils/toaster/Toaster:font	Ljava/awt/Font;
    //   67: aload_0
    //   68: new 63	java/awt/Color
    //   71: dup
    //   72: sipush 245
    //   75: sipush 153
    //   78: bipush 15
    //   80: invokespecial 66	java/awt/Color:<init>	(III)V
    //   83: putfield 68	com/nitido/utils/toaster/Toaster:borderColor	Ljava/awt/Color;
    //   86: aload_0
    //   87: getstatic 71	java/awt/Color:WHITE	Ljava/awt/Color;
    //   90: putfield 73	com/nitido/utils/toaster/Toaster:toasterColor	Ljava/awt/Color;
    //   93: aload_0
    //   94: getstatic 76	java/awt/Color:BLACK	Ljava/awt/Color;
    //   97: putfield 78	com/nitido/utils/toaster/Toaster:messageColor	Ljava/awt/Color;
    //   100: aload_0
    //   101: iconst_1
    //   102: putfield 52	com/nitido/utils/toaster/Toaster:useAlwaysOnTop	Z
    //   105: getstatic 80	com/nitido/utils/toaster/Toaster:class$0	Ljava/lang/Class;
    //   108: dup
    //   109: ifnonnull +28 -> 137
    //   112: pop
    //   113: ldc 82
    //   115: invokestatic 88	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   118: dup
    //   119: putstatic 80	com/nitido/utils/toaster/Toaster:class$0	Ljava/lang/Class;
    //   122: goto +15 -> 137
    //   125: new 90	java/lang/NoClassDefFoundError
    //   128: dup_x1
    //   129: swap
    //   130: invokevirtual 96	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   133: invokespecial 99	java/lang/NoClassDefFoundError:<init>	(Ljava/lang/String;)V
    //   136: athrow
    //   137: ldc 101
    //   139: iconst_1
    //   140: anewarray 84	java/lang/Class
    //   143: dup
    //   144: iconst_0
    //   145: getstatic 103	com/nitido/utils/toaster/Toaster:class$1	Ljava/lang/Class;
    //   148: dup
    //   149: ifnonnull +28 -> 177
    //   152: pop
    //   153: ldc 105
    //   155: invokestatic 88	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   158: dup
    //   159: putstatic 103	com/nitido/utils/toaster/Toaster:class$1	Ljava/lang/Class;
    //   162: goto +15 -> 177
    //   165: new 90	java/lang/NoClassDefFoundError
    //   168: dup_x1
    //   169: swap
    //   170: invokevirtual 96	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   173: invokespecial 99	java/lang/NoClassDefFoundError:<init>	(Ljava/lang/String;)V
    //   176: athrow
    //   177: aastore
    //   178: invokevirtual 109	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   181: pop
    //   182: goto +9 -> 191
    //   185: astore_1
    //   186: aload_0
    //   187: iconst_0
    //   188: putfield 52	com/nitido/utils/toaster/Toaster:useAlwaysOnTop	Z
    //   191: return
    //
    // Exception table:
    //   from	to	target	type
    //   113	118	125	java/lang/ClassNotFoundException
    //   153	158	165	java/lang/ClassNotFoundException
    //   105	185	185	java/lang/Exception
  }

  public void showToaster(Icon icon, String msg)
  {
    SingleToaster singleToaster = new SingleToaster();
    if (icon != null)
    {
      singleToaster.iconLabel.setIcon(icon);
    }
    singleToaster.message.setText(msg);
    singleToaster.animate();
  }

  public void showToaster(String msg)
  {
    showToaster(null, msg);
  }

  public Font getToasterMessageFont()
  {
    return this.font;
  }

  public void setToasterMessageFont(Font f)
  {
    this.font = f;
  }

  public Color getBorderColor()
  {
    return this.borderColor;
  }

  public void setBorderColor(Color borderColor)
  {
    this.borderColor = borderColor;
  }

  public int getDisplayTime()
  {
    return this.displayTime;
  }

  public void setDisplayTime(int displayTime)
  {
    this.displayTime = displayTime;
  }

  public int getMargin()
  {
    return this.margin;
  }

  public void setMargin(int margin)
  {
    this.margin = margin;
  }

  public Color getMessageColor()
  {
    return this.messageColor;
  }

  public void setMessageColor(Color messageColor)
  {
    this.messageColor = messageColor;
  }

  public int getStep()
  {
    return this.step;
  }

  public void setStep(int step)
  {
    this.step = step;
  }

  public int getStepTime()
  {
    return this.stepTime;
  }

  public void setStepTime(int stepTime)
  {
    this.stepTime = stepTime;
  }

  public Color getToasterColor()
  {
    return this.toasterColor;
  }

  public void setToasterColor(Color toasterColor)
  {
    this.toasterColor = toasterColor;
  }

  public int getToasterHeight()
  {
    return this.toasterHeight;
  }

  public void setToasterHeight(int toasterHeight)
  {
    this.toasterHeight = toasterHeight;
  }

  public int getToasterWidth()
  {
    return this.toasterWidth;
  }

  public void setToasterWidth(int toasterWidth)
  {
    this.toasterWidth = toasterWidth;
  }

  class SingleToaster extends JWindow
  {
    private static final long serialVersionUID = 1L;
    private JLabel iconLabel = new JLabel();

    private JTextArea message = new JTextArea();

    public SingleToaster()
    {
      initComponents();
    }

    private void initComponents()
    {
      setSize(Toaster.this.toasterWidth, Toaster.this.toasterHeight);
      this.message.setFont(Toaster.this.getToasterMessageFont());
      JPanel externalPanel = new JPanel(new BorderLayout(1, 1));
      externalPanel.setBackground(Toaster.this.getBorderColor());
      JPanel innerPanel = new JPanel(new BorderLayout(Toaster.this.getMargin(), Toaster.this.getMargin()));
      innerPanel.setBackground(Toaster.this.getToasterColor());
      this.message.setBackground(Toaster.this.getToasterColor());
      this.message.setMargin(new Insets(2, 2, 2, 2));
      this.message.setLineWrap(true);
      this.message.setWrapStyleWord(true);

      EtchedBorder etchedBorder = (EtchedBorder)
        BorderFactory.createEtchedBorder();
      externalPanel.setBorder(etchedBorder);

      externalPanel.add(innerPanel);
      this.message.setForeground(Toaster.this.getMessageColor());
      innerPanel.add(this.iconLabel, "West");
      innerPanel.add(this.message, "Center");
      getContentPane().add(externalPanel);
    }

    public void animate()
    {
      new Toaster.Animation(this).start();
    }
  }

  class Animation extends Thread
  {
    Toaster.SingleToaster toaster;

    public Animation(Toaster.SingleToaster toaster)
    {
      this.toaster = toaster;
    }

    protected void animateVertically(int posx, int fromY, int toY)
      throws InterruptedException
    {
      this.toaster.setLocation(posx, fromY);
      if (toY < fromY)
      {
        for (int i = fromY; i > toY; i -= Toaster.this.step)
        {
          this.toaster.setLocation(posx, i);
          Thread.sleep(Toaster.this.stepTime);
        }
      }
      else
      {
        for (int i = fromY; i < toY; i += Toaster.this.step)
        {
          this.toaster.setLocation(posx, i);
          Thread.sleep(Toaster.this.stepTime);
        }
      }
      this.toaster.setLocation(posx, toY);
    }

    public void run()
    {
      try
      {
        boolean animateFromBottom = true;
        GraphicsEnvironment ge = 
          GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect = ge.getMaximumWindowBounds();

        int screenHeight = screenRect.height;

        if (screenRect.y > 0)
        {
          animateFromBottom = false;
        }

        Toaster.this.maxToasterInSceen = (screenHeight / Toaster.this.toasterHeight);

        int posx = screenRect.width - Toaster.this.toasterWidth - 1;

        this.toaster.setLocation(posx, screenHeight);
        this.toaster.setVisible(true);
        if (Toaster.this.useAlwaysOnTop)
        {
          this.toaster.setAlwaysOnTop(true);
        }
        int startYPosition;
        int stopYPosition;
        if (animateFromBottom)
        {
          /*int */startYPosition = screenHeight;
          /*int */stopYPosition = startYPosition - Toaster.this.toasterHeight - 1;
          if (Toaster.this.currentNumberOfToaster > 0)
          {
            stopYPosition -= Toaster.this.maxToaster % Toaster.this.maxToasterInSceen * Toaster.this.toasterHeight;
          }
          else
          {
            Toaster.this.maxToaster = 0;
          }
        }
        else
        {
          startYPosition = screenRect.y - Toaster.this.toasterHeight;
          stopYPosition = screenRect.y;

          if (Toaster.this.currentNumberOfToaster > 0)
          {
            stopYPosition += Toaster.this.maxToaster % Toaster.this.maxToasterInSceen * Toaster.this.toasterHeight;
          }
          else
          {
            Toaster.this.maxToaster = 0;
          }
        }

        Toaster.this.currentNumberOfToaster += 1;
        Toaster.this.maxToaster += 1;

        animateVertically(posx, startYPosition, stopYPosition);
        Thread.sleep(Toaster.this.displayTime);
        animateVertically(posx, stopYPosition, startYPosition);

        Toaster.this.currentNumberOfToaster -= 1;
        this.toaster.setVisible(false);
        this.toaster.dispose();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}