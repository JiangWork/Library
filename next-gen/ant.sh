#!/bin/bash

# 2016/7/5: first version by jiazhao

# Using jdk 1.6 to complie future module to aviod the compile
# errors when invoked by the application build script.
# The errors are: javax.annotation package is missing and
# generated codes error.
# This script will temporary change the JDK to 1.6.
# However, this module is still complied to level 1.5 designated by
# the build.xml.

JAVA_HOME=/usr/java
export JAVA_HOME
ant "$@"
