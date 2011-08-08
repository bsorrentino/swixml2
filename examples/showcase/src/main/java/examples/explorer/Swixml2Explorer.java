package examples.explorer;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.MutableTreeNode;


import org.jdesktop.application.Action;
import org.swixml.SwingTagLibrary;
import org.swixml.XFrame;
import org.swixml.examples.wizard.DDLWizardApplication;
import org.swixml.jsr296.SwingApplication;

import examples.legacy.Accelerator;
import examples.legacy.Actions;
import examples.legacy.Cards;
import examples.legacy.CustomTags;
import examples.legacy.Form;
import examples.legacy.FormLayout;
import examples.legacy.GridBag;
import examples.legacy.Layout;
import examples.spinner.SpinnerExample;
import examples.table.TableExample;
import examples.task.BackgroundTaskExample;
import examples.text.TextExample;
import examples.tree.TreeExample;
import examples.util.GenericTreeModel;


public class Swixml2Explorer extends SwingApplication  {

	static final Logger logger = Logger.getAnonymousLogger();
	
	public static class PanelInfo {
	
		public final Class<?> panelClass;
		public final String swixmlUri;
		public final String name;
		public final String sourceCodeUri; 
		private String xmlPanel = "examples/explorer/Content.xml";
		
		public PanelInfo( String name, Class<?> panelClass ) {
			super();
			this.name = name;
			this.panelClass = panelClass;
			this.swixmlUri = null;
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
	public class MyFrame extends XFrame {
		
		Set<PanelInfo> panels = new HashSet<PanelInfo>(100);
		private Class<?> selectedApplicationClass = null;
		
		private final GenericTreeModel<PanelInfo> applications;
		public JPanel contentPanel ;
		
		public MyFrame()  {

			PanelInfo root = new PanelInfo( "Applications", null );
			root.setXmlPanel("examples/explorer/Summary.xml");
			panels.add( root );

			applications = new GenericTreeModel<PanelInfo>(root);
					
			
			{
				PanelInfo pp = new PanelInfo("Legacy", null);
				pp.setXmlPanel("examples/explorer/Legacy.xml");
				
				panels.add( pp );
				
				MutableTreeNode node =  applications.addNodeToRoot( pp, true );
				
				{
					PanelInfo p = new PanelInfo("Accelerator", Accelerator.class, "xml/accelerator.xml", "examples/legacy/Accelerator.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("CustomTags", CustomTags.class, "xml/customtags.xml", "examples/legacy/CustomTags.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Actions", Actions.class, "xml/actions.xml", "examples/legacy/Actions.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Cards", Cards.class, "xml/cards.xml", "examples/legacy/Cards.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("GridBag", GridBag.class, "xml/gridbag.xml", "examples/legacy/GridBag.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Layout", Layout.class, "xml/funlayout.xml", "examples/legacy/Layout.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Form", Form.class, "xml/form.xml", "examples/legacy/Form.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("FormLayout", FormLayout.class, "xml/formlayout.xml", "examples/legacy/FormLayout.java");
					panels.add( p );
					applications.addNode(node, p );
				}
			}
			
			{
				PanelInfo pp = new PanelInfo("New Features",null);
				pp.setXmlPanel("examples/explorer/NewFeatures.xml");
				
				panels.add( pp );
				
				MutableTreeNode node =  applications.addNodeToRoot( pp, true );
				{
					PanelInfo p = new PanelInfo("Layout", LayoutExample.class, "examples/explorer/LayoutFrame.xml", "examples/explorer/LayoutExample.java");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Combo Example", ComboExample.class, "examples/explorer/ComboDialog.xml", "examples/explorer/ComboExample.java");
					panels.add( p );
					applications.addNode(node, p );
				}
			
				{
					PanelInfo p = new PanelInfo("Table Binding Example", TableExample.class);
					p.setXmlPanel("examples/table/TableDialogContent.xml");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Tree Example", TreeExample.class);
					p.setXmlPanel("examples/tree/TreeDialogContent.xml");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Dialog Example", LoginExample.class);
					p.setXmlPanel("examples/explorer/LoginDialogContent.xml");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Background task Example", BackgroundTaskExample.class);
					p.setXmlPanel("examples/task/BackgroundTaskDialogContent.xml");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Spinner Example", SpinnerExample.class);
					p.setXmlPanel("examples/spinner/SpinnerDialogContent.xml");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Text Fields Example", TextExample.class);
					p.setXmlPanel("examples/text/TextDialogContent.xml");
					panels.add( p );
					applications.addNode(node, p );
				}
				{
					PanelInfo p = new PanelInfo("Wizard Integration Example", DDLWizardApplication.class);
					p.setXmlPanel("examples/wizard/WizardExample.xml");
					panels.add( p );
					applications.addNode(node, p );
				}
			}
			
		}

		JTree mainTree;
		
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
			
			CardLayout l = (CardLayout) contentPanel.getLayout();
			
			l.show(contentPanel, "Applications");

			for( int i=1; i<=mainTree.getRowCount(); ++i ) {
				mainTree.expandRow(i);
			}
			
			super.addNotify();
			
		}

		public final GenericTreeModel<PanelInfo> getApplications() {
			return applications;
		}

		public final Class<?> getSelectedApplicationClass() {
			return selectedApplicationClass;
		}


		public final void setSelectedApplicationClass(Class<?> selectedApplicationClass) {
			this.selectedApplicationClass = selectedApplicationClass;
			firePropertyChange("runnable", null, null);
		}


		@Action
		public void selectNode( ActionEvent ev ) {
		
			logger.info( String.format( "select node [%s]\n", ev ));
			
			PanelInfo info = applications.getSelectedObject( (TreeSelectionEvent) ev.getSource());

			setSelectedApplicationClass( info.panelClass );

			CardLayout l = (CardLayout) contentPanel.getLayout();
		
			l.show(contentPanel, info.name);
			

		}
		
		@Action
		public void activeNode( ActionEvent ev ) {
		
			logger.info( String.format("run application  [%s]\n", ev) );

			PanelInfo info = applications.getSelectedObject( (MouseEvent) ev.getSource());

			setSelectedApplicationClass( info.panelClass );
			
			runApplication();
			
			
		}
		
		public boolean isRunnable() {
			return selectedApplicationClass!=null;
		}
		
		@Action( enabledProperty="runnable")
		public void runApplication() {
		
			if( !isRunnable() ) return;
			logger.info( String.format("run application  [%s]\n", selectedApplicationClass) );

			try {
				Method main = getSelectedApplicationClass().getMethod("main", String[].class );
				
				Object args = new String[0];
				main.invoke(null, args);
				
			} catch (Exception e) {
				logger.log( Level.WARNING, "error on start application", e );
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
		return FileTextArea.loadResource(res);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
            
                String[] ff = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
                
                for( String f : ff ) {
                    System.out.printf( "font-family [%s]\n", f );
                }
		SwingApplication.launch(Swixml2Explorer.class, args);

	}

	@Override
	protected void startup() {
		try {
			
			SwingTagLibrary.getInstance().registerTag("FileTextArea", FileTextArea.class);
			
			MyFrame frame = render( new MyFrame(), "examples/explorer/Explorer.xml" );

			show( frame );
			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}
