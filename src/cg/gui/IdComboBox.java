package cg.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javafx.util.Pair;
import javax.swing.JComboBox;

/**
 * Combobox for choosing from a list of (Integer,String) pairs. The Integer is
 * referred to as the "ID", and the String as its "name". The combobox displays
 * the names. The getSelectedId method returns the ID corresponding to the
 * selected name.
 * 
 * TODO move to library
 */
public class IdComboBox extends JComboBox<String>
{
   /**
    * Mapping of JComboBox selection indices to the IDs. The key is a selection
    * index, and the value is the corresponding ID.
    */
   protected Map<Integer, Integer> _idMap = new HashMap<Integer, Integer>();

   /**
    * Constructs an IdComboBox from a map of (ID,name) entries. To ensure the
    * desired order of names in the combobox, the user should use the
    * appropriate type of Map, e.g. TreeMap to order the selection list by the
    * natural ordering of the map keys, or LinkedHashMap to order the selection
    * list per the order items were added to the map.
    */
   public IdComboBox(Map<Integer, String> aIds)
   {
      int i = 0;
      for (Integer tId : aIds.keySet())
      {
         addItem(aIds.get(tId));
         _idMap.put(i++, tId);
      }
   }

   /**
    * Constructs an IdComboBox from a vector of (ID,name) pairs.
    */
   public IdComboBox(Vector<Pair<Integer, String>> aIds)
   {
      int i = 0;
      for (Pair<Integer, String> tIdPair : aIds)
      {
         addItem(tIdPair.getValue());
         _idMap.put(i++, tIdPair.getKey());
      }
   }

   /**
    * Get the ID of the selected JComboBox item.
    */
   public int getSelectedId()
   {
      System.out.println("selected index: " + getSelectedIndex());
      if (_idMap.containsKey(getSelectedIndex()))
      {
         return _idMap.get(getSelectedIndex());
      }
      return -1;
   }
}
