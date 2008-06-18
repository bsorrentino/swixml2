/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr295;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Property;

/**
 *
 * @author Sorrentino
 */
public class BeanRegistry {
    BindingGroup bindingGroup = new BindingGroup();


      java.util.Map<String,Object> beanMap;
      
      /**
       * factory method 
       * 
       * @return
       */
      public static synchronized BeanRegistry createInstance() {
            return new BeanRegistry();
      }
      
      private BeanRegistry() {
          beanMap = java.util.Collections.synchronizedMap(new java.util.HashMap<String,Object>());
      }
      
      public final void registerBean( String name, Object bean ) {
          
          beanMap.put( name, bean);
      }

      public final Object unregisterBean( String name ) {
          
          return beanMap.remove( name );
      }
      
      public final Object getBean( String name ) {
          
          return beanMap.get( name );
      }
      
      public final void startup() {
          bindingGroup.bind();
      }
      
      public final void addRWAutoBinding( String bean, String beanProperty, Object target, String targetProperty  ) {
          if( null==bean ) throw new IllegalArgumentException( "bean argument is null!");
          if( null==target ) throw new IllegalArgumentException( "target argument is null!");
          if( null==targetProperty ) throw new IllegalArgumentException( "targetProperty argument is null!");
          
          Object beanInstance = getBean( bean );
          
          if( null==beanInstance ) throw new IllegalArgumentException( "bean " + bean + " doesn't not exists!");
          
          Property tp = ( targetProperty.startsWith("$") ) 
                  ? ELProperty.create(targetProperty) 
                  : BeanProperty.create(targetProperty);

          AutoBinding binding = Bindings.createAutoBinding(
                  AutoBinding.UpdateStrategy.READ_WRITE, 
                  beanInstance, 
                  BeanProperty.create(beanProperty), 
                  target, 
                  tp);
          
          bindingGroup.addBinding(binding);

      }

      public final void addRAutoBinding( String bean, String beanProperty, Object target, String targetProperty  ) {
          if( null==bean ) throw new IllegalArgumentException( "bean argument is null!");
          if( null==target ) throw new IllegalArgumentException( "target argument is null!");
          if( null==targetProperty ) throw new IllegalArgumentException( "targetProperty argument is null!");
          
          Object beanInstance = getBean( bean );
          
          if( null==beanInstance ) throw new IllegalArgumentException( "bean " + bean + " doesn't not exists!");
          
          Property tp = ( targetProperty.startsWith("$") ) 
                  ? ELProperty.create(targetProperty) 
                  : BeanProperty.create(targetProperty);

          AutoBinding binding = Bindings.createAutoBinding(
                  AutoBinding.UpdateStrategy.READ,
                  beanInstance, 
                  BeanProperty.create(beanProperty), 
                  target, 
                  tp);
          
          bindingGroup.addBinding(binding);

      }
      
      public final void shutdown() {
          beanMap.clear();
          bindingGroup.unbind();
      }
      
      
  }

