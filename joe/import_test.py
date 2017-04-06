# Before imports
import sys;
original_syspath = sys.path[0];
sys.path[0] = original_syspath+'/dependencies';
print (sys.path[0]);

# Imports go here
import sklearn;

# After imports
sys.path[0] = original_syspath;
print ("successful imports");