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
    private GrupoElementos antecedente;
    private GrupoElementos consecuente;
    
    private double soporte = 0;
    private double confianza =0;
    private double impacto = 0; //En función de la frecuencia del consecuente

    
    
    public Regla(){}
    
    public Regla(GrupoElementos antecedente, GrupoElementos consecuente){
        this.antecedente = antecedente;
        this.consecuente = consecuente;
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

    /**
     * @return the consecuente
     */
    public GrupoElementos getConsecuente() {
        return consecuente;
    }

    /**
     * @param consecuente the consecuente to set
     */
    public void setConsecuente(GrupoElementos consecuente) {
        this.consecuente = consecuente;
    }

    /**
     * @return the soporte
     */
    public double getSoporte() {
        return soporte;
    }

    /**
     * @param soporte the soporte to set
     */
    public void setSoporte(double soporte) {
        this.soporte = soporte;
    }

    /**
     * @return the confianza
     */
    public double getConfianza() {
        return confianza;
    }

    /**
     * @param confianza the confianza to set
     */
    public void setConfianza(double confianza) {
        this.confianza = confianza;
    }

    /**
     * @return the impacto
     */
    public double getImpacto() {
        return impacto;
    }

    /**
     * @param impacto the impacto to set
     */
    public void setImpacto(double impacto) {
        this.impacto = impacto;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
            if(antecedente!=null && consecuente!=null){
                sb.append(antecedente.toString()).append("->").append(consecuente.toString()).append(" S:").append(this.getSoporte()).append(" C:").append(this.getConfianza());
            }else{
                sb.append("¡¡¡NO HAY ANTECEDENTE O CONSECUENTE!!!");
            }
        
        return sb.toString();
    }
    
    //TODO para calcular el soporte de la regla será el soporte del grupo... ya viene calculado.
    
    /**
     * Utiliza los soportes de su antecedente y consecuente para calcular la confianza de la regla.
     */
    public void calcular_estadisticas(){
        if(antecedente!=null && consecuente!=null){
            //Calculamos la confianza como
            this.setConfianza((float)this.getSoporte()/(float)antecedente.getSoporte());
            
            //TODO Calculamos las demás estadísticas interesantes...
            
            
        }else{
            System.err.append("Error: intentando calcular estadísticas de una regla sin antecedente o consecuente");
        }
    }
}
