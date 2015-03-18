## Description ##

Always we have to create dialog that has default button. To do this with swixml2 is quite simple.
You have to inherit from `org.swixml.jsr.widgets.JDialogEx` and declare in your XML the chosen button having `id = 'defaultButton'`

#### Java source ####
```

public class LoginDialog extends JDialogEx  {

```

#### XML source ####

```

 <button id="defaultButton" text="Submit" action="enterAction"/>
 
```

See **dialog example** in [showcase](http://swixml2.googlecode.com/svn/jnlp/swixml2.jnlp)