import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private final Token value;
    private List<Nodo> hijos;

    public Nodo(Token value){
        this.value = value;
    }

    public void insertarHijo(Nodo n){
        if(hijos == null){
            hijos = new ArrayList<>();
            hijos.add(n);
        }
        else{
            hijos.add(0, n);
        }
    }

    public void insertarSiguienteHijo(Nodo n){
        if(hijos == null){
            hijos = new ArrayList<>();
            hijos.add(n);
        }
        else{
            hijos.add(n);
        }
    }

    public void insertarHijos(List<Nodo> nodosHijos){
        if(hijos == null){
            hijos = new ArrayList<>();
        }

        for(Nodo n : nodosHijos){
            hijos.add(n);
        }
    }

    public Token getValue(){
        return value;
    }

    public List<Nodo> getHijos(){
        return hijos;
    }

    public boolean comprobarHijos(){
        //retorno verdadero si el nodo tiene hijos
        if(hijos == null){
            return false;
        }
        //retorno falso si el nodo NO tiene hijos
        return true;
    }

}