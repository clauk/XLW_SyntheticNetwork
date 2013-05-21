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

public class NetworkGenerator {
	private NetworkConfig _networkConfig = null;

	// Network Parameters
	private int _n;
	private int _k;
	private Double[] _mu;
	private Double[][][] _theta;

	// Network Nodes
	private LinkedList<NetworkNode> _nodeList;

	// Network Edges Result
	private Map<Integer, LinkedList<Integer>> _edgeList;

	public NetworkGenerator() {
		_networkConfig = new NetworkConfig();
		_nodeList = new LinkedList<NetworkNode>();
	}

	public void generate() throws IOException, InterruptedException {
		_n = _networkConfig.getNodeNum();
		_k = _networkConfig.getAttriNum();
		_mu = _networkConfig.getMuVector();
		_theta = _networkConfig.getThetaVector();

	}

	private void generateNode() {
		for (int i = 0; i < _n; i++) {
			NetworkNode _node = new NetworkNode(i, _k);
			for (int j = 0; j < _k; j++) {
				if (Math.random() <= _mu[j])
					_node.setAttribute(j, 1);
			}
			_nodeList.add(_node);
		}
	}

	// TODO
	// Put the generated node list to other servers
	private void put() {

	}

	private void generateEdges() {
		// TODO
		LinkedList<NetworkNode> _targetNodeList = new LinkedList<NetworkNode>();

		Iterator<NetworkNode> myIterator = _nodeList.iterator();
		while (myIterator.hasNext()) {
			LinkedList<Integer> ans = new LinkedList<Integer>();
			NetworkNode myNode = myIterator.next();
			Iterator<NetworkNode> targetIterator = _targetNodeList.iterator();
			while (targetIterator.hasNext()) {
				NetworkNode targetNode = targetIterator.next();
				if (checkEdges(myNode, targetNode))
					ans.add(targetNode.node_id);
			}
			_edgeList.put(myNode.node_id, ans);
		}
	}

	private boolean checkEdges(NetworkNode myNode, NetworkNode targetNode) {
		double prob = 1.0;
		for (int i = 0; i < _k; i++) {
			prob *= _theta[i][myNode.attributes[i]][targetNode.attributes[i]];
		}
		if (Math.random() <= prob)
			return true;
		else
			return false;
	}
}
