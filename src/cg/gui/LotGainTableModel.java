package cg.gui;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;
import cg.GainAccessor;
import cg.OldLot;

/*
 * Note: These three methods must be implemented to extend AbstractTableModel
 *   public int getRowCount();
 *   public int getColumnCount();
 *   public Object getValueAt(int row, int column);
 *   TODO remove computed and tax year?
 */
public class LotGainTableModel extends AbstractTableModel implements
      GainColumnFinder
{

   private String[] columnNames = { "Shares", "Ticker", "Buy Date",
         "Buy Price", "Sell Date", "Sell Price", "Gross", "Basis", "Gain",
         "Term"/*
                * zzz, "Computed Tax Year", "Reported Tax Year"
                */};

   final static int COL_SHARES = 0;
   final static int COL_TICKER = 1;
   final static int COL_BUYDATE = 2;
   final static int COL_BUYPRICE = 3;
   final static int COL_SELLDATE = 4;
   final static int COL_SELLPRICE = 5;
   final static int COL_GROSS = 6;
   final static int COL_BASIS = 7;
   final static int COL_GAIN = 8;
   final static int COL_TERM = 9;
   // final static int COL_CTAXYEAR = 10;
   // final static int COL_RTAXYEAR = 11;

   private Vector data;

   LotGainTableModel()
   {
      data = new Vector(0);
   }

   @Override
   public int getGainColumn()
   {
      return COL_GAIN;
   }

   public int getColumnCount()
   {
      return columnNames.length;
   }

   public int getRowCount()
   {
      return data.size();
   }

   @Override
   public String getColumnName(int col)
   {
      return columnNames[col];
   }

   @Override
   public Class getColumnClass(int c)
   {
      if (data.size() > 0)
      {
         Object value = getValueAt(0, c);
         return (value == null ? Object.class : value.getClass());
      }
      else
         return Object.class;
   }

   public Object getValueAt(int arg0, int arg1)
   {
      if (arg0 > data.size() || arg1 > columnNames.length) throw new ArrayIndexOutOfBoundsException();
      else
         return ((Vector) (data.elementAt(arg0))).elementAt(arg1);
   }

   public void setData(Vector<GainAccessor> aGains)
   {
      data = new Vector();
      for (int i = 0; i < aGains.size(); i++)
         data.addElement(getDataRow(aGains.elementAt(i)));
      fireTableDataChanged();
   }

   /**
    * Get vector data for a gain.
    */
   private Vector<GainAccessor> getDataRow(Object row)
   {
      GainAccessor tGain = (GainAccessor) row;

      Vector v = new Vector();
      v.addElement(tGain.getNumShares()); // 0
      v.addElement(tGain.getSymbol()); // 1
      v.addElement(tGain.getBuyDate()); // 2
      v.addElement(tGain.getBuyPrice()); // 3
      v.addElement(tGain.getSellDate()); // 4
      v.addElement(tGain.getSellPrice()); // 5
      v.addElement(new Integer(Math.round(tGain.getProceeds()))); // 6
      v.addElement(new Integer(Math.round(tGain.getBasis()))); // 7
      v.addElement(new Integer(Math.round(tGain.getGain()))); // 8
      v.addElement(tGain.getTerm()); // 9
      // v.addElement(new Integer(lot.computedTaxYear.intValue())); //10
      // v.addElement(new Integer(lot.claimedTaxYear.intValue())); //11
      return v;
   }

   public Integer getTotalGain(RowFilter<AbstractTableModel, Integer> filter)
   {
      Integer totalGain = new Integer(0);
      for (int i = 0; i < data.size(); i++)
      {
         RowFilter.Entry<AbstractTableModel, Integer> entry = new TableModelFilterEntry(
               (AbstractTableModel) this, i);
         // pass over entry if filter.include() returns false
         if ((filter != null) && (filter.include(entry) == false)) continue;
         Integer gain = (Integer) entry.getValue(LotGainTableModel.COL_GAIN);
         totalGain += gain;
      }
      return totalGain;
   }
}
