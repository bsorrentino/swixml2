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
import org.jdesktop.application.ResourceConverter;
import org.jdesktop.application.ResourceConverter.ResourceConverterException;
import org.jdesktop.application.ResourceMap;
import org.swixml.LogUtil;

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
    private int iconCount = 0;

    private ItemListener _itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {

                if( timer.isRunning() ) {
                    stop();
                }
                else {
                    start();
                }

            }
            
        };

    public JAnimatedButton() {

        timer.setInitialDelay(DEFAULT_START_DELAY);
        timer.setDelay(DEFAULT_DELAY);

    }

    public boolean isStateChangedEnabled() {
        return Boolean.TRUE.equals(getClientProperty("stateChangedEnabled"));
    }

    public void setStateChangedEnabled(boolean value) {
        putClientProperty("stateChangedEnabled", value );
    }


    public final boolean isRunning() {
        return timer.isRunning();
    }

    public int getIconCount() {
        return iconCount;
    }

    public void setIconCount(int imageCount) {
        if( imageCount<0 ) throw new IllegalArgumentException("imageCount is less than 0!");
        this.iconCount = imageCount;
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

    public final void start() {
        if( !isAnimationValid() ) return;

        imageIconIndex = 1;

        timer.restart();
    }
    
    public final void stop() {
        if( !isAnimationValid() ) return;
        
        timer.stop();

        imageIconIndex = 0;

        setIcon( icons[imageIconIndex] );
    }

    private void loadFromResource() {
        if( iconCount<=0 ) {
            throw new IllegalStateException( "icon Count value is invalid" );
        }

        String name = super.getName();
        if( name==null ) throw new IllegalStateException("name is null!");

        Application app = Application.getInstance();
        if( app==null ) throw new IllegalStateException("application is not present!");

        ApplicationContext context = app.getContext();

        ResourceMap rm = context.getResourceMap();

        icons = new ImageIcon[iconCount];

        ResourceConverter converter = ResourceConverter.forType( ImageIcon.class );

        for( int i=0; i<iconCount ; ++i ) {

            final String iconIndex = String.format( "icon%d", i);

            final String icon = String.format( "%s.%s", name,  iconIndex);

            if( rm.containsKey(icon)) {
                icons[i] = rm.getImageIcon( icon );
            }
            else {

                String iconPath = (String)super.getClientProperty(iconIndex);
                
                if (converter!=null && iconPath!=null ) {

                    try {

                        icons[i] = (ImageIcon) converter.parseString(iconPath, rm);

                    } catch (ResourceConverterException ex) {
                        LogUtil.logger.warning( String.format( "icon [%s] not found ", iconPath ));
                        icons[i] = null;
                    }
                }
                else {
                    icons[i] = null;
                }
            }
        
        }

        imageIconIndex = 0;

        setIcon( icons[imageIconIndex] );

    }



    @Override
    public void addNotify() {
        super.addNotify();

        if( iconCount <=0 ) return;

        loadFromResource();

        if( isStateChangedEnabled() )
            addItemListener( _itemListener );

    }

    @Override
    public void removeNotify() {
        stop();

        icons = null;

        removeItemListener(_itemListener);
        
        super.removeNotify();

    }

}
