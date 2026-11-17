/**
 * COSC 4400 - Project #7
 * Translates checked MiniJava syntax into intermediate code.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
package Translate;

/**
 * Fragment that consists of a string literal.
 */

public class DataFrag extends Frag {
    public String data;
    public DataFrag(String s) {
	data = s;
    }
    public String toString() { return data; }
}
