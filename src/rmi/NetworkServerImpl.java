package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import gen.*;

public class NetworkServerImpl extends UnicastRemoteObject implements IServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1164375003981783962L;

	private NetworkGenerator _networkGenerator;
	
	protected NetworkServerImpl(NetworkGenerator _netGen) throws RemoteException {
		super();
		_networkGenerator = _netGen;
	}



	@Override
	public int put(Record[] records) throws RemoteException {
		for ( Record record : records) {
			System.out.println("Put record: " + record);
		}
		return 0;
	}



	@Override
	public Record[] get(int cursorPos, int len) throws RemoteException {
		Record[] records = new Record[len];
		ListIterator<Record> cursorIterator = _networkGenerator.getNodeResult().listIterator(cursorPos);
		for(int i=0; i<len; i++){
			records[i] = cursorIterator.next();
		}
		return records;		
	}

	

}
