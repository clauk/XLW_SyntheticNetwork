package rmi;

import java.rmi.Naming;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

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
			int fetchLen = Constants.FETCH_RECORD_BLOCK_SIZE;
			
			while(cursorPos <= nodeNum){
				Record [] records = networkClient.getRecord(cursorPos, fetchLen);
				_networkGenerator.generateEdges(records);
				cursorPos += fetchLen;
			}
			
			
			//TODO
			//just for check, remove this
			Map<Long, LinkedList<Long>> edgeListMap = _networkGenerator.getEdgeResult();
			Map<Long, LinkedList<Long>> orderMap = new TreeMap<Long, LinkedList<Long>>();
			for (Map.Entry entry : edgeListMap.entrySet()) {
				orderMap.put((Long)entry.getKey(), (LinkedList<Long>)entry.getValue());
			}
			Iterator<Long> nodeIterator = orderMap.keySet().iterator();
			while (nodeIterator.hasNext()) {
				Long recordID = nodeIterator.next();
				LinkedList<Long> edges = edgeListMap.get(recordID);
			    System.out.print(recordID+": ");
			    Iterator<Long> edgeIterator = edges.iterator();
			    while(edgeIterator.hasNext()){
			    	System.out.print(edgeIterator.next()+", ");
			    }
			    System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
