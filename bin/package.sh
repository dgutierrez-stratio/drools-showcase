#!/bin/bash -e

. $(dirname "$0")/env.sh

echo "---> Building packages for directory 'secrets'"
export packageGroup="com.stratio.tais"
export packageArtifact="secrets"
envsubst < "src/main/resources/version.yml" > "src/main/resources/secrets/version.yml"

zip -r -j target/showcase-secrets-$VERSION.zip src/main/resources/secrets/*
sudo chmod 777 "target/showcase-secrets-$VERSION.zip"
rm "src/main/resources/secrets/version.yml"

echo "---> Building packages for directory 'examples'"
export packageGroup="com.stratio.tais"
export packageArtifact="examples"
envsubst < "src/main/resources/version.yml" > "src/main/resources/examples/version.yml"

zip -r -j target/showcase-examples-$VERSION.zip src/main/resources/examples/*
sudo chmod 777 "target/showcase-examples-$VERSION.zip"
rm "src/main/resources/examples/version.yml"


