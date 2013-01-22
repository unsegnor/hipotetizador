/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Comparator;

/**
 * Compara las reglas por su confianza (mayor mejor), luego por su impacto (mayor mejor) y después por su tamaño (menor mejor)
 * @author Victor
 */
public class ComparaReglasBondad implements Comparator<Regla>{

    @Override
    public int compare(Regla t, Regla t1) {
        int respuesta = 0;
        
        //Comapramos por confianza, mayor -> mejor
        respuesta = Double.compare(t1.getConfianza(), t.getConfianza());
        
        //Comparamos por cantidad de información, mayor primero
        //respuesta = Double.compare(t1.getCantidad_de_informacion(), t.getCantidad_de_informacion());
        
        
        //Si empatan entonces ordenamos por tamaño de la regla
        //Que sean reglas pequeñas
        if(respuesta ==0){
            respuesta = (t.getAntecedente().getElementos().size()+t.getConsecuente().getElementos().size() 
                    - (t1.getAntecedente().getElementos().size()+t1.getConsecuente().getElementos().size()));
        }

        
        
        //Si empatan entonces ordenamos por impacto
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
        

        
        return respuesta;
    }
    
}
