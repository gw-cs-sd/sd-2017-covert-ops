
# coding: utf-8

# In[1]:

import pandas as pd;
import numpy as np;
import json;
import sys;

# import sys
# print (sys.argv[1])


# In[2]:

rawJSON = json.loads("".join(open('keystrokes-34c38-export (7).json').readlines()))


# In[3]:

def calculatePerKeyDwellTime(dataframe,DEBUG=False):
    dwell_times = {}
    for i in set(dataframe.key.unique()):
        if(DEBUG):
            print (i);
        running_average = 0;
        subset = dataframe[dataframe['key'] == i];
        if(len(dataframe[dataframe['key']==i])%2==0):
            for g in range(len(subset)-1):
                running_average+=(float(subset.iloc[g+1]['timestamp']) - float(subset.iloc[g]['timestamp']));
            if(DEBUG):
                print ("running average: "+str(running_average));
            running_average/=len(subset);
            if(DEBUG):
                print ("running average/subset_len = "+str(running_average));
        else: # last key release not recorded
            for g in range(len(subset)-2):
                running_average+=(float(subset.iloc[g+1]['timestamp']) - float(subset.iloc[g]['timestamp']));
            running_average/=len(subset);
        dwell_times[i] = running_average;
        if(DEBUG):
            print (" ");
    return dwell_times;    


def calculateFlightTime(dataframe):
#     time between release and next key press
    flight_times = [];
    index_of_releases = ((dataframe[dataframe['action']=='RELEASED']).index.tolist());
    range_of_events = list(range(index_of_releases[0], index_of_releases[-1]));
    index_of_presses = list(set(range_of_events).difference(index_of_releases));
    try:
        first_press = index_of_presses[0];
#         print (index_of_presses)
    except:
        return False;
    while (index_of_presses[0]<index_of_releases[0]):
        del index_of_releases[0];
    while(len(index_of_presses)>0):
        flight_times.append((float(dataframe['timestamp'].iloc[index_of_presses[0]]) - float(dataframe['timestamp'].iloc[index_of_releases[0]])));
        del index_of_releases[0]
        del index_of_presses[0]
    return flight_times;


# In[4]:

keystrokes=pd.DataFrame.from_dict(rawJSON)
obj = (keystrokes[:1]['samples'])
keystrokes=pd.DataFrame.from_dict(rawJSON['samples']).T


# In[5]:

# Just the new records
lorem_ipsum = keystrokes.iloc[22:]


# In[6]:

lorem_ipsum


# In[7]:

left_DwellTimes = [];
right_DwellTimes=[];
space_DwellTimes=[];
left_hand_keys = ['q','w','e','r','g','a','s','d','f','g','z','x','c','v'];


# In[8]:

if(sys.argv[1]=='-v' or sys.argv[1]=='--verbose'):
    __VERBOSE__ = True;
    number_of_chars_per_observation = int(sys.argv[2])
else:
    __VERBOSE__ = False;
    number_of_chars_per_observation = int(sys.argv[1])


# In[9]:

#  .o88o. 8888o. 8888o. 88  88 
#  88  88 88  88 88  88 88  88 
#  888888 88  88 88  88 'Y8888 
#  88  88 88  88 88  88     88 
#  88  88 88  88 8888Y' 8888Y' 

left_hand_keys = ['q','w','e','r','g','a','s','d','f','g','z','x','c','v'];

subject_lorem_keystrokes = json.loads(lorem_ipsum['keystrokes'].iloc[0])[0]
len(subject_lorem_keystrokes)
# There are 1456 keystroke events. We will divide these into groups of 16, giving us 91 records
split_keystrokes = [subject_lorem_keystrokes[x:x+15] for x in range(0, len(subject_lorem_keystrokes),number_of_chars_per_observation)]

descriptive_stats = [];
for n in split_keystrokes:
    left_DwellTimes = [];
    right_DwellTimes=[];
    space_DwellTimes=[];
    split_list=[];
    raw = pd.DataFrame(n);
    for n in raw.index:
        split_list.append(raw[0][n].split(" "));
    split = pd.DataFrame(split_list,columns=['timestamp','key','action']);
    
    for n in calculatePerKeyDwellTime(split):
        if(calculatePerKeyDwellTime(split)[n] != 0 ):
            if n.lower() in left_hand_keys:
                left_DwellTimes.append(calculatePerKeyDwellTime(split)[n]);
            elif n.lower() == 'spacebar':
                space_DwellTimes.append(calculatePerKeyDwellTime(split)[n]);
            else:
                right_DwellTimes.append(calculatePerKeyDwellTime(split)[n]);
#     results = list(pd.Series(list(calculatePerKeyDwellTime(split).values())).describe())[1:];
    results = list(pd.Series(list(left_DwellTimes)).describe())[1:];
#     results+=((list(pd.Series(calculateFlightTime(split)).describe())[1:]))
    results += list(pd.Series(list(right_DwellTimes)).describe())[1:];
    results += list(pd.Series(list(space_DwellTimes)).describe())[1:];
    results += ((list(pd.Series(calculateFlightTime(split)).describe())[1:]))
    descriptive_stats.append(results);
    
andy = pd.DataFrame(descriptive_stats);
andy['subject'] = 'Andy';

# andy.plot()


# In[10]:

#      88 .o88o. 888888 
#      88 88  88 88     
#      88 88  88 8888   
#  88  88 88  88 88     
#  'Y88Y' 'Y88Y' 888888 

left_hand_keys = ['q','w','e','r','g','a','s','d','f','g','z','x','c','v'];

subject_lorem_keystrokes = json.loads(lorem_ipsum['keystrokes'].iloc[0])[0]
len(subject_lorem_keystrokes)
# There are 1456 keystroke events. We will divide these into groups of 16, giving us 91 records
split_keystrokes = [subject_lorem_keystrokes[x:x+15] for x in range(0, len(subject_lorem_keystrokes),number_of_chars_per_observation)]

descriptive_stats = [];
for n in split_keystrokes:
#     print (' ');
    left_DwellTimes = [];
    right_DwellTimes=[];
    space_DwellTimes=[];
    split_list=[];
    raw = pd.DataFrame(n);
    for n in raw.index:
        split_list.append(raw[0][n].split(" "));
    split = pd.DataFrame(split_list,columns=['timestamp','key','action']);
    
    for n in calculatePerKeyDwellTime(split):
        if(calculatePerKeyDwellTime(split)[n] != 0 ):
            if n.lower() in left_hand_keys:
                left_DwellTimes.append(calculatePerKeyDwellTime(split)[n]);
            elif n.lower() == 'spacebar':
                space_DwellTimes.append(calculatePerKeyDwellTime(split)[n]);
            else:
                right_DwellTimes.append(calculatePerKeyDwellTime(split)[n]);
    results = list(pd.Series(list(left_DwellTimes)).describe())[1:];
    results += list(pd.Series(list(right_DwellTimes)).describe())[1:];
    results += list(pd.Series(list(space_DwellTimes)).describe())[1:];
    results += ((list(pd.Series(calculateFlightTime(split)).describe())[1:]))
    descriptive_stats.append(results);
    
joe = pd.DataFrame(descriptive_stats);
joe['subject'] = 'Joe';

# joe.plot()


# In[11]:

sample_descriptive_stats = pd.Series(list(calculatePerKeyDwellTime(split).values())).describe()
# sample_descriptive_stats['max']-sample_descriptive_stats['50%']


# In[12]:

# combine Joe & Andy
master = joe.append(andy);
master = master.reset_index();
answers = master['subject'];
X = master.drop('subject',axis=1);
# X


# In[13]:

ft_labels = ["flightTime "+n for n in list(pd.Series([1,2,3,4]).describe()[1:].keys())]
dt_labels = ["dwellTime "+n for n in list(pd.Series([1,2,3,4]).describe()[1:].keys())]


# In[14]:

labels = dt_labels + ft_labels


# In[15]:

joe = joe.fillna(value=0)
andy = andy.fillna(value=0)


# In[16]:

master = joe.append(andy);
master = master.reset_index();
answers = master['subject'];
X = master.drop('subject',axis=1);


# In[17]:

from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, answers, test_size=0.33, random_state=42)


# In[18]:

from sklearn import tree
clf = tree.DecisionTreeClassifier()
clf = clf.fit(X_train, y_train)
print ("DecisionTree: "+ str(clf.score(X_test, y_test)));
# list(clf.feature_importances_)


# In[19]:

from sklearn.neural_network import MLPClassifier
clf = MLPClassifier(solver='lbfgs', alpha=1e-5, hidden_layer_sizes=(10, 4), random_state=5)
clf.fit(X_train, y_train)
# clf.score(X_test, y_test)
print ("MultiLayer Perceptron: "+ str(clf.score(X_test, y_test)));


# In[20]:

from sklearn.svm import SVC
# clf1 = SVC(C=1.0, verbose=True, probability=True, decision_function_shape='ovr', kernel='linear')
clf1 = SVC(C=1.0, verbose=False, probability=True, decision_function_shape='ovr', kernel='linear')
clf1.fit(X_train, y_train) 
# clf1.predict_proba(X_test)
# clf1.score(X_test,y_test)
print ("SupportVectorMachine: "+ str(clf1.score(X_test, y_test)));


# In[21]:

from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
clf = LinearDiscriminantAnalysis()
clf.fit(X_train, y_train)
LinearDiscriminantAnalysis(n_components=None, priors=None, shrinkage=None,
              solver='svd', store_covariance=False, tol=0.0001)
# clf.score(X_test,y_test)
print ("LDA: "+ str(clf.score(X_test, y_test)));


# In[22]:

from sklearn.naive_bayes import GaussianNB
clf = GaussianNB()
clf.fit(X_train, y_train)
GaussianNB(priors=None)
# clf.score(X_test,y_test)
print ("GaussianNB: "+ str(clf.score(X_test, y_test)));
# clf.get_params()


# In[23]:

from sklearn.ensemble import RandomForestClassifier
clf = RandomForestClassifier(n_estimators=100, )
clf = clf.fit(X_train, y_train)
# clf.score(X_test, y_test)
print ("RandomForest: "+ str(clf.score(X_test, y_test)));


# In[24]:

from sklearn.ensemble import GradientBoostingClassifier
# clf = GradientBoostingClassifier(n_estimators=50, learning_rate=1.8, max_depth=5, random_state=0).fit(X_train, y_train)
clf = GradientBoostingClassifier(n_estimators=50, learning_rate=1.8, max_depth=5, random_state=0).fit(X_train, y_train)
# clf.score(X_test, y_test) 
print ("GradientBoostingClassifier: "+ str(clf.score(X_test, y_test)));

