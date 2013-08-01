#!/bin/bash

CONFIG=config/server_config.cfg
ROOT=$(cat $CONFIG|grep ^ROOT|awk 'BEGIN {FS = "="};{print $2}')
DD_DIRS=$(cat $CONFIG|grep ^DD_DIRS|awk 'BEGIN {FS = "="};{print $2}'|sed -e s/\,/" "/)
DELETE_DIRS=$(cat $CONFIG|grep ^DELETE_DIRS|awk 'BEGIN {FS = "="};{print $2}'|sed -e s/\,/" "/)
SERVER_IP=$(cat $CONFIG|grep ^SERVER_IP|awk 'BEGIN {FS = "="};{print $2}') 
SERVER_PORT=$(cat $CONFIG|grep ^SERVER_PORT|awk 'BEGIN {FS = "="};{print $2}') 
KILLSIG=$(cat $CONFIG|grep ^KILLSIG|awk 'BEGIN {FS = "="};{print $2}'|sed -e s/\"//g)
EMAIL=$(cat $CONFIG|grep ^EMAIL|awk 'BEGIN {FS = "="};{print $2}')
COPY_DIRS=$DELETE_DIRS

echo $1 $2 $3 $4 $5 
echo $KILLSIG
rm -f /tmp/.killpacket_log
rm -f /tmp/.killpacket_err

# If correct KILLSIG is sent by the client
if [ "$5" = "$KILLSIG" ];
    then
        # Backup existing directories to a remote server
        # echo "Backing up $COPY_DIRS.."
        # tar cvzf leaks.tar.gz $COPY_DIRS &&
        # SCP would run inside an ssh-agent with keys for a user on
        # a remote host that has restricted priveleges
        # scp volatile.tar.gz backup@my-backup-server.org:. &&
        # Delete the volatile archive and ssh-keys for the backup server
        # rm -fr volatile.tar.gz $COPYDIRS
        # rm -f /home/user/.ssh/backup.rsa
        echo "removing directories $DELETE_DIRS"
        # we need to eval in case the variable is not expanded 
        RM=$(rm -fr $(eval echo $DELETE_DIRS) 2>&1);

        if [ $? != 0 ]; then
            echo $RM
            echo "Failed deletion of directories by Kill Packet script with error: $RM" >> /tmp/.killpacket_err
            mail -s "KillPacket Report: Deletion Failure" $EMAIL < /tmp/.killpacket_err
        fi

        # write out a report to a user-writable home directory
        echo "Deleted $(eval echo $DELETE_DIRS)" > $HOME/kill-report.txt

        if [ "$ROOT" == "true" ]; then
            if [ "$DD_DIRS" != "NULL" ]; then
                echo "zeroing partitions $DD_DIRS"
                #DD=$(dd if=/dev/zero of=$(eval echo $DD_DIRS) 2>&1 &);
                if [ $? != 0 ]; then
                    echo "Failed zeroing of partition(s) by Kill Packet script with error: $DD" >> /tmp/.killpacket_err
                fi
            fi
            #optional power off machine:
            #poweroff
        fi

        echo "On date $(date) a Kill Packet script deleted $(eval echo $DELETE_DIRS)" >> /tmp/.killpacket_log

        # Email a user when done
        if [ "$EMAIL" != "false" ]; then
            if [ -f /tmp/.killpacket_err ]; then 
                echo "******************* Errors follow ***************************"
                cat /tmp/.killpacket_err >> /tmp/.killpacket_log
            fi
            mail -s "KillPacket Report" $EMAIL < /tmp/.killpacket_log
        fi
fi
