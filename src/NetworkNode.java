
public class NetworkNode {
	public int node_id;
	public int [] attributes;
	
	public NetworkNode(int nodeID, int attributeNum)
	{
		node_id = nodeID;
		attributes = new int[attributeNum];
	}
	
	public NetworkNode(int attributeNum)
	{
		node_id = -1;
		attributes = new int[attributeNum];
	}
	
	public void setAttribute(int pos, int value)
	{
		attributes[pos] = value;
	}
}
