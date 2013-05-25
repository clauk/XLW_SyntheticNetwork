package rmi;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1869779904245691367L;
	public Record record;
	public List<Long> edgeList;
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(record.userid);
		sb.append(": (");
		for (Long id : edgeList) {
			sb.append(id+", ");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public Result(){
	}
	
	public Result(Record rec, List<Long> edgeL){
		record = rec;
		edgeList = edgeL;
	}
}
