#!/bin/bash 

START_MESSAGE=""
END_MESSAGE=""

if [ $1 == -l ] && [ $2 == "abl" ]
then
START_MESSAGE="Start generating training data for attribute-based learning"
END_MESSAGE="End generating training data for attribute-based learning"
elif [ $1 == -l ] && [ $2 == "dll" ]
then
START_MESSAGE="Start generating training data for description logic learning" 
END_MESSAGE="End generating training data for description logic learning"
else
START_MESSAGE=""
$END_MESSAGE=""
fi

echo $START_MESSAGE
echo
mvn clean install
mvn -q clean compile exec:java -Dexec.mainClass="nz.ac.massey.httpmockskeletons.scripts.datapreparator.TrainingDataGenerator" -Dexec.args="-$*"
echo
echo $END_MESSAGE

exit 0