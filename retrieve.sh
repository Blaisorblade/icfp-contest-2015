#!/bin/sh
API_TOKEN="Ties5pfsheKfVJ7bIxBNICkNsJNUN0An7aCzbsf3CIU="
TEAM_ID=49
# OUTPUT is the more confusing part. Let's take in from standard input
curl --user :$API_TOKEN \
        https://davar.icfpcontest.org/teams/$TEAM_ID/solutions |sed -e 's/,/,\
/g' > solutions.txt
