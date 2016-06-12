package cg.gui;

import java.awt.Color;

/**
 * This class represents the color scheme used by a TwoColorTableRenderer. The
 * colors represented by the 0 index of the Color[] components represent one
 * color scheme, and the colors represented by the 1 index represent a second
 * color scheme. The abstract method getColorIndex of the TwoColorTableRenderer
 * class decides which of the two color schemes to use to render a specified row.
 * 
 */
public class TableTwoColorScheme {

	Color[] bg_Normal = { Color.cyan, Color.magenta };
	Color[] bg_IsSelected = { Color.lightGray, Color.lightGray };
	Color[] bg_HasFocus = { Color.white, Color.white };
	Color[] fg_Normal = { Color.black, Color.black };
	Color[] fg_IsSelected = { Color.black, Color.black };
	Color[] fg_HasFocus = { Color.black, Color.black };

	public TableTwoColorScheme() {
	}

}
