#!/bin/sh
#
# Gradle start up script for UN*X
#
##############################################################################
#  DEFAULTS
##############################################################################
DEFAULT_JVM_OPTS="-Xmx64m"
APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")
cygwin=false
msys=false
darwin=false
nonstop=false
case "$(uname)" in
  CYGWIN* ) cygwin=true ;;
  Darwin* ) darwin=true ;;
  MINGW* ) msys=true ;;
  MSYS* ) msys=true ;;
esac

##############################################################################
#  FIND JAVA
##############################################################################
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

##############################################################################
#  FIND GRADLE WRAPPER JAR
##############################################################################
WRAPPER_JAR="gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ] ; then
    die "ERROR: Could not find $WRAPPER_JAR"
fi

##############################################################################
#  EXECUTE GRADLE
##############################################################################
exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
  -Dorg.gradle.appname="$APP_BASE_NAME" \
  -classpath "$WRAPPER_JAR" \
  org.gradle.wrapper.GradleWrapperMain "$@"

# Helper function
die() {
    echo "$1" >&2
    exit 1
}