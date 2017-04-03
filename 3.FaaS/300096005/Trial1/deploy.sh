#!/bin/bash

### Ensure the variable AWS_ACCOUNT_ID is set
# http://stackoverflow.com/questions/3601515/how-to-check-if-a-variable-is-set-in-bash
if [ -z ${AWS_ACCOUNT_ID+x} ]; then
    echo "variable AWS_ACCOUNT_ID is not set"
    exit 1
fi

### Create the lambda package
zip -j test.zip *.py

### Create the role for the lambda to assume
role="test_exec_role"
trust="trust.json"
aws iam create-role --role-name $role --assume-role-policy-document file://$trust
aws iam update-assume-role-policy --role-name $role --policy-document file://$trust

### Create the lambda function
function_name="test"
handler_name="test.lambda_handler"
package_file=test.zip
runtime=python2.7.9 
aws lambda create-function \
  --function-name $function_name \
  --handler $handler_name \
  --runtime $runtime \
  --memory 512 \
  --timeout 60 \
  --role arn:aws:iam::${AWS_ACCOUNT_ID}:role/$role \
  --zip-file fileb://$package_file

###   aws lambda invoke --invocation-type RequestResponse --function-name test --payload '[""]' output.txt