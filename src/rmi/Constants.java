package rmi;

public class Constants {
	//the ATTRIBUTE_BLOCK_SIZE should be related to (2^(size+1) smaller than) L1 cache size
	public static final int ATTRIBUTE_BLOCK_SIZE = 6;
	public static final String GEN_NODE_BARRIER_LABEL = "genNodeBarrier";
	public static final String GEN_EDGE_BARRIER_LABEL = "genEdgeBarrier";
	public static final String FINISH_LABEL = "finish";
	public static final int PUT_RESULT_BLOCK_SIZE = 250;
	public static final int FETCH_RECORD_BLOCK_SIZE = 250;
}
