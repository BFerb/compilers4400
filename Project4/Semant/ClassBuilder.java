package Semant;

public class ClassBuilder
{
    private Symbol.Table<Types.CLASS> classEnv =
        new Symbol.Table<Types.CLASS>();

    public ClassBuilder()
    {
        referToClass("String");
        referToClass("Thread");
    }

    private Types.CLASS referToClass(String name)
    {
        Types.CLASS c = classEnv.get(name);

        if (c == null)
        {
            c = new Types.CLASS(name);
            classEnv.put(name, c);
        }

        return c;
    }

    private Types.Type convertType(Absyn.Type type)
    {
        if (type == null)
            return new Types.VOID();

        if (type instanceof Absyn.IntegerType)
            return new Types.INT();

        if (type instanceof Absyn.BooleanType)
            return new Types.BOOLEAN();

        if (type instanceof Absyn.ArrayType)
        {
            Absyn.ArrayType a = (Absyn.ArrayType)type;
            return new Types.ARRAY(convertType(a.base));
        }

        if (type instanceof Absyn.IdentifierType)
        {
            Absyn.IdentifierType i = (Absyn.IdentifierType)type;
            return referToClass(i.id).instance;
        }

        return null;
    }

    public void build(Absyn.Program program)
    {
        for (Absyn.ClassDecl cd : program.classes)
            referToClass(cd.name);

        for (Absyn.ClassDecl cd : program.classes)
            buildClass(cd);

        for (Absyn.ClassDecl cd : program.classes)
            buildInstance(cd.checktype);
    }

    private void buildClass(Absyn.ClassDecl cd)
    {
        Types.CLASS c = referToClass(cd.name);
        cd.checktype = c;

        if (cd.parent != null)
            c.parent = referToClass(cd.parent);

        for (Absyn.VarDecl field : cd.fields)
        {
            Types.Type type = convertType(field.type);
            field.checktype = type;
            c.fields.put(type, field.name);
        }

        for (Absyn.MethodDecl md : cd.methods)
        {
            Types.RECORD formals = new Types.RECORD();

            for (Absyn.Formal f : md.params)
            {
                Types.Type type = convertType(f.type);
                formals.put(type, f.name);
                f.checktype = type;
            }

            Types.Type result = convertType(md.returnType);

            Types.FUNCTION function =
                new Types.FUNCTION(md.name, c.instance, formals, result);

            md.checktype = function;
            c.methods.put(function, md.name);

            for (Absyn.VarDecl local : md.locals)
                local.checktype = convertType(local.type);
        }

        c.instance = new Types.OBJECT(c, c.methods, c.fields);
    }

    private void buildInstance(Types.CLASS c) 
    { 
        Types.RECORD fields = new Types.RECORD();
        Types.RECORD methods = new Types.RECORD();

        if (c.parent != null) 
        { 
            buildInstance(c.parent);

            for (Types.FIELD field : c.parent.instance.fields)
                fields.put(field.type, field.name);

            for (Types.FIELD field : c.parent.instance.methods)
                methods.put(field.type, field.name);
        }

        for (Types.FIELD field : c.fields)
            fields.put(field.type, field.name);

        for (Types.FIELD field : c.methods)
            methods.put(field.type, field.name);

        c.instance = new Types.OBJECT(c, methods, fields);

        for (Types.FIELD field : c.methods)
        {
            Types.FUNCTION function = (Types.FUNCTION)field.type;
            function.self = c.instance;
        }
    }

    public Symbol.Table<Types.CLASS> getClassEnv()
    {
        return classEnv;
    }
}
