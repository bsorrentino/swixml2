package examples.explorer;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;
import org.swixml.jsr296.SwingApplication;

import examples.SimpleBean;

public class TableExample extends SwingApplication {

	private static final String SWIXML_SOURCE = "examples/explorer/TableDialog.xml";

	@SuppressWarnings("serial")
	public class TableDialog extends JDialog {

		final List<SimpleBean> myData = ObservableCollections.observableList( new ArrayList<SimpleBean>() );

		public JTable table; /* automatically bound */
		
		public TableDialog() {
			myData.add( new SimpleBean( "Bartolomeo", 41 ) );
			myData.add( new SimpleBean( "Francesco", 38 ) );
			
		}
		/**
		 * list bound
		 */
		public final List<SimpleBean> getMyData() {
			return myData;
		}
		
		/**
		 * indicate type of list bound
		 */
		public Class<?> getMyDataClass() {
		      return SimpleBean.class;
		}

		/**
		 * event raised when a row is selected on table
		 */
		@Action
		public void selectRow( ActionEvent e ) {
		    
		    ListSelectionEvent ev = (ListSelectionEvent) e.getSource();
		    
		    System.out.printf( "selectRow firstIndex=%d lastIndex=%d valueIsAdjusting=%b\n", ev.getFirstIndex(), ev.getLastIndex(), ev.getValueIsAdjusting());
		    
		}

		/**
		 * event raised when a double click is performed upon row
		 */
		@Action
		public void activateRow( ActionEvent e ) {
		    
			    System.out.printf( "activate row [%d]\n", table.getSelectedRow());
		    
		}

		
	}
	
	@Override
	protected void startup() {
		try {
			
			JDialog dialog = render( new TableDialog(), SWIXML_SOURCE ); 
			
			show( dialog );

			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}

	}

	public static void main(String args []) {
		SwingApplication.launch(TableExample.class, args);
	}
	
}
