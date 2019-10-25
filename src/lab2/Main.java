/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rafael Lastra
 */
public class Main extends javax.swing.JFrame {

    ArrayList<String> terminal = new ArrayList();
    ArrayList<noTerminal> noterminal = new ArrayList();
    HashMap<String, HashSet <String>> prodM=new LinkedHashMap();
    DefaultTableModel mtableModel;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        this.setLocationRelativeTo(null);
        chooserFrame.setLocationRelativeTo(null);
        jButton2.setEnabled(false);
    }

    public ArrayList Recursividad(ArrayList<String> vec) {
        ArrayList<String> vec2 = new ArrayList<String>();
        String h = "";
        for (String k : vec) {
            String z = k.substring(0, 1);
            if (k.contains(z + "->" + z)) {
                h = h + z;
                for (String r : vec) {
                    if (r.compareTo(k) != 0 && r.contains(z + "->") && !r.contains(z + "->" + z)) {
                        if (!vec2.contains(r + z + "'")) {
                            vec2.add(r + z + "'");
                        }
                    }
                }
                if (!vec2.contains(z + "'" + "-" + ">" + k.substring(4, k.length()) + z + "'")) {
                    vec2.add(z + "'" + "-" + ">" + k.substring(4, k.length()) + z + "'");
                }
                if (!vec2.contains(z + "'" + "-" + ">" + "&")) {
                    vec2.add(z + "'" + "-" + ">" + "&");
                }
            }
        }
        for (String x : vec) {
            String z = x.substring(0, 1);
            if (!h.contains(z) || x.contains("'->")) {
                vec2.add(x);
            }
        }
//        for (String s : vec2) {
//            System.out.println(s);
//        }
        return vec2;
    }

    public ArrayList factor(ArrayList<String> vec, String fact, String z) {
        ArrayList<String> vec4 = new ArrayList<String>();
        for (String p : vec) {
            String ñ = p.substring(3, 4);
            String l = fact.substring(0, 1);
            if (ñ.compareTo(l) == 0 && p.contains(z + "->")) {
                vec4.add(p);
            }
        }
        return vec4;
    }

    public boolean verificar(ArrayList<String> vec, String d, String z) {
        ArrayList<String> vec4 = new ArrayList<String>();
        boolean sw = false;
        int cont = 0;
        int cont2 = 0;
        for (String x : vec) {
            String f = d.substring(0, 1);
            String k = x.substring(3, 4);
            if (x.contains(z + "->") && f.compareTo(k) == 0) {
                cont++;
                vec4.add(x);
            }
        }
        for (String u : vec4) {
            if (u.contains(d)) {
                cont2++;
            }
        }
        if (cont == cont2) {
            sw = true;
        }
        return sw;

    }

    public ArrayList Factorizacion(ArrayList<String> vec) {
        ArrayList<String> vec3 = new ArrayList<String>();
        ArrayList<String> vec4 = new ArrayList<String>();
        String c = "";
        String d2 = "";
        String z2 = "";
        for (String p : vec) {
            int cont2 = 0;
            String z = p.substring(0, 1);
            if (!c.contains(z)) {
                outerloop:
                for (int i = p.length(); i >= 4; i--) {
                    String d = p.substring(3, i);
                    int cont = 0;
                    for (String k : vec) {
                        String w = k.substring(3, k.length());
                        if (w.length() >= d.length()) {
                            if (k.substring(3, i).compareTo(d) == 0 && k.contains(z + "->")) {
                                cont++;
                            }
                        }
                    }
                    if ((cont - 1) > 0) {
                        if (verificar(vec, d, z)) {
                            cont2 = cont - 1;
                            d2 = d;
                            z2 = z;
                            // System.out.println(d);
                            break outerloop;
                        }
                    }
                }
                if (cont2 != 0) {
                    c = c + z2;
                    vec4 = factor(vec, d2, z2);
                    vec3.add(z2 + "-" + ">" + d2 + z2 + "'");
                    for (String t : vec) {
                        String ñ = t.substring(3, 4);
                        String n = d2.substring(0, 1);
                        if (ñ.compareTo(n) != 0 && t.contains(z2 + "->")) {
                            vec3.add(t);
                        }
                    }
                    for (String q : vec4) {
                        String ñ = q.substring(3, q.length());
                        if (ñ.compareTo(d2) == 0) {
                            vec3.add(z2 + "'" + "-" + ">" + "&");
                        } else if (ñ.compareTo(d2) > 0) {
                            vec3.add(z2 + "'" + "-" + ">" + ñ.substring(d2.length(), ñ.length()));
                        }
                    }
                }
            }
        }
        for (String g : vec) {
            String z = g.substring(0, 1);
            if (!c.contains(z)) {
                vec3.add(g);
            }
        }
        return vec3;
    }

    public String Prim(noTerminal nt, String p){
        for (Map.Entry<String,HashSet<String>> t: nt.getProducciones().entrySet()) {
            if (find(t.getKey().substring(0,1))==null) {
                p+=t.getKey().substring(0,1)+", ";
                t.getValue().add(t.getKey().substring(0,1));
            }else{
                if (t.getKey().length()>1 && t.getKey().substring(1, 2).equals("'")) {
                    p+=Prim(find(t.getKey().substring(0, 2)),"");
                    nt.addColumn(Prim(find(t.getKey().substring(0, 2)),"").split(", "),t.getKey());
                }else {
                    p+=Prim(find(t.getKey().substring(0, 1)),"");
                    nt.addColumn(Prim(find(t.getKey().substring(0, 1)),"").split(", "),t.getKey());
                }
                //nt.addColumn(p.split(", "),t.getKey());
            }    
            
        }   
        return p;
    }
    
    public void Primero() {
        for (noTerminal t: noterminal) {
            String s=Prim(t,"");
            t.setPrimero(s.split(", "));
            primsigTextArea.append("PRIMERO("+t.getSymbol()+")="+t.getPrimero());
            primsigTextArea.append(System.getProperty("line.separator"));
        }
        for (noTerminal t: noterminal){
        for (Map.Entry<String,HashSet<String>> p: t.getProducciones().entrySet()) {
            System.out.println("Producción: " + p.getKey() + " Columnas: " + p.getValue());
        }
        }
    }
    
    public void Siguiente() {
        int i=0;
        for (noTerminal t : noterminal) {
            String s=Sgte(t,"");
            if(s.length()>0)t.setSiguiente(s.split(", "));
            if (i==0) t.getSiguiente().add("$");
            primsigTextArea.append("SIGUIENTE("+t.getSymbol()+")="+t.getSiguiente());
            primsigTextArea.append(System.getProperty("line.separator"));
            i++;
        }
    }
    
    public String Sgte(noTerminal nt, String s){
        for(noTerminal not: noterminal){
            String cabezote=not.getSymbol();
            for (Map.Entry<String,HashSet<String>> prod: not.getProducciones().entrySet()) {
                String producido=prod.getKey();
                int sw=0;
                int i=producido.indexOf(nt.getSymbol());
                if (producido.contains(nt.getSymbol()) && !cabezote.equals(nt.getSymbol())) {
                    if(i+1<producido.length() && !(producido.substring(i+1,i+2).equals("'") && !nt.getSymbol().contains("'"))){
                        if (nt.getSymbol().contains("'")) {
                            s+=arrayToString(find(nt.getSymbol().substring(0,1)).getSiguiente());
                        }else {
                            sw=1;
                            i=producido.indexOf(nt.getSymbol())+1;
                        }
                    }else if(i+1==producido.length()){
                        sw=1;
                        i=producido.indexOf(nt.getSymbol())+1;
                    }
                }
                if (sw==1) {
                    if (i<producido.length()) {
                        String next=producido.substring(i, i+1);
                        if (terminal.contains(next)) {
                            s+=next+", ";
                        }else{
                            if (i+1<producido.length() && producido.substring(i+1, i+2).equals("'")) {
                                next=producido.substring(i, i+2);
                            }      
                            noTerminal t=find(next);
                            for(String p: t.getPrimero()){
                                if (p.equals("&")) {
                                    if (find(cabezote).getSiguiente().size()>0) {
                                        s+=arrayToString(find(cabezote).getSiguiente());
                                    }else s+=Sgte(find(cabezote),s);
                                }else{ 
                                    s+=p+", ";
                                }
                            }
                        }
                    }else{
                        if (find(cabezote).getSiguiente().size()>0) {
                            s+=arrayToString(find(cabezote).getSiguiente());
                        }else s+=Sgte(find(cabezote),s);
                    }
                }
            }
        }
        return s;
    }
    
    public String arrayToString(HashSet<String> ar){
        String s="";
        for(String t: ar){
            s+=t+", ";
        }
        return s;
    }

    public void tablaM() {
        mtableModel.addColumn(" T/NT ");
        terminal.add("$");
        for (String x : terminal) {
            mtableModel.addColumn(x);
        }
        mtableModel.setRowCount(noterminal.size());
        int cont = 0;
        for (noTerminal nt: noterminal) {
            mtableModel.setValueAt(nt.getSymbol(), cont, 0);
            for (Map.Entry<String,HashSet<String>> prod: nt.getProducciones().entrySet()) {
                for (String columna: prod.getValue()) {
                    if (!columna.equals("&")) {
                        mtableModel.setValueAt(nt.getSymbol()+"->"+prod.getKey(), noterminal.indexOf(nt), terminal.indexOf(columna)+1);
                    }else{
                        for (String sig: nt.getSiguiente()) {
                            mtableModel.setValueAt(nt.getSymbol()+"->"+prod.getKey(), noterminal.indexOf(nt), terminal.indexOf(sig)+1);
                        }
                    }
                }
            }
            cont++;
        }
        jTable1.setModel(mtableModel);
    }

    public noTerminal find(String s){
        for (noTerminal t: noterminal) {
            if (t.getSymbol().equals(s)) {
                return t;
            }
        }
        return null;
    }
    
    public void Terminal(ArrayList<String> gramatica) {
        String c = "";
        for (String x : gramatica) {
            if (x.contains("'->")) {
                c = x.substring(4, x.length());
                if (find(x.substring(0, 2))==null) {
                    noterminal.add(new noTerminal(x.substring(0, 2)));
                }
                find(x.substring(0,2)).addProduccion(c);
            } else {
                c = x.substring(3, x.length());
                if (find(x.substring(0, 1))==null) {
                    noterminal.add(new noTerminal(x.substring(0, 1)));
                }
                find(x.substring(0,1)).addProduccion(c);
            }
            c = c.replaceAll("[A-Z]+", "");
            c = c.replace("'", "");
            c = c.replace("&", "");
            String[] vector = c.split("");
            for (int i = 0; i < c.length(); i++) {
                if (!terminal.contains(vector[i])) {
                    terminal.add(vector[i]);
                }
            }
        }
    }

    public ArrayList ordenar(ArrayList<String> vec, ArrayList<String> j2) {
        ArrayList<String> j3 = new ArrayList<String>();
        for (String x : vec) {
            String h = x.substring(0, 1);
            for (String z : j2) {
                String r = z.substring(0, 1);
                if (h.compareTo(r) == 0) {
                    if (!j3.contains(z)) {
                        j3.add(z);
                    }
                }
            }
        }
        return j3;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooserFrame = new javax.swing.JFrame();
        chooser = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        gramaticaTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        primsigTextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        reconocerTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        chooserFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chooserFrameLayout = new javax.swing.GroupLayout(chooserFrame.getContentPane());
        chooserFrame.getContentPane().setLayout(chooserFrameLayout);
        chooserFrameLayout.setHorizontalGroup(
            chooserFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        chooserFrameLayout.setVerticalGroup(
            chooserFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial Black", 2, 18)); // NOI18N
        jLabel1.setText("Gramatica sin Vicios");

        jLabel2.setFont(new java.awt.Font("Arial Black", 2, 18)); // NOI18N
        jLabel2.setText("Primero y Siguiente");

        gramaticaTextArea.setEditable(false);
        gramaticaTextArea.setColumns(20);
        gramaticaTextArea.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        gramaticaTextArea.setRows(5);
        jScrollPane3.setViewportView(gramaticaTextArea);

        primsigTextArea.setEditable(false);
        primsigTextArea.setColumns(20);
        primsigTextArea.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        primsigTextArea.setRows(5);
        jScrollPane2.setViewportView(primsigTextArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(176, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Primero y Siguiente", jPanel1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel3.setFont(new java.awt.Font("Arial Black", 2, 18)); // NOI18N
        jLabel3.setText("Tabla M");

        jLabel4.setFont(new java.awt.Font("Arial Black", 2, 18)); // NOI18N
        jLabel4.setText("Reconocimiento");

        jLabel5.setText("Cadena a Verificar -->");

        jButton2.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jButton2.setText("Verificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        reconocerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(reconocerTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jTabbedPane1.addTab("Tabla M y Reconocimiento", jPanel2);

        jMenu1.setText("File");

        jMenuItem1.setText("Seleccionar archivo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooserActionPerformed
        // TODO add your handling code here:
        chooserFrame.setVisible(false);
        if (chooser.getSelectedFile()!=null) {
            mtableModel=new DefaultTableModel();
            terminal.clear();
            noterminal.clear();
            primsigTextArea.setText("");
            gramaticaTextArea.setText("");
            File file = chooser.getSelectedFile();
            String fullPath = file.getAbsolutePath();
            ArrayList<String> vec = new ArrayList();
            ArrayList<String> j1;
            ArrayList<String> gram;
            try {
                FileInputStream archivo = new FileInputStream(fullPath);
                try (DataInputStream entrada = new DataInputStream(archivo)) {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
                    String lineas;
                    while ((lineas = buffer.readLine()) != null) {
                        vec.add(lineas);
                    }
                }
            } catch (IOException e) {
            }
            //gramaticaTextArea.setOpaque(false);
            //gramaticaTextArea.setBackground(new Color(0, 0, 0, 0));
            j1 = Recursividad(vec);
            gram = Factorizacion(j1);
            gram = ordenar(vec, gram);
            for (String zx : gram) {
                gramaticaTextArea.append(zx);
                gramaticaTextArea.append(System.getProperty("line.separator"));
            }
            Terminal(gram);
            //primsigTextArea.setOpaque(false);
            //primsigTextArea.setBackground(new Color(0, 0, 0, 0));
            Primero();
            Siguiente();
            tablaM();
            chooser.setSelectedFile(null);
            jButton2.setEnabled(true);
        }
    }//GEN-LAST:event_chooserActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model=new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Pila","Entrada","Salida"});
        model.setColumnCount(3);
        String cadena="$"+jTextField1.getText();
        Stack pila=new Stack();
        pila.push("$");
        pila.push(noterminal.get(0).getSymbol());
        System.out.println("R: "+mtableModel.getValueAt(0,1+terminal.indexOf(cadena.substring(0,1))));
        String salida=mtableModel.getValueAt(0,1+terminal.indexOf(cadena.substring(1,2))).toString();
        model.addRow(new Object[]{"$"+pila.pop(),cadena,salida});
        reconocerTable.setModel(model);
        //while(!pila.isEmpty() && cadena.equals("")){
            if (pila.pop().equals(cadena.substring(0,1))) {
                pila.push(pila);
                cadena=cadena.substring(1);
            }else{

            }  
        //}
        
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        //if (chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            chooserFrame.setVisible(true);
            chooserFrame.pack();
            chooserFrame.setLocationRelativeTo(null);
        //}
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser chooser;
    private javax.swing.JFrame chooserFrame;
    private javax.swing.JTextArea gramaticaTextArea;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextArea primsigTextArea;
    private javax.swing.JTable reconocerTable;
    // End of variables declaration//GEN-END:variables
}
