/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.Inzerat;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jsoup.nodes.Document;

/**
 *
 * @author Janco1
 */
public class Aktualizator implements Runnable {

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    public String hostname = "";
//    public String connectionString = "";
//    public String dbuser = "";
//    public String dbpass = "";
    private final JsoupCrawler jcrawler = new JsoupCrawler();
    public JFrame component;
    public long casPoslednejAktualizacie;
    public String kluc = "";
    MySQLDatabase msd = new MySQLDatabase("", "", "");
    Database database;

    public void run() {
        casPoslednejAktualizacie = System.currentTimeMillis();
        String url = "http://www.jmurin.sk/manazer/autentifikacia.php?nazov_pc=" + hostname + "&kluc=" + kluc;
        //String url = "http://www.jmurin.sk/manazer/autentifikacia.php?nazov_pc=tomas&kluc=defaultKluc";
        System.out.println("ziskavam db udaje: [" + url + "]");
        changes.firePropertyChange("statusLabel", "", "Sťahujem DB údaje.");
        Document doc = jcrawler.getPage(url);
        String typ = doc.select("span.typ").text();
        String message = "";
        if (typ.equals("ERROR")) {
            message = doc.select("span.message").text();
            if (message.equals("neplatny kluc")) {
                JOptionPane.showMessageDialog(component, "Neúspešná autentifikácia: neplatný kľúč");
                //kreditLabel.setText("Kredit: 0 dní");
                changes.firePropertyChange("kreditLabel", "", " Kredit: ? dní");
                changes.firePropertyChange("zablokovatUpdaty", false, true);
                changes.firePropertyChange("statusLabel", "", " Status: autentifikačný kľúč je neplatný, nepodarilo sa získať údaje do DB.");
                return;
            }
            if (message.equals("nema povolenie")) {
                JOptionPane.showMessageDialog(component, "Neúspešná autentifikácia: autentifikačný kľúč už používa viac ako 5 clientov");
                //kreditLabel.setText("Kredit: 0 dní");
                changes.firePropertyChange("kreditLabel", "", " Kredit: 0 dní");
                changes.firePropertyChange("zablokovatUpdaty", false, true);
                changes.firePropertyChange("statusLabel", "", " Status: autentifikačný kľúč už používa viac ako 5 clientov, nepodarilo sa získať údaje do DB.");
                return;
            }
            if (message.equals("vycerpany kredit")) {
                String minutych = doc.select("span.minutych").text();
                String kredit = doc.select("span.kredit").text();
                JOptionPane.showMessageDialog(component, "Neúspešná autentifikácia: váš kredit je už vyčerpaný! \n\n Kreditov: " + kredit + " Vyčerpaných: " + minutych);
                //kreditLabel.setText("Kredit: 0 dní");
                changes.firePropertyChange("kreditLabel", "", " Kredit: 0 dní");
                changes.firePropertyChange("zablokovatUpdaty", false, true);
                changes.firePropertyChange("statusLabel", "", " Status: vyčerpaný kredit, nepodarilo sa získať údaje do DB.");
                return;
            }
        } else {
            String connString = doc.select("span.connectionString").text();
            String dbuser = doc.select("span.username").text();
            String dbpass = doc.select("span.password").text();
            int minutych = Integer.parseInt(doc.select("span.minutych").text());
            int kredit = Integer.parseInt(doc.select("span.kredit").text());
            System.out.println("ziskane db data: ");
            System.out.println("connection string: " + connString);
            System.out.println("username: " + dbuser);
            System.out.println("password: " + dbpass);
            System.out.println("minutych: " + minutych);
            System.out.println("kredit: " + kredit);
            //kreditLabel.setText("Kredit: " + (kredit - minutych) + " dní");
            changes.firePropertyChange("kreditLabel", "", " Kredit: " + (kredit - minutych) + " dní");
            // spustime aktualizaciu
            System.out.println("spustam aktualizaciu");
            aktualizujDatabazu(connString, dbuser, dbpass);
            System.out.println("DB aktualizacia ukoncena");
            changes.firePropertyChange("aktualizaciaUkoncena", true, false);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }

    private void aktualizujDatabazu(String connString, String dbuser, String dbpass) {
        msd.DB_URL = connString;
        msd.USER = dbuser;
        msd.PASS = dbpass;
        // vymazeme stare nase
        Map<Integer, String> aktualneLinky = null;
        changes.firePropertyChange("statusLabel", "", " získavam informácie o aktuálnych inzerátoch...");
        try {
            aktualneLinky = msd.getInzeratyIDLinks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(component, "Vyskytla sa chyba. Skontrolujte vaše pripojenie k internetu.");
            return;
        }
        System.out.println("aktualnych mysql linkov: " + aktualneLinky.size());
        long start = System.currentTimeMillis();
        // MUSIME ZABEZPECIT ABY V DATABAZE BOLI IBA TIE INZERATY, KTORE MAJU ZAROVEN ROVNAKE ID AJ AKTUALNY LINK AKO V MYSQLDB
        // vsetko ostatne sa maze
        List<Inzerat> vsetkyInzeraty = database.getInzeratyIdLink();// inzerat ma iba id a link kvoli setreniu pametou
        List<Integer> toDeleteIDs = new ArrayList<Integer>();
        for (Inzerat inz : vsetkyInzeraty) {
            // chcem zistit ci aktualny inzerat je aktualny alebo uz nie
            if (aktualneLinky.containsKey(inz.getId())) {
                if (aktualneLinky.get(inz.getId()).equals(inz.getAktualny_link())) {
                    // id aj link pre konkretny inzerat su zhodne s mysql, takze sa nemaze
                } else {
                    // pre konkretny inzerat sa nezhoduje id a link s tym co je v mysql takze ide von
                    toDeleteIDs.add(inz.getId());
                }
            } else {
                // nasli sme inzerat ktoreho idcko nie je v mysql, teda urcite ide von
                toDeleteIDs.add(inz.getId());
            }
        }
        changes.firePropertyChange("statusLabel", "", " mažem " + toDeleteIDs.size() + " neaktuálnych inzerátov...");
        database.deleteDuplikatneInzeraty(toDeleteIDs);
        
        System.out.println("zmazane neaktualne inzeraty cas: " + getElapsedTime(start)+" pocet zmazanych: "+toDeleteIDs.size());
        // stiahneme nove
        List<Integer> noveInzeratyIDs = database.getNoveInzeratyIDs(aktualneLinky);
//        String lastTimeInserted = database.getLastTimeInserted();
//        System.out.println("last time inserted v nasej databaze je: " + lastTimeInserted);
//        List<Integer> noveInzeratyIDs = null;
//        try {
//            noveInzeratyIDs = msd.getInzeratyIDsFromLastTimeInserted(lastTimeInserted);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(component, "Vyskytla sa chyba. Skontrolujte vaše pripojenie k internetu.");
//            return;
//        }

        System.out.println("downloading from remote server db count:" + noveInzeratyIDs.size());
        changes.firePropertyChange("statusLabel", "", " sťahujem zo servera " + noveInzeratyIDs.size() + " inzerátov...");
        //database.opravDuplicity();
        if (noveInzeratyIDs.size() == 0) {
            System.out.println("ziadne nove inzeraty nenajdene");
            //JOptionPane.showMessageDialog(component, "Nenašli sa žiadne nové inzeráty.");
            changes.firePropertyChange("statusLabel", "", " Nenašli sa žiadne nové inzeráty.");
            return;
        } else {
            System.out.println("nasli sa nove inzeraty");
        }
        List<Inzerat> fromServer = new ArrayList<Inzerat>();
        List<Integer> idsToDownload = new ArrayList<Integer>();
        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < noveInzeratyIDs.size(); i++) {
            if (idsToDownload.size() < 5000) {
                idsToDownload.add(noveInzeratyIDs.get(i));
            } else {
                List<Inzerat> down = null;
                try {
                    down = msd.getInzeratyListFromIDs(idsToDownload);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(component, "Vyskytla sa chyba. Skontrolujte vaše pripojenie k internetu.");
                    return;
                }
//                for (Inzerat inz : down) {
//                    if (inz.isSurne()) {
//                        System.out.println("dostal som surny inzerat zo servera");
//                    }
//                }
                database.inzertInzeraty(down);
                System.out.print("downloaded " + i + "/" + noveInzeratyIDs.size() + " ");
                changes.firePropertyChange("statusLabel", "", " stiahnutých zo servera " + i + "/" + noveInzeratyIDs.size() + " inzerátov...");
                //printETATime(startTime2, i, noveInzeratyIDs.size());
                idsToDownload = new ArrayList<Integer>();
            }
        }
        List<Inzerat> down = null;
        try {
            down = msd.getInzeratyListFromIDs(idsToDownload);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(component, "Vyskytla sa chyba. Skontrolujte vaše pripojenie k internetu.");
            return;
        }
        database.inzertInzeraty(down);

        // moze sa stat ze niektore inzeraty tam nedosli
        // spytame sa ktore tam dosli a znova posielame tie co nedosli
        boolean vsetkyDosli = false;
        while (!vsetkyDosli) {
            // zistime idcka ktore sa nepodarilo stiahnut zo servera
            // stiahneme postupne idcka ktore sa nam nevlozili do DB

            // najprv zistime ktore z novych inzeratov su v databaze
            List<Integer> noveInzeratyVlozeneIDs = database.getInzeratyIDsFrom(noveInzeratyIDs);
            if (noveInzeratyVlozeneIDs.size() == noveInzeratyIDs.size()) {
                System.out.println("vsetky inzeraty dosli");
                break;
            }
            // vytvorime zoznam nedoslych inzeratov
            List<Integer> nedosleIDcka = new ArrayList<Integer>();
            for (Integer idcko : noveInzeratyIDs) {
                if (!noveInzeratyVlozeneIDs.contains(idcko)) {
                    nedosleIDcka.add(idcko);
                }
            }
            System.out.println("nedoslo " + nedosleIDcka.size() + ", stahujem znova");
            changes.firePropertyChange("statusLabel", "", "nedošlo " + nedosleIDcka.size() + ", sťahujem znova...");
            startTime2 = System.currentTimeMillis();
            idsToDownload = new ArrayList<Integer>();
            for (int i = 0; i < nedosleIDcka.size(); i++) {
                if (idsToDownload.size() < 5000) {
                    idsToDownload.add(nedosleIDcka.get(i));
                } else {
                    down = null;
                    try {
                        down = msd.getInzeratyListFromIDs(idsToDownload);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(component, "Vyskytla sa chyba. Skontrolujte vaše pripojenie k internetu.");
                        return;
                    }
                    database.inzertInzeraty(down);
                    System.out.print("downloaded " + i + "/" + nedosleIDcka.size() + " ");
                    changes.firePropertyChange("statusLabel", "", " stiahnutých zo servera " + i + "/" + noveInzeratyIDs.size() + " inzerátov...");
                    //printETATime(startTime2, i, nedosleIDcka.size());
                    idsToDownload = new ArrayList<Integer>();
                }
            }
            down = null;
            try {
                down = msd.getInzeratyListFromIDs(idsToDownload);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(component, "Vyskytla sa chyba. Skontrolujte vaše pripojenie k internetu.");
                return;
            }
            database.inzertInzeraty(down);
        }
        System.out.println("vsetky inzeraty dosli");
        //database.opravDuplicity(aktualneLinky);
        //surneInzeraty = database.getSurneInzeraty();
        //JOptionPane.showMessageDialog(component, "Našlo sa " + noveInzeratyIDs.size() + " nových inzerátov.");
        changes.firePropertyChange("statusLabel", "", " stiahnutých zo servera " + noveInzeratyIDs.size() + "/" + noveInzeratyIDs.size() + " inzerátov...");
        changes.firePropertyChange("inzeratyDownloaded", false, true);

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
}
