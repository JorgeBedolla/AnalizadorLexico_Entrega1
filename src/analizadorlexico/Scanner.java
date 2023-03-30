package analizadorlexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Alumno: Garcia Bedolla Jorge Omar
//Grupo: 3CV13

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;
    
    public String mensajeError = null;
    
    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        //Palabras reservadas
        palabrasReservadas.put("y", TipoToken.Y);
        palabrasReservadas.put("clase", TipoToken.CLASE);
        palabrasReservadas.put("ademas", TipoToken.ADEMAS );
        palabrasReservadas.put("falso", TipoToken.FALSO);
        palabrasReservadas.put("para", TipoToken.PARA);
        palabrasReservadas.put("fun", TipoToken.FUNCION); //definir funciones
        palabrasReservadas.put("si", TipoToken.SI);
        palabrasReservadas.put("nulo", TipoToken.NULO);
        palabrasReservadas.put("o", TipoToken.O);
        palabrasReservadas.put("imprimir", TipoToken.IMPRIMIR);
        palabrasReservadas.put("retornar", TipoToken.RETORNAR);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("este", TipoToken.ESTE);
        palabrasReservadas.put("verdadero", TipoToken.VERDADERO);
        palabrasReservadas.put("var", TipoToken.VAR); //definir variables
        palabrasReservadas.put("mientras", TipoToken.MIENTRAS);
    }

    

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        //Aquí va el corazón del scanner.
        int i = 0;
        //boolean read = false;
        int estado = 0;
        char[] cadena = (source + "   ").toCharArray();
        
        String buffer = "";
        
        //
        int ultimaPos = 0;
        
        
        
        while(i < cadena.length){
            //System.out.println(buffer+" - Estado: "+estado+" i = "+i+ " cadena[i]:"+cadena[i]);
            
            
            switch(estado){
                case 0:  
                    
                    if(esLetra(cadena[i])){
                        buffer = buffer + cadena[i];
                        estado = 1;
                        break;
                    }
                    
                    if(esDigito(cadena[i])){
                        buffer = buffer + cadena[i];
                        estado = 5;
                        break;
                    }
                    
                    if(cadena[i] == '"'){
                        buffer = buffer + cadena[i];
                        estado = 14;
                        break;
                    }
                    
                    if(cadena[i] == ' '){
                        estado = 0;
                    }else{
                        estado = 16;
                        i--;
                    }
                    
                    break;
                    
                case 1:
                    estado = 2;
                    if(esLetra(cadena[i])||esDigito(cadena[i])){
                        buffer = buffer + cadena[i];
                        estado = 1;
                    }
                    break;
                    
                case 2:
                    if(palabrasReservadas.get(buffer)== null){
                        estado = 4;
                    }else{
                        estado = 3; 
                    }
                    
                    break;
                case 3:
                    //CREAMOS TOKEN DE PALABRA RESERVADA
                    tokens.add(new Token(palabrasReservadas.get(buffer), buffer, null, linea));//Creamos token de palabra reservada
                    buffer = "";
                    estado = 0;
                    i = i - 3;
                    break;
                case 4:
                    //CREAMOS TOKEN DE IDENTIFICADOR
                    tokens.add(new Token(TipoToken.IDENTIFICADOR, buffer, null, linea));
                    buffer = "";
                    estado = 0;
                    i = i - 3;
                    break;
                    
                case 5:
                    estado = 6;
                    
                    if(esDigito(cadena[i])){
                        estado = 5;
                        buffer = buffer + cadena[i];
                        break;
                    };
                    
                    if(cadena[i] == '.'){
                        estado = 7;
                        buffer = buffer + cadena[i];
                        break;
                    }
                    
                    if(cadena[i] == 'E'){
                        estado = 10;
                        buffer = buffer + cadena[i];
                        break;
                    }
                    
                    break;
                case 6:
                    //CREAMOS NUMERO ENTERO
                    tokens.add(new Token(TipoToken.NUM, buffer, Double.parseDouble(buffer), linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                    
                case 7:
                    if(esDigito(cadena[i])){
                        estado = 8;
                        buffer = buffer + cadena[i];
                    }else{
                        mensajeError = "El formato de Numero es invalido";
                        System.out.println(mensajeError);
                    }
                    break;
                case 8:
                    if(esDigito(cadena[i])){
                        estado = 8;
                        buffer = buffer + cadena[i];
                        break;
                    }
                    
                    if(cadena[i] == 'E'){
                        buffer = buffer + cadena[i];
                        estado = 10;
                    }else{
                        ultimaPos = i;
                        estado = 9;
                    }
                    
                    break;
                case 9:
                    //CREAMOS UN NUMERO FLOTANTE
                    tokens.add(new Token(TipoToken.NUM, buffer, Double.parseDouble(buffer), linea));
                    buffer = "";
                    estado = 0;
                    //i = i - 4;
                    i = ultimaPos - 1;
                    break;
                case 10:
                    if(cadena[i] == '+' || cadena[i] == '-'){
                        buffer = buffer + cadena[i];
                        estado = 11;
                        break;
                    }
                    if(esDigito(cadena[i])){
                        buffer = buffer + cadena[i];
                        estado = 11;
                    }else{
                        mensajeError = "El formato del numero en notacion exponencial es invalido";
                        //System.out.println(mensajeError);
                    }
                    
                    break;
                case 11:
                    
                    if(esDigito(cadena[i])){
                        buffer = buffer + cadena[i];
                        estado = 12;
                    }else{
                        mensajeError = "El formato del numero en notacion exponencial es invalido";
                        //System.out.println(mensajeError);
                    }
                    
                    break;
                case 12:
                    if(esDigito(cadena[i])){
                        buffer = buffer + cadena[i];
                        estado = 12;
                    }else{
                        ultimaPos = i;
                        estado = 13;
                    }
                    break;
                case 13:
                    //CREAMOS UN NUMERO EXPONENCIAL
                    tokens.add(new Token(TipoToken.NUM, buffer, Double.parseDouble(buffer), linea));
                    buffer = "";
                    estado = 0;
                    i = ultimaPos - 1;
                    break;
                    
                case 14:
                    if(esLetra(cadena[i]) || esDigito(cadena[i])){
                        estado = 14;
                        buffer = buffer + cadena[i];
                        break;
                    }
                    if(cadena[i] == ' '){
                        estado = 14;
                        buffer = buffer + ' ';
                        break;
                    }
                    
                    if(cadena[i] == '"'){
                        estado = 15;
                        buffer = buffer + cadena[i];
                    }
                    
                    break;
                case 15:
                    //CREAMOS UNA CADENA
                    tokens.add(new Token(TipoToken.CADENA, buffer, buffer.replaceAll("\"", ""),linea));
                    buffer = "";
                    estado = 0;
                    break;
                case 16:
                    
                    if(cadena[i] == '!'){
                        buffer = buffer + cadena[i];
                        estado = 17;
                        break;
                    }
                    
                    if(cadena[i] == '='){
                       buffer = buffer + cadena[i];
                       estado = 20;
                       break;
                    }
                    
                    if(cadena[i] == '<'){
                       buffer = buffer + cadena[i];
                       estado = 23;
                       break;
                    }
                    
                    if(cadena[i] == '>'){
                       buffer = buffer + cadena[i]; 
                       estado = 26;
                       break;}
                    
                    if(cadena[i] == '/' && cadena[i+1] == '/'){
                        estado = 30;
                        i++;
                        break;
                    }
                    
                    if(cadena[i] == '/' && cadena[i+1] == '*'){
                        estado = 31;
                        i++;
                        break;
                    }
                    
                    else{
                        estado = 29;
                        i--;
                    }
                    break;
                    
                case 17:
                    if(cadena[i] == '='){
                        buffer = buffer + cadena[i];
                        estado = 18;
                    }else{
                        
                        estado = 19;
                    }
                    break;
                    
                case 18:
                    //CREAMOS UN TOKEN !=
                    tokens.add(new Token(TipoToken.NE, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                case 19:
                    //CREAMOS UN TOKEN !
                    tokens.add(new Token(TipoToken.NOT, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                    
                case 20:
                    if(cadena[i] == '='){
                        buffer = buffer + cadena[i];
                        estado = 21;
                    }else{
                        estado = 22;
                    }
                    break;
                case 21:
                    //CREAMOS TOKEN ==
                    tokens.add(new Token(TipoToken.EQ, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                case 22:
                    //CREAMOS TOKEN =
                    tokens.add(new Token(TipoToken.ASIG, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                case 23:
                    if(cadena[i] == '='){
                        buffer = buffer + cadena[i];
                        estado = 24;
                    }else{
                        estado = 25;
                    }
                    break;
                case 24:
                    //CREAMOS TOKEN <=
                    tokens.add(new Token(TipoToken.LE, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                case 25:
                    //CREAMOS TOKEN <
                    tokens.add(new Token(TipoToken.LT, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                case 26:
                    if(cadena[i] == '='){
                        buffer = buffer + cadena[i];
                        estado = 27;
                    }else{
                        estado = 28;
                    }
                    break;
                case 27:
                    //CREAMOS TOKEN >=
                    tokens.add(new Token(TipoToken.GE, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                case 28:
                    //Creamos TOKEN >
                    tokens.add(new Token(TipoToken.GT, buffer, null,linea));
                    buffer = "";
                    estado = 0;
                    i = i - 2;
                    break;
                case 29:
                    //COMPROBAMOS SI HAY UN SIMBOLO
                    Token t = buscarSigno(cadena[i], linea);
                    if(t == null){
                        if(cadena [i] == '\n'){//Comprobamos si hay un salto de linea
                            linea++;
                            estado = 0;
                        }
                        else{
                            mensajeError = "caracter invalido";
                        }
                        
                    } 
                    else {
                        tokens.add(t);
                        buffer = "";
                        estado = 0;
                    }
                    
                    break;
                case 30:
                    if(cadena[i] != '\n'){
                        estado = 30;
                        break;
                    }else{
                        estado = 0;
                        break;
                    }
                case 31:
                    if(cadena[i] == '*'){
                        estado = 32;
                        break;
                    }else{
                        estado = 31;
                        break;
                    }
                case 32:
                    if(cadena[i] == '/'){
                        estado = 0;
                        break;
                    }else{
                        mensajeError = "formato de comentario invalido";
                        break;
                    }
                default:
                    mensajeError = "Ha ocurrido un error";
                    break;
            }
            i++;
        }
        

        /*
        Analizar el texto de entrada para extraer todos los tokens
        y al final agregar el token de fin de archivo
         */
        tokens.add(new Token(TipoToken.EOF, "", null, linea));

        return tokens;
    }
    
    
     public static boolean esDigito(char c){
        return Character.isDigit(c);
    }
    
    public static boolean esLetra(char c){
        return Character.isLetter(c);
    }
    
    public static Token buscarSigno(char c,int linea){
        Token t = null;
        
        switch(c){
            case '(':
                t = new Token(TipoToken.IZQ_PAR, "(" ,null, linea);
                break;
            case ')':
                t = new Token(TipoToken.DER_PAR, ")" ,null, linea);
                break;
            case '{':
                t = new Token(TipoToken.IZQ_LLA, "{" ,null, linea);
                break;
            case '}':
                t = new Token(TipoToken.DER_LLA, "}" ,null, linea);
                break;
            case ',':
                t = new Token(TipoToken.COMA, "," ,null, linea);
                break;
            case '.':
                t = new Token(TipoToken.PUNTO, "." ,null, linea);
                break;
            case ';':
                t = new Token(TipoToken.PUNTO_COMA, ";" ,null, linea);
                break;
            case '-':
                t = new Token(TipoToken.MENOS, "-" ,null, linea);
                break;
            case '+':
                t = new Token(TipoToken.MAS, "+" ,null, linea);
                break;
            case '*':
                t = new Token(TipoToken.MUL, "*" ,null, linea);
                break;
            case '/':
                t = new Token(TipoToken.DIV, "/" ,null, linea);
                break;
        }
        
        return t;
    }
    
    public boolean comprobarErrores(){
        if(mensajeError != null)
            return true;
        return false;
    }
    
    public void incrementarLinea(){
        linea++;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getLinea() {
        return linea;
    }
    
}



/*
Signos o símbolos del lenguaje:
(
)
{
}
,
.
;
-
+
*
/
!
!=
=
==
<
<=
>
>=
// -> comentarios (no se genera token)
/* ... * / -> comentarios (no se genera token)
Identificador,
Cadena
Numero
Cada palabra reservada tiene su nombre de token

 */