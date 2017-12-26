#!/usr/bin/env bash

pwd
echo pwd

echo 'To complete this script. It will have to copy files somewhere on a server (jar + docker-compose + dockerfile + deploy.sh)'
echo 'deploy.sh will be a script that search for container names and image names, destroy them and run docker-compose up'
echo 'To allow jenkins to connect to server as ssh, create key and add to server'
echo 'See to deploy over ssh: https://wiki.jenkins.io/display/JENKINS/Publish+Over+SSH+Plugin'
echo 'http://www.baeldung.com/spring-boot-app-as-a-service'

echo "Le 1er paramètre est : $1"

#echo 'The following Maven infrastructure installs your Maven-built Java application'
#echo 'into the local Maven repository, which will ultimately be stored in'
#echo 'Jenkins''s local Maven repository (and the "maven-repository" Docker data'
#echo 'volume).'
#set -x
#mvn jar:jar install:install help:evaluate -Dexpression=project.name
#set +x
#
#echo 'The following complex infrastructure extracts the value of the <name/> element'
#echo 'within <project/> of your Java/Maven project''s "pom.xml" file.'
#set -x
#NAME=`mvn help:evaluate -Dexpression=project.name | grep "^[^\[]"`
#set +x
#
#echo 'The following complex infrastructure behaves similarly to the previous one but'
#echo 'extracts the value of the <version/> element within <project/> instead.'
#set -x
#VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[^\[]"`
#set +x
#
#echo 'The following infrastructure runs and outputs the execution of your Java'
#echo 'application (which Jenkins built using Maven) to the Jenkins UI.'
#set -x
#java -jar target/${NAME}-${VERSION}.jar