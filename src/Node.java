
public class Node {
    public Node left, right;
    Token token;

    public Node() {
        this.left = null;
        this.right = null;
        this.token = null;
    }

    public Node(Node left, Node right, Token token) {
        this.left = left;
        this.right = right;
        this.token = token;
    }

    public Node(Token token) {
        this.token = token;
    }
    @Override
    public String toString() {
        return "("+this.left+", "+this.token+", "+this.right+")";
    }
}

class NumberNode extends Node {
    public NumberNode(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return "" + token;
    }
}

class BinOpNode extends Node {
    public BinOpNode(Node left, Token token, Node right) {
        super(left, right, token);
    }

    @Override
    public String toString() {
        return "("+this.left+", "+this.token+", "+this.right+")";
    }
}

class UnaryOpNode extends Node{
    Node node;
    public UnaryOpNode(Token token, Node node) {
        super(token);
        this.node = node;
    }

    @Override
    public String toString() {
        return "("+this.token+","+this.node+")";
    }
}