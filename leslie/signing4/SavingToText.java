package signing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SavingToText {

	public static void main(String[] args) throws IOException {
		
		int[] movementArray  = new int[20];
        /*
		String name = "John Doe";
        int age = 44;
        double temp = 26.9;
        FileWriter fw;
        try {
            fw = new FileWriter(new File("mytextfile.txt"));
            fw.write(String.format("My name is %s.",name));
            fw.write(System.lineSeparator()); //new line
            fw.write(String.format("I am %d years old.",age));
            fw.write(System.lineSeparator()); //new line
            fw.write(String.format("Today's temperature is %.2f.",temp));
            fw.write(System.lineSeparator()); //new line
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Done");
*/
		
		FileWriter fw = new FileWriter("test.txt");
	    String strs[] = { "com", "exe", "doc" };

	    for (int i = 0; i < strs.length; i++) {
	      fw.write(strs[i] + "\n");
	    }
	    fw.close();
	    
	    System.out.println("Saved");

	}

}
