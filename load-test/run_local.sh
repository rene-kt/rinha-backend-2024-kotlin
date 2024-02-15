#!/bin/bash

# Defina a localização do Gatling
GATLING_HOME=/home/rene/Downloads/gatling-charts-highcharts-bundle-3.10.3-bundle/gatling-charts-highcharts-bundle-3.10.3

GATLING_TEST_DIR=/home/rene/Desktop/rinhadebackend/load-test/user-files/simulations/rinhabackend

# Execute o teste Gatling
$GATLING_HOME/bin/gatling.sh -sf $GATLING_TEST_DIR
