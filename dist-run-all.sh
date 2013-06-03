./gen-jar.sh
echo "finish gen jar"
ps aux |grep java | grep barrier|awk '{print $2}'|xargs kill -9
rm results
rm log*

java -Xms512M -Xmx2G  -jar barrier.jar 4 > log_barrier 2>&1 & # 3 means number of servers in the system
sleep 2
echo "barrier started"

declare -i SERVER_NUM
declare -i NODE_NUM_PER_SERVER
declare -i START_SERVER_IP

SERVER_NUM=4
NODE_NUM_PER_SERVER=16384
START_SERVER_IP=11

JAR_PATH=/home/chen/XLW_SyntheticNetwork # change accordingly
# don't need scp, cause home dir is sync in all sysnet servers
for ((SERVER_ID=0;SERVER_ID<$SERVER_NUM; SERVER_ID++))
  do
    declare -i NODE_START_ID
    NODE_START_ID=NODE_NUM_PER_SERVER\*SERVER_ID
    declare -i SERVER_IP
    SERVER_IP=START_SERVER_IP+SERVER_ID
    LOG=log$SERVER_ID
    echo "server ip: $SERVER_IP  start node id: $NODE_START_ID  log: $LOG"
    ssh chen@compute-0-$SERVER_IP.local "ps aux|grep java |grep server.jar | awk '{print $2}'|xargs kill -9;"
    ssh chen@compute-0-$SERVER_IP.local " cd $JAR_PATH; echo `hostname -a`; java -Xms512M -Xmx2G -jar $JAR_PATH/server.jar $SERVER_ID $NODE_START_ID > $LOG 2>&1 &" &
  done
echo "finish invoking servers"
