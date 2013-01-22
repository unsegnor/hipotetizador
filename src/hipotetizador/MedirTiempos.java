/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 *
 * @author Victor
 */
public class MedirTiempos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Hipo h = new Hipo();
        int thistoria = 2;
        int tventana = 2;
        float umbral_de_certeza = 0.9f;
        float umbral_de_hipotesis = 0.1f;
        float umbral_de_ruido = 0.1f;
        float umbral_de_soporte = 0.1f;
        boolean generar_subreglas = true;

        //Ejecutar hasta que el tiempo sobrepase el tiempo máximo de espera
        long maxt = 60 * 10; //De momento tiempo máximo un minuto

        long actualt = 0;

        int nentradas = 0;

        while (true) {

            nentradas++;
            
            //Generamos una historia nueva aleatoria
            boolean[][] historia = historia_aleatoria(nentradas, thistoria);
            
            long t = System.currentTimeMillis();
            
            Teoria teoria = h.elaborar_teoria(historia, nentradas, tventana, umbral_de_certeza, umbral_de_hipotesis, umbral_de_ruido, umbral_de_soporte, generar_subreglas);

            actualt = System.currentTimeMillis() - t;
            
            System.out.println(nentradas + "\t" + actualt);

        }


    }

    private static boolean[][] historia_aleatoria(int nentradas, int longitud) {
        boolean[][] respuesta = new boolean[longitud][];

        //Rellenar con muestras aleatorias
        for (int i = 0; i < longitud; i++) {
            respuesta[i] = Muestras.aleatoria(nentradas);
        }

        return respuesta;
    }
}
