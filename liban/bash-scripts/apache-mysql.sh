#!/bin/bash

if [ $(id -u) != 0 ]; then
        echo "This script requires sudo privileges."
        exit
else
        echo 'Auto Setup Script'
fi

read -s -p "Enter a new password for MySQL: " pass
echo "\n"
read -s -p "Re-enter the password: " conPass
echo "\n"

if [ $pass != $conPass ]; then
        echo "Passwords are not equal."
        exit
fi

# Updates system
#apt-get update -y
#apt-get upgrade -y

# Install expect, Apache & MySQL server
apt-get install expect -y
#apt-get install apache2 -y

# Setup auto response during installation
debconf-set-selections <<< "mysql-server mysql-server/root_password password $pass"
debconf-set-selections <<< "mysql-server mysql-server/root_password_again password $pass"
apt-get install mysql-server -y

# Configures MySQL
expect -f - <<-CONF
        set timeout 1
        spawn mysql_secure_installation

	expect "Enter password for user root: "
	send "$pass\r"

        expect "Press y|Y for Yes, any other key for No: "
        send "n\r"

	expect "Change the password for root ? ((Press y|Y for Yes, any other key for No) : "
	send "n\r"

        expect "Remove anonymous users? (Press y|Y for Yes, any other key for No) : "
        send "y\r"

        expect "Disallow root login remotely? (Press y|Y for Yes, any other key for No) : "
        send "n\r"

        expect "Remove test database and access to it? (Press y|Y for Yes, any other key for No) : "
        send "y\r"

        expect "Reload privilege tables now? (Press y|Y for Yes, any other key for No) : "
        send "y\r"

        expect eof
CONF


# Creates user table
mysql -uroot -p"$pass" <<-QUERY
        DROP DATABASE IF EXISTS project;
        CREATE DATABASE project;
        USE project;
        CREATE TABLE user (
                name VARCHAR(255) NOT NULL,
                email VARCHAR(255) PRIMARY KEY,
                age INT(3) NOT NULL,
                password VARCHAR(64) NOT NULL);
QUERY