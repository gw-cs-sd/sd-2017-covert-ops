// Sandbox.java
import java.util.*;

public class Sandbox{
	
	public static String print(String words){
		System.out.println(words);
		return null;
	}

	public static List<Integer> findIndices(List<Object> list, Object obj){
	// public static List<Integers> findIndices(List<Object> list, Object obj){
		List<Integer> results = new ArrayList<Integer>();

		return results;
		
	}

	public static void main(String args[]){
		List<String> the_list = new ArrayList<String>();
		the_list.add("one");
		the_list.add("two");
		the_list.add("one");
		the_list.add("one");
		the_list.add("two");
		String target = "one";
		print(findIndices(the_list, target));
	}

}