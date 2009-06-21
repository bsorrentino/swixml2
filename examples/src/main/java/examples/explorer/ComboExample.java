package examples.explorer;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;
import org.swixml.jsr296.SwingApplication;


@SuppressWarnings("serial")
public class ComboExample extends SwingApplication {

	public static class ComboDialog extends JDialog {
		private static final String VALID_PROPERTY = "valid";

		public static String SWIXML_SOURCE = "examples/explorer/ComboDialog.xml";
		
		private static final String ENTER_ACTION = "ENTER";
		private static final String ESCAPE_ACTION = "ESCAPE";

		public JComboBox cmbResult; // Automatically bound
		public JComboBox cmbTemplate; // Automatically bound
		
		final  List<String> resultList ;
		final  List<String> templateList;

		private boolean removeOnSubmit = false;
		
		public ComboDialog() {

			templateList = Arrays.asList( "<select item>", "Item1", "Item2", "Item3" );
			resultList = ObservableCollections.observableList( new ArrayList<String>(Arrays.asList(  "Item1", "Item2", "Item3" )) );
		}
		
		public final List<String> getResultList() {
			return resultList;
		}

		public final List<String> getTemplateList() {
			return templateList;
		}

		public final boolean isRemoveOnSubmit() {
			return removeOnSubmit;
		}

		public final void setRemoveOnSubmit(boolean value) {
			this.removeOnSubmit = value;
		}

		@Action
		public void selectTemplate() {
			firePropertyChange(VALID_PROPERTY, null, null);		
		}
		
		@Action
		public void selectResult() {
			firePropertyChange(VALID_PROPERTY, null, null);		
		}
		
		public void cancel() {
			
		}

		@Action(enabledProperty=VALID_PROPERTY)
		public void submit() {
			String selectItem = (String) cmbTemplate.getSelectedItem();
			
			if( isRemoveOnSubmit() ) {
				resultList.remove(selectItem);
			}

			cmbTemplate.setSelectedIndex(0);				
			
			firePropertyChange(VALID_PROPERTY, null, null);
		}
		
		public boolean isValid() {
			
			if( cmbResult==null || cmbTemplate==null ) return false;
			
			return cmbTemplate.getSelectedIndex()>0;
		}
		
		protected JRootPane createRootPane() {
		    JRootPane rootPane = new JRootPane();
		    
		    KeyStroke stroke = KeyStroke.getKeyStroke(ESCAPE_ACTION);
		    KeyStroke strokeOk = KeyStroke.getKeyStroke(ENTER_ACTION);
		    
		    InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		    inputMap.put(stroke, ESCAPE_ACTION);
		    inputMap.put( strokeOk, ENTER_ACTION);
		    
		    rootPane.getActionMap().put(ESCAPE_ACTION, new AbstractAction() {
			      public void actionPerformed(ActionEvent actionEvent) {
				        cancel();
				      }
		    });
		    
		    rootPane.getActionMap().put(ENTER_ACTION, new AbstractAction() {

				public void actionPerformed(ActionEvent e) {
					if( isValid() ) 
						submit();
				}
		    	
		    });

		    return rootPane;
		  }

	}

	public static void main(String args []) {
		SwingApplication.launch(ComboExample.class, args);
	}
	
	
	@Override
	protected void startup() {

		try {
			
			JDialog dialog = render( new ComboDialog(), ComboDialog.SWIXML_SOURCE); 
			
			show( dialog );

			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}
