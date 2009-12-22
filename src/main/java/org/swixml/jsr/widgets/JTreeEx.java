/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.swixml.jsr295.BindingUtils;

/**
 *
 * @author sorrentino
 */
@SuppressWarnings("serial")
public class JTreeEx extends JTree {

    private Action action;
    private Action dblClickAction = null;
    private ImageIcon leafIcon = null;
    private ImageIcon openIcon = null;
    private ImageIcon closedIcon = null;

    /**
     * 
     */
    public JTreeEx() {
        super();
        init();
    }
    
    /**
     * 
     * @param newModel
     */
    public JTreeEx(TreeModel newModel) {
        super(newModel);
        init();
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

        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
            	int selRow = getRowForLocation(e.getX(), e.getY());
                //TreePath selPath = getPathForLocation(e.getX(), e.getY());
            	System.out.printf( "mousePressed selRow=[%d] clickCount=[%d]\n", selRow, e.getClickCount());
                if(selRow != -1) {
                    if(e.getClickCount() == 2) {
            			Action a = getDblClickAction();
            			
            			if( a==null ) return;
            			
            			ActionEvent ev = new ActionEvent(e, 0, null );
            			
            			a.actionPerformed(ev);

                    }
                }
            }
        };
        addMouseListener(ml);

    }
    
    public final Action getDblClickAction() {
		return dblClickAction;
	}

	public final void setDblClickAction(Action dblClickAction) {
		this.dblClickAction = dblClickAction;
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
    	
    	if( !BindingUtils.boundCheckAndSet(this)) {
	        if( null!=openIcon || null!=leafIcon || null!=closedIcon) {
	            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	            
	            if(null!=leafIcon) renderer.setLeafIcon(leafIcon);
	            if(null!=openIcon) renderer.setOpenIcon(openIcon);
	            if(null!=closedIcon) renderer.setClosedIcon(closedIcon);
	        
	            setCellRenderer(renderer);
	        }
    	}
        super.addNotify();
    }


}
