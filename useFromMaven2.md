# FROM NOW THESE ARTIFACTS ARE AVAILABLE FROM SONATYPE MAVEN REPO (see [Issue 68](https://code.google.com/p/swixml2/issues/detail?id=68) ) #

## Getting the Library ##

Getting the Library is simple. You just need to add a new Repository to your POM. An example, using the repository from the swixml2 project (a good one to start with ):

### Available Repository ###

```
<repositories>
        <repository>
          <id>sonatype-repository</id>
          <url>https://oss.sonatype.org/content/groups/public</url>
	  <snapshots>
		<enabled>false</enabled>
          </snapshots>
        </repository>

        <repository>
          <id>sonatype-repository</id>
          <url>https://oss.sonatype.org/content/repositories/snapshots</url>
	  <releases>
		<enabled>false</enabled>
	  </releases>
        </repository>

</repositories>
```

## import dependency ##

To use swixml2 you have to include in **dependencies** section of your POM the following declaration:

```
    <dependency>
      <groupId>org.swixml</groupId>
      <artifactId>swixml</artifactId>
      <version>2.6.20111005</version>
    </dependency>

```

If you want stay tuned on each update, use the SNAPSHOT version as shown below

```

    <dependency>
      <groupId>org.swixml</groupId>
      <artifactId>swixml</artifactId>
      <version>2.6-SNAPSHOT</version>
    </dependency>

```