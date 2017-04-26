from __future__ import unicode_literals;

# Before imports
# import sys;
# original_syspath = sys.path[0];
# sys.path[0] = original_syspath+'/dependencies';
# print (sys.path[0]);

import time;
import os;
from pynput.keyboard import Key, Listener;
import json;
import pandas as pd;
import numpy as np;
from sklearn import tree;
from sklearn.externals import joblib;


# After imports
# sys.path[0] = original_syspath;
# print ("successful imports");

import sys
if sys.version[0] != '3':
	print ("Must run via Python3.5 - we recommend the Anaconda3 distribution");
else:
	print (" ");
	print ("Logging in as user: Joseph");
	print (" ");

	presses = [];
	releases = [];
	master = {};

	def on_press(key):
		print ('{0} pressed'.format(key));
		if str(key)[:3]=='Key':
			print("skipping non-alphabetic characters");
			pass;
		else:
			presses.append((str(key), time.time()));

	def on_release(key):
		print('{0} release'.format(key));
		if key == Key.esc:
			# Stop listener
			return False
		elif key==Key.enter:
			process_timestamps();
			# Listener.stop();
			# pynput.Keyboard.Listener.stop();
			return False;
		elif str(key)[:3]=='Key':
			print("skipping non-alphabetic characters");
			pass;
		else:
			releases.append((str(key), time.time()));

	def process_timestamps():
		print (presses);
		print ();
		print (releases);
		master = {"presses": presses, "releases": releases};

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



	def main():
		time.sleep(1);
		print ("Enter your password: ");
		with Listener(on_press=on_press, on_release=on_release) as listener:
			print ("listener enabled");
			try:
				listener.join();
			except BaseException as e:
				# print('{0} was pressed'.format(e.args[0]));
				process_timestamps();
			os.system("clear");
		# Convert to GREYC format, assemble as dataframe object, load decision tree and call .classify()
		master = {"presses": presses, "releases": releases};
		# - Apply GREY-C functions to each raw file
		p_to_p = press_to_press(master);
		p_to_r = press_to_release(master);
		r_to_p = release_to_press(master);
		r_to_r = release_to_release(master);
		parsed_input = pd.DataFrame(p_to_p).append(p_to_r).append(r_to_p).append(r_to_r).reset_index().drop(['index'],axis=1);
		print (parsed_input);
		print (" ");
		print ("Classifying . . .");
		clf = joblib.load('DEMO_decision_tree.pkl');
		if(clf.predict(parsed_input.T)[0]=='genuine'):
			print ("welcome Joe!");
		else:
			print ("this isn't Joe");
		print();
		print(clf.predict_proba(parsed_input.T));

	main();

