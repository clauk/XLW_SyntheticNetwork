./gen-jar.sh
echo "finish gen jar"
ps aux |grep java | grep barrier|awk '{print $2}'|xargs kill -9
rm results
rm log*

java -jar barrier.jar 3 & # 3 means number of servers in the system
sleep 2
echo "barrier started"

JAR_PATH=/home/gel016/proj/XLW_SyntheticNetwork # change accordingly
# don't need scp, cause home dir is sync in all sysnet servers
ssh gel016@sysnet76.sysnet.ucsd.edu "cd $JAR_PATH; java -jar $JAR_PATH/server.jar 0 0 > log0 2>&1" &
ssh gel016@sysnet86.sysnet.ucsd.edu "cd $JAR_PATH; java -jar $JAR_PATH/server.jar 1 100 > log1 2>&1" & 
ssh gel016@sysnet89.sysnet.ucsd.edu "cd $JAR_PATH; java -jar $JAR_PATH/server.jar 2 200 > log2 2>&1" &
echo "finish invoking servers"

