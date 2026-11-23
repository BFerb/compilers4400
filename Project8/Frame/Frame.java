/**
 * COSC 4400 - Project #7
 * Defines stack frame and variable access interfaces.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
package Frame;
import java.util.List;
import java.util.LinkedList;

/**
 * Holds information about local variables and formal parameters 
 * that have been allocated in this frame.
 */

public abstract class Frame
{
  /* To store the name of the function as a label*/
  public Temp.Label name;
  /* Formals - This will contain a list of Access*/	
  public List<Access> formals;
  /* Incoming argument locations. */
  public List<Access> actuals;	

  public abstract Frame newFrame(Symbol.Symbol name);
  public abstract Access allocFormal();
  public abstract Access allocLocal();
  public abstract Temp.Temp FP();
  public abstract int wordSize();
  /* Function Symbol & Args list */
  public abstract Temp.Temp RV();

  public abstract Tree.Exp externalCall(
      Symbol.Symbol name, List<Tree.Exp> args);

	
  public abstract String string(Temp.Label label, String value);
  public abstract String record(Temp.Label label, int words);
  public abstract String record(Temp.Label label, List<Temp.Label> values);
  public abstract Temp.Label badPtr();
  public abstract Temp.Label badSub();
  public abstract String tempMap(Temp.Temp temp);
  public abstract void printFrame(java.io.PrintWriter pw);
  public void printAssemblyFrame(java.io.PrintWriter pw) { printFrame(pw); }

  public abstract List<Assem.Instr> codeGen(List<Tree.Stm> body);
  public abstract void procEntryExit1(List<Tree.Stm> body);
}
