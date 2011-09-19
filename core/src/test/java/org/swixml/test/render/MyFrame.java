package org.swixml.test.render;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {

	public MyFrame() throws HeadlessException {
	}

	public MyFrame(GraphicsConfiguration arg0) {
		super(arg0);
	}

	public MyFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	public MyFrame(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
	}

	@Override
	public void setSize(Dimension d) {
		System.out.printf( "setSize dimension=[%s]\n", d.toString());
		super.setSize(d);
	}

	@Override
	public void setSize(int width, int height) {
		System.out.printf( "setSize( %d, %d )\n", width, height);
		super.setSize(width, height);
	}

}
