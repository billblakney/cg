package CapGains.gui;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import CapGains.*;

public class AccountTreeModel extends DefaultTreeModel {

    public final static int ALL = 0;
    public final static int CURRENTLY_HELD = 1;
    public final static int FORMERLY_HELD = 2;

    DefaultMutableTreeNode root;

    // constructor
    public AccountTreeModel(Account p){
        super(new DefaultMutableTreeNode("All Stocks"));
        init(p,ALL);
    }

    // constructor
    public AccountTreeModel(Account p,int filter){
        super(new DefaultMutableTreeNode("All Stocks"));
        init(p,filter);
    }

    // build the tree model
    void init(Account p,int filter){

        root = (DefaultMutableTreeNode)getRoot();

        // set text of root node to reflect the filter
        if( filter == ALL )
            root.setUserObject("All Stocks");
        else if( filter == CURRENTLY_HELD )
            root.setUserObject("Currently Held Stocks");
        else if( filter == FORMERLY_HELD )
            root.setUserObject("Formerly Held Stocks");
        else
            root.setUserObject("SHOULD THROW AN EXCEPTION");

    }
}
