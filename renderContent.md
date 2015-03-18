## Introduction ##

The main feature of swixml2 is to transform XML to Swing objects. This process is known as **render**.  the SwingApplication class (see [newApplication](newApplication.md)) publish several methods to allow this transformation.
The methods are:

```

/**
*
*@param container instance of container that must be rendered
*@param resource resource path (see ClassLoader().getResourceAsStream()
*@return the rendered instance (same of given parameter container)
*/
public final <T extends Container> T render( T container, String resource) throws Exception ;
  
public final <T extends Container> T render( T container, Reader reader) throws Exception ;

public final <T extends Container> T render( T container, File xmlFile) throws Exception ;

public final <T extends Container> T render( T container, java.net.URL url) throws Exception;

```