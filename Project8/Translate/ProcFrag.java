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

import java.util.List;

/**
 * Fragment that consists of the frame descriptor and a body.
 */

public class ProcFrag extends Frag 
{
    public Tree.Stm body;
    public Frame.Frame frame;

    public ProcFrag(Tree.Stm b, Frame.Frame f)
    {
	body = b;
	frame = f;
    }
}
