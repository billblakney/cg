#!/bin/bash
# Runs the swing manager.
# cd to the sql directory where the sql files are easily found.
dbdir=$CG_DB/$1/data
mkdir -p $dbdir
cd $dbdir
echo Running server in `pwd` ...
java -classpath $HSQLDB/hsqldb.jar org.hsqldb.server.Server --database.0 file:$1 --dbname.0 $1
