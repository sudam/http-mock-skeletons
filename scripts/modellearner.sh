#!/bin/bash 

START_MESSAGE=""
END_MESSAGE=""

if [ $1 == -ilist ]
then
START_MESSAGE="Start checking optimal target attribute indexes for predictions"
END_MESSAGE="End checking optimal target attribute indexes for predictions"
elif [ $1 == -clist ]
then
START_MESSAGE="Start checking optimal target class names for predictions"
END_MESSAGE="End checking optimal target class names for predictions"
elif [ $1 == -a ] && [ $3 == -d ] && [ $5 == -i ]
then
START_MESSAGE="Start model training"
END_MESSAGE="End model training"
elif [ $1 == -a ] && [ $3 == -d ] && [ $5 == -c ]
then
START_MESSAGE="Start model training"
END_MESSAGE="End model training"
else
echo "Invalid arguments"
fi

echo $START_MESSAGE
echo
mvn clean install
mvn -q clean compile exec:java -Dexec.mainClass="nz.ac.massey.httpmockskeletons.scripts.modellearner.ModelTrainer" -Dexec.args="-$*"
echo
echo $END_MESSAGE

exit 0
