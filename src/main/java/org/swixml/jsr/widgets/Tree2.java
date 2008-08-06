/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import org.swixml.SwingEngine;

/**
 *
 * @author sorrentino
 */
public class Tree2 extends JTree {

    private Action action;
    private ImageIcon leafIcon = null;
    private ImageIcon openIcon = null;
    private ImageIcon closedIcon = null;

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
    }

    
    private void init() {
        super.addTreeSelectionListener(new TreeSelectionListener(){

            public void valueChanged(TreeSelectionEvent e) {
                Action a = getAction();

                if( null==a ) return;

                ActionEvent ev = new ActionEvent( e, 0, null );

                a.actionPerformed(ev);
            }
            
        });        
        
        if( null!=openIcon || null!=leafIcon || null!=closedIcon) {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            
            if(null!=leafIcon) renderer.setLeafIcon(leafIcon);
            if(null!=openIcon) renderer.setOpenIcon(openIcon);
            if(null!=closedIcon) renderer.setClosedIcon(closedIcon);
        
            setCellRenderer(renderer);
        }
    }
    
    /**
     * 
     * @param app
     */
    public static void register( SwingEngine engine ) {
        engine.getTaglib().registerTag( "Tree2", Tree2.class );
        
    }

    public ImageIcon getOpenIcon() {
        return openIcon;
    }

    public void setOpenIcon(ImageIcon folderIcon) {
        this.openIcon = folderIcon;
    }

    public ImageIcon getLeafIcon() {
        return leafIcon;
    }

    public void setLeafIcon(ImageIcon leafIcon) {
        this.leafIcon = leafIcon;
    }

    public ImageIcon getClosedIcon() {
        return closedIcon;
    }

    public void setClosedIcon(ImageIcon closedIcon) {
        this.closedIcon = closedIcon;
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

    @Override
    public void addNotify() {
        init();
        super.addNotify();
    }


}
