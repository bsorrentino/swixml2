package examples.explorer;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;
import org.swixml.jsr296.SwingApplication;


@SuppressWarnings("serial")
public class LoginDialog extends JDialog  {
	
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
	@Action
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
	@Action(enabledProperty=DATA_VALID)
	public void submit() {
		JOptionPane.showMessageDialog( SwingApplication.getInstance(SwingApplication.class).getMainFrame(), 
										String.format("submit login=[%s] password=[%s]\n", getLogin(), getPassword()));
	}

}
