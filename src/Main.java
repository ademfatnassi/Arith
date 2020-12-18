import jdk.nashorn.internal.parser.Lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print("Arith>");
            String input = s.nextLine();
//            String source = " ";
//            while (s.hasNext()) {
//                source += s.nextLine() + "\n";
//            }
            Tokenizer tokenizer = new Tokenizer(input);
            List list = tokenizer.AfficheTokens();
            Parser parser = new Parser(list);
            System.out.println(parser.parse());
        }
    }

//    public static void main(String[] args) {
//
//        if (args.length > 0) {
//            try {
//
//                File f = new File(args[0]);
//                Scanner s = new Scanner(f);
//                String source = " ";
//                while (s.hasNext()) {
//                    source += s.nextLine() + "\n";
//                }
//                Tokenizer tokenizer = new Tokenizer(source);
//                List list = tokenizer.AfficheTokens();
//                Parser parser = new Parser(list);
//                System.out.println(parser.parse());
////                parser.parse();
//            } catch (FileNotFoundException e) {
////                Tokenizer.error(-1, -1, "Exception: " + e.getMessage());
//                System.out.println("-1 -1 Exception: " + e.getMessage());
//            }
//        } else {
//              new Error(-1, -1, "No args").triggerError();
////            System.out.println("-1 -1 No args: ");
//
//        }
//    }
}

/*

*/

class Tokenizer {
    private String s;
    private char chr;
    private int position;
    private int line;
    private int pos;
    Map<String, TokenType> keywords = new HashMap<>();
    List<Token> tokenList = new ArrayList<>();

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    Tokenizer(String source) {
        this.s = source;
        this.chr = this.s.charAt(0);
        this.position = 0;
        this.line = 1;
        this.pos = 0;
        this.keywords.put("set", TokenType.Mc_set);
        this.keywords.put("print", TokenType.Mc_print);
        this.keywords.put("let", TokenType.Mc_let);
        this.keywords.put("in", TokenType.Mc_in);
    }

    char getNextChar() {
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


        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr)) {
            text += this.chr;
            if (!Character.isDigit(this.chr)) {
                is_number = false;
            }
            getNextChar();
        }

        if (text.equals("")) {
            error(line, pos, String.format("Unrecopgnized character: (%d) %c", (int) this.chr, this.chr));
        }

        if (Character.isDigit(text.charAt(0))) {
            if (!is_number) {
                error(line, pos, String.format("Invalid number: %s", text));
            }
            return new Token(TokenType.entier, text, line, pos);
        }

        if (this.keywords.containsKey(text)) {
            return new Token(this.keywords.get(text), text, line, pos);
        }
        return new Token(TokenType.Identifiant, text, line, pos);
    }

    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;

        switch (this.chr) {
            case '(':
                getNextChar();
                return new Token(TokenType.ParenGouche, "(", line, pos);
            case ')':
                getNextChar();
                return new Token(TokenType.ParenDroit, ")", line, pos);
            case '+':
                getNextChar();
                return new Token(TokenType.Op_addition, "+", line, pos);
            case '-':
                getNextChar();
                return new Token(TokenType.Op_soustraction, "-", line, pos);
            case '*':
                getNextChar();
                return new Token(TokenType.Op_multiplication, "*", line, pos);
            case '/':
                getNextChar();
                return new Token(TokenType.Op_division, "/", line, pos);
            case '=':
                getNextChar();
                return new Token(TokenType.Op_affectation, "=", line, pos);
            case '\u0000':
                return new Token(TokenType.End_of_input, "\u0000", this.line, this.pos);
            default:
                return identifier_or_integer(line, pos);
        }
    }

    List AfficheTokens() {
        Token token;
        while ((token = getToken()).tokenType != TokenType.End_of_input) {
//            System.out.println(token);
            this.tokenList.add(token);
        }
//        System.out.println(token);
//        this.tokenList.add(token);
        System.out.println(tokenList);
        return tokenList;
    }
}