package rmi;
import java.io.Serializable;


public class Record implements Serializable {

	public long userid;
	public boolean[] attributes;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9163774801769495403L;

	public Record(){}
	
	public Record(long uID, int attributeNum){
		userid = uID;
		attributes = new boolean[attributeNum];
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id: ");
		sb.append(userid);
		sb.append(" ");
		if (attributes!=null) {
			for (boolean b : attributes) {
				sb.append(b);
				sb.append(", ");
			}
		}
		return sb.toString();
		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Record) {
			Record other = (Record) obj;
			if (other.userid == this.userid) {
				return true;
			} else {
				return false;
			}
		} else {
			 return false;
		}
	}

	@Override
	public int hashCode() {
		return (int) userid;
	}
	
}
