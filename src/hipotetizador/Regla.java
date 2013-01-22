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
    private double confianza = 0;
    private double impacto = 0; //En función de la frecuencia del consecuente
    private double cantidad_de_informacion = 0;

    public Regla() {
    }

    public Regla(GrupoElementos antecedente, GrupoElementos consecuente) {
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
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (antecedente != null && consecuente != null) {
            sb.append(antecedente.toString()).append("->").append(consecuente.toString())
                    .append(" S:").append(this.getSoporte())
                    .append(" C:").append(this.getConfianza())
                    .append(" I:").append(this.getImpacto())
                    .append(" CI:").append(this.getCantidad_de_informacion());
        } else {
            sb.append("¡¡¡NO HAY ANTECEDENTE O CONSECUENTE!!!");
        }

        return sb.toString();
    }

    public String toStringBonito(int tentradas, int tventana) {
        StringBuilder sb = new StringBuilder();

        if (antecedente != null && consecuente != null) {
            sb.append("--------------------------------------------------------").append('\n')
                    .append(antecedente.toStringBonito(tentradas, tventana)).append('\n')
                    .append("|||").append('\n')
                    .append("VVV").append('\n').append('\n')
                    .append(consecuente.toStringBonito(tentradas, tventana)).append('\n')
                    .append(" S:").append(this.getSoporte())
                    .append(" C:").append(this.getConfianza())
                    .append(" I:").append(this.getImpacto())
                    .append("CI: ").append(this.getCantidad_de_informacion());

        } else {
            sb.append("¡¡¡NO HAY ANTECEDENTE O CONSECUENTE!!!");
        }

        return sb.toString();
    }

    //TODO para calcular el soporte de la regla será el soporte del grupo... ya viene calculado.
    /**
     * Utiliza los soportes de su antecedente y consecuente para calcular la
     * confianza de la regla.
     */
    public void calcular_estadisticas() {
        if (antecedente != null && consecuente != null) {
            //Calculamos la confianza como
            this.setConfianza((float) this.getSoporte() / (float) antecedente.getSoporte());

            //Calculamos el impacto como el soporte total dividido entre el soporte del consecuente
            //con esta medida pretendemos dar más importancia a las reglas que implican cosas que no son normales
            this.setImpacto((float) this.getSoporte() / (float) consecuente.getSoporte());

            //Calcular la cantidad de información que nos da
            //usamos la confianza como la probabilidad de que se de la regla
            //La confianza multiplicada por el logaritmo en base 2 de la confianza
            this.setCantidad_de_informacion(1-entropia((float)this.getConfianza()));

            //TODO Calculamos las demás estadísticas interesantes...


        } else {
            System.err.append("Error: intentando calcular estadísticas de una regla sin antecedente o consecuente");
        }
    }

    public static float entropia(float p) {
        float respuesta = 0;
        if(p<1 && p>0) {
            respuesta = -p * (float) log2(p) - (1 - p) * (float) log2(1 - p);
        }
        return respuesta;
    }

    public static double log2(float num) {
        return (Math.log(num) / Math.log(2));
    }

    public double log2(double num) {
        return (Math.log(num) / Math.log(2));
    }

    /**
     * @return the cantidad_de_informacion
     */
    public double getCantidad_de_informacion() {
        return cantidad_de_informacion;
    }

    /**
     * @param cantidad_de_informacion the cantidad_de_informacion to set
     */
    public void setCantidad_de_informacion(double cantidad_de_informacion) {
        this.cantidad_de_informacion = cantidad_de_informacion;
    }
    
    
    //TODO habría que comprobar que sea la misma o equivalente lógicamente
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Regla otro = (Regla) obj;

        

        return (this.getAntecedente().equals(otro.getAntecedente()) && this.getConsecuente().equals(otro.getConsecuente()));
    }
}
