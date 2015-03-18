## Introduction ##

During **render** (see [renderContent](renderContent.md))  is possible to set any attribute of swing component. The original [swixml](swixml.md) projects allowed to set  constant value to attributes and provided **initClass** feature to allow the developer to provide a dynamic value through constructor.

Not like [swixml](swixml.md) the [swixml2](swixml2.md) introduces the **dynamic expression evaluation** that allow us to set, whatever attribute, using a property (i.e. getter method) belonging to rendered object.

## Example ##

For example if we want to set size of a Dialog using a **dynamic expression evaluation** in Xml we have to write:

```
<?xml version="1.0" encoding="windows-1252"?>

<dialog Size="${customSize}" resizable="false" title="Login Dialog"  defaultCloseOperation="JFrame.HIDE_ON_CLOSE">

…..


</dialog>
```

while in the Java code :

```
public class MyDialog extends JDialog {

public Dimension getCustomSize() { return new Dimension(100,100); }

}
```

and automatically, during render process, to set the size property will be called the getter method (i.e. the property)

As you see, it is enough to use **${_<property name>_}** to active the very useful **dynamic expression evaluation**.