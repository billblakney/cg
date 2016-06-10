#!/bin/bash
# Runs the swing database manager.
# Startup in the sql directory for easier access to sql scripts.
sqldir=$CG_DB/dbl/sql
cd $sqldir
dbdir=$CG_DB/dbl
java -Ddbdir=$dbdir -classpath $HSQLDB/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing --rcFile $CG_DB/sqltool.rc --urlid dbl
