#
#
#

set LOCAL_REPO=C:/"Documents and Settings"/sorrentino/.m2/repository
set REMOTE_REPO=svn:https://swixml2.googlecode.com/svn/trunk/mavenrepo

mvn -e deploy:deploy-file -Durl=%REMOTE_REPO% -DrepositoryId=google-project -DgroupId=com.applet.eawt -DartifactId=ui -Dversion=swixml-bundled -Dpackaging=jar -Dfile=%LOCAL_REPO%/com/applet/eawt/ui/swixml-bundled/ui-swixml-bundled.jar 
mvn -e deploy:deploy-file -Durl=%REMOTE_REPO% -DrepositoryId=google-project -DgroupId=org.jdesktop -DartifactId=jsr296 -Dversion=1.0 -Dpackaging=jar -Dfile=%LOCAL_REPO%/org/jdesktop/jsr296/1.0/jsr296-1.0.jar 
mvn -e deploy:deploy-file -Durl=%REMOTE_REPO% -DrepositoryId=google-project -DgroupId=org.jdesktop -DartifactId=jsr295 -Dversion=1.2.1 -Dpackaging=jar -Dfile=%LOCAL_REPO%/org/jdesktop/jsr295/1.2.1/jsr295-1.2.1.jar 
mvn -e deploy:deploy-file -Durl=%REMOTE_REPO% -DrepositoryId=google-project -DgroupId=javax.jnlp -DartifactId=jnlp -Dversion=1.6 -Dpackaging=jar -Dfile=%LOCAL_REPO%/javax/jnlp/jnlp/1.6/jnlp-1.6.jar 
