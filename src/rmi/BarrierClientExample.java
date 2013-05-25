package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class BarrierClientExample {

	public static void main(String[] args) {
		try {
			BServer server = (BServer) Naming.lookup("//127.0.0.1:1099/BServer");

			/* Set serverID with argument */
			int serverID;
			if (args.length > 0)
				serverID = Integer.parseInt(args[0]);
			else
				serverID = 1;

			while (true) {
				System.out.println("into loop");
				try 
				{ 
					Thread.currentThread().sleep(2000); 
				} 
				catch(Exception e){
					System.out.println("Sleep Exception");
				}

				int result = server.CheckBarrierStatus(serverID, "test label");
				if (result == 1) {
					System.out.println("GOGOGO");
					break;
				}
				else
					System.out.println("Client #" + String.valueOf(serverID) + " is Waiting...");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void BarrierClientListen(String barrierServer, int serverID, String label){
		try {
			BServer server = (BServer) Naming.lookup(barrierServer);

			/* Set serverID with argument */
			while (true) {
				//System.out.println("into loop");
				try 
				{ 
					Thread.currentThread().sleep(2000); 
				} 
				catch(Exception e){
					//System.out.println("Sleep Exception");
				}

				int result = server.CheckBarrierStatus(serverID, label);
				if (result == 1) {
					//System.out.println("GOGOGO");
					break;
				}
				//else
					//System.out.println("Client #" + String.valueOf(serverID) + " is Waiting...");
				
			}
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putResult(String barrierServer, LinkedList<Record> records, Map<Long, LinkedList<Long>> edgeListMap, int blockSize){
		BServer server;
		try {
			server = (BServer) Naming.lookup(barrierServer);
			Result [] results = new Result[blockSize];
			int remainLen = records.size();
			ListIterator<Record> cursorIterator = records.listIterator();
			while(remainLen > 0){
				if(remainLen >= blockSize){
					for(int i=0; i<blockSize; i++){
						Record node = cursorIterator.next();
						results[i] = new Result( node, edgeListMap.get(node.userid));
					}
					server.putResults(results);
					remainLen -= blockSize;
				}
				else if(remainLen > 0){
					results = new Result[remainLen];
					for(int i=0; i<remainLen; i++){
						Record node = cursorIterator.next();
						results[i] = new Result( node, edgeListMap.get(node.userid));
					}
					server.putResults(results);
					remainLen = 0;
				}
			}			
						
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void finishPutResult(String barrierServer, int serverID){
		BServer server;
		try {
			server = (BServer) Naming.lookup(barrierServer);
			server.CheckBarrierStatus(serverID, Constants.FINISH_LABEL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
