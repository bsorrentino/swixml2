/*--
 $Id: SwingEngine.java,v 1.5 2005/06/29 08:02:37 wolfpaulus Exp $

 Copyright (C) 2003-2007 Wolf Paulus.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions, and the disclaimer that follows
 these conditions in the documentation and/or other materials provided
 with the distribution.

 3. The end-user documentation included with the redistribution,
 if any, must include the following acknowledgment:
        "This product includes software developed by the
         SWIXML Project (http://www.swixml.org/)."
 Alternately, this acknowledgment may appear in the software itself,
 if and wherever such third-party acknowledgments normally appear.

 4. The name "Swixml" must not be used to endorse or promote products
 derived from this software without prior written permission. For
 written permission, please contact <info_AT_swixml_DOT_org>

 5. Products derived from this software may not be called "Swixml",
 nor may "Swixml" appear in their name, without prior written
 permission from the Swixml Project Management.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE SWIXML PROJECT OR ITS
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 ====================================================================

 This software consists of voluntary contributions made by many
 individuals on behalf of the Swixml Project and was originally
 created by Wolf Paulus <wolf_AT_swixml_DOT_org>. For more information
 on the Swixml Project, please see <http://www.swixml.org/>.
*/
package org.swixml;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptException;
import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import static org.swixml.LogAware.logger;
import org.swixml.dom.DOMUtil;
import org.swixml.localization.LocalizerDefaultImpl;
import org.swixml.localization.LocalizerJSR296Impl;
import org.swixml.script.ScriptService;
import org.swixml.script.ScriptServiceDefaultImpl;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

/**
 * The SwingEngine class is the rendering engine able to convert an XML descriptor into a java.swing UI.
 * <p/>
 * <img src="doc-files/swixml_1_0.png" ALIGN="center">
 * </p>
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.5 $
 */
public class SwingEngine<T extends Container> implements LogAware {
    public static final String DESIGN_TIME = "org.swixml.designTime";

	public static interface Namespaces {
		
	    final String main = "http://www.swixml.org/2007/Swixml";
	    
	    final String script = "http://www.swixml.org/2007/Swixml/script";

	}
	//
	//  Static Constants
	//
  
	public static final String CLIENT_PROPERTY = "org.swixml.client";
	

  /**
   * Mac OSX locale variant to localize strings like quit etc.
   */
  public static final String MAC_OSX_LOCALE_VARIANT = "mac";

  /**
   * XML Error
   */
  private static final String XML_ERROR_MSG = "Invalid SwiXML Descriptor.";
  /**
   * IO Error Message.
   */
  private static final String IO_ERROR_MSG = "Resource could not be found ";
  /**
   * Mapping Error Message.
   */
  private static final String MAPPING_ERROR_MSG = " could not be mapped to any Object and remained un-initialized.";

  //
  //  Static Member Variables
  //

  /**
   * Debug / Release Mode
   */
  public static boolean DEBUG_MODE = true;
  /**
   * main frame
   */
  //private static Frame appFrame;
  /**
   * static resource bundle
   */
  private static String default_resource_bundle_name = null;
  /**
   * static locale
   */
  private static Locale default_locale = Locale.getDefault();
  /**
   * static Mac OS X Support, set to true to support Mac UI specialties
   */
  private static boolean MAC_OSX_SUPPORTED = true;

  //
  //   Static Initializer
  //
  /** display the swing release version to system out. */
  static {
    System.out.printf("SWIXML2 - %s\n", getVersion());
  }

  private static String getVersion() 
  {
    final String className = SwingEngine.class.getSimpleName() + ".class";
    final String classPath = SwingEngine.class.getResource(className).toString();
    
    if (!classPath.startsWith("jar")) {
      // Class not from JAR
      return "development version";
    }
    
    final String manifestPath = new StringBuilder()
                                    .append(classPath.substring(0, classPath.lastIndexOf("!") + 1))
                                    .append("/META-INF/MANIFEST.MF")
                                    .toString();
    
    
        try {
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            final java.util.jar.Attributes attr = manifest.getMainAttributes();

            /*
            for( Object a : attr.keySet()) {
                System.out.printf( "key=[%s]\n", String.valueOf(a) );
            }
            */
            return  attr.getValue("Implementation-Version");
        } catch (IOException ex) {
            
            return "<unknown version>";
        }
  }
  
  public static boolean isDesignTime( ) {
      return Boolean.getBoolean(DESIGN_TIME);
  }
  //
  //  Member Variables
  //
  /**
   * Swixml Parser.
   */
  private Parser parser = new Parser(this);
  /**
   * Client object hosting the swingengine, alternative to extending the SwinEngine Class
   */
  private T client;

  /**
   * Swing object map, contains only those object that were given an id attribute.
   */
  private Map<String, Object> idmap = new HashMap<String, Object>();
  /**
   * Flattened Swing object tree, contains all object, even the ones without an id.
   */
  private Collection<Component> components = null;
  /**
   * access to taglib to let overwriting class add and remove tags.
   */
  private Localizer localizer = null;
  //
  //  Private Constants
  //
  /**
   * Classload to load resources
   */
  private final TagLibrary taglib = SwingTagLibrary.getInstance();
  /**
   * Localizer, setup by parameters found in the xml descriptor.
   */
  protected ClassLoader cl = this.getClass().getClassLoader();
  
  private ScriptService script;
  
  /**
   * Default ctor for a SwingEngine.
   */
  private SwingEngine() {
    //if( Boolean.getBoolean( Application.USE_COMMON_LOCALIZER ) ) {
    if( Application.getBooleanProperty(Application.USE_COMMON_LOCALIZER) ) {
    	localizer = new LocalizerJSR296Impl();
    }
    else {
    	localizer = new LocalizerDefaultImpl(); 
    }
    this.setLocale(SwingEngine.default_locale);
    localizer.setResourceBundle(SwingEngine.default_resource_bundle_name);
    
  }

  /**
   * Constructor to be used if the SwingEngine is not extend but used through object composition.
   *
   * @param client <code>Object</code> owner of this instance
   */
  public SwingEngine(T client) {
    this(client, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Constructor to be used if the SwingEngine is not extend but used through object composition.
   *
   * @param client <code>Object</code> owner of this instance
   * @param loader 
   */
  public SwingEngine(T client, ClassLoader loader) {
      this();
      this.client = client;
      setClassLoader(loader);

      try {
          this.script = new ScriptServiceDefaultImpl();

          script.put("client", client);

      } catch (ScriptException e) {
          logger.log(Level.SEVERE, "error initializing script", e);
      }

  }

  public ScriptService getScript() {
	return script;
}

/**
   * Gets the parsing of the XML started.
   *
   * @param url <code>URL</code> url pointing to an XML descriptor
   * @return <code>Object</code>- instanced swing object tree root
   * @throws Exception
   */
  public T render(final URL url) throws Exception {
    Reader reader = null;
    T obj = null;
    try {
      InputStream in = url.openStream();
      if (in == null) {
        throw new IOException(IO_ERROR_MSG + url.toString());
      }
      reader = new InputStreamReader(in);
      obj = render(reader);
    } finally {
      try {
        reader.close();
      } catch (Exception ex) {
        // intentionally empty
      }
    }
    return obj;
  }

  /**
   * Gets the parsing of the XML file started.
   *
   * @param resource <code>String</code> xml-file path info
   * @return <code>Object</code>- instanced swing object tree root
   */
  public T render(final String resource) throws Exception {
    Reader reader = null;
    T obj = null;
    try {
      InputStream in = cl.getResourceAsStream(resource);
      if (in == null) {
        throw new IOException(IO_ERROR_MSG + resource);
      }
      reader = new InputStreamReader(in);
      obj = render(reader);
    } finally {
      try {
        reader.close();
      } catch (Exception ex) {
        // intentionally empty
      }
    }
    return obj;
  }

  /**
   * Gets the parsing of the XML file started.
   *
   * @param xml_file <code>File</code> xml-file
   * @return <code>Object</code>- instanced swing object tree root
   */
  public T render(final File xml_file) throws Exception {
    if (xml_file == null) {
      throw new IOException();
    }
    return render(new FileReader(xml_file));
  }

  /**
   * Gets the parsing of the XML file started.
   *
   * @param xml_reader <code>Reader</code> xml-file path info
   * @return <code>Object</code>- instanced swing object tree root
   */
  public T render(final Reader xml_reader) throws Exception {
    if (xml_reader == null) {
      throw new IllegalArgumentException( "input reader is null!");
    }
    try {
      Document doc = DOMUtil.getDocumentBuilder().parse( new InputSource(xml_reader) );	
      return render(doc);
    } catch (Exception e) {
    	logger.log(Level.SEVERE, "parse exception", e);
    	throw e;
    }
    //throw new Exception(SwingEngine.XML_ERROR_MSG);
  }

  /**
   * Gets the parsing of the XML file started.
   *
   * @param jdoc <code>Document</code> xml gui descritptor
   * @return <code>Object</code>- instanced swing object tree root
   */
  private T render(final Document jdoc) throws Exception {
    T result = null;

    idmap.clear();
    try {
        if( client!=null ) {
            parser.parse(jdoc, client);
            result = client;
        }
        else {
            result = (T)parser.parse(jdoc, null);
        }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "error parsing XML document", e);
      throw (e);
    }


    // reset components collection
    components = null;
    // initialize all client fields with UI components by their id
    
    // Issue 44
    //mapMembers(result);
    
    //if (Frame.class.isAssignableFrom(client.getClass())) {
    //  SwingEngine.setAppFrame((Frame) client);
    //}
    return result;
  }

  /**
   * Inserts swing object rendered from an XML document into the given container.
   * <p/>
   * <pre>
   *  Differently to the render methods, insert does NOT consider the root node of the XML document.
   * </pre>
   * <pre>
   *  <b>NOTE:</b><br>insert() does NOT clear() the idmap before rendering.
   * Therefore, if this SwingEngine's parser was used before, the idmap still
   * contains (key/value) pairs (id, JComponent obj. references).<br>If insert() is NOT
   * used to insert in a previously (with this very SwingEngine) rendered UI,
   * it is highly recommended to clear the idmap:
   * <br>
   *  <div>
   *    <code>mySwingEngine.getIdMap().clear()</code>
   *  </div>
   * </pre>
   *
   * @param url       <code>URL</code> url pointing to an XML descriptor *
   * @param container <code>Container</code> target, the swing obj, are added to.
   * @throws Exception
   */
  public void insert(final URL url, final T container) throws Exception {
    Reader reader = null;
    try {
      InputStream in = url.openStream();
      if (in == null) {
        throw new IOException(IO_ERROR_MSG + url.toString());
      }
      reader = new InputStreamReader(in);
      insert(reader, container);
    } finally {
      try {
        reader.close();
      } catch (Exception ex) {
        // intentionally empty
      }
    }
  }

  /**
   * Inserts swing objects rendered from an XML reader into the given container.
   * <p/>
   * <pre>
   *  Differently to the render methods, insert does NOT consider the root node of the XML document.
   * </pre>
   * <pre>
   *  <b>NOTE:</b><br>insert() does NOT clear() the idmap before rendering.
   * Therefore, if this SwingEngine's parser was used before, the idmap still
   * contains (key/value) pairs (id, JComponent obj. references).<br>If insert() is NOT
   * used to insert in a previously (with this very SwingEngine) rendered UI, it is highly
   * recommended to clear the idmap:
   * <br>
   *  <div>
   *    <code>mySwingEngine.getIdMap().clear()</code>
   *  </div>
   * </pre>
   *
   * @param reader    <code>Reader</code> xml-file path info
   * @param container <code>Container</code> target, the swing obj, are added to.
   * @throws Exception
   */
  public void insert(final Reader reader, final T container) throws Exception {
    if (reader == null) {
        throw new IllegalArgumentException( "input reader is null!");
    }
    
    Document doc = DOMUtil.getDocumentBuilder().parse( new InputSource(reader) );	

    insert(doc, container);
  }

  /**
   * Inserts swing objects rendered from an XML reader into the given container.
   * <p/>
   * <pre>
   *  Differently to the render methods, insert does NOT consider the root node of the XML document.
   * </pre>
   * <pre>
   *  <b>NOTE:</b><br>insert() does NOT clear() the idmap before rendering.
   * Therefore, if this SwingEngine's parser was used before, the idmap still
   * contains (key/value) pairs (id, JComponent obj. references).<br>
   * If insert() is NOT used to insert in a previously (with this very SwingEngine)
   * rendered UI, it is highly recommended to clear the idmap:
   * <br>
   *  <div>
   *    <code>mySwingEngine.getIdMap().clear()</code>
   *  </div>
   * </pre>
   *
   * @param resource  <code>String</code> xml-file path info
   * @param container <code>Container</code> target, the swing obj, are added to.
   * @throws Exception
   */
  public void insert(final String resource, final T container) throws Exception {
    Reader reader = null;
    try {
      InputStream in = cl.getResourceAsStream(resource);
      if (in == null) {
        throw new IOException(IO_ERROR_MSG + resource);
      }
      reader = new InputStreamReader(in);
      insert(reader, container);
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (Exception ex) {
        // intentionally empty
      }
    }
  }

  /**
   * Inserts swing objects rendered from an XML document into the given container.
   * <p/>
   * <pre>
   *  Differently to the parse methods, insert does NOT consider the root node of the XML document.
   * </pre>
   * <pre>
   *  <b>NOTE:</b><br>insert() does NOT clear() the idmap before rendering.
   * Therefore, if this SwingEngine's parser was used before, the idmap still
   * contains (key/value) pairs (id, JComponent obj. references).<br>
   * If insert() is NOT
   * used to insert in a previously (with this very SwingEngine) rendered UI,
   * it is highly recommended to clear the idmap:
   * <br>
   *  <div>
   *    <code>mySwingEngine.getIdMap().clear()</code>
   *  </div>
   * </pre>
   *
   * @param jdoc      <code>Document</code> xml-doc path info
   * @param container <code>Container</code> target, the swing obj, are added to
   * @throws Exception <code>Exception</code> exception thrown by the parser
   */
  public void insert(final Document jdoc, final T container)
          throws Exception {
    client = container;
    try {
      parser.parse(jdoc, container);
    } catch (Exception e) {
      if (SwingEngine.DEBUG_MODE)
        System.err.println(e);
      throw (e);
    }
    // reset components collection
    components = null;
    // initialize all client fields with UI components by their id
    mapMembers(client);
  }

  /**
   * Sets the SwingEngine's global resource bundle name, to be used by all SwingEngine instances. This name can be
   * overwritten however for a single instance, if a <code>bundle</code> attribute is places in the root tag of an XML
   * descriptor.
   *
   * @param bundlename <code>String</code> the resource bundle name.
   */
  public static void setResourceBundleName(String bundlename) {
    SwingEngine.default_resource_bundle_name = bundlename;
  }

  /**
   * Sets the SwingEngine's global locale, to be used by all SwingEngine instances. This locale can be overwritten
   * however for a single instance, if a <code>locale</code> attribute is places in the root tag of an XML descriptor.
   *
   * @param locale <code>Locale</code>
   */
  public static void setDefaultLocale(Locale locale) {
    SwingEngine.default_locale = locale;
  }

  /**
   * @return <code>Window</code> a parent for all dialogs.
   * 
   * use Application.getInstance(SwingApplication.class).getMainFrame()
   */
  public static Frame getAppFrame() {
	  SingleFrameApplication app = Application.getInstance(SingleFrameApplication.class);
	  if( app==null ) {
		  logger.warning("Application getInstance() has returned null!" );
		  return null;
	  }

	  return app.getMainFrame();
  }

  /**
   * Returns the object which instantiated this SwingEngine.
   *
   * @return <code>Objecy</code> SwingEngine client object
   *         <p/>
   *         <p><b>Note:</b><br>
   *         This is the object used through introspection the actions and fileds are set.
   *         </p>
   */
  public Object getClient() {
    return client;
  }

  /**
   * Returns an Iterator for all parsed GUI components.
   *
   * @return <code>Iterator</code> GUI components itearator
   */
  public Iterator<Component> getAllComponentItertor() {
    if (components == null) {
      traverse(client, components = new ArrayList<Component>());
    }
    return components.iterator();
  }

  /**
   * Returns an Iterator for id-ed parsed GUI components.
   *
   * @return <code>Iterator</code> GUI components itearator
   */
  public Iterator<?> getIdComponentItertor() {
    return idmap.values().iterator();
  }

  /**
   * Returns the id map, containing all id-ed parsed GUI components.
   *
   * @return <code>Map</code> GUI components map
   */
  public Map<String, Object> getIdMap() {
    return idmap;
  }

  /**
   * Removes all un-displayable compontents from the id map and deletes the components collection (for recreation at the
   * next request).
   * <p/>
   * <pre>
   *  A component is made undisplayable either when it is removed from a displayable containment hierarchy or when its
   * containment hierarchy is made undisplayable. A containment hierarchy is made undisplayable when its ancestor
   * window
   * is disposed.
   * </pre>
   *
   * @return <code>int</code> number of removed componentes.
   */
  public int cleanup() {
    List<Object> zombies = new ArrayList<Object>();
    Iterator<String> it = idmap.keySet().iterator();
    while (it != null && it.hasNext()) {
      String key = it.next();
      Object obj = idmap.get(key);
      if (obj instanceof Component && !((Component) obj).isDisplayable()) {
        zombies.add(key);
      }
    }
    for (int i = 0; i < zombies.size(); i++) {
      idmap.remove(zombies.get(i));
    }
    components = null;
    return zombies.size();
  }

  /**
   * Removes the id from the internal from the id map, to make the given id available for re-use.
   *
   * @param id <code>String</code> assigned name
   */
  public void forget(final String id) {
    idmap.remove(id);
  }

  /**
   * Returns the UI component with the given name or null.
   *
   * @param id <code>String</code> assigned name
   * @return <code>Component</code>- the GUI component with the given name or null if not found.
   */
  public Component find(final String id) {
    Object obj = idmap.get(id);
    if (obj != null && !Component.class.isAssignableFrom(obj.getClass())) {
      obj = null;
    }
    return (Component) obj;
  }

  /**
   * Sets the locale to be used during parsing / String conversion
   *
   * @param l <code>Locale</code>
   */
  public final void setLocale(Locale l) {
    if (SwingEngine.isMacOSXSupported() && SwingEngine.isMacOSX()) {
      l = new Locale(l.getLanguage(),
              l.getCountry(),
              SwingEngine.MAC_OSX_LOCALE_VARIANT);
    }
    this.localizer.setLocale(l);
  }

  /**
   * Sets the ResourceBundle to be used during parsing / String conversion
   *
   * @param bundlename <code>String</code>
   */
  public void setResourceBundle(String bundlename) {
    this.localizer.setResourceBundle(bundlename);
  }

  /**
   * @return <code>Localizer</code>- the Localizer, which is used for localization.
   */
  public Localizer getLocalizer() {
    return localizer;
  }

  /**
   * @return <code>TagLibrary</code>- the Taglibray to insert custom tags.
   *         <p/>
   *         <pre><b>Note:</b>ConverterLibrary and TagLibray need to be set up before rendering is called.
   *                                                                                                                                                                 </pre>
   */
  public TagLibrary getTaglib() {
    return taglib;
  }

  /**
   * Sets a classloader to be used for all <i>getResourse..()</i> and <i> loadClass()</i> calls. If no class loader is
   * set, the SwingEngine's loader is used.
   *
   * @param cl <code>ClassLoader</code>
   * @see ClassLoader#loadClass
   * @see ClassLoader#getResource
   */
  
  private void setClassLoader(ClassLoader cl) {
    this.cl = cl;
    this.localizer.setClassLoader(cl);
  }
  
  
  /**
   * @return <code>ClassLoader</code>- the Classloader used for all <i> getResourse..()</i> and <i>loadClass()</i>
   *         calls.
   */
  public ClassLoader getClassLoader() {
    return cl;
  }

  /**
   * Recursively Sets an ActionListener
   * <p/>
   * <pre>
   *  Backtracking algorithm: if al was set for a child component, its not being set for its parent
   * </pre>.
   *
   * @param c  <code>Component</code> start component
   * @param al <code>ActionListener</code>
   * @return <code>boolean</code> true, if ActionListener was set.
   */
  public boolean setActionListener(final Component c, final ActionListener al) {
    boolean b = false;
    if (c != null) {
      if (Container.class.isAssignableFrom(c.getClass())) {
        final Component[] s = ((Container) c).getComponents();
        for (Component value : s) {
          b = b | setActionListener(value, al);
        }
      }
      if (!b) {
        if (JMenu.class.isAssignableFrom(c.getClass())) {
          final JMenu m = (JMenu) c;
          final int k = m.getItemCount();
          for (int i = 0; i < k; i++) {
            b = b | setActionListener(m.getItem(i), al);
          }
        } else if (AbstractButton.class.isAssignableFrom(c.getClass())) {
          ((AbstractButton) c).addActionListener(al);
          b = true;
        }
      }

    }
    return b;
  }

  /**
   * Walks the whole tree to add all components into the <code>components<code> collection.
   *
   * @param c <code> Component</code> recursive start component.
   *          <p/>
   *          Note:There is another collection available that only tracks
   *          those object that were provided with an <em>id</em>attribute, which hold an unique id
   *          </p>
   * @return <code>Iterator</code> to walk all components, not just the id components.
   */
  public Iterator<Component> getDescendants(final Component c) {
    List<Component> list = new ArrayList<Component>(12);
    SwingEngine.traverse(c, list);
    return list.iterator();
  }

  protected void mapMember( Object widget, String fieldName) {
	  if( client==null) {
              if(isDesignTime()) return;
              throw new IllegalStateException("client obj is null!");
          }

	  final Class<?> cls = client.getClass();
  
	  mapMember( widget, fieldName, cls );
  }  
  
  /**
   * @param widget
   * @param field
   * @param obj
   * @param cls
   */
  protected void mapMember( Object widget, String fieldName, Class <?> cls) {
	// TODO Issue 44
	if( cls==null) return;
	
	if( widget==null) throw new IllegalArgumentException("parameter widget is null!");
	if( fieldName==null) throw new IllegalArgumentException("parameter fieldName is null!");
	if( client==null) throw new IllegalStateException("client obj is null!");

	boolean fullaccess = true;
	
	Field field = null;
	
	try{
		try {
			field = cls.getDeclaredField(fieldName);
		} catch (AccessControlException e) {
			fullaccess = false; // applet or otherwise restricted environment
			field = cls.getField(fieldName);
		}
	} catch (NoSuchFieldException e) {
		logger.fine( String.format("field [%s] in class [%s] doesn't exist! Ignored", fieldName, cls.getName()));	

		// Since getDeclaredFields() only works on the class itself, not the super class,
		// we need to make this recursive down to the object.class
		if (fullaccess) {
			// only if we have access to the declared fields do we need to visit the whole tree.
			mapMember(widget, fieldName, cls.getSuperclass());
		}
		return;
	}

	// field and object type need to be compatible and field must not be declared Transient
	if (field.getType().isAssignableFrom(widget.getClass()) && !Modifier.isTransient(field.getModifiers())) {
		try {
		
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			field.set(client, widget);
			field.setAccessible(accessible);

			logger.info( String.format("field [%s] mapped in class [%s]", fieldName, cls.getName()));	
	
		} catch (AccessControlException e) {
			try {
				fullaccess = false;
				field.set(client, widget);

				logger.info( String.format("field [%s] mapped in class [%s]", fieldName, cls.getName()));	
			
			} catch (IllegalAccessException e1) {
				logger.severe( "illegal access exception on set field!");
			}
		} catch (IllegalArgumentException e) {
		  // intentionally empty
		} catch (IllegalAccessException e) {
		  // intentionally empty
		}
	}

}
	  
  

  /**
   * Introspects the given object's class and initializes its non-transient fields with objects that have been instanced
   * during parsing. Mappping happens based on type and field name: the fields name has to be equal to the tag id,
   * psecified in the XML descriptor. The fields class has to be assignable (equals or super class..) from the class
   * that was used to instance the tag.
   *
   * @param obj <code>Object</code> target object to be mapped with instanced tags
   */
  protected void mapMembers(Object obj) {
    if (obj != null) {
      mapMembers(obj, obj.getClass());
    }
  }

  private void mapMembers(Object obj, Class<?> cls) {
    boolean fullaccess = true;

    if (obj != null && cls != null && !Object.class.equals(cls)) {
      Field[] flds = null;
      try {
        flds = cls.getDeclaredFields();
      } catch (AccessControlException e) {
        fullaccess = false; // applet or otherwise restricted environment
        flds = cls.getFields();
      }
      //
      // loops through class' declared fields and try to find a matching widget.
      //
      for (int i = 0; i < flds.length; i++) {
        Object widget = idmap.get(flds[i].getName());
        if (widget != null) {
          // field and object type need to be compatible and field must not be declared Transient
          if (flds[i].getType().isAssignableFrom(widget.getClass()) && !Modifier.isTransient(flds[i].getModifiers())) {
            try {

              boolean accessible = flds[i].isAccessible();
              flds[i].setAccessible(true);
              flds[i].set(obj, widget);
              flds[i].setAccessible(accessible);
            } catch (AccessControlException e) {
              try {
                fullaccess = false;
                flds[i].set(obj, widget);
              } catch (IllegalAccessException e1) {
              }

            } catch (IllegalArgumentException e) {
              // intentionally empty
            } catch (IllegalAccessException e) {
              // intentionally empty
            }
          }
        }

        //
        //  If an intended mapping didn't work out the objects member would remain un-initialized.
        //  To prevent this, we try to instantiate with a default ctor.
        //
        if (flds[i] == null) {
          if (!SwingEngine.DEBUG_MODE) {
            try {
              flds[i].set(obj, flds[i].getType().newInstance());
            } catch (IllegalArgumentException e) {
              // intentionally empty
            } catch (IllegalAccessException e) {
              // intentionally empty
            } catch (InstantiationException e) {
              // intentionally empty
            }
          } else { // SwingEngine.DEBUG_MODE)
            System.err.println(flds[i].getType()
                    + " : "
                    + flds[i].getName()
                    + SwingEngine.MAPPING_ERROR_MSG);
          }
        }
      }

      // Since getDeclaredFields() only works on the class itself, not the super class,
      // we need to make this recursive down to the object.class
      if (fullaccess) {
        // only if we have access to the declared fields do we need to visit the whole tree.
        mapMembers(obj, cls.getSuperclass());
      }
    }
  }

  /**
   * 
   */
  public static interface Predicate {
  
      public boolean evaluate( javax.swing.JComponent c );
  }
  
  /**
   * Walks the whole tree and evaluate each JComponent using predicate.
   *
   * @param container          <code>container</code> recursive start component.
   * @param predicate          evaluate component. Return false avoid to navigate it
   * 
   */
  public static void traverse(final java.awt.Container container, final Predicate predicate) {
      if( container==null ) return;
      if( predicate==null ) return;
      
      for (int i = 0; i < container.getComponentCount(); ++i) {

          final java.awt.Component c = container.getComponent(i);

          if (!(c instanceof javax.swing.JComponent)) continue;

          javax.swing.JComponent cc = (javax.swing.JComponent) c;

          if( predicate.evaluate(cc) ) traverse(cc, predicate);
      }
  }
  
  /**
   * Walks the whole tree to add all components into the <code>components<code> collection.
   *
   * @param c          <code>Component</code> recursive start component.
   * @param collection <code>Collection</code> target collection.
   *                   <p/>
   *                   Note:There is another collection available that only tracks
   *                   those object that were provided with an <em>id</em>attribute, which hold an unique id
   *                   </p>
   */
  protected static void traverse(final Component c, Collection<Component> collection) {
    if (c != null) {
      collection.add(c);
      if (c instanceof JMenu) {
        final JMenu m = (JMenu) c;
        final int k = m.getItemCount();
        for (int i = 0; i < k; i++) {
          traverse(m.getItem(i), collection);
        }
      } else if (c instanceof Container) {
        final Component[] s = ((Container) c).getComponents();
        for (Component value : s) {
          traverse(value, collection);
        }
      }
    }
  }

  /**
   * Enables or disables support of Mac OS X GUIs
   *
   * @param osx <code>boolean</code>
   */
  public static void setMacOSXSuport(boolean osx) {
    SwingEngine.MAC_OSX_SUPPORTED = osx;
  }

  /**
   * Indicates state of Mac OS X support (default is true = ON).
   *
   * @return <code>boolean</code>- indicating MacOS support is enabled
   */
  public static boolean isMacOSXSupported() {
    return SwingEngine.MAC_OSX_SUPPORTED;
  }

  /**
   * Indicates if currently running on Mac OS X
   * 
   * use Application.getInstance().isMacOSX();
   *
   * @return <code>boolean</code>- indicating if currently running on a MAC
   */
  @Deprecated
  public static boolean isMacOSX() {
    return Application.getInstance().isMacOSX();
  }

  /**
   * Displays the GUI during a RAD session. If the root component is neither a JFrame nor a JDialog, the a JFrame is
   * instantiated and the root is added into the new frames contentpane.
   */
  public void test() {
    WindowListener wl = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        System.exit(0);
      }
    };
    if (client != null) {
      if (JFrame.class.isAssignableFrom(client.getClass())
              || JDialog.class.isAssignableFrom(client.getClass())) {
        ((Window) client).addWindowListener(wl);
        client.setVisible(true);
      } else {
        JFrame jf = new JFrame("SwiXml Test");
        jf.getContentPane().add(client);
        jf.pack();
        jf.addWindowListener(wl);
        jf.setVisible(true);
      }
    }
  }
}
