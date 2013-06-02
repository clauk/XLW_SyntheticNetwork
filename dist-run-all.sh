./gen-jar.sh
echo "finish gen jar"
ps aux |grep java | grep barrier|awk '{print $2}'|xargs kill -9
rm results
rm log*

java -jar barrier.jar 4 & # 3 means number of servers in the system
sleep 2
echo "barrier started"

#start=$(date +%s)

JAR_PATH=/home/chen/XLW/experiment/trick/SyntheticNetworkGen # change accordingly
# don't need scp, cause home dir is sync in all sysnet servers
ssh chen@compute-0-11.local "cd $JAR_PATH; java -Xms512M -Xmx2G -jar $JAR_PATH/server.jar 0 0 > log0 2>&1" &
ssh chen@compute-0-12.local "cd $JAR_PATH; java -Xms512M -Xmx2G -jar $JAR_PATH/server.jar 1 16384 > log1 2>&1" &
ssh chen@compute-0-13.local "cd $JAR_PATH; java -Xms512M -Xmx2G -jar $JAR_PATH/server.jar 2 32768 > log2 2>&1" &
ssh chen@compute-0-14.local "cd $JAR_PATH; java -Xms512M -Xmx2G -jar $JAR_PATH/server.jar 3 49152 > log3 2>&1" &
echo "finish invoking servers"

#end=$(date +%s)  
#time=$(( $end - $start ))  
#echo "Run time: " $time  

