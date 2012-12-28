/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Compara dos grupos para indicar un orden
 * @author Victor
 */
public class ComparadorDeGrupos implements Comparator<GrupoElementos> {
    
    static ComparadorElementos comp = new ComparadorElementos();

    @Override
    public int compare(GrupoElementos t, GrupoElementos t1) {
        int respuesta = t.getElementos().size() - t1.getElementos().size();
               
        ArrayList<Elemento> e1 = t.getElementos();
        ArrayList<Elemento> e2 = t1.getElementos();
        
        if(respuesta == 0){
            //Si miden lo mismo desempatamos por los elementos, suponemos que están ordenados
            int i=0;
            while(respuesta==0 && i<e1.size()){
                respuesta = comp.compare(e1.get(i), e2.get(i));
                i++;
            }
        }
        
        return respuesta;
    }
    
}
