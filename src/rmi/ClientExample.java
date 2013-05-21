package rmi;

import java.rmi.Naming;

public class ClientExample {

	public static void main(String[] s) {
		try {
			IServer server = (IServer) Naming.lookup("//127.0.0.1:1099/Server");
			Record[] records = server.get(1,2);
			for ( Record record : records) {
				System.out.println("get record: " + record);
			}
			
			int result = server.put(records);
			
			System.out.println("put result: "+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
