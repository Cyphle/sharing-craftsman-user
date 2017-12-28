#!/usr/bin/env bash

path='/home/apps/user'

echo "Copying deployable files in app folder"
mkdir $path
cp docker-compose.yml $1/docker-compose.yml
cp Dockerfile $1/Dockerfile
cp target/user-1.0.jar $1/user.jar
echo "End copying deployable files in app folder"