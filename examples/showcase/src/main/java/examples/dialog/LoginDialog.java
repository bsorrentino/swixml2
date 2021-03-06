package examples.dialog;

import javax.swing.JOptionPane;

import org.jdesktop.application.Action;
import org.swixml.jsr.widgets.JDialogEx;
import org.swixml.jsr296.SWIXMLApplication;


@SuppressWarnings("serial")
public class LoginDialog extends JDialogEx  {
	
	private static final String DATA_VALID = "dataValid";
	/** bound property - MUST BE DEFINED getter & setter */
	private String login = "test";
	/** bound property - MUST BE DEFINED getter & setter */
	private String password;
	
	
	public final String getLogin() {
		return login;
	}

	public final void setLogin(String login) {
		this.login = login;
		
		validateData();
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
		
		validateData();
	}

	/**
	 * method that force the data validation
	 */
	protected void validateData() {
		firePropertyChange( DATA_VALID , null, null );// inherited method	
	}
	
	/**
	 * simple data validation method 
	 * @return true whether data are valid
	 */
	public boolean isDataValid() {
		return null!=password && (null!=login && login.length()>0);
	}
	
	/**
	 * JSR296 annotation 
	 * 
	 * this method is bound with close button
	 */
	@Action(name="escapeAction")
	public void close() {
		// Exit from application
		//Application.getInstance().exit();
		
		setVisible(false);
	}

	
	/**
	 * JSR296 annotation 
	 * 
	 * this method is bound with submit button
	 * 
	 * action will be enabled when property isDataValid returns true
	 */
	@Action(name="enterAction",enabledProperty=DATA_VALID)
	public void submit() {
		JOptionPane.showMessageDialog( SWIXMLApplication.getInstance(SWIXMLApplication.class).getMainFrame(), 
										String.format("submit login=[%s] password=[%s]\n", getLogin(), getPassword()));
	}

}
