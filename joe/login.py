import time;
import os;
from pynput.keyboard import Key, Listener

t0 = str(time.time());
user = input('What is your username?');
# Collect events until released
if not os.path.exists(user+"/"+t0):
	os.makedirs(user+"/"+t0);
raw = open(user+"/"+t0+"/raw", 'w+');

presses = [];
releases = [];

def on_press(key):
	# raw.write(key+" "+"PRESSED "+str(time.time()));
	print ('{0} pressed'.format(key));
	presses.append({key: time.time()});

def on_release(key):
	# raw.write(key+" "+"RELEASED "+str(time.time()));
	print('{0} release'.format(key));
	if key == Key.esc:
		# Stop listener
		return False
	elif key==Key.enter:
		raw.close();
		process_timestamps();
	else:
		releases.append({key: time.time()});

def process_timestamps():
	print (presses);
	print ();
	print (releases);

def main():
	with Listener(
	        on_press=on_press,
	        on_release=on_release) as listener:
	    listener.join()

main();