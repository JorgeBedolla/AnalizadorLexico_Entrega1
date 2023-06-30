import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
//VERSION FINAL 30/06/2023
//Alumno: Garcia Bedolla Jorge Omar
//Grupo: 3CV13

public class Principal {

    static boolean existenErrores = false;
    static int contadorLinea = 1;

    public static void main(String[] args) throws IOException {
      
        if(args.length > 1) {
            System.out.println("Uso correcto: interprete [script]");
            // Convención defininida en el archivo "system.h" de UNIX
            System.exit(64);
        } else if(args.length == 1){
            ejecutarArchivo(args[0]);
        } else{
            //ejecutarArchivo("Pruebas.txt"); //Para probar el archivo de ejemplo.txt;
            ejecutarPrompt();
        }
        
        
    }

    private static void ejecutarArchivo(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String codigo = new String(bytes, Charset.defaultCharset());
        codigo = codigo.replaceAll("\r"," ");
        ejecutar(codigo);     
        // Se indica que existe un error
        //if(existenErrores) System.exit(65);
    }

    private static void ejecutarPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();
            if(linea == null) break; // Presionar Ctrl + D
            ejecutar(linea);
            contadorLinea++;
            existenErrores = false;
        }
    }

    private static void ejecutar(String source){
        Scanner scanner = new Scanner(source);
        scanner.setLinea(contadorLinea);

        //Escaneamos Tokens
        List<Token> tokens = scanner.scanTokens();
       
        if(scanner.comprobarErrores()){
            contadorLinea = scanner.getLinea();
            error(scanner.mensajeError);
        }

      
        //Analisis Sintactico
        Parser parser = new Parser(tokens);
        parser.parse();

        if(parser.getErrores()) return;

        GeneradorPostfija gpf = new GeneradorPostfija(tokens);
        List<Token> postfija = gpf.convertir();


        GeneradorAST gast = new GeneradorAST(postfija);
        Arbol programa = gast.generarAST();
        programa.recorrer(); 

    }
    /*
    El método error se puede usar desde las distintas clases
    para reportar los errores:
    Interprete.error(....);
     */
    static void error(String mensaje){
        reportar(contadorLinea, " ", mensaje);
        System.exit(65);
    }

    private static void reportar(int linea, String donde, String mensaje){
        System.err.println(
                "[linea " + linea + "] Error " + donde + ": " + mensaje
        );
        existenErrores = true;
    }

}
