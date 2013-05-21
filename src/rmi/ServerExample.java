package rmi;

import java.rmi.Naming;

public class ServerExample {

	public ServerExample() {
		try {
			IServer server = new ServerImpl();
			Naming.rebind("//127.0.0.1:1099/Server", server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] argsStrings) {
		new ServerExample();
	}
}
