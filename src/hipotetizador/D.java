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
    public static int level = 0;

    static void d(String mensaje) {
        if(enabled && level ==0) {
            System.out.println(mensaje);
        }
    }
    
    static void d(int nivel, String mensaje){
        if(enabled && nivel >= level) {
            System.out.println(mensaje);
        }
    }
    
}
