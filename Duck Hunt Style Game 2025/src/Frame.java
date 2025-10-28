import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	
	//frame size
	private int screenWidth = 2048, screenHeight = 1080;
	private String title = "Duck HuntDivers";
	
	
	/**
	 * Declare and instantiate (create) your objects here
	 */
	private Background myBackground = new Background();
	private Helldiver helldiverObject = new Helldiver();
	private illuminateShip enemyShip1 = new illuminateShip();
	private illuminateShip enemyShip2 = new illuminateShip();
	private MainCharATEmplacement myMain = new MainCharATEmplacement();
	private AmmoHud ammoHud = new AmmoHud();
	private MyCursor myCursor = new MyCursor();
	private EndScreen ending = new EndScreen();
	private int kills;
	private int wave = 1;
	
	
	//music
	Music mouseClickSound = new Music("cannon_fire.wav", false);
	Music explosionSound = new Music("mixkit-explosion-in-battle-2809.wav", false);
	Music mainTheme = new Music("mainThemeSpaceDebris.wav", true);
	Music losingtheme = new Music("sci-fi-alarm-106436.wav",false);
	
	public void paint(Graphics pen) {
	
		//this line of code is to force redraw the entire frame
		super.paintComponent(pen);
		
		//background should be drawn before the objects 
		//or based on how you want to layer
		myBackground.paint(pen);
		ammoHud.paint(pen);
		//call paint for the object
		//for objects, you call methods on them using the dot operator
		//methods use always involve parenthesis
		
		myMain.paint(pen);
		enemyShip1.paint(pen);
		enemyShip2.paint(pen);
		helldiverObject.paint(pen);
		
		myCursor.paint(pen);
		
		Font f = new Font("Segoe UI", Font.PLAIN, 30);
		pen.setFont(f);
		pen.setColor(Color.white);
		pen.drawString("Number of Kills " + kills + "/40", 1600, 50);
		pen.drawString("WAVE:  " + wave , 1600, 90);
		
		if(wave >= 4) {
			ending.paint(pen);
			ending.changePicture("Victory for the helldivers.png");
		}
		
		
		
		if(ammoHud.ammoNumber() <= 0 || illuminateShip.tooManyEscapes()) {
			ending.paint(pen);
			this.losingtheme.play();
		}
		
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent mouse) {
	    // Runs when the mouse is clicked (pressed and released quickly).
	    // Example: You could use this to open a menu or select an object.
	
	}

	@Override
	public void mouseEntered(MouseEvent mouse) {
	    // Runs when the mouse enters the area of a component (like a button).
	    // Example: You could highlight the button when the mouse hovers over it.
		

	}

	@Override
	public void mouseExited(MouseEvent mouse) {
	    // Runs when the mouse leaves the area of a component.
	    // Example: You could remove the highlight when the mouse moves away.

	}

	@Override
	public void mousePressed(MouseEvent mouse) {
	    // Runs when a mouse button is pressed down.
	    // Example: You could start dragging an object here.
		System.out.println(mouse.getX()+":"+mouse.getY());
		enemyShip1.checkCollision(mouse.getX(), mouse.getY());
		enemyShip2.checkCollision(mouse.getX(), mouse.getY());
		myMain.startAnimation();
		this.mouseClickSound.play();

		
		if(enemyShip1.checkCollision(mouse.getX(), mouse.getY()) || 
				enemyShip2.checkCollision(mouse.getX(), mouse.getY())) {
			helldiverObject.startAnimation();
			kills++;
			System.out.println("Kill count " + kills);
			this.explosionSound.play();
		}
		if(enemyShip1.checkCollision(mouse.getX(), mouse.getY()) &&
				enemyShip2.checkCollision(mouse.getX(), mouse.getY())) {
			helldiverObject.startAnimation();
			kills++;
			System.out.println("Kill count " + kills);
		}
		if((enemyShip1.checkCollision(mouse.getX(), mouse.getY()) == false && 
				enemyShip2.checkCollision(mouse.getX(), mouse.getY()) == false) && ammoHud.ammoNumber() == 3) {
			ammoHud.changePicture("Ammo Hud element two shells.png");
			ammoHud.shellLost();
		} else if((enemyShip1.checkCollision(mouse.getX(), mouse.getY()) == false && 
				enemyShip2.checkCollision(mouse.getX(), mouse.getY()) == false) && ammoHud.ammoNumber() == 2) {
			ammoHud.changePicture("Ammo Hud element low ammo.png");
			ammoHud.shellLost();
		} else if ((enemyShip1.checkCollision(mouse.getX(), mouse.getY()) == false && 
				enemyShip2.checkCollision(mouse.getX(), mouse.getY()) == false) && ammoHud.ammoNumber() == 1) {
			ammoHud.changePicture("Ammo Hud Element.png");
			ammoHud.shellLost();
		} else if ((enemyShip1.checkCollision(mouse.getX(), mouse.getY()) && 
				enemyShip2.checkCollision(mouse.getX(), mouse.getY())) 
				&& (ammoHud.ammoNumber() == 1 || ammoHud.ammoNumber() == 2)){
			ammoHud.changePicture("Ammo Hud element full ammo.png");
			ammoHud.shellsReturned();
		}
	}

	@Override
	public void mouseReleased(MouseEvent mouse) {
	    // Runs when a mouse button is released.
	    // Example: You could stop dragging the object or drop it in place.
		helldiverObject.update();
		myMain.update();
		if(kills == 10) {
			wave++;
		}
		if(kills == 20) {
			wave++;
		}
		if(kills == 30) {
			wave++;
		}
		if(kills == 40) {
			wave++;
		}
	}



	/*
	 * This method runs automatically when a key is pressed down
	 */
	public void keyPressed(KeyEvent key) {
		
		System.out.println("from keyPressed method:"+key.getKeyCode());
		

		
	}

	/*
	 * This method runs when a keyboard key is released from a pressed state
	 * aka when you stopped pressing it
	 */
	public void keyReleased(KeyEvent key) {
		
	}

	/**
	 * Runs when a keyboard key is pressed then released
	 */
	public void keyTyped(KeyEvent key) {
		
		
	}
	
	
	/**
	 * The Timer animation calls this method below which calls for a repaint of the JFrame.
	 * Allows for our animation since any changes to states/variables will be reflected
	 * on the screen if those variables are being used for any drawing on the screen.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}
	
	/*
	 * Main method to create a Frame (the GUI that you see)
	 */
	public static void main(String[] arg) {
		new Frame();
	}
	
	
	
	public Frame() {
		JFrame f = new JFrame(title);
		f.setSize(new Dimension(screenWidth, screenHeight));
		f.setBackground(Color.blue);
		f.add(this);
		f.setResizable(false);
		f.setLayout(new GridLayout(1,2));
		f.addMouseListener(this);
		f.addKeyListener(this);
		
		this.mainTheme.play();
		

		
		
		//cursor icon code
		Toolkit toolkit =Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("reticle.png");
		Cursor a = toolkit.createCustomCursor(image, new Point(this.getX(), this.getY()), "");
		this.setCursor(a);
		
		Timer t = new Timer(16, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}

