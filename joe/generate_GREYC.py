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
		results.append((dictionary['presses'][n+1][1] - dictionary['presses'][n][1])*1000);
	return results;

def press_to_release(dictionary):
	results = [];
	for n in range(0,len(dictionary['presses'])-1):
		results.append((dictionary['releases'][n][1] - dictionary['presses'][n][1])*1000);
	return results;

def release_to_press(dictionary):
	results = [];
	for n in range(0,len(dictionary['releases'])-1):
		results.append((dictionary['presses'][n+1][1] - dictionary['releases'][n][1])*1000);
	return results;

def release_to_release(dictionary):
	results = [];
	for n in range(0,len(dictionary['releases'])-1):
		results.append((dictionary['releases'][n+1][1] - dictionary['releases'][n][1])*1000);
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
				print ("\nPress To Press times:");
				print (p_to_p);
				p_to_r = press_to_release(master);
				print ("\nPress To Release times:");
				print (p_to_r);
				r_to_p = release_to_press(master);
				print ("\nRelease to Press times:");
				print (r_to_p);
				r_to_r = release_to_release(master);
				print ("\nRelease to Release times:");
				print (r_to_r);
				# - Save results in 4 files: pp, pr, rp, and rr
				if not os.path.exists("keystrokes/processed/"+dir+"/"+k):
					os.makedirs("keystrokes/processed/"+dir+"/"+k);
				# Move raw
				os.rename('keystrokes/unprocessed/'+dir+"/"+k+"/raw", 'keystrokes/processed/'+dir+"/"+k+"/raw");
				
				pp = open('keystrokes/processed/'+dir+"/"+k+"/p_pp.txt", "w+");
				for n in p_to_p:
					pp.write(str(n)+"\n");
				pp.close();

				pr = open('keystrokes/processed/'+dir+"/"+k+"/p_pr.txt", "w+");
				for n in p_to_r:
					pr.write(str(n)+"\n");
				pr.close();

				rp = open('keystrokes/processed/'+dir+"/"+k+"/p_rp.txt", "w+");
				for n in r_to_p:
					rp.write(str(n)+"\n");
				rp.close();

				rr = open('keystrokes/processed/'+dir+"/"+k+"/p_rr.txt", "w+");
				for n in r_to_r:
					rr.write(str(n)+"\n");
				rr.close();
				# - remove unprocessed directory
				os.rmdir('keystrokes/unprocessed/'+dir+"/"+k);
		os.rmdir('keystrokes/unprocessed/'+dir);