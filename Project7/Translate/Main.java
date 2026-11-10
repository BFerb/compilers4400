/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */

package Translate;
import java.io.PrintWriter;
import java.util.Iterator;

//import Semant.*;
import Absyn.Program;
import Parse.*;

import java.util.HashMap; 
import java.util.List;
import java.util.LinkedList;

//import Translate.Translate;

public class Main 
{
    public static void main(String args[])
    {
	java.io.Reader reader = new java.io.BufferedReader(
            new java.io.InputStreamReader(System.in));

        Absyn.Program parse = null;
	try
	{
	    parse = new Parse.MiniJavaParser(reader).Goal();
	}
	catch (ParseException p)
	{
	    System.err.println(p.toString());
	    System.exit(-1);
	}
		
	Semant.ClassBuilder builder = new Semant.ClassBuilder();
        builder.build(parse);
        if (builder.hasErrors()) {
            System.exit(1);
            return;
        }

        Semant.TypeChecker checker =
            new Semant.TypeChecker(builder.getClassEnv());
        checker.check(parse);
        if (checker.hasErrors()) {
            System.exit(1);
            return;
        }

        Translate translate = new Translate(new Riscv.RiscvFrame());
	parse.accept(translate);
		
	PrintWriter writer = new PrintWriter(System.out);
		
	for (Iterator frags = translate.results().iterator(); 
	     frags.hasNext(); ) 
	{
	    Frag f = (Frag)frags.next();
	    if (f instanceof DataFrag) 
	    {
		writer.println("DataFrag(");
		writer.println(f);
		writer.println(")");
	    } 
	    else 
	    {
		writer.println("ProcFrag(");
		ProcFrag p = (ProcFrag)f;
		p.frame.printFrame(writer);
		if (p.body != null) 
		{
		    new Tree.Print(writer, p.body);
		    writer.println();
		}
		writer.println(")");
	    }
	}  
	writer.flush();
    }
}	

