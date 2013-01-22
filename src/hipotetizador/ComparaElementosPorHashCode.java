/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Comparator;

/**
 * Los ordena en función del hashcode
 * @author Victor
 */
class ComparaElementosPorHashCode implements Comparator<Elemento> {

    @Override
    public int compare(Elemento t, Elemento t1) {
        return t.hashCode() - t1.hashCode();
    }
    
}
