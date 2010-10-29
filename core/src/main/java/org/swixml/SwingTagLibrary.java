/*--
 $Id: SwingTagLibrary.java,v 1.1 2004/03/01 07:56:07 wolfpaulus Exp $

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

import java.util.ServiceLoader;
import static org.swixml.LogUtil.logger;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.table.JTableHeader;

import org.swixml.BoxFactory.Type;
import org.swixml.jsr.widgets.JCheckBoxEx;
import org.swixml.jsr.widgets.JComboBoxEx;
import org.swixml.jsr.widgets.JLabelEx;
import org.swixml.jsr.widgets.JListEx;
import org.swixml.jsr.widgets.JPasswordFieldEx;
import org.swixml.jsr.widgets.JRadioButtonEx;
import org.swixml.jsr.widgets.JSliderEx;
import org.swixml.jsr.widgets.JSpinnerEx;
import org.swixml.jsr.widgets.JTableEx;
import org.swixml.jsr.widgets.JTextAreaEx;
import org.swixml.jsr.widgets.JTextFieldEx;
import org.swixml.jsr.widgets.JTreeEx;
import org.swixml.jsr295.BindingUtils;
import org.swixml.processor.NopTagProcessor;
import org.swixml.processor.TableColumnTagProcessor;

/**
 * The SwingTagLibrary contains Factories for all Swing Objects that can be instatiated by
 * parsing an XML descriptor at runtime.
 *
 * Date: Dec 9, 2002
 *
 * @author <a href="mailto:wolf@wolfpaulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 * @see org.swixml.TagLibrary

 */
public final class SwingTagLibrary extends TagLibrary {

  private static SwingTagLibrary INSTANCE = new SwingTagLibrary();
  public static SwingTagLibrary getInstance() {
    return SwingTagLibrary.INSTANCE;
  }

  /**
   * Constructs a Swing Library by registering swings widgets
   */
  private SwingTagLibrary() {
    registerTags();
  }

  /**
   * Registers the tags this TagLibrary is all about.
   * Strategy method called by the super class, allowing
   * derived classes to change the registration behaviour.
   */
  protected void registerTags() {


    registerTag( "ButtonGroup", ButtonGroup.class, NopTagProcessor.instance );
    registerTag( "Separator", JSeparator.class, NopTagProcessor.instance );

    registerTag( "Applet", JApplet.class );
    registerTag( "Button", JButton.class );
    registerTag( "CheckBoxMenuItem", JCheckBoxMenuItem.class );
    registerTag( "Component", JComponent.class );
    registerTag( "DesktopPane", JDesktopPane.class );
    registerTag( "Dialog", XDialog.class );
    registerTag( "EditorPane", JEditorPane.class );
    registerTag( "FormattedTextField", JFormattedTextField.class );
    registerTag( "Frame",JFrame.class);
    registerTag( "Glue", XGlue.class );
    registerTag( "GridBagConstraints", XGridBagConstraints.class );
    registerTag( "InternalFrame", JInternalFrame.class );
    registerTag( "Menu", JMenu.class );
    registerTag( "Menubar", JMenuBar.class );
    registerTag( "Menuitem", JMenuItem.class );
    registerTag( "Panel", JPanel.class );
    registerTag( "PopupMenu", JPopupMenu.class );
    registerTag( "ProgressBar", JProgressBar.class );
    registerTag( "RadioButtonMenuItem", JRadioButtonMenuItem.class );
    registerTag( "OptionPane", JOptionPane.class );
    registerTag( "ScrollPane", XScrollPane.class );
    registerTag( "SplitPane", XSplitPane.class );
    registerTag( "TabbedPane", XTabbedPane.class );
    registerTag( "TableHeader", JTableHeader.class );
    registerTag( "TextPane", JTextPane.class );
    registerTag( "TitledSeparator", XTitledSeparator.class );
    registerTag( "ToggleButton", JToggleButton.class );
    registerTag( "Toolbar", JToolBar.class );

// LET'S INTRODUCE (JSR295) BINDING AND (JSR296) ACTION SUPPORT     
    //registerTag( "List", JList.class );
    registerTag( "List", JListEx.class );
    //registerTag( "ComboBox", JComboBox.class );
    registerTag( "ComboBox", JComboBoxEx.class );
    //registerTag( "Checkbox", JCheckBox.class );
    registerTag( "Checkbox", JCheckBoxEx.class );
    //registerTag( "PasswordField", JPasswordField.class );
    registerTag( "PasswordField", JPasswordFieldEx.class );
    //registerTag( "TextArea", JTextArea.class );
    registerTag( "TextArea", JTextAreaEx.class );
    //registerTag( "Table", JTable.class );
    registerTag( "Table", JTableEx.class, new TableColumnTagProcessor() );
    //registerTag( "Label", JLabel.class );
    registerTag( "Label", JLabelEx.class );
    //registerTag( "Tree", JTree.class );
    registerTag( "Tree", JTreeEx.class );
    //registerTag( "TextField", JTextField.class );
    registerTag( "TextField", JTextFieldEx.class );
    //registerTag( "RadioButton", JRadioButton.class );
    registerTag( "RadioButton", JRadioButtonEx.class );
    //registerTag( "Spinner", JSpinner.class );
    registerTag( "Spinner", JSpinnerEx.class );
    registerTag( "Spinner.Date", JSpinnerEx.Date.class );
    //registerTag( "Slider", JSlider.class );
    registerTag( "Slider", JSliderEx.class );
  
// NEW TAGs
    registerTag("box.glue", new BoxFactory(Type.GLUE));
    registerTag("box.hglue", new BoxFactory(Type.HGLUE));
    registerTag("box.vglue", new BoxFactory(Type.VGLUE));
    registerTag("box.hstrut", new BoxFactory(Type.HSTRUT));
    registerTag("box.vstrut", new BoxFactory(Type.VSTRUT));
    registerTag("box.rigidarea", new BoxFactory(Type.RIGIDAREA));
    registerTag("vgapbox", BoxFactory.VGapBox.class);
    registerTag("hgapbox", BoxFactory.HGapBox.class);
    //registerTag( "HBox", XHBox.class );
    registerTag("VBox", BoxFactory.VGapBox.class);
    //registerTag( "VBox", XVBox.class );
    registerTag("HBox", BoxFactory.HGapBox.class);
	
    registerTag("tableColumn", BindingUtils.Column.class );
    
    ServiceLoader<TagLibraryService> loader = ServiceLoader.load(TagLibraryService.class);
    if( loader== null ) return;
    
    for( TagLibraryService tls : loader ) {
    	logger.info( "processing TagLibrary service provider " + tls);
    	
    	tls.registerTags(this);
    	
    }
  }
}

