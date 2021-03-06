package cg.gui;

import javax.swing.*;
import cg.*;

/**
 * 
 *
 */
public class ReportTabbedPane extends JTabbedPane
{

   public ReportTabbedPane()
   {
   }

   /**
    * Adds a report tab for the specified report type for the specified account.
    * 
    * @param accountName
    *           type Type of report.
    * @param type
    *           Type of report.
    * @param aAccount
    *           Account being reported.
    */
   public void addReport(ReportType type,AbstractAccountData aAccount)
   {
      JPanel newPanel = getReportPanel(type, aAccount);
      String title = aAccount.getName() + " - " + type.toString();
      addTab(title, newPanel);
      int index = indexOfComponent(newPanel);
      setTabComponentAt(index, new ButtonTabComponent(this));
   }

   /**
    * Change the account displayed by all the report tabs.
    * 
    * @param acct
    */
   @Deprecated
   // not sure if will have any use
   public void changeAccount(AbstractAccountData acct)
   {
      int count = getTabCount();
      for (int i = 0; i < count; i++)
      {
         AccountReportPanel panel = (AccountReportPanel) getComponentAt(i);
         panel.setAccount(acct);
      }
   }

   private JPanel getReportPanel(ReportType type, AbstractAccountData acct)
   {
      if (type == ReportType.ALL_TRADES) return new TradePanel(acct);
      else if (type == ReportType.SHARES_HELD) return new SharesHeldPanel(acct);
      else if (type == ReportType.LOTS_HELD) return new LotsHeldPanel(acct);
      else if (type == ReportType.TAX_GAINS) return new GainPanel(acct);
      else if (type == ReportType.TAX_GAIN_LOTS) return new LotGainPanel(acct);
      else if (type == ReportType.GAINS_BY_SECURITY) return new SecurityGainsPanel(
            acct);
      else if (type == ReportType.GAINS_BY_YEAR) return new YearlyGainsPanel(
            acct);
      else
      {
         System.out.println("Error: unrecognized report type");
         return new JPanel();
      }
   }
}
