/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;


/**
 *
 * @author sorrentino
 */
public class TestTreeModel extends DefaultTreeModel {

    final MutableTreeNode root;
    
    public TestTreeModel() {
        super(new DefaultMutableTreeNode("ROOT"));
        
        root = (MutableTreeNode) super.getRoot();
        
        root.insert( new DefaultMutableTreeNode("node.1"), 0);
        
    }

}
