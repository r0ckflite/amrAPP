#!/bin/bash

curl -X POST -d @problemCodes.json http://localhost:9000/trouble/updateProblemCodes --header "Content-Type:application/json"
