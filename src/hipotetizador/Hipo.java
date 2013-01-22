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

    int tventana_interno = 2;
    int thistoria_interna = 12;//Integer.MAX_VALUE-1;
    //Memoria a corto plazo
    boolean[][] ventana;
    //Memoria a medio plazo
    boolean[][] historia_interna;
    //Memoria a largo plazo
    ArrayList<Regla> reglas_aprendidas;
    int ph = 0; //Apunta a la primera posición libre del vector de historia
    Par<Long, Long> cuentas[][]; //Aquí contamos las palabras...
    //La primera dimensión es la entrada
    //La segunda dimensión es el subíndice de la entrada
    //El par valor contiene en el primero la cantidad de veces que aparece con valor verdadero (1)
    //en el segundo la cantidad de veces que ha aparecido esa entrada con ese subíndice
    int nentradas_interno;
    HashMap<Integer, HashSet<HashSet<Elemento>>> itemsets;
    //Almacena los k-itemsets en la posición k

    public Hipo() {
    }

    public Hipo(int n_entradas, int t_ventana) {
        init(n_entradas, t_ventana);
    }

    private void init(int n_entradas, int t_ventana) {
        tventana_interno = t_ventana;
        nentradas_interno = n_entradas;

        cuentas = new Par[nentradas_interno][tventana_interno];
        //Inicializar cuentas
        for (int i = 0; i < nentradas_interno; i++) {

            for (int j = 0; j < tventana_interno; j++) {
                cuentas[i][j] = new Par<Long, Long>(0l, 0l);

            }

        }
        ventana = new boolean[nentradas_interno][tventana_interno];
        historia_interna = new boolean[thistoria_interna][nentradas_interno];
    }

    public void muestrear(boolean[] muestra) {
        //Añadimos la muestra a la historia
        historia_interna[ph] = muestra;

        //Contamos los unos (trues) que lleguen en cada posición
        anotar(ph, muestra);

        /*
         * Elementos clave que serán la entrada de muchas funciones - Historia -
         * Ventana de atención: Filtro de entradas y longitud - Grupos
         * frecuentes con soporte calculado - Subgrupos con soporte calculado
         *
         *
         */
        ph++;

        //Si la historia está llena procedemos a ejecutar el algoritmo
        if (ph >= thistoria_interna) {
            //Reinicializamos ph
            ph = 0;

            //Ejecutamos el algoritmo para extraer todas las reglas posibles
            //y determinar la ventana de atención para la nueva historia que venga
            //Ventana de atención -> entradas a tener en cuenta, muestras a tener en cuenta a la vez, 
            //frecuencia de muestreo?? (por si queremos saltarnos algunas muestras)
            //deducir_reglas();
            //this.TopDown(0f, 1f, 0.2f);//indicamos el umbral de soporte y el de confianza, el umbral de soporte en este momento no aporta beneficio alguno...
            //this.sinAmbiguedad(nentradas, tventana, historia, 0f, 1f);

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
        //Inicializar cuentas
        for (int i = 0; i < n_entradas; i++) {

            for (int j = 0; j < l_ventana; j++) {
                respuesta[i][j] = new Par<Long, Long>(0l, 0l);

            }

        }


        for (int i = 0; i < l; i++) {

            muestra = historia[i];

            //Indice en el que escribir
            for (int j = 0; j < n_entradas; j++) {

                for (int k = 0; k < l_ventana; k++) {

                    //Si la entrada es mayor o igual que el registro entonces podemos tenerlo en cuenta 
                    if (k <= i) {
                        //Si el valor de la entrada y el registro es verdadero entonces sumamos la frecuencia
                        if (muestra[j]) {
                            respuesta[j][k].setPrimero(respuesta[j][k].getPrimero() + 1);
                        }

                        //En cualquier caso sumamos uno a las apariciones
                        respuesta[j][k].setSegundo(respuesta[j][k].getSegundo() + 1);
                    }
                }

            }
        }
        return respuesta;
    }

    private void anotar(int i, boolean[] muestra) {

        //Indice en el que escribir
        for (int j = 0; j < nentradas_interno; j++) {

            for (int k = 0; k < tventana_interno; k++) {

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
                            nuevo.setEntrada(i);
                            nuevo.setSubindice(j);
                            nuevo.setVerdadero(true);
                            conjunto.add(nuevo);
                            itemset.add(conjunto);
                        } else if (((float) (par.getSegundo() - par.getPrimero()) / (float) par.getSegundo()) > frecuencia_umbral) {
                            nuevo.setEntrada(i);
                            nuevo.setSubindice(j);
                            nuevo.setVerdadero(false);
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

                /*
                 * Realizar función tal que AB+BC+AC = ABC AB+BC = nada
                 * ABC+ABD+ACD+BCD = ABCD Deben aparecer todas las permutaciones
                 * posibles del resultado para que sea válido AB -> es posible
                 * ABC, ABD, ABE... AE -> es más posible ABE BE -> ahora sí es
                 * posible ABE
                 *
                 * Parce fácil comprobar cuales de las posibles son realmente
                 * Cómo construir a partir de las que existen las posibles de
                 * una sola pasada.
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
    private ArrayList<InfoElemento> elaborar_tabla(Par<Long, Long>[][] _cuentas, float umbral, boolean[] entradas, int longitud) {
        //Leer la historia y elaborar la tabla de frecuencias

        //Aquí filtramos por umbral (que de momento no ha resultado ser útil)
        //y por entradas

        ArrayList<InfoElemento> tabla = new ArrayList<>();
        for (int i = 0; i < _cuentas.length; i++) { //Para cada entrada
            if (entradas[i]) { //Si es una entrada que queremos miramos sus elementos
                for (int j = 0; j < _cuentas[i].length; j++) {
                    Par<Long, Long> par = _cuentas[i][j];
                    long apariciones = par.getPrimero();
                    long total = par.getSegundo();
                    long ausencias = total - apariciones;
                    Elemento verdadero = new Elemento();
                    //Si la frecuencia es mayor lo añadimos al conjunto
                    if ((float) apariciones / (float) total > umbral) {
                        verdadero.setEntrada(i);
                        verdadero.setSubindice(j);
                        verdadero.setVerdadero(true);
                        InfoElemento infov = new InfoElemento();
                        infov.setElemento(verdadero);
                        infov.setApariciones(apariciones);
                        infov.setTotal(total);
                        tabla.add(infov);
                    }

                    if ((float) ausencias / (float) total > umbral) {
                        Elemento falso = new Elemento();
                        falso.setEntrada(i);
                        falso.setSubindice(j);
                        falso.setVerdadero(false);
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

    /**
     * Buscará una explicación a la totalidad de las acciones que sucedan en la
     * historia, inventará todo el contexto oculto que sea necesario devolverá
     * la explicación más sencilla
     *
     * @param umbral_de_hipotesis
     * @param umbral_de_certeza
     */
    //TODO implementar algoritmo greedy para seleccionar los bits que nos inventaremos
    //TODO ir probando con los diferentes casos y ver el aumento de explicabilidad que conseguimos
    //guardar en una tabla las características de la regla y el aumento de explicabilidad conseguido a ver si vemos la regla
    public Teoria sinAmbiguedad(int nentradas, int long_ventana, boolean[][] la_historia, double umbral_de_hipotesis, double umbral_de_certeza, float umbral_explicabilidad, boolean generar_subreglas) {

        //Comaparador de regla por bondad
        ComparaReglasBondad comp = new ComparaReglasBondad();

        float umbral_ruido = 0.1f;

        //Obtener la historia

        //Imprimir la historia
        D.d(3, "Historia");
        D.d(3, this.imprimir_historia(la_historia));

        //Hacer las cuentas primera pasada
        //TODO crear clase CUENTAS
        Par<Long, Long>[][] primerScan = this.hacer_cuentas(la_historia, long_ventana);

        //Seleccionar las entradas que queremos tener en cuenta
        //TODO crear clase SELECCION_DE_ENTRADAS
        boolean[] seleccion_de_entradas = new boolean[nentradas];
        Arrays.fill(seleccion_de_entradas, true);

        //Elaborar la tabla para el TDFPG (extensible)
        //TODO crear clase TABLA
        ArrayList<InfoElemento> tabla = this.elaborar_tabla(primerScan, 0, seleccion_de_entradas, long_ventana);

        //Crear instancia de TDFPG
        TDFPG td = new TDFPG();

        //Ejecutar TDFPG y obtener las reglas
        ArrayList<Regla> todas_las_reglas = td.extraer_reglas(tabla, la_historia, long_ventana, generar_subreglas);

        //Clasificar las reglas en certezas e hipótesis
        ArrayList<Regla> certezas = new ArrayList<>();
        ArrayList<Regla> hipotesis = new ArrayList<>();

        ArrayList<Regla> sin_ruido = new ArrayList<>();

        //Filtramos las reglas por el umbral de confianza
        for (Regla r : todas_las_reglas) {
            if (r.getConfianza() >= umbral_de_certeza) {
                certezas.add(r);
            } else if (r.getConfianza() >= umbral_de_hipotesis) {
                hipotesis.add(r);
            }
            if (r.getConfianza() >= umbral_ruido) {
                sin_ruido.add(r);
            }
        }

        Teoria teoria = new Teoria();
        teoria.setCertezas(certezas);
        teoria.setHipotesis(hipotesis);
        teoria.setSin_ruido(sin_ruido);
        teoria.setMaxTventana(tventana_interno);
        teoria.setMaxNentradas(nentradas);

        //Detectar y almacenar casos ambiguos (las hipótesis) que comparten antecedente y cuya confianza suma 1
        ArrayList<Contradiccion> contradicciones = buscar_contradicciones(teoria);

        //Ordenamos los casos ambiguos por bondad
        Collections.sort(contradicciones, new ComparaContradiccionesPorUtilidad());

        //Imprimimos los casos ambiguos
        //D.d(1, "Casos ambiguos");
        //D.d(1, td.imprime_reglas(contradicciones));
        //Imprimimos las contradicciones
        D.d("Contradicciones");
        for (Contradiccion c : contradicciones) {
            D.d(2, c.toString());
        }



        //Evaluar teoría
        EvaluacionTeoria eval = evaluar_teoria(teoria, la_historia);

        eval.calcular_explicabilidad();

        //Imprimir evaluación de la teoría
        D.d(2, "Evaluación de la teoría");
        D.d(2, eval.toString());
        D.d(3, "Explicabilidad: " + eval.getExplicabilidad());

        //Dividir en dos cosas -> historia y estados_ocultos, que se juntan para evaluar
        //los estados ocultos forman parte de la Teoria



        ArrayList<Regla> contradicciones_utilizadas = new ArrayList<>();

        //Mientras haya casos ambiguos hacer (no es mientras haya casos ambiguos sino mientras la teoría no esté completa)
        //También podemos hacerlo en función de si entendemos o no el contexto, lo que sucede, lo que no sucede no nos interesa
        //Si somos capaces de deducir el siguiente estado es que entendemos lo que sucede
        //hacemos esto hasta que somos capaces de explicar el X% de los estados para el Y% de las entradas, o en resumen poder explicar el Z% de lo percibido
        //pero esto puede cambiar constantemente, la búsqueda, diversificación vs intensificación
        while (eval.getExplicabilidad() < umbral_explicabilidad) {
            //Añadir entradas de casos ambiguos a la tabla
            //Necesitamos contar las frecuencias con las que se da cada caso
            //Para eso primero necesitamos una función que nos diga si un caso se da o no en un grupoElementos a analizar, que ya contiene todos los elementos de la ventana
            //Así que necesitamos los GrupoElementos para consultar, añadir y volver a deducir reglas sin tener que recorrer la historia entera

            //TODO también podría modificar la historia y volver a ejecutarlo todo (de momento hacerlo eficaz y más adelante eficiente)

            //Modificar la historia a partir de la historia original y obtener la historia ampliada
            //Hacer una matriz más grande, una entrada más por cada caso_ambiguo
            //Rellenar los valores de los casos ambiguos en función de si se cumple o no lo que dice el caso

            //Escogemos los n casos ambiguos mejor valorados

            int n = 1;

            boolean[][] nueva_historia = la_historia;

            if (contradicciones.size() >= n) {

                ArrayList<Contradiccion> mejores_contradicciones = new ArrayList<>(contradicciones.subList(0, n));



                //Extraemos la primera regla de la primera contradicción
                Contradiccion c = contradicciones.get(0);
                Regla r = c.getReglas().get(0);
                int con = 1;
                //Mientras la contradicción seleccionada no sea repetida
                while (contradicciones_utilizadas.contains(r)) {

                    c = contradicciones.get(con);
                    r = c.getReglas().get(0);
                    con++;
                }


                //La anotamos para no volver a repetirla
                contradicciones_utilizadas.add(r);



                //La montamos en un array para no tener que cambiar la función
                ArrayList<Regla> vector_de_reglas_a_add = new ArrayList<>();

                vector_de_reglas_a_add.add(r);

                //Añadimos también la segunda regla
                //vector_de_reglas_a_add.add(c.getReglas().get(1));

                nueva_historia = extender_historia(la_historia, vector_de_reglas_a_add, tventana_interno);
            }
            int n_entradas = nueva_historia[0].length;

            //Imprimir la nueva historia
            D.d(3, "Nueva historia");
            D.d(3, this.imprimir_historia(nueva_historia));

            //Hacer las cuentas primera pasada
            //TODO crear clase CUENTAS
            primerScan = this.hacer_cuentas(nueva_historia, long_ventana);

            //Seleccionar las entradas que queremos tener en cuenta
            //TODO crear clase SELECCION_DE_ENTRADAS
            seleccion_de_entradas = new boolean[n_entradas];
            Arrays.fill(seleccion_de_entradas, true);

            //Elaborar la tabla para el TDFPG (extensible)
            //TODO crear clase TABLA
            tabla = this.elaborar_tabla(primerScan, 0, seleccion_de_entradas, long_ventana);

            //Ejecutar TDFPG y obtener las reglas
            todas_las_reglas = td.extraer_reglas(tabla, nueva_historia, long_ventana, generar_subreglas);

            //Clasificar las reglas en certezas e hipótesis
            certezas.clear();
            hipotesis.clear();

            //Filtramos las reglas por el umbral de confianza
            for (Regla r : todas_las_reglas) {
                if (r.getConfianza() >= umbral_de_certeza) {
                    certezas.add(r);
                } else if (r.getConfianza() >= umbral_de_hipotesis) {
                    hipotesis.add(r);
                }
                if (r.getConfianza() >= umbral_ruido) { //Escoger las reglas que más se acerquen a 0.5 en confianza o que más información puedan dar según la fórmula de la cantidad de información
                    sin_ruido.add(r);
                }
            }

            teoria.setCertezas(certezas);
            teoria.setHipotesis(hipotesis);
            teoria.setSin_ruido(sin_ruido);
            teoria.setMaxTventana(tventana_interno);
            teoria.setMaxNentradas(n_entradas);

            //Detectar y almacenar casos ambiguos (las hipótesis) que comparten antecedente y cuya confianza suma 1
            contradicciones = buscar_contradicciones(teoria);

            //Imprimimos los casos ambiguos
            //D.d(1, "Casos ambiguos");
            //D.d(1, td.imprime_reglas(casos_ambiguos));

            //Imprimimos las contradicciones
            D.d("Contradicciones");
            for (Contradiccion c : contradicciones) {
                D.d(2, c.toString());
            }

            //Evaluar teoría
            eval = evaluar_teoria(teoria, nueva_historia);
            eval.calcular_explicabilidad();

            //Imprimir evaluación de la teoría
            D.d(2, "Evaluación de la teoría");
            D.d(2, eval.toString());
            D.d(3, "Explicabilidad: " + eval.getExplicabilidad());

            //Volver a hacer cuentas
            la_historia = nueva_historia;
        }

        //Aquí tenemos una teoría que puede explicar
        //Calculamos los siguientes valores

        /*
         int[] siguiente = this.evaluar(la_historia[la_historia.length - 1], teoria.getCertezas());
         D.d(2, this.imprime_array(siguiente));
         for (int i = 0; i < 10; i++) {
         siguiente = this.evaluar(siguiente, teoria.getCertezas());
         D.d(2, this.imprime_array(siguiente));
         }
         * */

        //Obtenemos la última entrada
        float[] ultima_entrada = boolean2float(la_historia[la_historia.length - 1]);

        float[] entrada_incognita = entrada_incognita(ultima_entrada.length);

        //Componemos la ventana
        float[][] ventana = new float[2][];
        ventana[0] = ultima_entrada;
        ventana[1] = entrada_incognita;


        float[][] deduccion = this.evaluar_probabilidad(ventana, teoria);

        //Imprimir la deducción, la seguna parte de la ventana
        D.d(3, this.imprime_array(deduccion[1]));

        int nejemplos = 20;
        float[][] prediccion = new float[nejemplos][];

        for (int i = 0; i < nejemplos; i++) {

            ventana[0] = deduccion[1];
            ventana[1] = entrada_incognita;

            deduccion = this.evaluar_probabilidad(ventana, teoria);

            deduccion = this.normalizar(deduccion);

            prediccion[i] = deduccion[1];
            D.d(3, this.imprime_array(deduccion[1]) + " " + Numeros.vectorAbinario(deduccion[1]));
        }

        //Convertir la predicción a números, sólo la entrada original
        for (int i = 2; i < prediccion.length; i++) {
            System.out.print(Numeros.vectorAbinario(Arrays.copyOfRange(prediccion[i], 0, nentradas)));
            System.out.print(" ");
        }

        //Devolver la teoría que explica la historia

        return teoria;
    }

    private String imprime_array(float[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]).append(" ");
        }


        return sb.toString();
    }

    private float[][] boolean2float(boolean[][] matriz) {
        float[][] respuesta = new float[matriz.length][];
        for (int i = 0; i < matriz.length; i++) {
            respuesta[i] = new float[matriz[i].length];
            for (int j = 0; j < matriz[i].length; j++) {
                //Convertir boolean a float
                respuesta[i][j] = matriz[i][j] ? 1f : 0f;
            }
        }
        return respuesta;
    }

    private float[] boolean2float(boolean[] v) {
        float[] respuesta = new float[v.length];

        for (int i = 0; i < v.length; i++) {
            respuesta[i] = v[i] ? 1f : 0f;
        }


        return respuesta;
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
    private void TopDown(float umbral, double umbral_de_certeza, double umbral_de_hipotesis, boolean generar_subreglas) {


        //Determinar las entradas y la longitud de la ventana
        boolean[] entradas = new boolean[nentradas_interno];

        Arrays.fill(entradas, true);

        //TODO determinar las entradas que se van a utilizar

        int longitud_ventana = 2;

        ArrayList<InfoElemento> tabla = this.elaborar_tabla(this.cuentas, umbral, entradas, longitud_ventana);


        //QUESTION ¿Procedimiento? Extraer reglas -> determinar hipótesis y certezas -> tratar de aumentar soporte de hipótesis para ver si la confianza aumenta o disminuye

        //Ejecutar TopDown con la lista
        TDFPG td = new TDFPG();
        ArrayList<Regla> reglas = td.extraer_reglas(tabla, historia_interna, tventana_interno, generar_subreglas);
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
        boolean[] entradas_afectadas = calcular_entradas_afectadas(hipotesis, nentradas_interno, true, true);



        //Imprimimos las reglas filtradas y ordenadas por impacto
        D.d("Certezas");
        D.d(imprime_reglas_bonitas(certezas, this.nentradas_interno, this.tventana_interno));

        //Imprimimos las reglas filtradas y ordenadas por impacto
        D.d("Hipotesis");
        D.d(imprime_reglas_bonitas(hipotesis, this.nentradas_interno, this.tventana_interno));


        //Determinar entradas que sabemos explicar (en realidad es para las que tenemos reglas que las implican
        //no tiene porqué existir una regla para cada posible caso)
        //TODO Comprobar que tenemos reglas que expliquen el 1 y el 0
        boolean[] entradas_explicables = calcular_entradas_afectadas(certezas, nentradas_interno, false, true);

        D.d("Entradas explicables");
        D.d(imprime_array(entradas_explicables));

        //Si podemos explicarlas todas deberíamos ser capaces de predecir el siguiente valor dado uno cualquiera
        //Para esto no deben existir conflictos entre reglas (habrá que comprobarlo antes)

        //Deducir la siguiente línea
        int[] siguiente = evaluar(historia_interna[historia_interna.length - 1], certezas);

        D.d("Siguiente valor:");
        D.d(imprime_array(siguiente));

        //Deducir las siguientes líneas
        for (int linea = 0; linea < 10; linea++) {
            siguiente = evaluar(siguiente, certezas);
            D.d(linea + "-> " + imprime_array(siguiente));
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

        /*
         * InputStreamReader isr = new InputStreamReader(System.in);
         * BufferedReader bf = new BufferedReader (isr); String lineaTeclado =
         * bf.readLine();
         */

    }

    public String imprime_array(boolean[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]).append(" ");
        }


        return sb.toString();
    }

    public String imprime_array(int[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
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
        D.d("Historia hasta el momento-----------------------------");
        D.d(imprimir_historia(historia_interna));

        //Imprimir cuentas
        D.d("Cuentas........................................");
        D.d(imprimir_cuentas());

        //Obtener 1itemset
        HashSet<HashSet<Elemento>> uno_itemset = this.obtener_k_itemsets(1, 0.8);

        //Imprimir 1-itemset
        D.d("1-Itemset-------------------------------------------");
        //D.d(uno_itemset);

    }

    private String imprimir_set(Set<Elemento> c) {
        StringBuilder sb = new StringBuilder();

        for (Elemento e : c) {
            sb.append(e.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public String imprimir_historia(boolean[][] la_historia) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < la_historia.length; i++) {
            boolean[] muestra = la_historia[i];
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
     *
     * @param reglas Array de reglas
     * @param nentradas número de entradas máximo
     * @param contar_antecedente indica si se tienen en cuenta los antecedentes
     * de las reglas
     * @param contar_consecuente indica si se tienen en cuenta los consecuentes
     * de las reglas
     * @return devuelve un vector de booleanos indicando si la entrada de la
     * posción i se ve afectada por alguna regla o no
     */
    private boolean[] calcular_entradas_afectadas(ArrayList<Regla> reglas, int nentradas, boolean contar_antecedente, boolean contar_consecuente) {
        boolean[] respuesta = new boolean[nentradas];

        //Llenar de falses
        Arrays.fill(respuesta, false);

        //Recorrer las reglas y anotar las entradas a las que afectan
        for (Regla r : reglas) {
            if (contar_antecedente) {
                for (Elemento a : r.getAntecedente().getElementos()) {
                    if (!respuesta[a.getEntrada()]) {
                        respuesta[a.getEntrada()] = true;
                    }
                }
            }
            if (contar_consecuente) {
                for (Elemento a : r.getConsecuente().getElementos()) {
                    if (!respuesta[a.getEntrada()]) {
                        respuesta[a.getEntrada()] = true;
                    }
                }
            }
        }

        return respuesta;
    }

    private int[] evaluar(boolean[] estado, ArrayList<Regla> reglas) {
        int[] transformado = new int[estado.length];
        //Convertir el estado en vector de int
        for (int i = 0; i < estado.length; i++) {
            transformado[i] = estado[i] ? 1 : 0;
        }

        return evaluar(transformado, reglas);
    }

    /**
     * Evalúa un estado para devolve el estado siguiente según las reglas TODO
     * En realidad debería recibir y devolver una ventana
     *
     * @param b
     * @return
     */
    private int[] evaluar(int[] b, ArrayList<Regla> reglas) {
        int[] contadores = new int[b.length];
        int[] resultado = new int[b.length];

        int[] respuesta = new int[b.length];
        //Recorrer las reglas a ver cuál se dispara
        for (Regla r : reglas) {
            boolean dispara = true;
            boolean alguna = false;
            for (Elemento e : r.getAntecedente().getElementos()) {//TODO Debería salir en cuanto se sabe que no va a disparar
                //TODO cambiar esto
                //Sólo podemos utilizar la primera línea del antecedente para comparar

                if (e.getSubindice() == 0) {
                    alguna = true;
                    //Si no se cumple alguna de las entradas del antecedente -> no se dispara
                    //Si es una incógnita sí que se dispara
                    if (b[e.getEntrada()] == 0 && e.isVerdadero()
                            || b[e.getEntrada()] == 1 && !e.isVerdadero()) {
                        dispara = false;
                    }
                } else {
                    dispara = false; //No podemos dejar que se disparen las reglas que tienen en sus antecedentes variables de otros subíndices
                }
            }
            //Si hay alguna regla con subíndice 0 y dispara entonces dispara
            dispara = dispara && alguna;
            //Si se dispara comprobamos el consecuente y lo escribimos en la respuesta
            if (dispara) {
                for (Elemento e : r.getConsecuente().getElementos()) {
                    //TODO cambiar esto
                    //Comprobamos sólo los consecuentes de la segunda línea

                    if (e.getSubindice() == 1) {
                        //Lo imprimimos en la respuesta ()
                        //TODO habría que comprobar si ya se había impreso otra cosa y hay conflicto entre las reglas, suponemos que no lo hay
                        //respuesta[e.getEntrada()] = e.isVerdadero();
                        contadores[e.getEntrada()]++; //Sumamos uno a los que han contribuido al resultado
                        if (e.isVerdadero()) {
                            resultado[e.getEntrada()]++; //Si es verdadero sumamos un voto
                        } else {
                            resultado[e.getEntrada()]--; //Si es falso restamos un voto
                        }
                    }

                }
            }
        }

        //La respuesta se escribe si estaban todos de acuerdo, sino queda como incógnita
        //0->false, 1->true, 2->hay contradicciones, 3->no hay reglas

        for (int i = 0; i < b.length; i++) {
            if (contadores[i] > 0) {
                if (resultado[i] == contadores[i]) {
                    //entonces estaban todas las reglas de acuerdo y es verdadero
                    respuesta[i] = 1;
                } else if (resultado[i] == -contadores[i]) {
                    //Si el resultado es igual al negativo de los contadores es que
                    //todas las reglas estaban de acuerdo y es falso
                    respuesta[i] = 0;
                } else {
                    //En otro caso es que existen contradicciones entre las reglas
                    respuesta[i] = 2;
                    //Lo hacemos por votación
                    if (resultado[i] > 0) {
                        respuesta[i] = 1;
                    } else if (resultado[i] < 0) {
                        respuesta[i] = 0;
                    }

                }
            } else {
                //No se han disparado reglas para esta variable
                respuesta[i] = 3;
            }
        }

        return respuesta;
    }

    private float[][] evaluar_probabilidad(boolean[][] entradas, Teoria teoria) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private float[][] evaluar_probabilidad(int[][] entradas, Teoria teoria) {
        return evaluar_probabilidad(entradas, teoria);
    }

    //Rellena las entradas que faltan según las reglas
    private float[][] evaluar_probabilidad(float[][] entradas, Teoria teoria) {
        //No miramos sólo las certezas sino todo lo que no es ruido, vamos a calcular lo más probable
        //la explicación más probable y sencilla
        ArrayList<Regla> reglas = teoria.getSin_ruido();

        int nentradas = teoria.getMaxNentradas();
        int tventana = teoria.getMaxTventana();

        int[] contadores = new int[entradas.length];
        int[] resultado = new int[entradas.length];
        float[][] respuesta = new float[tventana][nentradas];

        //Copiamos lo que trae la entrada
        for (int i = 0; i < entradas.length; i++) {
            System.arraycopy(entradas[i], 0, respuesta[i], 0, nentradas);
        }


        float[][] probabilidades = new float[tventana][nentradas];
        //Recorrer las reglas a ver cuál se dispara, ordenarlas por confianza y después por impacto y después por longitud

        ArrayList<Regla> aux = new ArrayList<>(reglas);

        ComparaReglasBondad comp = new ComparaReglasBondad();

        Collections.sort(aux, comp);

        //Recorremos todas las reglas y las que se disparen sólo modifican la respuesta si no lo había hecho ya otra con menor probabilidad
        for (Regla r : aux) {
            //Saber si dispara
            boolean dispara = true;
            boolean alguno = false;

            //Probabilidad con la que se dispara
            float probabilidad_disparo = 1;

            //Dispara cuando todas las entradas coinciden con su antecedente
            ArrayList<Elemento> antecedente = r.getAntecedente().getElementos();
            for (Elemento e : antecedente) {
                float valorEntrada = entradas[e.getSubindice()][e.getEntrada()];
                //El 0.5 coincide con todas
                if ((valorEntrada > 0.5 && !e.isVerdadero())
                        || valorEntrada < 0.5 && e.isVerdadero()) {
                    //No se dispara, no coinciden
                    dispara = false;
                } else if ((valorEntrada >= 0.5 && e.isVerdadero())
                        || valorEntrada <= 0.5 && !e.isVerdadero()) {
                    //Sí coincide, alguno coincide
                    alguno = true;

                    //Si coincide multiplicamos la probabilidad de que el valor sea correcto
                    //Si es cero entonces multiplicamos por la probabilidad inversa (1-p)
                    if (e.isVerdadero()) {
                        probabilidad_disparo *= valorEntrada;
                    } else {
                        probabilidad_disparo *= (1 - valorEntrada);
                    }
                }
            }

            //Si algun antecedente coincide y ninguno contradice las entradas entonces dispara
            dispara = dispara && alguno;

            //Si dispara modificamos la respuesta en función del consecuente
            if (dispara) {
                //Obtenemos el consecuente
                ArrayList<Elemento> consecuente = r.getConsecuente().getElementos();
                for (Elemento e : consecuente) {
                    //El elemento que indica el consecuente tendrá el valor que indica el consecuente salvo que ya esté escrito por una regla anterior (y mejor en teoría)
                    //Indicaremos el valor como la probabilidad de que sea 1 en función de la confianza de la regla
                    //Comprobamos que nadie haya escrito ya la respuesta
                    float valorActual = respuesta[e.getSubindice()][e.getEntrada()];
                    if (valorActual != 1 && valorActual != 0) {
                        int valorNuevo = e.isVerdadero() ? 1 : 0;
                        //respuesta[e.getSubindice()][e.getEntrada()] = valorNuevo;
                        //Si es cero la probabilidad se invierte
                        float probabilidad = (float) r.getConfianza();
                        //Multiplicamos la probabilidad por la probabilidad de que dispare
                        //así penalizamos a las reglas que tienen muchos antecedentes inciertos
                        probabilidad *= probabilidad_disparo;
                        if (valorNuevo == 0) {
                            probabilidad = 1 - probabilidad;
                        }

                        //Sólo la aceptamos si defiende un 0 y es menor que 0.5 o si defiende un 1 y es mayor que 0.5
                        //ya que las reglas no son dobles implicaciones, es decir, que no se cumpla una regla
                        //no aporta ninguna información sobre el cumplimiento o no de otras
                        if (valorNuevo == 0 && probabilidad < 0.5
                                || valorNuevo == 1 && probabilidad > 0.5) {

                            //Si la probabilidad es mejor que la que ya había entonces se sustituye
                            //Es mejor cuanto más alejada está de 0.5
                            //Bondad de la Probabilida actual
                            float BvalorActual = Math.abs(valorActual - 0.5f); //Calculamos la distancia a 0.5 para cada una
                            float BvalorNuevo = Math.abs(probabilidad - 0.5f);
                            if (BvalorNuevo > BvalorActual) {
                                respuesta[e.getSubindice()][e.getEntrada()] = probabilidad; //Nos quedamos con la que está más alejada del desconocimiento, del 0.5
                            }
                        }
                    }
                }
            }

        }

        return respuesta;
    }

    /**
     * Devolver la forma más sencilla de explicar lo sucedido, rellena lo que
     * falta
     *
     * @param entradas
     * @param reglas
     * @return
     */
    private int[][] evaluar_probable(int[][] entradas, Teoria teoria) {
        //No miramos sólo las certezas sino todo lo que no es ruido, vamos a calcular lo más probable
        //la explicación más probable y sencilla
        ArrayList<Regla> reglas = teoria.getSin_ruido();

        int nentradas = teoria.getMaxNentradas();
        int tventana = teoria.getMaxTventana();

        int[] contadores = new int[entradas.length];
        int[] resultado = new int[entradas.length];
        int[][] respuesta = new int[tventana][nentradas];

        //Copiamos lo que trae la entrada
        for (int i = 0; i < entradas.length; i++) {
            System.arraycopy(entradas[i], 0, respuesta[i], 0, nentradas);
        }


        float[][] probabilidades = new float[tventana][nentradas];
        //Recorrer las reglas a ver cuál se dispara, ordenarlas por confianza y después por impacto y después por longitud

        ArrayList<Regla> aux = new ArrayList<>(reglas);

        ComparaReglasBondad comp = new ComparaReglasBondad();

        Collections.sort(aux, comp);

        //Recorremos todas las reglas y las que se disparen sólo modifican la respuesta si no lo había hecho ya otra
        for (Regla r : reglas) {
            //Saber si dispara
            boolean dispara = true;
            boolean alguno = false;
            //Dispara cuando todas las entradas coinciden con su antecedente
            ArrayList<Elemento> antecedente = r.getAntecedente().getElementos();
            for (Elemento e : antecedente) {
                int valorEntrada = (int) entradas[e.getSubindice()][e.getEntrada()];
                if ((valorEntrada == 1 && !e.isVerdadero())
                        || valorEntrada == 0 && e.isVerdadero()) {
                    //No se dispara, no coinciden
                    dispara = false;
                } else if ((valorEntrada == 1 && e.isVerdadero())
                        || valorEntrada == 0 && !e.isVerdadero()) {
                    //Sí coincide, alguno coincide
                    alguno = true;
                }
            }

            //Si algun antecedente coincide y ninguno contradice las entradas entonces dispara
            dispara = dispara && alguno;

            //Si dispara modificamos la respuesta en función del consecuente
            if (dispara) {
                //Obtenemos el consecuente
                ArrayList<Elemento> consecuente = r.getConsecuente().getElementos();
                for (Elemento e : consecuente) {
                    //El elemento que indica el consecuente tendrá el valor que indica el consecuente salvo que ya esté escrito por una regla anterior (y mejor en teoría)
                    //Indicaremos el valor como la probabilidad de que sea 1 en función de la confianza de la regla
                    //Comprobamos que nadie haya escrito ya la respuesta
                    int valorActual = respuesta[e.getSubindice()][e.getEntrada()];
                    if (valorActual != 1 && valorActual != 0) {
                        int valorNuevo = e.isVerdadero() ? 1 : 0;
                        respuesta[e.getSubindice()][e.getEntrada()] = valorNuevo;
                        //Si es cero la probabilidad se invierte
                        float probabilidad = (float) (1 - (float) valorNuevo) - (float) r.getConfianza();
                        probabilidades[e.getSubindice()][e.getEntrada()] = probabilidad;
                    }
                }
            }

        }

        return respuesta;
    }

    private ArrayList<Regla> extraer_casos_ambiguos(ArrayList<Regla> reglas) {
        //Inicializar respuesta
        ArrayList<Regla> respuesta = new ArrayList<>();


        //Ordenar la lista por el antecedente y consecuente
        ComparaReglasPorAntecedente comp = new ComparaReglasPorAntecedente();
        Collections.sort(reglas, comp);

        //Recorrer la lista y hacer grupos de reglas que comparten el antecedente
        //¿¿es necesario hacer grupos?? de momento no

        ComparaGruposOrdenandoElementos compG = new ComparaGruposOrdenandoElementos();

        //Guardamos la regla actual
        Regla r_nueva = null;
        GrupoElementos nuevo = null; //Nuevo es el antecedente comparte todo el grupo
        int iguales = 0;

        D.d("Reglas ordenadas por antecedente y consecuente");
        for (Regla r : reglas) {
            D.d(r.toString());
            //Extraemos el grupo de elementos del antecedente
            GrupoElementos e = r.getAntecedente();
            if (nuevo == null) {//Soy el primero, me asigno el turno
                nuevo = e;
                r_nueva = r;
                iguales = 0;
            } else {
                if (compG.compare(e, nuevo) == 0) {//Si soy igual que el nuevo
                    //Si mi consecuente no incluye el consecuente del nuevo, entonces me sumo
                    //if(!nuevo.contenido_en(e)){
                    respuesta.add(r);
                    iguales++;
                    //}else{ //Si el consecuente del que estamos analizando contiene al del modelo

                    //}
                } else {
                    //Si soy diferente del nuevo es que se han terminado sus iguales y comienzan los míos
                    if (iguales > 0) {
                        respuesta.add(r_nueva);
                    } //Lo apunto si había más de uno
                    nuevo = e; //Me asigno el turno
                    r_nueva = r;
                    iguales = 0; //Reinicio el contador de iguales
                }
            }
        }

        //Si había iguales en el último caso añadimos también el último
        if (iguales > 0) {
            respuesta.add(r_nueva);
        }

        return respuesta;
    }

    /**
     * Añade a la historia la evaluación de los casos ambiguos
     *
     * @param historia
     * @param casos_ambiguos
     * @return
     */
    private boolean[][] extender_historia(boolean[][] la_historia, ArrayList<Regla> casos_ambiguos, int t_ventana) {

        //Longitud de la historia
        int t_historia = la_historia.length;
        //Longitud de la nueva historia
        int t_nueva_historia = t_historia - (t_ventana - 1);
        //Número de entradas de la historia
        int n_entradas = la_historia[0].length;
        //Número de casos ambiguos
        int n_casos = casos_ambiguos.size();
        //Número total de entradas
        int n_entradas_total = n_entradas + n_casos;

        boolean[][] nueva_historia = new boolean[t_nueva_historia][n_entradas + n_casos];

        //Rellenar la nueva historia con la historia antigua
        for (int i = 0; i < t_nueva_historia; i++) {
            for (int j = 0; j < n_entradas; j++) {
                nueva_historia[i][j] = la_historia[i][j];
            }
        }

        //Rellenar el resto d la historia con las evaluaciones de los casos ambiguos
        for (int i = 0; i < t_nueva_historia; i++) {
            //Evaluar para cada línea de la historia qué casos se cumplen
            for (int j = n_entradas, c = 0; j < n_entradas_total && c < n_casos; j++, c++) {
                //Caso a tratar
                Regla caso = casos_ambiguos.get(c);

                //Evaluar si el caso se cumple para la línea i de la historia antigua
                ArrayList<Elemento> antecedente = caso.getAntecedente().getElementos();
                ArrayList<Elemento> consecuente = caso.getConsecuente().getElementos();
                //Si no se cumple el antecedente entonces la regla es cierta?????????
                boolean reglaOK = true;
                boolean antecedenteOK = true;
                for (Elemento e : antecedente) {
                    int entrada = e.getEntrada();
                    int subindice = e.getSubindice();
                    boolean verdadero = e.isVerdadero();

                    //Si el momento de la historia al que apunta el elemento NO coincide con el valor del elemento sabemos que el antecedente no se cumple7
                    if (la_historia[i + subindice][entrada] != verdadero) {
                        antecedenteOK = false;
                    }
                }

                //Si el antecedente coincide hay que comprobar que coincida el consecuente
                if (antecedenteOK) {
                    boolean consecuenteOK = true;
                    for (Elemento e : consecuente) {
                        int entrada = e.getEntrada();
                        int subindice = e.getSubindice();
                        boolean verdadero = e.isVerdadero();

                        //Si el momento de la historia al que apunta el elemento NO coincide con el valor del elemento sabemos que el antecedente no se cumple7
                        if (la_historia[i + subindice][entrada] != verdadero) {
                            consecuenteOK = false;
                        }
                    }
                    if (!consecuenteOK) {
                        reglaOK = false;
                    }
                }

                //Aquí sabemos si la regla se cumple o no con reglaOK
                nueva_historia[i][j] = reglaOK;
            }
        }
        return nueva_historia;
    }

    private EvaluacionTeoria evaluar_teoria_con_ruido(Teoria teoria, boolean[][] la_historia) {
        EvaluacionTeoria eval = new EvaluacionTeoria();
        //Recorremos las certezas p


        return eval;
    }

    /**
     * Evalua una teoría.
     *
     * @param teoria
     * @param la_historia
     * @return La evaluación de la teoría.
     */
    private EvaluacionTeoria evaluar_teoria(Teoria teoria, boolean[][] la_historia) {
        EvaluacionTeoria r = new EvaluacionTeoria();

        ArrayList<Regla> reglas = teoria.getCertezas();

        int nentradas = la_historia[0].length;
        int t_historia = la_historia.length;

        int[] aciertos_entradas = new int[nentradas];
        int[] fallos_entradas = new int[nentradas];
        int[] aciertos_muestras = new int[t_historia];
        int[] fallos_muestras = new int[t_historia];

        //Recorremos la historia comprobando y tomando nota de cuántos pasos es capaz de explicar la teoría
        //TODO Deberíamos fijarnos sólo en las entradas originales, pero de momento nos fijamos en todas
        //más adelante pasaremos separada la historia de los estados extra

        for (int h = 0; h < t_historia - 1; h++) { //-1 para que no se salga ya que comprueba cada muestra con la siguiente para evaluar la predicción
            boolean[] muestra = la_historia[h];

            //Para cada muestra evaluar el estado siguiente con la teoría
            int[] siguiente = this.evaluar(muestra, reglas);


            //Comprobar aciertos con la siguiente
            boolean[] muestra_siguiente = la_historia[h + 1];


            for (int i = 0; i < nentradas; i++) {
                if (siguiente[i] == 1) {
                    if (muestra_siguiente[i]) {
                        //ACIERTO!
                        aciertos_entradas[i]++;
                        aciertos_muestras[h + 1]++;
                    } else {
                        //FALLO!
                        fallos_entradas[i]++;
                        fallos_muestras[h + 1]++;
                    }
                } else if (siguiente[i] == 0) {
                    if (!muestra_siguiente[i]) {
                        //ACIERTO!!
                        aciertos_entradas[i]++;
                        aciertos_muestras[h + 1]++;
                    }
                } else if (siguiente[i] == 2) { //El 2 indica Contradicción!
                    //FALLO!
                    fallos_entradas[i]++;
                    fallos_muestras[h + 1]++;
                }
            }
        }

        //Rellenamos la respuesta
        r.nentradas = nentradas;
        r.nmuestras = t_historia - 1; //No contamos una de las muestras porque sólo podemos evaluar los pasos que son uno menos
        r.aciertos_entradas = aciertos_entradas;
        r.aciertos_muestras = aciertos_muestras;
        r.fallos_entradas = fallos_entradas;
        r.fallos_muestras = fallos_muestras;

        return r;
    }

    private float[] entrada_incognita(int n) {
        //Devuelve una entrada incognita de la longitud indicada
        //Rellenamos un vector de floats de 0.5 que es la probabilidad que no nos dice nada

        float[] respuesta = new float[n];

        Arrays.fill(respuesta, 0.5f);

        return respuesta;

    }

    public ArrayList<Regla> extraer_reglas(boolean[][] historia, int nentradas, int tventana, float umbral_soporte, boolean generar_subreglas) {
        //Hacer las cuentas primera pasada
        //TODO crear clase CUENTAS
        Par<Long, Long>[][] primerScan = this.hacer_cuentas(historia, tventana);

        //Seleccionar las entradas que queremos tener en cuenta
        //TODO crear clase SELECCION_DE_ENTRADAS
        boolean[] seleccion_de_entradas = new boolean[nentradas];
        Arrays.fill(seleccion_de_entradas, true);

        //Elaborar la tabla para el TDFPG (extensible)
        //TODO crear clase TABLA
        ArrayList<InfoElemento> tabla = this.elaborar_tabla(primerScan, umbral_soporte, seleccion_de_entradas, tventana);

        //Crear instancia de TDFPG
        TDFPG td = new TDFPG();

        //Ejecutar TDFPG y obtener las reglas
        ArrayList<Regla> todas_las_reglas = td.extraer_reglas(tabla, historia, tventana, generar_subreglas);

        return todas_las_reglas;
    }

    public Teoria elaborar_teoria(boolean[][] historia, int nentradas, int tventana, double umbral_de_certeza, double umbral_de_hipotesis, double umbral_ruido, float umbral_soporte, boolean generar_subreglas) {

        //Extraer las reglas
        ArrayList<Regla> reglas = this.extraer_reglas(historia, nentradas, tventana, umbral_soporte, generar_subreglas);

        //Construir la teoría
        Teoria teoria = new Teoria();
        //Filtramos las reglas por el umbral de confianza
        for (Regla r : reglas) {
            if (r.getConfianza() >= umbral_de_certeza) {
                teoria.getCertezas().add(r);
            } else if (r.getConfianza() >= umbral_de_hipotesis) {
                teoria.getHipotesis().add(r);
            }
            if (r.getConfianza() >= umbral_ruido) {
                teoria.getSin_ruido().add(r);
            }
        }

        //Rellenamos la teoría
        teoria.setMaxTventana(tventana);
        teoria.setMaxNentradas(nentradas);

        return teoria;
    }

    /**
     * Predecir con la historia y la teoría un número de valores siguientes a
     * ser posible.
     *
     * @param historia
     * @param teoria
     * @param i
     * @return
     */
    float[][] predecir(boolean[][] historia, Teoria teoria, int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Elaborar la historia futura. Una historia incompleta con tantos ejemplos
     * de la historia original como se indiquen. La historia que se devuelve es
     * la continuación de la que se indica. Comienza los últmos nejemplos
     * registros y le siguen nincompletos registros inciertos.
     *
     * @param historia
     * @param nejemplos
     * @param ninciertos
     * @return
     */
    float[][] elaborar_historia_futura(boolean[][] historia, int nejemplos, int ninciertos) {

        //Calculamos los tamaños
        int nentradas = historia[0].length;
        int longitud = nejemplos + ninciertos;

        //La historia total
        float[][] respuesta = new float[longitud][];

        //Rellenamos el principio con los últimos nejemplos de la historia original
        int primerEjemplo = historia.length - nejemplos;
        for (int i = primerEjemplo, j = 0; i < historia.length; i++, j++) {
            respuesta[j] = boolean2float(historia[i]);
        }

        //Aquí ya tenemos los primeros ejemplos
        //Después rellenamos el resto de la historia con incertidumbre
        for (int i = nejemplos; i < longitud; i++) {
            respuesta[i] = this.entrada_incognita(nentradas);
        }

        return respuesta;
    }

    /**
     * Intentamos resolver las incógnitas de la historia que se nos pasa y
     * devolver la historia rellena.
     *
     * @param historia_incompleta
     * @param teoria
     * @return
     */
    float[][] rellenar_historia(float[][] historia_incompleta, Teoria teoria, boolean normalizar) {

        //Tamaño de la historia incompleta
        int thistoria_incompleta = historia_incompleta.length;

        //Tamaño de ventana de la teoría
        int tventana = teoria.getMaxTventana();

        //Tamaño de la historia que utilizaremos para rellenar la incompleta
        int thistoria = thistoria_incompleta + (tventana - 1);

        //Añadimos a la historia alguno registros al final que permitirán calcular hasta el último de la hstoria incompleta
        float[][] historia = new float[thistoria][];

        //Rellenamos la historia con la historia_incompleta
        System.arraycopy(historia_incompleta, 0, historia, 0, thistoria_incompleta);

        //Terminamos de rellenarla con incógnitas
        for (int i = thistoria_incompleta; i < thistoria; i++) {
            historia[i] = this.entrada_incognita(teoria.getMaxNentradas());
        }

        //El tamaño de la historia final será el de la historia incompleta menos tventana-1
        float[][] historia_rellena = new float[thistoria_incompleta][];



        //Daremos una pasada por la historia incompleta aplicando las reglas de la teoría a cada ventana

        //Cargamos la primera ventana interemdia
        int tventana_intermedia = tventana - 1;

        //La ventana intermedia contiene los registros que están siendo calculados
        float[][] ventana_intermedia = new float[tventana_intermedia][];
        System.arraycopy(historia, 0, ventana_intermedia, 0, tventana_intermedia);

        for (int i = tventana_intermedia, j = 0; i < thistoria; i++, j++) {
            //Componemos la ventana que enviaremos a rellenar
            float[][] ventana = new float[tventana][];

            //Los primeros elementos son los de la ventana intermedia
            System.arraycopy(ventana_intermedia, 0, ventana, 0, tventana_intermedia);

            //La ventana que vamos a enviar se compone de toda la ventana_intermedia más el siguiente registro
            //El último es el que añadimos que aún no ha empezado a ser calculado
            ventana[tventana - 1] = historia[i];

            //Enviamos la ventana a rellenar y recibimos la respuesta
            float[][] ventanaRellena = this.evaluar_probabilidad(ventana, teoria);

            //Normalizamos los valores de la ventanaRellena (para dejarlos en 0, 0.5 o 1)
            if (normalizar) {
                ventanaRellena = normalizar(ventanaRellena);
            }

            //Añadimos el primer registro a la historia que estamos componiendo
            //ya que este registro ha sido totalmente calculado
            historia_rellena[j] = ventanaRellena[0];

            //Volvemos a componer la ventana intermedia
            for (int vi = 0; vi < tventana_intermedia; vi++) {
                ventana_intermedia[vi] = ventanaRellena[vi + 1];
            }
        }

        return historia_rellena;
    }

    public String imprimir_historia(float[][] historia) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < historia.length; i++) {
            sb.append(imprime_array(historia[i])).append('\n');
        }

        return sb.toString();
    }

    public float[][] normalizar(float[][] array) {
        float[][] respuesta = new float[array.length][];

        for (int i = 0; i < array.length; i++) {
            respuesta[i] = new float[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                respuesta[i][j] = normalizar(array[i][j]);
            }
        }

        return respuesta;
    }
    
    public float[] normalizar(float[] array){
        float[] respuesta = new float[array.length];
        
        for(int i=0; i<array.length; i++){
            respuesta[i] = normalizar(array[i]);
        }
        
        return respuesta;
    }

    /**
     * Llevamos los valores a 0, 0.5 o 1 según su proximidad
     *
     * @param f
     * @return
     */
    private float normalizar(float f) {
        float r = 0f;
        if (f > 0f) {
            if (f < 0.25f) {
                r = 0f;
            } else if (f < 0.75f) {
                r = 0.5f;
            } else {
                r = 1f;
            }
        }
        return r;
    }

    ArrayList<Contradiccion> buscar_contradicciones(Teoria teoria) {
        //Una contradicción es un caso en el que dos reglas comparten el antecedente y tienen consecuentes contradictorios
        //normalmente la suma de sus confianzas estará cerca de 1
        //dos consecuentes son contradictorios si la unión contiene una contradicción
        ArrayList<Contradiccion> respuesta = new ArrayList<>();

        //Recorremos las hipótesis que es donde puede haber contradicciones
        ArrayList<Regla> hipotesis = teoria.getHipotesis();
        //Primero ordenamos las hipótesis por antecedente para hacer grupos con el mismo antecedente
        Collections.sort(hipotesis, new ComparaReglasPorAntecedente());

        //Vamos recorriendo la lista y haciendo grupos

        ComparaGruposOrdenandoElementos compG = new ComparaGruposOrdenandoElementos();

        //Guardamos la regla actual
        Regla r_nueva = null;
        GrupoElementos nuevo = null; //Nuevo es el antecedente que comparte todo el grupo
        float confianza_sumada = 0;
        int iguales = 0;

        ArrayList<Regla> grupo = new ArrayList<>();

        D.d("Reglas ordenadas por antecedente y consecuente");
        for (Regla r : hipotesis) {
            D.d(r.toString());
            //Extraemos el grupo de elementos del antecedente
            GrupoElementos e = r.getAntecedente();
            if (nuevo == null) {//Soy el primero, me asigno el turno
                nuevo = e;
                r_nueva = r;
                iguales = 0;
                confianza_sumada = (float) r.getConfianza();
            } else {
                GrupoElementos ge = r.getConsecuente();
                GrupoElementos gn = r_nueva.getConsecuente();
                //Si soy igual que el nuevo y si mi consecuente es del mismo tamaño que el del modelo 
                //TODO no estoy seguro de que se esté haciendo bien, voy a ver cómo de productivas son las contradicciones pequeñas
                //TODO Cambiar: De momento sólo nos fijaremos en las contradicciones con reglas con consecuente de tamaños iguales
                if (compG.compare(e, nuevo) == 0
                        && gn.getElementos().size() == ge.getElementos().size() //Alterar estas dos líneas 1 * para añadir las contradicciones de componentes grandes agrupadas por tamaños
                        && r.getCantidad_de_informacion() == r_nueva.getCantidad_de_informacion()) {

                    //Si mi consecuente contradice el del nuevo entonces me sumo al grupo
                    if (r.getConsecuente().contradice(r_nueva.getConsecuente()) //&& gn.getElementos().size() == ge.getElementos().size() //Alterar estas dos líneas 2 * . comentar las dos para mezclarlas todas
                            ) {
                        grupo.add(r);
                        iguales++;
                        confianza_sumada += r.getConfianza();
                    }

                } else {
                    //Si soy diferente del nuevo es que se han terminado sus iguales y comienzan los míos
                    if (iguales > 0) {
                        grupo.add(r_nueva);
                        //Creamos la contradicción y la añadimos a la lista
                        Contradiccion c = new Contradiccion();
                        c.setReglas(grupo);
                        c.setTotal_confianza(confianza_sumada);
                        c.setAntecedente(nuevo);
                        respuesta.add(c);
                    } //Lo apunto si había más de uno
                    nuevo = e; //Me asigno el turno
                    r_nueva = r;
                    iguales = 0; //Reinicio el contador de iguales
                    grupo = new ArrayList<>();
                    confianza_sumada = (float) r.getConfianza();
                }
            }
        }

        //Si había iguales en el último caso añadimos también el último
        if (iguales > 0) {
            grupo.add(r_nueva);
            //Creamos la contradicción y la añadimos a la lista
            Contradiccion c = new Contradiccion();
            c.setReglas(grupo);
            c.setTotal_confianza(confianza_sumada);
            c.setAntecedente(nuevo);
            respuesta.add(c);
        }

        return respuesta;
    }

    /**
     * Reducimos la teoría eliminando todas aquellas hipótesis que comparten
     * antecedente con una certeza pues ya no son hipótesis. No basta con que
     * compartan el antecedente??
     *
     * @param teoria
     * @return
     */
    Teoria reducir_teoria(Teoria teoria) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Mejorar la teoría quiere decir añadir estados para eliminar
     * contradicciones de las hipótesis.
     *
     * @param teoria
     * @return
     */
    Teoria mejorar_teoria(Teoria teoria) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
