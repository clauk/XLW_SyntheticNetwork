package gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServerConfig extends Config {
	private static final String Server_CONFIG_FILE = "ServerConfig.xml";
	private static final String BARRIER_ENTRY = "BarrierEntry";
	private static final String SERVER_ENTRY = "ServerEntry";
	private static final String IP = "ip";
	private static final String PORT = "port";
	private static final String SERVER_NUM = "ServerNum";
	

	private int _serverID;
	private int _serverNum;
	private ServerInfo _localServerInfo = new ServerInfo();
	private ServerInfo _barrierInfo = new ServerInfo();
	private ServerInfo[] _serverInfoArray;


	public ServerConfig(int serverID) {
		super(Server_CONFIG_FILE);
		_serverID = serverID;
		loadConfig();
		
	}

	protected void loadConfig() {
		readFile();
		loadServerInfo();

	}

	private void loadServerInfo() {
		
		//Retrieve Barrier server info 		
		Element barrierEntry = (Element) config.getElementsByTagName(BARRIER_ENTRY).item(0);
		Node barrierIP = barrierEntry.getElementsByTagName(IP).item(0);
		_barrierInfo._serverIP = barrierIP.getFirstChild().getNodeValue();
		Node barrierPort = barrierEntry.getElementsByTagName(PORT).item(0);
		_barrierInfo._serverPort = barrierPort.getFirstChild().getNodeValue();
		_barrierInfo._serverAddress = "//"+_barrierInfo._serverIP + ":" + _barrierInfo._serverPort +"/BServer";
		
		
		//Retrieve distributed servers info		
		Node serverNum = config.getElementsByTagName(SERVER_NUM).item(0);
		_serverNum = Integer.parseInt(serverNum.getFirstChild().getNodeValue());
		
		_serverInfoArray = new ServerInfo[_serverNum];
		
		NodeList serverEntryList = config.getElementsByTagName(SERVER_ENTRY);
		for(int i=0; i<_serverNum; i++){
			_serverInfoArray[i] = new ServerInfo();
			Element serverEntry = (Element)(serverEntryList.item(i));
			Node serverIP = serverEntry.getElementsByTagName(IP).item(0);
			_serverInfoArray[i]._serverIP = serverIP.getFirstChild().getNodeValue();
			Node serverPort = serverEntry.getElementsByTagName(PORT).item(0);
			_serverInfoArray[i]._serverPort = serverPort.getFirstChild().getNodeValue();
//			_serverInfoArray[i]._serverAddress = "//"+_serverInfoArray[i]._serverIP + ":" + _serverInfoArray[i]._serverPort+"/Server";
			_serverInfoArray[i]._serverAddress = "//"+_serverInfoArray[i]._serverIP + ":" + _serverInfoArray[i]._serverPort+"/"+i; 
			
			//_serverInfoArray[i]._serverAddress = "//"+_barrierInfo._serverIP + ":" + _barrierInfo._serverPort+"/"+i;
			
			if(i == _serverID){
				_localServerInfo._serverIP = _serverInfoArray[i]._serverIP;
				_localServerInfo._serverPort = _serverInfoArray[i]._serverPort;
				_localServerInfo._serverAddress = _serverInfoArray[i]._serverAddress;
			}
		}
		
	}
	
	public int getServerID() {
		return _serverID;
	}
	
	public int getServerNum() {
		return _serverNum;
	}

	public ServerInfo getBarrierInfo() {
		return _barrierInfo;
	}

	public ServerInfo getLocalServerInfo() {
		return _localServerInfo;
	}
	
	public ServerInfo[] getServerInfoArray() {
		return _serverInfoArray;
	}

}
