#!/bin/bash 

START_MESSAGE=""
END_MESSAGE=""

if [ $1 == -l ] && [ $2 == "abl" ]
then
START_MESSAGE="WEKA $2"
END_MESSAGE="END WEKA"
elif [ $1 == -l ] && [ $2 == "dll" ]
then
START_MESSAGE="DLLEARNER $2" 
END_MESSAGE="END DL"
else
START_MESSAGE="Start model training"
END_MESSAGE="End generating training data"
fi

echo $START_MESSAGE
echo
mvn clean install
mvn -q clean compile exec:java -Dexec.mainClass="nz.ac.massey.httpmockskeletons.scripts.datapreparator.TrainingDataGenerator" -Dexec.args="-$*"
echo
echo $END_MESSAGE

exit 0