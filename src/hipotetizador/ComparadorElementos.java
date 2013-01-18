/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Comparator;

/**
 * compara elementos para ordenarlos
 * @author Victor
 */
public class ComparadorElementos implements Comparator<Elemento> {

    @Override
    public int compare(Elemento t, Elemento t1) {
        int respuesta = t.getEntrada() - t1.getEntrada(); //Primero comprobamos la entrada
        
            if(respuesta == 0){
                respuesta = t.getSubindice() - t1.getSubindice(); //Si empatan, desempatamos con el subíndice
                
                if(respuesta == 0){
                    respuesta = (t.isVerdadero()?1:0) - (t1.isVerdadero()?1:0); //Si empatan desempatamos por si está activado o no
                    //Si empatan después de esto es que son iguales y respuesta debe ser 0
                }
            }
        
        return respuesta;
    }
    
}
