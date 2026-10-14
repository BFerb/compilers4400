package Semant;

import java.util.HashSet;

public class ClassBuilder
{
    private boolean hadError = false;

    public boolean hasErrors()
    {
        return hadError;
    }

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
        HashSet<String> declared = new HashSet<String>();

        for (Absyn.ClassDecl cd : program.classes)
        {
            if (!declared.add(cd.name))
            {
                hadError = true;
                System.out.println("ERROR duplicate class: " +
                    cd.name + ": line not available");
                return;
            }
        }

        for (Absyn.ClassDecl cd : program.classes)
        {
            if (cd.parent != null &&
                !declared.contains(cd.parent) &&
                !cd.parent.equals("Thread") &&
                !cd.parent.equals("String"))
            {
                hadError = true;
                System.out.println("ERROR cannot resolve parent class: " +
                    cd.parent + ": line not available");
                return;
            }
        }

        for (Absyn.ClassDecl cd : program.classes)
            referToClass(cd.name);

        for (Absyn.ClassDecl cd : program.classes)
        {
            java.util.HashSet<String> visited =
                new java.util.HashSet<String>();

            String current = cd.name;

            while (current != null)
            {
                if (!visited.add(current))
                {
                    hadError = true;
                    break;
                }

                String next = null;

                for (Absyn.ClassDecl candidate : program.classes)
                {
                    if (candidate.name.equals(current))
                    {
                        next = candidate.parent;
                        break;
                    }
                }

                current = next;
            }
        }

        if (hadError)
        {
            for (Absyn.ClassDecl cd : program.classes)
            {
                java.util.HashSet<String> visited =
                    new java.util.HashSet<String>();

                String current = cd.name;

                while (current != null && visited.add(current))
                {
                    String next = null;

                    for (Absyn.ClassDecl candidate : program.classes)
                    {
                        if (candidate.name.equals(current))
                        {
                            next = candidate.parent;
                            break;
                        }
                    }

                    current = next;
                }

                if (current != null && current.equals(cd.name))
                    System.out.println(
                        "ERROR cyclic inheritance involving " +
                        cd.name + ": line not available");
            }

            return;
        }

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

        c.instance.methods = c.methods;
        c.instance.fields = c.fields;
    }


    private boolean compatibleOverride(Types.FUNCTION child,
                                       Types.FUNCTION parent)
    {
        if (!child.result.coerceTo(parent.result))
            return false;

        if (child.formals.size() != parent.formals.size())
            return false;

        java.util.Iterator<Types.FIELD> a =
            child.formals.iterator();

        java.util.Iterator<Types.FIELD> b =
            parent.formals.iterator();

        while (a.hasNext() && b.hasNext())
        {
            Types.Type t1 = a.next().type;
            Types.Type t2 = b.next().type;

            if (!t1.coerceTo(t2) || !t2.coerceTo(t1))
                return false;
        }

        return true;
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
        {
            if (fields.get(field.name) != null)
                fields.replace(field.type, field.name);
            else
                fields.put(field.type, field.name);
        }

        for (Types.FIELD field : c.methods)
        {
            Types.FIELD inherited = methods.get(field.name);

            if (inherited != null)
            {
                Types.FUNCTION child =
                    (Types.FUNCTION)field.type;

                Types.FUNCTION parent =
                    (Types.FUNCTION)inherited.type;

                if (!compatibleOverride(child, parent))
                {
                    System.err.println(
                        "ERROR incompatible method override: " +
                        field.name + " in class " + c.name +
                        ": line not available");
                    hadError = true;
                    return;
                }

                methods.replace(field.type, field.name);
            }
            else
            {
                methods.put(field.type, field.name);
            }
        }

        c.instance.methods = methods;
        c.instance.fields = fields;

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
