#! /bin/bash

echo Extracting new application

# define variables...
EXTRACTED=$(mktemp -d)
TARGET=/home/gitlab-runner/deployment

tar -xf build/distributions/Teach4Service*.tar -C ${EXTRACTED}
cp -rf ${EXTRACTED}/Teach4Service*/* ${TARGET}/
rm -rf ${EXTRACTED}

echo Copying over new config.yml

# move the config file over
cp -f deploy/config.yml ${TARGET}/config.yml

cd ${TARGET}

# replace tokens in config file
sed -i "s/DB_DATABASE/${AWS_DB_DATABASE}/g" config.yml
sed -i "s/DB_USER/${AWS_DB_USER}/g" config.yml
sed -i "s/DB_PASSWORD/${AWS_DB_PASSWORD}/g" config.yml
sed -i "s/COOKIE_SECRET/${AWS_COOKIE_SECRET}/g" config.yml
sed -i "s/ADMIN_PASSWORD/${AWS_ADMIN_PASSWORD}/g" config.yml
sed -i "s/OT_SECRET/${AWS_OT_SECRET}/g" config.yml

sudo systemctl stop Teach4Service

echo Doing database migrations
./bin/Teach4Service db migrate config.yml

echo Starting application
sudo systemctl start Teach4Service
