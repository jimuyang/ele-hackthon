#!/bin/sh
dir=`echo $Tag | awk -F'_' '{print "/data/log/" $1 "_" $2}'`
mkdir -p "$dir"
logfile="${dir}/${Tag}.log"

exec > "$logfile"
exec 2>&1

echo $Tag

cd /data
java -cp .:pacman-1.0-SNAPSHOT-jar-with-dependencies.jar me.ele.hackathon.pacman.App config map1.txt http://pacman:1080 http://ghost:1080 "http://dummy:1080/" "$Tag"
