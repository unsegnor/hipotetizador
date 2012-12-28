/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Victor
 */
public class GrupoElementos {

    static ComparadorElementos comp = new ComparadorElementos();
    static ComparadorDeGrupos compG = new ComparadorDeGrupos();
    private ArrayList<GrupoElementos> _subgrupos;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Elemento e : elementos) {
            sb.append(e).append(" ");
        }
        //Eliminar el último espacio
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]:");
        sb.append(this.getSoporte());
        return sb.toString();
    }

    
    public void ordenar() {
        Collections.sort(elementos, comp);
    }
/**
 * 
 * @return Devuelve los últimos subgrupos calculados.
 */
    public ArrayList<GrupoElementos> getLastSubgrupos() {
        return _subgrupos;
    }
    
    /**
     * Siempre se calculan de nuevo los subgrupos al llamar a esta función. Si quieres los últimos calculados utiliza getLastSubgrupos()
     * @return 
     */
    public ArrayList<GrupoElementos> getSubgrupos() {

            ArrayList<GrupoElementos> subgrupos = new ArrayList<>();
            int l = elementos.size();
            boolean num[] = new boolean[l]; //El número que indica qué elementos pertenecen al conjunto

            //permutar opciones, contar en binario
            int c = 0; //Posición a cambiar
            boolean fin = false;
            while (c < l) {
                c = 0;//Inicializamos el contador de posición
                while (!fin && num[c]) {//Si la posición es un uno entonces ponemos un cero y nos movemos a la siguiente
                    num[c] = false;
                    c++;

                    //Si nos hemos pasado hemos acabado
                    if (c >= l) {
                        fin = true;
                    }

                }

                if (!fin) {//Si aún no hemos terminado...
                    //Cuando hayamos llegado a una posición en false la volvemos true
                    num[c] = true;

                    //Con el número que compone el vector formamos el subgrupo
                    GrupoElementos nuevo = new GrupoElementos();
                    for (int i = 0; i < l; i++) {//Recorremos el número bit a bit, en los que haya true se copian al subgrupo
                        if (num[i]) { //Si la posición i está a true ese elemento es del subgrupo
                            nuevo.getElementos().add(elementos.get(i));
                        }
                    }
                    //El soporte del grupo se hereda
                    nuevo.setSoporte(this.getSoporte());
                    //Añadimos el nuevo grupo a la lista a devolver
                    subgrupos.add(nuevo);

                }
            }

        return subgrupos;
    }
    
    @Override
    public boolean equals (Object obj){
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        
        GrupoElementos otro = (GrupoElementos) obj;
        
        
        
        return (compG.compare(this, otro)==0);
    }
}
