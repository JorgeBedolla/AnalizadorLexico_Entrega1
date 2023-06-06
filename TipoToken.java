
public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)

    IDENTIFICADOR,

    // Palabras reservadas
    //SELECT, FROM, DISTINCT,
    FOR, IF,
    ELSE, PRINT, RETURN, WHILE,

    // Caracteres
    COMA, PUNTO, 
    LLAVE_IZQ, LLAVE_DER, PAR_IZQ, PAR_DER,
    PUNTO_COMA,

    //Declaraciones
    CLASS, FUN, VAR,
    
    //COMPARADORES
    EQ, NE,
    LE, LT,// <
    GE, GT,// >
    ASIG, //=

    //Operaciones
    SUM, RES, DIV, MUL,
    //OPERADORES LOGICOS
    OR, AND, NOT,
    //RESERVADAS
    TRUE, FALSE, NULL, THIS, NUMBER, STRING, SUPER,


    // Final de cadena
    EOF
}