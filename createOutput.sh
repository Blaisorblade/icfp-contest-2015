#!/bin/sh
# Using stderr for the actual output, and stdout for diagnostics, is backward,
# but that's what SBT offers. At the same time, actual errors will go in the
# file.
sbt run 2> output.txt
