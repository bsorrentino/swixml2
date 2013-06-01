package swixml.tool;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.jdesktop.application.Action;
import org.jdesktop.application.LocalStorage;
import org.swixml.LogAware;
import org.swixml.LogUtil;
import org.swixml.SwingEngine;
import org.swixml.factory.BeanFactory;
import org.swixml.jsr296.SWIXMLApplication;


public class SwixmlTestApplication extends SWIXMLApplication implements LogAware {

    private static final String STORAGE = "swixmltool.storage";

    @SuppressWarnings("serial")
    public static class Settings implements Serializable {
        public Settings() {}

        private String lastSelectedFolder = null;
        private String lastSelectedResoureFolder = null;

        public String getLastSelectedFolder() {
            return lastSelectedFolder;
        }

        public void setLastSelectedFolder(String lastSelectedFolder) {
            this.lastSelectedFolder = lastSelectedFolder;
        }

        public String getLastSelectedResoureFolder() {
            return lastSelectedResoureFolder;
        }

        public void setLastSelectedResoureFolder(String lastSelectedResoureFolder) {
            this.lastSelectedResoureFolder = lastSelectedResoureFolder;
        }


    }

    Settings settings = null;

    @SuppressWarnings("serial")
    public class MainFrame extends JDialog {

        
        String currentSize;
        ClassloaderBuilder loader = new ClassloaderBuilder();

        
        final JFileChooser fc = new JFileChooser();

        public MainFrame() {
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setMultiSelectionEnabled(true);
                
                //loadResource( getResourceObject() );

        }

        public java.util.List<URL> getResourceList() {
        
            return loader.getUrls();
        } 
        
        public String getCurrentSize() {
            return currentSize;
        }

        public void setCurrentSize(Dimension value) {
            String oldValue = this.currentSize;
            this.currentSize = String.format("[w:%d,h:%d]", (int)value.getWidth(), (int)value.getHeight());
            firePropertyChange("currentSize", oldValue, currentSize);
        }


        public final File getFileObject() {
            String folder = getFile();
            return (folder==null) ? new File(".") : new File(folder);
	}

        public final File getResourceObject() {
            String folder = getResource();
            return (folder==null) ? new File(".") : new File(folder);
	}

        public final String getResource() {
            return settings.lastSelectedResoureFolder;
        }

        public final void setResource( String value ) {
                String oldValue = settings.getLastSelectedResoureFolder();
                settings.setLastSelectedResoureFolder(value);

                firePropertyChange( "resource", oldValue, settings.getLastSelectedResoureFolder());

        }

        public final String getFile() {
        	return settings.getLastSelectedFolder();
        }

        public final void setFile(String value) {
                String oldValue = settings.getLastSelectedFolder();
                settings.setLastSelectedFolder(value);
                firePropertyChange( "file", oldValue, settings.getLastSelectedFolder());
        }

        private void loadResource( File[] files ) {
            if( null==files || files.length==0 ) return;

            try {
                
                for( File file : files ) {
                    loader.add( file );
                }
                
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "error loading resource", ex);
            }


        }

	@Action
        public void resource() {
            fc.setCurrentDirectory( getResourceObject() );
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] files = fc.getSelectedFiles();
                //This is where a real application would open the file.


                loadResource( files );
                
                setResource( files[0].getAbsolutePath() );

            }
            //firePropertyChange("fileSelected", null, null);

        }
        
        @Action
        public void browse() {
			
            fc.setCurrentDirectory( getFileObject() );
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.

                setFile( file.getAbsolutePath() );

            }
            firePropertyChange("fileSelected", null, null);
        }
		
        public boolean isFileSelected() {
            return settings.lastSelectedFolder!=null;
        }
		
        @Action(enabledProperty="fileSelected")
        public void submit() {

            File f = getFileObject();

            try {

            final ClassLoader cl = loader.build();
            final SwingEngine<Container> engine = new SwingEngine<Container>( null, cl );
            
            try {
            engine.getTaglib().loadSPITags(cl);
            } catch( Throwable e ) {
                logger.log( Level.WARNING, "taglib looading error", e);
            }
            
            engine.getTaglib().registerUnknowTagFactory( new BeanFactory( JPanel.class ) );
            
            final Container c = engine.render(f);

            c.addComponentListener( new ComponentListener() {

                public void componentResized(ComponentEvent e) {
                    setCurrentSize( c.getSize() );

                }

                public void componentMoved(ComponentEvent e) {
                }

                public void componentShown(ComponentEvent e) {
                }

                public void componentHidden(ComponentEvent e) {
                }
            });

            if( c instanceof JDialog ) {
                JDialog dlg = (JDialog)c;
                dlg.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
                dlg.setResizable(true);
                dlg.setUndecorated(false);
                dlg.setModal(true);
            }

                    c.setVisible(true);

            } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
            }
        }
		
        @Action
        public void close() {
                exit();
        }
    }


    @Override
    protected void shutdown() {
        if( settings!=null ) {
            try {
                LocalStorage storage = getContext().getLocalStorage();
                storage.save(settings, STORAGE);
            } catch (IOException ex) {
                LogUtil.logger.log(Level.SEVERE, "settings saving error", ex);
            }
        }
        super.shutdown();
    }

    @Override
    protected void initialize(String[] args) {
        try {
            super.initialize(args);
            LocalStorage storage = getContext().getLocalStorage();

            settings = (Settings) storage.load( STORAGE );
            if( settings==null) {
                settings = new Settings();
            }
        } catch (IOException ex) {

            LogUtil.logger.log(Level.SEVERE, "initialization error", ex);

            settings = new Settings();
        }
    }
	
	@Override
	protected void startup() {

		try {
			JDialog frame = render( new MainFrame() );
			show( frame );
	        Rectangle rc = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			frame.setBounds(rc.x, rc.y, 600, 130);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                System.setProperty(SwingEngine.DESIGN_TIME, "true");


	}

	public static void main( String args[] ) {
		SWIXMLApplication.launch(SwixmlTestApplication.class, args);
	}
}
