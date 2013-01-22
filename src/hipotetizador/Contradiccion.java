/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;

/**
 * Es un par de reglas que tienen antecedentes iguales y consecuentes contradictorios.
 * @author Victor
 */
public class Contradiccion extends CasoDeEstudio {
    
    private GrupoElementos antecedente = new GrupoElementos();
    
    private ArrayList<Regla> reglas = new ArrayList<>();
    
    private float total_confianza = 0;

    /**
     * @return the reglas
     */
    public ArrayList<Regla> getReglas() {
        return reglas;
    }

    /**
     * @param reglas the reglas to set
     */
    public void setReglas(ArrayList<Regla> reglas) {
        this.reglas = reglas;
    }

    /**
     * @return the total_confianza
     */
    public float getTotal_confianza() {
        return total_confianza;
    }

    /**
     * @param total_confianza the total_confianza to set
     */
    public void setTotal_confianza(float total_confianza) {
        this.total_confianza = total_confianza;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for(Regla r:this.reglas){
            sb.append(r.toString()).append('\n');
        }
        
        sb.append("Confianza total: ").append(this.total_confianza);
        
        
        return sb.toString();
    }

    /**
     * @return the antecedente
     */
    public GrupoElementos getAntecedente() {
        return antecedente;
    }

    /**
     * @param antecedente the antecedente to set
     */
    public void setAntecedente(GrupoElementos antecedente) {
        this.antecedente = antecedente;
    }
    
}
