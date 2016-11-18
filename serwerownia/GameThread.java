package serwerownia;

import java.awt.EventQueue;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.swing.JFrame;

public class GameThread extends Thread {

	Frejm frame;
	PrintWriter pw;
	ObjectOutputStream oos;
	
	public GameThread(PrintWriter pw, ObjectOutputStream oos, String ID){
		this.pw = pw;
		this.oos = oos;
		frame = new Frejm(pw, oos, ID);
	}
	
	public Frejm getFrejm(){
		return frame;
	}
	public Board getBoard(){
		return frame.getBoard();
	}
	
	public void run(){
		
				
			
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
				
					
		
	}
	
	
}

class Frejm extends JFrame{
	
	Board board;
	
	public Frejm(PrintWriter pw, ObjectOutputStream oos, String ID){
		//setSize(Board.BOARD_WIDTH_PIX, Board.BOARD_HEIGHT_PIX);
		
				setTitle("Game");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setLocationRelativeTo(null);
				
				board = new Board(pw, oos, ID);
				add(board);
				pack();
				
				
				
				board.start();
	}
	public Board getBoard(){
		return board;
	}
}