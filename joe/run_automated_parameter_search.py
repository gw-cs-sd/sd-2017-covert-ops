import subprocess;
r = range(16,36,1)
for n in r:
	n=str(n);
	subprocess.call("~/anaconda3/bin/python ~/Documents/sd-2017-covert-ops/joe/Automate_parameter_search.py "+n+" > results_"+n, shell=True);