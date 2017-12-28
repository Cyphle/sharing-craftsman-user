#!/usr/bin/env bash

path='/home/apps/user'

echo "Copying deployable files in app folder"
mkdir $path
cp docker-compose.yml $path/docker-compose.yml
#cp Dockerfile /home/apps/user/Dockerfile
#cp target/user-1.0.jar /home/apps/user/user.jar
echo "End copying deployable files in app folder"