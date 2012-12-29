/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Un RegistroH contiene un conjunto de datos recibidos de una sola vez. En una única lectura de los sensores. En un momento determinado.
 * @author Victor
 */
public class RegistroH {
    
    //TODO sería bueno que almacenara de qué sensor proviene cada señal?? para facilitar la interpretación de los datos.
    //también podemos consultar después al receptor cómo se traducen
    
    //Contiene tantos elementos como posibles señales diferentes de los sensores
    final int[] signals;
    
    public RegistroH(int[] lectura){
        this.signals = lectura;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for(int i:signals){
            sb.append(i).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
    
}
