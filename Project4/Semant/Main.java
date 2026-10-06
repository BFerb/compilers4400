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
                System.out.println(cd.checktype);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
