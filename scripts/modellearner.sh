#!/bin/bash 

START_MESSAGE=""
END_MESSAGE=""

if [ $1 == -key ]
then
START_MESSAGE="Check attribute details"
END_MESSAGE="End checking attribute details"
elif [ $1 == -class ]
then
START_MESSAGE="Check valid classes"
END_MESSAGE="End checking valid classes"
elif [ $1 == -d ] && [ $3 == -a ] && [ $5 == -i ]
then
START_MESSAGE="Start model training"
END_MESSAGE="End model training"
elif [ $1 == -d ] && [ $3 == -a ] && [ $5 == -c ]
then
START_MESSAGE="Start OCEL"
END_MESSAGE="End OCEL"
else
echo "Invalid Arguments"
fi

echo $START_MESSAGE

mvn clean install
mvn -q clean compile exec:java -Dexec.mainClass="nz.ac.massey.httpmockskeletons.scripts.modellearner.ModelTrainer" -Dexec.args="-$*"

echo $END_MESSAGE

exit 0
