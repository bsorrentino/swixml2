/*
 * Copyright (C) 2007 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 *
 *//* Generated By:JJTree: Do not edit this line. AstFloatingPoint.java */

package org.jdesktop.el.impl.parser;

import java.math.BigDecimal;

import org.jdesktop.el.ELException;

import org.jdesktop.el.impl.lang.EvaluationContext;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public final class AstFloatingPoint extends SimpleNode {
    public AstFloatingPoint(int id) {
        super(id);
    }

    private Number number;

    public Number getFloatingPoint() {
        if (this.number == null) {
            try {
                this.number = new Double(this.image);
            } catch (ArithmeticException e0) {
                this.number = new BigDecimal(this.image);
            }
        }
        return this.number;
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        return this.getFloatingPoint();
    }

    public Class getType(EvaluationContext ctx)
            throws ELException {
        return this.getFloatingPoint().getClass();
    }
}
