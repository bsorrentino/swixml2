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

	final JProgressBar progressBar = new JProgressBar(0,100);
	
	private boolean useInternalProgressBar = true;
	
	public GoogleMapPanel() {
		super( new BorderLayout() );

		progressBar.setValue(progressBar.getMinimum());
		progressBar.setVisible(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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

	public final boolean isUseInternalProgressBar() {
		return useInternalProgressBar;
	}

	public final void setUseInternalProgressBar(boolean useInternalProgressBar) {
		this.useInternalProgressBar = useInternalProgressBar;
	}

	public final String getMapToolTipText() {
		return this.lblImageMap.getToolTipText();
	}

	public final void setMapToolTipText(String value) {
		this.lblImageMap.setToolTipText(value);
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
	
	/**
	 * 
	 * @param w ancestor window
	 * @param panelName could be null 
	 * @return panel or null 
	 */
	public static GoogleMapPanel find( Window w, String panelName ) {

		return findComponent(w, GoogleMapPanel.class, panelName);
		
	}
	
	public	static GoogleMapPanel find( Window w) {
		return find(w, null);
	}
	
	public  void loadMap( String latitude, String longitude, int zoom, MapMarker ...markers ) {
		this.loadMap(null, latitude, longitude, zoom, markers);
	}
	
	public  void loadMap( final JProgressBar externalProgressBar, String latitude, String longitude, int zoom, MapMarker ...markers ) {

		  final boolean useInternal = ( externalProgressBar==null && isUseInternalProgressBar() );
		  final boolean useMonitor = (externalProgressBar==null && !isUseInternalProgressBar());
		  
		  Dimension sz = new Dimension();
		  
		  if( useInternal || useMonitor ) {
			  this.getSize(sz);
		  }
		  else {
			  this.lblImageMap.getSize(sz);
		  }
		  
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

			if (useMonitor) {

				///////////////////////////////////
				// USE PROGRESS MONITOR FOR DEFAULT
				//////////////////////////////////
				is = new ProgressMonitorInputStream(
						SwingUtilities.getWindowAncestor(this),
						"Loading Map .... ", conn.getInputStream());

			} else {
				
				final SwingUIHookAdapter hook = new SwingUIHookAdapter();

				if(useInternal) {
			
					add( progressBar, BorderLayout.SOUTH);
					doLayout();
				}
				

				hook.addRecieveStatusListener(new PropertyChangeListener() {

					public void propertyChange(PropertyChangeEvent evt) {
						
						if( useInternal ) {
							progressBar.setValue(ProgressMonitorUtils.parsePercentFrom(evt));
							progressBar.setString(ProgressMonitorUtils.parseMessageFrom(evt));	
						}
						else {
							externalProgressBar.setValue(ProgressMonitorUtils.parsePercentFrom(evt));
							externalProgressBar.setString(ProgressMonitorUtils.parseMessageFrom(evt));	
							
						}
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
	      
			if( useInternal ) {
				remove( progressBar );
				revalidate();
			}
			
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
		final JProgressBar progressBar = new JProgressBar(0,100);
		progressBar.setValue(progressBar.getMinimum());
		progressBar.setVisible(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		progressBar.setStringPainted(true);
		
		
		
		panel.setMapToolTipText("THE MAP");
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout( new BorderLayout() );
		frame.add( panel, BorderLayout.CENTER );
		frame.add( progressBar, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setSize( 600, 600);		
		frame.setVisible(true);
		
		
		GoogleMapPanel p = GoogleMapPanel.find(frame);
		
		// USE PROGRESS MONITOR
		p.setUseInternalProgressBar(false);
		p.loadMap("41.8500", "-87.6501", 11);
		
		Thread.sleep(5000);

		// USE EXTERNAL
		p.loadMap(progressBar, "40.9166667", "14.7333333", 11);
		
		Thread.sleep(5000);
		
		// USE INTERNAL
		p.setUseInternalProgressBar(true);
		//progressBar.setVisible(false);		
		p.loadMap("45.4643", "9.1895", 11);

		
		
	}

}
