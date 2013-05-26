
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

jar cvfe ../server.jar gen.MainEntry gen/* rmi/* NetworkConfig.xml ServerConfig.xml
jar cvfe ../barrier.jar rmi.BarrierExample gen/* rmi/* NetworkConfig.xml ServerConfig.xml
cd ..
