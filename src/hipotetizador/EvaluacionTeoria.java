/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Almacena el resultado de evaluar una teoría con una historia
 *
 * @author Víctor
 */
class EvaluacionTeoria {

    //Número de entradas
    int nentradas;
    //Número de muestras
    int nmuestras;
    //Entradas que puede explicar
    int[] aciertos_entradas;
    int[] fallos_entradas;
    //Muestras que puede explicar
    int[] aciertos_muestras;
    int[] fallos_muestras;

    //Explicabilidad
    Float explicabilidad = null;
    
    
    public void calcular_explicabilidad(){
        //Hacemos la media de los aciertos por entrada
        float media = 0;
        for(int i=0; i<nentradas; i++){
            media += aciertos_entradas[i];
        }
        
        media = media / (float)(nentradas * nmuestras);
        
        explicabilidad = media;
    }
    
    public float getExplicabilidad(){
        if(explicabilidad != null) {
            return explicabilidad;
        }else{
            return -1;
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //Informar sobre las entradas
        //sb.append("Entradas: ").append(nentradas).append('\n');

        sb.append("Entradas").append('\t');
        for (int i = 0; i < nentradas; i++) {
            sb.append(i).append('\t');
        }
        sb.append('\n');

        sb.append("Aciertos").append('\t');
        for (int i = 0; i < nentradas; i++) {
            sb.append(aciertos_entradas[i]).append('\t');
        }
        sb.append('\n');

        sb.append("Fallos  ").append('\t');
        for (int i = 0; i < nentradas; i++) {
            sb.append(fallos_entradas[i]).append('\t');
        }
        sb.append('\n');
        sb.append('\n');

        //Informar sobre las muestras      
        //sb.append("Muestras: ").append(nmuestras).append('\n');

        sb.append("Muestras").append('\t');
        for (int i = 0; i < nmuestras; i++) {
            sb.append(i).append('\t');
        }
        sb.append('\n');

        sb.append("Aciertos").append('\t');
        for (int i = 0; i < nmuestras; i++) {
            sb.append(aciertos_muestras[i]).append('\t');
        }
        sb.append('\n');

        sb.append("Fallos  ").append('\t');
        for (int i = 0; i < nmuestras; i++) {
            sb.append(fallos_muestras[i]).append('\t');
        }
        sb.append('\n');


        return sb.toString();
    }
}
