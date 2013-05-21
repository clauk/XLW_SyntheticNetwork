package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends UnicastRemoteObject implements IServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1164375003981783962L;

	
	
	protected ServerImpl() throws RemoteException {
		super();
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
		System.out.println("get record: " + cursorPos + " " + len);
		
		Record[] records = new Record[2];  
		
		Record record = new Record();
		record.userid = 1234333;
		record.attributes = new boolean[8];
		record.attributes[2] = true;
		record.attributes[7] = true;
		
		Record record2 = new Record();
		record2.userid = 2222222;
		record2.attributes = new boolean[8];
		record2.attributes[4] = true;
		record2.attributes[6] = true;
		
		records[0] = record;
		records[1] = record2;
		return records;
		
	}

	

}
