package examples.explorer;

import java.awt.event.ActionEvent;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;
import org.swixml.jsr295.BindingUtils;
import org.swixml.jsr296.SwingApplication;

import examples.SimpleBean;
import examples.SimpleBean2;

public class TableExample extends SwingApplication {

	private static final String SWIXML_SOURCE = "examples/explorer/TableDialog.xml";

	@SuppressWarnings("serial")
	public class TableDialog extends JDialog {

		final List<SimpleBean> myData = ObservableCollections.observableList( new ArrayList<SimpleBean>() );
		final List<SimpleBean2> myData2 = ObservableCollections.observableList( new ArrayList<SimpleBean2>() );

		public JTable table; /* automatically bound */
		public JTable table2; /* automatically bound */
		
		public TableDialog() {
			myData.add( new SimpleBean( "Bartolomeo", 41 ) );
			myData.add( new SimpleBean( "Francesco", 38 ) );
			myData.add( new SimpleBean( "Vincenzo", 39 ) );
			
			for( int i=0; i<20 ; ++i ) {
				myData2.add( new SimpleBean2() );
			}
			
			initTable2MetaData();
		}
		
		/**
		 * Init table2 meta data using the implicit beanInfo
		 */
		private void initTable2MetaData() {
			Map<String,PropertyDescriptor> pMap = BindingUtils.getPropertyMap(getMyDataClass2());
			
			{
			PropertyDescriptor pd = pMap.get("field1");
			BindingUtils.setTableColumnIndex(pd, 1);
			pd.setDisplayName("index");
			}
			
			{
			PropertyDescriptor pd = pMap.get("field3");
			BindingUtils.setTableColumnEditable(pd, true);
			pd.setDisplayName("field3 [editable]");
			}
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
		 * list bound
		 */
		public final List<SimpleBean2> getMyData2() {
			return myData2;
		}
		
		/**
		 * indicate type of list bound
		 */
		public Class<?> getMyDataClass2() {
		      return SimpleBean2.class;
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
			    System.out.printf( "activate row [%d]\n[%s]\n", table.getSelectedRow(), myData.get(table.getSelectedRow()) );
		    
		}
		@Action
		public void activateRow2( ActionEvent e ) {
			    System.out.printf( "activate row [%d]\n[%s]\n", table2.getSelectedRow(), myData2.get(table2.getSelectedRow()) );
		    
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
