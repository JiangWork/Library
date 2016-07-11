#!/bin/bash

# this scrip will start the job server
# Note: the terminal will hang up while starting the server.

pid=$$
scriptwhere=`dirname $0`
cd $scriptwhere
scriptwhere=`pwd`
cd -  1>/dev/null 2>&1
appwhere=`dirname $scriptwhere`
libwhere=$appwhere/lib
libs=`find $libwhere -name "*.jar" -print`
CLASSPATH=""
SERVERCONFIG=$appwhere/conf/server
JAVA_EXEC=`which java`
JAVA_OPTS="-Xms20m -Xmx4g"


for jarFile in $libs
do
    CLASSPATH=$CLASSPATH\:$jarFile
done

exec $JAVA_EXEC $JAVA_OPTS -DloggingRoot=$appwhere/log -DAPP=JOBSERVICE -DPID=$pid -DAPPLOCATION=$appwhere -cp $CLASSPATH\:$SERVERCONFIG org.smartframework.jobhub.server.JobServer