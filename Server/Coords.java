package Server;

import java.io.Serializable;

public class Coords implements Serializable{

	public int X;
	public int Y;
	public int nr; 		//number, which tells which player is on that coords
	//public boolean isYours;	//if true coords belong to this player
	//public int numberOfPlayers;
	
	public boolean canUseID;
	public boolean checkingID=false;
	public String ID;
	
	public Coords(int X, int Y, int nr){
		this.X = X;
		this.Y = Y;
		this.nr = nr;
		this.checkingID=false;
		//this.numberOfPlayers = numberPlayers;
	}
	public Coords(boolean canUseID, boolean checkingID, String ID){
		this.canUseID=canUseID;
		this.checkingID = checkingID;
		this.ID = ID;
	}
}
