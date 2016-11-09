#!/bin/bash
unzip /home/tester/jar/CognitiveAgent.jar -d /home/tester/jar/
export JAR_HOME=/home/tester/jar/lib/

for f in $JAR_HOME/*.jar
do
JAR_CLASSPATH=$JAR_CLASSPATH:$f
done
export JAR_CLASSPATH

#the next line will print the JAR_CLASSPATH to the shell.
echo the classpath $JAR_CLASSPATH

java -classpath /home/tester/jar/:$JAR_CLASSPATH com.ncubo.controllers.InitApplication
