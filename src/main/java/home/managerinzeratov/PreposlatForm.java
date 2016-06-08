/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.OdosielaneTableModel;
import entity.PreposlanyEmail;
import entity.HistoriaTableModel;
import entity.Inzerat;
import home.managerinzeratov.MainForm2.JComponentTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static javafx.concurrent.Worker.State.FAILED;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Janco1
 */
public class PreposlatForm extends javax.swing.JDialog {

    public ArrayList<Inzerat> preposielane;
    public ArrayList<Inzerat> aktualnePreposielane = new ArrayList<Inzerat>();
    private String[] odosielaneTableColumnNames = {"X", "názov", "lokalita", "dátum"};
    private String[] historiaTableColumnNames = {"dátum", "príjemca", "predmet",};
    private OdosielaneTableModel odosielaneTableModel;
    public MainForm2 parent;
    public ImageIcon signedStar;
    public ImageIcon unsignedStar;
    private WebView view;
    private final JFXPanel jfxPanel = new JFXPanel();
    ScrollPane webViewScroll = new ScrollPane();
    private WebEngine engine;
    private final JPanel panel = new JPanel(new BorderLayout());
    private Database database;
    private int pocetOznacenych;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private ArrayList<String> emaily;
    private File emailFile;
    private List<PreposlanyEmail> historiaEmailov;
    private HistoriaTableModel historiaTableModel;
    private String aktualnyPredmet;
    private boolean aktualnyOdoslany = false;

    /**
     * Creates new form PreposlatForm
     */
    public PreposlatForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public PreposlatForm(java.awt.Frame parent, boolean modal, ArrayList<Inzerat> prep, Database db, String predmet) {
        super(parent, modal);
        database = db;
        this.preposielane = prep;
        aktualnePreposielane.addAll(prep);
        this.parent = (MainForm2) parent;
        signedStar = this.parent.signedStar;
        unsignedStar = this.parent.unsignedStar;
        //pocetOznacenych = preposielane.size(); pri refreshovani odosielaneTable sa to pocita
        initComponents();
        setLocationRelativeTo(null);

        sprievodnyTextTextArea.setText(database.getSprievodnyText());
        aktualnyPredmet = predmet;
        //predmetTextField.setText(aktualnyPredmet); toto sa ma zmenit az po kliknuti do historia table

        odosielaneTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                OdosielaneTableModel model = (OdosielaneTableModel) table.getModel();
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.white);
                Inzerat aktualny = preposielane.get(row);

                if (isSelected) {
                    c.setForeground(Color.BLACK);
                    c.setBackground(new Color(199, 223, 252));
                }
                if (column == 0) {
                    // klikli sme na hviezdicku
                    // TODO: osetrit vynimky ked zlyha databaza, aby sa vedelo hned ked sa nieco nepodari ulozit
                    JLabel novy = new JLabel();
                    novy.setOpaque(true);
                    if (hasFocus) {
                        // zakazdym sa pocet prepocitava pri refreshovani, ktore sa udeje po kazdom kliknuti
//                        if (aktualny.isZaujimavy()) {
//                            pocetOznacenych--;
//                        } else {
//                            pocetOznacenych++;
//                        }
                        System.out.println("pocet oznacenych: " + pocetOznacenych);
                        aktualny.setZaujimavy(!aktualny.isZaujimavy());
                        //database.updateInzerat(aktualny);
                    }

                    if (aktualny.isZaujimavy()) {
                        novy.setIcon(signedStar);
                    } else {
                        novy.setIcon(unsignedStar);
                    }
                    novy.setBackground(Color.white);
                    if (isSelected) {
                        novy.setBackground(new Color(199, 223, 252));
                    }
                    return novy;
                }
                return c;
            }
        });
// odosielane inzeraty zoznam sa zobrazi po kliknuti do historie table
//        refreshOdosielaneInzeratyTable(preposielane);
//        refreshEditorPane();

        File preposlaneFolder = new File("preposlane");
        String[] directories = preposlaneFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        emaily = new ArrayList<String>();
        for (int i = 0; i < directories.length; i++) {
            emaily.add(directories[i]);
        }
        DefaultComboBoxModel<String> emailyComboboxModel = new DefaultComboBoxModel<String>(directories);
        emailyComboBox.setModel(emailyComboboxModel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("window closing action");
                changes.firePropertyChange("preposlatFormEnded", true, false);
                super.windowClosing(e);
            }
        });

        refreshHistoriaTable();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel(){
            protected void paintComponent(Graphics g)
            {
                g.drawImage(parent.img, 0, 0, null);
                super.paintComponent(g);
            }
        };
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        prijemcaTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        odosielaneTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        emailyComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        historiaTable = new javax.swing.JTable();
        odoslatButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        sprievodnyTextTextArea = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        predmetTextField = new javax.swing.JTextField();
        nastavitButton = new javax.swing.JButton();
        pouziTextButton = new javax.swing.JButton();
        odosielatelTextField = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Preposlať inzeráty");
        setResizable(false);

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(1);
        jSplitPane1.setToolTipText("");
        jSplitPane1.setEnabled(false);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(1100, 636));

        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Od:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Príjemca:");

        odosielaneTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        odosielaneTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                odosielaneTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(odosielaneTable);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Odosielané inzeráty:");

        emailyComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        emailyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailyComboBoxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("História:");

        historiaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        historiaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                historiaTableMouseReleased(evt);
            }
        });
        jScrollPane5.setViewportView(historiaTable);

        odoslatButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        odoslatButton.setText("Odoslať email >>");
        odoslatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odoslatButtonActionPerformed(evt);
            }
        });

        sprievodnyTextTextArea.setColumns(20);
        sprievodnyTextTextArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        sprievodnyTextTextArea.setRows(5);
        sprievodnyTextTextArea.setText("Dobrý deň pán kolega,\n\nposielam Vám ďalší zoznam inzerátov.\n\nVedúci.");
        sprievodnyTextTextArea.setWrapStyleWord(true);
        sprievodnyTextTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sprievodnyTextTextAreaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(sprievodnyTextTextArea);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Predmet:");

        nastavitButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        nastavitButton.setText("Uložiť text");
        nastavitButton.setEnabled(false);
        nastavitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nastavitButtonActionPerformed(evt);
            }
        });

        pouziTextButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        pouziTextButton.setText("Použi text >>");
        pouziTextButton.setEnabled(false);
        pouziTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pouziTextButtonActionPerformed(evt);
            }
        });

        odosielatelTextField.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        odosielatelTextField.setForeground(new java.awt.Color(255, 255, 255));
        odosielatelTextField.setText("no-reply.inzeraty@jmurin.sk");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(predmetTextField))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(prijemcaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emailyComboBox, 0, 149, Short.MAX_VALUE))
                            .addComponent(odosielatelTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nastavitButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pouziTextButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(odoslatButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(odosielatelTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(prijemcaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(predmetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nastavitButton)
                    .addComponent(pouziTextButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(odoslatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        createScene();
        panel.add(jfxPanel, BorderLayout.CENTER);
        jSplitPane1.setRightComponent(panel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void odosielaneTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_odosielaneTableMouseClicked
        // zmenili sa inzeraty tak sa musi zmenit aj email file
        emailFile = InzeratViewGenerator.getEmail(preposielane, sprievodnyTextTextArea.getText());
        refreshOdosielaneInzeratyTable(preposielane);
        //refreshEditorPane(); automaticky sa refreshne po refreshnuti OdosielaneInzeratyTable
    }//GEN-LAST:event_odosielaneTableMouseClicked

    private void pouziTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pouziTextButtonActionPerformed
        // zmenil sa text tak sa musi zmenit aj email file
        emailFile = InzeratViewGenerator.getEmail(preposielane, sprievodnyTextTextArea.getText());
        refreshEditorPane();
        pouziTextButton.setEnabled(false);
    }//GEN-LAST:event_pouziTextButtonActionPerformed

    private void sprievodnyTextTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sprievodnyTextTextAreaKeyReleased
        pouziTextButton.setEnabled(true);
        nastavitButton.setEnabled(true);
    }//GEN-LAST:event_sprievodnyTextTextAreaKeyReleased

    private void odoslatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odoslatButtonActionPerformed
        if (odosielatelTextField.getText().isEmpty() || !odosielatelTextField.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Nesprávny formát odosielateľa.");
            return;
        }
        if (prijemcaTextField.getText().isEmpty() || !prijemcaTextField.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Nesprávny formát príjemcu.");
            return;
        }
        if (predmetTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nie je vyplnený predmet emailu.");
            return;
        }
        if (pouziTextButton.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Musíte najprv aktualizovať sprievodný text emailu\nkliknutím na >>Použiť text<<");
            return;
        }
        if (pocetOznacenych == 0) {
            int naozaj = JOptionPane.showConfirmDialog(this, "Neboli označené žiadne inzeráty do emailu.\nNaozaj chcete odoslať email?");
            if (naozaj != 0) {
                return;
            }
        }
        // teraz odoslem email
        try {
//            checkEmailExistuje(prijemcaTextField.getText());
            //odosliEmail();
             odosliEmailPHP();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Nepodarilo sa odoslať email. Skúste znova neskôr. \n" + e);
            return;
        }

        // email je odoslany, ulozim si preposlane inzeraty do DB
        boolean suborVytvoreny = false;
        if (emaily.contains(prijemcaTextField.getText().trim())) {
            // nemusime vytvarat osobitny adresar
            suborVytvoreny = true;
        } else {
            // este neexistuje dany email tak vytvorime novy adresar
            File theDir = new File("preposlane/" + prijemcaTextField.getText());

            // if the directory does not exist, create it
            if (!theDir.exists()) {
                System.out.println("creating directory: " + "preposlane/" + prijemcaTextField.getText().trim());
                try {
                    theDir.mkdir();
                    suborVytvoreny = true;
                } catch (SecurityException se) {
                    //handle it
                    System.out.println(se);
                    JOptionPane.showMessageDialog(this, "Nepodarilo sa vytvoriť adresár pre " + prijemcaTextField.getText().trim());
                    //return;
                }
                if (suborVytvoreny) {
                    System.out.println("DIR created");
                    emaily.add(prijemcaTextField.getText().trim());
                }
            }
        }
        String cesta = "preposlane";
        if (suborVytvoreny) {
            String pom = predmetTextField.getText();//.replaceAll("[ ]", "_");
            //pom = pom.replaceAll("[^a-zA-Z0-9ľščťžýáíéúôňäÁÉÚÍÓÝŤŠĎĽŽČŇvVwW_]", ""); //  \/:*?<>|"
            System.out.println("pom: " + pom);
            pom = pom.replaceAll("[\\\\/\\:\\*\\?\\<\\>\\|\"]", "");
            System.out.println("pom: " + pom);
            pom = pom.substring(0, Math.min(50, pom.length()));
            cesta = "preposlane/" + prijemcaTextField.getText() + "/" + getCas(System.currentTimeMillis()) + "_" + pom + ".html";
            // ulozime kopiu emailu
            ulozKopiu(cesta);
        }
        // updatnem v DB iba tie ktore su pre mna zaujimave, ktore som zmenil
        // aby som nezmenil odoslany email ohviezdickovany na neohviezdickovany
        ArrayList<Inzerat> zaujimave = new ArrayList<Inzerat>();
        for (Inzerat inz : preposielane) {
            if (inz.isZaujimavy()) {
                zaujimave.add(inz);
                inz.setPreposlany(true);
                inz.setArchivCesta(cesta);
            }
        }
        // zmenime iba tie ktore su pre nas zaujimave
        //database.updateInzeraty(preposielane);
        database.updateInzeraty(zaujimave);
        database.nastavOdosielatela(odosielatelTextField.getText());
        JOptionPane.showMessageDialog(this, "Email úspešne odoslaný.");
        // ak odosielame aktualny email, tak si zaznacime ze je odoslany a nebudeme ho uz pri refreshovani historie specialne pridavat 
        if (historiaTable.getSelectedRow() == 0) {
            aktualnyOdoslany = true;
        }
        // bol odoslany novy email, prekreslime celu tabulku historie
        refreshHistoriaTable();
        //prekresliHistoriaTable(); toto sa zavola uz v refresh historia

    }//GEN-LAST:event_odoslatButtonActionPerformed

    private void nastavitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nastavitButtonActionPerformed
        database.updateSprievodnyText(sprievodnyTextTextArea.getText());
        nastavitButton.setEnabled(false);
    }//GEN-LAST:event_nastavitButtonActionPerformed

    private void emailyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailyComboBoxActionPerformed
        prijemcaTextField.setText((String) emailyComboBox.getSelectedItem());
        //refreshHistoriaTable();
    }//GEN-LAST:event_emailyComboBoxActionPerformed

    private void historiaTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historiaTableMouseReleased
        // ked klikneme do historie tak zmenime zoznam aktivnych inzeratov podla zvoleneho stareho emailu
        int idx = historiaTable.getSelectedRow();
        if (idx == -1 || idx >= historiaEmailov.size()) {
            // return;
            idx = 0;// vzdy bude historia table neprazdna minimalne tam bude aktualny email
        }
        PreposlanyEmail aktualny = historiaEmailov.get(idx);
        preposielane = new ArrayList<Inzerat>();
        preposielane.addAll(aktualny.getInzeraty());
        prijemcaTextField.setText(aktualny.getPrijemca());
        predmetTextField.setText(aktualny.getSubject());
// toto netreba lebo v historii sme si poznacili aky ma mat aktualny email predmet
//        if (idx == 0) {
//            predmetTextField.setText(aktualnyPredmet);
//        }
        if (historiaTable.getSelectedRow() == 0 && !aktualnyOdoslany) {
            // generujeme si subor
            emailFile = InzeratViewGenerator.getEmail(preposielane, sprievodnyTextTextArea.getText());
        } else {
            // loadujeme ulozeny email
            emailFile = new File(historiaEmailov.get(historiaTable.getSelectedRow()).getCesta());
        }
        refreshOdosielaneInzeratyTable(preposielane);
    }//GEN-LAST:event_historiaTableMouseReleased

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PreposlatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreposlatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreposlatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreposlatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PreposlatForm dialog = new PreposlatForm(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox emailyComboBox;
    private javax.swing.JTable historiaTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton nastavitButton;
    private javax.swing.JTable odosielaneTable;
    private javax.swing.JLabel odosielatelTextField;
    private javax.swing.JButton odoslatButton;
    private javax.swing.JButton pouziTextButton;
    private javax.swing.JTextField predmetTextField;
    private javax.swing.JTextField prijemcaTextField;
    private javax.swing.JTextArea sprievodnyTextTextArea;
    // End of variables declaration//GEN-END:variables

    private void refreshOdosielaneInzeratyTable(List<Inzerat> inzeraty) {
        System.out.println("refreshOdosielaneInzeratyTable size: " + inzeraty.size());
        int idx = odosielaneTable.getSelectedRow();
        //System.out.println("refresh idx: " + idx);
        odosielaneTableModel = new OdosielaneTableModel(odosielaneTableColumnNames, inzeraty);
        odosielaneTable.setModel(odosielaneTableModel);
        if (inzeraty.size() > 0) {
            // mame inzeraty v tabulke
            // 1. index je -1 --> index bude 0
            // 2. index nie je -1 A  je < size --> index = index
            // 3. index nie je -1 A > size --> index=size-1
            if (idx == -1) {
                idx = 0;
            } else {
                if (idx >= inzeraty.size()) {
                    idx = inzeraty.size() - 1;
                } else {
                    idx = idx;
                }
            }
        } else {
            // prazdna tabulka, idx bude -1 lebo nic nemoze byt selectnute
            idx = -1;
        }
        //System.out.println("nastaveny idx="+idx);
        odosielaneTable.getSelectionModel().setSelectionInterval(idx, idx);
        pocetOznacenych = 0;
        for (Inzerat inz : inzeraty) {
            if (inz.isZaujimavy()) {
                pocetOznacenych++;
            }
        }

        Border headerBorder = UIManager.getBorder("TableHeader.cellBorder");
        JLabel blueLabel = new JLabel("", parent.starHeaderIcon, JLabel.CENTER);
        blueLabel.setBorder(headerBorder);
        TableColumnModel columnModel = odosielaneTable.getColumnModel();
        TableCellRenderer renderer = new JComponentTableCellRenderer();
        TableColumn column0 = columnModel.getColumn(0);
        column0.setHeaderRenderer(renderer);
        column0.setHeaderValue(blueLabel);

        // * column
        odosielaneTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        odosielaneTable.getColumnModel().getColumn(0).setMaxWidth(20);
        // nazov column
        odosielaneTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        // lokalita column
        odosielaneTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        odosielaneTable.getColumnModel().getColumn(2).setMaxWidth(150);
        // datum column
        odosielaneTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        odosielaneTable.getColumnModel().getColumn(3).setMaxWidth(150);

        refreshEditorPane();
    }

    private void refreshEditorPane() {
        String cesta = emailFile.getAbsolutePath();
        loadURL("file:///" + cesta);
    }

    private String getCas(long currentTimeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
        return sdf.format(new Date(currentTimeMillis));
    }

    private void ulozKopiu(String cesta) {
        System.out.println("ukladam: " + cesta);
        try {
            //File f1 = new File(fpath);
            File f2 = new File(cesta);
            InputStream in = new FileInputStream(emailFile);

            //For Append the file.
            //OutputStream out = new FileOutputStream(f2,true);
            //For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            //System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void odosliEmail() {
        System.out.println("username: [" + odosielatelTextField.getText() + "]");
//        final String username = odosielatelTextField.getText();
//        final String password = "navsetkovamstacijedenucet";

        final String username = "";
        final String password = "";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.registratorblockov.sk");
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Nove inzeraty <" + username + ">"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(prijemcaTextField.getText()));
            message.setSubject(predmetTextField.getText());
            //message.setText("Dear Mail Crawler,"
            //      + "\n\n No spam to my email, please!");
            message.setContent(getString(emailFile), "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // refreshujem historiu bud pri spusteni alebo po odoslani nejakeho emailu
    private void refreshHistoriaTable() {
        // nacitame historiu odoslanych emailov z adresara preposlane
        historiaEmailov = new ArrayList<PreposlanyEmail>();
        File preposlaneFolder = new File("preposlane");
        String[] directories = preposlaneFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        // System.out.println("nacitane adresare: " + Arrays.toString(directories));
        for (String dir : directories) {
            // vytahujeme z kazdeho adresara odoslane emaily
            File adresar = new File("preposlane/" + dir);
            String[] subory = adresar.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });
            // System.out.println("nacitane subory pre " + dir + ": " + Arrays.toString(subory));
            // mame subory pre aktualneho prijimatela
            // nahadzeme ich do historie
            for (int j = 0; j < subory.length; j++) {
                PreposlanyEmail pe = new PreposlanyEmail();
                pe.setCesta("preposlane/" + dir + "/" + subory[j]);
                String textDatum=subory[j].split("_")[0]+"_"+subory[j].split("_")[1];
                pe.setSortDatum(getSortDate(textDatum));
                pe.setDatum(subory[j].split("_")[0].substring(0, 5) + " " + subory[j].split("_")[1].substring(0, 5).replaceAll("\\.", ":"));
                pe.setPrijemca(dir);
                pe.setSubject(subory[j].split("_")[2].split("\\.html")[0]);
                //                pe.setInzeraty(null);// z databazy potom
                historiaEmailov.add(pe);
            }
        }
        Collections.sort(historiaEmailov);
        // dokym nie je aktualny email odoslany tak ho pridavame pri kazdom refreshi
        // ked sa stane odoslanym tak uz sa bude nacitavat zo suboru a nebude ho treba pridavat
        if (!aktualnyOdoslany) {
            // pridame do historie sucasne inzeraty
            PreposlanyEmail novy = new PreposlanyEmail();
            novy.setDatum("AKTUÁLNY");
            novy.setPrijemca(prijemcaTextField.getText());
            novy.setSubject(aktualnyPredmet);
            novy.getInzeraty().addAll(aktualnePreposielane);
            historiaEmailov.add(novy);
            // aktualnyOdoslany = true;
        }

        Collections.reverse(historiaEmailov);
//        for (PreposlanyEmail pe:historiaEmailov){
//            System.out.println(pe.getSortDatum());
//        }
        // mame zoznam vsetkych emailov teraz iba ziskame z databazy info o inzeratoch
        List<Inzerat> preposlane = database.getInzeratyPreposlane();
        // kazdy inzerat priradime do jeho emailu
        for (Inzerat inz : preposlane) {
            // najdeme email do ktoreho patri
            for (PreposlanyEmail p : historiaEmailov) {
                if (p.getCesta().equals(inz.getCesta())) {
                    p.getInzeraty().add(inz);
                    break;
                }
            }
        }

        prekresliHistoriaTable();
    }

    private void prekresliHistoriaTable() {
        //        for (PreposlanyEmail p:historiaEmailov){
//            System.out.println("preposlany email: "+p.getCesta().split("/")[2]+" inzeratov: "+p.getDatum() );
//        }
        int idx = historiaTable.getSelectedRow();
        //System.out.println("refresh idx: " + idx);
        historiaTableModel = new HistoriaTableModel(historiaTableColumnNames, historiaEmailov);
        historiaTable.setModel(historiaTableModel);
        if (historiaEmailov.size() > 0) {
            // mame inzeraty v tabulke
            // 1. index je -1 --> index bude 0
            // 2. index nie je -1 A  je < size --> index = index
            // 3. index nie je -1 A > size --> index=size-1
            if (idx == -1) {
                idx = 0;
            } else {
                if (idx >= historiaEmailov.size()) {
                    idx = historiaEmailov.size() - 1;
                } else {
                    idx = idx;
                }
            }
        } else {
            // prazdna tabulka, idx bude -1 lebo nic nemoze byt selectnute
            idx = -1;
        }
        //System.out.println("nastaveny idx="+idx);
        historiaTable.getSelectionModel().setSelectionInterval(idx, idx);
        // tu treba zobrazit inzeraty pre selectnuty email z historie
        historiaTableMouseReleased(null);

        // * column
        historiaTable.getColumnModel().getColumn(0).setPreferredWidth(77);
        historiaTable.getColumnModel().getColumn(0).setMaxWidth(77);
        // nazov column
        historiaTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        historiaTable.getColumnModel().getColumn(1).setMaxWidth(100);
        // lokalita column
        historiaTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        historiaTable.getColumnModel().getColumn(2).setMaxWidth(200);
        // datum column
//        historiaTable.getColumnModel().getColumn(3).setPreferredWidth(70);
//        historiaTable.getColumnModel().getColumn(3).setMaxWidth(150);
    }

    private String getString(File emailFile) {
        BufferedReader f = null;
        StringBuilder sb = new StringBuilder();
        try {
            f = new BufferedReader(new InputStreamReader(new FileInputStream(emailFile), "UTF8"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PreposlatForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PreposlatForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            StringTokenizer st = null;
            try {
                String line = f.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line + "\n");
            } catch (Exception ex) {
                Logger.getLogger(PreposlatForm.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        return sb.toString();
    }

    private void checkEmailExistuje(String emailAddress) {
//        try {
//            Hashtable env = new Hashtable();
//            env.put("java.naming.factory.initial",
//                    "com.sun.jndi.dns.DnsContextFactory");
//            DirContext ictx = new InitialDirContext(env);
//            Attributes attrs = ictx.getAttributes(emailAddress, new String[]{"MX"});
//            Attribute attr = attrs.get("MX");
//            if (attr == null) {
//                System.out.println("attr is null");
//                return;
//            }
//            System.out.println("attrsize: "+attr.size()+" "+attr);
//        } catch (NamingException ex) {
//            Logger.getLogger(PreposlatForm.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void odosliEmailPHP() {
                System.out.println("jcrawler: odosielam email PHP");
        try {
            Connection.Response loginForm = Jsoup.connect("http://www.jmurin.sk/manazer/emailForm.html")
                    .method(Connection.Method.GET)
                    .execute();

            Document document = Jsoup.connect("http://www.jmurin.sk/manazer/send_email2.php")
                    .data("sender", "no-reply.inzeraty@jmurin.sk")
                    .data("recipient", prijemcaTextField.getText())
                    .data("message", getString(emailFile))
                    .data("subject", predmetTextField.getText())
                    .data("submit", "Submit")
                    .cookies(loginForm.cookies())
                    .post();
            System.out.println(document);
        } catch (IOException e) {
            System.out.println("odosliEmailPHP exception: " + e);
        }
    }

    private Date getSortDate(String textDatum) {
        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
        try {
            return sdf.parse(textDatum);
        } catch (ParseException ex) {
            Logger.getLogger(PreposlatForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date(0);
    }

    class JComponentTableCellRenderer implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return (JComponent) value;
        }
    }

    private void createScene() {

        Platform.runLater(new Runnable() {
            private Scene scene;
            Number width = 1000;
            Number height = 2000;

            @Override
            public void run() {

                view = new WebView();
                //view.setPrefSize(1000, 2000);
                engine = view.getEngine();

                engine.titleProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                //MainForm2.this.setTitle("Manažér realitných inzerátov: " + aktualnyInzerat.getNazov());
                            }
                        });
                    }
                });

                engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(final WebEvent<String> event) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                //lblStatus.setText(event.getData());
                            }
                        });
                    }
                });

                engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                // progressBar.setValue(newValue.intValue());
                            }
                        });
                    }
                });

                engine.getLoadWorker()
                        .exceptionProperty()
                        .addListener(new ChangeListener<Throwable>() {

                            public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                                if (engine.getLoadWorker().getState() == FAILED) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            JOptionPane.showMessageDialog(
                                                    panel,
                                                    (value != null)
                                                            ? engine.getLocation() + "\n" + value.getMessage()
                                                            : engine.getLocation() + "\nUnexpected error.",
                                                    "Loading error...",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                                }
                            }
                        });

                scene = new Scene(view);
                jfxPanel.setScene(scene);

//                scene=new Scene(webViewScroll);
//                scene.widthProperty().addListener(new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                        System.out.println("Width: " + newSceneWidth);
//                        width=newSceneWidth;
//                        //view.setPrefSize(width.doubleValue(), height.doubleValue());
//                    }
//                });
//                scene.heightProperty().addListener(new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                        System.out.println("Height: " + newSceneHeight);
//                        height=newSceneHeight;
//                        //view.setPrefSize(width.doubleValue(), height.doubleValue());
//                    }
//                });
//
//                webViewScroll.setContent(view);
//                jfxPanel.setScene(scene);
            }
        });
    }

    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String tmp = toURL(url);

                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }

                engine.load(tmp);
            }
        });
    }

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }

}
