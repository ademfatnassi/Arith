import java.util.List;

public class Parser {
    private List<Token> source;
    private Token token;
    private int position;

    Parser(List<Token> source) {
        this.source = source;
        this.token = null;
        this.position = -1;
        this.getNextToken();
    }

    Token getNextToken() {
        this.position++;
        if (this.position < this.source.size()) {
            this.token = this.source.get(this.position);
        }
//        this.token = this.source.get(this.position++);
        return this.token;
    }

    //
//    Node listInstr(){
//        return null;
//    }
    Node instr() {
        return null;
    }

    Node expr() {
        Node left = this.term();
        while (this.token.tokenType.equals(TokenType.Op_addition) || this.token.tokenType.equals(TokenType.Op_soustraction)) {
            Token op_tok = this.token;
            getNextToken();
            Node right = this.term();
            left = new BinOpNode(left, op_tok, right); //<< make_node
        }
        return left;
    }

    Node term() {

        Node left = this.factor();
        while (this.token.tokenType.equals(TokenType.Op_multiplication) || this.token.tokenType.equals(TokenType.Op_division)) {
            Token op_tok = this.token;
            getNextToken();
            Node right = this.factor();
            left = new BinOpNode(left, op_tok, right); //<< make_node
        }
        return left;
    }

    Node factor() {
//        ParseResult result = new ParseResult();
        Token currentToken = this.token;
//        System.out.println(currentToken.value);
        if (currentToken.tokenType.equals(TokenType.Op_addition) || currentToken.tokenType.equals(TokenType.Op_soustraction)) {
            getNextToken();
            Node factor = this.factor();
            return new UnaryOpNode(currentToken,factor);
        }
        else if (currentToken.tokenType.equals(TokenType.entier) || currentToken.tokenType.equals(TokenType.Identifiant)) {
            getNextToken();
//            result.register(this.getNextToken());
//            return result.success(new NumberNode(currentToken));
            return new NumberNode(currentToken);
        } else if (currentToken.tokenType.equals(TokenType.ParenGouche)) {
            getNextToken();
            Node expr = this.expr();
            if (this.token.tokenType.equals(TokenType.ParenDroit)) {
                getNextToken();
                return expr;
            } else new Error(this.token.line, this.token.pos, "Invalid Syntax Error").triggerError();
        }
        return new Node();
//        return result.failure(new Error(currentToken.line,currentToken.pos,"Invalid Syntax Error: Expected int"));
    }

    Node parse() {
        Node res = this.expr();
        return res;
    }

}

class ParseResult {
    Node node = null;
    Error error = null;

    public ParseResult(Node node, Error error) {
        this.node = node;
        this.error = error;
    }

    public ParseResult() {

    }

    Node register(Object res) {
        if (res instanceof ParseResult) {
            if (!((ParseResult) res).error.msg.isEmpty()) {
                this.error = ((ParseResult) res).error;
            }
            return ((ParseResult) res).node;
        }
        return (Node) res;
    }

    ParseResult success(Node node) {
        this.node = node;
        return this;
    }

    ParseResult failure(Error error) {
        this.error = error;
        return this;
    }

}