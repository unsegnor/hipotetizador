/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Un elemento se compone del identificador de la entrada, el identificador de
 * subíndice y si es verdadero o falso
 *
 * @author Víctor
 */
public class Elemento {

    private int entrada = 0;
    private int subindice = 0;
    private boolean verdadero = true;
    private int ID = 0; //Almacena el identificador único que devolvemos cuando llaman al hashcode
    //Lo podemos hacer así o calculándolo cada vez que lo piden para componer el número

    public Elemento() {
    }

    public Elemento(int entrada, int subindice, boolean verdadero) {
        init(entrada, subindice, verdadero);
    }

    private void init(int entrada, int subindice, boolean verdadero) {
        this.setEntrada(entrada);
        this.setSubindice(subindice);
        this.setVerdadero(verdadero);
        actualizarID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Elemento otro = (Elemento) obj;

        return (getEntrada() == otro.getEntrada() && getSubindice() == otro.getSubindice() && isVerdadero() == otro.isVerdadero());
    }

    /**
     * Debería devolver un número que identifique unívocamente a un elemento
     * 
     *
     * @return
     */
    @Override
    public int hashCode() {
        return getID();

    }

    public String num() {
        StringBuilder sb = new StringBuilder();

        sb.append(getEntrada()).append(getSubindice()).append(isVerdadero() ? 1 : 0);

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(getEntrada()).append(",").append(getSubindice()).append(",").append(isVerdadero() ? 1 : 0).append(")");

        return sb.toString();

    }

    /**
     * @return the entrada
     */
    public int getEntrada() {
        return entrada;
    }

    /**
     * @param entrada the entrada to set
     */
    public void setEntrada(int entrada) {
        this.entrada = entrada;
        actualizarID();
        
    }

    /**
     * @return the subindice
     */
    public int getSubindice() {
        return subindice;
    }

    /**
     * @param subindice the subindice to set
     */
    public void setSubindice(int subindice) {
        this.subindice = subindice;
        actualizarID();
    }

    /**
     * @return the verdadero
     */
    public boolean isVerdadero() {
        return verdadero;
    }

    /**
     * @param verdadero the verdadero to set
     */
    public void setVerdadero(boolean verdadero) {
        this.verdadero = verdadero;
        actualizarID();
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    private void actualizarID() {
        ID = Integer.parseInt(num());
    }
}
