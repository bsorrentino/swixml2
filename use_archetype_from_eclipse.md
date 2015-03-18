## NOTE ##
This article imply that , at least, you have already installed eclipse at least 3.4.0 and the [Maven Plugin](http://m2eclipse.sonatype.org/update/)


## Create a new SWIXML2 project using online archetype ##

### What is an archetypes ###

The Archetype allows the user to create a Maven 2 project from an existing template called an archetype. Archetypes could be included within **archetypes catalog** that could be **local** or **remote** (for futher information see [Maven Archetype Plugin](http://maven.apache.org/plugins/maven-archetype-plugin/).
In this article we focused on local archetypes repository that is possible to use from eclipse ( dialog accesible from menu **Windows/Preferences** )

### Create a local Archetypes catalog ###

To create a Local Archetypes catalog you have simply add the **archetype-catalog.xml** under the folder **~/.m2/** (i.e. local maven repository ).
A catalog is an xml file with such content:

```
<?xml version="1.0" encoding="UTF-8"?>
<archetype-catalog>
<archetypes>
<archetype>
[1] <groupId>given_group_id</groupId>
[2] <artifactId>given_artifact_id</artifactId>
[3] <version>the_version</version>
[4] <repository>http://remote_maven_repo_host_name/remote_maven_repo_folder</repository>
[5] <description>the_description</description>
</archetype>
...
</archetypes>
</archetype-catalog>
```

  1. The groupId of the archetype. REQUIRED
  1. The artifactId of the archetype. REQUIRED
  1. The version of the archetype. RELEASE is a valid version. REQUIRED
  1. The repository where to find the archetype. OPTIONAL . When ommitted, the archetype is searched for in the repository where the catalog comes from.
  1. The description of the archetype. OPTIONAL


### Add Maven SWIXML2 remote archetype to local repository ###

In order to add the Maven GWTEXT archetype we have to add within archetype catalog the content shown below

```
<?xml version="1.0" encoding="UTF-8"?><archetype-catalog>
<archetypes>
    <archetype>
      <groupId>org.swixml</groupId>
      <artifactId>swixml-quickstart-archetype</artifactId>
      <version>2.6-SNAPSHOT</version>
      <description>Project to define a maven swixml-quickstart archetype</description>
    </archetype>
</archetypes>
</archetype-catalog>
```

### Use new maven project wizard from eclipse ###

Once we have configured the SWIXML2 archetype in the local archetype catalog we are ready to create our first project integrated with maven. So let's run eclipse and select the menu **File/new/Maven Project** and we have to do the following steps

  * step1 - select target folder (don't select skip archetype)
  * step2 - select the gwt archetype
  * step3 - provide maven project's main infos and finalize

**Application generated consists in a frame that contains two panels managed trought a tab.**

|Panel1 - show several basic bindings with text field, label, combo & list moreover implement actions and state management (i.e. enabling/dsabling) |
|:--------------------------------------------------------------------------------------------------------------------------------------------------|
|http://swixml2.googlecode.com/svn/trunk/images/swixml-simpleapp.PNG|

|Panel2 - show complex bindings with tree and table |
|:--------------------------------------------------|
|http://swixml2.googlecode.com/svn/trunk/images/swixml-simpleapp1.PNG|