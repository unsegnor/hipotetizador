/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Top Down Frequent Pattern Growth
 *
 * @author Víctor
 */
public class TDFPG {

    /**
     * Contiene el Elemento, su frecuencia y el puntero al primer nodo
     */
    //ArrayList<RegistroTD> tabla = new ArrayList< >();
    //Esta función espera la lista de elementos filtrados por soporte mínimo
    public ArrayList<Regla> ejecutar(ArrayList<InfoElemento> frecuencias, boolean[][] historia, int tventana) {
        ArrayList<Regla> reglas = new ArrayList<>();

        ArrayList<RegistroTD> tabla = inicializar_tabla(frecuencias);

        //Obtenemos el ranking
        ArrayList<Elemento> ranking = new ArrayList<>();
        for (RegistroTD r : tabla) {
            ranking.add(r.getElemento());
        }

        //Creamos el nodo raíz del árbol
        Nodo padre = new Nodo();

        System.out.println("Ranking de elementos");
        System.out.println(showarray(ranking));
        //Filtrar los nodos por el soporte mínimo o filtrarlos antes


        //Recorrer la historia por segunda vez construyendo el árbol
        for (int i = 0; i <= (historia.length - tventana); i++) {
            //Fabricamos los conjuntos en función del tamaño de la ventana
            //Desde i hasta i+tventana

            //historia[i][entrada]

            //Obtener los elementos que componen el conjunto a analizar

            ArrayList<Elemento> elementos = new ArrayList<>(); //Elementos de la ventana
            for (int e = 0; e < historia[i].length; e++) {
                for (int v = 0; v < tventana && (i + v < historia.length); v++) {
                    if (historia[i + v][e]) {
                        //Si el elemento es positivo entonces lo anotamos como positivo
                        elementos.add(new Elemento(e, v, true));
                    } else {
                        //Si no es verdadero entonces lo anotamos como falso
                        elementos.add(new Elemento(e, v, false));
                    }
                }
            }
            //Aquí ya tenemos los elementos que vamos a analizar
            System.out.println("Elementos que vamos a analizar");
            System.out.println(showarray(elementos));
            //Tenemos que ordenarlos y construir el árbol

            //Ordenar de mayor a menor soporte según la tabla que antes hemos calculado
            //En la tabla ya están ordenados así que es cuestión de ir emparejando en el orden que establece


            //Recorrer la tabla de ranking y ordenar igual
            ArrayList<Elemento> ordenados = new ArrayList<>();

            //Mientras la lista de ordenados sea menor que la de elementos, nos faltan por ordenar
            //int rc =0; //Elemento del ranking que estamos buscando
            //while(ordenados.size()<elementos.size()){
            //}




            //Generamos el comparador con el ranking
            CompElemList comp = new CompElemList(ranking);

            //Ordenamos la lista de elementos según el comparador
            Collections.sort(elementos, comp);

            System.out.println("Elementos que vamos a analizar ordenados");
            System.out.println(showarray(elementos));

            //Almacenar los índices para acceder más rápido a los registrosTD
            //Ya tenemos la lista de elementos ordenados
            //Ahora a recorrerlos en orden y construir el árbol

            Nodo nodo_actual = padre;
            for (Elemento elem : elementos) {
                //Tenemos que generar una rama de este conjunto
                //Buscamos un hijo del padre que sea igual que el nodo actual

                Nodo nodo_hijo = null;
                for (Nodo h : nodo_actual.getHijos()) {
                    if (elem.equals(h.getElemento())) {
                        nodo_hijo = h;
                    }
                }


                //Si no existe lo añadimos con el nodo actual como padre y frecuencia 0 (en el constructor del nuevo nodo)
                if (nodo_hijo == null) {
                    nodo_hijo = new Nodo(elem, nodo_actual);
                    nodo_actual.getHijos().add(nodo_hijo);

                    //Cuando añadimos un nodo nuevo ampliamos la lista enlazada
                    //Añadimos el enlace a la lista del registro correspondiente de la tabla
                    //Buscamos el elemento correspondiente de la lista, usamos el ranking que es una copia para eso
                    int registro = ranking.indexOf(elem);

                    Nodo link = tabla.get(registro).getNodo();

                    //Si es el primer nodo lo añadimos directamente
                    if (link == null) {
                        tabla.get(registro).setNodo(nodo_hijo);
                    } else {
                        //Sino tenemos que buscar el último nodo
                        Nodo ultimo = link;
                        while (link != null) {
                            ultimo = link;
                            link = ultimo.getSiguiente();
                        }
                        //En este momento tenemos que link es null y por tanto ultimo es el último nodo de la lista
                        ultimo.setSiguiente(nodo_hijo);
                    }
                }

                //En cualquier caso sumamos uno a la frecuencia del hijo
                nodo_hijo.setFreq(nodo_hijo.getFreq() + 1);



                //Actualizamos el nodo actual al hijo
                nodo_actual = nodo_hijo;
            }



        }
        //Aquí deberíamos tener el árbol construido
        System.out.println("Arbol construido");
        System.out.println(imprimir_arbol(padre, " ", true));


        //A partir de aquí hay que empezar a formar los grupos desde abajo
        //Recorremos la tabla en orden inverso y vamos extrayendo subconjuntos a la vez que restamos uno a las frecuencias

        ArrayList<HashSet<Elemento>> grupos = new ArrayList<>();

        for (int r = tabla.size() - 1; r >= 0; r--) {
            RegistroTD reg = tabla.get(r);
            Nodo atratar = reg.getNodo();

            while (atratar != null) { //Hasta que se acabe la lista
                //De quí saldrá un grupo nuevo
                HashSet<Elemento> grupo = new HashSet<>();
                Nodo n_anotar = atratar;

                //Sólo seguimos si la frecuencia del nodo es mayor que cero
                if (n_anotar.getFreq() > 0) {

                    Elemento e_anotar = n_anotar.getElemento();
                    while (e_anotar != null) { //Hasta que lleguemos al nodo raíz cuyo elemento es null
                        grupo.add(e_anotar); //Añadimos el elemento al grupo
                        n_anotar.setFreq(n_anotar.getFreq() - 1); //Restamos uno a la frecuencia
                        n_anotar = n_anotar.getPadre(); //Nos movemos al nodo padre
                        e_anotar = n_anotar.getElemento(); //Accedemos a su elemento
                    }


                    //Aquí ya tenemos el grupo relleno
                    grupos.add(grupo); //Lo añadimos a la lista de grupos
                }
                atratar = atratar.getSiguiente();
            }
        }
            //Aquí deberíamos tener la lista de grupos frecuentes
            System.out.append("Lista de grupos frecuentes\n");
            System.out.println(imprime(grupos));



        return reglas;
    }

    public String imprime(HashSet<Elemento> c) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Elemento e : c) {
            sb.append(e).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public String imprime(ArrayList<HashSet<Elemento>> c) {
        StringBuilder sb = new StringBuilder();

        for (HashSet<Elemento> e : c) {
            sb.append(imprime(e)).append("\n");
        }

        return sb.toString();
    }

    public String imprimir_arbol(Nodo padre, String historia, boolean ultimo) {
        StringBuilder sb = new StringBuilder();

        //Imprimimos al padre con la historia de nuestro padre
        //El elemento del nodo


        if (ultimo) {
            int l = historia.length();
            if (l > 0) {
                sb.append(historia.substring(0, l - 1));
            }
            sb.append((char) 9562); //Símbolo que indica que es el último hijo

        } else {
            int l = historia.length();
            if (l > 0) {
                sb.append(historia.substring(0, l - 1));
            }
            sb.append((char) 9568);
        }

        if (padre.getHijos().size() > 0) {
            sb.append((char) 9574); //Símbolo que indica que tiene hijos
        } else {
            sb.append((char) 9552); //Símbolo que indica que no tiene hijos
        }

        sb.append(padre.getElemento()).append(" ").append(padre.getFreq()).append("\n");


        //Guardamos la historia que nos viene dada
        //Y creamos una historia nueva para nuestros hijos
        //String nuevahistoria = historia

        //Para cada hijo añadimos su versión impresa
        ArrayList<Nodo> hijos = padre.getHijos();
        for (int i = 0; i < hijos.size(); i++) {

            if (i == hijos.size() - 1) {
                sb.append(imprimir_arbol(hijos.get(i), historia + " ", true));
            } else {
                sb.append(imprimir_arbol(hijos.get(i), historia + (char) 9553, false));
            }
        }
        return sb.toString();
    }

    private String showarray(ArrayList<Elemento> a) {
        StringBuilder sb = new StringBuilder();
        for (Elemento e : a) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }

    //Recibir la historia o directamente el ranking, las cuentas, o una lista específica con los Elementos y las frecuencias.
    private ArrayList<RegistroTD> inicializar_tabla(ArrayList<InfoElemento> frecuencias) {

        ArrayList<RegistroTD> tabla = new ArrayList<>();

        //Escribir la lista
        System.out.println("Imprimiendo lista desordenada");
        System.out.println(this.imprimir_lista(frecuencias));


        //Ordenar la lista por frecuencias
        Collections.sort(frecuencias, new ComparadorFreq());

        //Escribir la lista
        System.out.println("Imprimiendo lista ordenada por soporte");
        System.out.println(this.imprimir_lista(frecuencias));

        //Meter a lista en la tabla
        for (InfoElemento I : frecuencias) {
            RegistroTD nuevo = new RegistroTD();
            nuevo.setElemento(I.getElemento());
            nuevo.setSoporte(I.getApariciones());
            tabla.add(nuevo);
        }

        return tabla;
    }

    private String imprimir_lista(ArrayList<InfoElemento> lista) {
        StringBuilder sb = new StringBuilder();

        for (InfoElemento p : lista) {
            sb.append(p.getElemento().toString()).append(" ").append(p.getApariciones()).append("\n");
        }

        return sb.toString();
    }
}
