#!/usr/bin/perl
#
# Removes the first column from a trade file.
# Use to convert legacy trade files with seq num in the first
# column to the newer format that does not have a seq num column.
# After all legacy files have been convered, this file becomes obsolete.

BEGIN { push @INC, '/cygdrive/m/workspace/perlscripts' }

use strict;
use warnings;

use MyPerl::Utils qw(msg readFile createBakFiles printlist);

my $USAGE = "\nUsage: rmseq.pl <tradefilename1> ...";

# See if user wants help.
foreach my $arg (@ARGV) {
   if( $arg eq "-h" | $arg eq "-help" | $arg eq "help" ){
      print $USAGE;
      exit;
   }
}

# Need at least one trade file to process.
if( @ARGV == 0 ){
   print "No trade files found on command line\n";
   exit;
}

# Backup trade files before conversion.
createBakFiles(@ARGV);

# Convert the trade files.
foreach my $arg (@ARGV) {
   my @lines = readFile($arg);

   foreach my $item (@lines){
      if ($item =~ /^#/ ) { # comment line
      }
      elsif ($item =~ /^([\d]*),(.*)/ ) { # is a trade
         my $oldSeq = $1;
         my $theRest = $2;
         $item = sprintf("%s",$theRest);
      }
      else {
         print "Found unexpected line:\n$item\n";
         exit;
      }
   }

   open(my $fh, ">", $arg)
      or die "Could not open $arg\n";

   foreach my $line (@lines){
      print $fh $line . "\n";
   }

   close $fh;
}
