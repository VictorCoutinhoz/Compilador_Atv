%%

%{
    private ErrorList listaErros;

    public LexicalAnalyzer(java.io.FileReader in, ErrorList listaErros) {
        this(in);
        this.listaErros = listaErros;
    }

    public ErrorList getListaErros() {
        return listaErros;
    }

    private PyToken createToken(String name, String value) {
        return new PyToken(name, value, yyline, yycolumn);
    }
%}

%public
%class LexicalAnalyzer
%type PyToken
%line
%column

inteiro = 0 | [1-9][0-9]*
real = {inteiro} \. {inteiro}

def = "def"
print = "print"
input = "input"
if = "if"
elif = "elif"
else = "else"
while = "while"
for = "for"
in = "in"
return = "return"

SOMA = "+"
SUBTRACAO = "-"
MULTIPLICACAO = "*"
DIVISAO = "/"
MODULO = "%"
IGUAL = "="

diferente = "!="
menor = "<"
maior = ">"
menor_igual = "<="
maior_igual = ">="
and = "and"
or = "or"
not = "not"

abrepar = "("
fechapar = ")"
doispontos = ":"
virgula = ","
string = \"([^\"\\]|\\.)*\" | \'([^\'\\]|\\.)*\'
ID = [a-zA-Z_][a-zA-Z0-9_]*

comentario = "#" [^\r\n]*
bracos = [ \t\r\n]+

%%

{def} { return createToken("def", yytext()); }
{print} { return createToken("print", yytext()); }
{input} { return createToken("input", yytext()); }
{if} { return createToken("if", yytext()); }
{elif} { return createToken("elif", yytext()); }
{else} { return createToken("else", yytext()); }
{while} { return createToken("while", yytext()); }
{for} { return createToken("for", yytext()); }
{in} { return createToken("in", yytext()); }
{return} { return createToken("return", yytext()); }

{SOMA} { return createToken("SOMA", yytext()); }
{SUBTRACAO} { return createToken("SUBTRACAO", yytext()); }
{MULTIPLICACAO} { return createToken("MULTIPLICACAO", yytext()); }
{DIVISAO} { return createToken("DIVISAO", yytext()); }
{MODULO} { return createToken("MODULO", yytext()); }
{IGUAL} { return createToken("IGUAL", yytext()); }

{diferente} { return createToken("diferente", yytext()); }
{menor} { return createToken("menor", yytext()); }
{maior} { return createToken("maior", yytext()); }
{menor_igual} { return createToken("menor_igual", yytext()); }
{maior_igual} { return createToken("maior_igual", yytext()); }
{and} { return createToken("and", yytext()); }
{or} { return createToken("or", yytext()); }
{not} { return createToken("not", yytext()); }

{abrepar} { return createToken("abre_par", yytext()); }
{fechapar} { return createToken("fecha_par", yytext()); }
{doispontos} { return createToken("dois_pontos", yytext()); }
{virgula} { return createToken("virgula", yytext()); }

{string} { return createToken("string", yytext()); }
{ID} { return createToken("ID", yytext()); }
{inteiro} { return createToken("inteiro", yytext()); }
{real} { return createToken("real", yytext()); }

{comentario} { /* Ignorar comentarios que comecam com hashtag */ }
{bracos} { /* Ignorar espaços em branco e quebras de linha temporariamente para focar na logica basica */ }

. { 
    listaErros.addError("Caractere inválido: " + yytext(), yyline, yycolumn);
    return null;
}