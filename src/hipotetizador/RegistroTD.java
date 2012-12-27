/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Es un registro de a tabla inicial del TopDown
 * @author Víctor
 */
public class RegistroTD {
    
    //Elemento
    private Elemento elemento;
    
    //Soporte
    private Long soporte;
    
    //Enlace al primer nodo
    private Nodo nodo;

    /**
     * @return the elemento
     */
    public Elemento getElemento() {
        return elemento;
    }

    /**
     * @param elemento the elemento to set
     */
    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    /**
     * @return the soporte
     */
    public Long getSoporte() {
        return soporte;
    }

    /**
     * @param soporte the soporte to set
     */
    public void setSoporte(Long soporte) {
        this.soporte = soporte;
    }

    /**
     * @return the nodo
     */
    public Nodo getNodo() {
        return nodo;
    }

    /**
     * @param nodo the nodo to set
     */
    public void setNodo(Nodo nodo) {
        this.nodo = nodo;
    }
    
}
