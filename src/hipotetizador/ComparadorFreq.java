/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Comparator;

/**
 *
 * @author Víctor
 */
public class ComparadorFreq implements Comparator<InfoElemento> {
    @Override
    public int compare(InfoElemento o1, InfoElemento o2) {
        return o2.getApariciones().compareTo(o1.getApariciones());
    }      
}
