package examples.explorer;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.MutableTreeNode;

import legacy.Accelerator;
import legacy.Actions;
import legacy.Cards;
import legacy.CustomTags;

import org.jdesktop.application.Action;
import org.swixml.jsr296.SwingApplication;

import examples.ComboApplication;
import examples.util.GenericTreeModel;


public class Swixml2Explorer extends SwingApplication  {

	static final Logger logger = Logger.getAnonymousLogger();
	
	public static class PanelInfo {
	
		public final Class<?> panelClass;
		public final String swixmlUri;
		public final String name;
		public final String sourceCodeUri; 
		private String xmlPanel = "examples/explorer/Content.xml";
		
		public PanelInfo( String name, Class<?> panelClass, String swixmlUri ) {
			super();
			this.name = name;
			this.panelClass = panelClass;
			this.swixmlUri = swixmlUri;
			this.sourceCodeUri = null;
		}
		public PanelInfo( String name, Class<?> panelClass, String swixmlUri, String sourceCodeUri ) {
			super();
			this.name = name;
			this.panelClass = panelClass;
			this.swixmlUri = swixmlUri;
			this.sourceCodeUri = sourceCodeUri;
		}

		public final String getXmlPanel() {
			return xmlPanel;
		}

		public final void setXmlPanel(String xmlPanel) {
			if( null==xmlPanel ) throw new IllegalArgumentException("xmlPanels null!");
			this.xmlPanel = xmlPanel;
		}

		@Override
		public String toString() {
			return name;
		}
		
		
		
	}
	
	@SuppressWarnings("serial")
	public static class ContentPanel extends JPanel {
		
		private String xmlSource;
		private String javaSource;
		
		public ContentPanel( PanelInfo info ) {

			xmlSource = loadResource(info.swixmlUri);
			javaSource = loadResource(info.sourceCodeUri);
		}
		
		public final String getXmlSource() {
			return xmlSource;
		}
		public final void setXmlSource(String xmlSource) {
			this.xmlSource = xmlSource;
		}
		public final String getJavaSource() {
			return javaSource;
		}
		public final void setJavaSource(String javaSource) {
			this.javaSource = javaSource;
		}
		
		
	}
	
	@SuppressWarnings("serial")
	public class MyFrame extends JFrame {
		
		Set<PanelInfo> panels = new HashSet<PanelInfo>(100);
		
		private GenericTreeModel<PanelInfo> applications = new GenericTreeModel<PanelInfo>("applications");
		
		public JPanel contentPanel ;
		
		public MyFrame()  {
			
			{
				PanelInfo pp = new PanelInfo("Legacy",null,null);
				pp.setXmlPanel("examples/explorer/Legacy.xml");
				
				panels.add( pp );
				
				MutableTreeNode node =  applications.addNodeToRoot( pp, true );
				
				{
					PanelInfo p = new PanelInfo("Accelerator", Accelerator.class, "xml/accelerator.xml", "legacy/Accelerator.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Cards", Cards.class, "xml/cards.xml", "legacy/Cards.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("CustomTags", CustomTags.class, "xml/customtags.xml", "legacy/CustomTags.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Actions", Actions.class, "xml/actions.xml", "legacy/Actions.java");
					panels.add( p );
					applications.addNode(node, p );
				}
			}
			
			{
				PanelInfo pp = new PanelInfo("NewFeatures",null,null);
				pp.setXmlPanel("examples/explorer/NewFeatures.xml");
				
				panels.add( pp );
				
				MutableTreeNode node =  applications.addNodeToRoot( pp, true );
				{
					PanelInfo p = new PanelInfo("ComboBox", ComboApplication.class, "examples/ComboDialog.xml", "examples/ComboApplication.java");
					panels.add( p );
					applications.addNode(node, p );
				}
/*				
				{
					PanelInfo p = new PanelInfo("Cards", Cards.class, "xml/cards.xml", "legacy/Cards.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("CustomTags", CustomTags.class, "xml/customtags.xml", "legacy/CustomTags.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Actions", Actions.class, "xml/actions.xml", "legacy/Actions.java");
					panels.add( p );
					applications.addNode(node, p );
				}
*/				
			}
		}


		@Override
		public void addNotify() {
			
			for( PanelInfo p : panels ) {
				
				try {
					JPanel panel = Swixml2Explorer.this.render( new ContentPanel(p), p.getXmlPanel() );
					contentPanel.add( panel, p.name );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			super.addNotify();
		}

		public final GenericTreeModel<PanelInfo> getApplications() {
			return applications;
		}

		@Action
		public void selectNode( ActionEvent ev ) {
		
			logger.info( String.format( "select node [%s]\n", ev ));
			
			PanelInfo info = applications.getSelectedObject( (TreeSelectionEvent) ev.getSource());

			CardLayout l = (CardLayout) contentPanel.getLayout();
			l.show(contentPanel, info.name);
			
		}
		
		@Action
		public void runApplication( ActionEvent ev ) {
		
			logger.info( String.format("run application  [%s]\n", ev) );

			PanelInfo info = applications.getSelectedObject( (MouseEvent) ev.getSource());

			if( info.panelClass!=null ) {
				
				try {
					Method main = info.panelClass.getMethod("main", String[].class );
					
					Object args = new String[0];
					main.invoke(null, args);
					
				} catch (Exception e) {
					logger.log( Level.WARNING, "error on start application", e );
				}
			}
		}
		
	}

	
	@SuppressWarnings("unchecked")
	public static  <T extends Component> T getComponentByName( Container container, Class<? extends Component> clazz, String name ) {
		for( Component c : container.getComponents() ) {
			//System.out.printf( "component [%s]\n", c.getName() );
			if( name.equalsIgnoreCase(c.getName()) && clazz.isAssignableFrom(c.getClass())) {
				return (T)c;
			}
			else if( c instanceof Container ) {
				Component result = getComponentByName( (Container) c, clazz, name);
				if( null!=result ) {
					return (T)result;
				}
			}
		}
		return null;
	}

	public static String loadResource( String res ) {
		if( res==null ) return null;
		
		java.io.InputStream is = Swixml2Explorer.class.getClassLoader().getResourceAsStream(res);
		if( is==null ) {
			logger.warning( String.format("resource [%s] not found!", res));
		}
		else {
			java.io.StringWriter w = new java.io.StringWriter(4*1024);
			int c;
			try {
				while( -1!=(c=is.read()) ) {
					w.write(c);
				}
				w.flush();
				return w.toString();
			} catch (IOException e) {
				logger.log( Level.WARNING, "error reading resource", e );
			}
		}
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingApplication.launch(Swixml2Explorer.class, args);

	}

	@Override
	protected void startup() {
		try {

			MyFrame frame = render( new MyFrame(), "examples/Explorer.xml" );

			show( frame );
				
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}
