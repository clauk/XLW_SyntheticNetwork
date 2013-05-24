package gen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.json.simple.JSONValue;

import rmi.*;

public class NetworkGenerator {
	private NetworkConfig _networkConfig = null;

	// Network Parameters
	private int _n;
	private int _k;
	private long _startID;
	private Double[] _mu;
	private Double[][][] _theta;

	// Network Nodes
	private LinkedList<Record> _nodeList;

	// Network Edges Result
	private Map<Long, LinkedList<Long>> _edgeListMap;

	public NetworkGenerator() {
		_networkConfig = new NetworkConfig();
		_nodeList = new LinkedList<Record>();
		_edgeListMap = new HashMap<Long, LinkedList<Long>>();
		setParameters();
	}

	public void setParameters(){
		_n = _networkConfig.getNodeNum();
		_k = _networkConfig.getAttriNum();
		_mu = _networkConfig.getMuVector();
		_theta = _networkConfig.getThetaVector();
		_startID = _networkConfig.getStartID();
	}

	public void generateNode() {
		for (long i = _startID; i < _startID+_n; i++) {
			Record _node = new Record(i, _k);
			for (int j = 0; j < _k; j++) {
				if (Math.random() <= _mu[j])
					_node.attributes[j] = true;
			}
			_nodeList.add(_node);
		}
	}

	/*
	 * @Parameter LinkedList type Record
	 */
	public void generateEdges(LinkedList<Record> targetNodeList) {
		Iterator<Record> myIterator = _nodeList.iterator();
		while (myIterator.hasNext()) {
			Record myNode = myIterator.next();
			LinkedList<Long> ans = _edgeListMap.get(myNode.userid);
			if(ans == null) ans = new LinkedList<Long>();
			Iterator<Record> targetIterator = targetNodeList.iterator();
			while (targetIterator.hasNext()) {
				Record targetNode = targetIterator.next();
				if (checkEdges(myNode, targetNode))
					ans.add(targetNode.userid);
			}
			_edgeListMap.put(myNode.userid, ans);
		}
	}
	
	/*
	 * @Parameter array type Record
	 */
	public void generateEdges(Record targetNodeArray[]) {
		Iterator<Record> myIterator = _nodeList.iterator();
		while (myIterator.hasNext()) {
			Record myNode = myIterator.next();
			LinkedList<Long> ans = _edgeListMap.get(myNode.userid);
			if(ans == null) ans = new LinkedList<Long>();			
			for(Record targetNode : targetNodeArray){
				if (checkEdges(myNode, targetNode))
					ans.add(targetNode.userid);
			}
			_edgeListMap.put(myNode.userid, ans);
		}
	}

	private boolean checkEdges(Record myNode, Record targetNode) {
		double prob = 1.0;
		for (int i = 0; i < _k; i++) {
			if(myNode.attributes[i] == true){
				if(targetNode.attributes[i] == true){
					prob *= _theta[i][1][1];
				}
				else{
					prob *= _theta[i][1][0];
				}					
			}
			else{
				if(targetNode.attributes[i] == true){
					prob *= _theta[i][0][1];
				}
				else{
					prob *= _theta[i][0][0];
				}					
			}
		}
		if (Math.random() <= prob)
			return true;
		else
			return false;
	}
	
	public LinkedList<Record> getNodeResult(){
		return _nodeList;
	}
	
	public Map<Long, LinkedList<Long>> getEdgeResult(){
		return _edgeListMap;
	}
	
	public int getNodeNum(){
		return _n;
	}
	
	public int getAttriNum(){
		return _k;
	}
}
