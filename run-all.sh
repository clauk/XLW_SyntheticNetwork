./gen-jar.sh
ps aux | grep rmire|grep -v grep|awk '{print $2}'|xargs kill -9
rmiregistry 1097 &

java -jar barrier.jar 3 & # listen at localhost:1099/BServer, 3 means number of servers in the system
java -jar server.jar 0 0 & # args [0] server id(0-based) [1]: start record id  
java -jar server.jar 1 100 &
java -jar server.jar 2 200 &
