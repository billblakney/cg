#!/usr/bin/perl
#
# Compiles the yearly trade tables in the current directory into an "all trades" file.
# Trade filenames must be like "trades.<yyyy>.txt".
# The all trades file is created with name "trades.all.txt".
# Comments are preserved (lines starting with "#").

use strict;
use warnings;

BEGIN { push @INC, $ENV{"PERLSCRIPTS"} }

#use MyPerl::Utils qw(msg readFile createBakFiles printlist);
use MyPerl::Utils qw(msg readFile createBakFiles printlist);
use File::Copy qw(copy);

my $USAGE = "\nUsage: alltrades.pl [-h] [-p] [-s]
      -h      # print this message
      -p      # preview; show what will be done, but don't execute
      -s      # run silently (no output to terminal)
";

# command line settings
my $preview  = 0;
my $talking  = 1;

# read command line settings
foreach my $arg (@ARGV) {
   if( $arg eq "-h" | $arg eq "-help" | $arg eq "help" ){
      print $USAGE;
      exit;
   }
   elsif( $arg eq "-p" ){
      $preview = 1;
   }
   elsif( $arg eq "-s" ){
      $talking = 0;
   }
}

# directory to work in
my $directory = '.';

#-------------------------
# get list of trade file names
#-------------------------
msg("getting list of trade files from directory $directory\n");
my @tradefiles = getTradeFiles();
@tradefiles = sort @tradefiles;
#printlist("tradefiles:",@tradefiles);

#-------------------------
# read all trade files into an array
#-------------------------
my @lines;
foreach my $file (@tradefiles){
   my @morelines = readFile($file);
   push(@lines,@morelines);
}

#-------------------------
# Set seq number for all trades in @lines.
#-------------------------
my $seq = 1;
foreach my $item (@lines){
   if ($item =~ /^#/ ) { # comment line
   }
   else {
      $item = sprintf("%05d,%s",$seq++,$item);
   }
}

#-------------------------
# Create "all trades" file.
#-------------------------
my $allTradesFile = "allTrades.txt";

# Backup existing file if it exists.
if( -e $allTradesFile ){
   msg("backing up up existing $allTradesFile\n");
   createBakFiles($allTradesFile);
}

# Open the file and write all lines to it.
open(my $fh, ">", $allTradesFile)
   or die "Could not open $allTradesFile\n";

foreach my $line (@lines){
   print $fh $line . "\n";
}

close $fh;

#-------------------------
# Copy the all trades file to the ../all directory, with a new name that
# takes the tail of the current directory as the root of the copied file
# name. The copied file is given the "txt" extension.
#-------------------------
my $acctFile = getAccountFile();
print "acctFile: $acctFile\n";
copy($allTradesFile,$acctFile);

#-------------------------
# get list of trade file names
#-------------------------
sub getTradeFiles
{
   my @files;
   opendir (DIR, $directory) or die $!;
   while (my $file = readdir(DIR)) {
      #if ($file =~ /^trades.([1-2][0-9][0-9][0-9]).txt$/){
      if ($file =~ /^trades.([1-2][0-9][0-9][0-9]).txt$/){
         #print "$file\n";
         push( @files, $file);
      }
   }
   closedir(DIR);
   @files;
}

#-------------------------
# get list of trade file names
#-------------------------
sub getAccountFile
{
   my $currentDir =  $ENV{'PWD'};
   my @pathComponents = split /\//, $currentDir;
   my $lastComponent = pop @pathComponents;
   my $parentDir = join '/', @pathComponents;
   my $destinationDir = join '/', $parentDir, "all";
   my $fileName = $lastComponent . ".txt";
   my $destinationFile = $destinationDir . "/" . $fileName;
}
