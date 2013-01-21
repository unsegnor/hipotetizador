/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Una teoría son un conjunto de reglas que explican las relaciones entre un conjunto de entradas
 * @author Victor
 */
public class Teoria {
    
    ComparaReglasBondad comp = new ComparaReglasBondad();
    
    private ArrayList<Regla> certezas;
    private ArrayList<Regla> hipotesis;
    private ArrayList<Regla> sin_ruido;
    private ArrayList<Contradiccion> contradicciones;
    
    private ArrayList<Regla> estados;
    
    
    //Tamaño máximo de ventana utilizado
    private int maxTventana;
    
    //Número de entradas
    private int maxNentradas;
    
    //
    
    private void ordenar(){
        Collections.sort(certezas,comp);
        Collections.sort(hipotesis,comp);
        Collections.sort(sin_ruido,comp);
    }
    
    public Teoria(){
        certezas = new ArrayList<>();
        hipotesis = new ArrayList<>();
        sin_ruido = new ArrayList<>();
        contradicciones = new ArrayList<>();
    }

    /**
     * @return the certezas
     */
    public ArrayList<Regla> getCertezas() {
        return certezas;
    }

    /**
     * @param certezas the certezas to set
     */
    public void setCertezas(ArrayList<Regla> certezas) {
        this.certezas = certezas;
    }

    /**
     * @return the hipotesis
     */
    public ArrayList<Regla> getHipotesis() {
        return hipotesis;
    }

    /**
     * @param hipotesis the hipotesis to set
     */
    public void setHipotesis(ArrayList<Regla> hipotesis) {
        this.hipotesis = hipotesis;
    }

    /**
     * @return the sin_ruido
     */
    public ArrayList<Regla> getSin_ruido() {
        return sin_ruido;
    }

    /**
     * @param sin_ruido the sin_ruido to set
     */
    public void setSin_ruido(ArrayList<Regla> sin_ruido) {
        this.sin_ruido = sin_ruido;
    }

    /**
     * @return the maxTventana
     */
    public int getMaxTventana() {
        return maxTventana;
    }

    /**
     * @param maxTventana the maxTventana to set
     */
    public void setMaxTventana(int maxTventana) {
        this.maxTventana = maxTventana;
    }

    /**
     * @return the maxNentradas
     */
    public int getMaxNentradas() {
        return maxNentradas;
    }

    /**
     * @param maxNentradas the maxNentradas to set
     */
    public void setMaxNentradas(int maxNentradas) {
        this.maxNentradas = maxNentradas;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Collections.sort(sin_ruido, comp);
        for(Regla r: sin_ruido){
            sb.append(r.toStringBonito(maxNentradas, maxTventana)).append('\n');
        }
        
        return sb.toString();
    }

    /**
     * @return the contradicciones
     */
    public ArrayList<Contradiccion> getContradicciones() {
        return contradicciones;
    }

    /**
     * @param contradicciones the contradicciones to set
     */
    public void setContradicciones(ArrayList<Contradiccion> contradicciones) {
        this.contradicciones = contradicciones;
    }
    
}
