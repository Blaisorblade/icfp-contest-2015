#!/bin/sh
# Using stderr for the actual output, and stdout for diagnostics, is backward,
# but that's what SBT offers. At the same time, actual errors will go in the
# file.
exec 3>&1
sbt run 2>&1 1>&3 |sed -e 's/,/,\
/g' > output.txt
