/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.Arrays;

/**
 * 
 * @author Victor
 */
class Sensor<T> implements Legible {

    T puntero;
    
    private int precision;
    
    Sensor(T puntero_a_valor, int precision) {
        puntero = puntero_a_valor;
        this.precision = precision;
    }

    @Override
    public int[] leer() {
        //Generamos el vector de enteros con el tamaño adecuado
        int[] respuesta = null;
        
        if(puntero ==null){
            //Si es null rellenamos el vector con -1
            respuesta = new int[getPrecision()];
            Arrays.fill(respuesta, -1);
        }else{
            //sino rellenamos el vector con la representación binaria del valor del puntero y la precisión establecida
            if(puntero.getClass().equals(Integer.class)){
                respuesta = toBinary((Integer)puntero, precision);
            }else if(puntero.getClass().equals(Boolean.class)){
                respuesta = toBinary((Boolean)puntero, precision);
            }
        }
        
        return respuesta;
    }

    /**
     * @return the precision
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    private int[] toBinary(Integer puntero, int precision) {
        int[] respuesta = new int[precision];
        
        //Lo llenamos de ceros
        Arrays.fill(respuesta, 0);
        
        String n = Integer.toBinaryString(puntero.intValue());
        
        int l = Math.min(n.length(),precision);
        
        //Convertir el entero en binario
        int p=precision-1;
        int ps = n.length()-1;
        while(l>0){
            if(n.charAt(ps)!='0'){
                respuesta[p] = 1;
            }
            p--;
            ps--;
            l--;
        }
        
        return respuesta;
    }
    
    private int[] toBinary(Boolean puntero, int precision) {
        int[] respuesta = new int[precision];
        
        //Sea cual sea la precisión la respuesta será todo ceros y al final depende del valor de la variable
        Arrays.fill(respuesta, 0);
        
        if(puntero.booleanValue()){
            respuesta[precision-1] = 1;
        }
        
        return respuesta;
    }
    
    
}
