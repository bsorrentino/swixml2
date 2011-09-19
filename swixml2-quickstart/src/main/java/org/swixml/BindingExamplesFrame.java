package org.swixml;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;

import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;
import org.swixml.list.CustomListCellRenderer;
import org.swixml.table.SimpleBean;
import org.swixml.tree.TestTreeModel;

/**
 * The ApplicationTest shows in the usage of client attributes in swixml tags.
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 *
 * @since swixml 0.98
 */
@SuppressWarnings("serial")
public class BindingExamplesFrame extends JFrame  {

  public JTable testTable;
  
  final CustomListCellRenderer listRenderer = new CustomListCellRenderer();
  
  boolean connected = false;
  
  List<SimpleBean> myData = ObservableCollections.observableList( new ArrayList<SimpleBean>() );

  final List<String> comboList = ObservableCollections.observableList( Arrays.asList( "item1", "item2", "item3") );
  
  TestTreeModel myTree = new TestTreeModel();
  
  private String testValue = "TEST";

  private String message = "";
  
  public final CustomListCellRenderer getListRenderer() {
	return listRenderer;
}



	public final String getMessage() {
		return message;
	}


	public final void setMessage(String value) {
		String oldValue = message;
		this.message = value;
		firePropertyChange("message", oldValue, message);
	}


	public Class<?> getMyDataClass() {
	      return SimpleBean.class;
	}
  
	public final List<String> getComboList() {
		return comboList;
	}


	public List<SimpleBean> getMyData() {
        return myData;
    }

    public TestTreeModel getMyTree() {
        return myTree;
    }

  public Dimension getFrameSize() {
    return new Dimension(100,600);
  }
  
  @Action
  public void comboAction( ActionEvent e ) {

	  JComboBox cb = (JComboBox)e.getSource();
	  Object item = cb.getSelectedItem();
	  if( item!=null ) {
		  System.out.printf( "comboAction source[%s]\n", item);
	  }
  }
  
  @Action
  public void listAction( ActionEvent e ) {
	 
	  ListSelectionEvent le =  (ListSelectionEvent) e.getSource();
	  JList list = (JList) le.getSource();
	  
	  Object item =  list.getSelectedValue();
	  
	  if( item!=null ) {
		  System.out.printf( "listAction source:[%s]\n", item );
	  }
  }
  
  @Action(enabledProperty="connected")
  public void test() {
    System.out.printf( "hello world! %s\n", testValue);
    
    setTestValue("hello world!");
    firePropertyChange("testValue", null, null); // force update of text box
    
  }
  
  @Action
  public void selectNode( ActionEvent e ) {
    
    TreeSelectionEvent ev = (TreeSelectionEvent) e.getSource();
    
    System.out.printf( "selectNode path=%s\n", ev.getPath().toString());

    myData.add( new SimpleBean(ev.getPath().toString(), 0));
    
  }

  @Action
  public void selectRow( ActionEvent e ) {
    
    ListSelectionEvent ev = (ListSelectionEvent) e.getSource();
    
    System.out.printf( "selectRow firstIndex=%d lastIndex=%d valueIsAdjusting=%b\n", ev.getFirstIndex(), ev.getLastIndex(), ev.getValueIsAdjusting());
    
  }
  
  @Action()
  public void show( ActionEvent e ) {
	  JComponent btn = (JComponent) e.getSource();
      setMessage( String.format( "X:%s\nY:%s", btn.getClientProperty( "X" ), btn.getClientProperty( "Y" ) ) );
  }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        firePropertyChange("connected", null, connected);        
    }

  
    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
        System.out.printf( "updated %s\n", testValue);
    }

}
