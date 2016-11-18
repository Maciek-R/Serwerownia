package Client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;

import Server.Coords;
import serwerownia.Board;
import serwerownia.GameThread;

public class Client extends Thread{

	String ID;
	
	public void run(){
		try {
			Socket socket = new Socket("127.0.0.1", 5);
			System.out.println("CLIENT");
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			BufferedReader sbr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			
		
			
			
			//int ID = tryGetYourID(oos, ois);
			
			
			RegisterThread regThread = new RegisterThread(this, oos, ois);
			regThread.start();
			
			synchronized(this){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
			
			
			GameThread gameThread = new GameThread(pw, oos, ID);
			gameThread.start();
			
			if(gameThread.getFrejm() == null) System.out.println("asdasdsad");
			
			Client.ReceiveThread rT = new Client().new ReceiveThread(sbr, gameThread.getBoard(), socket, ois);
			rT.start();
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Client cl = new Client();
		cl.start();
		

	}

	private class ReceiveThread extends Thread{

		BufferedReader br;
		Board board;
		Socket socket;
		
		ObjectInputStream ois;
		
		public ReceiveThread(BufferedReader br, Board board, Socket socket, ObjectInputStream ois) {

			this.br = br;
			this.board = board;
			this.socket = socket;
			this.ois = ois;
		}
		
		public void run(){
			System.out.println("start");
			String text;
			
			
			
			try {
				
				Coords coords;
				//ois = new ObjectInputStream(socket.getInputStream());
				
				Object obj = null;
				
				while((coords = (Coords) ois.readObject())!=null){
					
					
					//System.out.println("aaa");
					System.out.println("Odebrano");
					
					
					board.updatePlayer(coords);
				}
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Serwer not found");
				e.printStackTrace();
			}
			
		}
		
	}
	private class SendThread extends Thread{

		BufferedReader br;
		PrintWriter pw;
		
		public SendThread(BufferedReader br, PrintWriter pw) {

			this.br = br;
			this.pw = pw;
		}
		
		public void run(){
			System.out.println("start");
			String text;
			
			
			try {
				while((text = br.readLine()) != null){
					pw.println(text);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
