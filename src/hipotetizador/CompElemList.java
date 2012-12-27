/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Comparador de elementos por el ranking en otra lista
 * @author Víctor
 */
public class CompElemList implements Comparator<Elemento>{

    ArrayList<Elemento> ranking;
    
    public CompElemList(ArrayList<Elemento> ranking){
        this.ranking = ranking;
    }
/**
 * Compara los elementos según su posición en el ranking
 * @param o1
 * @param o2
 * @return 
 */
    @Override
    public int compare(Elemento o1, Elemento o2) {
        return ranking.indexOf(o1) - ranking.indexOf(o2);
    }
    
}
