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
//        Node instr = this.instr();
//        while(!this.token.tokenType.equals(TokenType.End_of_input)){
//            Node listInstr = this.listInstr();
//        }
//        return new Node();
//    }

    Node instr() {
        Token currentToken = this.token;
        if (currentToken.tokenType.equals(TokenType.Mc_set)) {
            getNextToken();
            if (!this.token.tokenType.equals(TokenType.Identifiant)) {
                new Error(this.token.line, this.token.pos, "Invalid Syntax Error : Expected Identifiant").triggerError();
            }
            Token varNAme = this.token;
            getNextToken();
            if (!this.token.tokenType.equals(TokenType.Op_affectation)) {
                new Error(this.token.line, this.token.pos, "Invalid Syntax Error : Expected '='").triggerError();
            }
            Token EqSign = this.token;
            getNextToken();
            Node expr = this.expr();
            return new VarAssignNode(varNAme, expr);
        }
        else
        if (currentToken.tokenType.equals(TokenType.Mc_print)) {
            getNextToken();
            Node expr = this.expr();
            return new UnaryOpNode(currentToken,expr);
        }
        new Error(this.token.line, this.token.pos, "Invalid Syntax Error : Expected keyword").triggerError();
        return new Node();
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
            return new UnaryOpNode(currentToken, factor);
        } else if (currentToken.tokenType.equals(TokenType.entier)) {
            getNextToken();
//            result.register(this.getNextToken());
//            return result.success(new NumberNode(currentToken));
            return new NumberNode(currentToken);
        } else if (currentToken.tokenType.equals(TokenType.Identifiant)) {
            getNextToken();
//            result.register(this.getNextToken());
//            return result.success(new NumberNode(currentToken));
            return new VarAccessNode(currentToken);
        } else if (currentToken.tokenType.equals(TokenType.ParenGouche)) {
            getNextToken();
            Node expr = this.expr();
            if (this.token.tokenType.equals(TokenType.ParenDroit)) {
                getNextToken();
                return expr;
            } else new Error(this.token.line, this.token.pos, "Invalid Syntax Error").triggerError();
        } else
            if (currentToken.tokenType.equals(TokenType.Mc_let)){
                getNextToken();
                if (!this.token.tokenType.equals(TokenType.Identifiant)) {
                    new Error(this.token.line, this.token.pos, "Invalid Syntax Error : Expected Identifiant").triggerError();
                }
                Token localVar = this.token;
                getNextToken();
                if (!this.token.tokenType.equals(TokenType.Op_affectation)) {
                    new Error(this.token.line, this.token.pos, "Invalid Syntax Error : Expected '='").triggerError();
                }
                Token EqSign = this.token;
                getNextToken();
                Node exprLeft = this.expr();
                Node assignVar = new VarAssignNode(localVar,exprLeft);
                if (!this.token.tokenType.equals(TokenType.Mc_in)) {
                    new Error(this.token.line, this.token.pos, "Invalid Syntax Error : Expected 'IN'").triggerError();
                }
                Token INkey = this.token;
                getNextToken();
                Node exprRight = this.expr();

                return new BinOpNode(assignVar,INkey,exprRight);
            }
        return new Node();
//        return result.failure(new Error(currentToken.line,currentToken.pos,"Invalid Syntax Error: Expected int"));
    }

    Node parse() {
        Node res = this.instr();
        return res;
    }

}
