#!/bin/bash
:: Setup a link from a real database dir to cg/db/dbl.
:: @ECHO OFF
SET dbdir=%CG%/db
CD /d %dbdir%
RM dbl
MKLINK dbl %1
DIR
