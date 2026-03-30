import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final List<String> PALAVRAS_CHAVE = Arrays.asList(
        "print", "input", "def", "if", "else", "elif", "for", "while", "return"
    );
    private static final List<String> VARIAVEIS_VALIDAS = Arrays.asList(
        "x", "y", "z", "resultado"
    );

    public static void main(String[] args) {
        ErrorList listaErros = new ErrorList();

        try {
            FileReader reader = new FileReader("bin/program.py");
            LexicalAnalyzer lexer = new LexicalAnalyzer(reader, listaErros);
            PyToken token;

            System.out.println("Iniciando a leitura do arquivo...\n");

            while ((token = lexer.yylex()) != null) {
                System.out.println("Token: [" + token.name + "] '" + token.value + "'");
                if ("ID".equals(token.name)) {
                    if (!PALAVRAS_CHAVE.contains(token.value) && !VARIAVEIS_VALIDAS.contains(token.value)) {
                        listaErros.addError("Identificador desconhecido ou inválido: " + token.value, token.line, token.column);
                    }
                }
            }

            System.out.println("\n*** Resultado da Análise ***");
            listaErros.printErrors();

        } catch (IOException e) {
            System.err.println("Falha ao processar o arquivo: " + e.getMessage());
        }
    }
}