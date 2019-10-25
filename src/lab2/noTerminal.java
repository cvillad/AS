/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 *
 * @author carlo
 */
public class noTerminal {
    private String symbol;
    private HashMap<String, HashSet<String>> producciones;
    private HashSet<String> primero;
    private HashSet<String> siguiente;

    public noTerminal(String symbol) {
        this.symbol = symbol;
        this.producciones= new LinkedHashMap();
        this.primero = new LinkedHashSet();
        this.siguiente = new LinkedHashSet();
    }

    public void addColumn(String[] n, String prod){
        for (int i = 0; i < n.length; i++) {
           this.producciones.get(prod).add(n[i]);
        }
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void addProduccion(String p){
        this.producciones.put(p,new HashSet());
    }
    
    public HashMap<String,HashSet <String>> getProducciones(){
        return this.producciones;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public HashSet<String> getPrimero() {
        return primero;
    }
    
    public void setPrimero(String[] primero) {
        for (int i = 0; i < primero.length; i++) {
            this.primero.add(primero[i]);
        }
    }

    public HashSet<String> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(String[] s) {
        for (int i = 0; i < s.length; i++) {
            if (!this.siguiente.contains(s[i])) {
                this.siguiente.add(s[i]);
            }
        }
    }
    
    public void addSiguiente(String s) {
        this.siguiente.add(s);
    }
    
}
