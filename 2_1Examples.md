# SIMPLE LOGIN DIALOG #

## SimpleDialog.xml ##

```
<dialog Size="500,500" resizable="false" title="Simple Dialog"  defaultCloseOperation="JFrame.DO_NOTHING_ON_CLOSE">

<vbox>

    <panel  layout="GridLayout(2,2,1,10)" border="CompoundBorder(EmptyBorder(10,1,10,10), TitledBorder(Security information))">
        <label text="Login" />
        <textfield bindWith="login" columns="15"/>

        <label text="Password" />
        <passwordField bindWith="password"  columns="15" />
    </panel>

   	<box.vstrut height="10"/>

    <hbox>
    	<box.glue/>
    	<button text="Submit" action="submit"/>
    	<box.hstrut width="5"/>
    	<button text="Close" action="close"/>
    </hbox>
    
</vbox>

</dialog>
```


---


## SimpelDialogApplication.java ##

```
public class SimpleDialogApplication extends SwingApplication {

	private static final String DATA_VALID = "dataValid";
	/** bound property - MUST BE DEFINED getter & setter */
	private String login;
	/** bound property - MUST BE DEFINED getter & setter */
	private String password;
	
	public static void main(String args []) {
		SwingApplication.launch(SimpleDialogApplication.class, args);
	}
	
	public final String getLogin() { return login; }

	public final void setLogin(String login) { 
		this.login = login;
		validateData();
	}

	public final String getPassword() { return password; }

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
	 * this method is bound with submit button
	 * 
	 * action will be enabled when property isDataValid returns true
	 */
	@Action(enabledProperty=DATA_VALID)
	public void submit() {
		JOptionPane.showMessageDialog( getMainFrame(), String.format("submit login=[%s] password=[%s]\n", getLogin(), getPassword()));
	}
	
	/**
	 * JSR296 annotation 
	 * 
	 * this method is bound with close button
	 */
	@Action
	public void close() {
		exit();
	}

	
	@Override
	protected void startup() {

		try {
			JDialog dialog = renderDialog("examples/SimpleDialog.xml"); 
			
			show( dialog );
			
		} catch (Exception e) {

			e.printStackTrace();
			exit();
		}
		
	}

}

```