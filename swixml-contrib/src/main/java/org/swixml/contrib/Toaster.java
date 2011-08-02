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