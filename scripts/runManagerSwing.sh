#!/bin/bash
# Runs the database manager.
dbdir=$CG_DB/$1/sql
cd $dbdir
java -classpath $HSQLDB/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing
