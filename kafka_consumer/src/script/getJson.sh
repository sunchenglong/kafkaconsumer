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
jsonfile=$data/$datefmt.json
if [ $# == 3 ];then
datefmt=$1
output=$2
jsonfile=$3
fi
rawfile=$data/$datefmt.raw
output=$data/$datefmt.result
jsonfile=$data/$datefmt.json
#logfile=$data/$datefmt.log
##DEBUG--
#echo $jarfile
#echo $rawfile
#echo $datefmt
#echo $output
##DEBUG--
# Create Json File
line=`cat $output | awk -F"\t" '{
	if($2!=0&&$2!=-1&&$2!="div0"){
		print $0
		}
	}' | wc -l | awk '{$1-=1;print $1}'`
cat $output | awk -F"\t" '{
		if($2!=0&&$2!=-1&&$2!="div0"){
			print $0;
		}
	}' | awk -F"\t" 'BEGIN{count=0}{
    	if(count==0){
		printf ("[\{\"uploadid\":\"%s\",\"speed\":\"%s\",\"start\":\"%s\",\"end\":\"%s\",\"size\":\"%s\",\"clientip\":\"%s\",\"serverip\":\"%s\",\n",$1,$2,$3,$4,$5,$6,$7);
	}
	else if(count=='$line'){
		printf ("\{\"uploadid\":\"%s\",\"speed\":\"%s\",\"start\":\"%s\",\"end\":\"%s\",\"size\":\"%s\",\"clientip\":\"%s\",\"serverip\":\"%s\"\}]\n",$1,$2,$3,$4,$5,$6,$7);
	}else{
	printf ("\{\"uploadid\":\"%s\",\"speed\":\"%s\",\"start\":\"%s\",\"end\":\"%s\",\"size\":\"%s\",\"clientip\":\"%s\",\"serverip\":\"%s\"},\n",$1,$2,$3,$4,$5,$6,$7);
	}
	count+=1;
}' > $jsonfile
