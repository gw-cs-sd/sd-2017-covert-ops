// Leslie Ogu
// Signature Class to Handle Skeleton Events, Tracking, Signing, and "Mouse" Events

package signing;
/*
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Date;
import javax.swing.JPanel;
*/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.MouseEvent;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
//import j4kdemo.kinectviewerapp.Kinect;

/*
 * Copyright 2011-2014, Digital Worlds Institute, University of 
 * Florida, Angelos Barmpoutis.
 * All rights reserved.
 *
 * When this program is used for academic or research purposes, 
 * please cite the following article that introduced this Java library: 
 * 
 * A. Barmpoutis. "Tensor Body: Real-time Reconstruction of the Human Body 
 * and Avatar Synthesis from RGB-D', IEEE Transactions on Cybernetics, 
 * October 2013, Vol. 43(5), Pages: 1347-1356. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain this copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce this
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class Signature extends J4KSDK {
	
	VideoPanel viewer;
	Skeleton skel;
	CreateKinect mainkinect;
	int skeletoncount = 0;
	int tracker = 0;					// Will hold the index of the tracked skeleton
	
	int thisX = 0;
	int thisY = 0;
	int[] oldX = new int[10000];		// These two arrays are necessary to hold the coordinates of where the 
	int[] oldY = new int[10000];		// user's finger previously was in order to draw continuously w/o spaces
	int[] lastX = new int[10000];		// These two arrays will be keep track of the last spot the user was
	int[] lastY = new int[10000];		// which will help with the comparison part later
	int[] currentX = new int[10000];	// These two arrays will hold the current position of the cursor
	int[] currentY = new int[10000];
	static int[] visitedX = new int [10000];	// This will be used for comparisons that happen later to 
												// compare
	static int[] visitedY = new int[10000];		// signatures
	
	int paintPlace = 0;					// This int will dictate what step in the drawing the user is using the
										// index of the array
	int sigcoordinates = 0;				// Index for what places the user signs on the screen
	
	// Is the user drawing or not
	boolean isDrawing = false;
		
	// These booleans will be used to notify the user if they are drawing or outside of the bounds
	boolean east, west, north, south;
	
	// Minimum distance between body parts to let program know to initiate drawing
	public double DRAW_TRESHOLD = 0.4;
	
	// Values used for mouse movement corrections
	double headOffsetX = -0.3;
	double headOffsetY = 0.45;
	
	// X and Y drawing sensitivity (inverted - lower values means higher sensitivity)
	public static int X_SENSITIVITY = 500;
	public static int Y_SENSITIVITY = 600;
	
	// We will save all coordinates to a file
	public static FileWriter fw;
	
	// We will concatenate a number to the end of the file name so that each file will be saved
	// in a different file - For now, it's a random number
	//int newFile = (int) (Math.random() * 50 + 1);
	int fileCounter = 0;
	
	String testruns = "leslie";
	
	public Signature() throws IOException{
		super();
		
		// Initializing the necessary Kinect components
		skel = new Skeleton();
		viewer = new VideoPanel();
		east = false;
		west = false;
		north = false;
		south = false;
		
		// Next lines are used to save each run to different files - Want it to be incremented
		String addToEnd = Integer.toString(fileCounter);
		testruns = testruns + addToEnd + ".txt";
		
		// Create a string that will be the path with the possible text file name appended to it so we can check if it already
		// exists
		String siglocation = "C:/Users/young_000/git/j4k2demo" + "/" + testruns;
		Path signatures = Paths.get(siglocation);
		
		// Run a check to see if the filename already exists. If it does, increment the count and append the new number
		while (Files.exists(signatures)) {
			fileCounter++;
			addToEnd = Integer.toString(fileCounter);
			testruns = "leslie" + addToEnd + ".txt";
			siglocation = "C:/Users/young_000/git/j4k2demo" + "/" + testruns;
			signatures = Paths.get(siglocation);
		}
		
		// Finally create the test file with
		fw = new FileWriter(testruns);
		System.out.println("The name of the file: " + testruns);
	}
	
	// Tell the program that the viewer should be the one created in Signature class
	public void setViewer(VideoPanel viewer){
		this.viewer = viewer;
	}
	
	// Initialize the CreateKinect so you can get the height, width, and other values from the video feed
	public void setCreateKinect (CreateKinect kinect) {
		this.mainkinect = kinect;
	}
	
	// Use these methods to let the program know if a user is out of bounds and so the program
	// can add JLabels to print to the screen to let the user know this
	public void setNorth(boolean change) {
		north = change;
	}
	
	public void setSouth(boolean change) {
		south = change;
	}
	
	public void setEast(boolean change) {
		east = change;
	}
	
	public void setWest(boolean change) {
		west = change;
	}
	
	@Override
	public void onColorFrameEvent(byte[] color_frame) {
		// Will determine later if this is necessary
		// Probably for coding the paint library
		if(viewer==null || viewer.videoTexture==null) 
			return;
		viewer.videoTexture.update(getColorWidth(), getColorHeight(), color_frame);
	}

	@Override
	public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
		// Check if code is necessary
	}
	
	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] joint_positions, 
			float[] joint_orientations, byte[] joint_states) {
		// Debugger
		// System.out.println("Skeleton found!");
		
		// If I want to have multiple users, this can be used later
		// skel.setPlayerID(skeletoncount);
		// skeletoncount++;
		
		// Determine which skeleton is being tracked
		for (int i = 0 ; i < skeleton_tracked.length ; i++) {
			if (skeleton_tracked[i] == true) {
				tracker = i;
			}
		}
		
		skel = Skeleton.getSkeleton(tracker, skeleton_tracked, joint_positions, joint_orientations,
				joint_states, J4KSDK.MICROSOFT_KINECT_2);
		
		// Track the skeleton using the tracking method with the right hand and shoulder for drawing,
		// the left hand to determine when to draw, and the head for reference to the left hand
		// true just says there is a skeleton
		if (skeleton_tracked[tracker]) {
			try {
				trackSkeleton(skel.get3DJoint(skel.HAND_RIGHT), skel.get3DJoint(skel.SHOULDER_RIGHT),
						skel.get3DJoint(skel.HAND_LEFT), skel.get3DJoint(skel.HEAD), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* Debugger for hand coordinates
		System.out.println("Skeleton RIGHT HAND X: " + skel.get3DJointX(skel.HAND_RIGHT));
		System.out.println("Skeleton RIGHT HAND Y: " + skel.get3DJointY(skel.HAND_RIGHT));
		System.out.println("Skeleton RIGHT HAND Z: " + skel.get3DJointZ(skel.HAND_RIGHT));
		
		System.out.println("Skeleton LEFT HAND X: " + skel.get3DJointX(skel.HAND_LEFT));
		System.out.println("Skeleton LEFT HAND Y: " + skel.get3DJointY(skel.HAND_LEFT));
		System.out.println("Skeleton LEFT HAND Z: " + skel.get3DJointZ(skel.HAND_LEFT));
		*/
		
		// Conduct Checks to see if the user's right hand (the signing hand) is within bounds
		/* Bounds Being Used for Video Feed
		 	X Right: .55
			X Left: -0.40
			Y Top: 0.32
			Y Bottom: -0.30 */
		float temp = (float) -.30;
		//System.out.println("Temp: " + temp);
		if (Float.compare(skel.get3DJointY(skel.HAND_RIGHT), temp) < 0) {
			//System.out.println("Too Far Below!");
			this.north = true;
			//mainkinect.tooFarBelow();
		} else {
			this.north = false;
		}
		
		float temp2 = (float) 0.35;
		//System.out.println("Temp2: " + temp2);
		if (Float.compare(skel.get3DJointY(skel.HAND_RIGHT), temp2) > 0) {
			//System.out.println("Too Far Above!");
			this.south = true;
			//mainkinect.tooFarAbove();
		} else {
			this.south = false;
		}
		
		float temp3 = (float) -0.25;
		//System.out.println("Temp3: " + temp3);
		if (Float.compare(skel.get3DJointX(skel.HAND_RIGHT), temp3) < 0) {
			//System.out.println("Too Far Left!");
			this.west = true;
			//mainkinect.tooFarRight();
		} else {
			this.west = false;
		}
		
		float temp4 = (float) 0.45;
		//System.out.println("Temp4: " + temp4);
		if (Float.compare(skel.get3DJointX(skel.HAND_RIGHT), temp4) > 0) {
			//System.out.println("Too Far Right!");
			this.east = true;
			//mainkinect.tooFarRight();
		} else {
			this.east = false;
		}
		
	}
	
	// Mouse Click Method - Simulating a mouse click by using the user's hand	
	public void dispatchMouseClick(){		
		// 1st Parameter - On which panel does the event happen
		// 3rd Parameter - 0 because only one user is drawing
		// How does the user know where on the screen they are drawing? - Come Back To
		MouseEvent e = new MouseEvent(mainkinect, MouseEvent.MOUSE_PRESSED, 0, 0, currentX[paintPlace], 
				currentY[paintPlace], 1, false);
		
		try {
			mainkinect.dispatchEvent(e);
		} catch (Exception m) {
			
		}
		
		oldX[paintPlace] = lastX[paintPlace] = currentX[paintPlace];
		oldY[paintPlace] = lastY[paintPlace] = currentY[paintPlace];
	}
	
	// Mouse Drag Method - Simulating a user dragging the mouse while clicked
	// Will update the last x and y coordinate drawn on
	public void dispatchMouseDrag() throws IOException{
		
		// This piece of the code is what allows the user to draw on the screen
		// Calling drawCenteredLine so that the user is able to draw from exactly where they click
		mainkinect.paint.drawCenteredLine(lastX[paintPlace], lastY[paintPlace], 
				currentX[paintPlace], currentY[paintPlace]);	
		
		// Each time the user draws, update the screen to reflect that
		mainkinect.drawingboard.repaint();
		
		// SAVES COORDINATES OF THE LAST PLACE ON THE SCREEN THE USER WAS DRAWING
		// LAST COORDINATE NOW BECOMES THE NEW CURRENT ONE
		// The first and last point is necessary in order to continue drawing something without empty spaces
		// or gaps due to pauses because the program needs to know from where to continue drawing
		lastX[paintPlace] = currentX[paintPlace];
		lastY[paintPlace] = currentY[paintPlace];
		
		// Write those coordinates into the file - X on odd lines, Y on even lines
		fw.write(lastX[paintPlace] + "\n");
		fw.write(lastY[paintPlace] + "\n");
		
		// Will be used to hold the coordinates of where the user draws - May not be needed
		visitedX[sigcoordinates] = lastX[paintPlace];
		visitedY[sigcoordinates] = lastY[paintPlace];
		System.out.println("Signature X: " + visitedX[sigcoordinates] + ", Signature Y: " + visitedY[sigcoordinates]);
		sigcoordinates++;
		
		//System.out.println("X: " + lastX[paintPlace] + ", Y: " + lastY[paintPlace]);
		//fw.close();
	}
		
	// Mouse Release Method - Simulating that the user has released the mouse at the current coordinate
	public void dispatchMouseRelease(){		
		MouseEvent e = new MouseEvent(mainkinect, MouseEvent.MOUSE_RELEASED, 0, 0, currentX[paintPlace], 
				currentY[paintPlace], 1, true);
		mainkinect.dispatchEvent(e);
	}
	
	// Tracking Skeleton Method
	// drawHand = right hand, drawShoulder = right shoulder, controlHand = left hand, deltaJoint = head
	private void trackSkeleton(double [] drawHand, double [] drawShoulder, double [] controlHand, 
			double [] deltaJoint, boolean first) throws IOException {
		
		// index 0 = x; index 1 = y
		this.currentX[paintPlace] = convertX(drawHand[0], deltaJoint[0] - headOffsetX);
		this.currentY[paintPlace] = convertY(drawHand[1], deltaJoint[1] - headOffsetY);
		
		// Debugging the Conversion methods
		// System.out.println("X Conversion: " + currentX[paintPlace]);
		// System.out.println("Y Conversion: " + currentY[paintPlace]);
		
		// Refer to the left hand
		// Program works by calculating the distance of the left hand
		// When the left hand is far enough forward from the head, it activates the DRAW_THRESHOLD variable 
		// that lets the program know you want to draw and the user will be able to draw to the screen
		// Retracting the hand will deactivate the DRAW_THRESHOLD
		// index 2 = z (which will be used to tell how far apart they are)
		double dz = Math.abs(deltaJoint[2] - controlHand[2]);
		// System.out.println("Dz: " + dz);
		
		if (dz >= DRAW_TRESHOLD){
			if (isDrawing){
				this.dispatchMouseDrag();
			} else {
				this.dispatchMouseClick();
				isDrawing = true;
			}				
		} else {
			if (isDrawing){
				isDrawing = false;
				this.dispatchMouseRelease();
			}
		}
		
		//for (int i = 0 ; i < lastX.length ; i++) {
		//	fw.write(lastX[i] + "\n");
		//	fw.write(lastY[i] + "\n");
		//}
	}
	
	// Convert X and Y values from Kinect Feed into Screen Coordinates
	// coordinates are in meters
	// x0 = offset from head / distance to head
	// drawingx = drawing hand (right hand) x - positive if to right of your head, negative if to the left
	int convertX(double drawingx, double x0){
		int frameWidth = mainkinect.kinectWidth();
		// First, the screen size is mapped to a smaller screen size so user doesn't have to move a lot 
		// to get to the other side of the screen when drawing		
		double coef = 1.0 * frameWidth / X_SENSITIVITY;	
		// Multiplying the right hand offset (distance) from the head by 1000 so it can be used a whole 
		// number (the 1000 value is gotten from trying different values until it worked well)
		drawingx = (drawingx - x0) * 1000;
		// Offset the position by half of the new, smaller, screen and multiply by coefficient to get
		// real screen coordinates from the smaller screen so drawing is accurate
		int x = (int) (coef * (drawingx + X_SENSITIVITY / 2));
		// Store that converted value
		x = Math.max(0, Math.min(frameWidth, x));
		return x;
	}
	
	int convertY(double drawingy, double y0){
		int frameHeight = mainkinect.kinectHeight();
		double coef = 1.0 * frameHeight / Y_SENSITIVITY;	
		drawingy = (drawingy - y0) * 1000;
		int y = (int) (coef * (drawingy + Y_SENSITIVITY / 2));
		y = frameHeight - Math.max(0, Math.min(frameHeight, y));
		return y;
	}
	
	// Possibility: Adding Color Option for Drawing
	
}
