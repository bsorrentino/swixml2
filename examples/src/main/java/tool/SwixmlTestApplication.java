package tool;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.jdesktop.application.Action;
import org.jdesktop.application.LocalStorage;
import org.swixml.LogUtil;
import org.swixml.SwingEngine;
import org.swixml.jsr296.SwingApplication;


public class SwixmlTestApplication extends SwingApplication {

    private static final String STORAGE = "swixmltool.storage";

    @SuppressWarnings("serial")
	public static class Settings implements Serializable {
        public Settings() {}

        private String lastSelectedFolder = null;

        public String getLastSelectedFolder() {
            return lastSelectedFolder;
        }

        public void setLastSelectedFolder(String lastSelectedFolder) {
            this.lastSelectedFolder = lastSelectedFolder;
        }


    }

    Settings settings = null;

	@SuppressWarnings("serial")
	public class MainFrame extends JDialog {

        String currentSize;

		final JFileChooser fc = new JFileChooser();
		
		public MainFrame() {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		}

        public String getCurrentSize() {
            return currentSize;
        }

        public void setCurrentSize(Dimension value) {
            String oldValue = this.currentSize;
            this.currentSize = value.toString();
            firePropertyChange("currentSize", oldValue, currentSize);
        }


        public final String getFile() {
			return settings.getLastSelectedFolder();
		}

		public final void setFile(String value) {
			String oldValue = settings.getLastSelectedFolder();
			settings.setLastSelectedFolder(value);
			firePropertyChange( "file", oldValue, settings.getLastSelectedFolder());
		}
		
		
		@Action
		public void browse() {
            fc.setCurrentDirectory( new File(getFile()) );
			int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            //This is where a real application would open the file.
	            
	            setFile( file.getPath() );
	            
	        }
            firePropertyChange("fileSelected", null, null);
		}
		
		public boolean isFileSelected() {
			return settings.lastSelectedFolder!=null;
		}
		
		@Action(enabledProperty="fileSelected")
		public void submit() {
			
			File f = new File( getFile() );
		
			try {

                final SwingEngine<Container> engine = new SwingEngine<Container>( null );
                engine.setClassLoader( getClass().getClassLoader() );

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
            exit();
        }
    }
	
	@Override
	protected void startup() {

		try {
			JDialog frame = render( new MainFrame(), "examples/SwixmlTestDialog.xml");
			show( frame );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static void main( String args[] ) {
		SwingApplication.launch(SwixmlTestApplication.class, args);
	}
}
