package examples;

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

public class ComboApplication extends SwingApplication {

	public static class ComboDialog extends JDialog {
		private static final String VALID_PROPERTY = "valid";

		public static String SWIXML_SOURCE = "examples/ComboDialog.xml";
		
		private static final String ENTER_ACTION = "ENTER";
		private static final String ESCAPE_ACTION = "ESCAPE";

		public JComboBox cmbCallResult;
		public JComboBox cmbChain;
		
		final  List<String> callResultList ;
		final  List<String> outboundChainList;

		private boolean personal = false;
		
		public ComboDialog() {

			callResultList = Arrays.asList( "<select chain>", "Item1", "Item2", "Item3" );
			outboundChainList = ObservableCollections.observableList( new ArrayList<String>(Arrays.asList(  "Item1", "Item2", "Item3" )) );
		}
		
		public final List<String> getCallResultList() {
			return callResultList;
		}

		public final List<String> getOutboundChainList() {
			return outboundChainList;
		}

		public final boolean isPersonal() {
			return personal;
		}

		public final void setPersonal(boolean personal) {
			this.personal = personal;
		}

		@Action
		public void selectChain() {
			firePropertyChange(VALID_PROPERTY, null, null);		
		}
		
		@Action
		public void selectCallResult() {
			firePropertyChange(VALID_PROPERTY, null, null);		
		}
		
		public void cancel() {
			
		}

		@Action(enabledProperty=VALID_PROPERTY)
		public void submit() {
			int chainIndex = cmbChain.getSelectedIndex();
			//int resultIndex = cmbCallResult.getSelectedIndex();

			System.out.println( "submit" );

			outboundChainList.remove(chainIndex);
			cmbCallResult.setSelectedIndex(0);
			
			firePropertyChange(VALID_PROPERTY, null, null);
		}
		
		public boolean isValid() {
			
			if( cmbCallResult==null || cmbChain==null ) return false;
			
			return cmbCallResult.getSelectedIndex()>0 && cmbChain.getSelectedIndex()!=-1;
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
		SwingApplication.launch(ComboApplication.class, args);
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
