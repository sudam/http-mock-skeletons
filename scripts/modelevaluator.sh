#!/bin/bash 

START_MESSAGE=""
END_MESSAGE=""
ERROR_MESSAGE="Invalid arguments"
HAS_ERROR=""

if [ $1 == -ilist ]
then
START_MESSAGE="Start checking optimal target attribute indexes for predictions"
END_MESSAGE="End checking optimal target attribute indexes for predictions"
HAS_ERROR="FALSE"

elif [ $1 == -clist ]
then
START_MESSAGE="Start checking optimal target class names for predictions"
END_MESSAGE="End checking optimal target class names for predictions"
HAS_ERROR="FALSE"

elif [ $1 == -a ] && [ $3 == -d ] && [ $5 == -i ]
then
START_MESSAGE="Start model testing"
END_MESSAGE="End model testing"
HAS_ERROR="FALSE"

elif [ $1 == -a ] && [ $3 == -d ] && [ $5 == -c ]
then
START_MESSAGE="Start model testing"
END_MESSAGE="End model testing"
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
mvn -q clean compile exec:java -Dexec.mainClass="nz.ac.massey.httpmockskeletons.scripts.modelevaluator.ModelTester" -Dexec.args="-$*"
echo
echo $END_MESSAGE

fi

exit 0