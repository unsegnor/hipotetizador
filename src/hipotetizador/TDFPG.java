/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
    //VOY A DESCOMPONERLA!!
    /**
     * 
     * @param tabla_frecuencias
     * @param historia
     * @param tventana
     * @param generar_todas Si está a true indica que se generen también las subreglas
     * @return 
     */
    public ArrayList<Regla> extraer_reglas(ArrayList<InfoElemento> tabla_frecuencias, boolean[][] historia, int tventana, boolean generar_todas) {
        ArrayList<Regla> reglas = new ArrayList<>();

        ArrayList<RegistroTD> tabla = inicializar_tabla(tabla_frecuencias);

        //Obtenemos el ranking
        ArrayList<Elemento> ranking = new ArrayList<>();
        for (RegistroTD r : tabla) {
            ranking.add(r.getElemento());
        }

        D.d("Ranking de elementos");
        D.d(showarray(ranking));
        //Filtrar los nodos por el soporte mínimo o filtrarlos antes

        //Obtener las listas de elementos que vamos a tratar (en función de la ventana)
        ArrayList<ArrayList<Elemento> > listas_elementos = hacer_listas(historia, tventana, ranking);
        
        //TODO hacer que se pueda llamar al algoritmo pasándole una listas ya hechas
        
        Nodo padre = construir_arbol(listas_elementos, tabla, ranking);
        //Aquí deberíamos tener el árbol construido
        D.d("Arbol construido");
        D.d(imprimir_arbol(padre, " ", true));


        //A partir de aquí hay que empezar a formar los grupos desde abajo
        //Recorremos la tabla en orden inverso y vamos extrayendo subconjuntos a la vez que restamos uno a las frecuencias
        
        ArrayList<GrupoElementos> grupos_frecuentes = obtener_grupos_frecuentes(tabla);
 
        //Aquí deberíamos tener la lista de grupos frecuentes
        D.d("Lista de grupos frecuentes\n");
        D.d(imprime(grupos_frecuentes));

        //Aquí ya deberíamos ser capaces de generar
        //de cada grupo de elementos todas las reglas posibles con su soporte y confianza
        //primero hay que conseguir todos los subgrupos y sus soportes
        //a partir de la lista de grupos frecuentes obtenida
        //permutamos para seleccionar subgrupos e ir anotando su soporte

        //debemos obtener una lista con todos los subgrupos y su soporte (heredado del grupo)
        ArrayList<GrupoElementos> subgrupos = new ArrayList<>();
        for (GrupoElementos g : grupos_frecuentes) {
            g.ordenar(); //Ordenamos los elementos del grupo
            subgrupos.addAll(g.getSubgrupos());
        }

        //Imprimimos todos los subgrupos con sus soportes
        D.d("Lista de subgrupos \n");
        D.d(imprime(subgrupos));

        //después juntaremos los grupos idénticos y sumaremos sus soportes
        //primero convendría ordenarlos
        ComparadorDeGrupos comp = new ComparadorDeGrupos();
        Collections.sort(subgrupos, comp);

        //Imprimimos todos los subgrupos con sus soportes
        D.d("Lista de subgrupos ordenados\n");
        D.d(imprime(subgrupos));

        //después ir comprobando si uno y el siguiente son el mismo para ir reduciendo
        //y así evitar una comparación de todos con todos
        ArrayList<GrupoElementos> soporte_de_grupos = new ArrayList<>();


        /*int l = subgrupos.size();
         GrupoElementos nuevo = subgrupos.get(0);
         for(int i=0; i<l-1; i++){
         //Si eres igual que el siguiente entonces 
         }*/



        GrupoElementos nuevo = null;
        for (GrupoElementos e : subgrupos) {
            if (nuevo == null) {//Soy el primero, me asigno el turno
                nuevo = e;
            } else {
                if (comp.compare(e, nuevo) == 0) {//Si soy igual que el nuevo, me sumo
                    nuevo.setSoporte(nuevo.getSoporte() + e.getSoporte());
                } else {
                    //Si soy diferente del nuevo es que se han terminado sus iguales y comienzan los míos
                    soporte_de_grupos.add(nuevo); //Lo apunto
                    nuevo = e; //Me asigno el turno
                }
            }
        }
        //Al finalizar el que quede en nuevo hay que añadirlo también
        soporte_de_grupos.add(nuevo);

        //Aquí deberíamos tener los grupos únicos con sus respectivos soportes
        //Imprimimos todos los subgrupos con sus soportes
        D.d("Lista de subgrupos sumados\n");
        D.d(imprime(soporte_de_grupos));

        //Ahora hay que generar las reglas y calcular su confianza
        //Para cada grupo frecuente, obtenemos todos sus subgrupos
        //y probamos relaciones entre ellos, consultamos el soporte de cada grupo con soporte_de_grupos
        //así calculamos la confianza de cada posible relación
        ArrayList<Regla> reglas_totales = new ArrayList<Regla>();
        
        if(!generar_todas){
        // Esto es lo que elabora reglas sólo de los subgrupos frecuentes
        for (GrupoElementos g : grupos_frecuentes) {
            //Hacemos permutaciones de los subgrupos para generar reglas que después analizaremos
            ArrayList<Regla> reglas_de_g = this.generar_reglas(g);
            //TODO si pudiéramos reutilizar los subgrupos calculados y simplemente apuntarlos...
            reglas_totales.addAll(reglas_de_g);
        }
        }
        //QUESTION ¿Se hace así? ¿Se conforman las reglas para todos los subgrupos de un grupo frecuente? ¿O sólo B = X-A? ¿Por qué? ¿Diferencias?...
        //En lugar de eso vamos a calcular las reglas a partir de los subgrupos de los grupos frecuentes
        
        /*
        for(GrupoElementos g:grupos_frecuentes){
            //Obtenemos sus subgrupos
            ArrayList<GrupoElementos> gs = g.getLastSubgrupos(); //Obtenemos los que ya han sido calculados
            //Para cada subgrupo generamos todas las reglas posibles y las añadimos
            for(GrupoElementos sub : gs){
                //Generar las reglas
                ArrayList<Regla> reglas_de_sub =this.generar_reglas(sub);
                //Las añadimos a la lista de reglas
                reglas_totales.addAll(reglas_de_sub);
            }
        }
        */
        if(generar_todas){
        //Vamos a generar las reglas a partir de los grupos con soporte directamente
        for(GrupoElementos g:soporte_de_grupos){
            //Para cada subgrupo posible sacamos las reglas que puede haber con él
            reglas_totales.addAll(this.generar_reglas(g));
        }
        }
        //Ahora tenemos todas las reglas deducibles en reglas_totales
        //vamos a consultar el soporte de los grupos que las componen y a anotárselo
// Sí que es necesario CREO QUE ESTO YA NO ES NECESARIO PORQUE ENLAZAMOS DIRECTAMENTE LAS REGLAS CON LOS GRUPOS
        for (Regla r : reglas_totales) {
            //Buscar el antecedente en la lista de soporte_grupos
            int i_antecedente = soporte_de_grupos.indexOf(r.getAntecedente());
            int i_consecuente = soporte_de_grupos.indexOf(r.getConsecuente());
            //Apuntamos el antecedente al mismo objeto
            r.setAntecedente(soporte_de_grupos.get(i_antecedente));
            r.setConsecuente(soporte_de_grupos.get(i_consecuente));
        }

        //Imprimimos las reglas con sus subgrupos vestidos (son sus soportes)
        D.d("Reglas con soportes en los grupos");
        D.d(imprime_reglas(reglas_totales));

        //Ahora vamos a calcular la confianza de todas las reglas basada en los soportes de sus antecedentes y consecuentes
        for (Regla r : reglas_totales) {
            r.calcular_estadisticas();
        }

        //Imprimimos las reglas con sus subgrupos vestidos (son sus soportes)
        D.d("Reglas con soportes y estadísticas");
        D.d(imprime_reglas(reglas_totales));       
        
        //Devolvemos las reglas sin filtrar, ya las filtrará el cliente
        reglas = reglas_totales;
        
        return reglas;
    }

    public String imprime_reglas(ArrayList<Regla> reglas) {
        StringBuilder sb = new StringBuilder();

        for (Regla r : reglas) {
            sb.append(r.toString()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Combina todos los grupos que se le pasan en el array para obtener todas
     * las reglas posibles
     *
     * @param g
     * @return
     */
    public ArrayList<Regla> generar_reglas(GrupoElementos g) {
        ArrayList<Regla> respuesta = new ArrayList<Regla>();

        ArrayList<Elemento> elementos = g.getElementos();

        int l = elementos.size();
        boolean num[] = new boolean[l]; //El número que indica qué elementos pertenecen a la parte izquierda de la regla

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
            //TODO se puede mejorar ya que se generan reglas que podrían generarse simplemente cambiando la posición del antecedente y el consecuente
            //invirtiendo la regla

            if (!fin) {//Si aún no hemos terminado...
                //Cuando hayamos llegado a una posición en false la volvemos true
                num[c] = true;

                //Con el número que compone el vector formamos la regla
                GrupoElementos antecedente = new GrupoElementos();
                GrupoElementos consecuente = new GrupoElementos();
                for (int i = 0; i < l; i++) {//Recorremos el número bit a bit, en los que haya true se copian al antecedente
                    if (num[i]) { //Si la posición i está a true ese elemento es del antecedente
                        antecedente.getElementos().add(elementos.get(i));
                    } else {
                        consecuente.getElementos().add(elementos.get(i));
                    }
                }

                //Si cualquiera de los dos antecedente o conscuente está vacío la regla no se copia
                if (antecedente.getElementos().size() > 0 && consecuente.getElementos().size() > 0) {

                    //El soporte de la regla es el soporte del grupo
                    Regla r = new Regla(antecedente, consecuente);
                    r.setSoporte(g.getSoporte());

                    //Añadimos la nueva regla a la lista a devolver
                    respuesta.add(r);
                }

            }
        }

        return respuesta;
    }

    public String imprime(ArrayList<GrupoElementos> c) {
        StringBuilder sb = new StringBuilder();

        for (GrupoElementos e : c) {
            sb.append(e).append("\n");
        }

        return sb.toString();
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

    /*
     public String imprime(ArrayList<HashSet<Elemento>> c) {
     StringBuilder sb = new StringBuilder();

     for (HashSet<Elemento> e : c) {
     sb.append(imprime(e)).append("\n");
     }

     return sb.toString();
     }
     */
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
        D.d("Imprimiendo lista desordenada");
        D.d(this.imprimir_lista(frecuencias));


        //Ordenar la lista por frecuencias
        Collections.sort(frecuencias, new ComparadorFreq());

        //Escribir la lista
        D.d("Imprimiendo lista ordenada por soporte");
        D.d(this.imprimir_lista(frecuencias));

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

    public ArrayList<ArrayList<Elemento> > hacer_listas(boolean[][] historia, int tventana, Iterable<Elemento> ranking) {
        ArrayList<ArrayList<Elemento> > respuesta = new ArrayList<>();
        //Recorrer la historia por segunda vez construyendo el árbol
        for (int i = 0; i <= (historia.length - tventana); i++) {
            //TODO de esta forma estamos obviando los datos del final que no componen una ventana entera,
            //así que a mayor tamaño de ventana mayor diferencia habrá entre los valores de la tabla de entrada (calculada línea por línea)
            //y los valores (de soporte) del resultado de esta exploración, ya que se están tirando muestras.
            //Puedo intentar aprovechar las de aquí de algún otro modo, tirar también las que se usaban para calcular las tablas iniciales
            //o dejarlo así siendo consciente del porqué de la diferencia de soportes.


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
            D.d("Elementos que vamos a analizar");
            D.d(showarray(elementos));

            //Antes de continuar tenemos que filtrarlos para quedarnos sólo con los que aparezcan en el ranking
            //que serán aquellos que han superado el soporte mínimo
            //TODO esto hay que cambiarlo... podríamos referenciarlos al encontrarlos arriba y ya sólo utilizar su índice

            ArrayList<Elemento> filtrados = new ArrayList<>();

            for (Elemento e : ranking) {
                if (elementos.contains(e)) {
                    filtrados.add(e);
                }
            }

            //Creo que aquí están filtrados y ordenados

            //Tenemos que ordenarlos y construir el árbol

            //Ordenar de mayor a menor soporte según la tabla que antes hemos calculado
            //En la tabla ya están ordenados así que es cuestión de ir emparejando en el orden que establece


            //Recorrer la tabla de ranking y ordenar igual
            ArrayList<Elemento> ordenados = filtrados;//new ArrayList<>();

            //Mientras la lista de ordenados sea menor que la de elementos, nos faltan por ordenar
            //int rc =0; //Elemento del ranking que estamos buscando
            //while(ordenados.size()<elementos.size()){
            //}




            //Generamos el comparador con el ranking
            //CompElemList comp = new CompElemList(ranking);

            //Ordenamos la lista de elementos según el comparador
            //Collections.sort(elementos, comp);

            D.d("Elementos que vamos a analizar ordenados");
            D.d(showarray(ordenados));
            
            //Almacenar los índices para acceder más rápido a los registrosTD
            
            respuesta.add(ordenados);
            
            //Ya tenemos la lista de elementos ordenados
            //Ahora a recorrerlos en orden y construir el árbol
        }
        return respuesta;
    }

    /**
     * La tabla se modifica
     * @param listas_elementos
     * @param tabla
     * @param ranking
     * @return 
     */
    private Nodo construir_arbol(ArrayList<ArrayList<Elemento>> listas_elementos, ArrayList<RegistroTD> tabla, List<Elemento> ranking) {
        
        //Creamos el nodo raíz del árbol
        Nodo padre = new Nodo();
        
        for(ArrayList<Elemento> ordenados : listas_elementos){
            Nodo nodo_actual = padre;
            for (Elemento elem : ordenados) {
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

                    //TODO efecto colateral, estamos modificando la tabla que se le pasa además de construir el árbol
                    
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
        return padre;
    }

    private ArrayList<GrupoElementos> obtener_grupos_frecuentes(ArrayList<RegistroTD> tabla) {
        ArrayList<GrupoElementos> grupos_frecuentes = new ArrayList<>();
        
                for (int r = tabla.size() - 1; r >= 0; r--) {
            RegistroTD reg = tabla.get(r);
            Nodo atratar = reg.getNodo();

            while (atratar != null) { //Hasta que se acabe la lista
                //De quí saldrá un grupo nuevo
                GrupoElementos grupo = new GrupoElementos();
                Nodo n_anotar = atratar;

                //Nos quedamos con la frecuencia del nodo
                int freq = n_anotar.getFreq();
                //Sólo seguimos si la frecuencia del nodo es mayor que cero
                if (freq > 0) {

                    Elemento e_anotar = n_anotar.getElemento();
                    while (e_anotar != null) { //Hasta que lleguemos al nodo raíz cuyo elemento es null
                        grupo.getElementos().add(e_anotar); //Añadimos el elemento al grupo
                        grupo.setSoporte(freq); //Anotamos el soporte del grupo
                        n_anotar.setFreq(n_anotar.getFreq() - freq); //Restamos el soporte que estamos contando (la dejamos a cero)
                        n_anotar = n_anotar.getPadre(); //Nos movemos al nodo padre
                        e_anotar = n_anotar.getElemento(); //Accedemos a su elemento
                    }


                    //Aquí ya tenemos el grupo relleno
                    grupos_frecuentes.add(grupo); //Lo añadimos a la lista de grupos
                }
                atratar = atratar.getSiguiente();
            }
        }
                return grupos_frecuentes;
    }
}
