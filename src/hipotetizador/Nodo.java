/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;

/**
 * Nodo del arbol de TopDownFPG
 * @author Víctor
 */
public class Nodo {
    
    //Elemento que se cuenta
    private Elemento elemento;
    
    //Frecuencia de aparición
    private int freq = 0;
    
    //Hijos
    private ArrayList<Nodo> hijos = new ArrayList<>();
    
    //Padre
    private Nodo padre;
    
    //Siguiente, para implementar la lista enlazada con la misma clase
    private Nodo siguiente;
    
    
    public Nodo(){}
    
    public Nodo(Elemento elemento, Nodo padre){
        this.elemento = elemento;
        this.padre = padre;
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
     * @return the freq
     */
    public int getFreq() {
        return freq;
    }

    /**
     * @param freq the freq to set
     */
    public void setFreq(int freq) {
        this.freq = freq;
    }

    /**
     * @return the hijos
     */
    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    /**
     * @param hijos the hijos to set
     */
    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }

    /**
     * @return the padre
     */
    public Nodo getPadre() {
        return padre;
    }

    /**
     * @param padre the padre to set
     */
    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    /**
     * @return the siguiente
     */
    public Nodo getSiguiente() {
        return siguiente;
    }

    /**
     * @param siguiente the siguiente to set
     */
    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
    
    
}
