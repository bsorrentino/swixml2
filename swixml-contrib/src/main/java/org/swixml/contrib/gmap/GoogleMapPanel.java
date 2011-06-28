package org.swixml.contrib.gmap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.HttpURLConnection;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.swixml.LogUtil;

import Task.ProgressMonitor.InputStreamUIHookSupport;
import Task.ProgressMonitor.ProgressMonitorUtils;
import Task.ProgressMonitor.SwingUIHookAdapter;
import Task.Support.CoreSupport.ByteBuffer;
import Task.Support.GUISupport.ImageUtils;

@SuppressWarnings("serial")
public class GoogleMapPanel extends JPanel  {
	
	private final MapLookup lookup = new MapLookup();

	private JLabel lblImageMap = new JLabel();
	private JProgressBar progressBar = new JProgressBar(0,100);
	
	private boolean useProgressBar = true;
	private final Border pbBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	
	public GoogleMapPanel() {
		super( new BorderLayout() );
		
		progressBar.setValue(progressBar.getMinimum());
		progressBar.setVisible(true);
		progressBar.setBorder(pbBorder);
		progressBar.setStringPainted(true);
		
		this.add( lblImageMap, BorderLayout.CENTER);

		
		lblImageMap.addMouseListener(new MouseListener() {
		    public void mouseClicked(MouseEvent e) {}
		    public void mousePressed(MouseEvent e) { /*frame.dispose();*/ }
		    public void mouseReleased(MouseEvent e) { }
		    public void mouseEntered(MouseEvent e) { }
		    public void mouseExited(MouseEvent e) { }
		  });
		
	}

	public final String getMapToolTipText() {
		return this.lblImageMap.getToolTipText();
	}

	public final void setMapToolTipText(String value) {
		this.lblImageMap.setToolTipText(value);
	}

	public final boolean isUseProgressBar() {
		return useProgressBar;
	}


	/**
	 * 
	 * @param value true use a progressbar otherwise use a default ProgressMonitor
	 */
	public final void setUseProgressBar(boolean value) {
		this.useProgressBar = value;
	}

	public final MapLookup getLookup() {
		return lookup;
	}


	@Override
	public void addNotify() {
		super.addNotify();
		
	}

	@SuppressWarnings("unchecked")
	private static <T extends Component> T findComponent( Container c , Class<T> type, String name) {
		for( int i = 0; i < c.getComponentCount(); ++i ) {
			Component cc = c.getComponent(i);
			
			if( type.isInstance(cc) ) {
				if( name==null || name.equalsIgnoreCase(cc.getName())) 
					return (T)cc;
			}
			if( cc instanceof Container ) {
				cc =  findComponent((Container)cc, type, name);
				
				if( cc!=null ) return (T)cc;
			}
		}
		
		return null;
	}
	
	public	static void loadMap( Window w, String panelName, String latitude, String longitude, int zoom, MapMarker ...markers ) {

		GoogleMapPanel panel = findComponent(w, GoogleMapPanel.class, panelName)	;
		
		if( panel!=null ) {
			panel.loadMap(latitude, longitude, zoom, markers);
		}
	}
	
	public	static void loadMap( Window w, String latitude, String longitude, int zoom, MapMarker ...markers ) {
		loadMap(w, null, latitude, longitude, zoom, markers);
	}
	
	protected void loadMap( String latitude, String longitude, int zoom, MapMarker ...markers ) {
		
		  Dimension sz = new Dimension();
		  
		  this.getSize(sz);
		  
	      // get the uri for the static map
	      String uri = lookup.getMap(Double.parseDouble(latitude),
	                                    Double.parseDouble(longitude),
	                                    sz.width,
	                                    sz.height,
	                                    zoom,
	                                    markers
	      								);

    	  try {

			java.net.URL url = new java.net.URL(uri);

			java.io.InputStream is = null;

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.connect();

			if (!isUseProgressBar()) {

				///////////////////////////////////
				// USE PROGRESS MONITOR FOR DEFAULT
				//////////////////////////////////
				is = new ProgressMonitorInputStream(
						SwingUtilities.getWindowAncestor(this),
						"Loading Map .... ", conn.getInputStream());

			} else {
				add( progressBar, BorderLayout.SOUTH);
				doLayout();

				SwingUIHookAdapter hook = new SwingUIHookAdapter();

				hook.addRecieveStatusListener(new PropertyChangeListener() {

					public void propertyChange(PropertyChangeEvent evt) {
						
						progressBar.setValue(ProgressMonitorUtils.parsePercentFrom(evt));
						progressBar.setString(ProgressMonitorUtils.parseMessageFrom(evt));	
						
					}
				});
				
				hook.addUnderlyingIOStreamInterruptedOrClosed( new PropertyChangeListener() {
					
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO
					}
				});

				is = new InputStreamUIHookSupport(
						InputStreamUIHookSupport.Type.RecvStatus,
						hook == null ? null : hook.getUIHook(), conn);


			}

			ByteBuffer data = new ByteBuffer(is);

			BufferedImage _img = ImageUtils.toCompatibleImage(ImageIO.read(data
					.getInputStream()));

			lblImageMap.setIcon(new ImageIcon(_img));	      
	      
			remove( progressBar );
			
	      }
    	  catch( IOException ioex ) {
  	        final String msg = String.format("error opening google map URI\n[%s]", uri);
			LogUtil.logger.warning( msg );
			lblImageMap.setText(msg);
    		  
    	  }
	      catch (Exception e) {
	        final String msg = String.format("The URI is not an image. Data is downloaded, can't display it as an image.\n[%s]", uri);
			LogUtil.logger.warning(msg );
			lblImageMap.setText(msg);
	      }
    	  finally {
    		  // TODO 
    	  }
	}
	
	
	public static void main( String args[] ) throws Exception {
		
		JFrame frame = new JFrame();
		
		final GoogleMapPanel panel = new GoogleMapPanel();
		
		panel.setMapToolTipText("THE MAP");
		panel.setUseProgressBar(true); 
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout( new BorderLayout() );
		frame.add( panel, BorderLayout.CENTER );
		
		frame.pack();
		frame.setSize( 600, 600);		
		frame.setVisible(true);
		
		GoogleMapPanel.loadMap(frame, "40.9166667", "14.7333333", 11);
		
		
	}

}
