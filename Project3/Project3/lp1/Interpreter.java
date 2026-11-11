/**
 * Interpreter.java
 *
 * A skeleton of the interpreter for the "straight-line" language of
 *    Appel chapter 1.
 *
 * @author Dennis Brylow <brylow@cs.mu.edu>
 * @version 1.1
 */

public class Interpreter{
    private boolean debug = false;

    java.util.Hashtable table = new java.util.Hashtable();

    public static void main(String[] args)
    {
	Interpreter interpreter = new Interpreter();
	System.out.println("Evaluating program.");
	Absyn.Goal g = null;
	try
	  {
	    java.io.FileReader reader = new java.io.FileReader(args[0]);
	    g = new LP1Parser(reader).Goal();
	  }
	catch (java.io.IOException e)
	  {
	    System.err.println(e.toString());
	  }
	catch (ParseException p)
	  {
	    System.out.println(p.toString());
	    System.exit(-1);
	  }
	System.out.println(interpreter.visit(g));
    }

    ////////////////////////////////
    // Visit methods.             //
    // This is where all the REAL //
    //  interpreter action takes  //
    //  place.                    //
    ////////////////////////////////

    int visit(Absyn.Goal goal) 
    { 
      return visit(goal.e);
    }

    int visit(Absyn.Exp exp)
    {
      if      (exp instanceof Absyn.EquExp) return visit((Absyn.EquExp)exp);
      else if (exp instanceof Absyn.NeqExp) return visit((Absyn.NeqExp)exp);
      else if (exp instanceof Absyn.GeqExp) return visit((Absyn.GeqExp)exp);
      else if (exp instanceof Absyn.LeqExp) return visit((Absyn.LeqExp)exp);
      else if (exp instanceof Absyn.NumExp) return visit((Absyn.NumExp)exp);
      else if (exp instanceof Absyn.IdExp)  return visit((Absyn.IdExp)exp);
      else if (exp instanceof Absyn.BoolLiteral)  return visit((Absyn.BoolLiteral)exp);
      else return 0;
    }

    int visit(Absyn.IdExp exp)
    {
      System.err.println("ERROR Interpreter::"
			 + " Unbound identifier, \""
			 + exp.id + "\"");
      return 0;
    }

    int visit(Absyn.NumExp exp)
    {
	return exp.num;
    }

    int visit(Absyn.BoolLiteral exp)
    {
	return exp.b ? 1 : 0;
    }

    int visit(Absyn.EquExp exp)
    {   return visit(exp.left) == visit(exp.right) ? 1 : 0;   }

    int visit(Absyn.NeqExp exp)
    {   return visit(exp.left) != visit(exp.right) ? 1 : 0;  }

    int visit(Absyn.GeqExp exp)
    {   return visit(exp.left) >= visit(exp.right) ? 1 : 0;   }

    int visit(Absyn.LeqExp exp)
    {   return visit(exp.left) <= visit(exp.right) ? 1 : 0;   }

} // Interpreter
