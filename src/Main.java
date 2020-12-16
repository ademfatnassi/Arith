import jdk.nashorn.internal.parser.Lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.print(">");
//            String input = scanner.nextLine();
//        }
        if (args.length > 0) {
            try {

                File f = new File(args[0]);
                Scanner s = new Scanner(f);
                String source = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Tokenizer tokenizer = new Tokenizer(source);
                tokenizer.AfficheTokens();
            } catch(FileNotFoundException e) {
//                error(-1, -1, "Exception: " + e.getMessage());
                System.out.println("-1 -1 Exception: "+e.getMessage());
            }
        } else {
//            error(-1, -1, "No args");
            System.out.println("-1 -1 No args: ");

        }
    }
}

class Tokenizer{
    private String s;
    private char chr;
    private int position;
    private int line;
    private int pos;
    Map<String, TokenType> keywords = new HashMap<>();

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    Tokenizer(String source){
        this.s = source;
        this.chr = this.s.charAt(0);
        this.position = 0;
        this.line=1;
        this.pos=0;
        this.keywords.put("set", TokenType.Mc_set);
        this.keywords.put("print", TokenType.Mc_print);
        this.keywords.put("let", TokenType.Mc_let);
        this.keywords.put("in", TokenType.Mc_in);
    }

    char  getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    Token identifier_or_integer(int line, int pos) {
        boolean is_number = true;
        String text = "";

        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_') {
            text += this.chr;
            if (!Character.isDigit(this.chr)) {
                is_number = false;
            }
            getNextChar();
        }

        if (text.equals("")) {
            error(line, pos, String.format("identifer_or_integer unrecopgnized character: (%d) %c", (int)this.chr, this.chr));
        }

        if (Character.isDigit(text.charAt(0))) {
            if (!is_number) {
                error(line, pos, String.format("invaslid number: %s", text));
            }
            return new Token(TokenType.entier, text, line, pos);
        }

        if (this.keywords.containsKey(text)) {
            return new Token(this.keywords.get(text), "", line, pos);
        }
        return new Token(TokenType.Identifiant, text, line, pos);
    }

    Token getToken(){
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;

        switch (this.chr){
            case '(': getNextChar(); return new Token(TokenType.ParenGouche, "", line, pos);
            case ')': getNextChar(); return new Token(TokenType.ParenDroit, "", line, pos);
            case '+': getNextChar(); return new Token(TokenType.Op_addition, "", line, pos);
            case '-': getNextChar(); return new Token(TokenType.Op_soustraction, "", line, pos);
            case '*': getNextChar(); return new Token(TokenType.Op_multiplication, "", line, pos);
            case '/': getNextChar(); return new Token(TokenType.Op_division, "", line, pos);
            case '=': getNextChar(); return new Token(TokenType.Op_affectation, "", line, pos);
            case '\u0000': return new Token(TokenType.End_of_input, "", this.line, this.pos);
            default: return identifier_or_integer(line, pos);
        }
    }

    void AfficheTokens(){
        Token token;
        while ((token = getToken()).tokenType != TokenType.End_of_input) {
            System.out.println(token);
        }
        System.out.println(token);
    }
}

//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.print(">");
//            String input = scanner.nextLine();
//            if (isNullOrWhitespace(input)) {
//                return;
//            }
//            Lexer lexer = new Lexer(input);
////            System.out.println(lexer);
//
//            while (true) {
//                SyntaxToken token = lexer.nextToken();
////                System.out.println(token);
//                if (token.kind == SyntaxKind.endOfFileToken) {
//                    break;
//                }
//                System.out.printf("%s: %s%n", token.kind, token.text);
//                if (token.value!= null){
//                    System.out.println(token.value);
//                }
//            }
////            if (input.equals("1 + 2 + 3")) {
////                System.out.println("7");
////            } else {
////                System.out.println("ERROR: Invalid expression!");
////            }
//        }
//    }
//
//    public static boolean isNullOrEmpty(String s) {
//        return s == null || s.length() == 0;
//    }
//
//    public static boolean isNullOrWhitespace(String s) {
//        return s == null || isWhitespace(s);
//
//    }
//
//    private static boolean isWhitespace(String s) {
//        int length = s.length();
//        if (length > 0) {
//            for (int i = 0; i < length; i++) {
//                if (!Character.isWhitespace(s.charAt(i))) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//}
//
//enum SyntaxKind {whiteSpaceToken, plusToken, minusToken, starToken, slashToken, openParenthesisToken, closeParenthesisToken, endOfFileToken, BadToken, numberToken}
//
//class SyntaxToken {
//    public SyntaxKind kind;
//    public int position;
//    public String text;
//    public Object value;
//
//    public SyntaxToken(SyntaxKind kind, int position, String text, Object value) {
//        this.kind = kind;
//        this.position = position;
//        this.text = text;
//        this.value = value;
//    }
//
//    @Override
//    public String toString() {
//        return "SyntaxToken{" +
//                "kind=" + kind +
//                ", position=" + position +
//                ", text='" + text + '\'' +
//                ", value=" + value +
//                '}';
//    }
//}
//
//class Lexer {
//    private String text;
//    private int position;
//
//    @Override
//    public String toString() {
//        return "Lexer{" +
//                "text='" + text + '\'' +
//                ", position=" + position +
//                ", current=" + current +
//                '}';
//    }
//
//    public Lexer(String text) {
//        this.text = text;
//        if (position >= text.length())
//            this.current= '\0';
//        this.current= text.charAt(position);
//    }
//
//    private char current;
//
//    SyntaxToken nextToken() {
//        // <numbers>
//        // + - * ( )
//        // whitespaces
//
//        if (position >= text.length()) return new SyntaxToken(SyntaxKind.endOfFileToken, position, "\0", null);
////        System.out.println("isdigit"+Character.isDigit(getCurrent())+getCurrent());
//        if (Character.isDigit(getCurrent())) {
//            int start = position;
////            System.out.println("start"+position);
//
//            while (Character.isDigit(getCurrent()))
//                next();
////            System.out.println("end"+position);
//
//            int length = position - start;
////            System.out.println("digit length!"+length);
//            String text = this.text.substring(start, length);
////            System.out.println("digit text"+text);
//            int value = Integer.parseInt(text);
//            return new SyntaxToken(SyntaxKind.numberToken, start, text, value);
//        }
//        if (Character.isWhitespace(getCurrent())) {
//            int start = position;
//            while (Character.isWhitespace(getCurrent()))
//                next();
//            int length = position - start;
//            String text = this.text.substring(start, length);
//            return new SyntaxToken(SyntaxKind.whiteSpaceToken, start, text, null);
//        }
//
//        if (getCurrent() == '+') {
//            return new SyntaxToken(SyntaxKind.plusToken, position++, "+", null);
//        }
//        if (getCurrent() == '-') {
//            return new SyntaxToken(SyntaxKind.minusToken, position++, "-", null);
//        }
//        if (getCurrent() == '*') {
//            return new SyntaxToken(SyntaxKind.starToken, position++, "*", null);
//        }
//        if (getCurrent() == '/') {
//            return new SyntaxToken(SyntaxKind.slashToken, position++, "/", null);
//        }
//        if (getCurrent() == '(') {
//            return new SyntaxToken(SyntaxKind.openParenthesisToken, position++, "(", null);
//        }
//        if (getCurrent() == ')') {
//            return new SyntaxToken(SyntaxKind.closeParenthesisToken, position++, ")", null);
//        }
//        System.out.println(position);
//        return new SyntaxToken(SyntaxKind.BadToken, position++, text.substring(position-1,1), null);
//    }
//
//    private void next() {
//        position++;
//    }
//
//    private char getCurrent() {
//        if (position >= text.length())
//            return '\0';
//        return text.charAt(position);
//    }
//}