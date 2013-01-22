/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Victor
 */
public class GrupoElementos {

    static ComparadorElementos comp = new ComparadorElementos();
    static ComparadorDeGrupos compG = new ComparadorDeGrupos();
    private ArrayList<GrupoElementos> _subgrupos;
    private ArrayList<Elemento> elementos = new ArrayList<>();
    private long soporte = 0;

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
    public long getSoporte() {
        return soporte;
    }

    /**
     * @param soporte the soporte to set
     */
    public void setSoporte(long soporte) {
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

    public String toStringBonito(int tentradas, int tventana) {
        StringBuilder sb = new StringBuilder();

        //Generamos la matriz de tventana x tentradas de enteros
        int[][] m = new int[tventana][tentradas];
        
        //La inicializamos a -1
        for(int v=0; v<tventana; v++){
            Arrays.fill(m[v], -1);
        }
        
        //Recorremos los elementos del grupo y vamos anotando unos o ceros
        for(Elemento e: elementos){           
                m[e.getSubindice()][e.getEntrada()] = e.isVerdadero()?1:0;
        }
        
        //Generamos la matriz de tventana x tentradas en un string
        for (int v = 0; v < tventana; v++) {
            for (int e = 0; e < tentradas; e++) {
                //Añadimos a la cadena caracteres en función del valor de la matriz
                //-1 -> X : quiere decir que no importa el valor que tenga
                //1 -> 1
                //0 -> 0
                sb.append(m[v][e]==-1?'X':(m[v][e]==0?'0':'1'));    
            }
            //Al finalizar una línea de ventana se introduce un intro
            sb.append('\n');
        }
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
     * Siempre se calculan de nuevo los subgrupos al llamar a esta función. Si
     * quieres los últimos calculados utiliza getLastSubgrupos()
     *
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

        //Almacenamos el puntero al resultado por si se vuelve a pedir inmediatamente
        _subgrupos = subgrupos;
        
        return subgrupos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        GrupoElementos otro = (GrupoElementos) obj;



        return (compG.compare(this, otro) == 0);
    }
    /**
     * Indica si el Grupo this está contenido en el grupo e
     * @param e
     * @return 
     */
    boolean contenido_en(GrupoElementos e) {
        return contenido_en(this,e);
    }
    /**
     * Indica si el grupo A está contenido en B. Si B contiene a A
     * @param a
     * @param b
     * @return 
     */
    boolean contenido_en(GrupoElementos a, GrupoElementos b){
        //Si B contiene a A tendrá todos los elementos de A
        //Recorremos entonces los elementos de A si falta alguno es que no lo contiene
        //Primero ordenamos los dos grupos
        
        a.ordenar();
        b.ordenar();
        
        ArrayList<Elemento> Aordenado = a.getElementos();
        ArrayList<Elemento> Bordenado = b.getElementos();
        
        
        boolean respuesta = true;
        int Bi=0;
        int lA = Aordenado.size();
        int lB = Bordenado.size();
        
        for(int Ai=0; Ai<lA && Bi<lB; Ai++){
            //Para cada elemento de A buscamos su igual en B
            //hacer hasta que se encuentre el elemento igual en B o se acabe B
            
            while(Bi<lB && !Aordenado.get(Ai).equals(Bordenado.get(Bi))){
                //Si hemos llegado al final de B aquí es que el último no era igual y por lo tanto algún elemento de A no se encuentra en B
                if(Bi==lB-1){
                    respuesta = false;
                }
                Bi++;
            }
        }
        return respuesta;
    }
    /**
     * Inddica si este grupo contradice al otro
     * @param nuevo
     * @return 
     */
    boolean contradice(GrupoElementos otro) {
        return se_contradicen(this,otro);
    }
    
    /**
     * Indica si dos grupos de elementos se contradicen
     * @param a
     * @param b
     * @return 
     */
    boolean se_contradicen(GrupoElementos a, GrupoElementos b){
        //De entrada no podemos demostrar que se contradigan
        boolean respuesta = false;
        
        //Para comprobar si se contradicen iremos anotando lo que dicen en un hashMap
        //HashMap<Integer, Boolean> diccionario = new HashMap<>(); 
        
        //Anotamos los elementos del primero
        //...
        
        //Podríamos juntarlos en un grupo, ordenarlos y buscar entre los adyacentes comparamos el ID
        //TODO peligro, dependemos de que no varíe la forma de calcular el ID..bueno utilizaremos num()
        
        //Juntamos todos los elementos en un array
        ArrayList<Elemento> todos = new ArrayList<>(a.getElementos());
        todos.addAll(b.getElementos());
        
        //Ordenamos el array según el ID
        ComparaElementosPorID comparador_de_elementos_por_ID = new ComparaElementosPorID();
        Collections.sort(todos, comparador_de_elementos_por_ID);
        
        //Vamos recorriéndolo y si hay dos elementos que sólo se diferencien en una unidad es que se contradicen
        //ya que la última cifra del ID sólo puede ser 1 o 0 en función de si es verdadero o falso
        //si tenemos el 300 y el 301 querrá decir que la entrada 3 subíndice 0 tiene el valor 0 y 1 a la vez -> contradicción
        for(int i=0; i<todos.size()-1 && !respuesta; i++){
            Elemento uno = todos.get(i);
            Elemento dos = todos.get(+1);
            
            int ID1 = uno.getID();
            int ID2 = dos.getID();
            
            int diferencia = Math.abs(ID2-ID1);
            
            //Si la dierencia es 1 entonces se contradicen
            if(diferencia == 1){
                respuesta = true;
            }
        }
        
        
        return respuesta;
    }
}
