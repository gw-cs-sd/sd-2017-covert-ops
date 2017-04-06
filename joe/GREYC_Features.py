
# coding: utf-8

# In[1]:

import pandas as pd;
import numpy as np;
import os


# In[2]:

# ~450 genuine entries, ~150 impostor entries

genuine = {};
genuine['pressToPress'] = [];
genuine['pressToRelease'] = [];
genuine['releaseToPress'] = [];
genuine['releaseToRelease'] = [];
impostor = {};
impostor['pressToPress'] = [];
impostor['pressToRelease'] = [];
impostor['releaseToPress'] = [];
impostor['releaseToRelease'] = [];

for root, dirs, files in os.walk("data/user_001", topdown=False):
    for subdir in dirs:
        try:
            if(len(open(os.path.join(root,subdir)+"/p_pr.txt").readlines())==8):
                if "genuine" in root:
                    genuine['pressToPress'].append(open(os.path.join(root,subdir)+"/p_pp.txt").readlines())
                    genuine['pressToRelease'].append(open(os.path.join(root,subdir)+"/p_pr.txt").readlines())
                    genuine['releaseToPress'].append(open(os.path.join(root,subdir)+"/p_rp.txt").readlines())
                    genuine['releaseToRelease'].append(open(os.path.join(root,subdir)+"/p_rr.txt").readlines())
                else:
                    impostor['pressToPress'].append(open(os.path.join(root,subdir)+"/p_pp.txt").readlines())
                    impostor['pressToRelease'].append(open(os.path.join(root,subdir)+"/p_pr.txt").readlines())
                    impostor['releaseToPress'].append(open(os.path.join(root,subdir)+"/p_rp.txt").readlines())
                    impostor['releaseToRelease'].append(open(os.path.join(root,subdir)+"/p_rr.txt").readlines())
#         ignore files that aren't timestampped keystroke data
        except: 
            pass;


# In[3]:

genuine['master'] = [genuine['pressToPress'][k] + genuine['pressToRelease'][k] + genuine['releaseToPress'][k] + genuine['releaseToRelease'][k] for k in range(0,len(genuine['pressToPress']))]
impostor['master'] = [impostor['pressToPress'][k] + impostor['pressToRelease'][k] + impostor['releaseToPress'][k] + impostor['releaseToRelease'][k] for k in range(0,len(impostor['pressToPress']))]


# In[4]:

genuine_df = pd.DataFrame(genuine['master']).astype(int)
genuine_df['user'] = 'genuine'

impostor_df = pd.DataFrame(impostor['master']).astype(int)
impostor_df['user'] = 'impostor'


# In[5]:

# Balanced dataset; 100 Genuine trials vs 100 Impostor trials

dataset = pd.concat([genuine_df.iloc[:100],impostor_df],ignore_index=True)


# In[20]:

pd.concat([dataset.iloc[:10],dataset.iloc[190:]])


# In[6]:

y=dataset['user']
X=dataset.drop(['user'], axis=1)


# In[7]:

# Split and shuffle 

from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=42)


# In[8]:

# Linear Support Vector Classifier

from sklearn.svm import LinearSVC
svc = LinearSVC().fit(X_train,y_train)


# In[9]:

# Predict on test set and evaluate performance
svc.score(X_test,y_test)


# In[10]:

svc.decision_function(X_test)


# In[11]:

# DecisionTree Classifier
from sklearn import tree
dt = tree.DecisionTreeClassifier().fit(X_train,y_train)


# In[12]:

dt.score(X_test,y_test)


# In[13]:

dt.feature_importances_


# In[14]:

genuine_df.iloc[:100][2].describe()


# In[15]:

impostor_df[2].describe()

