/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.Inzerat;
import deleted.MainForm;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Janco1
 */
public class MySQLDatabase {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public String DB_URL = "";

    //  Database credentials
    public String USER = "";
    public String PASS = "";
    private Connection conn;
    private Statement stmt;

    public MySQLDatabase(String url, String user, String pass) {
        DB_URL = url;
        USER = user;
        PASS = pass;
    }

    public List<Inzerat> getInzeratyList() {
        List<Inzerat> inzeraty = new ArrayList<Inzerat>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Inzeraty";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Inzerat inz = new Inzerat();
                inz.setAktualny_link(rs.getString("aktualny_link"));
                inz.setCena(rs.getString("cena"));
                inz.setId(Integer.parseInt(rs.getString("id")));
                inz.setLokalita(rs.getString("lokalita"));
                inz.setMeno(rs.getString("meno"));
                inz.setNazov(rs.getString("nazov"));
                inz.setTyp(rs.getString("typ"));
                inz.setKategoria(rs.getString("kategoria"));
                inz.setPortal(rs.getString("portal"));
                inz.setTelefon(rs.getString("telefon"));
                inz.setText(rs.getString("text"));
                inz.setOdoslany(Byte.parseByte(rs.getString("sent")));
                inz.setPrecitany(Byte.parseByte(rs.getString("precitany")));
                inz.setTimeInserted(rs.getString("time_inserted"));
                inz.setZaujimavy(Byte.parseByte(rs.getString("zaujimavy")));
                inz.setSurne(Byte.parseByte(rs.getString("surne")));
                inz.setPocetZobrazeni(Integer.parseInt(rs.getString("pocet_zobrazeni")));
                inzeraty.add(inz);
            }
        } catch (Exception e) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
        return inzeraty;
    }

    // you need to close all three to make sure
    private void close() {
        close((Closeable) resultSet);
        close((Closeable) statement);
        close((Closeable) connect);
    }

    private void close(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            // don't throw now as it might leave following closables in undefined state
        }
    }

    public static void main(String[] args) throws Exception {

    }

    public List<Inzerat> getInzeratyTimeInsertedLast(String lastModified) {
        List<Inzerat> inzeraty = new ArrayList<Inzerat>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Inzeraty where time_inserted>timestamp('" + lastModified + "')";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Inzerat inz = new Inzerat();
                inz.setAktualny_link(rs.getString("aktualny_link"));
                inz.setCena(rs.getString("cena"));
                inz.setId(Integer.parseInt(rs.getString("id")));
                inz.setLokalita(rs.getString("lokalita"));
                inz.setMeno(rs.getString("meno"));
                inz.setNazov(rs.getString("nazov"));
                inz.setTyp(rs.getString("typ"));
                inz.setKategoria(rs.getString("kategoria"));
                inz.setPortal(rs.getString("portal"));
                inz.setTelefon(rs.getString("telefon"));
                inz.setText(rs.getString("text"));
                inz.setOdoslany(Byte.parseByte(rs.getString("sent")));
                inz.setTimeInserted(rs.getString("time_inserted"));
                inz.setZaujimavy(Byte.parseByte(rs.getString("zaujimavy")));
                inz.setSurne(Byte.parseByte(rs.getString("surne")));
                inz.setPrecitany(Byte.parseByte(rs.getString("precitany")));
                inz.setPocetZobrazeni(Integer.parseInt(rs.getString("pocet_zobrazeni")));
                inzeraty.add(inz);
            }
        } catch (Exception e) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
        return inzeraty;
    }

    public List<Integer> getInzeratyIDsFromLastTimeInserted(String lastTimeInserted) {
        List<Integer> inzeraty = new ArrayList<Integer>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id FROM Inzeraty WHERE time_inserted>timestamp('" + lastTimeInserted + "')";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                inzeraty.add(Integer.parseInt(rs.getString("id")));
            }
        } catch (Exception e) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
        return inzeraty;
    }

    public List<Inzerat> getInzeratyListFromIDs(List<Integer> idsToDownload) {
        if (idsToDownload.isEmpty()) {
            System.out.println("getInzeratyListFromIDs: idsToDownload is empty. No download from serverDB.");
            return null;
        }
        List<Inzerat> inzeraty = new ArrayList<Inzerat>();
        try {
            StringBuilder idsString = new StringBuilder();
            for (int i = 0; i < idsToDownload.size() - 1; i++) {
                idsString.append(idsToDownload.get(i) + ",");
            }
            idsString.append(idsToDownload.get(idsToDownload.size() - 1));
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Inzeraty WHERE id IN(" + idsString + ") ;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Inzerat inz = new Inzerat();
                inz.setAktualny_link(rs.getString("aktualny_link"));
                inz.setCena(rs.getString("cena"));
                inz.setId(Integer.parseInt(rs.getString("id")));
                inz.setLokalita(rs.getString("lokalita"));
                inz.setMeno(rs.getString("meno"));
                inz.setNazov(rs.getString("nazov"));
                inz.setTyp(rs.getString("typ"));
                inz.setKategoria(rs.getString("kategoria"));
                inz.setPortal(rs.getString("portal"));
                inz.setTelefon(rs.getString("telefon"));
                inz.setText(rs.getString("text"));
                inz.setOdoslany(Byte.parseByte(rs.getString("sent")));
                inz.setTimeInserted(rs.getString("time_inserted"));
                inz.setZaujimavy(Byte.parseByte(rs.getString("zaujimavy")));
                inz.setSurne(Byte.parseByte(rs.getString("surne")));
                inz.setPrecitany(Byte.parseByte(rs.getString("precitany")));
                inz.setPocetZobrazeni(Integer.parseInt(rs.getString("pocet_zobrazeni")));
                inzeraty.add(inz);
            }
        } catch (Exception e) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
        return inzeraty;
    }

    public List<Inzerat> getSurneInzeraty() {
        List<Inzerat> inzeraty = new ArrayList<Inzerat>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Inzeraty WHERE surne=true;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Inzerat inz = new Inzerat();
                inz.setAktualny_link(rs.getString("aktualny_link"));
                inz.setCena(rs.getString("cena"));
                inz.setId(Integer.parseInt(rs.getString("id")));
                inz.setLokalita(rs.getString("lokalita"));
                inz.setMeno(rs.getString("meno"));
                inz.setNazov(rs.getString("nazov"));
                inz.setTyp(rs.getString("typ"));
                inz.setKategoria(rs.getString("kategoria"));
                inz.setPortal(rs.getString("portal"));
                inz.setTelefon(rs.getString("telefon"));
                inz.setText(rs.getString("text"));
                inz.setOdoslany(Byte.parseByte(rs.getString("sent")));
                inz.setTimeInserted(rs.getString("time_inserted"));
                inz.setZaujimavy(Byte.parseByte(rs.getString("zaujimavy")));
                inz.setSurne(Byte.parseByte(rs.getString("surne")));
                inz.setPrecitany(Byte.parseByte(rs.getString("precitany")));
                inz.setPocetZobrazeni(Integer.parseInt(rs.getString("pocet_zobrazeni")));
                inzeraty.add(inz);
            }
        } catch (Exception e) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
        return inzeraty;
    }

    // BUDEM STAHOVAT LINKY A ZAROVEN IDCKA
//    List<String> getInzeratyLinks() {
//        List<String> inzeraty = new ArrayList<String>();
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            stmt = conn.createStatement();
//            String sql;
//            sql = "SELECT aktualny_link FROM Inzeraty";
//            ResultSet rs = stmt.executeQuery(sql);
//            while (rs.next()) {
//                inzeraty.add(rs.getString("aktualny_link"));
//            }
//        } catch (Exception e) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
//        } finally {
//            close();
//        }
//        return inzeraty;
//    }

    List<Inzerat> getInzeratyListLinkNOTIn(List<String> linky) {
        if (linky.isEmpty()) {
            System.out.println("getInzeratyListFromIDs: idsToDownload is empty. No download from serverDB.");
            return null;
        }
        List<Inzerat> inzeraty = new ArrayList<Inzerat>();
        try {
            StringBuilder idsString = new StringBuilder("'");
            for (int i = 0; i < linky.size() - 1; i++) {
                idsString.append(linky.get(i) + "','");
            }
            idsString.append(linky.get(linky.size() - 1)+"'");
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Inzeraty WHERE aktualny_link NOT IN(" + idsString + ") ;";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Inzerat inz = new Inzerat();
                inz.setAktualny_link(rs.getString("aktualny_link"));
                inz.setCena(rs.getString("cena"));
                inz.setId(Integer.parseInt(rs.getString("id")));
                inz.setLokalita(rs.getString("lokalita"));
                inz.setMeno(rs.getString("meno"));
                inz.setNazov(rs.getString("nazov"));
                inz.setTyp(rs.getString("typ"));
                inz.setKategoria(rs.getString("kategoria"));
                inz.setPortal(rs.getString("portal"));
                inz.setTelefon(rs.getString("telefon"));
                inz.setText(rs.getString("text"));
                inz.setOdoslany(Byte.parseByte(rs.getString("sent")));
                inz.setTimeInserted(rs.getString("time_inserted"));
                inz.setZaujimavy(Byte.parseByte(rs.getString("zaujimavy")));
                inz.setSurne(Byte.parseByte(rs.getString("surne")));
                inz.setPrecitany(Byte.parseByte(rs.getString("precitany")));
                inz.setPocetZobrazeni(Integer.parseInt(rs.getString("pocet_zobrazeni")));
                inzeraty.add(inz);
            }
        } catch (Exception e) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
        return inzeraty;
    }

    Map<Integer, String> getInzeratyIDLinks() {
        Map<Integer,String> inzeraty = new HashMap<Integer,String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id,aktualny_link FROM Inzeraty";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                //inzeraty.add();
                inzeraty.put(Integer.parseInt(rs.getString("id")), rs.getString("aktualny_link"));
            }
        } catch (Exception e) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
        return inzeraty;
    }
}
