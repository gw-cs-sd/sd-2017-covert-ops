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
# def press_to_press():

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
				password = ([list(n.keys())[0].replace("'","") for n in master['presses']]);
		    	# - Apply GREY-C functions to each raw file
				# - Save results in 4 files: pp, pr, rp, and rr
				# - rename directory into the "processed" directory


# with open('raw') as data_file:    
#     master = json.load(data_file);

# password = ([list(n.keys())[0].replace("'","") for n in master['presses']]);

# print (master['presses']);
# print ();
# print (master['releases']);