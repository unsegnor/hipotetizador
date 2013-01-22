/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Mide tiempo
 * @author Victor
 */
public class Cronometro {
    
    long valor_comienzo = 0;
    
    public Cronometro(){
        
    }
    
    public void Start(){
        valor_comienzo = System.currentTimeMillis();
    }
    
    public long getValor(){
        return System.currentTimeMillis() - valor_comienzo;
    }
    
}
