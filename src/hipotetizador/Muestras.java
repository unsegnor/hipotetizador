/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Arrays;
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
    
    public static boolean[] secuencia(int t){
        boolean muestra[] = new boolean[t];
        
        //Posición a cambiar
        int c = 0;
        
        
        
        
        return muestra;
    }
    
    public static boolean[] masuno_binario(boolean[] m){
        boolean muestra[] = Arrays.copyOf(m, m.length);
        
        //Sumar uno a la primera posición y propagar el acarreo
        
        //Posición a cambiar
        int c = 0;
        while(muestra[c]){//Mientras la posición a cambiar fuera un uno hacer
            muestra[c] = false; //Poner a cero
            c++; //Pasar a la siguiente
            if(c>=m.length){ //Si nos hemos pasado volvemos a empezar
                c = 0;
            }
        }
        //Después ponemos la que quede a 1 salvo que sea la última en cuyo caso lo ponemos todo a cero (ya se habrá puesto a cero)
            muestra[c] = true;
       
        
        
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
