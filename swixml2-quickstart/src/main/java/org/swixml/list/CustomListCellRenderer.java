package org.swixml.list;

import java.awt.Component;
import java.util.logging.Level;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.swixml.LogUtil;

@SuppressWarnings("serial")
public class CustomListCellRenderer extends JLabel implements ListCellRenderer {
	
	public CustomListCellRenderer() {
		super();

		Application app = Application.getInstance();
		
		try {
			ResourceMap res = app.getContext().getResourceMap();
			
			Icon icon = res.getIcon( "listIcon");
		
			if( null!=icon ) {
		        super.setIconTextGap(6);
				super.setIcon(icon);
			}
		}
		catch( Exception ex  ) {
			LogUtil.logger.log( Level.WARNING, "icon doesn't exist", ex );
		}
	}

	public Component getListCellRendererComponent(JList list, Object value,	int index, boolean isSelected, boolean cellHasFocus) {
		String s = value.toString();
        setText(s);
        //setIcon((s.length() > 10) ? longIcon : shortIcon);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;

	}

	
}
