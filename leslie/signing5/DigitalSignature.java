// Leslie Ogu
// Digital Signature Class will Run the Signature Signing
// The program works by using the left hand to determine if the user wants to draw or not (if the left
// is held forward, it means the user would like to draw). This is handled within the Signature class and
// the GUI actions are handled in the CreateKinect class.

package signing;

import java.io.FileWriter;
import java.io.IOException;

public class DigitalSignature extends Signature {
	
	static CreateKinect mainkinect;
	int xdistfromtip = 8;									// These two variables will dictate how far from the center the tip
	int ydistfromtip = 24;									// of the mouse is - will be our hand in this case
	static int[] offsetCoorX = new int[visitedX.length];;	// Value that will be used to offset values (shift positioning
	static int[] offsetCoorY = new int[visitedY.length];;	// so signature starts at(0, 0)
	static int biggestX = 0;										// Biggest X and Y Values in signature
	static int biggestY = 0;
	
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
	
	public static void offsetSignature(int[] xValues, int[] yValues) {
		// We want to offset the values so that the first point of the signature technically is the "start" of the signature
		// Then, we will offset the rest of the coordinates to match
		int offsetValX = visitedX[0];
		int offsetValY = visitedY[0];
		
		// This array will hold the offset values for X and Y
		// offsetCoorX = new int[visitedX.length];
		// offsetCoorY = new int[visitedY.length];
		
		// Subtract the offset from the coordinate to "shift" the signature to start at (0, 0)
		for (int i = 0 ; i < xValues.length ; i++) {
			
			// Step / Try 1 - Offset the coordinates and allow negative values
			xValues[i] = visitedX[i] - offsetValX;
			yValues[i] = visitedY[i] - offsetValY;
			System.out.println("Norm. Coord. X: " + visitedX[i] +", Norm. Coord. Y: " + visitedY[i] + " && " + "Offset X: "
			+ xValues[i] + ", Offset Y: " + yValues[i]);
			
			// Step / Test 2 - Offset and do not allow negatives; let anything in the signature <0 be 0
			//if (visitedX[i] - offsetValX <= 0) {
			//	offsetCoorX[i] = 0;
			//	
			//} else {
			//}
			
			// Step / Try 3 - Find the lowest, highest, leftmost, and rightmost coordinates, and use those to scale the overall
			// area of the drawing space to be more precise
			// Maybe then we can offset from there so that they start in similar spots
		}	
		
		System.out.println("All coordinates have been offset!");
	}
	
	public static void transformData(int[] xValues, int[] yValues) {
		// Original Size of Signature Display - 730 x 570 (width x height)
		// This method will find the farthest x and y value after the coordinates are offset to start at the same
		// spot on the screen ("0, 0"). Then, it will transform the data so that anything outside of a 400 x 300
		// range will be shrunk to be inside. 
		// ** Possible Add-On: Increasing size of signatures below 300 x 200 **
		
		// Find the largest X and Y values
		for (int k = 0 ; k < xValues.length ; k++) {
			if (xValues[k] > biggestX) {
				biggestX = xValues[k];
			}
			if (yValues[k] > biggestY) {
				biggestY = yValues[k];
			}
		}
		
		// Determine what transformation steps need to be taken by figuring out which sides of the signature are out of bounds
		int action = 0;
		
		if (biggestX > 400 && biggestY <= 300) {
			// Case 1: X is farther than bounds
			action = 1;
		} else if (biggestX <= 400 && biggestY > 300) {
			// Case 2: Y is farther than bounds
			action = 2;
		} else if (biggestX > 400 && biggestY > 300) {
			// Case 3: X and Y are farther than bounds
			action = 3;
		} else {
			// Case 4: Nothing to Change because it's in bounds
			action = 0;
		}
		
		switch(action) {
		case 0:
			System.out.println("Nothing to transform.");
			break;
		case 1:
			int difference = biggestX - 400;
			for (int j = 0 ; j < xValues[j] ; j++) {
				xValues[j] = xValues[j] - difference;		// Transform the X values so that it shifts the signature to the left
			}
			System.out.println("X's have been corrected!");
			break;
		case 2:
			difference = biggestY - 300;
			for (int j = 0 ; j < yValues[j] ; j++) {
				yValues[j] = yValues[j] - difference;		// Transform the Y values so that it shifts the signature upwards
			}
			System.out.println("Y's have been corrected!");
			break;
		case 3:
			int differenceX = biggestX - 400;
			int differenceY = biggestY - 300;
			for (int j = 0 ; j < xValues[j] ; j++) {
				xValues[j] = xValues[j] - differenceX;		// Transform the X and Y values to move it up and to the left
				yValues[j] = yValues[j] - differenceY;
			}
			System.out.println("X's and Y's have been corrected!");
			break;
		}
			
		System.out.println("All offset coordinates have been transformed!");
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
		
		System.out.println();
		
		// Give the user 15 seconds to draw a signature
		try {Thread.sleep(15000);} catch (InterruptedException e) {}
		
		closeText();
		mainkinect.stop();
		offsetSignature(offsetCoorX, offsetCoorY);
		transformData(offsetCoorX, offsetCoorY);
		
		// Buffered Reader method to read from the signature files of a person
		// Offset Signatures method goes within Buffered Reader method 
	}
}


//testkinect.start(Kinect.DEPTH | Kinect.COLOR | Kinect.SKELETON | Kinect.XYZ | Kinect.PLAYER_INDEX);
/*
public static void arffCreator () throws IOException {
	// This method will create an ARFF file for the user that the system is being trained for
	FileWriter lesliesAvg = new FileWriter("lesliesAvg.arff");
	
	// Write the format of an ARFF File
	lesliesAvg.write("@RELATION signaturesLes");
	lesliesAvg.write("@ATTRIBUTE xvalue NUMERIC");
	lesliesAvg.write("@ATTRIBUTE yvalue NUMERIC");
	lesliesAvg.write("@ATTRIBUTE class {runs}");
	lesliesAvg.write("@DATA");
	// Add the coordinates per line (x and y values will be on each line following the other (x, y, classname))
	lesliesAvg.write(", runs");
	
}
*/
