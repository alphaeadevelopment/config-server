#!/bin/bash

# check for local changes
if [[ "`git status -s | grep -v scripts | wc -l`" -ne "0" ]]
then
  echo "Error: commit local changes first"
  exit 1
fi

function exit_error {
    exit 1
}

version=`xml-js pom.xml --compact | jq .project.version._text | awk -F "\"" '{print $2}'`

echo $version

AWS_SDK_LOAD_CONFIG=1 node-cf-deploy update-stack \
  --name CfConfigServer-${Env} \
  -P Env=test \
  -P SubDomainName=config-server \
  -P ArtifactVersion=$version \
  -P AccessKey=x \
  -P SecretKey=x \
  -f config/cloudformation/config-server-cf-template.json \
  -S