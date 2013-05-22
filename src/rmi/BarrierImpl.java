package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BarrierImpl extends UnicastRemoteObject implements BServer {

	/**
	 * 
	 */
	private boolean[] status = new boolean[10];
	private int ServerNum;
	private int ServerGetBarrier;

	
	protected BarrierImpl() throws RemoteException {
		super();
	}
	
	public void SetServerNum(int n) throws RemoteException {
		ServerNum = n;
	}

	@Override
	public int CheckBarrierStatus(int serverID) throws RemoteException {
		System.out.println("Server #" + String.valueOf(serverID) + " check barrier status");
		if (status[serverID] == false) {
			ServerGetBarrier++;
			status[serverID] = true;
		}

		if (ServerGetBarrier == ServerNum)
			return 1;
		else
			return 0;
	}

}
