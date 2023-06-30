public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int linea;

    public Token(TipoToken tipo, String lexema, Object literal, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.linea = linea;
    }


    public Token(TipoToken tipo, String lexema, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.literal = null;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal; 
        this.linea = 0;
    }


    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.linea = 0;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }

        if(this.tipo == ((Token)o).tipo){
            return true;
        }

        return false;
    }

    public String toString(){
        return "< "+tipo + " " + lexema + " " + literal +">";
    }  
    
    
    // Métodos auxiliares
    public boolean esOperando(){
        switch (this.tipo){
            case IDENTIFICADOR:
            case NUMBER:
            case STRING:
            case TRUE:
            case FALSE:
                return true;
            default:
                return false;
        }
    }

    public boolean esOperador(){
        switch (this.tipo){
            case SUM:
            case RES:
            case MUL:
            case DIV:
            case NE:
            case EQ:
            case GT:
            case GE:
            case LT:
            case LE:
            //OPERACIONES LOGICAS
            case AND:
            case OR:
            case ASIG:
            case NOT:
                return true;
            default:
                return false;
        }
    }

    public boolean esPalabraReservada(){
        switch (this.tipo){
            case FOR:
            case RETURN:
            case WHILE:
            case CLASS:
            case FUN:

            case VAR:
            case IF:
            case PRINT:
            case ELSE:
                return true;
            default:
                return false;
        }
    }

    public boolean esEstructuraDeControl(){
        switch (this.tipo){
            case IF:
            case ELSE:

            case FOR:
            case WHILE:
                return true;
            default:
                return false;
        }
    }

    public boolean precedenciaMayorIgual(Token t){
        return this.obtenerPrecedencia() >= t.obtenerPrecedencia();
    }

    private int obtenerPrecedencia(){
        switch (this.tipo){
            case MUL:
            case DIV:
                return 7;
            case SUM:
            case RES:
                return 6;
            case GT:
            case GE:
            case LT:
            case LE:
                return 5;
            case EQ:
            case NE:
                return 4;
            case AND:
                return 3;
            case OR:
                return 2;
            case ASIG:
                return 1;
        }

        return 0;
    }

    public int aridad(){
        switch (this.tipo) {
            case MUL:
            case DIV:
            case SUM:
            case RES:
            case EQ:
            case NE:
            case GT:
            case GE:
            case LT: 
            case LE:
            case AND:
            case OR:
            case ASIG:
                return 2;
            case NOT:
               return 1;
        }
        return 0;
    }
}

