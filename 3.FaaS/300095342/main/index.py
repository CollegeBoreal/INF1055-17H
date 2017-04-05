# index.py
from __future__ import print_function # from __future__ helps ensure compatibility with Python 2 or 3.

import json

print('Loading function')


def main_handler(event, context):
    #print("Received event: " + json.dumps(event, indent=2))
    print("value1 = " + event['key1'])
    print("value2 = " + event['key2'])
    print("value3 = " + event['key3'])
    return event['key1']  # Echo back the first key value
    #raise Exception('Something went wrong')
