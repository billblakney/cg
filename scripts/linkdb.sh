#!/bin/bash
# Runs the database manager.
# Startup in the sql directory for easier access to sql scripts.
if [ $# -eq 0 ]
  then
    echo "missing db urlid"
    exit;
fi
link=$CG_DB/dbl
dbdir=$CG_DB/$1
echo "removing db link $link..."
rm $link
echo "creating db link to $dbdir..."
ln -s $dbdir $link
