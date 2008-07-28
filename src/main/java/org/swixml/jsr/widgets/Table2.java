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
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author sorrentino
 */
public class Table2 extends JTable {

    private Action action;
    private Class<?> beanClass;
    private List<?> beanList;
    
    public Table2() {
        super();
        init();
    }

    public Table2(TableModel dm) {
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
    /**
     * 
     * @param app
     */
    public static void register( SwingApplication app ) {
        app.getSwix().getTaglib().registerTag( "Table2", Table2.class );
        
    }

    public Class<?> getBindClass() {
        return beanClass;
    }

    public void setBindClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public List<?> getBindWith() {
        return beanList;
    }

    public void setBindWith(List<?> beanList) {
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
            BindingUtils.initTableBinding(UpdateStrategy.READ_WRITE, this, beanList, beanClass);
      

        System.out.printf( "addNotify beanClass=%s beanList=%s\n", beanClass, beanList);
        super.addNotify();
    }

    
}
