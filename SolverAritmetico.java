
public class SolverAritmetico {

    private final Nodo nodo;
    private TablaSimbolos tabla = new TablaSimbolos();
    private boolean error = false;

    public SolverAritmetico(Nodo nodo) {
        this.nodo = nodo;
    }

    public Object resolver(){
        return resolver(nodo);
    }

    private Object resolver(Nodo n){

        // No tiene hijos, es un operando
        if(n.getHijos() == null){
            if(n.getValue().tipo == TipoToken.NUMBER || n.getValue().tipo == TipoToken.STRING){
                return n.getValue().literal;
            }
            else if(n.getValue().tipo == TipoToken.IDENTIFICADOR){
                // Ver la tabla de símbolos
                if(tabla.existeIdentificador(n.getValue().lexema)){
                    return tabla.obtener(n.getValue().lexema);
                }else{
                    System.out.println("ERROR: la variable '"+n.getValue().lexema+"' NO existe");
                    error = true;
                    System.exit(1);
                }  
            }else if(n.getValue().tipo == TipoToken.TRUE || n.getValue().tipo == TipoToken.FALSE){
                if(n.getValue().tipo == TipoToken.TRUE){
                    return Boolean.TRUE;
                }else{
                    return Boolean.FALSE;
                }
            }
        }

        if(n.getValue().tipo == TipoToken.NOT){
            Object resultadoNegar = resolver(n.getHijos().get(0));
            if(resultadoNegar instanceof Boolean){
                return !((Boolean)resultadoNegar);
            }

        }
        // Por simplicidad se asume que la lista de hijos del nodo tiene dos elemento

        Nodo izq = n.getHijos().get(0);
        Nodo der = n.getHijos().get(1);

        Object resultadoIzquierdo = resolver(izq);
        Object resultadoDerecho = resolver(der);

        
        if(resultadoIzquierdo instanceof Double && resultadoDerecho instanceof Double){
            //OPERACIONES ARITMETICAS
            switch (n.getValue().tipo){
                
                case SUM:
                    return ((Double)resultadoIzquierdo + (Double) resultadoDerecho);
                case RES:
                    return ((Double)resultadoIzquierdo - (Double) resultadoDerecho);
                case MUL:
                    return ((Double)resultadoIzquierdo * (Double) resultadoDerecho);
                case DIV:
                    return ((Double)resultadoIzquierdo / (Double) resultadoDerecho);
            }

            //OPERACIONES DE COMPARACION
            Double DResIzq = (Double) resultadoIzquierdo;
            Double DResDer = (Double) resultadoDerecho;
            switch (n.getValue().tipo) {
                case EQ:
                    return DResIzq.equals(DResDer);
                case NE:
                    return !(DResIzq.equals(DResDer));
                case LT:
                    return (DResIzq < DResDer);//MENOR
                case LE:
                    return (DResIzq <= DResDer);
                case GT:
                    return (DResIzq > DResDer);//MAYOR
                case GE:
                    return (DResIzq >= DResDer);
            }

            System.out.println("ERROR SEMANTICO: operacion invalida para tipos de dato Double");
            error = true;
            System.exit(1);
        }
        else if(resultadoIzquierdo instanceof String && resultadoDerecho instanceof String){
            if (n.getValue().tipo == TipoToken.SUM){
                // Ejecutar la concatenación
                return ((String)resultadoIzquierdo + (String)resultadoDerecho);
            }else if(n.getValue().tipo == TipoToken.EQ){
                String cadena1 = (String)resultadoIzquierdo;
                String cadena2 = (String)resultadoDerecho;
                return cadena1.equals(cadena2);
            }else if(n.getValue().tipo == TipoToken.NE){
                String cadena1 = (String)resultadoIzquierdo;
                String cadena2 = (String)resultadoDerecho;
                return !cadena1.equals(cadena2);
            }else{
                // Error por diferencia de tipos
                System.out.println("ERROR SEMANTICO: operacion invalida para String");
                //error = true;
                System.exit(1);

            }
        }else if(resultadoIzquierdo instanceof Boolean && resultadoDerecho instanceof Boolean){ 
            switch(n.getValue().tipo){
                case AND:
                    return ((Boolean)resultadoIzquierdo && (Boolean)resultadoDerecho);
                case OR:
                    return ((Boolean)resultadoIzquierdo || (Boolean)resultadoDerecho);
            }
        }else{
            // Error por diferencia de tipos
            System.out.println("ERROR: Diferencia de tipos");
            error = true;
            System.exit(1);
        }

        return null;
    }


}
