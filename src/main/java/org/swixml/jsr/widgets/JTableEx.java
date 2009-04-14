/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableModel;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.swixml.jsr295.BindingUtils;

/**
 *
 * @author sorrentino
 */
@SuppressWarnings("serial")
public class JTableEx extends JTable {

    private Action action;
    private Class<?> beanClass;
    private List<?> beanList;
    private boolean allPropertiesBound = true;
    
    public JTableEx() {
        super();
        init();
    }

    public JTableEx(TableModel dm) {
        super(dm);
        init();
        
    }

    private void onRowOrColSelection( ListSelectionEvent e ) {
            Action a = getAction();

            if( null==a ) return;

            ActionEvent ev = new ActionEvent( e, 0, null );

            a.actionPerformed(ev);        
    }
    
    private void init() {
        super.getSelectionModel().addListSelectionListener( new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                onRowOrColSelection(e);
            }
            
        });        
        
        super.getColumnModel().addColumnModelListener( new TableColumnModelListener() {

            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnMoved(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
                onRowOrColSelection(e);
            }
            
        });
/*        
        super.getModel().addTableModelListener( new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
 */
    }
    
    
    
    public final boolean isAllPropertiesBound() {
		return allPropertiesBound;
	}

	public final void setAllPropertiesBound(boolean allPropertyBound) {
		this.allPropertiesBound = allPropertyBound;
	}

	public Class<?> getBindClass() {
        return beanClass;
    }

    public void setBindClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public List<?> getBindList() {
        return beanList;
    }

    public void setBindList(List<?> beanList) {
        this.beanList = beanList;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public void addNotify() {

        if( beanList!=null && beanClass!=null )
        	
            BindingUtils.initTableBinding( null, UpdateStrategy.READ_WRITE, this);
      

        super.addNotify();
    }

    
}
