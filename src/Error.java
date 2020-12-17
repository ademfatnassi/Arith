class Error {
    int line;
    int pos;
    String msg;

    public Error(int line, int pos, String msg) {
        this.line = line;
        this.pos = pos;
        this.msg = msg;

    }
    void triggerError(){
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }
}

class IllegalCharError extends Error{

    public IllegalCharError(int line, int pos, String msg) {
        super(line, pos, msg);
    }
}
