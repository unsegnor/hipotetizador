/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Comparator;

/**
 * Compara las reglas en una lista para ordenarlas por importancia
 * @author Victor
 */
public class ComparadorDeReglas implements Comparator<Regla> {

    @Override
    public int compare(Regla t, Regla t1) {
        
        int respuesta = 0;
        

        //Que sean reglas pequeñas
        if(respuesta ==0){
            respuesta = (t.getAntecedente().getElementos().size()+t.getConsecuente().getElementos().size() 
                    - (t1.getAntecedente().getElementos().size()+t1.getConsecuente().getElementos().size()));
        }
        
        //Que el impacto sea mayor para estar antes
        if(respuesta == 0){
        if(t.getImpacto() > t1.getImpacto()){
            respuesta = -1;
        }else if(t1.getImpacto()>t.getImpacto()){
            respuesta = 1;
        }else{
            respuesta = 0;
        }
        }
        
        
        /*if(respuesta ==0){
            respuesta = t.getConsecuente().getElementos().size() - t1.getConsecuente().getElementos().size();
        }
        */
        //Comparamos las estadísticas de las reglas
        
        
        return respuesta;
    }
    
    
    
}
