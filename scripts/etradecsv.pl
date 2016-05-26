#!/usr/bin/perl
#
# Converts an ETrade transactions csv file to trade file entries
# and prints them to the terminal.
# The name of the csv file is the first and only command line arg.
# Note that the order of the trades in the csv file downloaded from
# ETrade is sometimes out of order, i.e. it may list a buy and sell
# in the wrong order if they are in the same day. Need to look at
# trade number and manually fix the csv or resulting trade file
# output.

BEGIN { push @INC, $ENV{"MY_PERLSCRIPTS"} }

use strict;
use warnings;

use MyPerl::Utils qw(msg trim);

my $USAGE = "\nUsage: etradecsv.pl [-h] etradeCsvFilename
      -h                # print this message
      etradeCsvFilename # name of etrade csv file to be converted
";

# See if user wants help.
foreach my $arg (@ARGV) {
   if( $arg eq "-h" | $arg eq "-help" | $arg eq "help" ){
      print $USAGE;
      exit;
   }
}

# Need at least one trade file to process.
if( @ARGV == 0 ){
   print "No etrade csv filename found on command line\n";
   exit;
}

# open csv file
open(my $fh, "<", $ARGV[0])
   or die "Failed to open $ARGV[0]\n";

my @trades;
my $date;
my $type;
my $symbol;
my $quantity;
my $price;
my $comm;

# Read lines from the csv file. Save those that are trades into @trades.
while(<$fh>) {
   chomp;
   trim($_);
   #            date  , B/S         ,EQ,  sym   , +/-quant , amount   , price , commission
   if( $_ =~ /^([^,]+),(Bought|Sold),EQ,([A-Z]+),\-?([\d]+),\-?[0-9]+\.?[0-9]*,(\d+\.?\d*),(\d+\.?\d*)/ ){
      #print "matched $1,$2,$3,$4,$5,$6\n";
      $date = $1;
      $type = $2;
      $symbol = $3;
      $quantity = $4;
      $price = $5;
      $comm = $6;

      # todo: change cap gains app and eliminate this
      if( $type eq "Bought" ){
         $type = "Buy";
      }
      else{
         $type = "Sell";
      }

      my $trade = join(",", $date, $type, $symbol, $quantity, $price, $comm);
      #print "new trade: $trade\n";
      push(@trades,$trade);
   }
}

close($fh);

# Print the trades. Need to reverse order from the csv file.
# Note: maybe for a given day, trades are in correct order (even though daily are in reverse)?
@trades = reverse @trades;

print join("\n",@trades);
