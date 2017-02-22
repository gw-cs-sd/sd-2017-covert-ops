# Records username and timestampped key events for password entry to a JSON object

import time;
import os;
from pynput.keyboard import Key, Listener;
import json;

t0 = str(time.time());
user = input('What is your username?');
if not os.path.exists("keystrokes/"+user+"/"+t0):
	os.makedirs("keystrokes/"+user+"/"+t0);
raw = open("keystrokes/"+user+"/"+t0+"/raw", 'w+');

running = True;

presses = [];
releases = [];

def on_press(key):
	print ('{0} pressed'.format(key));
	if str(key)[:3]=='Key':
		print("skipping non-alphabetic characters");
		pass;
	else:
		presses.append({str(key): time.time()});

def on_release(key):
	print('{0} release'.format(key));
	if key == Key.esc:
		# Stop listener
		return False
	elif key==Key.enter:
		process_timestamps();
		return False;
	elif str(key)[:3]=='Key':
		print("skipping non-alphabetic characters");
		pass;
	else:
		releases.append({str(key): time.time()});

def process_timestamps():
	print (presses);
	print ();
	print (releases);
	master = {"presses": presses, "releases": releases};
	json.dump(master, raw);
	raw.close();


def main():
	time.sleep(1);
	print ("Enter your password: ");
	with Listener(
        on_press=on_press,
        on_release=on_release) as listener:
    		listener.join();

main();
