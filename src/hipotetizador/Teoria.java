/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;

/**
 * Una teoría son un conjunto de reglas que explican las relaciones entre un conjunto de entradas
 * @author Victor
 */
public class Teoria {
    
    private ArrayList<Regla> certezas;

    /**
     * @return the certezas
     */
    public ArrayList<Regla> getCertezas() {
        return certezas;
    }

    /**
     * @param certezas the certezas to set
     */
    public void setCertezas(ArrayList<Regla> certezas) {
        this.certezas = certezas;
    }
    
}
