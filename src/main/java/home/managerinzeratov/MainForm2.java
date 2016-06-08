/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.MestaListModel;
import entity.Kategoria;
import entity.Typ;
import entity.PortalyComboboxModel;
import entity.InzeratyTableModel;
import entity.Inzerat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static javafx.concurrent.Worker.State.FAILED;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Janco1
 */
public class MainForm2 extends javax.swing.JFrame implements PropertyChangeListener {

    public Image img;
    public ImageIcon signedStar;
    public ImageIcon unsignedStar;
    public ImageIcon signedStarPreposlana;
    private ImageIcon unsignedStarPreposlana;
    private ImageIcon vytlacene;
    private ImageIcon nevytlacene;
    public ImageIcon starHeaderIcon;
    private ImageIcon printHeaderIcon;
    private ImageIcon warningIcon;
    private Database database;
    private String[] inzeratyTableColumnNames = {"*", "názov", "T", "meno", "lokalita", "dátum"};
    private InzeratyTableModel inzeratyTableModel;
    private List<String> vsetkyMesta;
    private List<String> vybraneMesta;
    private List<String> keywords;
    private List<String> portaly;
    private List<Inzerat> inzeraty;
    private List<Inzerat> kategoriaInzeraty;
    private List<Inzerat> mestoInzeraty;
    private List<Inzerat> keywordsInzeraty;
    private ArrayList<Inzerat> tableInzeraty; //toggleInzeraty
    private List<Inzerat> surneInzeraty;
    private List<Inzerat> archivovaneInzeraty;
    private ArrayList<Inzerat> preposlavaneInzeraty;
    private ComboBoxModel<String> kategoriaComboboxModel = new DefaultComboBoxModel<String>(Kategoria.values);
    private int[] keywordsCounts;
    private Color bckgTableColor = Color.white;
    int aktRow;
    private WebView view;
    private final JFXPanel jfxPanel = new JFXPanel();
    ScrollPane webViewScroll = new ScrollPane();
    private WebEngine engine;
    final Button capture = new Button("Capture");
    private final JLabel statusLabel = new JLabel();
    private final JLabel kreditLabel = new JLabel();
    private final JProgressBar progressBar = new JProgressBar();
    JPanel statusBar = new JPanel(new BorderLayout(5, 0));
    private final JPanel panel = new JPanel(new BorderLayout());
    private boolean keySelectedRow;
    private Inzerat aktualnyInzerat;
    //private JLayeredPane lpane = new JLayeredPane();
    // private final JPanel panel_pred = new JPanel(new BorderLayout());
    private List<String> archivovaneInzeratyLinky;
    private boolean vyhladavanie;
    private String hladane = "";
    private JsoupCrawler jcrawler = new JsoupCrawler();
    private String hostname;
    private long casPoslednejAktualizacie = System.currentTimeMillis();
    Timer timer = new Timer(System.currentTimeMillis());
    ExecutorService es = Executors.newCachedThreadPool();
    private boolean prebiehaAktualizacia;
    private Aktualizator aktualizator;
    private boolean inzeratDownloadedNeklikat;
    private Archivator archivator = new Archivator();
    private SpustacDatabazy spustac = new SpustacDatabazy();
    private Process process;
    private boolean mozemAktualizovat = true;
    private String predmet;

    private boolean zablokovaneUpdaty = false;

    /**
     * Creates new form MainForm2
     */
    public MainForm2() {
        //SpustacDatabazy.execute();
        //es.execute(spustac);
        try {
            process = Runtime.getRuntime().exec("java -cp lib/hsqldb.jar org.hsqldb.Server -database.1 db/inzeratydb -dbname.1 inzeratydb -port 1235");
        } catch (Exception ex) {
            Logger.getLogger(MainForm2.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            img = ImageIO.read(new File("backg.jpg"));
            signedStar = new ImageIcon("pom/signedStar.png");
            unsignedStar = new ImageIcon("pom/unsignedStar.png");
            signedStarPreposlana = new ImageIcon("pom/signedStarPreposlana.png");
            unsignedStarPreposlana = new ImageIcon("pom/unsignedStarPreposlana.png");
            vytlacene = new ImageIcon("pom/vytlacene.png");
            nevytlacene = new ImageIcon("pom/nevytlacene.png");
            starHeaderIcon = new ImageIcon("pom/theader2.png");
            printHeaderIcon = new ImageIcon("pom/theader3.png");
            warningIcon = new ImageIcon("pom/warning3.png");
        } catch (IOException ex) {
            Logger.getLogger(MainForm2.class.getName()).log(Level.SEVERE, null, ex);
        }
        JPanel progresPanel = new JPanel(new BorderLayout(5, 0));
        progresPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        statusBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        statusLabel.setText("lblStatus");
        statusLabel.setPreferredSize(new Dimension(150, 18));
        statusLabel.setOpaque(true);
        //statusLabel.setBackground(Color.cyan);
        kreditLabel.setText(" Kredit: 0 dní");
        kreditLabel.setPreferredSize(new Dimension(100, 18));
        kreditLabel.setOpaque(true);
        //kreditLabel.setBackground();
        progresPanel.add(progressBar, BorderLayout.WEST);
        progresPanel.add(kreditLabel, BorderLayout.EAST);
        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.add(progresPanel, BorderLayout.EAST);
        initComponents();
        pripomienkaButton.setIcon(warningIcon);
        preposlatButton.setIcon(signedStar);

        setLocationRelativeTo(null);

        // nastav portal, spinner a kategoria
        database = new Database();
        aktualizator = new Aktualizator();
        aktualizator.database = database;
        aktualizator.addPropertyChangeListener(this);
        aktualizator.component = this;
        archivator.database = database;
        database.addPropertyChangeListener(this);
        archivovaneInzeratyLinky = database.getArchivovaneInzeratyLinky();
        archivovaneInzeraty = database.getArchivInzeratyList();
        portaly = new ArrayList<String>();
        portaly.add("všetky");
        portaly.addAll(database.getPortalyNames());
        portalComboBox.setModel(new PortalyComboboxModel(portaly));
        pocetPovolenychInzeratovUserSpinner.setModel(new SpinnerNumberModel(10, 1, 10000, 1));
        surneInzeraty = database.getSurneInzeraty();
        vybraneMesta = new ArrayList<String>();
        vybraneMesta.add("všetky (0)");
        vybraneMesta.addAll(database.getVybraneMesta());
        vybraneMesta.add("+ ...");
        kategoriaComboBox.setModel(kategoriaComboboxModel);
        // load inzeraty
        portalComboBox.setSelectedIndex(0); // spustia sa vsetky refreshe

        //inzeratyTable.setTableHeader(new TableHeaderUI);
        inzeratyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                InzeratyTableModel model = (InzeratyTableModel) table.getModel();
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(bckgTableColor);
                Inzerat aktualny = tableInzeraty.get(row);

                // Only for specific cell
                if (isSelected) {
                    c.setFont(new Font("Calibri", 0, 18));
                    // you may want to address isSelected here too
                    c.setForeground(Color.BLACK);
                    //c.setBackground(/*special background color*/);
                    c.setBackground(new Color(199, 223, 252));
                }
                if (model.isPrecitany(row)) {
                    c.setFont(new Font("Calibri", 0, 18));
                } else {
                    c.setFont(new Font("Calibri", 1, 18));
                }
                if (column == 0) {
                    // klikli sme na hviezdicku
                    // TODO: osetrit vynimky ked zlyha databaza, aby sa vedelo hned ked sa nieco nepodari ulozit
                    JLabel novy = new JLabel();
                    novy.setOpaque(true);
                    if (hasFocus && !keySelectedRow) {
                        if (!aktualny.isPreposlany() && aktualny.isZaujimavy()) {
                            // stane sa nezaujimavym a preto  ho vyradime zo zoznamu preposlavanych
                            preposlavaneInzeraty.remove(aktualny);
                            refreshPreposlatButtonText();
                        }

                        //System.out.println("mame focus");
                        aktualny.setZaujimavy(!aktualny.isZaujimavy());
                        database.updateInzerat(aktualny);

                        // ak nie je preposlany a je oznaceny tak ho pridame do zoznamu na preposlanie
                        if (!aktualny.isPreposlany() && aktualny.isZaujimavy()) {
                            // stal sa zaujimavym
                            preposlavaneInzeraty.add(aktualny);
                            refreshPreposlatButtonText();
                        }
                    }

                    if (aktualny.isZaujimavy()) {
                        if (aktualny.isPreposlany()) {
                            novy.setIcon(signedStarPreposlana);
                        } else {
                            novy.setIcon(signedStar);
                        }
                    } else {
                        if (aktualny.isPreposlany()) {
                            novy.setIcon(unsignedStarPreposlana);
                        } else {
                            novy.setIcon(unsignedStar);
                        }
                    }
                    novy.setBackground(bckgTableColor);
                    if (isSelected) {
                        novy.setBackground(new Color(199, 223, 252));
                    }
                    return novy;
                }
                if (column == 2) {
                    JLabel novy = new JLabel();
                    novy.setOpaque(true);
                    if (aktualny.isOdoslany()) {
                        novy.setIcon(vytlacene);
                    } else {
                        novy.setIcon(nevytlacene);
                    }
                    novy.setBackground(bckgTableColor);
                    if (isSelected) {
                        novy.setBackground(new Color(199, 223, 252));
                    }
                    return novy;
                }
                return c;
            }

        });

        timer.setStop(false);
        timer.addPropertyChangeListener(this);
        es.execute(timer);
        // spustime autentifikaciu a stiahneme najnovsie inzeraty
        loadHostName();
        String kluc = loadKluc();
        aktualizator.hostname = hostname;
        aktualizator.kluc = kluc;
        prebiehaAktualizacia = true;
        es.execute(aktualizator);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("window closing action");
                //database.shutdown();
                process.destroy();
                super.windowClosing(e);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pocetPovolenychInzeratovUserSpinner = new javax.swing.JSpinner();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel(){
            protected void paintComponent(Graphics g)
            {
                g.drawImage(img, 0, 0, null);
                super.paintComponent(g);
            }
        }
        ;
        mestaTabbedPane = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        vsetkyMestaList = new javax.swing.JList();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        vybraneMestaList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        predamCheckBox = new javax.swing.JCheckBox();
        kupimCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        kategoriaComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        keywordsList = new javax.swing.JList();
        pripomienkaButton = new javax.swing.JButton();
        portalComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        inzeratyTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        precitaneToggleButton = new javax.swing.JToggleButton();
        neprecitaneToggleButton = new javax.swing.JToggleButton();
        oznaceneToggleButton = new javax.swing.JToggleButton();
        hladatTextField = new javax.swing.JTextField();
        nevytlaceneToggleButton = new javax.swing.JToggleButton();
        archivovaneToggleButton = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        vytlacitButton = new javax.swing.JButton();
        archivovatButton = new javax.swing.JButton();
        preposlatButton = new javax.swing.JButton();

        pocetPovolenychInzeratovUserSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                pocetPovolenychInzeratovUserSpinnerStateChanged(evt);
            }
        });
        pocetPovolenychInzeratovUserSpinner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pocetPovolenychInzeratovUserSpinnerMouseReleased(evt);
            }
        });
        pocetPovolenychInzeratovUserSpinner.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pocetPovolenychInzeratovUserSpinnerPropertyChange(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manažér realitných inzerátov");
        setPreferredSize(new java.awt.Dimension(1100, 737));

        jSplitPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSplitPane1.setDividerLocation(190);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setLastDividerLocation(170);

        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(30, 247));

        mestaTabbedPane.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        mestaTabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mestaTabbedPaneMouseClicked(evt);
            }
        });

        vsetkyMestaList.setFont(new java.awt.Font("Arial Narrow", 0, 13)); // NOI18N
        vsetkyMestaList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        vsetkyMestaList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        vsetkyMestaList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vsetkyMestaListMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(vsetkyMestaList);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );

        mestaTabbedPane.addTab("Všetky", jPanel5);

        vybraneMestaList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        vybraneMestaList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        vybraneMestaList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vybraneMestaListMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(vybraneMestaList);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );

        mestaTabbedPane.addTab("Vybrané", jPanel6);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" Mestá:");

        predamCheckBox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        predamCheckBox.setForeground(new java.awt.Color(255, 255, 204));
        predamCheckBox.setSelected(true);
        predamCheckBox.setText("Predám");
        predamCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                predamCheckBoxActionPerformed(evt);
            }
        });

        kupimCheckBox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        kupimCheckBox.setForeground(new java.awt.Color(255, 255, 204));
        kupimCheckBox.setSelected(true);
        kupimCheckBox.setText("Kúpim");
        kupimCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kupimCheckBoxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(" Kategória:");

        kategoriaComboBox.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        kategoriaComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        kategoriaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kategoriaComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(" Typ:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText(" Kľúčové slová:");

        keywordsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        keywordsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        keywordsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keywordsListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(keywordsList);

        pripomienkaButton.setText("Odoslať pripomienku");
        pripomienkaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pripomienkaButtonActionPerformed(evt);
            }
        });

        portalComboBox.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        portalComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        portalComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portalComboBoxActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText(" Portál:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mestaTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kategoriaComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(portalComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(predamCheckBox)
                            .addComponent(kupimCheckBox)))
                    .addComponent(jLabel4)
                    .addComponent(pripomienkaButton)
                    .addComponent(jLabel5))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(9, 9, 9)
                .addComponent(portalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(predamCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kupimCheckBox)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kategoriaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mestaTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addComponent(pripomienkaButton))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jSplitPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSplitPane2.setDividerLocation(350);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel3.setLayout(new java.awt.BorderLayout());

        inzeratyTable.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        inzeratyTable.setModel(new javax.swing.table.DefaultTableModel(
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
        inzeratyTable.setOpaque(false);
        inzeratyTable.setRowHeight(22);
        inzeratyTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        inzeratyTable.setShowHorizontalLines(false);
        inzeratyTable.setShowVerticalLines(false);
        inzeratyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inzeratyTableMouseClicked(evt);
            }
        });
        inzeratyTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inzeratyTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inzeratyTableKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(inzeratyTable);

        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(0, 102, 255));

        precitaneToggleButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        precitaneToggleButton.setText("prečítané");
        precitaneToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precitaneToggleButtonActionPerformed(evt);
            }
        });

        neprecitaneToggleButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        neprecitaneToggleButton.setText("neprečítané");
        neprecitaneToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neprecitaneToggleButtonActionPerformed(evt);
            }
        });

        oznaceneToggleButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        oznaceneToggleButton.setText("označené");
        oznaceneToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oznaceneToggleButtonActionPerformed(evt);
            }
        });

        hladatTextField.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        hladatTextField.setForeground(new java.awt.Color(204, 204, 204));
        hladatTextField.setText("hľadať");
        hladatTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hladatTextFieldMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hladatTextFieldMousePressed(evt);
            }
        });
        hladatTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hladatTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hladatTextFieldKeyReleased(evt);
            }
        });

        nevytlaceneToggleButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        nevytlaceneToggleButton.setText("nevytlačené");
        nevytlaceneToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nevytlaceneToggleButtonActionPerformed(evt);
            }
        });

        archivovaneToggleButton.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        archivovaneToggleButton.setText("archivované");
        archivovaneToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archivovaneToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(precitaneToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(neprecitaneToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oznaceneToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nevytlaceneToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(archivovaneToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hladatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(precitaneToggleButton)
                .addComponent(neprecitaneToggleButton)
                .addComponent(oznaceneToggleButton)
                .addComponent(hladatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(nevytlaceneToggleButton)
                .addComponent(archivovaneToggleButton))
        );

        jPanel3.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(0, 102, 255));

        vytlacitButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        vytlacitButton.setText("Vytlačiť");
        vytlacitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vytlacitButtonActionPerformed(evt);
            }
        });

        archivovatButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        archivovatButton.setText("Archivovať");
        archivovatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archivovatButtonActionPerformed(evt);
            }
        });

        preposlatButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        preposlatButton.setText("Preposlať ");
        preposlatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preposlatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(preposlatButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 473, Short.MAX_VALUE)
                .addComponent(archivovatButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vytlacitButton)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vytlacitButton)
                    .addComponent(archivovatButton)
                    .addComponent(preposlatButton)))
        );

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jSplitPane2.setTopComponent(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
        );

        createScene();
        panel.add(jfxPanel, BorderLayout.CENTER);
        jSplitPane2.setBottomComponent(panel);

        jSplitPane1.setRightComponent(jPanel1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);
        getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void portalComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portalComboBoxActionPerformed
        System.out.println("portalComboBoxActionPerformed");
        String aktPortal = (String) portalComboBox.getSelectedItem();
        int aktPocet = (Integer) pocetPovolenychInzeratovUserSpinner.getValue();
        inzeraty = database.getInzeratyList(aktPortal, aktPocet);
        Collections.sort(inzeraty);
        Collections.reverse(inzeraty);
        System.out.println("nacitane inzeraty z portalu: " + inzeraty.size());
        refreshMestaList();
    }//GEN-LAST:event_portalComboBoxActionPerformed

    private void pocetPovolenychInzeratovUserSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_pocetPovolenychInzeratovUserSpinnerStateChanged
        System.out.println("pocetPovolenychInzeratovUserSpinnerStateChanged");
        String aktPortal = (String) portalComboBox.getSelectedItem();
        int aktPocet = (Integer) pocetPovolenychInzeratovUserSpinner.getValue();
        inzeraty = database.getInzeratyList(aktPortal, aktPocet);
        Collections.sort(inzeraty);
        Collections.reverse(inzeraty);
        System.out.println("nacitane inzeraty zo spinneru: " + inzeraty.size());
        refreshMestaList();
    }//GEN-LAST:event_pocetPovolenychInzeratovUserSpinnerStateChanged

    private void pocetPovolenychInzeratovUserSpinnerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pocetPovolenychInzeratovUserSpinnerMouseReleased

    }//GEN-LAST:event_pocetPovolenychInzeratovUserSpinnerMouseReleased

    private void pocetPovolenychInzeratovUserSpinnerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pocetPovolenychInzeratovUserSpinnerPropertyChange

    }//GEN-LAST:event_pocetPovolenychInzeratovUserSpinnerPropertyChange

    private void predamCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_predamCheckBoxActionPerformed
        System.out.println("predamCheckBoxActionPerformed");
        if (!predamCheckBox.isSelected() && !kupimCheckBox.isSelected()) {
            kupimCheckBox.setSelected(true);
        }
        refreshMestaList();
    }//GEN-LAST:event_predamCheckBoxActionPerformed

    private void kupimCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kupimCheckBoxActionPerformed
        System.out.println("kupimCheckBoxActionPerformed");
        if (!predamCheckBox.isSelected() && !kupimCheckBox.isSelected()) {
            predamCheckBox.setSelected(true);
        }
        refreshMestaList();
    }//GEN-LAST:event_kupimCheckBoxActionPerformed

    private void kategoriaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kategoriaComboBoxActionPerformed
        refreshMestaList();
    }//GEN-LAST:event_kategoriaComboBoxActionPerformed

    private void vsetkyMestaListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vsetkyMestaListMouseClicked
        System.out.println("vsetkyMestaListMouseClicked");
        refreshKeywords(false);
    }//GEN-LAST:event_vsetkyMestaListMouseClicked

    private void vybraneMestaListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vybraneMestaListMouseClicked
        System.out.println("vybraneMestaListMouseClicked");
        if (vybraneMestaList.getSelectedIndex() == vybraneMesta.size() - 1) {
            // pridavame vybrane mesta
            System.out.println("pridat vybrane mesto do zoznamu");
            PridatVybraneForm pvf = new PridatVybraneForm(this, true, vsetkyMesta, vybraneMesta, database);
            pvf.addPropertyChangeListener(this);
            pvf.setVisible(true);
        } else {
            // nepridavame, chceme zobrazit inzeraty z vybranych miest
            refreshKeywords(true);
        }
    }//GEN-LAST:event_vybraneMestaListMouseClicked

    private void mestaTabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mestaTabbedPaneMouseClicked
        if (mestaTabbedPane.getSelectedIndex() == 0) {
            vsetkyMestaListMouseClicked(evt);
        }
        if (mestaTabbedPane.getSelectedIndex() == 1) {
            vybraneMestaListMouseClicked(evt);
        }
    }//GEN-LAST:event_mestaTabbedPaneMouseClicked

    private void keywordsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_keywordsListMouseClicked
        refreshInzeratyWithKeywords();
    }//GEN-LAST:event_keywordsListMouseClicked

    private void precitaneToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precitaneToggleButtonActionPerformed
        System.out.println("precitaneToggleButtonActionPerformed");
        if (archivovaneToggleButton.isSelected()) {
            refreshTableWithToggleButtons(archivovaneInzeraty);
        } else {
            refreshTableWithToggleButtons(keywordsInzeraty);
        }
    }//GEN-LAST:event_precitaneToggleButtonActionPerformed

    private void neprecitaneToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neprecitaneToggleButtonActionPerformed
        System.out.println("neprecitaneToggleButtonActionPerformed");
        if (archivovaneToggleButton.isSelected()) {
            refreshTableWithToggleButtons(archivovaneInzeraty);
        } else {
            refreshTableWithToggleButtons(keywordsInzeraty);
        }
    }//GEN-LAST:event_neprecitaneToggleButtonActionPerformed

    private void oznaceneToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oznaceneToggleButtonActionPerformed
        System.out.println("oznaceneToggleButtonActionPerformed");
        if (archivovaneToggleButton.isSelected()) {
            refreshTableWithToggleButtons(archivovaneInzeraty);
        } else {
            refreshTableWithToggleButtons(keywordsInzeraty);
        }
    }//GEN-LAST:event_oznaceneToggleButtonActionPerformed

    private void nevytlaceneToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nevytlaceneToggleButtonActionPerformed
        System.out.println("nevytlaceneToggleButtonActionPerformed");
        if (archivovaneToggleButton.isSelected()) {
            refreshTableWithToggleButtons(archivovaneInzeraty);
        } else {
            refreshTableWithToggleButtons(keywordsInzeraty);
        }
    }//GEN-LAST:event_nevytlaceneToggleButtonActionPerformed

    private void inzeratyTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inzeratyTableMouseClicked
        System.out.println("inzeratyTableMouseClicked");
        //refreshInzeratDisplayed();
        // musim refreshnut celu tabulku aby sa mi okamzite zafarbila hviezdicka alebo nazov prcitaneho inzeratu
        refreshInzeratyTable(tableInzeraty);
    }//GEN-LAST:event_inzeratyTableMouseClicked

    private void inzeratyTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inzeratyTableKeyReleased
        System.out.println("inzeratyTableKeyReleased");
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                inzeratyTableMouseClicked(null);
                keySelectedRow = false;
                break;
            case KeyEvent.VK_UP:
                inzeratyTableMouseClicked(null);
                keySelectedRow = false;
                break;
            default:
                break;
        }
    }//GEN-LAST:event_inzeratyTableKeyReleased

    private void inzeratyTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inzeratyTableKeyPressed
        //System.out.println("inzeratyTableKeyPressed");
        keySelectedRow = true;
    }//GEN-LAST:event_inzeratyTableKeyPressed

    private void archivovatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archivovatButtonActionPerformed
        System.out.println("archivovatButtonActionPerformed: " + aktualnyInzerat);
        database.inzertArchivInzerat(aktualnyInzerat);
        List<Inzerat> nove = new ArrayList<Inzerat>();
        nove.add(aktualnyInzerat);
        nove.addAll(archivovaneInzeraty);
        archivovaneInzeraty = nove;
        archivovaneInzeratyLinky.add(aktualnyInzerat.getAktualny_link());
        archivovatButton.setEnabled(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                capture.fire();
            }
        });
//        WritableImage image = view.snapshot(null, null);
//        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
//        try {
//            File captureFile = new File("obrazok");
//            ImageIO.write(bufferedImage, "png", captureFile);
//            System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }//GEN-LAST:event_archivovatButtonActionPerformed

    private void archivovaneToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archivovaneToggleButtonActionPerformed
        System.out.println("archivovaneToggleButtonActionPerformed");
        if (archivovaneToggleButton.isSelected()) {
            preposlatButton.setEnabled(false);
            refreshTableWithToggleButtons(archivovaneInzeraty);
        } else {
            preposlatButton.setEnabled(true);
            refreshTableWithToggleButtons(keywordsInzeraty);
        }
    }//GEN-LAST:event_archivovaneToggleButtonActionPerformed

    private void vytlacitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vytlacitButtonActionPerformed
        //   try {
//            File subor = InzeratViewGenerator.getIzeratViewFileTlac(aktualnyInzerat);
//            String cesta = "file:///" + subor.getAbsolutePath();
//            System.out.println("cesta: " + cesta);
//            JEditorPane temp = new JEditorPane();
//            MediaPrintableArea mpa = new MediaPrintableArea(0, 0, 200, 275, MediaPrintableArea.MM);
//// in above line u can set paper size and margin
//            HashPrintRequestAttributeSet hpras = new HashPrintRequestAttributeSet(mpa);
//            hpras.add(mpa);
//            hpras.add(new JobName(aktualnyInzerat.getNazov(), Locale.ENGLISH));
//
//            // add attribute to PrintRequestAttributeSet
//            temp.setPage(cesta);
//            temp.print(null, null, true, null, hpras, false);
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            //job.showPageSetupDialog(null);
            job.showPrintDialog(null);
            job.getJobSettings().setJobName(aktualnyInzerat.getNazov());
            engine.print(job);

            //engine.getHistory().getEntries().get(0).
            job.endJob();
        }
//        WritableImage image = view.snapshot(null, null);
//        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
//        try {
//            File captureFile=new File("obrazok");
//            ImageIO.write(bufferedImage, "png", captureFile);
//            System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        aktualnyInzerat.setOdoslany(true);
        database.updateInzerat(aktualnyInzerat);
        if (!archivovaneInzeratyLinky.contains(aktualnyInzerat.getAktualny_link())) {
            System.out.println("inzerat este nie je archivovany");
            archivovatButtonActionPerformed(evt);
//            List<Inzerat> nove = new ArrayList<Inzerat>();
//            nove.add(aktualnyInzerat);
//            nove.addAll(archivovaneInzeraty);
//            archivovaneInzeraty = nove;
//            archivovaneInzeratyLinky.add(aktualnyInzerat.getAktualny_link());
//            archivovatButton.setEnabled(false);
//            database.inzertArchivInzerat(aktualnyInzerat);
        } else {
            System.out.println("inzerat uz je archivovany");
        }
//        } catch (IOException ex) {
//            Logger.getLogger(MainForm2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (PrinterException ex) {
//            Logger.getLogger(MainForm2.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }//GEN-LAST:event_vytlacitButtonActionPerformed

    private void hladatTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hladatTextFieldKeyPressed
        if (hladatTextField.getText().contains("hľadať")) {
            hladatTextField.setText("");
            hladatTextField.setFont(new Font("Tahoma", 0, 13));
            hladatTextField.setForeground(Color.black);
            vyhladavanie = true;
        }
    }//GEN-LAST:event_hladatTextFieldKeyPressed

    private void hladatTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hladatTextFieldKeyReleased
        hladane = hladatTextField.getText();

        if (hladane.length() > 0) {
            // vyhladavanie je nieco ako specialny toggle button
            // hladame bud nad archivovanymy alebo keyword inzeratmi
            if (archivovaneToggleButton.isSelected()) {
                refreshTableWithToggleButtons(archivovaneInzeraty);
            } else {
                refreshTableWithToggleButtons(keywordsInzeraty);
            }
        } else {
            hladatTextField.setText("hľadať");
            hladatTextField.setCaretPosition(0);
            hladatTextField.setFont(new Font("Tahoma", 2, 13));
            hladatTextField.setForeground(Color.gray);
            vyhladavanie = false;
            if (archivovaneToggleButton.isSelected()) {
                refreshTableWithToggleButtons(archivovaneInzeraty);
            } else {
                refreshTableWithToggleButtons(keywordsInzeraty);
            }
        }
    }//GEN-LAST:event_hladatTextFieldKeyReleased

    private void hladatTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hladatTextFieldMouseClicked

    }//GEN-LAST:event_hladatTextFieldMouseClicked

    private void hladatTextFieldMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hladatTextFieldMousePressed
        if (hladatTextField.getText().contains("hľadať")) {
            hladatTextField.setCaretPosition(0);
        }
    }//GEN-LAST:event_hladatTextFieldMousePressed

    private void pripomienkaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pripomienkaButtonActionPerformed
        PripomienkaForm pf = new PripomienkaForm(this, true, hostname);
        pf.setVisible(true);
    }//GEN-LAST:event_pripomienkaButtonActionPerformed

    private void preposlatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preposlatButtonActionPerformed
        System.out.println("preposlatButtonActionPerformed");

        // vsetky co su oznacene a nie su este odoslane odovzdam do PreposlatFormu
        PreposlatForm pf = new PreposlatForm(this, true, preposlavaneInzeraty, database, predmet);
        pf.addPropertyChangeListener(this);
        // zakazeme updaty dokym sa neodosle email
        mozemAktualizovat = false;
        pf.setVisible(true);
    }//GEN-LAST:event_preposlatButtonActionPerformed

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
            java.util.logging.Logger.getLogger(MainForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton archivovaneToggleButton;
    private javax.swing.JButton archivovatButton;
    private javax.swing.JTextField hladatTextField;
    private javax.swing.JTable inzeratyTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JComboBox kategoriaComboBox;
    private javax.swing.JList keywordsList;
    private javax.swing.JCheckBox kupimCheckBox;
    private javax.swing.JTabbedPane mestaTabbedPane;
    private javax.swing.JToggleButton neprecitaneToggleButton;
    private javax.swing.JToggleButton nevytlaceneToggleButton;
    private javax.swing.JToggleButton oznaceneToggleButton;
    private javax.swing.JSpinner pocetPovolenychInzeratovUserSpinner;
    private javax.swing.JComboBox portalComboBox;
    private javax.swing.JToggleButton precitaneToggleButton;
    private javax.swing.JCheckBox predamCheckBox;
    private javax.swing.JButton preposlatButton;
    private javax.swing.JButton pripomienkaButton;
    private javax.swing.JList vsetkyMestaList;
    private javax.swing.JList vybraneMestaList;
    private javax.swing.JButton vytlacitButton;
    // End of variables declaration//GEN-END:variables

    private void refreshMestaList() {
        System.out.println("refreshMestaList: predam=" + predamCheckBox.isSelected() + " kupim=" + kupimCheckBox.isSelected() + " kategoria=" + kategoriaComboBox.getSelectedItem());
        kategoriaInzeraty = new ArrayList<Inzerat>();
        Map<String, Integer> poctyMesta = new HashMap<String, Integer>();
        int pocetOstatne = 0;
        for (Inzerat inz : inzeraty) {
            // TYP: kontrola, iba 2 pripady nechceme
            if (predamCheckBox.isSelected() && !kupimCheckBox.isSelected() && inz.getTyp().equals(Typ.Kúpim + "")) {
                // chceme predam ale mame kupim
                continue;
            }
            if (!predamCheckBox.isSelected() && kupimCheckBox.isSelected() && inz.getTyp().equals(Typ.Predám + "")) {
                // chceme kupim ale mame predam
                continue;
            }
            // KATEGORIA: kontrola, iba jeden pripad, ked nemame vsetky a mame nezhodne kategorie
            String selectedKategoria = (String) kategoriaComboBox.getSelectedItem();
            if (!selectedKategoria.equals(Kategoria.values[0]) && !selectedKategoria.equals(inz.getKategoria())) {
                // neni zaskrtnuta kategoria vsetky a nezhoduje sa zaskrtnuta kategoria s kategoriou inzeratu
                continue;
            }

            kategoriaInzeraty.add(inz);
            inz.setSurne(zistiCiJeSurny(inz));
            // 1. spocitame mesta kolko maju inzeratov
            String mesto = inz.getLokalita().trim();
            if (mesto.equals("Ostatné")) {
                pocetOstatne++;
            } else {
                if (poctyMesta.containsKey(mesto)) {
                    poctyMesta.put(mesto, poctyMesta.get(mesto) + 1);
                } else {
                    poctyMesta.put(mesto, 1);
                }
            }
        }
        // vytiahneme udaje z mapy hodime to do finalneho listu
        vsetkyMesta = new ArrayList<String>();
        for (String m : poctyMesta.keySet()) {
            vsetkyMesta.add(m + " (" + poctyMesta.get(m) + ")");
        }
        Collections.sort(vsetkyMesta);
        int prevSelected = vsetkyMestaList.getSelectedIndex();
        List<String> m = new ArrayList<String>();
        if (poctyMesta.size() == 0) {
            m.add("všetky (0)");
        } else {
            m.add("všetky (" + kategoriaInzeraty.size() + ")");
        }
        m.addAll(vsetkyMesta);
        m.add("Ostatné (" + pocetOstatne + ")");
        vsetkyMesta = m;
        vsetkyMestaList.setModel(new MestaListModel(vsetkyMesta));
        if (prevSelected != -1) {
            vsetkyMestaList.setSelectedIndex(prevSelected);
        } else {
            vsetkyMestaList.setSelectedIndex(0);
        }

        // aktualizovat vybrane mesta list
        prevSelected = vybraneMestaList.getSelectedIndex();
        List<String> noveVybraneMesta = new ArrayList<String>();
        int pocetVybranychInzeratov = 0;
        //System.out.println("vybrane mesta: " + vybraneMesta);
        for (int i = 1; i < vybraneMesta.size() - 1; i++) {
            // nechceme prve mesto lebo tam je ulozene: "vsetky"
            String mesto = vybraneMesta.get(i);
            String aktmesto = mesto.split("\\(")[0].trim();
            int pocet = getPocetPreVybraneMesto(aktmesto);
            noveVybraneMesta.add(aktmesto + " (" + pocet + ")");
            pocetVybranychInzeratov += pocet;
        }
        noveVybraneMesta.add("+ ...");
        vybraneMesta = new ArrayList<String>();
        vybraneMesta.add("všetky (" + pocetVybranychInzeratov + ")");
        vybraneMesta.addAll(noveVybraneMesta);
        vybraneMestaList.setModel(new MestaListModel(vybraneMesta));
        if (prevSelected == -1 || prevSelected == vybraneMesta.size() - 1) {
            vybraneMestaList.setSelectedIndex(0);
        } else {
            if (vybraneMesta.size() > 2) {
                vybraneMestaList.setSelectedIndex(prevSelected);
            } else {
                vybraneMestaList.setSelectedIndex(0);
            }
        }

        // mame aktualizovany list miest a pocet inzeratov za jednotlive mesta
        // ideme aktualizovat keywords
        if (mestaTabbedPane.getSelectedIndex() == 0) {
            vsetkyMestaListMouseClicked(null);
        } else {
            vybraneMestaListMouseClicked(null);
        }
        //refreshKeywords();
    }

    private boolean zistiCiJeSurny(Inzerat hladany) {
        for (Inzerat inz : surneInzeraty) {
            if (inz.getId() == hladany.getId()) {
                return true;
            }
        }
        return false;
    }

    private void refreshKeywords(boolean chcemeVybrane) {
        if (vybraneMestaList.getSelectedValue() == null || vsetkyMestaList.getSelectedValue() == null) {
            System.out.println("vybrane mesto je null. ziaden refresh");
            return;
        }
        keywords = new ArrayList<String>();
        // keywordy inicializacia:
        // 0- vsetko, 1-surne
        keywordsCounts = new int[2];

        // z mestaInzeraty si vyberieme inzeraty jedneho mesta a pocitame keywordy
        mestoInzeraty = new ArrayList<Inzerat>();
        String vybraneMesto = "";
        if (chcemeVybrane) {
            System.out.println("refresh keywords z vybranych miest: " + vybraneMestaList.getSelectedValue());
            vybraneMesto = (String) vybraneMestaList.getSelectedValue();
            vybraneMesto = vybraneMesto.split("\\(")[0].trim();
            if (vybraneMesto.equals("všetky")) {
                predmet = "Všetky vybrané mestá, nové inzeráty";
                // ideme for cyklom
                for (int i = 1; i < vybraneMesta.size() - 1; i++) {
                    String vyb = vybraneMesta.get(i).split("\\(")[0].trim();
                    // System.out.println("zbieram inzeraty pre vybrane mesto: " + vyb);
                    spocitajKeywordCounts(vyb);
                }
            } else {
                predmet = vybraneMesto + ", nové inzeráty";
                // mame mesto ktoreho chceme inzeraty tak prechadzame mestaInzeraty
                spocitajKeywordCounts(vybraneMesto);
            }
        } else {
            System.out.println("refresh keywords zo vsetkych miest: " + vsetkyMestaList.getSelectedValue());
            vybraneMesto = (String) vsetkyMestaList.getSelectedValue();
            vybraneMesto = vybraneMesto.split("\\(")[0].trim();
            if (vybraneMesto.equals("všetky")) {
                predmet = "Všetky mestá, nové inzeráty";
                for (Inzerat inz : kategoriaInzeraty) {
                    mestoInzeraty.add(inz);

                    // teraz pocitame jednotlive keywordy
                    if (inz.isSurne()) {
                        keywordsCounts[1]++;
                    }
                }
            } else {
                predmet = vybraneMesto + ", nové inzeráty";
                // mame mesto ktoreho chceme inzeraty tak prechadzame mestaInzeraty
                spocitajKeywordCounts(vybraneMesto);
            }
        }

        // vygenerujeme keywordList
        keywords = new ArrayList<String>();
        keywords.add("všetko (" + mestoInzeraty.size() + ")");
        keywords.add("súrne (" + keywordsCounts[1] + ")");
        keywordsList.setModel(new MestaListModel(keywords));
        keywordsList.setSelectedIndex(0);
        refreshInzeratyWithKeywords();
    }

    private void spocitajKeywordCounts(String vyb) {
        for (Inzerat inz : kategoriaInzeraty) {
            if (inz.getLokalita().trim().equals(vyb)) {
                mestoInzeraty.add(inz);

                // teraz pocitame jednotlive keywordy
                if (inz.isSurne()) {
                    keywordsCounts[1]++;
                }
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("vybrane_mestaUpdated".equals(evt.getPropertyName())) {
            System.out.println("updatnute vybrane mesta");
            int prevSelected = vybraneMestaList.getSelectedIndex();
            vybraneMesta = new ArrayList<String>();
            vybraneMesta.add("všetky (0)");
            vybraneMesta.addAll(database.getVybraneMesta());
            vybraneMesta.add("+ ...");
            if (prevSelected == -1 || prevSelected > vybraneMesta.size() - 1) {
                vybraneMestaList.setSelectedIndex(0);
            } else {
                if (vybraneMesta.size() > 2) {
                    vybraneMestaList.setSelectedIndex(prevSelected);
                } else {
                    vybraneMestaList.setSelectedIndex(0);
                }
            }
            predamCheckBoxActionPerformed(null);
        }
        if ("secondAdded".equals(evt.getPropertyName())) {
            if (!zablokovaneUpdaty && mozemAktualizovat) {
                if (prebiehaAktualizacia) {
                    //lblStatus.setText("Čas: " + getCas() + lblStatus.getText().substring(10));
                } else {
                    if (System.currentTimeMillis() - casPoslednejAktualizacie > 900000 && !prebiehaAktualizacia) {
                        // ubehlo 15 minut, spustam aktualizaciu
                        //ziskajDBDataAktualizujDB();
                        System.out.println("property change spustam aktualizaciu");
                        prebiehaAktualizacia = true;
                        es.execute(aktualizator);
                    } else {
                        statusLabel.setText(" Status: DB aktualizovaná, najbližšia aktualizácia: " + getETAtime(casPoslednejAktualizacie, System.currentTimeMillis() - casPoslednejAktualizacie, 900000));
                    }
                }
            }
        }
        if ("kreditLabel".equals(evt.getPropertyName())) {
            kreditLabel.setText((String) evt.getNewValue());
        }
        if ("statusLabel".equals(evt.getPropertyName())) {
            statusLabel.setText((String) evt.getNewValue());
        }
        if ("zablokovatUpdaty".equals(evt.getPropertyName())) {
            zablokovaneUpdaty = true;
        }
        if ("aktualizaciaUkoncena".equals(evt.getPropertyName())) {
            prebiehaAktualizacia = false;
            casPoslednejAktualizacie = System.currentTimeMillis();
        }
        if ("preposlatFormEnded".equals(evt.getPropertyName())) {
            System.out.println("preposlatFormEnded action ");
            mozemAktualizovat = true;
            //preposlavaneInzeraty=new ArrayList<Inzerat>(); uz sa to deje v refreshi
            refreshTableWithToggleButtons(keywordsInzeraty);
            System.out.println("preposlavane size: " + preposlavaneInzeraty.size());
//            for (Inzerat inz:tableInzeraty){
//                if 
//            }
        }
        if ("inzeratyDownloaded".equals(evt.getPropertyName())) {
            surneInzeraty = database.getSurneInzeraty();
            Inzerat selectedInzerat = null;
            try {
                selectedInzerat = tableInzeraty.get(inzeratyTable.getSelectedRow());
            } catch (Exception e) {
            }
            inzeratDownloadedNeklikat = true;
            portalComboBoxActionPerformed(null);
            inzeratDownloadedNeklikat = false;
            if (selectedInzerat != null) {
                for (int i = 0; i < tableInzeraty.size(); i++) {
                    if (tableInzeraty.get(i).getAktualny_link().equals(selectedInzerat.getAktualny_link())) {
                        inzeratyTable.getSelectionModel().setSelectionInterval(i, i);
                        inzeratyTableMouseClicked(null);
                        break;
                    }
                }
            }
        }
    }

    private int getPocetPreVybraneMesto(String aktmesto) {
        for (int i = 1; i < vsetkyMesta.size(); i++) {
            String m = vsetkyMesta.get(i).split("\\(")[0].trim();
            if (m.equals(aktmesto)) {
                return Integer.parseInt(vsetkyMesta.get(i).split("\\(")[1].trim().split("\\)")[0]);
            }
        }
        return 0;
    }

    private void refreshInzeratyWithKeywords() {
        String selectedKeyword = (String) keywordsList.getSelectedValue();
        System.out.println("refreshInzeratyWithKeywords: " + selectedKeyword);
//        precitaneToggleButton.setSelected(false);
//        neprecitaneToggleButton.setSelected(false);

        if (selectedKeyword.startsWith("všetko")) {
            keywordsInzeraty = new ArrayList<Inzerat>();
            keywordsInzeraty.addAll(mestoInzeraty);
        } else {
            keywordsInzeraty = new ArrayList<Inzerat>();
            // pridavame do zoznamu inzeraty s keywordom
            for (Inzerat inz : mestoInzeraty) {
                if (selectedKeyword.startsWith("súrne")) {
                    if (inz.isSurne()) {
                        keywordsInzeraty.add(inz);
                    }
                }
            }
        }
        System.out.println("keywordsInzeraty size: " + keywordsInzeraty.size());
        if (archivovaneToggleButton.isSelected()) {
            archivovaneToggleButton.setSelected(false);
        }
        refreshTableWithToggleButtons(keywordsInzeraty);
    }

    private void refreshTableWithToggleButtons(List<Inzerat> inzeraty) {
        System.out.println("refreshingTable with toggle buttons");
        tableInzeraty = new ArrayList<Inzerat>();
        preposlavaneInzeraty = new ArrayList<Inzerat>();
        for (Inzerat inz : inzeraty) {
            boolean p1 = precitaneToggleButton.isSelected();
            boolean p2 = neprecitaneToggleButton.isSelected();
            boolean p3 = oznaceneToggleButton.isSelected();
            boolean p4 = nevytlaceneToggleButton.isSelected();
            boolean p11 = inz.isPrecitany();
            boolean p22 = !inz.isPrecitany();
            boolean p33 = inz.isZaujimavy();
            boolean p44 = !inz.isOdoslany();
            if ((p1 && p11 || !p1) && (p2 && p22 || !p2) && (p3 && p33 || !p3) && (p4 && p44 || !p4)) {
                if (vyhladavanie) {
                    if (inz.getText().contains(hladane) || inz.getNazov().contains(hladane) || inz.getMeno().contains(hladane)) {
                        tableInzeraty.add(inz);
                        // ak je oznaceny a nie je preposlany tak ho pridame do preposlavanych
                        if (inz.isZaujimavy() && !inz.isPreposlany()) {
                            preposlavaneInzeraty.add(inz);
                        }
                    }
                } else {
                    tableInzeraty.add(inz);
                    // ak je oznaceny a nie je preposlany tak ho pridame do preposlavanych
                    if (inz.isZaujimavy() && !inz.isPreposlany()) {
                        preposlavaneInzeraty.add(inz);
                    }
                }
            }
            refreshPreposlatButtonText();

            // ak nie je zvoleny ani jeden filter, tak vsetko pridavame
            if (!precitaneToggleButton.isSelected() && !neprecitaneToggleButton.isSelected()
                    && !oznaceneToggleButton.isSelected() && !nevytlaceneToggleButton.isSelected() && !archivovaneToggleButton.isSelected()) {
                bckgTableColor = Color.white;
            } else {
                //inzeratyTable.setBackground(new Color(242, 249, 252));
                bckgTableColor = new Color(242, 249, 252);
                if (archivovaneToggleButton.isSelected()) {
                    bckgTableColor = new Color(230, 230, 230);
                }
            }
        }
        Collections.sort(tableInzeraty);
        Collections.reverse(tableInzeraty);
        refreshInzeratyTable(tableInzeraty);
    }

    /**
     * tato metoda sa vola iba od refreshTableWithToggleButtons a iba vtedy ak sa zmenia nastavenia
     * filtrov
     *
     * @param inzeraty
     */
    private void refreshInzeratyTable(List<Inzerat> inzeraty) {
        System.out.println("refreshInzeratyTable size: " + inzeraty.size());
        int idx = inzeratyTable.getSelectedRow();
        //System.out.println("refresh idx: " + idx);
        inzeratyTableModel = new InzeratyTableModel(inzeratyTableColumnNames, inzeraty);
        inzeratyTable.setModel(inzeratyTableModel);
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
        inzeratyTable.getSelectionModel().setSelectionInterval(idx, idx);

        refreshInzeratDisplayed();

        Border headerBorder = UIManager.getBorder("TableHeader.cellBorder");
        JLabel blueLabel = new JLabel("", starHeaderIcon, JLabel.CENTER);
        JLabel blueLabel2 = new JLabel("", printHeaderIcon, JLabel.CENTER);
        blueLabel.setBorder(headerBorder);
        TableColumnModel columnModel = inzeratyTable.getColumnModel();
        TableCellRenderer renderer = new JComponentTableCellRenderer();
        TableColumn column0 = columnModel.getColumn(0);
        TableColumn column1 = columnModel.getColumn(2);
        column0.setHeaderRenderer(renderer);
        column0.setHeaderValue(blueLabel);
        column1.setHeaderRenderer(renderer);
        column1.setHeaderValue(blueLabel2);

//        JTableHeader th = inzeratyTable.getTableHeader();
//        TableColumnModel tcm = th.getColumnModel();
//        TableColumn tc = tcm.getColumn(1);
//        JLabel novy = new JLabel();
//        novy.setOpaque(true);
//        novy.setIcon(new ImageIcon(nevytlacene));
//        tc.setHeaderValue(novy);
//        //tc.setHeaderValue("???");
//        th.repaint();
        ///spocitajMestaNastavList(inzeraty);
        //inzeratyTable.getColumnModel().getColumn(0).setCellRenderer(new JLabelCellRenderer(tableInzeraty, database, bckgTableColor, aktRow));
//        polozkyTable.setAutoCreateRowSorter(true);
//        polozkyTable.setUpdateSelectionOnSort(true);
        //  inzeratyTableColumnNames = {"*","názov", "T",  "meno", "lokalita", "dátum"};
        // * column
        inzeratyTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        inzeratyTable.getColumnModel().getColumn(0).setMaxWidth(20);
        // T column
        inzeratyTable.getColumnModel().getColumn(2).setPreferredWidth(20);
        inzeratyTable.getColumnModel().getColumn(2).setMaxWidth(20);
        // nazov column
        inzeratyTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        // datum column
        inzeratyTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        inzeratyTable.getColumnModel().getColumn(5).setMaxWidth(150);
        // meno column
        inzeratyTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        inzeratyTable.getColumnModel().getColumn(3).setMaxWidth(150);
        // lokalita column
        inzeratyTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        inzeratyTable.getColumnModel().getColumn(4).setMaxWidth(150);
    }

    private void loadHostName() {
        hostname = "Unknown";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
        if (hostname.equalsIgnoreCase("Unknown")) {
            JOptionPane.showMessageDialog(null, "Nepodarilo sa správne spustiť program. \n Overte či nie je tento program blokovaný firewallom.");
            System.exit(0);
        }
        hostname = hostname.replaceAll("'", "");
        if (hostname.length() > 20) {
            hostname = hostname.substring(0, 20);
        }
        System.out.println("hostname: " + hostname);
    }

    private void refreshPreposlatButtonText() {
        if (!archivovaneToggleButton.isSelected()) {
            preposlatButton.setText("Preposlať (" + preposlavaneInzeraty.size() + ")");
        } else {
            preposlatButton.setText("Preposlať (0)");
        }
    }

    class JComponentTableCellRenderer implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return (JComponent) value;
        }
    }

    private void refreshInzeratDisplayed() {
        if (!tableInzeraty.isEmpty()) {
            try {
                //System.out.println("inzeratyTable selected row: "+inzeratyTable.getSelectedRow());
                aktualnyInzerat = tableInzeraty.get(inzeratyTable.getSelectedRow());
                aktualnyInzerat.setPocetZobrazeni(aktualnyInzerat.getPocetZobrazeni() + 1);
                aktualnyInzerat.setPrecitany(true);
                database.updateInzerat(aktualnyInzerat);
                //refreshInzeratDisplayed(); <- toto netreba lebo sa to deje v refreshInzeratyTable
                if (archivovaneInzeratyLinky.contains(aktualnyInzerat.getAktualny_link())) {
                    archivovatButton.setEnabled(false);
                } else {
                    archivovatButton.setEnabled(true);
                }
                System.out.println("zobrazeny inzerat: " + aktualnyInzerat);

                if (inzeratDownloadedNeklikat) {

                } else {
                    if (archivovaneToggleButton.isSelected()) {
                        // nebudeme tahat z internetu ale z archivu 
                        File novy = InzeratViewGenerator.getArchivIzeratViewFile(aktualnyInzerat);
                        String cesta = "file:///" + novy.getAbsolutePath();
                        System.out.println("cesta: " + cesta);
                        loadURL(cesta);
                    } else {
                        loadURL(aktualnyInzerat.getAktualny_link());
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(MainForm2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("nie je vybrany ziaden inzerat");
        }
    }

    private void insertIfTrue(ArrayList<Inzerat> tableInzeraty, boolean precitany, Inzerat inz) {
        if (precitany) {
            tableInzeraty.add(inz);
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

                capture.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        archivator.archivovanyInzerat = aktualnyInzerat;
                        archivator.loadURL(engine.getLocation());
                    }
                });

                engine.titleProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                MainForm2.this.setTitle("Manažér realitných inzerátov: " + aktualnyInzerat.getNazov());
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
                                progressBar.setValue(newValue.intValue());
//                                if (newValue.intValue() == 100) {
//                                    // prescrolujeme sa kde chceme
//                                    Set<Node> nodes = view.lookupAll(".scroll-bar");
//
//                                    for (Node node : nodes) {
//                                        if (ScrollBar.class.isInstance(node)) {
//                                            ScrollBar scroll = (ScrollBar) node;
//                                            double scrollPos = scroll.getValue();
//                                            System.out.println("scrollpos: "+scrollPos);
//                                            scroll.setValue(scrollPos+100);
//                                        }
//                                    }
//                                }
                            }
                        }
                        );
                    }

                });

                engine.getLoadWorker().exceptionProperty()
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

    private String getCas() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(System.currentTimeMillis())).toString();
    }

    public String getElapsedTime(long startTime) {
        double elapsedTime = ((System.currentTimeMillis() - startTime) / 1000.0);
        int hodinE = (int) ((elapsedTime) / (3600));
        int minutE = (int) ((elapsedTime) / (60));
        int sekundE = (int) ((elapsedTime));
        sekundE %= 60;
        minutE %= 60;
        String hodinStringE = "" + hodinE;
        if (hodinE < 10) {
            hodinStringE = "0" + hodinE;
        }
        String minutStringE = "" + minutE;
        if (minutE < 10) {
            minutStringE = "0" + minutE;
        }
        String sekundStringE = "" + sekundE;
        if (sekundE < 10) {
            sekundStringE = "0" + sekundE;
        }
        //System.out.println("ETA:" + (hodinStringE + ":" + minutStringE + ":" + sekundStringE));
        return (hodinStringE + ":" + minutStringE + ":" + sekundStringE);
    }

    private String getETAtime(long startTime, long pocetInzeratov, long vsetkych) {
        double rychlost = ((System.currentTimeMillis() - startTime) / 1000.0) / pocetInzeratov;
        double etaTime = (vsetkych - pocetInzeratov) * rychlost;
        int hodinE = (int) ((etaTime) / (3600));
        int minutE = (int) ((etaTime) / (60));
        int sekundE = (int) ((etaTime));
        sekundE %= 60;
        minutE %= 60;
        String hodinStringE = "" + hodinE;
        if (hodinE < 10) {
            hodinStringE = "0" + hodinE;
        }
        String minutStringE = "" + minutE;
        if (minutE < 10) {
            minutStringE = "0" + minutE;
        }
        String sekundStringE = "" + sekundE;
        if (sekundE < 10) {
            sekundStringE = "0" + sekundE;
        }
        //return (hodinStringE + ":" + minutStringE + ":" + sekundStringE);
        return (minutStringE + ":" + sekundStringE);
    }

    private String loadKluc() {
        File file = new File("kluc.ppk");
        String kluc = "defaultKluc";

        BufferedReader f = null;
        try {
            f = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            kluc = f.readLine();
        } catch (FileNotFoundException ex) {
            System.out.println("nepodarilo sa nacitat subor kluc.ppk");
            boolean demo = true;
            if (!demo) {
                JOptionPane.showMessageDialog(this, "Nepodarilo sa načítať autentifikačný kľúč.\nPoužije sa defaultný kľúč. ");
            }
            //Logger.getLogger(Aktualizator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Aktualizator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Aktualizator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kluc;
    }
}
