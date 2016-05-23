package util;

import java.io.*;
import java.util.*;

/**
 * Converts a formatted text file into a Vector of objects.<br><br>
 *
 * The file to be read is expected to contain a list of lines, with each line
 * representing one object. The 'readVectorFile' method reads the lines of the
 * file, converts them into objects, and stores them in a Vector, which is
 * returned to the caller. It is up to the concrete subclass to provide the
 * implementation of the 'convertLineToObj' method, which does the line
 * conversion.<br><br>
 *
 * The file to be read can contain comment lines (which will not be converted
 * to objects). By default, comment lines are indicated by a "#" character in
 * the first position (after the line is "trimmed").  This criteria can be
 * modified by overriding the 'isCommentLine' method.
 */
public abstract class VectorFileReader
    extends Vector {

    /**
     * Converts a line of text into an object. The format of the line and the
     * types of objects to be created are defined by the subclass that
     * implements this method.  This method is called by the 'readVectorFile'
     * method to convert each non-comment line to an object.
     *
     * @param line Line to be converted.
     * */
   public abstract Object convertLineToObj(String line);

   /**
    * Converts and object to line of text. The format of the line of text
    * should be the same format expected by the 'convertLineToObj'. <i><b>This
    * method is used by the 'writeVectorFile" method, which is not yet
    * supported.</i></b>
    *
    * @param obj Object to be converted.
    **/
   public abstract String convertObjToLine(Object obj);

   /**
    * Default constructor.
    * */
   public VectorFileReader() {
   }

   /**
    * Reads lines from a file, converts them to objects, and adds them to the
    * Vector to be returned. Comment lines are not processed. The
    * 'isCommentLine' method is used to identify comment lines.
    *
    * @param fileName Name of the input file.
    * */
   public final Vector readVectorFile(String fileName) throws IOException {
      Vector vector = new Vector();
      try {
         FileReader fileReader = new FileReader(fileName);
         LineNumberReader lineReader = new LineNumberReader(fileReader);

         String line;
         while ( (line = lineReader.readLine()) != null) {
            //System.out.println(line);
            if (isCommentLine(line) == false) {
               System.out.println(line);
               Object obj = convertLineToObj(line);
               vector.addElement(obj);
            }
         }
      }
      catch (FileNotFoundException e) {
         System.out.println("FileNotFoundException");
         System.exit(1);
      }
      catch (IOException e) {
         System.out.println("IOException");
         System.exit(1);
      }
      return vector;

   }
   /**
    * Tests a string to see if it is a comment line. This method is called by
    * 'readVectorFile' to determine which lines of the input file are comment
    * lines. The default implementation considers any line that begins with a
    * '#' character to be a comment line. (Note that the line is "trimmed"
    * before checking the first character.)<br><br>
    *
    * Subclasses can modify this method to change the criteria for determining
    * which lines of the input file are comment lines.
    *
    * @param line Line to be tested.
    * */
   public boolean isCommentLine(String line) {
      line.trim();
      if (line.charAt(0) == '#') {
         return true;
      }
      else {
         return false;
      }
   }
}
