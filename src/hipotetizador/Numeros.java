/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 *
 * @author Victor
 */
public class Numeros {
    
    public static int vectorAbinario(float[] num){
        int respuesta=0;
        
            //Recorrer el número e ir sumando
            for(int i=0; i<num.length; i++){
                respuesta+= num[i]*(Math.pow(2, i));
            }
        
        return respuesta;
    }
    
}
