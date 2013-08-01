#!/bin/bash

# This setup script is responsible for writing the 'config/server_config.cfg'
# And setting basic permissions.

CONFIG=config/server_config.cfg
rm -f $CONFIG

echo "*************************************************************************************************"
while true; do
read -p "* Do you need to remove directories or files user '$(eval echo $USER)' does not own? [Yy/Nn] " yn 
            case $yn in
            [Yy]* ) 
                # create config
                echo "ROOT=true" > $CONFIG; 
echo "*************************************************************************************************"
read -p "* Do you want to destroy/zero any partitions? [Yy/Nn] " yn
                    case $yn in
                    [Yy]* ) 
echo "*************************************************************************************************"
read -p "Which partitions do you want to zero?
* Take care to use full paths!
* Separate paritions with a comma
> " dd_dirs
                        echo "DD_DIRS=$dd_dirs" >> $CONFIG
                        ;;
                    [Nn]* ) 
                        echo "DD_DIRS=NULL" >> $CONFIG
                        ;;
                        * ) ;; 
                    esac
                    ;;
            [Nn]* ) 
                # create config
                echo "ROOT=false" > $CONFIG;
                echo "DD_DIRS=NULL" >> $CONFIG;
                ;;
            * ) ;; 
        esac

echo "*************************************************************************************************"
read -p "Which directories do you want to delete?
* Use $HOME/path/do/dir or /home/bill/path/to/dir
* Separate multiple directories with a comma
> " dir

             echo 'DELETE_DIRS='"$dir" >> $CONFIG 
echo "*************************************************************************************************"
    read -p "Provide a unique passphrase (unused elsewhere)
* Be sure to copy it directly into the Kill Packet app config interface on your phone
* Don't write it down
> " pw
             echo 'KILLSIG='"$pw" >> $CONFIG 
echo "*************************************************************************************************"
    read -p "Which port would you like to use?
* Be sure to use a port higher than 1000 (1001-65535).
* Don't write it down and don't forget it! 
> " port
             echo 'SERVER_PORT='"$port" >> $CONFIG 
echo "*************************************************************************************************"
    read -p "Do you wish to receive an email confirming file/folder deletion? [Y\N] " yn
        case $yn in
        [Yy]* ) 
echo "*************************************************************************************************"
read -p "Please provide a valid email address
> " email; echo "EMAIL=$email" >> $CONFIG; 
            ;; 
        [Nn]* ) 
            echo "EMAIL=false" >> $CONFIG ;
            ;;
        * ) ;; 
    esac
echo "*************************************************************************************************"
echo "Writing to $CONFIG 
You can edit it later, by hand or with this script"
echo "*************************************************************************************************"

    #setup permissions
    chmod go-rwx,u+rwx config/server_config.cfg
    chmod go-x,u+x start.sh
    chmod go-x,u+x scripts/server_kill.sh
    exit

done
