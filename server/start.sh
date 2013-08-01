#!/bin/bash

SERVER_PORT=$(cat config/server_config.cfg|grep ^SERVER_PORT|awk 'BEGIN {FS = "="};{print $2}') 
ROOT=$(cat config/server_config.cfg|grep ^ROOT|awk 'BEGIN {FS = "="};{print $2}') 

while true;
    do
        if [ "$ROOT" == "true" ]; then
            if [ "$USER" != "root" ]; then
                echo '
You selected root but are not running this script as root.
Please run this script with:
            su -c "sh start.sh"'
                exit
            fi
        fi
        echo "listening on port $SERVER_PORT"
        ./bin/socat -u TCP4-LISTEN:$SERVER_PORT STDIO | ./scripts/server_kill.sh `tee`; 
        #echo "Done. Sleeping for 10 seconds."
        sleep 10;
done 
