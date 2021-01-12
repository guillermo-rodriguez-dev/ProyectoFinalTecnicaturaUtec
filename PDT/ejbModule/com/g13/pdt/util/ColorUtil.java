package com.g13.pdt.util;

import javafx.scene.paint.Color;

public class ColorUtil {
	
	public static String colorToHex(Color color) {
	    String hex1;
	    String hex2;

	    hex1 = Integer.toHexString(color.hashCode()).toUpperCase();

	    switch (hex1.length()) {
	    case 2:
	        hex2 = "000000";
	        break;
	    case 3:
	        hex2 = String.format("00000%s", hex1.substring(0,1));
	        break;
	    case 4:
	        hex2 = String.format("0000%s", hex1.substring(0,2));
	        break;
	    case 5:
	        hex2 = String.format("000%s", hex1.substring(0,3));
	        break;
	    case 6:
	        hex2 = String.format("00%s", hex1.substring(0,4));
	        break;
	    case 7:
	        hex2 = String.format("0%s", hex1.substring(0,5));
	        break;
	    default:
	        hex2 = hex1.substring(0, 6);
	    }
	    return "#" + hex2;
	}

}
