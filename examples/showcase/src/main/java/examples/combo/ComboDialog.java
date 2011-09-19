package examples.combo;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ComboBoxEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;

@SuppressWarnings("serial")
public class ComboDialog extends JDialog {
	private static final String VALID_PROPERTY = "validInput";
	
	private static final String ENTER_ACTION = "ENTER";
	private static final String ESCAPE_ACTION = "ESCAPE";

	public JComboBox cmbResult; // Automatically bound
	public JComboBox cmbTemplate; // Automatically bound
	protected JComboBox cmbNumber; // Automatically bound

	final  List<String> resultList ;
	final  List<String> templateList;

	private boolean removeOnSubmit = false;

    private String number;
	private String template;
	
	public ComboDialog() {

		templateList = Arrays.asList( "<select item>", "Item1", "Item2", "Item3" );
		resultList = ObservableCollections.observableList( new ArrayList<String>(Arrays.asList(  "Item1", "Item2", "Item3" )) );
		
		setNumber("Item2");
		setTemplate("Item3");
		
		
	}

    @Override
    public void addNotify() {

        ComboBoxEditor editor = cmbNumber.getEditor();


        Object editorComponent = editor.getEditorComponent();

        if( editorComponent instanceof JTextField ) {

            System.out.println( "TextField");
            
            final JTextComponent tc = (JTextComponent) editorComponent;

            tc.getDocument().addDocumentListener( new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {

                    SwingUtilities.invokeLater( new Runnable() {

                        public void run() {
                            String text = tc.getText();

                            tc.setText(text.replace('\n', '\b').replace('\r', '\b'));
                        }
                        
                    });

                    System.out.println( "insertUpdate" );
                }

                public void removeUpdate(DocumentEvent e) {
                    System.out.println( "removeUpdate" );
                }

                public void changedUpdate(DocumentEvent e) {
                    System.out.println( "changedUpdate" );
                }

            });

        }

        super.addNotify();

    }
	
	public final String getTemplate() {
		return template;
	}

	public final void setTemplate(String template) {
		this.template = template;
		firePropertyChange("template", null, null);
	}

	public final List<String> getResultList() {
		return resultList;
	}

	public final List<String> getTemplateList() {
		return templateList;
	}

	public final boolean isRemoveOnSubmit() {
		return removeOnSubmit;
	}

	public final void setRemoveOnSubmit(boolean value) {
		this.removeOnSubmit = value;
	}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
		firePropertyChange("number", null, null);
    }

	@Action
	public void selectTemplate() {
		firePropertyChange(VALID_PROPERTY, null, null);		
	}
	
	@Action
	public void selectResult() {
		firePropertyChange(VALID_PROPERTY, null, null);		
	}
	
	public void cancel() {
		
	}

	@Action(enabledProperty=VALID_PROPERTY)
	public void submit() {
		JOptionPane.showMessageDialog(this, cmbNumber.getSelectedItem());


		String selectItem = (String) cmbTemplate.getSelectedItem();
		
		if( isRemoveOnSubmit() ) {
			resultList.remove(selectItem);
		}

		//cmbTemplate.setSelectedIndex(0);				
		
		firePropertyChange(VALID_PROPERTY, null, null);
	}
	
	public boolean isValidInput() {
		
		return cmbTemplate.getSelectedIndex()>0;
	}
	
	protected JRootPane createRootPane() {
	    JRootPane rootPane = new JRootPane();
	    
	    KeyStroke stroke = KeyStroke.getKeyStroke(ESCAPE_ACTION);
	    KeyStroke strokeOk = KeyStroke.getKeyStroke(ENTER_ACTION);
	    
	    InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(stroke, ESCAPE_ACTION);
	    inputMap.put( strokeOk, ENTER_ACTION);
	    
	    rootPane.getActionMap().put(ESCAPE_ACTION, new AbstractAction() {
		      public void actionPerformed(ActionEvent actionEvent) {
			        cancel();
			      }
	    });
	    
	    rootPane.getActionMap().put(ENTER_ACTION, new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				if( isValid() ) 
					submit();
			}
	    	
	    });

	    return rootPane;
	  }

}
