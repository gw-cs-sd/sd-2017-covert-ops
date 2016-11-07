## Keystroke ##
A simple Java program for logging timestamped keystokes. For collecting data to build Senior Design project.

### Usage ###
```javac /final/CompleteDemo.java``` to compile the program.
```java final/CompleteDemo _corpus_name_ _subject_name_``` to run the program, saving the output to /_corpus_name_/_subject_name_.
!Make sure the corpus and subject directories exist before running the program!

Now that Weka is installed, compiling with the correct library references involves using the classpath flag

From /keystroke:
```javac -cp weka-3-9-0/weka.jar final/Model_Trainer.java```
creates Model_Trainer.class in the /keystroke directory. Then run
```java Model_Trainer```

#### Example Usage ####

``` java CompleteDemo quick_brown_fox joehaaga``` would be how I run the program to record myself typing "The quick brown fox jumped over the lazy dog."
