#!/bin/bash

java -jar /shareKernel.jar &
java -jar /domain.jar &
java -jar /application.jar &
java -jar /infrastructure.jar &
java -jar /presentation.jar &

wait