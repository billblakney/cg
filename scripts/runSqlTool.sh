#!/bin/bash
# Runs the sql tool.
if [ $# -eq 0 ]
  then
    echo "missing db urlid"
    exit;
fi
dbdir=$CG_DB/$1
java -jar $HSQLDB/sqltool.jar --rcFile $dbdir/sqltool.rc $1
