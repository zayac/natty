#!/bin/bash
echo "Downloading and installing postgresql..."
pacman -S postgresql --needed --noconfirm --quiet
if [ $? -eq 0 ] ; then
    echo "Download and installation successful. Starting daemon..."
else
    echo "aw, crap. Pacman failed."
    exit
fi
/etc/rc.d/postgresql restart >> /dev/null 
if [ $? -eq 0 ] ; then
    echo "Daemon has started successfully"
fi
echo 'Creating natty user...'
su - postgres -c "echo 'Please enter the password for nattyuser user:';createuser -W -S -R -d -e nattyuser;createdb -e -O nattyuser natty"
psql -U nattyuser -d natty < init.sql
