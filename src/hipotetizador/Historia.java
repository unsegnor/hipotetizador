/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Historia del estado de los sensores en cada medición a lo largo del tiempo.
 * @author Victor
 */
public class Historia {
    
    //Almacena registros de datos
    private ArrayList<RegistroH> registros = new ArrayList<>();
    /**
     * Reiniciar la historia.
     */
    public void reiniciar(){
        registros.clear();
    }
    
    public int getSize(){
        return registros.size();
    }
    
    /**
     * Añadir un registro en la historia
     * @param registro 
     */
    public void registrar(RegistroH registro){
        registros.add(registro);
    }
    /**
     * Añadir una lectura en la historia.
     * @param lectura 
     */
    public void registrar(int[] lectura){
        registros.add(new RegistroH(lectura));
    }
    /**
     * 
     * @return una lista de acceso read-only de los registros que contiene hasta el momento.
     */
    public List<RegistroH> getRegistros(){
        return Collections.unmodifiableList(this.registros);
    }
    
}
