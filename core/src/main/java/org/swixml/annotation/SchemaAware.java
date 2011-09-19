/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author bsorrentino
 */
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(ElementType.FIELD)
public @interface SchemaAware {

}
