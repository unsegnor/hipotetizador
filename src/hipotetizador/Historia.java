/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

/**
 * Una historia es un registro de la realidad que se ha efectuado de forma continua
 * @author Victor
 */
public class Historia {
    
    //Número de entradas que tiene
    private int nentradas;
    
    //Identificadores de las entradas
    private String[] IDs;
    
    //Historia
    private int[][] historia;
    
    
    public Historia(int numero_de_entradas, String[] identificadores, int[][] historia){
        init(numero_de_entradas,identificadores,historia);
    }
    
    private void init (int numero_de_entradas, String[] identificadores, int[][] historia){
        setNentradas(numero_de_entradas);
        setIDs(identificadores);
        this.setHistoria(historia);
    }

    /**
     * @return the nentradas
     */
    public int getNentradas() {
        return nentradas;
    }

    /**
     * @param nentradas the nentradas to set
     */
    public void setNentradas(int nentradas) {
        this.nentradas = nentradas;
    }

    /**
     * @return the IDs
     */
    public String[] getIDs() {
        return IDs;
    }

    /**
     * @param IDs the IDs to set
     */
    public void setIDs(String[] IDs) {
        this.IDs = IDs;
    }

    /**
     * @return the historia
     */
    public int[][] getHistoria() {
        return historia;
    }

    /**
     * @param historia the historia to set
     */
    public void setHistoria(int[][] historia) {
        this.historia = historia;
    }
    
}
