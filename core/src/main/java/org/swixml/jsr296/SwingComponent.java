/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr296;

import java.awt.Container;

import javax.swing.Action;

/**
 *
 * @author Sorrentino
 */
public interface SwingComponent {

		Action getComponentAction( String name );

		<T extends Container> T render( String resource ) throws Exception;
}
