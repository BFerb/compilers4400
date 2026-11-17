/**
 * COSC 4400 - Project #7
 * Builds, checks, reads, or prints MiniJava type information.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Semant;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            ReadAbsyn reader = new ReadAbsyn(System.in);
            Absyn.Program program = reader.Program();

            ClassBuilder builder = new ClassBuilder();
            builder.build(program);

            if (builder.hasErrors())
                return;

            TypeChecker checker = new TypeChecker(builder.getClassEnv());
            checker.check(program);

            if (!checker.hasErrors())
            {
                PrintVisitor printer =
                    new PrintVisitor(new java.io.PrintWriter(System.out));
                program.accept(printer);
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
