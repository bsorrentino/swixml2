/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.processor;

import java.awt.LayoutManager;
import org.jdom.Element;
import org.swixml.Parser;

/**
 *
 * @author softphone
 */
public interface TagProcessor {

    boolean process( Parser p, Object obj, Element child, LayoutManager layoutMgr  ) throws Exception;

}
