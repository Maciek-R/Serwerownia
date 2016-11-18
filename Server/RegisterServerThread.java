package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Client.Parcel;
import Server.Server.MainThread;

public class RegisterServerThread extends Thread {

	Socket socket;
	//Server.SendThread sT;
	
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	MainThread mThread;
	
	public RegisterServerThread(Socket socket, MainThread mThread) {
		this.socket=socket;
		//this.sT=sendThread;
		this.mThread = mThread;
	}
	
	@Override
	public void run() {
		
		
		
		Parcel p;
		boolean flag = false;
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			while((p = (Parcel) ois.readObject())!=null){
				
			//while(true){
			//p = (Parcel) ois.readObject();
				
				Coords coords = null;
				
				if(p.isCheckingID==true){
					
					
					System.out.println("Sprawdzanie czy juz jest zajety ten ID");
					
					if(mThread.Players.containsKey(p.ID)){
						coords = new Coords(false, true, p.ID);
						
					}
					else{
						coords = new Coords(true, true, p.ID);
						mThread.Players.put(p.ID, mThread.nextNR++);
						flag = true;
					}
					oos.writeObject(coords);
					
					if(flag==false) continue;
					
					
					if(mThread.sT==null){
						mThread.sT = new Server().new SendThread(oos);
						mThread.sT.start();
					}else{
						mThread.sT.add(oos);
					
					}
				
				Server.ReceiveThread rT = new Server().new ReceiveThread(ois, mThread.sT, mThread);
				
				rT.start();
				if(flag)
					break;
				}
				else{
					
					
					
				}
			
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
