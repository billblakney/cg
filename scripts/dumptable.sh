#!/bin/bash
table="trade"
if [ ! $# -eq 0 ]
  then
    table=$1
fi
rcfile=$CG_DB/sqltool.rc
java -jar $HSQLDB/sqltool.jar --rcFile $rcfile --sql="select * from $table;" dbl
