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
			String label = null;
			if (args.length == 2) {
				serverID = Integer.parseInt(args[0]);
				label = args[1];
			}
			else {
				serverID = 1;
				label = "default";
			}

			while (true) {
				System.out.println("into loop");
				try 
				{ 
					Thread.currentThread().sleep(2000); 
				} 
				catch(Exception e){
					System.out.println("Sleep Exception");
				}

				int result = server.CheckBarrierStatus(serverID, label);
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
			long totalTime = 0, startTime, endTime;
			while(remainLen > 0){
				if(remainLen >= blockSize){
					for(int i=0; i<blockSize; i++){
						Record node = cursorIterator.next();
						results[i] = new Result( node, edgeListMap.get(node.userid));
					}
					startTime = System.currentTimeMillis();
					server.putResults(results);
					endTime = System.currentTimeMillis();
					totalTime += (endTime - startTime);
					remainLen -= blockSize;
				}
				else if(remainLen > 0){
					results = new Result[remainLen];
					for(int i=0; i<remainLen; i++){
						Record node = cursorIterator.next();
						results[i] = new Result( node, edgeListMap.get(node.userid));
					}
					startTime = System.currentTimeMillis();
					server.putResults(results);
					endTime = System.currentTimeMillis();
					totalTime += (endTime - startTime);
					remainLen = 0;
				}
			}
			System.out.println("Total Time on putResults: "+totalTime +" ms");
			System.out.println("Total Node Num: " +records.size());
			System.out.println("Pagesize: " +blockSize);
			System.out.println("Total putResult Operation Num: " +(records.size()+blockSize-1)/blockSize);			
						
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
