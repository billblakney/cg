package cg.gui;

import java.io.*;
import java.util.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import cg.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 *
 * @author Bill Blakney
 * @version 1.0
 */

public class AccountTreeCellRenderer extends JLabel
implements TreeCellRenderer {

  public AccountTreeCellRenderer() {

    setFont(new Font("Monospaced", Font.PLAIN, 12));
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value,
                           boolean selected, boolean leaf, boolean expanded,
                           int row, boolean hasFocus) {
    if( value instanceof DefaultMutableTreeNode ){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object obj = node.getUserObject();

        // BuyTrade node
        if( obj instanceof BuyTrade ){
            setOpaque(false);
            setForeground(Color.blue);
            setText(obj.toString());
        }
        // SellTrade node
        else if( obj instanceof SellTrade ){
            setOpaque(true);
            setBackground(Color.lightGray);
            setForeground(Color.red);
            setText(obj.toString());
        }
        // StockMgr node
        else if( obj instanceof SecurityTradeList ){
/*
            StockMgr mgr = (StockMgr)obj;
            long numUnsoldShares = mgr.getNumSharesHeld();
            String str = new String(" ");

            if( numUnsoldShares == 0 ){
                setOpaque(true);
                setForeground(Color.white);
                if( mgr.ttlGain > 0.0 )
                    setBackground(Color.black);
                else
                    setBackground(Color.red);
                str += mgr.getTicker() + " $" + mgr.ttlGain;
            }
            else{
                setOpaque(false);
                setForeground(Color.black);
                str += mgr.getTicker();
            }
            setText(str);
*/
        }
        else{
            setOpaque(false);
            setForeground(Color.black);
            setText(value.toString());
        }
    }
    else
        setText("ERROR in PortfolioTreeCellRenderer");

    return this;
  }
}
