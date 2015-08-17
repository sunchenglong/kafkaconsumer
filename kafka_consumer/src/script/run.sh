#!/bin/bash
set -x
path="/letv/upload"
jarfile=$path/shell/kafkaconsumer.jar
data=$path/data
datefmt=`date -d yesterday +%Y%m%d`
if [ $# == 1 ];then
datefmt=$1
fi
rawfile=$data/$datefmt.raw
output=$data/$datefmt.result
json=$data/$datefmt.json
#logfile=$data/$datefmt.log
##DEBUG--
#echo $jarfile
#echo $rawfile
#echo $datefmt
#echo $output
##DEBUG--
java -jar $jarfile $rawfile $datefmt $output
#Get Json File
sh getJson.sh $datefmt $output $json
