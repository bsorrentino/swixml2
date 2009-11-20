/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.test.issue34;

/**
 *
 * @author softphone
 */
public class Issue34Bean2 extends Issue34Bean {

    public Issue34Bean2( Issue34Bean b ) {
        super( b.getDescription(), b.getDn() );
    }

}
