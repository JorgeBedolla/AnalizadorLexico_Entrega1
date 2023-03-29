package analizadorlexico;

public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)


    // Palabras clave:
    Y, 
    CLASE,
    ADEMAS,
    FALSO,
    PARA,
    FUNCION,
    SI,
    NULO,
    O,
    IMPRIMIR,
    RETORNAR,
    SUPER,
    ESTE,
    VERDADERO,
    VAR,
    MIENTRAS,

    //id, string, num
    IDENTIFICADOR,
    CADENA,
    NUM,

    //signos o simbolos

    //SIGNOS
    //=====
    DER_PAR, // (
    IZQ_PAR, // )
    DER_LLA, // }
    IZQ_LLA, // {
    COMA, // , 
    PUNTO,  // .
    PUNTO_COMA, // ;
    
    //OPERADORES
    //=======
    MENOS,  //-
    MAS,  // +
    MUL, // *
    DIV, // /
    NOT, // !
    
    //OPERADORES RELACIONALES
    //=====
    NE, // !=
    ASIG, //=
    EQ, // ==
    LT, // <
    LE, // <=
    GT, // >
    GE, // >=
    
    



    // Final de cadena
    EOF
}
