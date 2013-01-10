/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Comparator;

/**
 *
 * @author Victor
 */
class ComparaReglasPorAntecedente implements Comparator<Regla>{

    ComparaGruposOrdenandoElementos comp = new ComparaGruposOrdenandoElementos();
    
    
    @Override
    public int compare(Regla t, Regla t1) {
        int respuesta = comp.compare(t.getAntecedente(), t1.getAntecedente());
        
        //Si los antecedentes son iguales entonces ordenamos por el consecuente
        if(respuesta == 0){
            respuesta = comp.compare(t.getConsecuente(), t1.getConsecuente());
        }
        
        return respuesta;
    }
    
}
