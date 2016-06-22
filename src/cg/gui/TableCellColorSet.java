package cg.gui;

import java.awt.Color;

/**
 */
public class TableCellColorSet {

   static final TableCellColorSet kDefaultColorSet1 = new TableCellColorSet(
         Color.cyan,
         Color.lightGray,
         Color.white,
         Color.black,
         Color.black,
         Color.black);

   static final TableCellColorSet kDefaultColorSet2 = new TableCellColorSet(
         Color.magenta,
         Color.lightGray,
         Color.white,
         Color.black,
         Color.black,
         Color.black);

	public Color _bgNormal =    Color.cyan;
	public Color _bgSelected =  Color.lightGray;
	public Color _bgFocus =     Color.white;
	public Color _fgNormal =    Color.black;
	public Color _fgSelected =  Color.black;
	public Color _fgFocus =     Color.black;

	public TableCellColorSet()
	{
	}

	public TableCellColorSet(TableCellColorSet aSet)
	{
	_bgNormal =    aSet._bgNormal;
	_bgSelected =  aSet._bgSelected;
	_bgFocus =     aSet._bgFocus;
	_fgNormal =    aSet._fgNormal;
	_fgSelected =  aSet._fgSelected;
	_fgFocus =     aSet._fgFocus;
	}

	public TableCellColorSet(Color aBgNormal,Color aBgSelected,Color aBgFocus)
	{
	   _bgNormal = aBgNormal;
	   _bgSelected = aBgSelected;
	   _bgFocus = aBgFocus;
	}

	public TableCellColorSet(Color aBgNormal,Color aBgSelected,Color aBgFocus,
	      Color aFgNormal,Color aFgSelected,Color aFgFocus)
	{
	   _bgNormal = aBgNormal;
	   _bgSelected = aBgSelected;
	   _bgFocus = aBgFocus;
	   _fgNormal = aFgNormal;
	   _fgSelected = aFgSelected;
	   _fgFocus = aFgFocus;
	}
}
