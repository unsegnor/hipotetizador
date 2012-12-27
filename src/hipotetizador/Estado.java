/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Un estado es el conjunto de bits que componen al entrada de los sensores del bicho
 * @author Víctor
 */
public class Estado {
    
    boolean contenido[];
    
    public Estado(int tamanio){
        
        contenido = new boolean[tamanio];
        
    }
    
}
