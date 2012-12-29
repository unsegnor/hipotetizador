/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Víctor
 */
public class Hipotetizador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //TODO probar con una serie secuencial de intervalo uniforme
        //TODO después probar con una serie secuencial de intervalo variable
        //TODO identificar entradas interesantes y filtrar el resto
        //TODO identificar velocidad de muestreo interesante
        //TODO identificar tamaño de ventana interesante
        //TODO poder hacer saltar el procesamiento de la historia antes de que se llene
        //TODO no dejar tan claros los subíndices, no deben expresar un instante fijo después, sino, un rato después, difuso

        
        //Generar entorno
        Reloj reloj = new Reloj();
        
        //Generar sensores
        Sensor<Integer> s_segundos = new Sensor<>(reloj.getSegundos(),8);
        Sensor<Integer> s_minutos = new Sensor<>(reloj.getMinutos(),8);
        Sensor<Integer> s_horas = new Sensor<>(reloj.getHoras(),7);
        
        //Generar receptor (es el hub donde se juntan las señales de los sensores)
        Receptor receptor = new Receptor();
        
        //Inscribir sensores al receptor
        receptor.inscribir(s_segundos);
        receptor.inscribir(s_minutos);
        receptor.inscribir(s_horas);
        
        //Generar deductor
        Deductor deductor = new Deductor();
        
        //Subscribir deductor al receptor (para ir recibiendo la historia)
        receptor.subscribir(deductor);
        
        //Poner en funcionamiento el entorno si es necesario
        //reloj.start();
        
        //Empezar a muestrear
        for(int i=0; i<100; i++){
            reloj.actualizar();
            receptor.muestrear();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hipotetizador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Observar las reglas que el deductor va deduciendo
        
        
        
        
        
        
        //int entradas = 4;
        //int ventana = 2;

        //Hipo h = new Hipo(entradas, ventana);

        //muestras_aleatorias(h,entradas);
        //muestras_secuenciales(h,entradas);
    }

    public static void muestras_aleatorias(Hipo h, int entradas) {

        //Generar muestras aleatorias
        for (int i = 0; i < 100; i++) {

            //Generar muestra
            boolean muestra[] = Muestras.aleatoria(entradas);

            //Imprimir muestra
            System.out.println(i + "-" + Muestras.imprimir_muestra(muestra));

            //Introducir muestra
            h.muestrear(muestra);
        }
    }

    public static void muestras_secuenciales(Hipo h, int entradas) {

        //Muestra inicial
        boolean[] muestra = new boolean[entradas];

        //Inicializar la muestra
        for (int i = 0; i < muestra.length; i++) {
            muestra[i] = false;
        }

        for (int i = 0; i < 1000; i++) {

            //Generar muestra
            muestra = Muestras.masuno_binario(muestra);

            //Imprimir muestra
            System.out.println(i + "-" + Muestras.imprimir_muestra(muestra));

            //Introducir muestra
            h.muestrear(muestra);
        }
    }
}
