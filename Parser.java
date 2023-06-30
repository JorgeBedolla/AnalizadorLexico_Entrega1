
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    
    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "");

    //==================================================================
    //-------------------------DECLARACIONES----------------------------
    private final Token classDecl = new Token(TipoToken.CLASS, "class");
    private final Token classInher = new Token(TipoToken.LT, "<");
    private final Token funDecl = new Token(TipoToken.FUN, "fun");
    private final Token varDecl = new Token(TipoToken.VAR, "var");
    private final Token varInit = new Token(TipoToken.ASIG, "=");

    //-------------------------SENTENCIAS---------------------------
    private final Token forStmt = new Token(TipoToken.FOR, "for");
    private final Token ifStmt = new Token(TipoToken.IF, "if");
    private final Token elseStmt = new Token(TipoToken.ELSE, "else");
    private final Token printStmt = new Token(TipoToken.PRINT, "print");
    private final Token returnStmt = new Token(TipoToken.RETURN, "return");
    private final Token whileStmt = new Token(TipoToken.WHILE, "while");

    //-------------------------EXPRESIONES---------------------------
    private final Token asig = new Token(TipoToken.ASIG, "=");
    private final Token orStmt = new Token(TipoToken.OR, "or");
    private final Token andStmt = new Token(TipoToken.AND, "and");
    private final Token equalStmt = new Token(TipoToken.EQ, "==");
    private final Token notEqualStmt = new Token(TipoToken.NE, "!=");
    private final Token gtStmt = new Token(TipoToken.GT, ">");
    private final Token geStmt = new Token(TipoToken.GE, ">=");
    private final Token ltStmt = new Token(TipoToken.LT, "<");
    private final Token leStmt = new Token(TipoToken.LE, "<=");
    


    


    private final Token parIzq = new Token(TipoToken.PAR_IZQ, "(");
    private final Token parDer = new Token(TipoToken.PAR_DER, ")");
    
    private final Token llaveIzq = new Token(TipoToken.LLAVE_IZQ, "{");
    private final Token llaveDer = new Token(TipoToken.LLAVE_DER, "}");

    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token puntoComa = new Token(TipoToken.PUNTO_COMA, ";");

    
    private final Token mul = new Token(TipoToken.MUL, "*");
    private final Token sum = new Token(TipoToken.SUM, "+");
    private final Token res = new Token(TipoToken.RES, "-");
    private final Token div = new Token(TipoToken.DIV, "/");

    private final Token notStmt = new Token(TipoToken.NOT, "!");
    //-------------------------RESERVADAS--------------------------
    
    private final Token trueStmt = new Token(TipoToken.TRUE, "true");
    private final Token falseStmt = new Token(TipoToken.FALSE, "false");
    private final Token nullStmt = new Token(TipoToken.NULL, "null");
    private final Token thisStmt = new Token(TipoToken.THIS, "this");
    private final Token numberStmt = new Token(TipoToken.NUMBER, "number");
    private final Token stringStmt = new Token(TipoToken.STRING,"string");
    private final Token superStmt = new Token(TipoToken.SUPER, "super");


    private final Token finCadena = new Token(TipoToken.EOF, "");

    //==================================================================

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    private int posicion = 0;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        posicion++;
        PROGRAM();
       

        if(!hayErrores && !preanalisis.equals(finCadena)){
            System.out.println("ERROR PARSER: la linea " + posicion + ". No se esperaba el token " + preanalisis.tipo);
            hayErrores = true;
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            //System.out.println("SENTENCIA VALIDA");
        }

        /*if(!preanalisis.equals(finCadena)){
            System.out.println("Error en la posicion " + posicion + ". No se esperaba el token " + preanalisis.tipo);
        }else if(!hayErrores){
            System.out.println("sentencia válida");
        }*/
    }

//=====================================================================

    void PROGRAM(){
        if(hayErrores)return;
        DECLARATION();
    }
//---------------------DECLARACIONES-------------------------------------
    void DECLARATION(){
        if(hayErrores)return;
        if(preanalisis.equals(classDecl)){
            CLASS_DECL();
            DECLARATION();
        }else if(preanalisis.equals(funDecl)){
            FUN_DECL();
            DECLARATION();
        }else if(preanalisis.equals(varDecl)){
            VAR_DECL();
            DECLARATION();
        }else if(!preanalisis.equals(finCadena) && !preanalisis.equals(llaveDer)){
            STATEMENT();
            DECLARATION();
        }else{
            return;
        }
    }

    void CLASS_DECL(){
        if(hayErrores)return;

        if(preanalisis.equals(classDecl)){
            coincidir(classDecl);
            coincidir(identificador);
            CLASS_INHER();
            coincidir(llaveIzq);
            FUNCTIONS();
            coincidir(llaveDer);
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + ". Se esperaba un class");
        }
    }

    void CLASS_INHER(){
        if(hayErrores)return;

        if(preanalisis.equals(classInher))
        {
            coincidir(classInher);
            coincidir(identificador);
        }
    }

    void FUN_DECL(){
        if(hayErrores)return;

        if(preanalisis.equals(funDecl)){
            coincidir(funDecl);
            FUNCTION();
        }
        else{
            hayErrores = true;
            System.out.println("ERROR PARSER: en la posicion " + posicion + ". Se esperaba un fun");
        }
    }

    void VAR_DECL(){
        if(hayErrores)return;

        if(preanalisis.equals(varDecl)){
            coincidir(varDecl);
            coincidir(identificador);
            VAR_INIT();
            coincidir(puntoComa);
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + ". Se esperaba un fun");
        }
    }

    void VAR_INIT(){
        if(hayErrores)return;

        if(preanalisis.equals(varInit)){
            coincidir(varInit);
            EXPRESSION();
        }
    }

//---------------------SENTENCIAS------------------------------------
    void STATEMENT(){
        if(hayErrores)return;

        if(preanalisis.equals(forStmt)){
            FOR_STMT();
        }else if(preanalisis.equals(ifStmt)){
            IF_STMT();
        }else if(preanalisis.equals(printStmt)){
            PRINT_STMT();
        }else if(preanalisis.equals(returnStmt)){
            RETURN_STMT();
        }else if(preanalisis.equals(whileStmt)){
            WHILE_STMT();
        }else if(preanalisis.equals(llaveIzq)){
            BLOCK();
        }else{  
            EXPR_STMT();
        }
    }

    void EXPR_STMT(){
        if(hayErrores)return;

        EXPRESSION();
        coincidir(puntoComa);
    }

    void FOR_STMT(){
        if(hayErrores)return;

        if(preanalisis.equals(forStmt)){
            coincidir(forStmt);
            coincidir(parIzq);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            coincidir(parDer);
            STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + " FORMATO DE SENTENCIA FOR INVALIDO");
        }
    }
    
    void FOR_STMT_1(){
        if(hayErrores)return;

        if(preanalisis.equals(varDecl)){
            VAR_DECL();
        }else if(preanalisis.equals(puntoComa)){

            coincidir(puntoComa);
        }else{
            EXPR_STMT();
        }
    }

    void FOR_STMT_2(){
        if(hayErrores)return;


        if(preanalisis.equals(puntoComa)){
            coincidir(puntoComa);
        }else{
            EXPRESSION();
            coincidir(puntoComa);
        }
    }

    void FOR_STMT_3(){
        if(hayErrores)return;
        if(!preanalisis.equals(parDer)){
            EXPRESSION();
        }
        
    }

    void IF_STMT(){
        if(hayErrores)return;

        if(preanalisis.equals(ifStmt)){
            coincidir(ifStmt);
            coincidir(parIzq);
            EXPRESSION();
            coincidir(parDer);
            STATEMENT();
            ELSE_STATEMENT();

        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + " formato if invalido");
        }
    }

    void ELSE_STATEMENT(){
        if(hayErrores)return;

        if(preanalisis.equals(elseStmt)){
            coincidir(elseStmt);
            STATEMENT();
        }
    }

    void PRINT_STMT(){
        if(hayErrores)return;

        if(preanalisis.equals(printStmt)){
            coincidir(printStmt);
            EXPRESSION();
            coincidir(puntoComa);
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + " formato print invalido");
        }
    }

    void RETURN_STMT(){
        if(hayErrores)return;

        if(preanalisis.equals(returnStmt)){
            coincidir(returnStmt);
            RETURN_EXP_OPC();
            coincidir(puntoComa);
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + " formato return invalido");
        }
    }

    void RETURN_EXP_OPC(){
        if(hayErrores)return;
        if(!preanalisis.equals(puntoComa)){
            EXPRESSION();
        }
        
    }

    void WHILE_STMT(){
        if(hayErrores)return;

        if(preanalisis.equals(whileStmt)){
            coincidir(whileStmt);
            coincidir(parIzq);
            EXPRESSION();
            coincidir(parDer);
            STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + " formato while invalido");
        }
    }

    void BLOCK(){
        if(hayErrores)return;

        if(preanalisis.equals(llaveIzq)){
            coincidir(llaveIzq);
            BLOCK_DECL();
            coincidir(llaveDer);
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + " se esperaba un bloque");
        }
    }

    void BLOCK_DECL(){
        if(hayErrores)return;
        if(!preanalisis.equals(finCadena) && !preanalisis.equals(llaveDer)){
            DECLARATION();
            BLOCK_DECL();
        }
    }

//---------------------EXPRESIONES------------------------------------
    void EXPRESSION(){
        if(hayErrores)return;
        ASSIGNMENT();
    }

    void ASSIGNMENT(){
        if(hayErrores)return;
        LOGIC_OR();
        ASSIGNMENT_OPC();
    }

    void ASSIGNMENT_OPC(){
        if(hayErrores)return;

        if(preanalisis.equals(asig)){
            coincidir(asig);
            EXPRESSION();
        }
    }

    void LOGIC_OR(){
        if(hayErrores)return;
        LOGIC_AND();
        LOGIC_OR_2();
    }

    void LOGIC_OR_2(){
        if(hayErrores)return;

        if(preanalisis.equals(orStmt)){
            coincidir(orStmt);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    void LOGIC_AND(){
        if(hayErrores)return;
        EQUALITY();
        LOGIC_AND_2();
    }

    void LOGIC_AND_2(){
        if(hayErrores)return;

        if(preanalisis.equals(andStmt)){
            coincidir(andStmt);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    void EQUALITY(){
        if(hayErrores)return;
        COMPARISON();
        EQUALITY_2();
    }

    void EQUALITY_2(){
        if(hayErrores)return;

        if(preanalisis.equals(notEqualStmt)){
            coincidir(notEqualStmt);
            COMPARISON();
            EQUALITY_2();
        }else if(preanalisis.equals(equalStmt)){
            coincidir(equalStmt);
            COMPARISON();
            EQUALITY_2();
        }
    }

    void COMPARISON(){
        if(hayErrores)return;

        TERM();
        COMPARISON_2();
    }

    void COMPARISON_2(){
        if(hayErrores)return;

        if(preanalisis.equals(gtStmt)){
            coincidir(gtStmt);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(geStmt)){
            coincidir(geStmt);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(ltStmt)){
            coincidir(ltStmt);
            TERM();
            COMPARISON_2();

        }else if(preanalisis.equals(leStmt)){
            coincidir(leStmt);
            TERM();
            COMPARISON_2();
        }
    }

    void TERM(){
        if(hayErrores)return;

        FACTOR();
        TERM_2();
    }

    void TERM_2(){
        if(hayErrores)return;

        if(preanalisis.equals(res)){
            coincidir(res);
            FACTOR();
            TERM_2();
        }else if(preanalisis.equals(sum)){
            coincidir(sum);
            FACTOR();
            TERM_2();
        }
    }

    void FACTOR(){
        if(hayErrores)return;

        UNARY();
        FACTOR_2();
    }


    void FACTOR_2(){
        if(hayErrores)return;
        if(preanalisis.equals(div)){
            coincidir(div);
            UNARY();
            FACTOR_2();
        }else if(preanalisis.equals(mul)){
            coincidir(mul);
            UNARY();
            FACTOR_2();
        }
    }

    void UNARY(){
        if(hayErrores)return;

        if(preanalisis.equals(notStmt)){
            coincidir(notStmt);
            UNARY();
        }else if(preanalisis.equals(res)){
            coincidir(res);
            UNARY();
        }else{
            CALL();
        }
    }

    void CALL(){
        if(hayErrores)return;

        PRIMARY();
        CALL_2();
    }

    void CALL_2(){
        if(hayErrores)return;

        if(preanalisis.equals(parIzq)){
            coincidir(parIzq);
            ARGUMENTS_OPC();
            coincidir(parDer);
            CALL_2();
        }else if(preanalisis.equals(punto)){
            coincidir(punto);
            coincidir(identificador);
            CALL_2();
        }
    }


    void CALL_OPC(){
        if(hayErrores)return;
        CALL();
        coincidir(punto);
    }

    void PRIMARY(){
        if(hayErrores)return;

        
        if(preanalisis.equals(trueStmt)){
            coincidir(trueStmt);
        }else if(preanalisis.equals(falseStmt)){
            coincidir(falseStmt);
        }else if(preanalisis.equals(nullStmt)){
            coincidir(nullStmt);
        }else if(preanalisis.equals(thisStmt)){
            coincidir(thisStmt);
        }else if(preanalisis.equals(numberStmt)){
            coincidir(numberStmt);
        }else if(preanalisis.equals(stringStmt)){
            coincidir(stringStmt);
        }else if(preanalisis.equals(identificador)){
            coincidir(identificador);
        }else if(preanalisis.equals(parIzq)){
            coincidir(parIzq);
            EXPRESSION();
            coincidir(parDer);
        }else if(preanalisis.equals(superStmt)){
            coincidir(superStmt);
            coincidir(punto);
            coincidir(identificador);
        /*}else if(preanalisis.equals(parDer)){
            return;
        */}else{
            hayErrores = true;
            System.out.println("PARSER ERROR en la posicion " + posicion + " formato invalido, se esperaba una expresion");   
        }
    }

//---------------------OTRAS-------------------------------------------

    void FUNCTION(){
        if(hayErrores)return;
        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            coincidir(parIzq);
            PARAMETERS_OPC();
            coincidir(parDer);
            BLOCK();
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR:en la posicion " + posicion + " formato invalido, se esperaba un identificador");   
        }
    }

    void FUNCTIONS(){
        if(hayErrores)return;

        if(preanalisis.equals(identificador)){
            FUNCTION();
            FUNCTIONS();
        }
    }

    void PARAMETERS_OPC(){
        if(hayErrores)return;

        if(preanalisis.equals(identificador)){
            PARAMETERS();
        }
    }

    void PARAMETERS(){
        if (hayErrores)return;

        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            PARAMETERS_2();
        }else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + " formato invalido, se esperaba un identificador");    
        }
    }

    void PARAMETERS_2(){
        if(hayErrores)return;
        if(preanalisis.equals(coma)){
            coincidir(coma);
            coincidir(identificador);
            PARAMETERS_2();
        }
    }

    void ARGUMENTS_OPC(){
        if(hayErrores)return;
        if(!preanalisis.equals(parDer)){
            ARGUMENTS();
        }
        
    }

    void ARGUMENTS(){
        if(hayErrores)return;
        EXPRESSION();
        ARGUMENTS_2();
    }

    void ARGUMENTS_2(){
        if(hayErrores)return;
        if(preanalisis.equals(coma)){
            coincidir(coma);
            EXPRESSION();
            ARGUMENTS_2();
        }
    }
//=====================================================================

    void coincidir(Token t){
        if(hayErrores) return;
      // System.out.println(preanalisis.tipo+"---"+t.tipo+"==="+(preanalisis.tipo == t.tipo));
        if(preanalisis.tipo == t.tipo){
            i++;
            posicion++;
            preanalisis = tokens.get(i);
            //System.out.println("i = "+i+" TOKEN = "+preanalisis);
        }
        else{
            hayErrores = true;
            System.out.println("PARSER ERROR: en la posicion " + posicion + ". Se esperaba un  " + t.tipo);
            //System.exit(1);
        }
    }

    public Boolean getErrores(){
        if(hayErrores) return true;
            return false;
    }

}
