#!/bin/bash -e

. $(dirname "$0")/env.sh

packages_dir=${BASEDIR}/packages
descriptors=($(find "${packages_dir}" -type f -print))
success=true


echo "---> Testing descriptors"
virtualenv -p `which python3` env
source env/bin/activate
pip install pytest docker
pytest
deactivate
rm -r env

if ${success}; then
    echo "$(tput sgr0)---> Test successfully passed"

    echo "---> Testing drools files"
    mvn test -s settings.xml

    echo "---> Tests successfully passed"
else
    echo "$(tput sgr0)---> Errors encountered during test evaluation"
    exit 1
fi
