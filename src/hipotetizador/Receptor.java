/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Es el receptor del entorno. Aquí confluyen las señales de todos los sensores para que el programa las recoja una a una o con toda la historia.
 * @author Victor
 */
public class Receptor {
    
    //Historia
    private Historia historia = new Historia();
    
    //Subscriptores de lecturas
    ArrayList<Subscriptor<RegistroH>> subscriptores = new ArrayList<>();
    
    //Sensores
    ArrayList<Legible> sensores = new ArrayList<>();

    /**
     * @return la historia
     */
    public Historia getHistoria() {
        return historia;
    }
    /**
     * Reinicia la historia.
     */
    public void reiniciar_historia(){
        historia.reiniciar();
    }
    /**
     * Subscribir un objeto para recibir las lecturas cada vez que se efectúen
     * @param nuevo_subscriptor 
     */
    public void subscribir(Subscriptor<RegistroH> nuevo_subscriptor){
        subscriptores.add(nuevo_subscriptor);
    }
    
    /**
     * Cada vez que muestreamos leemos todos los sensores en orden, 
     * componemos la lectura completa, la almacenamos en la historia
     * y se la enviamos a los subscriptores
     */
    public void muestrear(){
        //Cantidad de sensores
        int cs = sensores.size();
        
        //Tamaño total
        int totalsize = 0;
        
        int[][] muestras = new int[cs][];
        //Leer todos los sensores en orden y componer una única lectura
        for(int i=0; i<cs; i++){
            int[] m = sensores.get(i).leer();
            totalsize+=m.length;
            muestras[i]=m;
        }
        
        //Juntar todas las muestras
        int[] todasjuntas = new int[totalsize];
        int indice = 0;
        for(int i=0; i<cs; i++){
            for(int j=0; j<muestras[i].length; j++){
                todasjuntas[indice] = muestras[i][j];
                indice++;
            }
        }
        
        //Montar registro con la lectura
        RegistroH r = new RegistroH(todasjuntas);
        
        //Anotar registro en la historia
        historia.registrar(r);
        
        //TODO parelelizar Informar a los subscriptores
        for(Subscriptor<RegistroH> s:subscriptores){
            s.recibir(r);
        }
        
    }

    void inscribir(Legible sensor) {
        sensores.add(sensor);
    }
    
    
    
}
