#!/bin/bash

# this scrip will shutdown the future server

if [ "$1"X = "-hX" -o "$1"X = "-helpX" ]; then
   echo "Usage: $0 [-force]"
   exit 0 
fi
dir=`dirname $0`
sh $dir/future.sh stop $1

