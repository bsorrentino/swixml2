/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import org.jdom.Attribute;
import org.swixml.BeanFactory;
import org.swixml.Converter;
import org.swixml.ConverterLibrary;
import org.swixml.SwingEngine;
import static org.swixml.SwingEngine.logger;


/**
 *
 * @author sorrentino
 */
public class BoxFactory extends BeanFactory {

    /**
    *
    * @author sorrentino
    */
    public static class VGapBox extends Box {

        int gap;

        public VGapBox() {
            super(BoxLayout.Y_AXIS);
            gap=1;
        }

        public int getGap() { return gap; }

        public void setGap(int gap) { this.gap = gap; }

        @Override
        public Component add(Component comp) {
             Component result = super.add(comp);
             super.add( Box.createVerticalStrut(gap) );

             return result;
        }
    }

    /**
    *
    * @author sorrentino
    */
    public static class HGapBox extends Box {

        int gap;

        public HGapBox() {
            super(BoxLayout.X_AXIS);
            gap=1;
        }

        public int getGap() { return gap; }

        public void setGap(int gap) { this.gap = gap; }

        @Override
        public Component add(Component comp) {
             Component result = super.add(comp);
             super.add( Box.createHorizontalStrut(gap) );
             return result;
        }
    }

    public enum Type {
        GLUE, HGLUE, VGLUE, HSTRUT, VSTRUT, RIGIDAREA
    }
    
    Type type;
    
    public BoxFactory( Type type ) {
        super(Component.class);
        this.type = type;
    
    }

    protected Integer getIntAttribute( String name, List<Attribute> attributes ) throws Exception {
        if( null==attributes || attributes.isEmpty() ) {
            final String msg = "attribute list is empty!"; 
            logger.severe( msg );
            throw new IllegalArgumentException(msg);
        } 
        for( Attribute a : attributes ) {
            
            if( name.equalsIgnoreCase( a.getName() )){
                attributes.remove(a);
                Converter c = ConverterLibrary.getInstance().getConverter(int.class);
                
                return (Integer)c.convert(int.class, a, null);
            }
        }
        
        final String msg = "required attribute " + name + " not found!"; 
        logger.severe( msg );
        throw new Exception(msg);
        
    }

    protected Dimension getDimensionAttribute( String name, List<Attribute> attributes ) throws Exception {
        if( null==attributes || attributes.isEmpty() ) {
            final String msg = "attribute list is empty!"; 
            logger.severe( msg );
            throw new IllegalArgumentException(msg);
        } 
        for( Attribute a : attributes ) {
            
            if( name.equalsIgnoreCase( a.getName() )){
                attributes.remove(a);
                Converter c = ConverterLibrary.getInstance().getConverter(Dimension.class);
                
                return (Dimension)c.convert(Dimension.class, a, null);
            }
        }

        final String msg = "required attribute " + name + " not found!"; 
        logger.severe( msg );
        throw new Exception(msg);
        
    }
    
    protected Component createGlue(List<Attribute> attributes) {
        return Box.createGlue();
    }

    protected Component createHGlue(List<Attribute> attributes) {
        return Box.createHorizontalGlue();
    }

    protected Component createVGlue(List<Attribute> attributes) {
        return Box.createVerticalGlue();
    }

    protected Component createHStrut(List<Attribute> attributes) throws Exception {
        
        Integer width = getIntAttribute("width", attributes);
        
        return Box.createHorizontalStrut(width);
    }

    protected Component createVStrut(List<Attribute> attributes) throws Exception {
        
        Integer heigth = getIntAttribute("height", attributes);
        
        return Box.createVerticalStrut(heigth);
    }

    protected Component createRigidArea(List<Attribute> attributes) throws Exception {
        
        Dimension d = getDimensionAttribute("size", attributes);
        
        return Box.createRigidArea(d);
    }

    @Override
    public Object newInstance(List<Attribute> attributes) throws Exception {
        Component c = null;
        switch( type ) {
            case GLUE: c = createGlue(attributes); break;
            case HGLUE: c = createHGlue(attributes); break;
            case VGLUE: c = createVGlue(attributes); break;
            case HSTRUT: c = createHStrut(attributes); break;
            case VSTRUT: c = createVStrut(attributes); break;
            case RIGIDAREA: c = createRigidArea(attributes); break;
        }
        
        return c;
    }
    
    
    public static void register( SwingEngine engine ) {
        engine.getTaglib().registerTag("box.glue", new BoxFactory(Type.GLUE));
        engine.getTaglib().registerTag("box.hglue", new BoxFactory(Type.HGLUE));
        engine.getTaglib().registerTag("box.vglue", new BoxFactory(Type.VGLUE));
        engine.getTaglib().registerTag("box.hstrut", new BoxFactory(Type.HSTRUT));
        engine.getTaglib().registerTag("box.vstrut", new BoxFactory(Type.VSTRUT));
        engine.getTaglib().registerTag("box.rigidarea", new BoxFactory(Type.RIGIDAREA));
        engine.getTaglib().registerTag("vgapbox", BoxFactory.VGapBox.class);
        engine.getTaglib().registerTag("hgapbox", BoxFactory.HGapBox.class);

    }
}
