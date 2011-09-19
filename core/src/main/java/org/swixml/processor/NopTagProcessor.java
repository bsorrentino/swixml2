/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.processor;

import java.awt.LayoutManager;

import org.swixml.Parser;

/**
 *
 * @author softphone
 */
public class NopTagProcessor implements TagProcessor {
    public static final TagProcessor instance = new NopTagProcessor();

    public boolean process(Parser p, Object obj, org.w3c.dom.Element child, LayoutManager layoutMgr) throws Exception {
        return true;
    }

}
