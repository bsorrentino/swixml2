## Getting the Library ##

Getting the Library is simple. You just need to add a new Repository to your POM. An example, using the repository from the swixml2 project (a good one to start with ):

```
<repositories>
        <repository>
          <id>google-swixml2</id>
          <url>http://swixml2.googlecode.com/svn/mavenrepo</url>
        </repository>
</repositories>
```

## import dependency ##

To use swixml2 you have to include in **dependencies** section of your POM the following declaration:

```
    <dependency>
      <groupId>org.swixml</groupId>
      <artifactId>swixml</artifactId>
      <version>2.5.20090630</version>
    </dependency>

```

If you want stay tuned on each update, use the SNAPSHOT version as shown below

```
    <dependency>
      <groupId>org.swixml</groupId>
      <artifactId>swixml</artifactId>
      <version>2.5-SNAPSHOT</version>
    </dependency>

```