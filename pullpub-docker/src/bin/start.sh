#!/bin/bash

##
## Copy configuration
##
ACTIVE_CONFIG=/app/config-active
function copyConfig {
    SOURCE_FILE=$ACTIVE_CONFIG/$1
    DESTINATION_FILE=$2

    if [ -f $SOURCE_FILE ]; then
       cp "$SOURCE_FILE" "$DESTINATION_FILE"
    fi
}

mkdir -p $ACTIVE_CONFIG
cp -r /app/config-default/* $ACTIVE_CONFIG/
cp -r /app/config/* $ACTIVE_CONFIG/

copyConfig "gitconfig" "/root/.gitconfig"
mkdir -p "/root/.ssh"
copyConfig "id_rsa" "/root/.ssh/id_rsa"
copyConfig "id_rsa.pub" "/root/.ssh/id_rsa.pub"
copyConfig "git-credentials" "/root/.git-credentials"

##
## Get repo details
##
git config --global credential.helper store

REPO_URL=$(cat $ACTIVE_CONFIG/git-repository)
REPO_BRANCH=$(cat $ACTIVE_CONFIG/git-branch)
mkdir -p /app/repository
##
## Run
##
echo "Starting server..."
java -jar bin/server.jar 80 /admin /app/repository webroot "$REPO_URL" "$REPO_BRANCH"
