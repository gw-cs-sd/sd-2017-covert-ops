public static List<String> findFlightTimes(List<List> list){
	// the time between "key up" and the next "key down"
	List<String> flightTimes = new ArrayList<String>();
	LinkedList<String> pressedButNotReleasedKeys = new LinkedList<String>();
	LinkedList<String> pressedButNotReleasedTimestamps = new LinkedList<String>();

	String tempAction, tempKey, tempTimestamp, releasedData, releaseTimestampString;
	int index, releaseTimestamp;

	print("\n");
	print("FlightTime: "+list.get(0));
	print("\n");

	// for(int i=0; i<list.get(0).size()-1; i++){
	// 	tempAction = list.get(2).get(i).toString(); // PRESSED or RELEASED
	// 	tempKey = list.get(1).get(i).toString(); // Letter of key pressed/released
	// 	tempTimestamp = list.get(0).get(i).toString(); // Unix epoch timestamp
	// 	if(tempAction.toString().contains("PRESSED")){
	// 		pressedButNotReleasedKeys.push(tempKey);
	// 		pressedButNotReleasedTimestamps.push(tempTimestamp);
	// 	}else if(tempAction.toString().contains("RELEASED")){
	// 		// Key Released
	// 		index = pressedButNotReleasedKeys.indexOf(tempKey);
	// 		releasedData = pressedButNotReleasedKeys.remove(index);
	// 		releaseTimestamp = Integer.parseInt(tempTimestamp) - Integer.parseInt(pressedButNotReleasedTimestamps.remove(index));
	// 		releaseTimestampString = new Integer(releaseTimestamp).toString();
	// 		flightTimes.add(tempKey+" "+releaseTimestampString);
	// 		// print(tempKey+" "+releaseTimestampString);
	// 	}
	// }

	return flightTimes;
}