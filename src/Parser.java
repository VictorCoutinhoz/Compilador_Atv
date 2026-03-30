import java.io.IOException;
import java.util.Scanner;

public class Parser {
    private LexicalAnalyzer lexer;
    private PyToken lookahead;
    private ErrorList errorList;
    public SymbolTable symbolTable = new SymbolTable();
    private Scanner teclado = new Scanner(System.in);

    public Parser(LexicalAnalyzer lexer, ErrorList errorList) throws IOException {
        this.lexer = lexer;
        this.errorList = errorList;
        this.lookahead = lexer.yylex();
    }

    private void consume(String expected) throws IOException {
        if (lookahead != null && lookahead.name.equals(expected)) {
            lookahead = lexer.yylex();
        } else {
            String encontrado = (lookahead != null) ? lookahead.value : "EOF";
            errorList.addError("Esperado: " + expected + ", mas encontrou: " + encontrado, 0, 0);
        }
    }

    public void executar() throws IOException {
        System.out.println("\n==================================================");
        System.out.println("        INICIANDO INTERPRETAÇÃO DO SCRIPT         ");
        System.out.println("==================================================\n");

        while (lookahead != null) {
            if (lookahead.name.equals("print")) {
                interpretPrint();
            } else if (lookahead.name.equals("ID")) {
                interpretAtribuicao();
            } else {
                expr();
            }
        }

        System.out.println("==================================================");
        System.out.println("        EXECUÇÃO FINALIZADA COM SUCESSO           ");
        System.out.println("==================================================\n");
    }

    private void interpretAtribuicao() throws IOException {
        String nomeVar = lookahead.value;
        consume("ID");
        consume("IGUAL");

        double resultado = 0;

        if (lookahead != null && lookahead.name.equals("input")) {
            consume("input");
            consume("abre_par");
            if (lookahead.name.equals("string")) {
                System.out.print("[?] " + lookahead.value.replace("\"", "") + " ");
                consume("string");
            }
            consume("fecha_par");

            try {
                if (teclado.hasNextLine()) {
                    resultado = Double.parseDouble(teclado.nextLine());
                }
            } catch (NumberFormatException e) {
                resultado = 0;
            }
        } else {
            resultado = expr();
        }

        symbolTable.assign(nomeVar, "number", resultado);
        System.out.println("   [MEMÓRIA] " + nomeVar + " = " + resultado);
        System.out.println("--------------------------------------------------");
    }

    private void interpretPrint() throws IOException {
        consume("print");
        consume("abre_par");
        StringBuilder output = new StringBuilder();

        while (lookahead != null && !lookahead.name.equals("fecha_par")) {
            if (lookahead.name.equals("string")) {
                output.append(lookahead.value.replace("\"", ""));
                consume("string");
            } else if (lookahead.name.equals("ID")) {
                String varBusca = lookahead.value;
                if (symbolTable.contains(varBusca)) {
                    output.append(" ").append(symbolTable.lookup(varBusca).value);
                }
                consume("ID");
            } else if (lookahead.name.equals("virgula")) {
                consume("virgula");
            } else {
                output.append(expr());
            }
        }

        // Saída do código Python formatada de forma diferente dos logs do interpretador
        System.out.println("\n  >> PYTHON: " + output.toString() + "\n");
        consume("fecha_par");
    }

    public double expr() throws IOException {
        double val = termo();
        while (lookahead != null && (lookahead.name.equals("SOMA") || lookahead.name.equals("SUBTRACAO"))) {
            String op = lookahead.value;
            String opName = lookahead.name;
            consume(opName);

            double proxVal = termo();
            double res = opName.equals("SOMA") ? (val + proxVal) : (val - proxVal);

            // Formatação conforme sua solicitação
            System.out.println(
                    "   [CONTA] 1º termo: " + val + " | " + op + " | 2º termo: " + proxVal + " | Resultado: " + res);
            val = res;
        }
        return val;
    }

    public double termo() throws IOException {
        double val = fator();
        while (lookahead != null && (lookahead.name.equals("MULTIPLICACAO") || lookahead.name.equals("DIVISAO"))) {
            String op = lookahead.value;
            String opName = lookahead.name;
            consume(opName);

            double proxVal = fator();
            double res = opName.equals("MULTIPLICACAO") ? (val * proxVal) : (val / proxVal);

            // Formatação conforme sua solicitação
            System.out.println(
                    "   [CONTA] 1º termo: " + val + " | " + op + " | 2º termo: " + proxVal + " | Resultado: " + res);
            val = res;
        }
        return val;
    }

    public double fator() throws IOException {
        if (lookahead == null)
            return 0;
        switch (lookahead.name) {
            case "inteiro":
                double vI = Double.parseDouble(lookahead.value);
                consume("inteiro");
                return vI;
            case "real":
                double vR = Double.parseDouble(lookahead.value);
                consume("real");
                return vR;
            case "ID":
                String nome = lookahead.value;
                consume("ID");
                return symbolTable.lookup(nome).value;
            case "abre_par":
                consume("abre_par");
                double r = expr();
                consume("fecha_par");
                return r;
            default:
                return 0;
        }
    }
}