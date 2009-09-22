/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.contrib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author softphone
 */
public class JAnimatedButton extends JToggleButton implements ActionListener {
    private static final int DEFAULT_DELAY = 1000;
    private static final int DEFAULT_START_DELAY = 200;

    private final javax.swing.Timer timer = new javax.swing.Timer( DEFAULT_START_DELAY, this);
    private ImageIcon[] icons = null;
    private int imageIconIndex = 0;

    private ItemListener itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {

                int state = e.getStateChange();

                System.out.printf( "ItemEvent state=[%d %d %s] [%b]\n", state , ItemEvent.SELECTED, ItemEvent.DESELECTED, isSelected() );

                if( isSelected() ) {
                    start();
                }
                else {
                    stop();
                }


            }
            
        };

    public JAnimatedButton() {

        timer.setInitialDelay(DEFAULT_START_DELAY);
        timer.setDelay(DEFAULT_DELAY);

    }

    public void setDelay( int delay ) {
        timer.setDelay( delay );
    }

    public void actionPerformed(ActionEvent event) {
        
        setIcon( icons[imageIconIndex++] );
        if( imageIconIndex>=icons.length ) imageIconIndex = 0;
        
    }

    private boolean isAnimationValid() {
        return (icons!=null && icons.length>1 );
    }

    protected void start() {
        if( !isAnimationValid() ) return;

        imageIconIndex = 1;

        timer.restart();
    }
    
    protected void stop() {
        
        timer.stop();

        imageIconIndex = 0;

        setIcon( icons[imageIconIndex] );
    }

    @Override
    public void addNotify() {
        super.addNotify();

        String name = super.getName();
        if( name==null ) throw new IllegalStateException("name is null!");

        Application app = Application.getInstance();
        if( name==null ) throw new IllegalStateException("application is not present!");

        ApplicationContext context = app.getContext();

        ResourceMap rm = context.getResourceMap();

        final String iconLength = name + ".icon.length";

        int iconCount = rm.getInteger( iconLength );

        if( iconCount<=0 ) {
            throw new IllegalStateException( String.format("respurce [%s] isn't defined or contains aa invalid number", iconLength));
        }

        icons = new ImageIcon[iconCount];

        for( int i=0; i<iconCount ; ++i ) {

            final String icon = String.format( "%s.icon[%d]", name,  i);

            icons[i] = rm.getImageIcon( icon );
        }

        imageIconIndex = 0;

        setIcon( icons[imageIconIndex] );

        addItemListener( itemListener );

    }

    @Override
    public void removeNotify() {
        stop();

        icons = null;

        removeItemListener(itemListener);
        
        super.removeNotify();

    }

}
