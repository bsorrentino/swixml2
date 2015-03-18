# Introduction #

Since the most important java  IDE provide the XML editor that has ability to give to developer an tag completion facilities based on XML Schema (or DTD), also swixml2 (like the swixml) would like to provide a online schema

There are two way to use schema

## OFFLINE MODE ##

Download the schema from download section, put it in the same folder of  XML source and write the follow declaration on the root tag

```

<dialog xmlns="http://www.swixml.org/2007/Swixml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.swixml.org/2007/Swixml swixml2-schema-2.6-SNAPSHOT.xsd ">
</dialog>

```

## ONLINE MODE ##

Open the XML source and write the follow declaration on the root tag
```

<dialog xmlns="http://www.swixml.org/2007/Swixml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.swixml.org/2007/Swixml https://bsc-documentation-repo.googlecode.com/svn/trunk/swixml2/swixml2-schema-2.6-SNAPSHOT.xsd ">

</dialog>

```