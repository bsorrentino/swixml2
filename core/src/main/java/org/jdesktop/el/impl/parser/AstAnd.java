/*
 * Copyright (C) 2007 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 *
 *//* Generated By:JJTree: Do not edit this line. AstAnd.java */

package org.jdesktop.el.impl.parser;

import org.jdesktop.el.ELContext;
import org.jdesktop.el.ELException;

import org.jdesktop.el.impl.lang.EvaluationContext;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public final class AstAnd extends BooleanNode {
    public AstAnd(int id) {
        super(id);
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        Object obj = children[0].getValue(ctx);
        if (obj == ELContext.UNRESOLVABLE_RESULT) {
            return ELContext.UNRESOLVABLE_RESULT;
        }
        Boolean b = coerceToBoolean(obj);
        if (!b.booleanValue()) {
            return b;
        }
        obj = children[1].getValue(ctx);
        if (obj == ELContext.UNRESOLVABLE_RESULT) {
            return ELContext.UNRESOLVABLE_RESULT;
        }
        b = coerceToBoolean(obj);
        return b;
    }
}
