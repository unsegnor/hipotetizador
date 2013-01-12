/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Víctor
 */
public class Hipotetizador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        //TODO probar con una serie secuencial de intervalo uniforme
        //TODO después probar con una serie secuencial de intervalo variable
        //TODO identificar entradas interesantes y filtrar el resto
        //TODO identificar velocidad de muestreo interesante
        //TODO identificar tamaño de ventana interesante
        //TODO poder hacer saltar el procesamiento de la historia antes de que se llene
        //TODO no dejar tan claros los subíndices, no deben expresar un instante fijo después, sino, un rato después, difuso

        //Activamos la depuración
        D.enabled = true;
        D.level = 2;


        int entradas = 1;
        int ventana = 2;

        Hipo h = new Hipo(entradas, ventana);

        //muestras_aleatorias(h,entradas);
        //muestras_secuenciales(h,entradas);
        //cuenta_hasta(h,entradas,4);
        //inmediato(h);
        //historia_inmediata(h);
        //serie_numerica(h);
        float[][] resultados = determinar_confianza_minima(h);
        
    }
    
    public String imprimir_resultados(float[][] resultados){
        StringBuilder sb = new StringBuilder();
        
        //Imprimir los resultados
        for(float[] r:resultados){
            sb.append(r[0]).append('\t').append(r[1]).append('\t').append(r[2]).append('\n');
        }
        
        return sb.toString();
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
         {true},
         {false},
         {false},
         {true},
         {false},
         {false},
         {true},
         {false},
         {false},
         {true}
         };

        int tventana = 2;
        float umbral_de_hipotesis = 0.2f;
        float umbral_de_certeza = 1f;
        float umbral_de_explicabilidad = 0.2f;
        
        h.sinAmbiguedad(historia[0].length, tventana, historia, umbral_de_hipotesis, umbral_de_certeza, umbral_de_explicabilidad);
    }

    private static void serie_numerica(Hipo h) throws IOException {
        
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader (isr);
        
        //Obtener input en una sola línea separado por espacios
        System.out.println("Introducir serie numérica en una sola línea separada por espacios");
        String linea = bf.readLine();
        
        //Separar la entrada por espacios
        String[] s_num = linea.split(" ");
        
        //Convertir los números en binario
        String[] s_bin = new String[s_num.length];
        //Almacenamos la longitud máxima
        int maxlength = 0;
        for(int i=0; i<s_num.length; i++){
            s_bin[i] = Integer.toBinaryString(Integer.parseInt(s_num[i]));
            maxlength = Math.max(maxlength, s_bin[i].length());
        }
        
        //La longitud máxima es el número de entradas
        int nentradas = maxlength;
        //La cantidad de números es el tamaño de la historia
        int t_historia = s_bin.length;
        
        //Componemos la historia
        boolean[][] historia = new boolean[t_historia][nentradas];
        //Para cada línea de la historia
        for(int i=0; i<t_historia; i++){
            //Leemos la cadena binaria correspondiente
            String c_bin = s_bin[i];
            int l = c_bin.length();
            //Vamos rellenando la historia hasta donde lleguemos
            for(int j=0; j<l; j++){
                historia[i][j] = c_bin.charAt(j)=='0'?false:true;
            }
            //Después llenamos el resto de ceros
            for(int j=l; j<nentradas; j++){
                historia[i][j] = false;
            }
        }              

        int tventana = 2;
        float umbral_de_hipotesis = 0.2f;
        float umbral_de_certeza = 1f;
        float umbral_explicabilidad = 0.8f;
        
        
        h.sinAmbiguedad(historia[0].length, tventana, historia, umbral_de_hipotesis, umbral_de_certeza, umbral_explicabilidad);
    }

    private static float[][] determinar_confianza_minima(Hipo h) {
        
        //Ir generando una historia aleatoria, ir añadiendo muestras a la historia e ir anotando la confianza de las reglas que se generan
        //con esto hacer un gráfico para comoprobar cómo varía y qué reglas se pueden descartar.
        //Variar la historia en longitud y una vez conseguida una buena longitud también en anchura (número de entradas)
        
        ArrayList<boolean[]> historia = new ArrayList<>();
        
        
        int nentradas = 3;
        int long_ventana = 2;
        int max_h = 10;
        float umbral_de_hipotesis = 0f;
        float umbral_de_certeza = 0f; //Para meterlas todas en certezas
        float umbral_de_explicabilidad = -100f; //Para ejecutarlo una sola vez
        
        historia.add(Muestras.aleatoria(nentradas));
        
        float[][] resultados = new float[max_h][3]; //Anotamos el máximo, el mínimo y la media
        //Lo hacemos max_h veces
        for(int i=0; i<max_h; i++){
            int long_h = i+1;
            
            //Generamos una muestra más de la historia aleatoria
            boolean[] muestra = Muestras.aleatoria(nentradas);
            historia.add(muestra);
            
            //Pasamos la historia a array
            boolean[][] a_historia = historia.toArray(new boolean[historia.size()][]);
            
            //Mandamos el array a ser analizado y recibimos la teoría que lo explica
            Teoria teoria = h.sinAmbiguedad(nentradas, long_ventana, a_historia, umbral_de_hipotesis, umbral_de_certeza, umbral_de_explicabilidad);
            
            //Calculamos las medidas de las reglas que obtenemos
            //Mínimo, máximo, media
            float maxconf = 0;
            float minconf = Float.MAX_VALUE;
            float mediaconf = 0;
            
            for(Regla r:teoria.getCertezas()){
                float conf = (float) r.getConfianza();
                
                maxconf = Math.max(conf, maxconf);
                
                minconf = Math.min(conf, minconf);
                
                mediaconf += conf;
            }
            
            mediaconf = mediaconf / (float) long_h;
            
            //Anotar los resultados
            resultados[i][0] = maxconf;
            resultados[i][1] = mediaconf;
            resultados[i][2] = minconf;
        }
        
        return resultados;
    }

    private static boolean[][] addmuestra(boolean[][] historia, boolean[] muestra, int nentradas) {
        int t_historia = historia.length;
        
        //Generamos la nueva historia con una línea más
        boolean[][] respuesta = new boolean[t_historia+1][nentradas];
        
        //
        
        
        return respuesta;
    }
}
