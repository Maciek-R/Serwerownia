package Client;

import java.io.PrintWriter;
import java.io.Serializable;

public class Parcel implements Serializable{

	
	public int key;
	public int press;
	public String ID;
	
	public transient PrintWriter pwi;
	
	public boolean isCheckingID=false;
	
	public Parcel(int a, int b, PrintWriter pw, String ID, boolean isCheckingID){
		key = a;
		press = b;
		pwi = pw;
		this.ID = ID;
		this.isCheckingID = isCheckingID;
	}
	public Parcel(boolean isCheckingID, String ID){
		this.isCheckingID = isCheckingID;
		this.ID = ID;
	}
	
	public String toString(){
		return new String(key+" "+press);
	}
	
}
