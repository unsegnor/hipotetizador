/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Información sobre el elemento, frecuencia de aparición y veces que sale
 * @author Víctor
 */
public class InfoElemento {
    
    private Elemento elemento;
    
    private Long total;
    
    private Long apariciones;
    
    public float getFrecuencia(){
        return (float)getApariciones()/(float)getTotal();
    }

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
     * @return the total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * @return the apariciones
     */
    public Long getApariciones() {
        return apariciones;
    }

    /**
     * @param apariciones the apariciones to set
     */
    public void setApariciones(Long apariciones) {
        this.apariciones = apariciones;
    }
    
}
