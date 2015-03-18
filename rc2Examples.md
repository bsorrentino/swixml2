## EXAMPLES ##

### Use tree2 tag ###

**XML**
```
<scrollpane>
 <tree2 model="${myTreeModel}" action="selectNode"/>
</scrollpane>
```

**Java Code**
```

private TreeModel myTreeModel = new MyTreeModel();

/**
* this property will be used to set Tree Model
*
*/
public TreeModel getMyTreeModel() {
  return myTreeModel
}

/**
* This action catch the tree node selection
*
*/
@Action
public void selectNode( ActionEvent e ) {
    
 TreeSelectionEvent ev = (TreeSelectionEvent) e.getSource();
    
 System.out.printf( "selectNode path=%s\n", ev.getPath().toString());
    
}

```


### Use table2 tag ###

**XML**
```
        <scrollpane>
        <table2 id="testTable" action="selectRow" cellSelectionEnabled="true" bindWith="${myData}" bindClass="${myDataClass}" />
        </scrollpane>
```

**Java Code**
```

/**
* This action catch both row and cell selection
*
*/
@Action
public void selectRow( ActionEvent e ) {
    
 ListSelectionEvent ev = (ListSelectionEvent) e.getSource();
    
 System.out.printf( "selectRow firstIndex=%d lastIndex=%d valueIsAdjusting=%b\n",  ev.getFirstIndex(), ev.getLastIndex(), ev.getValueIsAdjusting());
    
}


/* List Must Be Observable so that any change will be sync with table and vice-versa */ 
List<SimpleBean> myData = ObservableCollections.observableList( new ArrayList<SimpleBean>() );
  

/**
* return class of bean bound to table
*/
public Class<?> getMyDataClass() {
      return SimpleBean.class;
}

/**
*
* return list of bean managed by table
*/
public List<SimpleBean> getMyData() {
        return myData;
}


```

#### SimpleBean.java ####

```
public class SimpleBean {
    private String name;
    private int age;

    public SimpleBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return new StringBuilder().append( "name=").append(name).append("age=").append(age).toString();
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
}
```

#### SimpleBeanBeanInfo ####

```
public class SimpleBeanBeanInfo extends SimpleBeanInfo {

    // Bean descriptor                          
    /* lazy BeanDescriptor */
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor( SimpleBean.class , null ); 
        return beanDescriptor;     
    }                         

    // Property identifiers                      
    private static final int PROPERTY_age = 0;
    private static final int PROPERTY_name = 1;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[2];
    
        try {
            properties[PROPERTY_age] = new PropertyDescriptor ( "age", SimpleBean.class, "getAge", "setAge" ); 
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", SimpleBean.class, "getName", "setName" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }                          
    // Here you can add code for customizing the properties array.
    // column.index set the column's order
    // column.editable that the editable property

        properties[PROPERTY_age].setValue("column.index", 2); 
        properties[PROPERTY_name].setValue("column.index", 1);
        properties[PROPERTY_name].setValue("column.editable", true);

        return properties;     
    }                     


    private static final int defaultPropertyIndex = -1;               
    private static final int defaultEventIndex = -1;             

    public BeanDescriptor getBeanDescriptor() {
	return getBdescriptor();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
	return getPdescriptor();
    }

}
```