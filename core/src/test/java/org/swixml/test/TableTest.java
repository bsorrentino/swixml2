package org.swixml.test;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.junit.Test;



public class TableTest {
	
	@Test
	public void dummy() {}
	
	public static void main(String args[]) {

	    final Object rowData[][] = { 
	        { "1", "one",  "I" },
	        { "2", "two",  "II" },
	        { "3", "three", "III" }};
	    final String columnNames[] = { "#", "English", "Roman" };

	    final JTable table = new JTable(rowData, columnNames);
	    JScrollPane scrollPane = new JScrollPane(table);


	    JFrame frame = new JFrame("Resizing Table");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    frame.add(scrollPane, BorderLayout.CENTER);

	    
	    TableColumn tc = null;
	    for (int i = 0; i < 3; i++) {
	    	
	        tc = table.getColumnModel().getColumn(i);

	        if(tc.getModelIndex()==0)tc.setResizable(false);
	        if (i == 2) {
	            tc.setPreferredWidth(100); //sport column is bigger
	        } else {
	            tc.setPreferredWidth(50);
	        }
      	  System.out.printf("column [%s] header=[%s] modelIndex=[%d] resizable=[%b] minWidth=[%s] maxWidth=[%d] preferredWidth=[%d]\n", 
      			  tc.getIdentifier(),
      			  tc.getHeaderValue(),
      			  tc.getModelIndex(),
      			  tc.getResizable(),
      			  tc.getMinWidth(),
      			  tc.getMaxWidth(),
      			  tc.getPreferredWidth()
      			  );
            
	    }    
	    
	    frame.setSize(300, 150);
	    frame.setVisible(true);
	}
}
