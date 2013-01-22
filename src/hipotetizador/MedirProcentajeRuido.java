/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 *
 * @author Victor
 */
public class MedirProcentajeRuido {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Hipo h = new Hipo();
        int thistoria = 1000000;
        int tventana = 2;
        float umbral_de_certeza = 0.05f;
        float umbral_de_hipotesis = 0f;
        float umbral_de_ruido = 0f;
        float umbral_de_soporte = 0f;
        boolean generar_subreglas = true;


        int nentradas = 3;

            nentradas++;
            
            for(umbral_de_certeza = 0f; umbral_de_certeza<0.6f; umbral_de_certeza += 0.01f){
            
            //Generamos una historia nueva aleatoria
            boolean[][] historia = historia_aleatoria(nentradas, thistoria);
            
            //Obtenemos las reglas
            Teoria teoria = h.elaborar_teoria(historia, nentradas, tventana, umbral_de_certeza, umbral_de_hipotesis, umbral_de_ruido, umbral_de_soporte, generar_subreglas);
            
            //Contamos las reglas que están por encima y por debajo del 0.1 de confianza
            //Con los parámetros seleccionados nos devolverá en certezas todas las que están por encima del 0.1
            //y en hipótesis todas, restamos los tamaños y tendremos las reglas que están por debajo del 0.1
            int ruido = teoria.getHipotesis().size();
            int total = teoria.getSin_ruido().size();
            
            System.out.println(umbral_de_certeza + "\t" + ((float)ruido/(float)total));
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
