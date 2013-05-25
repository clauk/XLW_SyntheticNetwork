package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BServer extends Remote {
	/**
	 * 
	 * @param serverID 	ID of the server
	 * @param label
	 * @return int	1-could go	0-need to wait
	 * @throws RemoteException
	 */
	public int CheckBarrierStatus(int serverID, String label) throws RemoteException;

	public void putResults(Result[] results) throws RemoteException;
	
}
