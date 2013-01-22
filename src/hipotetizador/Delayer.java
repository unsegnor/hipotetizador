/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Llama a una función con un delay
 *
 * @author Victor
 */
public class Delayer implements Runnable {

    private Llamable llamable;
    private long delay;
    Thread contador;

    /**
     * @return the llamable
     */
    public Llamable getLlamable() {
        return llamable;
    }

    /**
     * @param llamable the llamable to set
     */
    public void setLlamable(Llamable llamable) {
        this.llamable = llamable;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void llamar_con_delay(Llamable llamable, long delay) {
        setDelay(delay);
        start();
    }

    public void start() {
        //Paramos el otro contador si había
        if (contador != null) {
            contador.interrupt();
        }
        contador = new Thread();
    }

    @Override
    public void run() {
        try {
            //Dormir durante el delay y luego avisar al llamable
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(Delayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        llamable.recibir();
    }
}
