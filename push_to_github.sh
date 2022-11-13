#!bin/bash

echo '######################################'
echo '##### push source code to github #####'
echo '######################################'

## push source code to github
#################
echo '##########################'
echo '######push to github######'
echo '##########################'
time=$(date "+%Y-%m-%d %H:%M:%S")  # get current time
git status
git pull
git add .
git commit -m "only push at ${time}"
git push origin master
