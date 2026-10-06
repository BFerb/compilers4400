/**
 * COSC 4400 - Project #4
 * Semantic analysis of MiniJava programs
 * @authors [Nick Grons, Ben Ferber]
 * Instructor [Brylow]
 * TA-BOT:MAILTO [nicholas.grons@marquette.edu benjamin.ferber@marquette.edu]
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

            for (Absyn.ClassDecl cd : program.classes)
            {
                Types.CLASS c = cd.checktype;

                System.out.println("CLASS(" + c.name);
                System.out.println(" parent = " + c.parent);
                System.out.println(" methods = " + c.methods);
                for (Types.FIELD field : c.methods)
                {
                    Types.FUNCTION f = (Types.FUNCTION)field.type;
                    System.out.println(" self = " + f.self);
                }
                System.out.println(" fields = " + c.fields);
                System.out.println(" object = " + c.instance);
                System.out.println(")");
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
