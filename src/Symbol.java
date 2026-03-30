public class Symbol {
    public String name;
    public String type;
    public double value; // Novo campo para o valor real

    public Symbol(String name, String type, double value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Symbol[name=" + name + ", type=" + type + ", value=" + value + "]";
    }
}