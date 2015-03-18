## NOTE ##


## Create a new SWIXML2 project using online archetype ##


### What is an archetypes ###

The Archetype allows the user to create a Maven 2 project from an existing template called an archetype. Archetypes could be included within **archetypes catalog** that could be **local** or **remote**

### Create project from Maven in batch mode ###

```
> mvn archetype:generate -B \
        -DarchetypeRepository=https://oss.sonatype.org/content/repositories/snapshots \
        -DarchetypeGroupId=org.swixml \
        -DarchetypeArtifactId=swixml-quickstart-archetype \
        -DarchetypeVersion=2.6-SNAPSHOT \
        -DgroupId=com.company \
        -DartifactId=project \
        -Dversion=1.0-SNAPSHOT \
        -Dpackage=com.company.swixml
```

### Create project from Maven in interactive mode ###

```
> mvn archetype:generate \
        -DarchetypeRepository=https://oss.sonatype.org/content/repositories/snapshots \
        -DarchetypeGroupId=org.swixml \
        -DarchetypeArtifactId=swixml-quickstart-archetype \
        -DarchetypeVersion=2.6-SNAPSHOT
```

### Run generated application ###

```
> mvn package exec:java
```


