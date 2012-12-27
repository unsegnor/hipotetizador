/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;

/**
 *
 * @author Victor
 */
public class GrupoElementos {
    
   private ArrayList<Elemento> elementos = new ArrayList<>();
   
   private int soporte = 0;

    /**
     * @return the elementos
     */
    public ArrayList<Elemento> getElementos() {
        return elementos;
    }

    /**
     * @param elementos the elementos to set
     */
    public void setElementos(ArrayList<Elemento> elementos) {
        this.elementos = elementos;
    }

    /**
     * @return the soporte
     */
    public int getSoporte() {
        return soporte;
    }

    /**
     * @param soporte the soporte to set
     */
    public void setSoporte(int soporte) {
        this.soporte = soporte;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
            for(Elemento e : elementos){
                sb.append(e).append(" ");
            }
            //Eliminar el último espacio
            sb.deleteCharAt(sb.length()-1);
        sb.append("] -> ");
        sb.append(this.getSoporte());
        return sb.toString();
    }
    
}
