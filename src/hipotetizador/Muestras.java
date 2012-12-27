/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Random;

/**
 *
 * @author Víctor
 */
public class Muestras {
    
    static Random r = new Random();
    
    //Generar una muestra aleatoria
    public static boolean[] aleatoria(int t){
        
        boolean muestra[] = new boolean[t];
        
        for(int i=0; i<t; i++){
            muestra[i] = r.nextBoolean();
        }
        
        return muestra;
    }
    
    
        static public String imprimir_muestra(boolean[] muestra){
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<muestra.length-1; i++){
            int valor = muestra[i]?1:0;
            sb.append(valor).append(" ");
        }
        sb.append(muestra[muestra.length-1]?1:0);
        
        return sb.toString();
    }
    
}
