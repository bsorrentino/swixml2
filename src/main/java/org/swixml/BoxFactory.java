/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml;

import static org.swixml.LogUtil.logger;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.jdom.Attribute;


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
    
    
}
