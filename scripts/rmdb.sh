#!/bin/bash
# Runs the database manager.
# Startup in the sql directory for easier access to sql scripts.
#if [ $# -eq 0 ]
#  then
#    echo "missing db urlid"
#    exit;
#fi
datadir=$CG_DB/dbl/data
rm -rf $datadir
mkdir $datadir
