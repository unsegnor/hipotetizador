/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 *
 * @author Víctor
 */
public class Par<T, K> {
    
    private T primero;
    private K segundo;

    
    public Par(T a, K b){
        
        primero = a;
        segundo = b;
        
    }
    
    /**
     * @return the primero
     */
    public T getPrimero() {
        return primero;
    }

    /**
     * @param primero the primero to set
     */
    public void setPrimero(T primero) {
        this.primero = primero;
    }

    /**
     * @return the segundo
     */
    public K getSegundo() {
        return segundo;
    }

    /**
     * @param segundo the segundo to set
     */
    public void setSegundo(K segundo) {
        this.segundo = segundo;
    }
    
}
