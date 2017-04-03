#!/bin/bash
role="test_exec_role"
function_name="test"
aws lambda delete-function --function-name $function_name
aws iam delete-role --role-name $role