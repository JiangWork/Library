#!/bin/bash

# this scrip will start the future server
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
SERVERCONFIG=$appwhere/config/server
CLIENTCONFIG=$appwhere/config/client
JAVA_OPTS="-Xms20m -Xmx4g"

for jarFile in $libs
do
    CLASSPATH=$CLASSPATH\:$jarFile
done

if [ "$1" = "start" ]; then
    shift
    PORT=32100
    if [ "$1" = "-port" ]; then
        PORT=$2
    fi
    if ! [[ $PORT =~ '^[0-9]+$' ]]; then
         echo "invalid port: $PORT"
         exit 1
    fi
    exec /app/java/bin/java $JAVA_OPTS -DAPP=FUTURESERVICE -DPID=$pid -DAPPLOCATION=$appwhere -cp $CLASSPATH\:$SERVERCONFIG com.klatencor.klara.future.server.Server $PORT

elif [ "$1" = "stop" ]; then
    shift
    FORCE=0
    if [ "$1" = "-force" ]; then
       FORCE=1
       shift
    fi
    SHUTDOWNFILE=$appwhere/tmp/.shutdown
    rm $SHUTDOWNFILE 1> /dev/null 2>&1
    /app/java/bin/java  -cp $CLASSPATH\:$CLIENTCONFIG  com.klatencor.klara.future.server.Client stop $FORCE "@"
    sleep 0.2s
    if [ ! -s $SHUTDOWNFILE ]; then
        FUTUREPID=`ps -ef | grep FUTURESERVICE | grep -v grep | awk '{print $2}'`
    else
   	FUTUREPID=`cat $SHUTDOWNFILE 2>/dev/null | head -n 1`
    fi
    if [ -z $FUTUREPID ]; then
        echo "Future server is not running"
        exit
    fi 
    ps -p $FUTUREPID 1>/dev/null 2>&1
    if [ $? -eq 0 ]; then
    # server is still running
        cat $SHUTDOWNFILE | tail -n 1
    else 
        echo "You can use startup.sh to start server again."
    fi

elif [ "$1" = "util" ]; then
    shift
    /app/java/bin/java -cp $CLASSPATH\:$CLIENTCONFIG  com.klatencor.klara.future.server.Client "$@"

else
    echo "Usage: $0 (commands)"
    echo "start (-port 10000)  start the server with default port 32100 or use -port the specify a port."
    echo "stop (-force)        stop the server (by force or not)"
    echo "util                 run the util functions"
fi


