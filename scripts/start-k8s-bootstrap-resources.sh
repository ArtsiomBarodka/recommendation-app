#!/bin/bash
function runK8sResource() {
  FOLDER=$1
  NAME=$2

  CURRENT=$(pwd)

  cd $FOLDER

  echo "Starting $NAME items..."
  kubectl apply -f .
  sleep 3s
  echo

  cd $CURRENT
}

cd ../k8s

runK8sResource security/ security
kubectl get persistentvolume
kubectl get persistentvolumeclaim
echo "Completed starting shared items"

echo "Starting bootstrap items"
runK8sResource bootstrap/kafka kafka
runK8sResource bootstrap/mysql-read mysql-read
runK8sResource bootstrap/mysql-write mysql-write

kubectl get pods
sleep 10s
echo "Completed starting bootstrap items"