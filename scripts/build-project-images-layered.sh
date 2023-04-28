#!/bin/bash

function unpack() {
  FOLDER=$1
  NAME=$2

  CURRENT=$(pwd)

  cd $FOLDER/build/libs
  java -jar -Djarmode=layertools ${NAME}.jar extract

  cd $CURRENT
}

function build() {
  FOLDER=$1
  NAME=$2

  docker build -f ./docker/Dockerfile \
    --build-arg JAR_FOLDER=${FOLDER}/build/libs \
    -t ${NAME}:latest \
    -t ${NAME}:layered .

  docker push ${NAME}:layered
}

cd ..

echo "Building JAR files"
gradle clean bootJar

echo "Unpacking JARs"
unpack currency currency
unpack aggregator aggregator
unpack api-gateway api-gateway

echo "Building Docker image"
build currency artsiombarodka/currency
build aggregator artsiombarodka/aggregator
build api-gateway artsiombarodka/api-gateway
