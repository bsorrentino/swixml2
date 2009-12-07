/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyDescriptor;
import java.util.List;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.commons.beanutils.PropertyUtils;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Converter;
import org.swixml.jsr295.BindingUtils;
import static org.swixml.SwingEngine.isDesignTime;

/**
 *
 * @author sorrentino
 */
@SuppressWarnings("serial")
public class JTableEx extends JTable implements BindableListWidget {

    private Action action;
    private Action dblClickAction = null;
    
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

    @Override
    public TableCellRenderer getCellRenderer(int row, int col) {
        if( beanClass!=null && beanList!=null ) {
                PropertyDescriptor[] pp = PropertyUtils.getPropertyDescriptors(beanClass);

                PropertyDescriptor p = pp[col];

                Object r = p.getValue(BindingUtils.TABLE_COLUMN_RENDERER);

                if( r instanceof TableCellRenderer ) {
                        return (TableCellRenderer) r;
                }
        }
        return super.getCellRenderer(row, col);
    }

    private void onRowOrColSelection( ListSelectionEvent e ) {
        // ISSUE-5
        if( e.getValueIsAdjusting() ) return;
        if( getSelectedRow()==-1 ) return;

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

        // ISSUE-6
        super.addMouseListener(new MouseAdapter(){
            
        	public void mouseClicked(MouseEvent e){
        		if (e.getClickCount() == 2){
        			Action a = getDblClickAction();
        			
        			if( a==null ) return;
        			
        			ActionEvent ev = new ActionEvent(e, 0, null );
        			
        			a.actionPerformed(ev);
        			
                }
             }
            } );
         
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

    public void setConverter(Converter<?, ?> converter) {
    }

    public Converter<?, ?> getConverter() {
        return null;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public final Action getDblClickAction() {
		return dblClickAction;
	}

	public final void setDblClickAction(Action dblClickAction) {
		this.dblClickAction = dblClickAction;
	}

	@Override
    public void addNotify() {

        if( !isDesignTime() && beanList!=null && !BindingUtils.isBound(this) ) {
        	  	
        	if( beanClass!=null ) {
        		BindingUtils.initTableBindingFromBeanInfo( null, UpdateStrategy.READ_WRITE, this, getBindList(), getBindClass(), isAllPropertiesBound());
        	}
        	else {
        	    super.setAutoCreateColumnsFromModel(false);

        		BindingUtils.initTableBindingFromTableColumns( null, UpdateStrategy.READ_WRITE, this, getBindList() );        		
        	}
            BindingUtils.setBound(this, true);
        }
       
        
        super.addNotify();
        
    }

    
}
