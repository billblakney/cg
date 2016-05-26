#!/usr/bin/perl

use warnings;
use strict;

#
#open(DATA, "<echo.txt");

#
my $USAGE = "\nUsage: perl cvt_td_trades.pl <infile.txt\n";

#-------------------------
# read and process input file
#-------------------------
while(<STDIN>)
{
	$_ = trim($_);

	if( length($_) == 0 ) {
	}
	#-------------------------
	#-------------------------
	elsif ($_ =~ /^([^\s]+)\s+([^\s]+)\s+([^\s]+)\s+([^\s]+)\s+([^\s]+)\s+([^\s]+)\s+([^\s]+)\s+([^\s]+)\s+([^\s]+)/ ) {
		#if ($_ =~ /^([^\s]+)\s+(.*)\s*(.*)\s*(.*)\s*(.*)\s*(.*)\s*(.*)\s*(.*)\s*(.*)/ ) {
		my $date = $1;
		my $transaction = $2;
		my $action = $3;
		my $symbol = $4;
		my $quantity = $5;
		my $price = $6;
		my $principal_amount = $7;
		my $commission = $8;
		my $net_amount = $9;

		# change $action to Buy or Sell
		if( $action eq "Bought" ) {
			$action = "Buy";
		}
		elsif( $action eq "Sold" ) {
			$action = "Sell";
		}
		else {
			print("Found unexpected action: $action\n");
			exit;
		}

		# remove commas from quanity
		$quantity =~ s/,//g;

		# print the string
		print("$transaction,$date,$action,$symbol,$quantity,$price,$commission\n");
	}
	else {
		print "Found unexpected line:\n$_\n";
		exit;
	}
}

#--------------------------------------------------
# remove whitespace from the start and end of the string
#--------------------------------------------------
sub trim
{
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}

#--------------------------------------------------
# remove leading whitespace
#--------------------------------------------------
sub ltrim
{
	my $string = shift;
	$string =~ s/^\s+//;
	return $string;
}

#--------------------------------------------------
# remove trailing whitespace
#--------------------------------------------------
sub rtrim
{
	my $string = shift;
	$string =~ s/\s+$//;
	return $string;
}
