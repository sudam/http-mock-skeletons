#!/bin/bash 
echo "Start model evaluation"
mvn clean install
mvn -q clean compile exec:java -Dexec.mainClass="nz.ac.massey.httpmockskeletons.scripts.modelevaluator.ModelTester" -Dexec.args="-$*"
echo "End model evaluation"
exit 0