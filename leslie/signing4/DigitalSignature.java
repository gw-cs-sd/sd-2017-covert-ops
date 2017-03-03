// Leslie Ogu
// Digital Signature Class will Run the Signature Signing
// The program works by using the left hand to determine if the user wants to draw or not (if the left
// is held forward, it means the user would like to draw). This is handled within the Signature class and
// the GUI actions are handled in the CreateKinect class.

package signing;

import java.io.IOException;

public class DigitalSignature extends Signature {
	
	static CreateKinect mainkinect;
	int xdistfromtip = 8;				// These two variables will dictate how far from the center the tip
	int ydistfromtip = 24;				// of the mouse is - will be our hand in this case
	
	public DigitalSignature() throws IOException{
		super();
	}
	
	public int getHeight () {
		return mainkinect.kinectHeight();
	}
	
	public int getWidth () {
		return mainkinect.kinectWidth();
	}
	
	// Possibility: Cursor Switching Method - different forms/colors when mouse is and isn't drawing
	
	public void GUIclosing() {
		mainkinect.stop();
	}
	
	public static void closeText() throws IOException {
		System.out.println("Everything is written!");
		fw.close();
	}
	
	public static void offsetSignature() {
		// We want to offset the values so that the first point of the signature technically is the "start" of the signature
		// Then, we will offset the rest of the coordinates to match
		// Note: This method will probably read from the average file and compare from there
		int offsetValX = visitedX[0];
		int offsetValY = visitedY[0];
		
		// This array will hold the offset values for X and Y
		int[] offsetCoorX = new int[visitedX.length];
		int[] offsetCoorY = new int[visitedY.length];
		
		/* NEED TO FIGURE OUT WHERE THE 0,0 COORDINATE IS (IS IT TOP LEFT OR BOTTOM LEFT??) */
		
		// Subtract the offset from the coordinate to "shift" the signature to start at (0, 0)
		for (int i = 0 ; i < offsetCoorX.length ; i++) {
			
			// Step / Try 1 - Offset the coordinates and allow negative values
			offsetCoorX[i] = visitedX[i] - offsetValX;
			offsetCoorY[i] = visitedY[i] - offsetValY;
			System.out.println("Norm. Coord. X: " + visitedX[i] +", Norm. Coord. Y: " + visitedY[i] + " && " + "Offset X: "
			+ offsetCoorX[i] + ", Offset Y: " + offsetCoorY[i]);
			
			// Step / Test 2 - Offset and do not allow negatives; let anything in the signature <0 be 0
			//if (visitedX[i] - offsetValX <= 0) {
			//	offsetCoorX[i] = 0;
			//	
			//} else {
			//}
			
			// Steo / Try 3 - Find the lowest, highest, leftmost, and rightmost coordinates, and use those to scale the overall
			// area of the drawing space to be more precise
			// Maybe then we can offset from there so that they start in similar spots
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		
		// Java version check
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0) {
			System.out.println("WARNING: You are running a 32bit version of Java.");
			System.out.println("This may reduce significantly the performance of "
					+ "this application.");
			System.out.println("It is strongly adviced to exit this program and "
					+ "install a 64bit version of Java.\n");
		}
		
		//System.out.println("This program will run for about 30 seconds.");
		
		mainkinect.createMainFrame("Digital Signature Authentication");
		mainkinect = new CreateKinect();
		mainkinect.app = mainkinect;
		// If you want your own icon, you can change null to an input stream to be something you want
		// Revisit Later
		mainkinect.setFrameSize(730, 570, null);
		
		//DigitalSignature testkinect = new DigitalSignature();
		mainkinect.start();
		
		//testkinect.start(J4KSDK.COLOR | J4KSDK.DEPTH | J4KSDK.SKELETON);
		//System.out.println("Start: " + testkinect.start(J4KSDK.COLOR | J4KSDK.DEPTH | J4KSDK.SKELETON));
		//testkinect.showViewerDialog();
		
		System.out.println();
		
		// Give the user 15 seconds to draw a signature
		try {Thread.sleep(15000);} catch (InterruptedException e) {}
		
		mainkinect.stop();
		closeText();
		
		// Buffered Reader method to read from the signature files of a person
		// Offset Signatures method goes within Buffered Reader method 
		
		//testkinect.stop();
	}
}


//testkinect.start(Kinect.DEPTH | Kinect.COLOR | Kinect.SKELETON | Kinect.XYZ | Kinect.PLAYER_INDEX);

