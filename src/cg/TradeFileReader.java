package cg;

import java.io.IOException;
import java.util.*;
import java.math.BigDecimal;

import util.*;

/**
 * This class reads a trade file and returns a Vector of the trades.
 */
public class TradeFileReader extends VectorFileReader {

	/**
	 * Default constructor.
	 */
	public TradeFileReader() {
	}

	/**
	 * Converts a String of format m+/d+/yyyy to a TradeDate.
	 * 
	 * @param line
	 *            String The string to be converted.
	 * @return TradeDate The resulting date.
	 */
	private SimpleDate stringToDate(String dateString) {
		int month = 0;
		int day = 0;
		int year = 0;
		StringTokenizer tok = new StringTokenizer(dateString, "/");
		try {
			month = Integer.valueOf(tok.nextToken()).intValue();
			day = Integer.valueOf(tok.nextToken()).intValue();
			year = Integer.valueOf(tok.nextToken()).intValue();
			// The year should be in "YYYY" format. But for some old trade
			// files it may be in "YY" format. The following block makes
			// adjustments to handle the "YY" case. Specifically, years with
			// values 00-49 will translate to 2000-2049, and the values
			// 50-99 will translate to 1950-1999.
			if( year < 1900 ){
				year += 1900;
				if (year < 1950)
					year += 100;
			}
		} catch (Exception e) {
			System.out.println("Error in TradeFileTable.stringToDate()");
		}
		SimpleDate fmtDate = new SimpleDate(year, month - 1, day);
		return fmtDate;
	}

	/**
	 * Converts a String of format ??? to a share price.
	 * 
	 * @param line
	 *            String The string to be converted.
	 * @return TradeDate The resulting share price.
	 */
	private float stringToSharePrice(String priceStr) {
		float sharePrice = (float) 0.0;

		System.out.println("priceStr: " + priceStr);
		StringTokenizer pt = new StringTokenizer(priceStr, " ");
		String part1 = null;
		String part2 = null;

		part1 = pt.nextToken();
		if (pt.hasMoreTokens())
			part2 = pt.nextToken();

		if (part2 == null && part1.indexOf('.') > -1) { // is decimal
			sharePrice = Float.valueOf(part1).floatValue();
		} else if (part2 == null) { // is simple frac
			StringTokenizer ft = new StringTokenizer(part1, "/");
			System.out.println("part1: " + part1);
			float num = Float.valueOf(ft.nextToken()).floatValue();
			System.out.println("num: " + num);
			float den = Float.valueOf(ft.nextToken()).floatValue();
			System.out.println("den: " + den);
			float fracPart;
			if (den < 1)
				fracPart = 0;
			else
				fracPart = num / den;
			sharePrice = fracPart;
		} else { // is compound frac
			float intPart = Float.valueOf(part1).floatValue();
			StringTokenizer ft = new StringTokenizer(part2, "/");
			float num = Float.valueOf(ft.nextToken()).floatValue();
			float den = Float.valueOf(ft.nextToken()).floatValue();
			float fracPart;
			if (den < 1)
				fracPart = 0;
			else
				fracPart = num / den;
			sharePrice = intPart + fracPart;
		}
		return sharePrice;
	}

	/**
	 * Converts a String to a Trade.
	 * 
	 * @param line
	 *            String The string to be converted.
	 * @return Object The resulting Trade object.
	 * @todo clean up note and instruction
	 */
	@Override
	public Object convertLineToObj(String line) {

		// Initialize the trade to be returned. If the line is parsed
		// successfully, the trade will be updated before being returned.

		BuyTrade bt = null;
		SellTrade st = null;

		try {

			// Initialize all of the items that will be parsed from 'line'.
			int tradeID = 0;
			Trade.Type tradeType = Trade.Type.BUY;
			String ticker = new String();
			int numShares = 0;
			float sharePrice;
			BigDecimal comm;
			Trade.SpecialInstruction instruction = Trade.SpecialInstruction.NO_INSTRUCTION;
			String note = new String();

			// Create a string tokenizer for the line to be parsed.
			StringTokenizer tok = new StringTokenizer(line, ",\t");

			// id
			tradeID = Integer.valueOf(tok.nextToken()).intValue();

			// date
			SimpleDate date = stringToDate(tok.nextToken());

			// BUY or SELL
			String typeStr = tok.nextToken();
			if (typeStr.equals("Buy"))
				tradeType = Trade.Type.BUY;
			else
				tradeType = Trade.Type.SELL;

			// ticker
			ticker = tok.nextToken();

			// num shares
			numShares = Integer.valueOf(tok.nextToken()).intValue();

			// share price
			sharePrice = stringToSharePrice(tok.nextToken());

			// commission
			comm = new BigDecimal(tok.nextToken());
			comm.setScale(2);

			// note
			if (tok.hasMoreTokens())
				note = tok.nextToken();

			if (note.startsWith("NO_WASH"))
				instruction = Trade.SpecialInstruction.NO_WASH;

			// Instantiate the Trade that represents 'line'.
			if (tradeType == Trade.Type.BUY) {
				bt = new BuyTrade(tradeID, date, tradeType, ticker, numShares,
						sharePrice, comm, instruction, note);
				return bt;
			} else {
				st = new SellTrade(tradeID, date, tradeType, ticker, numShares,
						sharePrice, comm, instruction, note);
				return st;
			}
		} catch (Exception e) {
			System.out.println("Error in Trade.convertLineToObj()");
			System.exit(1);
		}

		if (bt != null)
			return bt;
		else
			return st;
	}

	@Override
	public String convertObjToLine(Object obj) {
		return obj.toString();
	}

	/**
	 * Loads a trade file and returns the vector of trades read.
	 */
	public static TradeList loadTradeFile(String tradeFile) {

		Vector v = new Vector();

		// Load the trade file into a trade table.
		TradeFileReader reader = new TradeFileReader();
		try {
			v = reader.readVectorFile(tradeFile);
		} catch (IOException e) {
			System.out.println("reader.readVectorFile() failed");
			System.exit(1);
		}
		TradeList trades = new TradeList(v);
		return trades;
	}
}
