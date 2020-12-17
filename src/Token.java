public class Token {
    TokenType tokenType;
    String value;
    int line;
    int pos;

    public  Token(TokenType tokenType, String value, int line, int pos) {
        this.tokenType = tokenType;
        this.value = value;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public String toString() {
        String result = String.format("%s", this.tokenType);
        switch (this.tokenType) {
            case entier:
            case Identifiant:
                result += String.format(":%s", value);
                break;
        }
        return result;
    }
}
