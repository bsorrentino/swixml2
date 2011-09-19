/*--
 Copyright (C) 2003-2007 Wolf Paulus.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions, and the disclaimer that follows
 these conditions in the documentation and/or other materials provided
 with the distribution.

 3. The end-user documentation included with the redistribution,
 if any, must include the following acknowledgment:
        "This product includes software developed by the
         SWIXML Project (http://www.swixml.org/)."
 Alternately, this acknowledgment may appear in the software itself,
 if and wherever such third-party acknowledgments normally appear.

 4. The name "Swixml" must not be used to endorse or promote products
 derived from this software without prior written permission. For
 written permission, please contact <info_AT_swixml_DOT_org>

 5. Products derived from this software may not be called "Swixml",
 nor may "Swixml" appear in their name, without prior written
 permission from the Swixml Project Management.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE SWIXML PROJECT OR ITS
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 ====================================================================

 This software consists of voluntary contributions made by many
 individuals on behalf of the Swixml Project and was originally
 created by Wolf Paulus <wolf_AT_swixml_DOT_org>. For more information
 on the Swixml Project, please see <http://www.swixml.org/>.
*/
package org.swixml;


import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.logging.Level;

import javax.swing.AbstractAction;

import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.swixml.jsr296.SwingApplication;
/**
 * XAction, Action Wrapper to generate Actions on  the fly.
 * @author <a href="mailto:wolf@wolfpaulus.com">Wolf Paulus</a>
 */

@SuppressWarnings("serial")
public class XAction extends AbstractAction implements LogAware {
  Method method;
  Object client;
  ApplicationAction delegate;
  
  public XAction(Object client, String methodName) throws NoSuchMethodException {
    this.client= client;

    try {
    	ApplicationContext ctx = SwingApplication.getInstance().getContext();
    	
    	ApplicationActionMap actionMap =ctx.getActionMap(client);
    	
    	delegate = (ApplicationAction)actionMap.get(methodName);

    	if( delegate==null ) {
    		method= client.getClass().getMethod(methodName);
    	}
	
    } catch( Exception e ) {
    	// @TODO
    	//logger.log( Level.WARNING, "erron on Action initialization", e);
    	logger.warning( String.format( "error on Action initialization [%s]", e.getMessage()));
    }
    

  }
  
  public void actionPerformed(ActionEvent e) {
    try {
        if( null==delegate ) {
        	if( this.method!=null ) this.method.invoke(client);
        }
        else {
            delegate.actionPerformed(e);
        }
        
    } catch (Exception e1) {
    	logger.log( Level.WARNING, "action performed error", e1);
    } 
  }

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if( null!=delegate ) delegate.addPropertyChangeListener(listener);
        super.addPropertyChangeListener(listener);
    }

    @Override
    public Object getValue(String key) {
        return ( null!=delegate ) ? delegate.getValue(key): super.getValue(key);
    }

    @Override
    public boolean isEnabled() {
        return  ( null!=delegate ) ? delegate.isEnabled(): super.isEnabled();        
    }

    @Override
    public void putValue(String key, Object newValue) {
        if( null!=delegate )
        	delegate.putValue(key, newValue);
        else
        	super.putValue(key, newValue);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if( null!=delegate ) delegate.removePropertyChangeListener(listener);
        super.removePropertyChangeListener(listener);
    }

    @Override
    public void setEnabled(boolean newValue) {
        logger.fine(String.format( "setEnabled(%s)\n", newValue));
        if( null!=delegate ) delegate.setEnabled(newValue);
        super.setEnabled(newValue);
    }
  
}
