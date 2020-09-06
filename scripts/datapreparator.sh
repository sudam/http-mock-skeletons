#!/bin/bash 

START_MESSAGE=""
END_MESSAGE=""
ERROR_MESSAGE="Invalid arguments"
HAS_ERROR=""

if [ $1 == -l ] && [ $2 == "abl" ] && [ $3 == -d ]
then
START_MESSAGE="Start generating training data for attribute-based learning"
END_MESSAGE="End generating training data for attribute-based learning"
HAS_ERROR="FALSE"

elif [ $1 == -l ] && [ $2 == "dll" ] && [ $3 == -d ]
then
START_MESSAGE="Start generating training data for description logic learning" 
END_MESSAGE="End generating training data for description logic learning"
HAS_ERROR="FALSE"

else
HAS_ERROR="TRUE"

fi

if [ $HAS_ERROR == "TRUE" ]
then 
echo $ERROR_MESSAGE

else 

echo $START_MESSAGE
echo
mvn clean install
mvn -q clean compile exec:java -Dexec.mainClass="nz.ac.massey.httpmockskeletons.scripts.datapreparator.TrainingDataGenerator" -Dexec.args="-$*"
echo
echo $END_MESSAGE

fi

exit 0