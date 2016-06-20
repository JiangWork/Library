#!/bin/bash

# this scrip will start the future server
dir=`dirname $0`

sh $dir/future.sh start -port 32100 &

