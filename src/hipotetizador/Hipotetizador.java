/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

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

        //Activamos la depuración
        D.enabled = true;
        D.level = 0;


        int entradas = 1;
        int ventana = 2;

        Hipo h = new Hipo(entradas, ventana);

        //muestras_aleatorias(h,entradas);
        //muestras_secuenciales(h,entradas);
        //cuenta_hasta(h,entradas,4);
        //inmediato(h);
        historia_inmediata(h);
    }

    public static void cuenta_hasta(Hipo h, int entradas, int num) {



        //Generar muestras aleatorias
        for (int i = 0; i < 100; i++) {

            //Muestra inicial
            boolean[] muestra = new boolean[entradas];

            if (i % num == 0) {
                muestra[0] = true;
            } else {
                muestra[0] = false;
            }

            //Imprimir muestra
            D.d(i + "-" + Muestras.imprimir_muestra(muestra));

            //Introducir muestra
            h.muestrear(muestra);
        }
    }

    public static void muestras_aleatorias(Hipo h, int entradas) {

        //Generar muestras aleatorias
        for (int i = 0; i < 100; i++) {

            //Generar muestra
            boolean muestra[] = Muestras.aleatoria(entradas);

            //Imprimir muestra
            D.d(i + "-" + Muestras.imprimir_muestra(muestra));

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
            D.d(i + "-" + Muestras.imprimir_muestra(muestra));

            //Introducir muestra
            h.muestrear(muestra);
        }
    }

    private static void inmediato(Hipo h) {
        
        boolean[][] historia = {
         {false},
         {false},
         {false},
         {true},
         {false},
         {false},
         {false},
         {true},
         {false},
         {false},
         {false},
         {true},
         {false},
         {false},
         {false},
         {true}
         };


       /* boolean[][] historia = {
            {false, false, true},
            {false, true, false},
            {true, true, true},
            {false, false, true},
            {false, true, false},
            {true, true, true},
            {false, false, true},
            {false, true, false},
            {true, true, true},
            {false, false, true},
            {false, true, false}
        };*/
        
        
        
        boolean[] muestra;

        for (int i = 0; i < historia.length; i++) {
            //Generar muestra
            muestra = historia[i];

            //Imprimir muestra
            D.d(i + "-" + Muestras.imprimir_muestra(muestra));

            //Introducir muestra
            h.muestrear(muestra);
        }
        


    }

    private static void historia_inmediata(Hipo h) {
        boolean[][] historia = {
         {false},
         {false},
         {false},
         {true},
         {false},
         {false},
         {false},
         {true},
         {false},
         {false},
         {false},
         {true},
         {false},
         {false},
         {false},
         {true}
         };

        int tventana = 2;
        float umbral_de_hipotesis = 0.2f;
        float umbral_de_certeza = 1f;
        
        
        h.sinAmbiguedad(historia[0].length, tventana, historia, umbral_de_hipotesis, umbral_de_certeza);
    }
}
