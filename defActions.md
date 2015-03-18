## Introduction ##

The very important feature added in Swixml2 is the possibility to bind an action from Xml to code using the **JSR296** annotation **`@org.jdesktop.application.Action`**.
Using of annotations make the code more understandable and easier to mantain.

To use this feature we have to set in XML attribute **action** with a name that must correspond to a method in Component decorated with **`@Action`** annotation.

Returning on [LoginDialog](newApplication#Define_Root_Component.md) example, we see that we have two public methods named **submit** and **close** that have the **@Action** annotation, they are bound with the buttons in [XML](newApplication#Define_XML_for_root_component.md) with action attributes set to **submit** and **close**

#### LoginDialog.java ####
```
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

#### LoginDialog.xml ####

```
    <hbox>
        <box.glue/>
        <button text="Submit" action="submit"/>
        <box.hstrut width="5"/>
        <button text="Close" action="close"/>
    </hbox>

```

## enable/disable actions ##

_TODO_