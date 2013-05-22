package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BServer extends Remote {
	/**
	 * 
	 * @param serverID 	ID of the server
	 * @return int	1-could go	0-need to wait
	 * @throws RemoteException
	 */
	public int CheckBarrierStatus(int serverID) throws RemoteException;

	public void SetServerNum(int n) throws RemoteException;
;
}
