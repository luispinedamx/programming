#!/bin/sh

HOSTS=`echo $LSB_HOSTS | /usr/bin/tr [:blank:] "\n" | /bin/sort -u -m`
echo "Hosts = $HOSTS"
echo $HOSTS > hostsList
CMD=`echo $1 $2 $3 $4 $5 $6 $7 $8 $9`
echo "command = $CMD"
nodesList=""

# node1
for i in $HOSTS
do
  lsgrun -p -m "$i" $CMD &
  nodesList="${nodesList} //$i/${i}Node1"
done
echo $nodesList > nodesList
echo local > localFileTag
sleep 5

# node2
for i in $HOSTS
do
  lsgrun -p -m "$i" $CMD &
done

# echo Copying files 
#copyFiles.sh 
     # 2> /dev/null
