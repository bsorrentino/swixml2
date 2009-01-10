#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ${package}.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

@SuppressWarnings("serial")
public class TestTreeModel extends DefaultTreeModel {

    final MutableTreeNode root;
    
    public TestTreeModel() {
        super(new DefaultMutableTreeNode("ROOT"));
        
        root = (MutableTreeNode) super.getRoot();
        
        root.insert( new DefaultMutableTreeNode("node.1"), 0);
        
    }

}
