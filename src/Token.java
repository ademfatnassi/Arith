public class Token {
    TokenType tokenType;
    String value;
    int line;
    int pos;

    public Token(TokenType tokenType, String value, int line, int pos) {
        this.tokenType = tokenType;
        this.value = value;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public String toString() {
        String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokenType);
        switch (this.tokenType) {
            case entier:
                result += String.format("  %4s", value);
                break;
            case Identifiant:
                result += String.format(" %s", value);
                break;
        }
        return result;
    }
}
