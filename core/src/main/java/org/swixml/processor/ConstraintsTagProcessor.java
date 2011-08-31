/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.processor;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import org.jdom.Attribute;
import org.jdom.Element;
import org.swixml.LayoutConverter;
import org.swixml.LayoutConverterLibrary;
import org.swixml.Parser;

/**
 *
 * @author softphone
 */
public class ConstraintsTagProcessor implements TagProcessor {

    public static final TagProcessor instance = new ConstraintsTagProcessor();

    @SuppressWarnings("unchecked")
	public static <T extends Component> T processComponent(Parser p, Object obj, Element child, LayoutManager layoutMgr) throws Exception {
        
    	T result = null;
    	//
        //  A CONSTRAINTS attribute is removed from the childtag but used to add the child into the currrent obj
        //
        Attribute constrnAttr = child.getAttribute(Parser.ATTR_CONSTRAINTS);
        Object constrains = null;
        if (constrnAttr != null && layoutMgr != null) {
          child.removeAttribute(Parser.ATTR_CONSTRAINTS); // therefore it won't be used in getSwing(child)
          LayoutConverter layoutConverter = LayoutConverterLibrary.getInstance().getLayoutConverter(layoutMgr.getClass());
          if (layoutConverter != null)
            constrains = layoutConverter.convertConstraintsAttribute(constrnAttr);
        }

        //
        //  A CONSTRAINTS element is used to add the child into the currrent obj
        //
        Element constrnElement = child.getChild(Parser.TAG_CONSTRAINTS);
        if (constrnElement != null && layoutMgr != null) {
          LayoutConverter layoutConverter = LayoutConverterLibrary.getInstance().getLayoutConverter(layoutMgr.getClass());
          if (layoutConverter != null)
            constrains = layoutConverter.convertConstraintsElement(constrnElement);
        }
                //
        //  A constraints or GridBagConstraints grand-childtag is not added at all ..
        //  .. but used to add the child into this container
        //
        Element grandchild = child.getChild(Parser.TAG_GRIDBAGCONSTRAINTS);
        if (grandchild != null) {
        	result = (T) p.getSwing(child, null);
          p.addChild((Container) obj, result, p.getSwing(grandchild, null));
        } else if (!child.getName().equals(Parser.TAG_CONSTRAINTS) &&  !child.getName().equals(Parser.TAG_GRIDBAGCONSTRAINTS)) {
        	result = (T) p.getSwing(child, null);
          p.addChild((Container) obj, result, constrains);
        }

        return result;
    	
    }
    
    public boolean process(Parser p, Object obj, Element child, LayoutManager layoutMgr) throws Exception {

    	Component c = processComponent( p, obj, child, layoutMgr );
    	return c!=null;
    	
    }

}
