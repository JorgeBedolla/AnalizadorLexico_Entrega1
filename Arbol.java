
import java.util.List;


public class Arbol {
    private final Nodo raiz;
    private TablaSimbolos tabla = new TablaSimbolos();
    

    public Arbol(Nodo raiz){
        this.raiz = raiz;
    }

    public void recorrer(){
        for(Nodo n : raiz.getHijos()){
            Token t = n.getValue();
            Object res = null;

            switch (t.tipo){
                // Operadores aritméticos
                case SUM:
                case RES:
                case MUL:
                case DIV:              
                    //System.out.println("OPERACION ARITMETICA");
                    SolverAritmetico solver = new SolverAritmetico(n);
                    res = solver.resolver();
                    //System.out.println(res);

                break;
                // Operadores de comparacion
                case EQ:
                case NE:
                case LT:
                case LE:
                case GT:
                case GE:
                    //System.out.println("OPERACION DE COMPARACION");
                    SolverAritmetico solverC = new SolverAritmetico(n);
                    res = solverC.resolver();
                    //System.out.println(res);
                break;

                case VAR:
                    // Crear una variable. Usar tabla de simbolos
                    List<Nodo> tokenNodo = n.getHijos();
                    Token nombreID = tokenNodo.get(0).getValue();


                    //Token valor = tokenNodo.get(1).getValue();
                    
                    Nodo nodoValor = tokenNodo.get(1);
                    Object valor = null;

                    if(nodoValor.comprobarHijos()){
                        SolverAritmetico solverValor = new SolverAritmetico(nodoValor);
                        valor = solverValor.resolver();
                        

                    }else{
                        valor = nodoValor.getValue().literal;
                    }
                   // System.out.println("El valor a guardar es");
                    //System.out.println(valor);


                    //Comprobamos si existe la variable
                   if(tabla.existeIdentificador(nombreID.lexema)){
                    //Enviamos un error
                        System.out.println("ERROR SEMANTICO: la variable '"+nombreID.lexema+"' ya existe");
                        System.exit(1);
                   }else{
                    //Registramos la variable en la tabla de simbolos
                        tabla.asignar(nombreID.lexema, valor);
                   }

                    
                    break;

                case ASIG:
                  // Crear una variable. Usar tabla de simbolos
                    List<Nodo> listaAsigHijos = n.getHijos();
                    Token nombreVariable = listaAsigHijos.get(0).getValue();


                    //Token valor = tokenNodo.get(1).getValue();
                    
                    Nodo valorAsignar = listaAsigHijos.get(1);
                    Object valorA = null;

                    if(valorAsignar.comprobarHijos()){
                        SolverAritmetico solverValor = new SolverAritmetico(valorAsignar);
                        valorA = solverValor.resolver();
                        

                    }else{
                        valorA = valorAsignar.getValue().literal;
                    }
                    //System.out.println("El valor a guardar es");
                    //System.out.println(valorA);


                    //Comprobamos si existe la variable
                   if(tabla.existeIdentificador(nombreVariable.lexema)){
                    //Registramos la variable en la tabla de simbolos
                        tabla.asignar(nombreVariable.lexema, valorA);
                  
                   }else{
                      //Enviamos un error
                        System.out.println("ERROR SEMANTICO: la variable '"+nombreVariable.lexema+"' NO existe");
                        System.exit(1);        
                   }
                    break;
            
                //OPERACIONES LOGICAS
                case AND:
                case OR:
                case NOT:
                    //System.out.println("OPERACION LOGICA");
                    SolverAritmetico solverL = new SolverAritmetico(n);
                    res = solverL.resolver();
                    //System.out.println(res);
                    break;
                case IF:
                   List<Nodo> hijosIf = n.getHijos();//Obtenemos los hijos del if
                   Nodo condicionIf = hijosIf.get(0);//Obtenemos la expresion a evaluar
                   

                   SolverAritmetico solverIf = new SolverAritmetico(condicionIf);
                   Object resCondicion = solverIf.resolver();//resolvemos la expresion a evaluar


                   Arbol arbolBloqueIf = null;//creamos un arbol
                   if(resCondicion instanceof Boolean){//comrobamos que la expresion sea boleana
                        if((Boolean)resCondicion){//Verificamos que se cumpla la condicion
                            Nodo subA = new Nodo(null);
                            for(int i = 1; i < n.getHijos().size(); i++ ){
                                Nodo primerBloque = hijosIf.get(i);
                                subA.insertarSiguienteHijo(primerBloque);
                            }
                
                            arbolBloqueIf = new Arbol(subA);
                            arbolBloqueIf.recorrer();
                        } else{
                            int numeroHijos = n.getHijos().size() - 1;
                            if(hijosIf.size() > 2 && hijosIf.get(numeroHijos).getValue().tipo == TipoToken.ELSE){
                                Nodo segundoBloque = hijosIf.get(numeroHijos);
                                arbolBloqueIf = new Arbol(segundoBloque);
                                arbolBloqueIf.recorrer();
                            }
                        }

                   }else{
                    System.out.println("ERROR SEMANTICO: la condicion no devuelve un valor booleano valido para el IF");
                    System.exit(1);
                   }
                   
                    break;
                case WHILE:
                   List<Nodo> hijosWhile = n.getHijos();
                   //Obtenemos la condicion 
                   Nodo condicionWhile = hijosWhile.get(0);
                   //Asignamos la condicion
                   SolverAritmetico evaluarCondicionW = new SolverAritmetico(condicionWhile);
                  

                   //Creamos un subArbol para el bloque de instrucciones dentro del while
                   Nodo bloqueWhile = new Nodo(null);
                   for(int i = 1; i < n.getHijos().size(); i++){
                    Nodo nodoBloque = hijosWhile.get(i);
                    bloqueWhile.insertarSiguienteHijo(nodoBloque);
                   }
                   Arbol arbolBloqueWhile = new Arbol(bloqueWhile);

                   Object resConWhile = null;
                    //EJECUTAMOS EL BUCLE
                    while(true){
                        //Primero resolvemos la condicion
                        resConWhile = evaluarCondicionW.resolver();

                        //Comprobamos que la condicion sea instancia de un booleano

                        if(resConWhile instanceof Boolean){
                            //Si la condicion no se cumple rompemos el bucle
                            if(!(Boolean)resConWhile) break;
                            //Ejecutamos lo que se encuentra entre {}
                            arbolBloqueWhile.recorrer();

                        }else{
                            System.out.println("ERROR SEMANTICO: la condicion no devuelve un valor booleano valido para el WHILE");
                            System.exit(1);
                        }

                    }
                   break;
                case FOR:
                    List<Nodo> hijosFor = n.getHijos();

                    //Obtenemos la inicializacion 
                    Nodo inicializacionFor = hijosFor.get(0);
                    //Obtenemos la condicion
                    Nodo condicionFor = hijosFor.get(1);
                    //Obtenemos el incremento
                    Nodo incrementoFor = hijosFor.get(2);

                    //creamos un solver para la condicion
                    SolverAritmetico evaluarCondicionFor = new SolverAritmetico(condicionFor);

                    //Creamos un subArbol para el bloque de instrucciones del FOR
                    Nodo bloqueFor = new Nodo(null);
                    for(int i = 3; i < n.getHijos().size(); i++){
                        Nodo subNodoFor = hijosFor.get(i);
                        bloqueFor.insertarSiguienteHijo(subNodoFor);
                    }

                    Arbol arbolBloqueFor = new Arbol(bloqueFor);
                    Object resConFor = null;



                    //INICIALIZAMOS LA VARIABLE
                    Nodo bloqueInicializacionVariable = new Nodo(null);
                    bloqueInicializacionVariable.insertarSiguienteHijo(inicializacionFor);
                    Arbol arboInicializacion = new Arbol(bloqueInicializacionVariable);
                    arboInicializacion.recorrer();

                    //Creamos lo solver para el incremento;
                    Nodo bloqueIncrementoVariable = new Nodo(null);
                    bloqueIncrementoVariable.insertarSiguienteHijo(incrementoFor);
                    Arbol arbolIncremento = new Arbol(bloqueIncrementoVariable);


                    //EJECUTAMOS BUCLE
                    while(true){

                        //PRIMERO COMPROBAMOS LA CONDICION
                        resConFor = evaluarCondicionFor.resolver();
                        
                        if(resConFor instanceof Boolean){
                            if(!(Boolean)resConFor)break;
                            //DESPUES EJECUTAMOS EL BLOQUE
                           arbolBloqueFor.recorrer();

                        }else{
                            System.out.println("ERROR SEMANTICO: la condicion no devuelve un valor booleano valido para el WHILE");
                            System.exit(1);
                        }

                        // HACEMOS EL INCRMENTO
                        arbolIncremento.recorrer();
                        

                    } 
                    break;
                case PRINT:
                    List<Nodo> hijoAImprimir = n.getHijos();
                    Nodo nodoImprimir = hijoAImprimir.get(0);
                    if(nodoImprimir.comprobarHijos()){
                        SolverAritmetico solverImprimir = new SolverAritmetico(nodoImprimir);
                        res = solverImprimir.resolver();
                        System.out.println(res);

                    }else{
                        Token IDimprimir = hijoAImprimir.get(0).getValue();
                        if(IDimprimir.tipo == TipoToken.IDENTIFICADOR){
                             if(tabla.existeIdentificador(IDimprimir.lexema)){
                            System.out.println(tabla.obtener(IDimprimir.lexema));
                            }else{
                                System.out.println("ERROR SEMANTICO: la variable '"+IDimprimir.lexema+"' NO existe");
                                System.exit(1); 
                            }
                        }else{
                            System.out.println(IDimprimir.literal);
                        }                       
                    }
                    break;

            }
        }
    }

    public void imprimirArbol(){
        for(Nodo n : raiz.getHijos()){

            System.out.println(n.getValue().tipo);
            for(Nodo sub : n.getHijos()){
                System.out.println("==Segundo nivel");
                System.out.println(sub.getValue().tipo);
                 for(Nodo subN : sub.getHijos()){
                System.out.println("---------TERCER NIVEL nivel");
                System.out.println(subN.getValue().tipo);
                }
            }
        }
    }

}

