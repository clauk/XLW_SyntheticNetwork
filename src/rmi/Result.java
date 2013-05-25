package rmi;

import java.util.List;

public class Result {

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
	
	public Result(Record rec, List<Long> edgeL){
		record = rec;
		edgeList = edgeL;
	}
}
