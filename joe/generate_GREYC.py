# Convert raw timestamp files to GREY-C features
# - Press to Press
# - Press to Release
# - Release to Press
# - Release to Release

# ! Should be run from the same directory as login.py! 
import json;
import os;

# Functions

# press to press times
def press_to_press(dictionary):
	results = [];
	for n in range(0,len(dictionary['presses'])-1):
		print (n)
		results.append((dictionary['presses'][n+1][1] - dictionary['presses'][n][1])*1000);
	return results;


# Process
# - Walk keystrokes/unprocessed
for dir in os.listdir('keystrokes/unprocessed'):
	# 'dir' is a particular user
	if dir!='.DS_Store':
		print ("-"+dir);
		for k in os.listdir('keystrokes/unprocessed/'+dir):
			if k!='.DS_Store':
				# 'k' is a particular timestamp directory, containing 'raw'
				print ("--"+k);
				with open("keystrokes/unprocessed/"+dir+"/"+k+"/raw") as data_file:
					master = json.load(data_file);
				password = [n[0].replace("'","") for n in master['presses']];
				# - Apply GREY-C functions to each raw file
				p_to_p = press_to_press(master);
				print (p_to_p);
				# - Save results in 4 files: pp, pr, rp, and rr
				# - rename directory into the "processed" directory