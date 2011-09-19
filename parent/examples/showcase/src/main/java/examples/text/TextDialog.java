package examples.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.jdesktop.application.Action;
import org.swixml.jsr.widgets.JDialogEx;

@SuppressWarnings("serial")
public class TextDialog extends JDialogEx{

	class JTextFieldLimit extends PlainDocument {
		  private int limit;

		  JTextFieldLimit(int limit) {
		    super();
		    this.limit = limit;
		  }

		  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		    if (str == null)
		      return;

		    if ((getLength() + str.length()) <= limit) {
		      super.insertString(offset, str, attr);
		    }
		  }
		}
	
	private final JTextFieldLimit textLimit = new JTextFieldLimit(15);

	/*
	 * property bound with textField (see TextDialog.xml)
	 */
	public JTextFieldLimit getTextLimit() {
		return textLimit;
	}
	
	@Action
	public void close() {
		setVisible(false);
		//SwingApplication.getInstance().exit();
	}
	
}
