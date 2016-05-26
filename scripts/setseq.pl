#!/usr/bin/perl
#
# This script replaces the first column of a trade file with a sequence of
# integers starting with "0001".
#

BEGIN { push @INC, $ENV{"MY_PERLSCRIPTS"} }

use warnings;
use strict;

use MyPerl::Utils qw(trim);

#
#open(MYFILE, "<myfile.txt");

#
my $USAGE = "\nUsage: perl setseq.pl <trades.txt\n";

my $seq = 1;

#-------------------------
# read and process input file
#-------------------------
while(<STDIN>)
{
	$_ = trim($_);

	#-------------------------
	#-------------------------
	if ($_ =~ /^#/ ) { # comment line
		print "$_\n";
	}
	elsif ($_ =~ /^([\d]*),(.*)/ ) { # is a trade
		my $oldSeq = $1;
		my $theRest = $2;
		printf("%04d,%s\n",$seq++,$theRest);
	}
	else {
		print "Found unexpected line:\n$_\n";
		exit;
	}
}
