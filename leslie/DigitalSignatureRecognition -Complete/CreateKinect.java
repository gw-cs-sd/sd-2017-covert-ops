// Leslie Ogu
// CreateKinect class will create the video application for the feed to be run, and all other GUI tasks
// NOTE: Ctrl + Shift + O imports all necessary imports for program

package signing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.ufl.digitalworlds.gui.DWApp;
import edu.ufl.digitalworlds.j4k.J4KSDK;

public class CreateKinect extends DWApp {
	
	DigitalSignature viewkinect;
	VideoPanel mainframe;
	JButton showBorder;
	JPanel rootpanel;
	JPanel options;
	JPanel drawingboard;				// Panel to draw on - white board
	BufferedImage drawing;				// Java class used for its methods that allow you to draw
	BufferedImage cursor;				// Mouse Cursor will be displayed on the screen so the user sees where they are drawing
	PaintBase paint;					// Used to access the paint tool to draw on screen
	
	
	@Override
	public void GUIsetup(JPanel rootpanel) {
		
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0) {
			if(DWApp.showConfirmDialog("Performance Warning", "<html><center><br>WARNING: "
					+ "You are running a 32bit version of Java.<br>This may reduce "
					+ "significantly the performance of this application.<br>It is strongly "
					+ "adviced to exit this program and install a 64bit version of Java."
					+ "<br><br>Do you want to exit now?</center>"))
				System.exit(0);
		}
		
		setLoadingProgress("Loading Kinect Sensor ...", 20);
		try {
			viewkinect = new DigitalSignature();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Initialize the kinect
		viewkinect.setCreateKinect(this);
		
		if(!viewkinect.start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON)) {
			DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect device could "
					+ "not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was "
					+ "succesfully installed on this computer.<br> 2. Check if the Kinect is "
					+ "plugged into a power outlet.<br>3. Check if the Kinect is connected to "
					+ "a USB port of this computer.</center>");
			//System.exit(0); 
		}
		
		// Possible Later Addition - Allow user to click a button to show a bounds within the pane to draw in
		showBorder = new JButton("Display Border");
		showBorder.addActionListener(this);
		
		options = new JPanel(new GridLayout(0,6));
		//options.add(new JLabel("Live Feed: "));
		options.add(showBorder);
		
		// Need this ahead of most code so that you can call the Height and Width methods
		mainframe = new VideoPanel();
		
		// The first two parameter values can be changed later to be whatever the kinect window size is
		// once manually placed values work without errors
		// Using Buffered Image in order to manipulate what is happening on the screen, such as the color
		// at a pixel, or its resolution
		drawing = new BufferedImage(730, 570, BufferedImage.TYPE_INT_ARGB);
		// Create the new cursor
		try {
			cursor = ImageIO.read(new File("C:/Users/young_000/git/j4k2demo/bin/cursor/mouse.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		drawingboard = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {	
				// Protected allows all classes in the package to access
				// The paint component will continue to update the image being drawn with what the user
				// does, such as drawing at a specific area
				super.paintComponent(g);
				// The drawImage method from the Graphics class allows the user to draw on the "image" 
				// which is the screen, and dictates the top-left of the image is at 0, 0
				// drawing is the image you are drawing on
				g.drawImage(drawing, 0, 0, null);
				// Have the cursor follow wherever the user's hand goes (image of cursor, x place of right hand, y place of right
				// hand)
				g.drawImage(cursor, viewkinect.currentX[viewkinect.paintPlace], viewkinect.currentY[viewkinect.paintPlace], null);
				//g.drawImage(cursor, viewkinect.wheresHandX[viewkinect.xMover], viewkinect.wheresHandY[viewkinect.yMover], null);
			}
		};
		
		drawingboard.setPreferredSize(new Dimension(drawing.getWidth(), drawing.getHeight()));
		drawingboard.setBackground(Color.white);
		
		paint = new PaintBase();
		paint.setGraphics((Graphics2D) drawing.getGraphics());
		
		setLoadingProgress("Intitializing Kinect Feed...", 60);
		viewkinect.setViewer(mainframe);
		//rootpanel.add(mainframe, BorderLayout.CENTER);
		rootpanel.add(drawingboard);
		//rootpanel.add(options, BorderLayout.SOUTH);		
	}
	
	public int kinectHeight () {
		return drawingboard.getHeight();
	}
	
	public int kinectWidth () {
		return drawingboard.getWidth();
	}
	
	// These methods will be used to print to the screen and tell the user if they are out of bounds and
	// in which direction - Debugging Methods
	public void tooFarLeft() {
		JPanel message = new JPanel(new GridLayout(0,6));
		// Add the warning if they are out of bounds
		if (viewkinect.west == true) {
			message.add(new JLabel("You're Too Far Left!"));
			rootpanel.add(message, BorderLayout.NORTH);	
		}
		// Remove the message if they are within bounds
		if (viewkinect.west == false) {
			rootpanel.remove(message);	
		}
	}
	
	public void tooFarRight() {
		JPanel message = new JPanel(new GridLayout(0,6));
		if (viewkinect.east == true) {			
			message.add(new JLabel("You're Too Far Right!"));
			rootpanel.add(message, BorderLayout.NORTH);	
		}
		if (viewkinect.east == false) {
			rootpanel.remove(message);	
		}
	}
	
	public void tooFarBelow() {
		JPanel message = new JPanel(new GridLayout(0,6));
		if (viewkinect.south == true) {
			message.add(new JLabel("You're Too Far Down!"));
			rootpanel.add(message, BorderLayout.NORTH);	
		}
		if (viewkinect.south == false) {
			rootpanel.remove(message);	
		}
	}
	
	public void tooFarAbove() {
		JPanel message = new JPanel(new GridLayout(0,6));
		if (viewkinect.north == true) {
			message.add(new JLabel("You're Too Far Up!"));
			rootpanel.add(message, BorderLayout.NORTH);	
		}
		if (viewkinect.north == false) {
			rootpanel.remove(message);	
		}
	}
}
