# Convert raw timestamp files to GREY-C features
# - Press to Press
# - Press to Release
# - Release to Press
# - Release to Release

import json;

# Functions

# press to press times
# def press_to_press():


with open('raw') as data_file:    
    master = json.load(data_file);

password = ([list(n.keys())[0].replace("'","") for n in master['presses']]);

print (master['presses']);
print ();
print (master['releases']);