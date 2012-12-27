/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Un elemento se compone del identificador de la entrada, el identificador de subíndice y si es verdadero o falso
 * @author Víctor
 */
public class Elemento {
    
    public int entrada = 0;
    public int subindice = 0;
    public boolean verdadero = true;
    
    public Elemento(){}
    
    public Elemento(int entrada, int subindice, boolean verdadero){
        this.entrada = entrada;
        this.subindice = subindice;
        this.verdadero = verdadero;
    }
    
    @Override
    public boolean equals (Object obj){
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        
        Elemento otro = (Elemento) obj;
        
        return (entrada == otro.entrada && subindice == otro.subindice && verdadero == otro.verdadero);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("(").append(entrada).append(",").append(subindice).append(",").append(verdadero?1:0).append(")");
        
        return sb.toString();
                
    }
    
}
