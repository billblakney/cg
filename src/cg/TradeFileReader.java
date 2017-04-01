package cg;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import bbj.file.TextFileReader;
import util.*;

/**
 * Reads a trade file and returns a Vector of the trades.
 */
public class TradeFileReader
{
   /**
    * Default constructor.
    */
   public TradeFileReader()
   {
   }

   /**
    * Loads a trade file and returns the vector of trades read.
    * @param Name of file to read.
    * @return Vector of trades from the file.
    */
   public static TradeList loadTradeFile(String tradeFile)
   {

      Vector<OldTrade> tTradeVector = new Vector<OldTrade>();

      OldTradeLineConverter tTradeConverter =
            new OldTradeLineConverter();
      
      TextFileReader<OldTrade> tReader =
            new TextFileReader<OldTrade>(tradeFile,tTradeConverter);

      tReader.setCommentLineTest(
            (line)->((line.charAt(0)=='#')?true:false));

      tReader.setTerminateLineTest(
            (line)->((line.charAt(0)=='@')?true:false));

      try
      {
         tTradeVector = tReader.getVector();
      }
      catch (IOException e)
      {
         System.out.println("Failed to read and convert " + tradeFile);
         System.exit(1);
      }

      TradeList trades = new TradeList(tTradeVector);

      return trades;
   }
}
