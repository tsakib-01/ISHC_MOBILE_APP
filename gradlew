#!/usr/bin/env sh

# Resolve Gradle distribution
if [ -n "$GRADLE_HOME" ] ; then
    exec "$GRADLE_HOME/bin/gradle" "$@"
else
    exec gradle "$@"
fi
