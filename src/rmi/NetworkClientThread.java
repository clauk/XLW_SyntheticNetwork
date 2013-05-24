package rmi;

import java.rmi.Naming;

import gen.NetworkGenerator;

public class NetworkClientThread  implements Runnable{
	private NetworkGenerator _networkGenerator;
	private String _serverAddress;
	
	public NetworkClientThread(NetworkGenerator networkGenerator, String serverAddress){
		_networkGenerator = networkGenerator;
		_serverAddress = serverAddress;
	}
	
	public void run() {
		try {
			NetworkClient networkClient = new NetworkClient(_serverAddress);
			
			int nodeNum = _networkGenerator.getNodeNum();
			int cursorPos = 0;
			int fetchLen = 1000;
			
			while(cursorPos <= nodeNum){
				Record [] records = networkClient.getRecord(cursorPos, fetchLen);
				_networkGenerator.generateEdges(records);
				cursorPos += fetchLen;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
