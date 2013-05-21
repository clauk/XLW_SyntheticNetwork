package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {

	/**
	 * 
	 * @param records
	 * @return actual # of records received
	 * @throws RemoteException
	 */
	public int put(Record[] records) throws RemoteException;
	

	/**
	 * 
	 * @param cursorPos 0-based index
	 * @param len # of records to return
	 * @return
	 * @throws RemoteException
	 */
	public Record[] get(int cursorPos, int len) throws RemoteException;
}
