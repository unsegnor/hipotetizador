/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Indica que el objeto está preparado para recibir objetos del tipo T de forma asíncrona.
 * @author Victor
 */
public interface Subscriptor<T> {
    
    public void recibir(T envio);
    
}
