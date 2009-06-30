
JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home
export JAVA_HOME
PATH=/opt/subversion/bin:$PATH
export PATH
/Applications/apache-maven-2.0.10/bin/mvn -e --batch-mode -Dtag=swixml-2.5.20090630 release:prepare -DreleaseVersion=2.5.20090630 -DdevelopmentVersion=2.5-SNAPSHOT -Dresume=false