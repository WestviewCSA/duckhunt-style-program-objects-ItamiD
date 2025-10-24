import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

// The Duck class represents a picture of a duck that can be drawn on the screen.
public class illuminateShip {
    // Instance variables (data that belongs to each Duck object)
    private Image img;  
    
    private Image normal;
    private Image beam;
    
    
    // Stores the picture of the duck
    private AffineTransform tx;      // Used to move (translate) and resize (scale) the image

    // Variables to control the size (scale) of the duck image
    private double scaleX;           
    private double scaleY;           

    // Variables to control the location (x and y position) of the duck
    private double x;                
    private double y;        
    
    //variables for speed
    private int vx;
    private int vy;
    
    //debugging
    public boolean debuging = true;

    // Constructor: runs when you make a new Duck object
    public illuminateShip() {
        normal = getImage("/imgs/alienship normal.gif"); // Load the image file
        
        beam = getImage("/imgs/alienship beam.gif");
        
        img = normal;
        
        tx = AffineTransform.getTranslateInstance(0, 0); // Start with image at (0,0)
        
        // Default values
        scaleX = 1.0;
        scaleY = 1.0;
        x = 0;
        y = 0;

        
        //int the vx and vy
        vx = 3;
        vy = 2;
        
        init(x, y); // Set up the starting location and size
    }
    
    //2nd constructor to initialize location and scale!
    public illuminateShip(int x, int y, int scaleX, int scaleY) {
    	this();
    	this.x 		= x;
    	this.y 		= y;
    	this.scaleX = scaleX;
    	this.scaleY = scaleY;
    	init(x,y);
    }
    
    //2nd constructor to initialize location and scale!
    public illuminateShip(int x, int y, int scaleX, int scaleY, int vx, int vy) {
    	this();
    	this.x 		= x;
    	this.y 		= y;
    	this.scaleX = scaleX;
    	this.scaleY = scaleY;
    	this.vx 	= vx; 
    	this.vy 	= vy;
    	init(x,y);
    }
    
    public void setVelocityVariables(int vx, int vy) {
    	this.vx = vx;
    	this.vy = vy;
    }
    
    
    // Changes the picture to a new image file
    public void changePicture(String imageFileName) {
        try {
            // Load fresh bytes to avoid GIF caching issues
            InputStream is = getClass().getResourceAsStream("/imgs/" + imageFileName);
            if (is != null) {
                byte[] imageBytes = is.readAllBytes();
                ImageIcon icon = new ImageIcon(imageBytes);
                img = icon.getImage(); // Fresh image that will animate from the beginning
            } else {
                System.err.println("Could not find image: " + imageFileName);
                img = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            img = null;
        }

        init(x, y); // keep same location when changing image
    
    }
    
    //update any variables for the object such as x, y, vx, vy
    public void update() {
    	//x position updates based on vx
    	x += vx;
    	y += vy;
    	
    	if(x > 1700) {
    		vx *= -1;//bounce off the right side
    	}
    	if(x < 0) {
    		vx *= -1;//bounce off the right side
    	}
    	if(y < -200) {
    		vy = (int)(Math.random()*7+4);;//bounce off the top side
    		vx = (int)(Math.random()*16-8);
    		x = (int)(Math.random()*1600);
    	}
    	if(vy == 12 && y > 640) {
    	y = -200;
    	x = (int)(Math.random()*1600);
    	vx = (int)(Math.random()*16-8);
    	vy = (int)(Math.random()*7+4);
		changePicture("alienship normal.gif");
    	} else if(y>640) {
    	changePicture("alienship beam.gif");

    	
    	changePicture("alienship normal.gif");
    	vy = -(int)(Math.random()*7+4);
    	}


    }
    
    
    
    // Draws the duck on the screen
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;   // Graphics2D lets us draw images
        g2.drawImage(img, tx, null);      // Actually draw the duck image
        update();
        init(x,y);
        
        //create great hit box
        g.setColor(Color.green);
        g.drawRect((int)x, (int)y, 400, 200);
    }
    
    // Setup method: places the duck at (a, b) and scales it
    private void init(double a, double b) {
        tx.setToTranslation(a, b);        // Move the image to position (a, b)
        tx.scale(scaleX, scaleY);         // Resize the image using the scale variables
    }

    // Loads an image from the given file path
    private Image getImage(String path) {
        Image tempImage = null;
        try {
            URL imageURL = illuminateShip.class.getResource(path);
            tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    // NEW: Method to set scale
    public void setScale(double sx, double sy) {
        scaleX = sx;
        scaleY = sy;
        init(x, y);  // Keep current location
    }

    // NEW: Method to set location
    public void setLocation(double newX, double newY) {
        x = newX;
        y = newY;
        init(x, y);  // Keep current scale
 
    }
    
    //Collision and collision logic
    public boolean checkCollision (int mX, int mY) {
    	
    	Rectangle mouse = new Rectangle(mX, mY, 50, 50);
    	
    	//represent this object as a Rectangle
    	Rectangle thisObject = new Rectangle((int)x, (int) y, 400, 200);
    	
    	if(mouse.intersects(thisObject)) {
    		
    		//logic if colliding
    		vy = 12; // all y - gravity
    		changePicture("alienship explosion.gif");
    		return true;
    		

    		
    		
    		
    	}else {
    		
    		return false;
    	}
    }
    
    
    
    
    
    
}
