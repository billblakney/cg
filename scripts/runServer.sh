#!/bin/bash
# Runs the database server.
# cd to the sql directory where the sql files are easily found.
dbdir=$CG_DB/dbl/data
mkdir -p $dbdir
cd $dbdir
echo Running server in `pwd` ...
java -classpath $HSQLDB/hsqldb.jar org.hsqldb.server.Server --database.0 file:dbl --dbname.0 dbl
