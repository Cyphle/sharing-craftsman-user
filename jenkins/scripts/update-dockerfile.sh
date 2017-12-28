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

# Example ./update-dockerfile.sh user-infos.yml ../../Dockerfile
source=$1
target=$2

ymldata="$(parse_yaml $source)"

infos=(${ymldata// / })

# String to parse infos
profileEntry='user_profile'
hostEntry='user_database_host'
userEntry='user_database_user'
passwordEntry='user_database_password'
portEntry='user_database_port'
databaseNameEntry='user_database_name'

# Length of data to get
profileLength=$((${#infos[0]} - ${#profileEntry} - 5))
hostLength=$((${#infos[1]} - ${#hostEntry} - 5))
userLength=$((${#infos[2]} - ${#userEntry} - 5))
passwordLength=$((${#infos[3]} - ${#passwordEntry} - 5))
portLength=$((${#infos[4]} - ${#portEntry} - 5))
databaseNameLength=$((${#infos[5]} - ${#databaseNameEntry} - 5))

# Parsed infos
profile=${infos[0]:${#profileEntry} + 3:profileLength}
host=${infos[1]:${#hostEntry} + 3:hostLength}
user=${infos[2]:${#userEntry} + 3:userLength}
password=${infos[3]:${#passwordEntry} + 3:passwordLength}
port=${infos[4]:${#portEntry} + 3:portLength}
database=${infos[5]:${#databaseNameEntry} + 3:databaseNameLength}

# Replace
sed -i -e "s/<PROFILE>/${profile}/g" $target
sed -i -e "s/<DB_HOST>/${host}/g" $target
sed -i -e "s/<DB_USER>/${user}/g" $target
sed -i -e "s/<DB_PASSWORD>/${password}/g" $target
sed -i -e "s/<DB_PORT>/${port}/g" $target
sed -i -e "s/<DB_NAME>/${database}/g" $target
rm $target-e

docker-compose up -d