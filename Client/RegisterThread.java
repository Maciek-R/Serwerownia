package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import Server.Coords;

public class RegisterThread extends Thread {

	Client cl;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public RegisterThread(Client cl, ObjectOutputStream oos, ObjectInputStream ois) {
		this.cl = cl;
		this.oos=oos;
		this.ois=ois;
	}
	
	@Override
	public void run() {
		
		String text;
		Coords coords=null;
	//	Client.SendThread sT = new Client().new SendThread(br, pw);
	//	sT.start();
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("Your name: ");
		String ID = input.nextLine();
		
		Parcel p = new Parcel(true, ID);
		
		
		
		try {
			oos.writeObject(p);
			while((coords = (Coords) ois.readObject())!=null){
				
				if(coords.canUseID) 
				{
					System.out.println("Zaakceptowano ID"); 
					synchronized (cl) {
						cl.ID = ID;  
						cl.notify();
					}
					
					break;
				}
				else{
					System.out.println("ID already taken");
					System.out.print("Your name: ");
					ID = input.nextLine();
					p = new Parcel(true, ID);
					oos.writeObject(p);
				}
				
			
				
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return null;
		
	}

	
}
