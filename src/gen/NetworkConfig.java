package gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NetworkConfig extends Config {
	private static final String Network_CONFIG_FILE = "NetworkConfig.xml";
	private static final String NetworkConfig = "NetworkConfig";
	private static final String NETWORK_PARAMETER = "NetworkParameter";
	private static final String NODE_NUMBER = "nodeNum";
	private static final String ATTRIBUTE_NUMBER = "attriNum";
	private static final String START_ID = "startID";
	private static final String MU_VECTOR = "mu";
	private static final String MU_VALUE = "muValue";
	private static final String THETA_VECTOR = "theta";
	private static final String THETA_ENTRY = "thetaEntry";
	private static final String THETA_VALUE_00 = "thetaValue00";
	private static final String THETA_VALUE_01 = "thetaValue01";
	private static final String THETA_VALUE_10 = "thetaValue10";
	private static final String THETA_VALUE_11 = "thetaValue11";

	private int _nodeNum;
	private int _attributeNum;
	private long _startID;
	private Double[] _muVector;
	private Double[][][] _thetaVector;

	public NetworkConfig() {
		super(Network_CONFIG_FILE);
		loadConfig();
	}

	protected void loadConfig() {
		readFile();
		loadNetworkParameter();

	}

	private void loadNetworkParameter() {
		Element networkParameter = (Element) config.getElementsByTagName(NETWORK_PARAMETER).item(0);

		Node nodeNum = networkParameter.getElementsByTagName(NODE_NUMBER).item(0);
		_nodeNum = Integer.parseInt(nodeNum.getFirstChild().getNodeValue());
		Node attriNum = networkParameter.getElementsByTagName(ATTRIBUTE_NUMBER).item(0);
		_attributeNum = Integer.parseInt(attriNum.getFirstChild().getNodeValue());
		Node startID = networkParameter.getElementsByTagName(START_ID).item(0);
		_startID = Long.parseLong(attriNum.getFirstChild().getNodeValue());

		_muVector = new Double[_attributeNum];
		_thetaVector = new Double[_attributeNum][2][2];

		NodeList mu = networkParameter.getElementsByTagName(MU_VALUE);
		for (int i = 0; i < _attributeNum; i++) {
			double muValue = Double.parseDouble(mu.item(i).getFirstChild().getNodeValue());
			_muVector[i] = muValue;
		}

		NodeList theta = networkParameter.getElementsByTagName(THETA_ENTRY);
		for (int i = 0; i < _attributeNum; i++) {
			Element thetaEntry = (Element) (theta.item(i));
			Node thetaValue00 = thetaEntry.getElementsByTagName(THETA_VALUE_00).item(0);
			_thetaVector[i][0][0] = Double.parseDouble(thetaValue00.getFirstChild().getNodeValue());
			Node thetaValue01 = thetaEntry.getElementsByTagName(THETA_VALUE_01).item(0);
			_thetaVector[i][0][1] = Double.parseDouble(thetaValue01.getFirstChild().getNodeValue());
			Node thetaValue10 = thetaEntry.getElementsByTagName(THETA_VALUE_10).item(0);
			_thetaVector[i][1][0] = Double.parseDouble(thetaValue10.getFirstChild().getNodeValue());
			Node thetaValue11 = thetaEntry.getElementsByTagName(THETA_VALUE_11)	.item(0);
			_thetaVector[i][1][1] = Double.parseDouble(thetaValue11.getFirstChild().getNodeValue());
		}
	}

	public int getNodeNum() {
		return _nodeNum;
	}

	public int getAttriNum() {
		return _attributeNum;
	}

	public Double[] getMuVector() {
		return _muVector;
	}

	public Double[][][] getThetaVector() {
		return _thetaVector;
	}
	
	public long getStartID(){
		return _startID;
	}

}
