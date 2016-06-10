#!/bin/bash
# Runs the sql tool.
# Optional $1 is sql script to run.
dbdir=$CG_DB/dbl
java -Ddbdir=$dbdir -jar $HSQLDB/sqltool.jar --rcFile $CG_DB/sqltool.rc dbl $1
