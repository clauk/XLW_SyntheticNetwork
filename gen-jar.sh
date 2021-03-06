
# compile
rm -rf bin
mkdir bin

javac -d bin src/gen/*.java src/rmi/*.java
cp NetworkConfig.xml bin/NetworkConfig.xml
cp ServerConfig.xml bin/ServerConfig.xml


# gen jar

cd bin

if [ -f "../server.jar" ]
then 
rm ../server.jar
fi

if [ -f "../barrier.jar" ]
then 
rm ../barrier.jar
fi

rmic rmi.NetworkServerImpl
rmic rmi.BarrierImpl

jar cfe ../server.jar gen.MainEntry gen/* rmi/* NetworkConfig.xml ServerConfig.xml
jar cfe ../barrier.jar rmi.BarrierExample gen/* rmi/* NetworkConfig.xml ServerConfig.xml
cd ..
