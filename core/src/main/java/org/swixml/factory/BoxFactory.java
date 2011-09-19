/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.factory;

import static org.swixml.LogUtil.logger;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.swixml.Converter;
import org.swixml.ConverterLibrary;
import org.swixml.SwingEngine;
import org.swixml.dom.Attribute;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;


/**
 *
 * @author sorrentino
 */
public class BoxFactory extends BeanFactory {

	public interface GapBox {
	
		boolean isAlignmentChanged();
	}
	
    /**
    *
    * @author sorrentino
    */
    @SuppressWarnings("serial")
	public static class VGapBox extends Box implements GapBox {

        int gap = 0;
        boolean alignmentChanged = false;
        
        public VGapBox() {
            super(BoxLayout.Y_AXIS);
            
        }

        @Override
		public void setAlignmentX(float alignmentX) {
			super.setAlignmentX(alignmentX);
			alignmentChanged = true;
		}

		public boolean isAlignmentChanged() {
			return alignmentChanged;
		}

		public int getGap() { return gap; }

        public void setGap(int gap) { this.gap = gap; }

        @Override
        public Component add(Component comp) {
        	boolean process = true;
        	
        	if( comp instanceof GapBox ) {
        		process = !((GapBox)comp).isAlignmentChanged();
        	}
        	
        	if(  process && comp instanceof JComponent ) {
        		JComponent c = (JComponent) comp;
        		c.setAlignmentX(super.getAlignmentX());
        	}
        	
        	Component result = super.add(comp);
             if( gap>0 ) {
            	 super.add( Box.createVerticalStrut(gap) );
             }
             return result;
        }


    }

    /**
    *
    * @author sorrentino
    */
    @SuppressWarnings("serial")
	public static class HGapBox extends Box implements GapBox {

        int gap = 0;
        boolean alignmentChanged = false;
        
        
        public HGapBox() {
            super(BoxLayout.X_AXIS);
        }

        
        public boolean isAlignmentChanged() {
			return alignmentChanged;
		}


		@Override
		public void setAlignmentX(float alignmentX) {
			super.setAlignmentX(alignmentX);
			alignmentChanged = true;
		}


		public int getGap() { return gap; }

        public void setGap(int gap) { this.gap = gap; }

        @Override
        public Component add(Component comp) {
        	boolean process = true;
        	
        	if( comp instanceof GapBox ) {
        		process = !((GapBox)comp).isAlignmentChanged();
        	}
        	
        	if(  process && comp instanceof JComponent ) {
        		JComponent c = (JComponent) comp;
        		c.setAlignmentX(super.getAlignmentX());
        	}
        	
             Component result = super.add(comp);
             if( gap>0 ) {
                 super.add( Box.createHorizontalStrut(gap) );            	 
             }
             return result;
        }
    }

    public enum Type {
        GLUE, HGLUE, VGLUE, HSTRUT, VSTRUT, RIGIDAREA
    }
    
    final Type type;
    
    public BoxFactory( Type type ) {
        super(Component.class);
        this.type = type;
    
    }

    public Type getType() {
        return type;
    }

    protected Integer getIntAttribute( String name, NamedNodeMap attributes ) throws Exception {
        if( null==attributes || attributes.getLength()==0 ) {
            final String msg = "attribute list is empty!"; 
            logger.severe( msg );
            throw new IllegalArgumentException(msg);
        } 
        
        org.w3c.dom.Node an = attributes.getNamedItem(name);
        if( an==null ) {
            final String msg = "required attribute " + name + " not found!"; 
            logger.severe( msg );
            throw new Exception(msg);        	
        }
        
        attributes.removeNamedItem(name);
            
        Converter<Integer> c = ConverterLibrary.getInstance().getConverter(Integer.class);
            
        return c.convert(Integer.class, new Attribute(an), (SwingEngine<?>)null);
        	
        /*
        for( Attribute a : attributes ) {
            
            if( name.equalsIgnoreCase( a.getLocalName() )){
                attributes.remove(a);
                Converter<Integer> c = ConverterLibrary.getInstance().getConverter(Integer.class);
                
                return c.convert(Integer.class, a, (SwingEngine<?>)null);
            }
        }
        */
        
        
    }

    protected Dimension getDimensionAttribute( String name, NamedNodeMap attributes ) throws Exception {
        if( null==attributes || attributes.getLength()==0 ) {
            final String msg = "attribute list is empty!"; 
            logger.severe( msg );
            throw new IllegalArgumentException(msg);
        } 
        
        org.w3c.dom.Node an = attributes.getNamedItem(name);
        if( an==null ) {
            final String msg = "required attribute " + name + " not found!"; 
            logger.severe( msg );
            throw new Exception(msg);        	
        }
        
        attributes.removeNamedItem(name);
            
        Converter<Dimension> c = ConverterLibrary.getInstance().getConverter(Dimension.class);
            
        return c.convert(Dimension.class, new Attribute(an), (SwingEngine<?>)null);
        
        /*
        for( Attribute a : attributes ) {
            
            if( name.equalsIgnoreCase( a.getLocalName() )){
                attributes.remove(a);
                Converter<Dimension> c = ConverterLibrary.getInstance().getConverter(Dimension.class);
                
                return c.convert(Dimension.class, a, (SwingEngine<?>)null);
            }
        }
		*/
        
        
    }
    
    protected Component createGlue( Element element) {
        return Box.createGlue();
    }

    protected Component createHGlue(Element element) {
        return Box.createHorizontalGlue();
    }

    protected Component createVGlue(Element element) {
        return Box.createVerticalGlue();
    }

    protected Component createHStrut(Element element) throws Exception {
        
        @SuppressWarnings("unchecked")
        Integer width = getIntAttribute("width", element.getAttributes());
        
        return Box.createHorizontalStrut(width);
    }

    protected Component createVStrut(Element element) throws Exception {
        
        @SuppressWarnings("unchecked")
        Integer heigth = getIntAttribute("height", element.getAttributes());
        
        return Box.createVerticalStrut(heigth);
    }

    protected Component createRigidArea(Element element) throws Exception {
        
        @SuppressWarnings("unchecked")
        Dimension d = getDimensionAttribute("size", element.getAttributes());
        
        return Box.createRigidArea(d);
    }

    @Override
    public Object create( Object owner, Element element) throws Exception {
        Component c = null;
        switch( type ) {
            case GLUE: c = createGlue(element); break;
            case HGLUE: c = createHGlue(element); break;
            case VGLUE: c = createVGlue(element); break;
            case HSTRUT: c = createHStrut(element); break;
            case VSTRUT: c = createVStrut(element); break;
            case RIGIDAREA: c = createRigidArea(element); break;
        }
        
        return c;
    }
    
    
}
