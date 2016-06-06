#!/bin/bash
# Runs the database manager.
# Startup in the sql directory for easier access to sql scripts.
if [ $# -eq 0 ]
  then
    echo "missing db urlid"
    exit;
fi
dbdir=$CG_DB/$1
sqldir=$CG_DB/$1/sql
cd $sqldir
java -classpath $HSQLDB/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing --rcFile $dbdir/sqltool.rc --urlid $1
