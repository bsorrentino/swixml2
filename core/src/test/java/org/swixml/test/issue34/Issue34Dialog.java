package org.swixml.test.issue34;

import java.util.List;
import javax.swing.JDialog;



@SuppressWarnings("serial")
public class Issue34Dialog extends JDialog  {

    final java.util.List<Issue34Bean> items = new java.util.ArrayList<Issue34Bean>(100);
    final java.util.List<Issue34Bean2> selectItems = new java.util.ArrayList<Issue34Bean2>(100);

    public Issue34Dialog() {
    
        for( int i=0; i< 50 ; ++i ) {
            final Issue34Bean a = new Issue34Bean( "b"+i, "c" + i );
            
            items.add(a);
            
            if( i%3==0 ) selectItems.add( new Issue34Bean2(a) );
            
        }

    }

    public List<Issue34Bean> getItems() {
        return items;
    }

    public List<Issue34Bean2> getSelectItems() {
        return selectItems;
    }




}
