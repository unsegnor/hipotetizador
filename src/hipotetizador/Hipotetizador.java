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
        
        
        int entradas = 4;
        int ventana = 2;
        
        Hipo h = new Hipo(entradas, ventana);
        
        //Generar muestras
        for(int i=0; i<100; i++){
            
            //Generar muestra
            boolean muestra[] = Muestras.aleatoria(entradas);
            
            //Imprimir muestra
            System.out.println(i+"-"+Muestras.imprimir_muestra(muestra));
            
            //Introducir muestra
            h.muestrear(muestra);
        }
    }
}
