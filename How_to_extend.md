## Description ##

Below we describe how to extends Swixml2 framework

### Add a New Tag(s) ###

#### Basic Technique ####

the simplest way to add a new tag in swixml is to use the `registerTag` method of `org.swixml.SwingTagLibrary`. An example has shown below:

```
org.swixml.SwingTagLibrary.getInstance().registerTag( "buttonClose", JButtonClose.class);
org.swixml.SwingTagLibrary.getInstance().registerTag( "textFieldDn", JTextFieldDn.class);
org.swixml.SwingTagLibrary.getInstance().registerTag( "mediaChannelGroup", MediaChannelGroup.class);
org.swixml.SwingTagLibrary.getInstance().registerTag( "interactionList", JInteractionListEx.class);
org.swixml.SwingTagLibrary.getInstance().registerTag( "toggleButton", JToggleButtonEx.class);
org.swixml.SwingTagLibrary.getInstance().registerTag( "buttonKey", JButtonKey.class);
org.swixml.SwingTagLibrary.getInstance().registerTag( "phoneNumberCombo", JPhoneNumberComboBox.class);
org.swixml.SwingTagLibrary.getInstance().registerTag("slider", JSliderEx.class);
org.swixml.SwingTagLibrary.getInstance().registerTag("linecontrol", TabbedLineeControl.class);

```

#### Advanced Technique -  Using Service Provider Interface (SPI) ####

There is another possibility provided by swixml to add custom tags in more flexible way. It is the java specification named **Service Provide Interface(SPI)**

This allow you to add custom tags using **Inversion of Control(IoC)** technique. The basic steps to implements custom tags using SPI are outlined below:

  1. create a new class as example `MyTagLibProvider` that implements **`org.swixml.TagLibraryService`**
  1. add your registration tag statements within method **`registerTags(TagLibrary library)`**. An example has shown below
```
	public void registerTags(TagLibrary library) {
         library.registerTag("menubutton", JSimpleMenuButton.class);
         library.registerTag("animatedbutton", JAnimatedButton.class);
        
         library.registerTag("RTextScrollPane", RTextScrollPane.class);     
         library.registerTag("RSyntaxTextArea", RSyntaxTextAreaEx.class);
        
         library.registerTag("GoogleMapPanel", GoogleMapPanel.class);
	}

```
  1. create a new txt file named **org.swixml.TagLibraryService** under  folder _<jar home>_**/META-INF/services**
  1. put within the file **org.swixml.TagLibraryService** one line with the **Full Qualified Name(FQN)** of your provider class.

After this , start the application and the provider will be invoked in automatic way registering your tags

#### Notes ####
**The steps 3,4 could be automatically handled by using [metainf  services](http://metainf-services.kohsuke.org/)**