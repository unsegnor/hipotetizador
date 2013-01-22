/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Victor
 */
public class PrediccionProbabilistica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        D.enabled = true;
        D.level = 2;
        
        Hipo h = new Hipo();
        
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(isr);

        //Obtener input en una sola línea separado por espacios
        System.out.println("Introducir serie numérica en una sola línea separada por espacios");
        String linea = bf.readLine();

        //Separar la entrada por espacios
        String[] s_num = linea.split(" ");

        //Convertir los números en binario
        String[] s_bin = new String[s_num.length];
        //Almacenamos la longitud máxima
        int maxlength = 0;
        for (int i = 0; i < s_num.length; i++) {
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
        for (int i = 0; i < t_historia; i++) {
            //Leemos la cadena binaria correspondiente
            String c_bin = s_bin[i];
            int l = c_bin.length();
            //Vamos rellenando la historia hasta donde lleguemos
            for (int j = 0; j < l; j++) {
                historia[i][j] = c_bin.charAt((l-1)-j) == '0' ? false : true;
            }
            //Después llenamos el resto de ceros
            for (int j = l; j < nentradas; j++) {
                historia[i][j] = false;
            }
        }

        int tventana = 2;
        float umbral_de_hipotesis = 0.5f;
        float umbral_de_certeza = 1f;
        float umbral_explicabilidad = 0.8f;
        float umbral_de_ruido = 0.1f;
        float umbral_de_soporte = 0.1f;
        boolean generar_subreglas = true;

                //Mostrar la historia
        System.out.println("Historia");
        D.d(3,h.imprimir_historia(historia));
        
        //Elaboramos una teoría para la historia
        Teoria teoria = h.elaborar_teoria(historia, nentradas, tventana, umbral_de_certeza, umbral_de_hipotesis, umbral_de_ruido, umbral_de_soporte, generar_subreglas);
             
        //Imprimimos la teoría
        D.d(2,"Teoría");
        D.d(2, teoria.toString());
        
        //Intentamos rellenar una historia incompleta
        //Elaborar la historia incompleta       
        //Número de ejemplos
        int nejemplos = 2;
        //Número de casos incompletos
        int nincompletos = 30;
        
        float[][] historia_incompleta = h.elaborar_historia_futura(historia, nejemplos, nincompletos);
        
        //Mostrar la historia incompleta
        System.out.println("Historia incompleta");
        D.d(3,h.imprimir_historia(historia_incompleta));
        
        //Rellenar la historia incompleta con la teoría
        boolean normalizar = false;
        float[][] prediccion = h.rellenar_historia(historia_incompleta, teoria, normalizar);
        
        //Mostrar la predicción
        System.out.println("Predicción");
        D.d(3,h.imprimir_historia(prediccion));
        
        
        //Convertir la predicción a números
        for(int i=0; i<prediccion.length; i++){
            System.out.print(Numeros.vectorAbinario(h.normalizar(prediccion[i])));
            System.out.print(" ");
        }
    }
}
