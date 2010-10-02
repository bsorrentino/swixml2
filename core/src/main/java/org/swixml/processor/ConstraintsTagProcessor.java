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

    public boolean process(Parser p, Object obj, Element child, LayoutManager layoutMgr) throws Exception {

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
        p.addChild((Container) obj, (Component) p.getSwing(child, null), p.getSwing(grandchild, null));
      } else if (!child.getName().equals(Parser.TAG_CONSTRAINTS) &&
              !child.getName().equals(Parser.TAG_GRIDBAGCONSTRAINTS)) {
        p.addChild((Container) obj, (Component) p.getSwing(child, null), constrains);
      }

      return true;
    }

}
