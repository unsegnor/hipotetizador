/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Compara los grupos, primero el que menos elementos tiene, si tienene los
 * mismos entonces
 *
 * Efectos colaterales, ordena los elementos de los grupos
 * 
 * @author Victor
 */
class ComparaGruposOrdenandoElementos implements Comparator<GrupoElementos> {

    ComparaElementosPorHashCode comp = new ComparaElementosPorHashCode();
    
    
    
    public ComparaGruposOrdenandoElementos() {
    }
    
    

    @Override
    public int compare(GrupoElementos t, GrupoElementos t1) {
        ArrayList<Elemento> e1 = t.getElementos();
        ArrayList<Elemento> e2 = t1.getElementos();
        //Primero ordenamos por tamaño, los más pequeños delante
        int respuesta = e1.size() - e2.size();

        
        if (respuesta == 0) {
            //Si miden lo mismo desempatamos por los elementos
            //Ordenamos los elementos del array
            Collections.sort(e1, comp);
            Collections.sort(e2, comp);
            
            //Los comparamos hasta que alguno quede por encima del otro
            int i = 0;
            while (respuesta == 0 && i < e1.size()) {
                respuesta = comp.compare(e1.get(i), e2.get(i));
                i++;
            }
        }

        return respuesta;
    }
}
