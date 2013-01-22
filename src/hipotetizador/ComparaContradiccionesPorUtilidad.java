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
public class ComparaContradiccionesPorUtilidad implements Comparator<Contradiccion> {

    @Override
    public int compare(Contradiccion t, Contradiccion t1) {
        int respuesta = 0;
        
        //Es más útil una contradicción pequeña
        //Comparamos cantidad de reglas que tiene (menor mejor)
        if(respuesta == 0){
            respuesta = t.getReglas().size() - t1.getReglas().size(); //TODO cambiar t1 por t
        }
        //Comparamos por tamaño del antecedente (menor mejor)
        if(respuesta == 0){
            respuesta = t.getAntecedente().getElementos().size() - t1.getAntecedente().getElementos().size();
        }
        //Comparamos por confianza total (mayor mejor)
        if(respuesta==0){
            respuesta = Float.compare(t1.getTotal_confianza(), t.getTotal_confianza());
        }
        
        return respuesta;
    }
    
}
