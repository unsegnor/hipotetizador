/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Calendar;
import java.util.Date;

/**
 * Devuelve los segundos, minutos y horas actuales
 *
 * @author Victor
 */
class Reloj {
    
    private Integer segundos = new Integer(0);
    private Integer minutos = new Integer (0);
    private Integer horas = new Integer(0);

    public void actualizar(){
        setSegundos((Integer) Calendar.getInstance().get(Calendar.SECOND));
        setMinutos((Integer) Calendar.getInstance().get(Calendar.MINUTE));
        setHoras((Integer) Calendar.getInstance().get(Calendar.HOUR));
    }

    /**
     * @return the segundos
     */
    public Integer getSegundos() {
        actualizar();
        return segundos;
    }

    /**
     * @param segundos the segundos to set
     */
    public void setSegundos(Integer segundos) {
        this.segundos = segundos;
    }

    /**
     * @return the minutos
     */
    public Integer getMinutos() {
        actualizar();
        return minutos;
    }

    /**
     * @param minutos the minutos to set
     */
    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    /**
     * @return the horas
     */
    public Integer getHoras() {
        actualizar();
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(Integer horas) {
        this.horas = horas;
    }
    

}
