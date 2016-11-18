package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Vector;

import Client.Parcel;

public class Server {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		try {
			ServerSocket serversocket = new ServerSocket(5);
			
			Server.MainThread mainThread = new Server().new MainThread(serversocket);
			System.out.println("SERVER");
			mainThread.start();
			
			
			System.out.println(serversocket.getInetAddress());
			System.out.println(serversocket.getLocalSocketAddress());
			
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	class SendThread extends Thread{

		//Vector<ObjectOutputStream> ooss;
		Vector<Socket> sockets;
		
		
		
		Coords coords;
		ObjectOutputStream oos;
		
		Vector<ObjectOutputStream> ooss;
		
		int X[];
		int Y[];
		
		public SendThread(ObjectOutputStream oos) throws IOException{

		
			//this.sockets = new Vector<Socket>();
			//this.sockets.addElement(socket);
			
			
			
			this.ooss = new Vector<ObjectOutputStream>();
		//	this.ooss.addElement(new ObjectOutputStream(socket.getOutputStream()));
			this.ooss.addElement(oos);
			
			X = new int[10];
			Y = new int[10];
			
			
			
		}
		public void updateCoords(Coords coords){
			this.coords = coords;
			X[coords.nr]+=coords.X;
			Y[coords.nr]+=coords.Y;
		}
		public void add(PrintWriter pw){
			//this.pw.addElement(pw);
		}
		public void add(ObjectOutputStream oos){
			this.ooss.addElement(oos);
		}
		public void add(Socket socket) throws IOException{
			System.out.println("tu");
			this.sockets.addElement(socket);
			
			this.ooss.addElement(new ObjectOutputStream(socket.getOutputStream()));
		}
		
		public void run(){
			System.out.println("start");
			String text;
			
			
			while(true){
				
				synchronized (this) {
					try {
						//System.out.println("czekam");
						wait();
						
						if(coords.checkingID==false){
							System.out.println("wszyscy");
							
							
								for(ObjectOutputStream oos:ooss){
									for(int i=0; i<10; ++i){
										coords = new Coords(X[i], Y[i], i);
										oos.writeObject(coords);
										//System.out.println(i++);
									}
								}
						}
						//else{
						//	oos = ooss.elementAt(coords.ID);
						//	oos.writeObject(coords);
						//}
						
						//System.out.println("po czekaniu");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
				
			}
			
		
			
		}
		
	}

	
	
	class ReceiveThread extends Thread{

	//	BufferedReader br;
		ObjectInputStream ois;
		Socket socket;
		
		ObjectOutputStream oos;
		//Boolean sendAll;
		
		SendThread sendThread;
		
		MainThread mThread;
		
		int[][] Press = new int[4][10];
		
		public ReceiveThread(ObjectInputStream ois, SendThread sendThread, MainThread mThread) {

			//this.br = br;
			//this.socket = socket;
		//	this.sendAll = sendAll;
			this.ois = ois;
			this.sendThread = sendThread;
			this.mThread = mThread;
		}
		
		public void run(){
			System.out.println("start");
			String text;
			
			
			
			try {
				
				
					//ois = new ObjectInputStream(socket.getInputStream());
					//oos = new ObjectOutputStream(socket.getOutputStream());
					Parcel p;
					
					while((p = (Parcel) ois.readObject())!=null){
						
						
						
						Coords coords = null;
						
						if(p.isCheckingID==false){
							
							Integer in = new Integer(mThread.Players.get(p.ID));
							int i = in.intValue();
							
							if(p.key==1 && p.press==1){
								Press[1][i]=1;
								coords = new Coords(10, 0, i);
							}
							else if(p.key==0 && p.press==1){
								Press[1][i]=1;
								coords = new Coords(-10, 0, i);
							}
							else if(p.key==2 && p.press==1){
								Press[1][i]=1;
								coords = new Coords(0, -10, i);
							}
							else if(p.key==3 && p.press==1){
								Press[1][i]=1;
								coords = new Coords(0, 10, i);
							}
							else if(p.key==1 && p.press==0){
								Press[1][i]=0;
								coords = new Coords(0,0,i);
							}
							else if(p.key==0 && p.press==0){
								Press[1][i]=0;
								coords = new Coords(0,0,i);
							}
							else if(p.key==2 && p.press==0){
								Press[1][i]=0;
								coords = new Coords(0,0,i);
							}
							else if(p.key==3 && p.press==0){
								Press[1][i]=0;
								coords = new Coords(0,0,i);
							}
							else{
								coords = new Coords(0,0,i);
							}
							
							
							
							
							
							
							
						}
						else{
							
							System.out.println("Sprawdzanie czy juz jest zajety ten ID");
							coords = new Coords(true, true, p.ID);
							
						}
						
						
						synchronized (sendThread) {
							sendThread.updateCoords(coords);
							sendThread.notify();
						}
						
						System.out.println(p.key+" "+p.press);
						
						
						//oos.writeObject(coords);
						
						
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				//e.printStackTrace();
				System.out.println("Client wyszedl z serwera");
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	class MainThread extends Thread{
		
		ServerSocket serverSocket;
		Socket socket;
		Vector<Socket> sockets;
		
		
		
		HashMap<String, Integer> Players;
		
		int nextNR = 0;
		
	//	Vector<BufferedReader> sbrs;
	//	Vector<PrintWriter> pws;
	//	BufferedReader br;
		
		
		//Vector<Server.ReceiveThread> rTs;
		
		Server.SendThread sT=null;
		
		public MainThread(ServerSocket serverSocket){
			this.serverSocket = serverSocket;
			sockets = new Vector<Socket>();
			
			Players = new HashMap<String, Integer>();
		//	sbrs = new Vector<BufferedReader>();
		//	pws = new Vector<PrintWriter>();
			//rTs = new Vector<Server.ReceiveThread>();
		//	br = new BufferedReader(new InputStreamReader(System.in));
		}
		
		public void run(){
			
			while(true){
				try {
					socket = serverSocket.accept();
					sockets.add(socket);
					System.out.println("NOWE POLACZENIE");
					
				//	BufferedReader sbr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					RegisterServerThread regSerThread = new RegisterServerThread(socket, this);
					regSerThread.start();
					
					/*if(sT==null){
						sT = new Server().new SendThread(socket);
						sT.start();
					}else{
						sT.add(socket);
						
					}*/
					
				//	Server.ReceiveThread rT = new Server().new ReceiveThread(socket, sT);
					
				//	rT.start();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		public Socket getSocket(){
			return socket;
		}
	}
	
}


