#!/usr/bin/env bash

parse_yaml() {
    local prefix=$2
    local s
    local w
    local fs
    s='[[:space:]]*'
    w='[a-zA-Z0-9_]*'
    fs="$(echo @|tr @ '\034')"
    sed -ne "s|^\($s\)\($w\)$s:$s\"\(.*\)\"$s\$|\1$fs\2$fs\3|p" \
        -e "s|^\($s\)\($w\)$s[:-]$s\(.*\)$s\$|\1$fs\2$fs\3|p" "$1" |
    awk -F"$fs" '{
    indent = length($1)/2;
    vname[indent] = $2;
    for (i in vname) {if (i > indent) {delete vname[i]}}
        if (length($3) > 0) {
            vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
            printf("%s%s%s=(\"%s\")\n", "'"$prefix"'",vn, $2, $3);
        }
    }' | sed 's/_=/+=/g'
}

# Example ./update-dockerfile.sh user-infos.yml Dockerfile docker-compose.yaml
source=$1
target=$2
composeTarget=$3

ymldata="$(parse_yaml $source)"

infos=(${ymldata// / })

# String to parse infos
profileEntry='user_profile'
hostEntry='user_database_host'
userEntry='user_database_user'
passwordEntry='user_database_password'
portEntry='user_database_port'
databaseNameEntry='user_database_name'
uploadPathEntry='user_upload_path'
logPathEntry='user_logs_path'
appPortEntry='user_app_port'
toInitializeEntry='user_app_toinitialize'
dockerNetworkEntry='user_docker_network'

# Length of data to get
profileLength=$((${#infos[0]} - ${#profileEntry} - 5))
hostLength=$((${#infos[1]} - ${#hostEntry} - 5))
userLength=$((${#infos[2]} - ${#userEntry} - 5))
passwordLength=$((${#infos[3]} - ${#passwordEntry} - 5))
portLength=$((${#infos[4]} - ${#portEntry} - 5))
databaseNameLength=$((${#infos[5]} - ${#databaseNameEntry} - 5))
uploadPathLength=$((${#infos[6]} - ${#uploadPathEntry} - 5))
logsPathLength=$((${#infos[7]} - ${#logPathEntry} - 5))
appPortLength=$((${#infos[8]} - ${#appPortEntry} - 5))
toInitializeLength=$((${#infos[9]} - ${#toInitializeEntry} - 5))
dockerNetworkLength=$((${#infos[10]} - ${#dockerNetworkEntry} - 5))

# Parsed infos
profile=${infos[0]:${#profileEntry} + 3:profileLength}
host=${infos[1]:${#hostEntry} + 3:hostLength}
user=${infos[2]:${#userEntry} + 3:userLength}
password=${infos[3]:${#passwordEntry} + 3:passwordLength}
port=${infos[4]:${#portEntry} + 3:portLength}
database=${infos[5]:${#databaseNameEntry} + 3:databaseNameLength}
uploadPath=${infos[6]:${#uploadPathEntry} + 3:uploadPathLength}
logsPath=${infos[7]:${#logPathEntry} + 3:logsPathLength}
appPort=${infos[8]:${#appPortEntry} + 3:appPortLength}
toInitialize=${infos[9]:${#toInitializeEntry} + 3:toInitializeLength}
dockerNetwork=${infos[10]:${#dockerNetworkEntry} + 3:dockerNetworkLength}

# Replace
sed -i -e "s/<PROFILE>/${profile}/g" $target
sed -i -e "s/<DB_HOST>/${host}/g" $target
sed -i -e "s/<DB_USER>/${user}/g" $target
sed -i -e "s/<DB_PASSWORD>/${password}/g" $target
sed -i -e "s/<DB_PORT>/${port}/g" $target
sed -i -e "s/<DB_NAME>/${database}/g" $target
sed -i -e 's|<UPLOAD_PATH>|'$uploadPath'|g' $target
sed -i -e "s/<TO_INITIALIZE>/${toInitialize}/g" $target
rm $target-e

sed -i -e "s/<APP_PORT>/\"${appPort}\"/g" $composeTarget
sed -i -e 's|<LOGS_PATH>|'$logsPath'|g' $composeTarget
sed -i -e "s/<DOCKER_NETWORK>/${dockerNetwork}/g" $composeTarget
rm $composeTarget-e

docker-compose up -d
rm Dockerfile
rm docker-compose.yml
rm user-infos.yml