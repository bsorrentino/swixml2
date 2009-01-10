#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ${package}.tree;

import java.util.Collection;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("serial")
public class GenericTreeModel<T> extends DefaultTreeModel {

    public GenericTreeModel( String rootName ) {
        super( new DefaultMutableTreeNode(rootName), true);
    }

    public GenericTreeModel(DefaultMutableTreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }

    public GenericTreeModel(DefaultMutableTreeNode root) {
        super(root);
    }

    public void addNodeToRoot( T userObject ) {
        
        DefaultMutableTreeNode _root = (DefaultMutableTreeNode) getRoot();
        
        _root.add(  new DefaultMutableTreeNode(userObject,false) );
    }

    public void addNodeToRoot( Collection<T> userObjectList ) {
        
        DefaultMutableTreeNode _root = (DefaultMutableTreeNode) getRoot();
        
        for( T o : userObjectList ) {
            _root.add(  new DefaultMutableTreeNode(o,false) );
        }
    }
    
    @SuppressWarnings("unchecked")
	public T getSelectedObject( TreeSelectionEvent ev ) {
    
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) ev.getPath().getLastPathComponent();
        
        T bean = (T)n.getUserObject();
 
        return bean;
    }
    
}
