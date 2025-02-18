#!/bin/bash

# Check if the environment variable is set
if [ -z "$srcprj" ]; then
  echo "The environment variable srcprj is not set."
  exit 1
fi

# Check if at least one parameter is provided
if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <parameter1> <parameter2> ... <parameterN>"
  exit 1
fi

# Loop through each parameter
for x in "$@"; do
  # Execute the cat command to overwrite the content for each parameter
  # shellcheck disable=SC2154
  cat $srcprj/$x/*  > $ing1/projet-web-mobile/UniversitySchedule_backend/prompts/$x.txt
  echo "Content from $x/* has been written to propmts/$x.txt"
done
