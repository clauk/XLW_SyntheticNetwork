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


import rmi.*;

public class NetworkGenerator {
	private NetworkConfig _networkConfig = null;

	// Network Parameters
	private int _n;
	private int _k;
	private Double[] _mu;
	private Double[][][] _theta;
	private Random randomProb;
	private long _startID;
	
	//Pre-compute attribute block theta
	private int _attribute_block_num;
	private int _attribute_block_size;
	private Double[][][] _block_theta;

	// Network Nodes
	private LinkedList<Record> _nodeList;

	// Network Edges Result
	private Map<Long, LinkedList<Long>> _edgeListMap;

	public NetworkGenerator(int localServerID, long startRecordID) {
		_networkConfig = new NetworkConfig(startRecordID);
		_nodeList = new LinkedList<Record>();
		_edgeListMap = new HashMap<Long, LinkedList<Long>>();
		randomProb = new Random(_startID);
		setParameters();
	}

	public void setParameters(){
		_n = _networkConfig.getNodeNum();
		_k = _networkConfig.getAttriNum();
		_mu = _networkConfig.getMuVector();
		_theta = _networkConfig.getThetaVector();
		_startID = _networkConfig.getStartID();
		_attribute_block_size = Constants.ATTRIBUTE_BLOCK_SIZE;
		_attribute_block_num = (_k + Constants.ATTRIBUTE_BLOCK_SIZE-1)/Constants.ATTRIBUTE_BLOCK_SIZE;
		_block_theta = new Double[_attribute_block_num][(int)Math.pow(2.0, (double)(_attribute_block_size))][(int)Math.pow(2.0, (double)(_attribute_block_size))];
		precomputeBlockTheta();
	}

	private void precomputeBlockTheta(){
		int theta_array_size = (int)Math.pow(2.0, (double)(_attribute_block_size));
		int [] theta_one = new int[_attribute_block_size];
		int [] theta_two = new int[_attribute_block_size];
		int remain_attribute_block_size = _k;
		for(int i=0; i<_attribute_block_num; i++){
			if(i==_attribute_block_num-1){
				int remain_theta_array_size = (int)Math.pow(2.0, (double)(remain_attribute_block_size));
				
				for(int j=0; j<remain_theta_array_size; j++){
					int theta_one_value = j;
					for(int k = remain_attribute_block_size-1; k>=0; k--){
						theta_one[k] = theta_one_value%2;
						theta_one_value/=2;
					}
					for(int k=0; k<remain_theta_array_size; k++){
						int theta_two_value = k;
						for(int h = remain_attribute_block_size-1; h>=0; h--){
							theta_two[h] = theta_two_value%2;
							theta_two_value/=2;
						}
						
						Double prob = 1.0;
						for(int h=0; h<remain_attribute_block_size; h++){
							prob *= _theta[(int)(i*_attribute_block_size + h)][theta_one[h]][theta_two[h]];
						}
						_block_theta[i][j][k] = prob;					
					}
				}
			}
			else{
				for(int j=0; j<theta_array_size; j++){
					int theta_one_value = j;
					for(int k = _attribute_block_size-1; k>=0; k--){
						theta_one[k] = theta_one_value%2;
						theta_one_value/=2;
					}
					for(int k=0; k<theta_array_size; k++){
						int theta_two_value = k;
						for(int h = _attribute_block_size-1; h>=0; h--){
							theta_two[h] = theta_two_value%2;
							theta_two_value/=2;
						}
						
						Double prob = 1.0;
						for(int h=0; h<_attribute_block_size; h++){
							prob *= _theta[(int)(i*_attribute_block_size + h)][theta_one[h]][theta_two[h]];
						}
						_block_theta[i][j][k] = prob;					
					}
				}
				remain_attribute_block_size -= _attribute_block_size;
			}			
		}
	}
	
	
	
	public void generateNode() {
		for (long i = _startID; i < _startID+_n; i++) {
			Record _node = new Record(i, _k, _attribute_block_num);
			for (int j = 0; j < _k; j++) {
				if (randomProb.nextDouble() > _mu[j])
					_node.attributes[j] = true;
			}
			generateAttributeBlockFromBinary(_node);
			_nodeList.add(_node);
		}
	}
	
	private void generateAttributeBlockFromBinary(Record _node){
		int blockValue = 0;
		int blockNum = 0;
		for(int i=1; i<=_k; i++){
			blockValue = blockValue*2 + (_node.attributes[i-1]?1:0);
			if(i==_k || i%_attribute_block_size==0){
				_node.attribute_blocks[blockNum] = blockValue;
				blockValue = 0;
				blockNum = blockNum + 1;
			}
		}
	}
	
	/*
	 * @Parameter LinkedList type Record
	 */
	public void generateSelfEdges() {
		int count = 0;
		Iterator<Record> myIterator = _nodeList.iterator();
		while (myIterator.hasNext()) {
			Record myNode = myIterator.next();
			LinkedList<Long> ans = _edgeListMap.get(myNode.userid);
			if(ans == null) ans = new LinkedList<Long>();
			Iterator<Record> targetIterator = _nodeList.iterator();
			while (targetIterator.hasNext()) {
				Record targetNode = targetIterator.next();
				if (checkEdges(myNode, targetNode)){
					ans.add(targetNode.userid);
					count++;
				}				
			}
			_edgeListMap.put(myNode.userid, ans);
		}
		count++;
	}
		
	/*
	 * @Parameter array type Record
	 */
	public void generateEdges(Record targetNodeArray[]) {
		Iterator<Record> myIterator = _nodeList.iterator();
		while (myIterator.hasNext()) {
			Record myNode = myIterator.next();
			LinkedList<Long> ans_temp = new  LinkedList<Long>();
			for(Record targetNode : targetNodeArray){
				if (checkEdges(myNode, targetNode))
					ans_temp.add(targetNode.userid);
			}
			
			LinkedList<Long> ans = _edgeListMap.get(myNode.userid);
			if (ans == null) {
				System.out.println("ans is null!!!!!!!!");
				System.exit(1);
			}
			synchronized (ans) {
				ans.addAll(ans_temp);
			}
		}
	}
	
	/*
	 * @Parameter array type Record
	 */
	public void generateEdges(Record targetNodeArray[], LinkedList<Record> nodeList, Map<Long, LinkedList<Long>> edgeListMap) {
		Iterator<Record> myIterator = nodeList.iterator();
		while (myIterator.hasNext()) {
			Record myNode = myIterator.next();
			LinkedList<Long> ans_temp = new  LinkedList<Long>();
			for(Record targetNode : targetNodeArray){
				if (checkEdges(myNode, targetNode))
					ans_temp.add(targetNode.userid);
			}
			
			LinkedList<Long> ans = edgeListMap.get(myNode.userid);
			if (ans == null) {
				System.out.println("ans is null!!!!!!!!");
				System.exit(1);
			}
			
			ans.addAll(ans_temp);			
		}
	}
	
	public void mergeEdgeListMap(Map<Long, LinkedList<Long>> edgeListMap){
		for(Long key: edgeListMap.keySet()){
			if(_edgeListMap.containsKey(key)){
				LinkedList<Long> ans = _edgeListMap.get(key);
				synchronized (ans) {
					ans.addAll(edgeListMap.get(key));
				}
			} else {
				_edgeListMap.put(key, edgeListMap.get(key));
			}
		}
	}
	
	private double one(Record myNode, Record targetNode){
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
		return prob;
	}
	private double two(Record myNode, Record targetNode){
		double prob = 1.0;
		for (int i = 0; i < _attribute_block_num; i++) {
			prob *= _block_theta[i][myNode.attribute_blocks[i]][targetNode.attribute_blocks[i]];
		}
		return prob;
	}
	private boolean checkEdges2(Record myNode, Record targetNode) {
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
		if (randomProb.nextDouble() <= prob)
			return true;
		else
			return false;
	}
	
	private boolean checkEdges(Record myNode, Record targetNode) {
		double prob = 1.0;
		for (int i = 0; i < _attribute_block_num; i++) {
			prob *= _block_theta[i][myNode.attribute_blocks[i]][targetNode.attribute_blocks[i]];
		}
		if (randomProb.nextDouble() <= prob)
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
