package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class NetworkClient {
	private IServer _server;
	
	public NetworkClient(String serverAddress){
		try {
			_server = (IServer) Naming.lookup(serverAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Record[] getRecord(int cursorPos, int length){
		try {
			return _server.get(cursorPos, length);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
