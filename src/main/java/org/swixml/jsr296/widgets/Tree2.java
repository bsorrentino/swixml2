/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296.widgets;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author sorrentino
 */
public class Tree2 extends JTree {

    private Action action;

    /**
     * 
     */
    public Tree2() {
        super();
    }
    
    /**
     * 
     * @param newModel
     */
    public Tree2(TreeModel newModel) {
        super(newModel);
        
        super.addTreeSelectionListener(new TreeSelectionListener(){

            public void valueChanged(TreeSelectionEvent e) {
                Tree2.this.valueChanged(e);
            }
            
        });
    }

    /**
     * 
     * @param app
     */
    public static void register( SwingApplication app ) {
        app.getSwix().getTaglib().registerTag( "Tree2", Tree2.class );
        
    }

    /**
     * 
     * @return
     */
    public Action getAction() {
        return action;
    }

    /**
     * 
     * @param action
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * 
     * @param e
     */
    public void valueChanged(TreeSelectionEvent e) {
        
        Action a = getAction();
        
        if( null==a ) return;
        
        ActionEvent ev = new ActionEvent( e, 0, null );
        
        a.actionPerformed(ev);
    }


}
