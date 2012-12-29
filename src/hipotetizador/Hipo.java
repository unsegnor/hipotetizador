/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * NUEVA RAMA REESTRUCTURACIÓN
 * 
 * 
 * Memoria a corto plazo -> tamaño de la ventana Memoria a medio plazo -> tamaño
 * de la historia Memoria a largo plazo -> reglas
 *
 * Aprendizaje ------------- En su comportamiento normal se van recogiendo
 * muestras hasta que se llena la historia cuando la historia está llena se
 * ejecuta un algoritmo de deducción de reglas con el tamaño de ventana
 * determinado se deducen y se anotan las reglas
 *
 *
 * Experimentación ---------------- Si no hay ninguna regla que supere el umbral
 * de confianza determinado procedemos a realizar movimientos aleatorios. Si
 * existe alguna regla que supera el umbral de confianza entonces: escogemos la
 * regla con mayor confianza y menor soporte para intentar darle soporte o
 * reducir su confianza si la regla contiene en el antecedente parámetros que
 * nosotros podemos modificar directamente entonces experimentamos con ella si
 * podemos modificarlos a partir de una regla con suficiente soporte y confianza
 * y en la que podemos influir entonces podemos utilizarla para experimentar
 *
 *
 *
 * No importa tanto el tamaño de la ventana como la capacidad de deducir
 * contexto oculto El tamaño de la ventana nos permite establecer relaciones
 * "inmediatas" más separadas en el tiempo
 *
 * La primera versión debería deducir reglas con el algoritmo apriori con
 * ventana de tamaño 1 y sin deducir contexto oculto sin hebras y sin realizar
 * la comprobación en stream
 *
 * @author Víctor
 */
public class Hipo {

    int tventana = 3;
    int thistoria = 1000;//Integer.MAX_VALUE-1;
    //Memoria a corto plazo
    boolean[][] ventana;
    //Memoria a medio plazo
    boolean[][] historia;
    //Memoria a largo plazo
    ArrayList<Regla> reglas_aprendidas;
    int ph = 0; //Apunta a la primera posición libre del vector de historia
    Par<Long, Long> cuentas[][]; //Aquí contamos las palabras...
    //La primera dimensión es la entrada
    //La segunda dimensión es el subíndice de la entrada
    //El par valor contiene en el primero la cantidad de veces que aparece con valor verdadero (1)
    //en el segundo la cantidad de veces que ha aparecido esa entrada con ese subíndice
    int nentradas;
    HashMap<Integer, HashSet<HashSet<Elemento>>> itemsets;
    //Almacena los k-itemsets en la posición k

    public Hipo(int n_entradas, int t_ventana) {
        init(n_entradas, t_ventana);
    }

    private void init(int n_entradas, int t_ventana) {
        tventana = t_ventana;
        nentradas = n_entradas;

        cuentas = new Par[nentradas][tventana];
        //Inicializar cuentas
        for (int i = 0; i < nentradas; i++) {

            for (int j = 0; j < tventana; j++) {
                cuentas[i][j] = new Par<Long, Long>(0l, 0l);

            }

        }
        ventana = new boolean[nentradas][tventana];
        historia = new boolean[thistoria][nentradas];
    }

    public void muestrear(boolean[] muestra) {
        //Añadimos la muestra a la historia
        historia[ph] = muestra;

        //Contamos los unos (trues) que lleguen en cada posición
        anotar(ph, muestra);

        ph++;

        //Si la historia está llena procedemos a ejecutar el algoritmo
        if (ph >= thistoria) {
            //Reinicializamos ph
            ph = 0;

            //Ejecutamos el algoritmo para extraer las reglas
            //deducir_reglas();
            this.TopDown(0f, 0.8f);//indicamos el umbral de soporte y el de confianza, el umbral de soporte en este momento no aporta beneficio alguno...

            //Reiniciar memorias a corto y medio plazo
            reiniciar_memorias();

        }

    }

    private void reiniciar_memorias() {
        //Limpiar los k-itemsets
        //itemsets.clear();
    }

    private void anotar(int i, boolean[] muestra) {

        //Indice en el que escribir
        for (int j = 0; j < nentradas; j++) {

            for (int k = 0; k < tventana; k++) {

                //Si la entrada es mayor o igual que el registro entonces podemos tenerlo en cuenta 
                if (k <= i) {
                    //Si el valor de la entrada y el registro es verdadero entonces sumamos la frecuencia
                    if (muestra[j]) {
                        cuentas[j][k].setPrimero(cuentas[j][k].getPrimero() + 1);
                    }

                    //En cualquier caso sumamos uno a las apariciones
                    cuentas[j][k].setSegundo(cuentas[j][k].getSegundo() + 1);
                }
            }

        }

    }

    //Devuelve los k-itemsets con frecuencias superiores a la frecuencia_umbral
    private HashSet<HashSet<Elemento>> obtener_k_itemsets(int k, double frecuencia_umbral) {
        HashSet<HashSet<Elemento>> itemset = new HashSet<>();


        if (k == 1) {
            //Si k=1 entonces recorremos las cuentas y devolvemos el conjunto de entradas con frecuencia mayor que el umbral

            //Si ya existe el 1-itemset entonces se devuelve y punto
            itemset = itemsets.get(1);
            if (itemset == null) {
                for (int i = 0; i < cuentas.length; i++) {
                    for (int j = 0; j < cuentas[i].length; j++) {
                        Par<Long, Long> par = cuentas[i][j];
                        Elemento nuevo = new Elemento();
                        HashSet<Elemento> conjunto = new HashSet<Elemento>();
                        if ((float) par.getPrimero() / (float) par.getSegundo() > frecuencia_umbral) {
                            //Si la frecuencia es mayor lo añadimos al conjunto
                            nuevo.entrada = i;
                            nuevo.subindice = j;
                            nuevo.verdadero = true;
                            conjunto.add(nuevo);
                            itemset.add(conjunto);
                        } else if (((float) (par.getSegundo() - par.getPrimero()) / (float) par.getSegundo()) > frecuencia_umbral) {
                            nuevo.entrada = i;
                            nuevo.subindice = j;
                            nuevo.verdadero = false;
                            conjunto.add(nuevo);
                            itemset.add(conjunto);
                        }
                    }
                }

                //Añadir el itemset al diccionario
                itemsets.put(1, itemset);
            }
        } else {
            //Comprobamos si el kitemset ya está calculado
            itemset = itemsets.get(k);
            //Si no está lo calculamos
            if (itemset == null) {
                //Obtenemos el (k-1)-itemset
                HashSet<HashSet<Elemento>> itemsetanterior = this.obtener_k_itemsets(k - 1, frecuencia_umbral);

                //Componer el nivel k a partir del anterior
                //Componer todas las combinaciones que existen

                /*  Realizar función tal que AB+BC+AC = ABC 
                 *   AB+BC = nada
                 *   ABC+ABD+ACD+BCD = ABCD
                 *   Deben aparecer todas las permutaciones posibles del resultado para que sea válido
                 *   AB -> es posible ABC, ABD, ABE...
                 *   AE -> es más posible ABE
                 *   BE -> ahora sí es posible ABE
                 * 
                 *   Parce fácil comprobar cuales de las posibles son realmente
                 *   Cómo construir a partir de las que existen las posibles de una sola pasada.
                 * 
                 */

                //Componer ABC si existen AB, BC y AC
                //Componer ABCD si existen ABC, ABD, ACD, BCD

                //TODO: Hacer Top Down Frequent Pattern Growth


                //Desechar las que no valen


                //Lo añadimos al diccionario
                itemsets.put(k, itemset);
            }
        }
        //Recorrer la historia y obtener los kitemsets
        //Comprobar las frecuencias

        return itemset;
    }

    private void TopDown(float umbral, double umbral_de_confianza) {
        //Leer la historia y elaborar la tabla de frecuencias
        ArrayList<InfoElemento> tabla = new ArrayList<>();
        for (int i = 0; i < cuentas.length; i++) {
            for (int j = 0; j < cuentas[i].length; j++) {
                Par<Long, Long> par = cuentas[i][j];
                long apariciones = par.getPrimero();
                long total = par.getSegundo();
                long ausencias = total - apariciones;
                Elemento verdadero = new Elemento();
                //Si la frecuencia es mayor lo añadimos al conjunto
                if ((float)apariciones/(float)total > umbral) {
                    verdadero.entrada = i;
                    verdadero.subindice = j;
                    verdadero.verdadero = true;
                    InfoElemento infov = new InfoElemento();
                    infov.setElemento(verdadero);
                    infov.setApariciones(apariciones);
                    infov.setTotal(total);
                    tabla.add(infov);
                }

                if ((float)ausencias/(float)total > umbral) {
                    Elemento falso = new Elemento();
                    falso.entrada = i;
                    falso.subindice = j;
                    falso.verdadero = false;
                    InfoElemento infof = new InfoElemento();
                    infof.setElemento(falso);
                    infof.setApariciones(ausencias);
                    infof.setTotal(total);
                    tabla.add(infof);
                }
            }
        }

        //Ejecutar TopDown con la lista
        TDFPG td = new TDFPG();
        ArrayList<Regla> reglas = td.ejecutar(tabla, historia, tventana);
        ArrayList<Regla> reglas_filtradas = new ArrayList<>();
        
        //Filtramos las reglas por el umbral de confianza
        for(Regla r:reglas){
            if(r.getConfianza()>= umbral_de_confianza){
                reglas_filtradas.add(r);
            }
        }
        
        //Imprimimos las reglas filtradas
        System.out.println("Reglas filtradas por confianza >=" + umbral_de_confianza);
        System.out.println(td.imprime_reglas(reglas_filtradas));
        
        //Ordenamos las reglas por impacto
        Collections.sort(reglas_filtradas, new ComparadorDeReglas());
        
        
        //Imprimimos las reglas filtradas y ordenadas por impacto
        System.out.println("Reglas filtradas por confianza >=" + umbral_de_confianza);
        System.out.println(imprime_reglas_bonitas(reglas_filtradas,this.nentradas, this.tventana));
        
    }
    
    public String imprime_reglas_bonitas(ArrayList<Regla> reglas, int tentradas, int tventana){
                StringBuilder sb = new StringBuilder();

        for (Regla r : reglas) {
            sb.append(r.toStringBonito(tentradas, tventana)).append("\n");
        }

        return sb.toString();
    }

    private HashSet<HashSet<Elemento>> cartesiano(Set<Elemento> A, Set<Elemento> B) {
        HashSet<HashSet<Elemento>> respuesta = new HashSet<>();

        //Recorrer a
        for (Elemento a : A) {
            for (Elemento b : B) {
                HashSet<Elemento> nuevo = new HashSet<Elemento>();
                nuevo.add(a);
                nuevo.add(b);
                respuesta.add(nuevo);
            }
        }


        return respuesta;
    }

    private void deducir_reglas() {

        //Imprimir la historia.
        System.out.println("Historia hasta el momento-----------------------------");
        System.out.println(imprimir_historia());

        //Imprimir cuentas
        System.out.println("Cuentas........................................");
        System.out.println(imprimir_cuentas());

        //Obtener 1itemset
        HashSet<HashSet<Elemento>> uno_itemset = this.obtener_k_itemsets(1, 0.8);

        //Imprimir 1-itemset
        System.out.println("1-Itemset-------------------------------------------");
        System.out.println(uno_itemset);

    }

    private String imprimir_set(Set<Elemento> c) {
        StringBuilder sb = new StringBuilder();

        for (Elemento e : c) {
            sb.append(e.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    private String imprimir_historia() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < historia.length; i++) {
            boolean[] muestra = historia[i];
            sb.append(Muestras.imprimir_muestra(muestra)).append("\n");
        }


        return sb.toString();
    }

    private String imprimir_cuentas() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cuentas.length; i++) {
            for (int j = 0; j < cuentas[i].length; j++) {
                sb.append("Entrada: ").append(i).append(" ");
                sb.append("Subíndice: ").append(j).append(" ");
                sb.append("Verdadero: ").append(cuentas[i][j].getPrimero()).append(" ");
                sb.append("Apariciones: ").append(cuentas[i][j].getSegundo()).append(" ");
                sb.append("Frecuencia pos: ").append((float) cuentas[i][j].getPrimero() / (float) cuentas[i][j].getSegundo()).append(" ");
                sb.append("Frecuencia neg: ").append((float) (cuentas[i][j].getSegundo() - cuentas[i][j].getPrimero()) / (float) cuentas[i][j].getSegundo());
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public ArrayList<Regla> deducir() {
        ArrayList<Regla> reglas = new ArrayList<Regla>();

        //Recorrer la lista

        return reglas;
    }

    public ArrayList<Regla> deducir(boolean[][] v) {
        ArrayList<Regla> reglas = new ArrayList<>();

        //Comprobar todas las combinaciones posibles de elementos en función de 

        return reglas;
    }
}
