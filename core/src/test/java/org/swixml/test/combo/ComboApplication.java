/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.test.combo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author softphone
 */
public class ComboApplication extends SwingApplication {

        public class ComboDialog extends JDialog {

            private java.util.List<String> data = new java.util.ArrayList<String>();
            private java.util.List<String> data2 = new java.util.ArrayList<String>();

            JComboBox combo;
            String text = "item0";
            String item = "item00";
            
            public ComboDialog() {

                data.add( "item1" );
                data2.add( "item11" );

            }
            public void setItem( String value ) {
                System.out.println( "setItem" + value );
                item = value;
            }

            public String getItem() {
                System.out.println( "getItem" + item );
                return item;
            }

            public void setText( String value ) {
                System.out.println( "setText" + value );
                text = value;
            }

            public String getText() {
                System.out.println( "getText" + text );
                return text;
            }

            public List<String> getData() {
                return data;
            }

            public List<String> getData2() {
                return data2;
            }

            @Override
            public void addNotify() {
                super.addNotify();

                final Binding b = Bindings.createAutoBinding( UpdateStrategy.READ_WRITE,
					this,
					//ELProperty.create( "${testValue}"),
					BeanProperty.create( "text"),
					this.combo.getEditor().getEditorComponent(),
					BeanProperty.create( "text")
					);

                b.bind();

                combo.addActionListener( new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        System.out.println( e );
                    }

                });

                combo.getEditor().addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        System.out.println( e );
                    }

                });

                combo.getEditor().getEditorComponent().addKeyListener(new KeyListener() {

                    public void keyTyped(KeyEvent e) {
                    }

                    public void keyPressed(KeyEvent e) {
                        //System.out.println( e );
                    }

                    public void keyReleased(KeyEvent e) {

                        System.out.println( combo.getEditor().getItem() );
                    }
                });

                //combo.getEditor().setItem("test");


            }

            public void submit() {
                System.out.println( combo.getEditor().getItem() );
            }


        }

	@Override
	protected void startup() {

		try {
			ComboDialog dialog = super.render(new ComboDialog(), "combo/ComboDialog.xml");

			super.show(dialog);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "error on startup " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

			// Exit to application
			//exit();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Application.launch(ComboApplication.class, args);

	}

}

