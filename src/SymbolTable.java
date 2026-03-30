import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Symbol> table = new HashMap<>();

    // Ajustado para receber 3 parametros: nome, tipo e o valor numerico
    public void assign(String name, String type, double value) {
        table.put(name, new Symbol(name, type, value));
    }

    public Symbol lookup(String name) {
        return table.get(name);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    public void printTable() {
        System.out.println("\n--- TABELA DE VARIÁVEIS FINAL ---");
        for (Map.Entry<String, Symbol> entry : table.entrySet()) {
            System.out.println(" " + entry.getKey() + " = " + entry.getValue().value);
        }
    }
}