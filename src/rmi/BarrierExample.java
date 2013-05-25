package rmi;

import java.rmi.Naming;

public class BarrierExample {

	public BarrierExample(int n) {
		try {
			BarrierImpl server = new BarrierImpl();
			server.SetServerNum(n);
			Naming.rebind("//127.0.0.1:1099/BServer", server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length > 0)
			new BarrierExample(Integer.parseInt(args[0]));
		else
			new BarrierExample(1);
	}
}
