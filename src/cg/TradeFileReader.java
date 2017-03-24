package cg;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import util.*;

/**
 * Reads a trade file and returns a Vector of the trades.
 */
public class TradeFileReader extends VectorFileReader
{
   /**
    * Loads a trade file and returns the vector of trades read.
    * @param Name of file to read.
    * @return Vector of trades from the file.
    */
   public static TradeList loadTradeFile(String tradeFile)
   {

      Vector<OldTrade> tTradeVector = new Vector<OldTrade>();

      // Load the trade file into a trade table.
      TradeFileReader tReader = new TradeFileReader();
      try
      {
         tTradeVector = tReader.readVectorFile(tradeFile);
      }
      catch (IOException e)
      {
         System.out.println("reader.readVectorFile() failed");
         System.exit(1);
      }

      TradeList trades = new TradeList(tTradeVector);
      return trades;
   }

   /**
    * Default constructor.
    */
   public TradeFileReader()
   {
   }

   /**
    * Converts a String to a Trade.
    * 
    * @param line String The string to be converted.
    * @return The resulting Trade object.
    * @todo clean up note and instruction
    */
   @Override
   public Object convertLineToObj(String line)
   {
      // Initialize the trade to be returned. If the line is parsed
      // successfully, the trade will be updated before being returned.
      // System.out.println("bbb converting: " + line);

      BuyTrade bt = null;
      SellTrade st = null;

      try
      {
         // Initialize all of the items that will be parsed from 'line'.
         int tradeID = 0;
         OldTrade.Type tradeType = OldTrade.Type.BUY;
         String ticker = new String();
         int numShares = 0;
         float sharePrice;
         BigDecimal comm;
         OldTrade.SpecialInstruction instruction = OldTrade.SpecialInstruction.NO_INSTRUCTION;
         String note = new String();

         // Create a string tokenizer for the line to be parsed.
         StringTokenizer tok = new StringTokenizer(line, ",\t");

         // id
         tradeID = Integer.valueOf(tok.nextToken()).intValue();

         // date
         SimpleDate date = stringToDate(tok.nextToken());

         // BUY or SELL
         String typeStr = tok.nextToken();
         if (typeStr.equals("Buy")) tradeType = OldTrade.Type.BUY;
         else
            tradeType = OldTrade.Type.SELL;

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
         if (tok.hasMoreTokens()) note = tok.nextToken();

         if (note.startsWith("NO_WASH")) instruction = OldTrade.SpecialInstruction.NO_WASH;

         // Instantiate the Trade that represents 'line'.
         if (tradeType == OldTrade.Type.BUY)
         {
            bt = new BuyTrade(tradeID, date, tradeType, ticker, numShares,
                  sharePrice, comm, instruction, note);
            return bt;
         }
         else
         {
            st = new SellTrade(tradeID, date, tradeType, ticker, numShares,
                  sharePrice, comm, instruction, note);
            return st;
         }
      }
      catch (Exception e)
      {
         System.out.println("Error in Trade.convertLineToObj()");
         System.exit(1);
      }

      if (bt != null) return bt;
      else
         return st;
   }

   /**
    * Note: not really used, so just give any implementation.
    * @param obj
    * @return
    */
   @Override
   public String convertObjToLine(Object obj)
   {
      return obj.toString();
   }

   /**
    * Converts a String of format m+/d+/yyyy to a TradeDate.
    * 
    * @param line String The string to be converted.
    * @return SimpleDate The resulting date.
    */
   private SimpleDate stringToDate(String dateString)
   {
      int month = 0;
      int day = 0;
      int year = 0;
      StringTokenizer tok = new StringTokenizer(dateString, "/");
      try
      {
         month = Integer.valueOf(tok.nextToken()).intValue();
         day = Integer.valueOf(tok.nextToken()).intValue();
         year = Integer.valueOf(tok.nextToken()).intValue();
         // The year should be in "YYYY" format. But for some old trade
         // files it may be in "YY" format. The following block makes
         // adjustments to handle the "YY" case. Specifically, years with
         // values 00-49 will translate to 2000-2049, and the values
         // 50-99 will translate to 1950-1999.
         if (year < 1900)
         {
            year += 1900;
            if (year < 1950) year += 100;
         }
      }
      catch (Exception e)
      {
         System.out.println("Error in TradeFileTable.stringToDate()");
      }
      SimpleDate fmtDate = new SimpleDate(year, month - 1, day);
      return fmtDate;
   }

   /** Share price patterns. */
   private static final Pattern tDecimalPattern = Pattern
         .compile("^(\\d+\\.\\d+)$");
   private static final Pattern tIntegerPattern = Pattern.compile("^(\\d+)$");
   private static final Pattern tProperFractionPattern = Pattern
         .compile("^(\\d+)\\/(\\d+)$");
   private static final Pattern tMixedFractionPattern = Pattern
         .compile("^(\\d+)\\s+(\\d+)\\/(\\d+)$");

   /**
    * Converts a String to a share price. Examples of the valid formats:
    * "3.45", "3", "5/8", "20 5/8"
    * 
    * @param priceStr String The string to be converted.
    * @return The resulting share price.
    */
   private float stringToSharePrice(String priceStr)
   {
      float sharePrice = (float) 0.0;

      Matcher tDecimalMatcher = tDecimalPattern.matcher(priceStr);
      if (tDecimalMatcher.find())
      {
         sharePrice = Float.valueOf(tDecimalMatcher.group(0)).floatValue();
      }
      else
      {
         Matcher tIntegerMatcher = tIntegerPattern.matcher(priceStr);
         if (tIntegerMatcher.find())
         {
            sharePrice = Float.valueOf(tIntegerMatcher.group(0)).floatValue();
         }
         else
         {
            Matcher tProperFractionMatcher = tProperFractionPattern
                  .matcher(priceStr);
            if (tProperFractionMatcher.find())
            {
               float tNum = Float.valueOf(tProperFractionMatcher.group(1));
               float tDen = Float.valueOf(tProperFractionMatcher.group(2));
               sharePrice = tNum / tDen;
            }
            else
            {
               Matcher tMixedFractionMatcher = tMixedFractionPattern
                     .matcher(priceStr);
               if (tMixedFractionMatcher.find())
               {
                  float tWholePart = Float.valueOf(tMixedFractionMatcher
                        .group(1));
                  float tNum = Float.valueOf(tMixedFractionMatcher.group(2));
                  float tDen = Float.valueOf(tMixedFractionMatcher.group(3));
                  sharePrice = tWholePart + tNum / tDen;
               }
               else
               {
                  System.out.println("ERROR: "
                        + "Bad price in TradeFileReader.convertLineToObj(): "
                        + priceStr);
                  System.exit(1);
               }
            }
         }
      }
      return sharePrice;
   }
}
