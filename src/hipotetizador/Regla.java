/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * 
 * @author Víctor
 */
public class Regla {
    boolean[] antecedente;
    boolean[] consecuente;
    
    double soporte;
    double confianza;
    double impacto; //En función de la frecuencia del consecuente
}
