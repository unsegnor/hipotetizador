/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Debug
 * @author Victor
 */
class D {
    
    public static boolean enabled = false;

    static void d(String mensaje) {
        if(enabled) {
            System.out.println(mensaje);
        }
    }
    
}
