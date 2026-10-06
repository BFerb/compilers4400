package Symbol;

import java.util.HashMap;
import java.util.LinkedList;

public class Table<T>
{
    private LinkedList<HashMap<String, T>> scopes;

    public Table()
    {
        scopes = new LinkedList<HashMap<String, T>>();
        beginScope();
    }

    public T put(String key, T value)
    {
        return scopes.getFirst().put(key, value);
    }

    public T get(String key)
    {
        for (HashMap<String, T> scope : scopes)
        {
            T value = scope.get(key);
            if (value != null)
                return value;
        }
        return null;
    }

    public void beginScope()
    {
        scopes.addFirst(new HashMap<String, T>());
    }

    public void endScope()
    {
        if (!scopes.isEmpty())
            scopes.removeFirst();
    }
}
