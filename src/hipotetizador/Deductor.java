/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Esta es la clase que recibe las lecturas una por una o de historia en historia y deduce cosas
 * @author Victor
 */
class Deductor implements Subscriptor<RegistroH> {

    @Override
    public void recibir(RegistroH envio) {
        System.out.println("Recibido: "+envio);
    }
    
}
