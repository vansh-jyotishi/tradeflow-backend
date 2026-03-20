#!/bin/sh
# Maven Wrapper script for Unix/macOS/Git Bash
# Downloads Maven if not present, then runs it

set -e

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

if [ -f "$MAVEN_WRAPPER_PROPERTIES" ]; then
    MAVEN_DIST_URL=$(grep 'distributionUrl' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2- | tr -d '\r')
fi

MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-3.9.9"
MVN_CMD="$MAVEN_HOME/bin/mvn"

if [ ! -f "$MVN_CMD" ]; then
    echo "Downloading Maven 3.9.9..."
    mkdir -p "$MAVEN_HOME"
    TMPFILE=$(mktemp)
    if command -v curl > /dev/null 2>&1; then
        curl -fsSL -o "$TMPFILE" "$MAVEN_DIST_URL"
    elif command -v wget > /dev/null 2>&1; then
        wget -q -O "$TMPFILE" "$MAVEN_DIST_URL"
    else
        echo "Error: curl or wget required to download Maven." >&2
        exit 1
    fi
    unzip -q -o "$TMPFILE" -d "$HOME/.m2/wrapper/dists/"
    rm -f "$TMPFILE"
    # Move contents up if nested
    if [ -d "$HOME/.m2/wrapper/dists/apache-maven-3.9.9" ]; then
        MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-3.9.9"
    else
        EXTRACTED=$(ls -d "$HOME/.m2/wrapper/dists/apache-maven-"* 2>/dev/null | head -1)
        if [ -n "$EXTRACTED" ]; then
            MAVEN_HOME="$EXTRACTED"
        fi
    fi
    MVN_CMD="$MAVEN_HOME/bin/mvn"
    echo "Maven downloaded to $MAVEN_HOME"
fi

exec "$MVN_CMD" \
    -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
    "$@"
