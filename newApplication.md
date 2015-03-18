## Description ##

Below we describe the basic steps to develop the first Swixml2 application

  * ### Define main application ###

> To create a new Swixml2 Application we have to inherit **SwingApplication** class that is the Base class for Swixml2 Desktop applications.

> It defines application lifecycle:
    * **initialize**
    * **startup**
    * **shutdown**

> It provides access to:
    * **Resources**
    * **Actions**
    * **Preferences**

> It provides integration with swixml engine through methods:
    * **render**
    * **show**

  * ### Define root component ###

> After created the Application we have to create the root component of application. It could be either a JFrame or JDialog. The Application must instantiate the root component, render and show it

  * ### Define XML for root component ###

> After created root component class we have to write  XML (swixml compliant) to define the layout, bindings and actions.

## Example ##

> Example concerns an application that show a classic login dialog

  * ### Define main application ###
```
public class LoginExample extends SwingApplication {

	@Override
	protected void startup() {
		
		try {
                        // Render dialog using swixml engine
			LoginDialog dialog = super.render(new LoginDialog(), "examples/explorer/LoginDialog.xml");

                        // Show Dialog
			super.show(dialog);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "error on startup " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			
			// Exit to application
			exit();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Application.launch(LoginExample.class, args);

	}

}
```

  * ### Define Root Component ###
```
public class LoginDialog extends JDialog  {
	
	private String login = "test";
	private String password;
	
	
	public final String getLogin() {
		return login;
	}

	public final void setLogin(String login) {
		this.login = login;		
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

	
	/**
	 * JSR296 annotation 
	 * 
	 * this method is bound with close button
	 */
	@Action
	public void close() {		
		setVisible(false);
	}

	
	/**
	 * JSR296 annotation 
	 * 
	 * this method is bound with submit button
	 */
	@Action
	public void submit() {
		JOptionPane.showMessageDialog(this,
					String.format("submit login=[%s] password=[%s]\n", getLogin(), getPassword()));
	}

```

  * ### Define XML for root component ###
```
<?xml version="1.0" encoding="windows-1252"?>

<dialog Size="500,500" resizable="false" title="Login Dialog"  defaultCloseOperation="JFrame.HIDE_ON_CLOSE">

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