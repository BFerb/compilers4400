package Symbol;

/** A unique symbol for each distinct name. */
public final class Symbol {
    private final String name;

    private static final java.util.Map<String, Symbol> symbols =
        new java.util.HashMap<String, Symbol>();

    private Symbol(String name) {
        this.name = name;
    }

    public static Symbol symbol(String name) {
        Symbol result = symbols.get(name);
        if (result == null) {
            result = new Symbol(name);
            symbols.put(name, result);
        }
        return result;
    }

    public String toString() {
        return name;
    }
}
