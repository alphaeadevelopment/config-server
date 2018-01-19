#!/bin/bash


function exit_error() {
    exit 1
}

function usage() {
    echo "usage:  `basename "$0"` <options>"
    echo "  -e              Env (dev|prod)"
    echo "  -u              Jdbc username"
    echo "  -p              Jdbc password"
    echo "  -l              Jdbc URL"
    echo "  -d              Jdbc Driver"
    echo "  -h              Print this usage and exit"

  if [[ $# == 0 ]]; then exit 1;
  else exit $1
  fi
}

stack_name=CfConfigServer
driver=com.mysql.jdbc.Driver
# Read cmd line arguments
while [ $# -gt 0 ]; do
    case "$1" in
        -e)
            env=$2
            shift 2
            ;;
        -u)
          username=$2
          shift 2
          ;;
        -p)
          password=$2
          shift 2
          ;;
        -l)
          url=$2
          shift 2
          ;;
        -d)
          driver=$2
          shift 2
          ;;
        -n)
          stack_name=$2
          shift 2
          ;;
        -h)
          usage 0
          ;;
        *)
        usage
        ;;
    esac
done

if [[ "$env" == "" || "$username" == "" || "$url" == "" ]]
then
  usage
  exit_error
fi

# check for local changes
if [[ "`git status -s | grep -v scripts | wc -l`" -ne "0" ]]
then
  echo "Error: commit local changes first"
  exit 1
fi

version=`xml-js pom.xml --compact | jq .project.version._text | awk -F "\"" '{print $2}'`

AWS_SDK_LOAD_CONFIG=1 node-cf-deploy update-stack \
  --name ${stack_name} \
  -P Env=${env} \
  -P SubDomainName=config-server-${env} \
  -P ArtifactVersion=${version} \
  -P AccessKey=x \
  -P SecretKey=x \
  -P JdbcUsername=${username} \
  -P JdbcPassword=${password} \
  -P JdbcUrl=${url} \
  -P JdbcDriverClassName=${driver} \
  -f config/cloudformation/config-server-cf-template.json \
  -S || exit_error
 
 node-cf-deploy terminate-stack-instances -n ${stack_name}