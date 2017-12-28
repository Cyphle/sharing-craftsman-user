#!/usr/bin/env bash

echo "Copying deployable files in app folder"
mkdir $1
cp docker-compose.yml $1/docker-compose.yml
cp Dockerfile $1/Dockerfile
cp target/user-1.0.jar $1/user.jar
cp jenkins/scripts/update-dockerfile.sh $1/update-dockerfile.sh
cp jenkins/scripts/user-infos.yml $1/user-infos.yml
cp jenkins/scripts/README.md $1/README.md
echo "End copying deployable files in app folder"