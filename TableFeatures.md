# Introduction #

The  tag table from original SWIXML didn't allow possibility to describe the columns.
In SWIXML2 we have added the possibility to describe column using XML Rapresentation

An example of code could be checkout from svn : http://swixml2.googlecode.com/svn/trunk/examples the package is examples.table (use the last swixml2 SNAPSHOT)

## Table Examples ##

```
<scrollpane constraints="BorderLayout.CENTER">

 <table id="table2" action="selectRow" dblClickAction="activateRow2" cellSelectionEnabled="true" bindList="${myData2}" >

  <tableColumn bindWith="field2" type="java.lang.Boolean" maxWidth="20" resizable="false" headerValue="X"  editable="true"  />
  <tableColumn bindWith="field1" type="java.lang.Integer" headerValue="#" maxWidth="50"  resizable="false"  />
  <tableColumn bindWith="field3" preferredWidth="20"  editable="true" resizable="true"/>

 </table>
</scrollpane>

```

To have a complete live example click [here](http://swixml2.googlecode.com/svn/trunk/jnlp/swixml2.jnlp)

## Table Tag extended Attributes ##

|dblClickAction|allow to handle double click event|optional|
|:-------------|:---------------------------------|:-------|
|bindList|allow to bind a List to the table. We have to use the syntax **${**_property name_**}** | mandatory |

## TableColumn Tag  Attributes ##

|bindWith| name of the property belonging to bean contained in 'bindList' |mandatory|
|:-------|:---------------------------------------------------------------|:--------|
|type | java type. We have to indicate the FQN |optional (default 'java.lang.Object')|
|maxWidth| maximum column width|optional|
|minWidth| minimum column width|optional|
|preferredWidth| preferred column width |optional|
|resizable| set column as resizable |optional (default 'true')|
|headerValue| set column's caption |optional (default 'value of bindWith')|
|editable| set column as editable |optional (default 'false')|
|cellEditor | set a custom cell editor. We have to use the syntax **${**_property name_**}** |optional|
|cellRenderer  |set a custom cell renderer. We have to use the syntax **${**_property name_**}**|optional|
|headerRenderer  |set a custom header renderer editor. We have to use the syntax **${**_property name_**}**|optional|