package serwerownia;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import Client.Parcel;
import Server.Coords;




/**
 * Klasa obs³uguj¹ca zadania wykonywane po stronie serwera tj.
 * zmiana wszystkich ruszaj¹cych siê obiektów zarówno pierwszego jak i drugiego gracza
 * (ruch pocisków, poruszanie siê gracza)
 * oraz odpowiedzialna za przesy³anie informacji do klienta
 * 
 * @author Maciek Ruszczyk
 * @see MainBoard
 * @see ServerEncoder
 * @see ServerMessage
 */

public class Board extends JPanel implements ActionListener{
	
	private final static double PIXELS_PER_SECONDS=400;	
	private final static double PIXELS_FOR_BULLETS=1000;
	private final static double PIXELS_FOR_GIFTS=200;
	
	protected final static int BOARD_WIDTH_PIX = 300; //1024;
	protected final static int BOARD_HEIGHT_PIX = 200;//820;
	protected final static int WIDTH_KLOCKA = 32;
	protected final static int HEIGHT_KLOCKA = 32;
	
	Adapter adap;
	MouseAdapter MouseAdapter;
//	MouseListenerAdapter MouseListenerAdapter;
	
	javax.swing.Timer timer;
	
	double time_start;
	double time_elapsed;
	
	//double X = 100;
	//double Y = 100;
	//double X[];
	//double Y[];
	int myID;
	int numberOfPlayers;
	
	double X[] = new double[10];
	double Y[] = new double[10];
	
	int Press[];

	boolean GameOver = false;
	long Time_end_round;
/*	Vector<Ball> ball;
	
	Platform platform;
	
	Vector<Block> blocks;
	
	Random random;
	
	boolean isRunning = false;
	
	int MouseEnteredOption = 0; //0-nothing, 1-play,2-Exit 
	Vector<Ball> ballsInMenu;
	
	boolean isPaused = false;
	
	Loader loader;
	
	Vector<Block> MenuBlocks;*/
	
	PrintWriter pw;
	ObjectOutputStream oos;
	
	
	String ID;
	
	public Board(PrintWriter pw, ObjectOutputStream oos, String ID) {
		
		this.pw = pw;
		this.oos = oos;
		this.ID=ID;
		
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setPreferredSize(new Dimension(BOARD_WIDTH_PIX, BOARD_HEIGHT_PIX));
		
		
		adap = new Adapter();	
		this.addKeyListener(adap);
		MouseAdapter = new MouseAdapter();
		this.addMouseMotionListener(MouseAdapter);
		
		Press = new int[4];
		
		
		
		//System.out.print("Your name is "+ID);
		
		
	//	MouseListenerAdapter = new MouseListenerAdapter();
		//this.addMouseListener(MouseListenerAdapter);
		
		
	/*	ball = new Vector<Ball>();
		ballsInMenu = new Vector<Ball>();
		random = new Random();
		
		
		for(int i=0; i<10; ++i){
			int x = random.nextInt(BOARD_WIDTH_PIX);
			int y = random.nextInt(BOARD_HEIGHT_PIX-10);
			int an = random.nextInt(360);
			ball.add(new Ball(x, y, an));
		}
		for(int i=0; i<10; ++i){
			int x = random.nextInt(BOARD_WIDTH_PIX);
			int y = random.nextInt(BOARD_HEIGHT_PIX-10);
			int an = random.nextInt(360);
			ballsInMenu.add(new Ball(x, y, an));
		}
		
		platform = new Platform();
		
		blocks = new Vector<Block>();
		
		
		
		for(int i=0; i<10; ++i){
			blocks.add(new Block(i*50, 100, 50));
			blocks.add(new Block(500+ i*40, 200, 40));
		}
		
		loader = new Loader();
		
		MenuBlocks = new Vector<Block>();
		MenuBlocks.add(new Block(BOARD_WIDTH_PIX/2-50, 180, 120, 30));
		MenuBlocks.add(new Block(BOARD_WIDTH_PIX/2-50, 280, 120, 30));
		MenuBlocks.add(new Block(BOARD_WIDTH_PIX/2-50, 380, 120, 30));
		*/
		
		timer = new Timer(12, this);
		
	}
	
	public void start(){
		
		timer.start();
		time_start = System.currentTimeMillis();
	}
	public void stop(){
		timer.stop();
	}
	

	//@Override
	
	public void actionPerformed(ActionEvent act) {
		
		long time = System.currentTimeMillis();
		time_elapsed = time-time_start;
		time_start = time;	
		double time_elapsed_in_sec = time_elapsed/1000;
		
		double przes = (((time_elapsed_in_sec)) * PIXELS_PER_SECONDS);     
		
		/*if(Press[0]){
			X-=przes;
		}
		else if(Press[1]){
			X+=przes;
		}
		*/
		if(GameOver){
			if(time-Time_end_round > 5000){
				GameOver = false;
				
			}
		}
		
		/*if(isRunning){
			ball_serve(przes);
			platformServe(przes);
			repaint();
		}
		else{
			ballServeInMenu(przes);
		}*/
	}
	
	public void updatePlayer(Coords coords){
		//System.out.println(X + " " +Y);

		/*if(numberOfPlayers!=coords.numberOfPlayers){
			numberOfPlayers = coords.numberOfPlayers;
			X = new double[numberOfPlayers];
			Y = new double[numberOfPlayers];
		}*/
		
		X[coords.nr]=coords.X;
		Y[coords.nr]=coords.Y;
	}
	
	/*private void ball_serve(double przes){
		
		for(int j=0; j<ball.size(); ++j){
			Ball b = ball.get(j);
			
			double przes_x = Math.cos(Math.toRadians(b.getAngle())) * przes;
			double przes_y = Math.sin(Math.toRadians(b.getAngle())) * przes;
			
			b.setPos_x(b.getPos_x()+przes_x);
			b.setPos_y(b.getPos_y()+przes_y);
			
			
			if(b.getPos_x()+b.getwidth()>=BOARD_WIDTH_PIX) {
				b.setAngle(((180-b.getAngle()) % 360));
				b.setPos_x(BOARD_WIDTH_PIX-b.getwidth());
			}
			else if(b.getPos_x()<0) {
				b.setAngle(((180-b.getAngle()) % 360));
				b.setPos_x(0);
			}
			
			if(b.getPos_y()>=BOARD_HEIGHT_PIX){
				ball.remove(j);
				continue;
			}*/
			
			/*if(b.getPos_y()+b.getheight()>=BOARD_HEIGHT_PIX){
				b.setAngle(((-b.getAngle()) % 360));
				b.setPos_y(BOARD_HEIGHT_PIX-b.getheight());
			}*/
		/*	else if(b.getPos_y()<0){
				b.setAngle(((-b.getAngle()) % 360));
				b.setPos_y(0);
			}
			
			
			if(b.getPos_y()+b.getheight() >= platform.getPos_y() && b.getPos_y() <= platform.getPos_y()+platform.getHeight()){
				if(b.getPos_x()+b.getwidth()>=platform.getPos_x() && b.getPos_x() <= platform.getPos_x()+platform.getWidth()){
					
					//System.out.println("JEB");
					double diff = b.getPos_x() - platform.getPos_x()+b.getwidth();
					double angle = (diff/(platform.getWidth()+b.getwidth()))*180  + 180;
					
					b.setAngle(angle);
				}
			}
			
			for(int i=0; i<blocks.size(); ++i){
				
				if(b.getPos_y()+b.getheight() >= blocks.get(i).getPos_y() && b.getPos_y() <= blocks.get(i).getPos_y()+blocks.get(i).getHeight())
					if(b.getPos_x()+b.getwidth()>=blocks.get(i).getPos_x() && b.getPos_x() <= blocks.get(i).getPos_x()+blocks.get(i).getWidth()){
						
						if(b.getPos_x()+b.getwidth() -8> blocks.get(i).getPos_x() && b.getPos_x()<blocks.get(i).getPos_x()+blocks.get(i).getWidth()-8){
							
							b.setAngle(((-b.getAngle()) % 360));
						}
						else{
							b.setAngle(((180-b.getAngle()) % 360));
						}
						blocks.remove(i);
						break;
					}
			}
			
			
		}
	}*/
	/*private void ballServeInMenu(double przes){
		
		for(Ball b:ballsInMenu){
			double przes_x = Math.cos(Math.toRadians(b.getAngle())) * przes;
			double przes_y = Math.sin(Math.toRadians(b.getAngle())) * przes;
			
			b.setPos_x(b.getPos_x()+przes_x);
			b.setPos_y(b.getPos_y()+przes_y);
			
			
			if(b.getPos_x()+b.getwidth()>=BOARD_WIDTH_PIX) {
				b.setAngle(((180-b.getAngle()) % 360));
				b.setPos_x(BOARD_WIDTH_PIX-b.getwidth());
			}
			else if(b.getPos_x()<0) {
				b.setAngle(((180-b.getAngle()) % 360));
				b.setPos_x(0);
			}
			
			if(b.getPos_y()+b.getheight()>=BOARD_HEIGHT_PIX){
				b.setAngle(((-b.getAngle()) % 360));
				b.setPos_y(BOARD_HEIGHT_PIX-b.getheight());
			}
			else if(b.getPos_y()<0){
				b.setAngle(((-b.getAngle()) % 360));
				b.setPos_y(0);
			}
			
			*/
		/*	for(int i=0; i<MenuBlocks.size(); ++i){
				
				if(b.getPos_y()+b.getheight() >= MenuBlocks.get(i).getPos_y() && b.getPos_y() <= MenuBlocks.get(i).getPos_y()+MenuBlocks.get(i).getHeight())
					if(b.getPos_x()+b.getwidth()>=MenuBlocks.get(i).getPos_x() && b.getPos_x() <= MenuBlocks.get(i).getPos_x()+MenuBlocks.get(i).getWidth()){
						
						if(b.getPos_x()+b.getwidth() -8> MenuBlocks.get(i).getPos_x() && b.getPos_x()<MenuBlocks.get(i).getPos_x()+MenuBlocks.get(i).getWidth()-8){
							
							b.setAngle(((-b.getAngle()) % 360));
						}
						else{
							b.setAngle(((180-b.getAngle()) % 360));
						}
						//blocks.remove(i);
						break;
					}
			}
			
		}
	}*/
/*	private void platformServe(double przes){
		if(platform.press[0] == true){
			platform.setPos_x(platform.getPos_x()-przes);
			if(platform.getPos_x()<0)
				platform.setPos_x(0);
		}
		else if(platform.press[1] == true){
			platform.setPos_x(platform.getPos_x()+przes);	
			if(platform.getPos_x()+platform.getWidth()>=BOARD_WIDTH_PIX)
				platform.setPos_x(BOARD_WIDTH_PIX-platform.getWidth());
		}
	}*/
	
/*	private void drawBall(Graphics g){
	//	for(int i=0; i<pociski.size(); ++i)
		//	g.drawImage(Pocisk.im, (int)pociski.elementAt(i).getPositionX(), (int)pociski.elementAt(i).getPositionY(), null);
		for(Ball b:ball)
			g.drawImage(Ball.img, (int)b.getPos_x(), (int)b.getPos_y(), null);
			//g.drawRect((int)b.getPos_x(), (int)b.getPos_y(), 10, 10);
	}*/
	/*private void drawPlatform(Graphics g){
		g.setColor(Color.red);
		g.fillRect((int)platform.getPos_x(), (int)platform.getPos_y(), 80, 10);
	}
	private void drawBlocks(Graphics g){
		
		for(Block block:blocks){
			g.setColor(Color.black);
			g.drawRect((int)block.getPos_x(), (int)block.getPos_y(), block.getWidth(), block.getHeight());
			g.setColor(Color.green);
			g.fillRect((int)block.getPos_x()+1, (int)block.getPos_y()+1, block.getWidth()-1, block.getHeight()-1);
		}
		repaint();
			//g.fillRect((int)block.getPos_x(), (int)block.getPos_y(), block.getWidth(), block.getHeight());
	}*/
/*	private void drawMenu(Graphics g){
		g.setColor(Color.black);
		g.drawString("ARKANOID", 490, 100);
	
		
		g.drawRect(BOARD_WIDTH_PIX/2-50, 180, 120, 30);
		g.drawRect(BOARD_WIDTH_PIX/2-50, 280, 120, 30);
		g.drawRect(BOARD_WIDTH_PIX/2-50, 380, 120, 30);
		g.drawRect(BOARD_WIDTH_PIX/2-50, 480, 120, 30);
		
		g.drawString("Play", BOARD_WIDTH_PIX/2, 200);
		g.drawString("Load Game", BOARD_WIDTH_PIX/2-18, 300);
		g.drawString("Save Game", BOARD_WIDTH_PIX/2-18, 400);
		g.drawString("Exit", BOARD_WIDTH_PIX/2, 500);
		
		if(MouseEnteredOption == 0){
			//g.drawRect(BOARD_WIDTH_PIX/2-50, 180, 120, 30);
			//g.drawRect(BOARD_WIDTH_PIX/2-50, 280, 120, 30);
		}
		else if(MouseEnteredOption == 1){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 180, 120, 30);
		}
		else if(MouseEnteredOption == 2){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 280, 120, 30);
		}
		else if(MouseEnteredOption == 3){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 380, 120, 30);
		}
		else if(MouseEnteredOption == 4){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 480, 120, 30);
		}

	}
	private void drawBallsInMenu(Graphics g){
		
		for(Ball b:ballsInMenu){
		//	g.drawRect((int)b.getPos_x(), (int)b.getPos_y(), 10, 10);
			g.drawImage(Ball.img, (int)b.getPos_x(), (int)b.getPos_y(), null);
		}
		repaint();
	}*/
	
	private void drawPlayer(Graphics g){
		g.setColor(Color.RED);
		
		for(int i=0; i<10; ++i){
			g.drawRect((int)X[i], (int)Y[i], 30, 30);
		}
		repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		drawPlayer(g);
	/*	if(isRunning){
			drawBall(g);
			drawPlatform(g);
			drawBlocks(g);
			
			if(isPaused){
				g.setColor(Color.black);
				g.drawString("Pause", 500, 400);
				repaint();
			}
		}*/
		/*else{
			
			drawMenu(g);
			drawBallsInMenu(g);
		}*/
		//g.drawImage(Map.bg, 0, 0, null);
		
		/*drawMap(g);
		drawHealth(g);
		drawBullets(g);
		drawHelper(g);
		drawGifts(g);
		drawPlayer(g);*/
}
	
	
//tylko obsluga gracza nr 1
	private void pressed(int a, int b){
		Press[a]=b;
		/*pw.print(a);
		pw.println();
		pw.print(b);
		pw.println();*/
		Parcel pp = new Parcel(a, b, pw, ID, false);
		
		//pw.println(pp.toString());
		//pw.println(pp);
		
		try {
			oos.writeObject(pp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//pw.flush();
	}
	
	class Adapter implements KeyListener{
		
		
		public void keyPressed(KeyEvent e) {
			
			
			int keyCode = e.getKeyCode();
			
			
			switch(keyCode){
			
			case KeyEvent.VK_A: pressed(0, 1); break;
			case KeyEvent.VK_D: pressed(1, 1); break;
			case KeyEvent.VK_W: pressed(2, 1); break;
			case KeyEvent.VK_S: pressed(3, 1); break;
			/*case KeyEvent.VK_SPACE: start(); isPaused = false; break;
			case KeyEvent.VK_P: stop(); isPaused = true; break;*/
		
		
			/*case KeyEvent.VK_ESCAPE: 
				if(!isRunning) System.exit(0); 
				else{
					//stop();
					isRunning = false;
				}
				break;	*/
			// gracz 2
		/*	case KeyEvent.VK_W: if(!player[1].isJumping) player[1].jump(); break;
			case KeyEvent.VK_A: player[1].dir=player[1].look=Direction.Left; player[1].isMoving=true; player[1].press[2] = true; break;
			case KeyEvent.VK_D: player[1].dir=player[1].look=Direction.Right;player[1].isMoving=true;player[1].press[3] = true; break;
			case KeyEvent.VK_G: cannonUpOrDown(Podnoszenie.dol, 1); break;
			case KeyEvent.VK_T: cannonUpOrDown(Podnoszenie.gora, 1); break;
			case KeyEvent.VK_Y: nowy_pocisk(1); break;*/
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int keyCode = e.getKeyCode();
			
			switch(keyCode){
			
			case KeyEvent.VK_A: pressed(0, 0); break;
			case KeyEvent.VK_D: pressed(1, 0); break;
			case KeyEvent.VK_W: pressed(2, 0); break;
			case KeyEvent.VK_S: pressed(3, 0); break;
			}
			
			
		
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
			
		}
			
		
	}
	
	
	
	class MouseAdapter implements MouseMotionListener{
		

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		/*	if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=180 && e.getY() <= 180+30){
			
				MouseEnteredOption = 1;
			}
			else if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=280 && e.getY() <= 280+30){
			
				MouseEnteredOption = 2;
			}
			else if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=380 && e.getY() <= 380+30){
			
				MouseEnteredOption = 3;
			}
			else if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=480 && e.getY() <= 480+30){
			
				MouseEnteredOption = 4;
			}
			else{
				MouseEnteredOption = 0;
			}
			repaint();*/
		}
		
	}
	/*class MouseListenerAdapter implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(MouseEnteredOption==1){
				
				System.out.println("Play");
				isRunning = true;
				isPaused = false;
				start();
			}
			else if(MouseEnteredOption==2){
			
				System.out.println("Load Game");
				loader.loadGame();
				//System.out.println(loader.getPlatform().getPos_x()+"   "+loader.getPlatform().getPos_y());
				
				platform = loader.getPlatform();
				*/
				/*for(int i=0; i<loader.getBallSize(); ++i){
					System.out.println(loader.getBall(i).getPos_x()+" "+loader.getBall(i).getPos_y()+" "+loader.getBall(i).getAngle());
				}*/
			/*	ball.clear();
				for(int i=0; i<loader.getBallSize(); ++i){
					ball.add(loader.getBall(i));
				}
				
				blocks.clear();
				for(int i=0; i<loader.getBlockSize(); ++i){
					blocks.add(loader.getBlock(i));
				}
				
				System.out.println(ball.size());
				
			}
			else if(MouseEnteredOption==3){
				System.out.println("Save Game");
				loader.saveGame(platform, ball, blocks);
			}
			else if(MouseEnteredOption==4){
				System.out.println("Exit");
				System.exit(0);
			}
			else{
				System.out.println("Nothing");
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}*/



	
	

	
	
	
	
}
