/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * PROBANDO REESTUCTURACIÓN II
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

        /*Elementos clave que serán la entrada de muchas funciones
         * - Historia
         * - Ventana de atención: Filtro de entradas y longitud
         * - Grupos frecuentes con soporte calculado
         * - Subgrupos con soporte calculado
         *
         * 
         */
        ph++;

        //Si la historia está llena procedemos a ejecutar el algoritmo
        if (ph >= thistoria) {
            //Reinicializamos ph
            ph = 0;

            //Ejecutamos el algoritmo para extraer todas las reglas posibles
            //y determinar la ventana de atención para la nueva historia que venga
            //Ventana de atención -> entradas a tener en cuenta, muestras a tener en cuenta a la vez, 
            //frecuencia de muestreo?? (por si queremos saltarnos algunas muestras)
            //deducir_reglas();
            this.TopDown(0f, 1f, 0.2f);//indicamos el umbral de soporte y el de confianza, el umbral de soporte en este momento no aporta beneficio alguno...

            //Reiniciar memorias a corto y medio plazo
            reiniciar_memorias();

        }

    }

    private void reiniciar_memorias() {
        //Limpiar los k-itemsets
        //itemsets.clear();
    }

    public Par<Long, Long>[][] hacer_cuentas(boolean[][] historia, int l_ventana) {
        int l = historia.length;
        int n_entradas = historia[0].length;
        boolean[] muestra;
        Par<Long, Long>[][] respuesta = new Par[n_entradas][l_ventana];


        for (int i = 0; i < l; i++) {

            muestra = historia[i];

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
        return respuesta;
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

    /**
     *
     * @param umbral
     * @param entradas vector de booleanos de tamaño nentradas que indica qué
     * entradas estamos utilizando
     * @param longitud
     * @return
     */
    private ArrayList<InfoElemento> elaborar_tabla(float umbral, boolean[] entradas, int longitud) {
        //Leer la historia y elaborar la tabla de frecuencias

        //Aquí filtramos por umbral (que de momento no ha resultado ser útil)
        //y por entradas


        ArrayList<InfoElemento> tabla = new ArrayList<>();
        for (int i = 0; i < cuentas.length; i++) { //Para cada entrada
            if (entradas[i]) { //Si es una entrada que queremos miramos sus elementos
                for (int j = 0; j < cuentas[i].length; j++) {
                    Par<Long, Long> par = cuentas[i][j];
                    long apariciones = par.getPrimero();
                    long total = par.getSegundo();
                    long ausencias = total - apariciones;
                    Elemento verdadero = new Elemento();
                    //Si la frecuencia es mayor lo añadimos al conjunto
                    if ((float) apariciones / (float) total > umbral) {
                        verdadero.entrada = i;
                        verdadero.subindice = j;
                        verdadero.verdadero = true;
                        InfoElemento infov = new InfoElemento();
                        infov.setElemento(verdadero);
                        infov.setApariciones(apariciones);
                        infov.setTotal(total);
                        tabla.add(infov);
                    }

                    if ((float) ausencias / (float) total > umbral) {
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
        }
        return tabla;
    }

    //Con cada historia buscamos nuevas hipótesis y desmentimos o confirmamos las que ya teníamos.
    //no hace falta aprovechar al máximo todas las historias... tenemos muchas... podemos guardar resúmenes
    /**
     * Aquí se deberían dar todas las pasadas a la historia que creamos
     * conveniente para extraer reglas y elaborar hipótesis listas para ser
     * confirmadas o desmentidas conlas siguientes historias.
     *
     * @param umbral
     * @param umbral_de_certeza
     */
    private void TopDown(float umbral, double umbral_de_certeza, double umbral_de_hipotesis) {


        //Determinar las entradas y la longitud de la ventana
        boolean[] entradas = new boolean[nentradas];

        Arrays.fill(entradas, true);

        //TODO determinar las entradas que se van a utilizar

        int longitud_ventana = 2;

        ArrayList<InfoElemento> tabla = this.elaborar_tabla(umbral, entradas, longitud_ventana);


        //QUESTION ¿Procedimiento? Extraer reglas -> determinar hipótesis y certezas -> tratar de aumentar soporte de hipótesis para ver si la confianza aumenta o disminuye

        //Ejecutar TopDown con la lista
        TDFPG td = new TDFPG();
        ArrayList<Regla> reglas = td.extraer_reglas(tabla, historia, tventana);
        ArrayList<Regla> certezas = new ArrayList<>();
        ArrayList<Regla> hipotesis = new ArrayList<>();

        //Aquí tenemos las reglas

        //Filtramos las reglas por si unas se pueden deducir de otras
        ArrayList<Regla> reglas_reducidas = reducir_reglas(reglas);

        //Filtramos las reglas por el umbral de confianza
        for (Regla r : reglas) {
            if (r.getConfianza() >= umbral_de_certeza) {
                certezas.add(r);
            } else if (r.getConfianza() >= umbral_de_hipotesis) {
                hipotesis.add(r);
            }
        }


        //Ordenamos las reglas por impacto
        Collections.sort(certezas, new ComparadorDeReglas());
        Collections.sort(hipotesis, new ComparadorDeReglas());

        //Determinar entradas que se ven afectadas por las hipótesis
        boolean[] entradas_afectadas = calcular_entradas_afectadas(hipotesis, nentradas, true, true);



        //Imprimimos las reglas filtradas y ordenadas por impacto
        System.out.println("Certezas");
        System.out.println(imprime_reglas_bonitas(certezas, this.nentradas, this.tventana));

        //Imprimimos las reglas filtradas y ordenadas por impacto
        System.out.println("Hipotesis");
        System.out.println(imprime_reglas_bonitas(hipotesis, this.nentradas, this.tventana));


        //Determinar entradas que sabemos explicar
        boolean[] entradas_explicables = calcular_entradas_afectadas(certezas, nentradas, false, true);
        
        System.out.println("Entradas explicables");
        System.out.println(imprime_array(entradas_explicables));
        
        //Si podemos explicarlas todas deberíamos ser capaces de predecir el siguiente valor dado uno cualquiera
        //Para esto no deben existir conflictos entre reglas (habrá que comprobarlo antes)
        
        //Deducir la siguiente línea
        boolean[] siguiente = evaluar(historia[historia.length-1],certezas);
        
        System.out.println("Siguiente valor:");
        System.out.println(imprime_array(siguiente));
        
        //Deducir las siguientes líneas
        for(int linea=0; linea<10; linea++){
            siguiente = evaluar(siguiente, certezas);
                    System.out.println(linea + "-> " + imprime_array(siguiente));
        }
        
        //Lo siguiente es mejorar lo que ya hace y empezar a investigar el contexto oculto
        //ir añadiendo bits cuando dos antecedentes llevan a consecuentes disjuntos (no pueden ser las dos certezas)
        //los porcentajes de confianza deberían sumar 1.
        //cuando A->B y A->C quiere decir que hay algo que no estamos teniendo en cuenta y que afecta
        //más claro cuando A->B y a veces A->¬B
        //en este caso añadimos un bit por cada opción que se pone a 1 cuando se da cada opción
        //estos bits también se utilizarán en la historia para intentar deducir su comportamiento
        //hay momentos en los que no se sabe qué valor tienen, entre muestra y muestra que le importa... (reflexionar)
        
        //Leer estado para devolver el siguiente
        
        /*InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader (isr);
        String lineaTeclado = bf.readLine();
        */

    }

    public String imprime_array(boolean[] array){
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<array.length; i++){
            sb.append(array[i]).append(" ");
        }
        
        
        return sb.toString();
    }
            
    
    public String imprime_reglas_bonitas(ArrayList<Regla> reglas, int tentradas, int tventana) {
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

    private ArrayList<Regla> reducir_reglas(ArrayList<Regla> reglas) {
        //Ordenar las reglas por tamaño de antecedente de menor a mayor y después por tamaño del consecuente de mayor a menor
        //si A->B y A->C y A->BC la última sobra?
        //si A->B para cualquier X tal que AX->B esta regla no nos dice nada nuevo salvo que tenga mayor confianza que la primera
        //luego nos interesan reglas de antecedente pequeño y consecuente grande

        //si dos reglas tienen el mismo antecedente

        return reglas;
    }

    /**
     * Indica las entradas que se ven afectadas por las reglas que se le pasan
     * @param reglas Array de reglas
     * @param nentradas número de entradas máximo
     * @param contar_antecedente indica si se tienen en cuenta los antecedentes de las reglas
     * @param contar_consecuente indica si se tienen en cuenta los consecuentes de las reglas
     * @return devuelve un vector de booleanos indicando si la entrada de la posción i se ve afectada por alguna regla o no
     */
    private boolean[] calcular_entradas_afectadas(ArrayList<Regla> reglas, int nentradas, boolean contar_antecedente, boolean contar_consecuente) {
        boolean[] respuesta = new boolean[nentradas];

        //Llenar de falses
        Arrays.fill(respuesta, false);

        //Recorrer las reglas y anotar las entradas a las que afectan
        for (Regla r : reglas) {
            if (contar_antecedente) {
                for (Elemento a : r.getAntecedente().getElementos()) {
                    if (!respuesta[a.entrada]) {
                        respuesta[a.entrada] = true;
                    }
                }
            }
            if (contar_consecuente) {
                for (Elemento a : r.getConsecuente().getElementos()) {
                    if (!respuesta[a.entrada]) {
                        respuesta[a.entrada] = true;
                    }
                }
            }
        }

        return respuesta;
    }
/**
 * Evalúa un estado para devolve el estado siguiente según las reglas
 * TODO En realidad debería recibir y devolver una ventana
 * @param b
 * @return 
 */
    private boolean[] evaluar(boolean[] b, ArrayList<Regla> reglas) {
        boolean[] respuesta = new boolean[b.length];
        //Recorrer las reglas a ver cuál se dispara
        for(Regla r:reglas){
            boolean dispara = true;
            for(Elemento e:r.getAntecedente().getElementos()){
                //TODO cambiar esto
                //Sólo podemos utilizar la primera línea del antecedente para comparar
                
                if(e.subindice==0){
                    //Si no se cumple alguna de las entradas del antecedente -> no se dispara
                    if(b[e.entrada]!=e.verdadero){ 
                        dispara = false;
                    }
                }
            }
            //Si se dispara comprobamos el consecuente y lo escribimos en la respuesta
            if(dispara){
                for(Elemento e:r.getConsecuente().getElementos()){
                    //TODO cambiar esto
                    //Comprobamos sólo los consecuentes de la segunda línea
                    
                    if(e.subindice==1){
                        //Lo imprimimos en la respuesta ()
                        //TODO habría que comprobar si ya se había impreso otra cosa y hay conflicto entre las reglas, suponemos que no lo hay
                        respuesta[e.entrada] = e.verdadero;
                    }
                    
                }
            }
        }
        return respuesta;
    }
}
