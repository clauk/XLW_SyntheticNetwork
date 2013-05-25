package rmi;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarrierImpl extends UnicastRemoteObject implements BServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7901818628605257664L;

	private static final int LEN = 20;
	
	private int ServerNum;
	
	private Map<String, Integer> ServerGetBarrierMap;
	private Map<String, boolean[]> statusMap;
	private List<Result> results;
	
	protected BarrierImpl() throws RemoteException {
		super();
		statusMap = new HashMap<String, boolean[]>();
		ServerGetBarrierMap = new HashMap<String, Integer>();
		results = new ArrayList<Result>();
	}
	
	public void SetServerNum(int n) {
		ServerNum = n;
	}

	@Override
	synchronized public int CheckBarrierStatus(int serverID, String label) throws RemoteException {
		System.out.println("Server #" + String.valueOf(serverID) + " check barrier status for label: " + label);
		if (statusMap.get(label) == null) {
			boolean[] status = new boolean[LEN];
			statusMap.put(label, status);
			ServerGetBarrierMap.put(label, 0);
		}
		boolean[] status = statusMap.get(label);
		Integer ServerGetBarrier = ServerGetBarrierMap.get(label);
		if (status[serverID] == false) {
			ServerGetBarrier++;
			ServerGetBarrierMap.put(label, ServerGetBarrier);
			status[serverID] = true;
		}

		if (ServerGetBarrier == ServerNum) {
			if (Constants.FINISH_LABEL.equals(label)) {
				// when finished, sort results
				try {
					onFinished();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return 1;
		}
			
		else {
			return 0;
		}
			
	}

	@Override
	synchronized public void putResults(Result[] results) throws RemoteException {
		for (Result r : results) {
			this.results.add(r);
		}
	}
	
	private void onFinished() throws IOException {
		
		Collections.sort(results, new Comparator<Result>() {

			@Override
			public int compare(Result o1, Result o2) {
				return (int) (o1.record.userid - o2.record.userid);
			}
		});
		System.out.println("finish sorting " + results.size() + " records");
		
		// write to file
		PrintWriter pw = new PrintWriter("results");
		for (Result r : results) {
			pw.println(r.toString());
		}
		pw.flush();
		pw.close();
		System.out.println("finish output " + results.size() + " records");
	}

}
