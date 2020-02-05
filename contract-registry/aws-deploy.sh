#!/usr/bin/env sh

gradle clean build shadowJar

aws cloudformation package --template-file sam.yaml --output-template-file build/output-sam.yaml --s3-bucket cgoulding-random
aws cloudformation deploy --template-file build/output-sam.yaml --stack-name bindingz-contract-registry --capabilities CAPABILITY_IAM
aws cloudformation describe-stacks --stack-name bindingz-contract-registry