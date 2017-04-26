// Leslie Ogu
// Digital Signature Class will Run the Signature Signing, Save the necessary ARFF Files, and Send them to WEKA
// The program works by using the left hand to determine if the user wants to draw or not (if the left
// is held forward, it means the user would like to draw). This is handled within the Signature class and
// the GUI actions are handled in the CreateKinect class. After the coordinates are saved, they are offset so that each signature
// run starts at the same spot. There will be two ways of possibly feeding data to WEKA for machine learning:
// 1) Giving it a features file with data such as signature width, height, center X and Y values, and possibly more such as where
// letters curve
// 2) Feeding it an entire signature transformed to fit a 400 x 300 scale
// Or 3) A possible combination of the two

package signing;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DigitalSignature extends Signature {
	
	static CreateKinect mainkinect;
	int xdistfromtip = 8;				// These two variables will dictate how far from the center the tip
	int ydistfromtip = 24;				// of the mouse is - will be our hand in this case
	
	static int[] copyCoorX;
	static int[] copyCoorY;
	static int[] transformedX;			// These arrays will hold the new coordinates of the transformed data
	static int[] transformedY;
	static int biggestX = 0;			// Biggest X and Y Values in signature
	static int biggestY = 0;
	
	static int startX = 1000;			// These variables will hold the bounds of each signature
	static int farthestX = 0;
	static int highestY = 0;
	static int lowestY = 1000;
	static int sigWidth = 0;			// These next variables will hold the width, height, and center location of each signature
	static int sigHeight = 0;
	static int sigCenterX = 0;
	static int sigCenterY = 0;
	
	static FileWriter features;			// This file writers will save the features of each run to an arff file
	static FileWriter tester;
	static String featlocation;			// These next two variables will hold the locations of the features file and testing file
	static String testfeatures;
	static int command = 0;				// Will hold the user's options when the main program is run
	static String user;					// This variable will hold the user's name so we know what to save the file as
	
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
	
	// We need to copy the coordinates into a new array of the exact size of the signature so there are no extra 0s at the end
	public static void copyCoordinates() {
		// Copy the coordinates of the old array into a new array of exact size
		copyCoorX = new int[countSize];
		copyCoorY = new int[countSize];
		
		for (int k = 0 ; k < copyCoorX.length ; k++) {
			copyCoorX[k] = visitedX[k];
			copyCoorY[k] = visitedY[k];
		}
		
		// Debugger
		// System.out.println("Visited Array has been Copied");
		// System.out.println("");
	}
	
	// This method saves the data from the signature run into an ARFF file to be fed into WEKA
	public static void saveFeatures (int width, int height, int centerX, int centerY) throws IOException {
		
		// We use this Path variable to do a check to see if the filename already exists in the directory
		Path sigfeatures = Paths.get(featlocation);
		
		// New feature information from subsequent runs will be added to the end of the feature file that is used for training
		if (Files.exists(sigfeatures)) {
			// Write additional information to that file
			features = new FileWriter(featlocation, true);
			
			features.write(width + ", " + height + ", " + centerX + ", " + centerY + ", ");
			
			// System.out.println("More features have been written to the features file.");
			// System.out.println("");
			
		} else {
			// Create the file and save it to the location you dictated above
			features = new FileWriter(featlocation);
			
			// Give the file the ARFF syntax
			features.write("@RELATION featuresToCompare" + "\n");
			features.write("\n");
			features.write("@ATTRIBUTE width NUMERIC" + "\n");
			features.write("@ATTRIBUTE height NUMERIC" + "\n");
			features.write("@ATTRIBUTE centerx NUMERIC" + "\n");
			features.write("@ATTRIBUTE centery NUMERIC" + "\n");
			features.write("@ATTRIBUTE 1cellSlopes NUMERIC" + "\n");		// The next 100 variables for the ARFF file will represent
			features.write("@ATTRIBUTE 2cellSlopes NUMERIC" + "\n");		// the 100 cells the drawing pane is divided into and the
			features.write("@ATTRIBUTE 3cellSlopes NUMERIC" + "\n");		// averages of the slopes within them
			features.write("@ATTRIBUTE 4cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 5cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 6cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 7cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 8cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 9cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 10cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 11cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 12cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 13cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 14cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 15cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 16cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 17cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 18cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 19cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 20cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 21cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 22cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 23cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 24cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 25cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 26cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 27cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 28cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 29cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 30cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 31cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 32cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 33cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 34cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 35cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 36cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 37cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 38cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 39cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 40cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 41cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 42cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 43cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 44cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 45cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 46cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 47cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 48cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 49cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 50cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 51cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 52cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 53cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 54cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 55cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 56cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 57cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 58cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 59cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 60cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 61cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 62cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 63cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 64cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 65cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 66cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 67cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 68cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 69cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 70cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 71cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 72cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 73cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 74cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 75cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 76cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 77cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 78cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 79cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 80cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 81cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 82cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 83cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 84cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 85cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 86cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 87cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 88cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 89cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 90cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 91cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 92cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 93cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 94cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 95cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 96cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 97cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 98cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 99cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE 100cellSlopes NUMERIC" + "\n");
			features.write("@ATTRIBUTE class {features, vectors}" + "\n");
			features.write("\n");
			features.write("@DATA" + "\n");
			
			// Write the width, height, centerX, and centerY of the signature to the features file
			features.write(width + ", " + height + ", " + centerX + ", " + centerY + ",");
			
			//System.out.println("New Features File has been created and basic data has been written to it.");
			features.close();
		}
		
	}
	
	public static void saveVector (int[] slopesData) throws IOException {
		
		Path sigfeatures = Paths.get(featlocation);
		
		// New slopes data from each subsequent run will be added as a new line to the arff features file
		if (Files.exists(sigfeatures)) {
			// Write additional information to that file
			features = new FileWriter(featlocation, true);
			
			for (int i = 0 ; i < slopesData.length ; i++) {
				if (i == 0) {
					features.write(slopesData[i] + ", ");
				} else if (i == slopesData.length - 1) {
					features.write(slopesData[i] + ", features" + "\n");
				} else {
					features.write(slopesData[i] + ", ");
				}
			}
			
			// System.out.println("Vector data has been written to file!");
			System.out.println("");
			
			features.close();
		} else {
			System.out.println("We can't find that file! The program must not have received the user's other features ...");
		}
		
	}
	
	// This method will create the test file to attempt to authorize with the trained data
	public static void saveTestFile (int width, int height, int centerX, int centerY) throws IOException {
		
		testfeatures = "tester.arff";	
		String testlocation = "C:/Users/young_000/Documents/lesliesfeatures" + "/" + testfeatures;
		tester = new FileWriter(testlocation);
		
		// Give the file the ARFF syntax
		tester.write("@RELATION featuresLeslie" + "\n");
		tester.write("\n");
		tester.write("@ATTRIBUTE width NUMERIC" + "\n");
		tester.write("@ATTRIBUTE height NUMERIC" + "\n");
		tester.write("@ATTRIBUTE centerx NUMERIC" + "\n");
		tester.write("@ATTRIBUTE centery NUMERIC" + "\n");
		tester.write("@ATTRIBUTE class {features, vectors}" + "\n");
		tester.write("\n");
		tester.write("@DATA" + "\n");
		System.out.println("The name of the file: " + testfeatures);
		
		tester.write(width + ", " + height + ", " + centerX + ", " + centerY + ", features" + "\n");
		
		System.out.println("New Test File Created");
		System.out.println("");
		
		tester.close();
	}
	
	// We want to find the left-most and right-most value of X, and the highest and lowest points of Y
	// The left-most value of X and lowest point of Y will serve as the left and top bounds of the signature	
	public static void extractFeatures(int[] xValues, int[] yValues) throws IOException {
		
		for (int j = 0 ; j < xValues.length ; j++) {
			// Find the left-most value of X
			if (xValues[j] < startX) {
				startX = xValues[j];
			}
			// Find the right-most value of X
			if (xValues[j] > farthestX) {
				farthestX = xValues[j];
			}
		}
		
		for (int m = 0 ; m < yValues.length ; m++) {
			// Find the highest point of Y
			if (yValues[m] > highestY) {
				highestY = yValues[m];
			}
			// Find the lowest point of Y - the lowest value will be towards the bottom since it increases as you move south
			if (yValues[m] < lowestY) {
				lowestY = yValues[m];
			}
		}
		
		// System.out.println("The start of X is: " + startX + ", the end of X is: " + farthestX + ", the start of Y is: " + lowestY
		//		+ ", and the end of Y is: " + highestY);
		
		// Next, we want to find the width and height of the entire signature
		sigWidth = farthestX - startX;
		sigHeight = highestY - lowestY;
		// System.out.println("The width of the signature is: " + sigWidth + " and the height of the signature is: " + sigHeight);
		
		// Then, we will find the center point of the signature
		sigCenterX = sigWidth / 2;
		sigCenterY = sigHeight / 2;
		// System.out.println("The center of the signature has a coordinate of: (" + sigCenterX + ", " + sigCenterY + ")");
		
		// Save a file that contains these specific features - More defining features to be added later
		if (command == 1) {
			saveFeatures(sigWidth, sigHeight, sigCenterX, sigCenterY);
		} else if (command == 2) {
			saveTestFile(sigWidth, sigHeight, sigCenterX, sigCenterY);
		}
		
		// Now we want to add the vector data into a new line for that same signature
		createVector(xValues, yValues);
	}
	
	// This method will give the features file more data by dividing the drawing pad into a coordinate plan and taking the
	// average slopes of each line segment within a cell. That number will be added as a separate class for the ARFF file - vectors
	public static void createVector (int[] xValues, int[] yValues) throws IOException {
		// Size of Frame: 730 (Multiples: 10, 73, 146) x 570 (10, 15, 19, 30, 38, 57, 95, 114) (width x height)
		// Number of Cells: 10 (movements across x) * 10 (movements down the y) = 100 (size of slope averages array)
		// Cell Range: 73 in width, 57 in height
		
		int[] cellSlopeAverages = new int[100];
		int slopespercell = 0;
		int cellcounter = 0;
		int xPrev = 0;
		int yPrev = 0;
		int xMover = 73;
		int yMover = 57;
		
		// What we want to do is take the slopes of every cell and get the average for all of them
		// Then place that average within the array for the cell index (corresponding with its cell number)
		while (cellcounter < 100) {
			// The next line was used for debugging
			// System.out.println("Cell #" + cellcounter + " with the max of X: " + xMover + " and Y: " + yMover);
			for (int w = 0 ; w < xValues.length ; w++) {
				if (w == 0) {
					// Just move to the next coordinate
				} else if ((xValues[w] <= xMover) && (xValues[w] >= xPrev)) { 			// if the first x value is within the x of the cell
					if ((xValues[w-1] <= xMover) && (xValues[w-1] >= xPrev)) { 			// check the one before it to make sure it is in the cell, too
						if ((yValues[w] <= yMover) && (yValues[w] >= yPrev)) {			// then check that the y value is in the cell as well
							if ((yValues[w-1] <= yMover) && (yValues[w-1] >= yPrev)) {	// and that the y value before it is within the cell as well
								if (xValues[w] == xValues[w-1]) {						// If the x values are the same
									// Next two lines are for debugging purposes
									// System.out.println("X's being compared: xValue2 & xValue1 - " + xValues[w] + " & " + xValues[w-1]);
									// System.out.println("y's being compared: yValue2 & yValue1 - " + yValues[w] + " & " + yValues[w-1]);
									int slope = 0;										// The slope will just be 0 since the x values equal each other
									cellSlopeAverages[cellcounter] = cellSlopeAverages[cellcounter] + slope;
									slopespercell++;
								} else {
									// Next two lines are for debugging purposes
									// System.out.println("X's being compared: xValue2 & xValue1 - " + xValues[w] + " & " + xValues[w-1]);
									// System.out.println("y's being compared: yValue2 & yValue1 - " + yValues[w] + " & " + yValues[w-1]);
									// Slope Math = (y2 - y1) / (x2 - x2)
									int slope = ((yValues[w] - yValues[w-1]) / (xValues[w] - xValues[w-1]));
									// Debugging the slope
									// System.out.println("The Slope is: " + slope);
									cellSlopeAverages[cellcounter] = cellSlopeAverages[cellcounter] + slope;
									slopespercell++;
								}
							}
						}
					}
				}
			}
			
			// System.out.println("Number of Slopes in Cell #" + cellcounter + "' is: " + slopespercell);
			// Find the average for the slopes in that cell
			if (slopespercell == 0) {					// To avoid a divide by 0 error
				cellSlopeAverages[cellcounter] = 0;
			} else {									// If divide by 0 isn't a problem, divide the total by the number of slopes
				cellSlopeAverages[cellcounter] = cellSlopeAverages[cellcounter] / slopespercell;
			}
			slopespercell = 0;							// Reset slopes per cell counter
			cellcounter++;								// Increase cell count to the next cell
			xMover = xMover + 73;						// Increment the x and y movers to symbolize shifting to the next cell
			xPrev = xPrev + 73;
			if (xMover > 730) {
				xMover = 73;							// Reset the xMover to 0 once you have reached the end of the drawing pane
				xPrev = 0;
				yMover = yMover + 57;					// Move y to the next line once we have reached the end of the previous row
				yPrev = yPrev + 57;
			}
		}
		
		// Now, send the vector array to the save vector method so that the vector can be written to the ARFF File
		saveVector(cellSlopeAverages);
	}
	 
	// This method will feed the signature data into WEKA to be evaluated
	public static void wekaModelEval () throws Exception {
		// This method is where the magic happens - training the system using the data from the signature runs and then
		// testing a new signature with the trained set
		
		// Load the dataset file (used for the training)
		DataSource source = new DataSource(featlocation);
		Instances traineddataset = source.getDataSet();
		// Set class index to the last attribute
		// Lets the program know the class is the last column in the set of attributes (-1 because index starts at 0
		traineddataset.setClassIndex(traineddataset.numAttributes()-1); //
		
		J48 tree = new J48();
		tree.buildClassifier(traineddataset);
		
		// Evaluation - Class for evaluating machine learning models.
		Evaluation eval = new Evaluation(traineddataset);
		
		// Test data set for evaluation
		DataSource source1 = new DataSource("C:/Users/young_000/Documents/lesliesfeatures/tester.arff");
		Instances testdataset = source.getDataSet();
		testdataset.setClassIndex(testdataset.numAttributes()-1);
		
		// Evaluate the model using the decision tree and data provided at the top from iris
		eval.evaluateModel(tree, testdataset);
		
		System.out.println(eval.toSummaryString("Evaluation results:\n", false));
		
		// Statistics that can be used for the percentage of correct, incorrect, error rate, and more
		
		System.out.println("Correct % = "+eval.pctCorrect());
		System.out.println("Incorrect % = "+eval.pctIncorrect());
		System.out.println("AUC = "+eval.areaUnderROC(1));
		System.out.println("kappa = "+eval.kappa());
		System.out.println("MAE = "+eval.meanAbsoluteError());
		System.out.println("RMSE = "+eval.rootMeanSquaredError());
		System.out.println("RAE = "+eval.relativeAbsoluteError());
		System.out.println("RRSE = "+eval.rootRelativeSquaredError());
		System.out.println("Precision = "+eval.precision(1));
		System.out.println("Recall = "+eval.recall(1));
		System.out.println("fMeasure = "+eval.fMeasure(1));
		System.out.println("Error Rate = "+eval.errorRate());
		System.out.println("");
	    //the confusion matrix
		//System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));
		
		if (eval.pctCorrect() >= 80.0) {
			System.out.println("Authentication Confirmed. Welcome, " + user + "!");
		} else {
			System.out.println("IMPOSTER!!! You aren't " + user + "! Calling Security Now ....");
		}
	}
	
	public static void closeText() throws IOException {
		fw.close();
		
		// Debugger
		// System.out.println("Everything is written!");
	}
	
	public static void main(String[] args) throws Exception {
		
		// Java version check
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0) {
			System.out.println("WARNING: You are running a 32bit version of Java.");
			System.out.println("This may reduce significantly the performance of "
					+ "this application.");
			System.out.println("It is strongly adviced to exit this program and "
					+ "install a 64bit version of Java.\n");
		}
		
		// Revisit Later - If you want your own icon, you can change null to an input stream to be something you want
		
		Scanner scans = new Scanner(System.in);
		System.out.println("This is the second authenetication step of Covert Ops.");
		
		System.out.println("First, what is your name?");
		user = scans.nextLine();
		// Set the location of where all the user's features will be stored
		featlocation = "C:/Users/young_000/Documents/lesliesfeatures/" + user + "sfeatures.arff";
		
		// The while statement doesn't seem to work with the Kinect as the Kinect must close and restart itself.
		// Once the Kinect closes, it seems to end the entire program with it
		while (command != 3) {	
			System.out.println("Now, will you be 1) Training (type 1) 2) Testing (press 2) or 3) Quitting the program");
			command = scans.nextInt();
			
			if (command == 1) {
				// System.out.println("The system will now run 25x to train Covert Ops with your signature.");
				// Running this 25x in a for loop seems to throw errors because of the Kinect, so you will have to keep training manually
				// for (int c = 0 ; c < 25 ; c++) {
				mainkinect.createMainFrame("Digital Signature Authentication");
				mainkinect = new CreateKinect();
				mainkinect.app = mainkinect;
				mainkinect.setFrameSize(730, 570, null);
				//DigitalSignature testkinect = new DigitalSignature();
				mainkinect.start();
				System.out.println();
				
				// Give the user 15 seconds to draw a signature
				try {Thread.sleep(15000);} catch (InterruptedException e) {}
				copyCoordinates();
				closeText();
				extractFeatures(copyCoorX, copyCoorY);
				// }
				mainkinect.stop();
			} else if (command == 2) {
				mainkinect.createMainFrame("Digital Signature Authentication");
				mainkinect = new CreateKinect();
				mainkinect.app = mainkinect;
				mainkinect.setFrameSize(730, 570, null);
				mainkinect.start();
				System.out.println();
				
				try {Thread.sleep(15000);} catch (InterruptedException e) {}
				copyCoordinates();
				closeText();
				extractFeatures(copyCoorX, copyCoorY);
				wekaModelEval();
				mainkinect.stop();
			} else if (command == 3) {
				System.out.println("Thanks for using Covert Ops! You can try using it again later.");
				break;
			} else {
				System.out.println("You used an unrecognized command... Try again.");
			}
		}
	}
}





//testkinect.start(Kinect.DEPTH | Kinect.COLOR | Kinect.SKELETON | Kinect.XYZ | Kinect.PLAYER_INDEX);

/*
// Original Size of Signature Display - 730 x 570 (width x height)
// This method will use the farthest x and y value after the coordinates are offset to start
// Then, it will transform the data so that anything outside of a 400 x 300 range will be shrunk to be inside. 
// ** Possible Add-On: Increasing size of signatures below 300 x 200 **
public static void transformData(int[] xValues, int[] yValues) throws IOException {
	
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
	int action = 3;
	
	if ((biggestX > 400) && (biggestY <= 300)) {
		// Case 0: X is outside of bounds
		action = 0;
	} else if ((biggestX <= 400) && (biggestY > 300)) {
		// Case 1: Y is outside of bounds
		action = 1;
	} else if ((biggestX > 400) && (biggestY > 300)) {
		// Case 2: X and Y are out of bounds
		action = 2;
	} else {
		// Case 3: Nothing to Change because it's in bounds
		action = 3;
	}
	
	System.out.println("The values to transform by will be: " + biggestX + " and " + biggestY + " and action #: " + action);
	
	// Initialize transformation array
	transformedX = new int[xValues.length];
	transformedY = new int[yValues.length];
	
	switch(action) {
		case 0:
			int difference = biggestX - 400;
			System.out.println("The difference is: " + difference);
			// Why isn't it getting into the for loops?????
			// Why are things not being saved to different folders??
			for (int j = 0 ; j < xValues[j] ; j++) {
				transformedX[j] = xValues[j] - difference;	// Transform the X values so that it shifts the signature to the left
				System.out.println("Norm. Coord. X: " + xValues[j] + " && " + "Transform X: " + transformedX[j]);
			}
			System.out.println("X's have been corrected!");
			break;
		case 1:
			difference = biggestY - 300;
			System.out.println("The difference is: " + difference);
			for (int j = 0 ; j < yValues[j] ; j++) {
				transformedY[j] = yValues[j] - difference;	// Transform the Y values so that it shifts the signature upwards
				System.out.println("Norm. Coord. Y: " + yValues[j] + " && " + "Transform y: " + transformedY[j]);
			}
			System.out.println("Y's have been corrected!");
			break;
		case 2:
			int differenceX = biggestX - 400;
			int differenceY = biggestY - 300;
			System.out.println("The X difference is: " + differenceX + " and the Y difference: " + differenceY);
			for (int k = 0 ; k < xValues[k] ; k++) {
				transformedX[k] = xValues[k] - differenceX;		// Transform the X and Y values to move it up and to the left
				transformedY[k] = yValues[k] - differenceY;
				System.out.println("Norm. Coord. X: " + xValues[k] +", Norm. Coord. Y: " + yValues[k] + " && " + "Transform X: "
						+ transformedX[k] + ", Transform Y: " + transformedY[k]);
			}
			System.out.println("X's and Y's have been corrected!");
			break;
		default:
			System.out.println("Nothing was done");
	}
	
	saveTransforms(transformedX, transformedY);
	
	System.out.println("All offset coordinates have been transformed!");
}
	static int[] offsetCoorX;			// Value that will be used to offset values (shift positioning
	static int[] offsetCoorY;			// so signature starts at(0, 0)
	
	// We want to offset the values so that the first point of the signature technically is the "start" of the signature
	// We will offset it with the lowest values of x and y so it will be like we are shifting it to the top left corner
	// Then, we will offset the rest of the coordinates to match
	int offsetValX = startX;
	int offsetValY = lowestY;
	
	// // Initialize offset array
	offsetCoorX = new int[xValues.length];
	offsetCoorY = new int[yValues.length];
	
	// Subtract the offset from the coordinate to "shift" the signature to start at (0, 0)
	for (int i = 0 ; i < xValues.length ; i++) {
		offsetCoorX[i] = xValues[i] - offsetValX;
		offsetCoorY[i] = yValues[i] - offsetValY;
		//System.out.println("Norm. Coord. X: " + xValues[i] +", Norm. Coord. Y: " + yValues[i] + " && " + "Offset X: "
		//+ offsetCoorX[i] + ", Offset Y: " + offsetCoorY[i]);
	}	

	System.out.println("All coordinates have been offset!");
	System.out.println("");
*/