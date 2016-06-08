/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.InzeratRowMapper;
import entity.Inzerat;
import deleted.MainForm;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author Janco1
 */
public class Database {

    private JdbcTemplate jdbcTemplate;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private String AKTUALNY_CAS;

    public Database() {

        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost:1235/inzeratydb");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        jdbcTemplate = new JdbcTemplate(dataSource);

        AKTUALNY_CAS = getTimestamp();
    }

//    public List<Inzerat> getInzeratyList(String portalName) {
//        try {
//            RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
//            String sql = "SELECT * FROM inzeraty WHERE portal='" + portalName + "'";
//            List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
//            return inzeraty;
//        } catch (Exception exception) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
//        }
//        return null;
//    }
    public List<Inzerat> getArchivInzeratyList() {
        try {
            RowMapper<Inzerat> rowMapper = new RowMapper() {

                public Inzerat mapRow(ResultSet rs, int rowNum) throws SQLException {
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
                    inz.setOdoslany(Boolean.parseBoolean(rs.getString("sent")));
                    inz.setTimeInserted(rs.getString("time_inserted"));
                    //inz.setArchivaciaTime(rs.getString("archivacia_time"));
                    inz.setPocetZobrazeni(Integer.parseInt(rs.getString("pocet_zobrazeni")));
                    inz.setZaujimavy(Boolean.parseBoolean(rs.getString("zaujimavy")));
                    inz.setSurne(Boolean.parseBoolean(rs.getString("surne")));
                    inz.setPrecitany(Boolean.parseBoolean(rs.getString("precitany")));
                    inz.setPreposlany(Boolean.parseBoolean(rs.getString("preposlany")));

                    inz.setArchivCesta(rs.getString("archivcesta"));
                    return inz;
                }
            };
            String sql = "SELECT * FROM archiv ORDER BY archivacia_time desc";
            List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

//    public List<Inzerat> getPreposlaneInzeratyList() {
//        try {
//            RowMapper<Inzerat> rowMapper = new RowMapper() {
//
//                public Inzerat mapRow(ResultSet rs, int rowNum) throws SQLException {
//                    Inzerat inz = new Inzerat();
//                    inz.setAktualny_link(rs.getString("aktualny_link"));
//                    inz.setCena(rs.getString("cena"));
//                    inz.setId(Integer.parseInt(rs.getString("id")));
//                    inz.setLokalita(rs.getString("lokalita"));
//                    inz.setMeno(rs.getString("meno"));
//                    inz.setNazov(rs.getString("nazov"));
//                    inz.setTyp(rs.getString("typ"));
//                    inz.setKategoria(rs.getString("kategoria"));
//                    inz.setPortal(rs.getString("portal"));
//                    inz.setTelefon(rs.getString("telefon"));
//                    inz.setText(rs.getString("text"));
//                    inz.setOdoslany(Boolean.parseBoolean(rs.getString("sent")));
//                    inz.setTimeInserted(rs.getString("time_inserted"));
//                    inz.setPreposlanieTime(rs.getString("preposlanie_time"));
//                    inz.setPocetZobrazeni(Integer.parseInt(rs.getString("pocet_zobrazeni")));
//                    inz.setZaujimavy(Boolean.parseBoolean(rs.getString("zaujimavy")));
//                    inz.setSurne(Boolean.parseBoolean(rs.getString("surne")));
//                    inz.setPrecitany(Boolean.parseBoolean(rs.getString("precitany")));
//                    inz.setPreposlany(Boolean.parseBoolean(rs.getString("preposlany")));
//
//                    inz.setEmail(rs.getString("email"));
//                    return inz;
//                }
//            };
//            String sql = "SELECT * FROM preposlane ORDER BY preposlanie_time desc";
//            List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
//            return inzeraty;
//        } catch (Exception exception) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
//        }
//        return null;
//    }
    /**
     * pouziva sa iba pri stahovani novych inzeratov z webu NESMIE SA POUZIVAT IBA NA STAHOVANIE
     * AKTUALIZACII lebo pocet a zaujimavy su false
     *
     * @param inzeraty
     */
    public void inzertInzeraty(List<Inzerat> inzeraty) {
        if (inzeraty == null) {
            System.out.println("inzeraty insert: inzeraty list is null");
            return;
        }
        StringBuilder sql = new StringBuilder();
        for (Inzerat inzerat : inzeraty) {
            sql.append("INSERT INTO inzeraty (id,portal,nazov,text,meno,telefon,lokalita,aktualny_link,cena,sent,time_inserted,pocet_zobrazeni,zaujimavy,surne,typ,kategoria,precitany,preposlany)"
                    + "VALUES('" + inzerat.getId() + "',"
                    + "'" + inzerat.getPortal() + "',"
                    + "'" + inzerat.getNazov() + "',"
                    + "'" + inzerat.getText() + "',"
                    + "'" + inzerat.getMeno() + "',"
                    + "'" + inzerat.getTelefon() + "',"
                    + "'" + inzerat.getLokalita() + "',"
                    + "'" + inzerat.getAktualny_link() + "',"
                    + "'" + inzerat.getCena() + "',"
                    + "'" + inzerat.isOdoslany() + "',"
                    + "'" + inzerat.getTimeInserted() + "','0',false," + inzerat.isSurne() + ","
                    + "'" + inzerat.getTyp() + "',"
                    + "'" + inzerat.getKategoria() + "',false,false); \n");
        }
        //System.out.println("INSERT SQL:" +sql.toString());
        if (sql.length() == 0) {
            return;
        }
        try {
            jdbcTemplate.execute(sql.toString());
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println("BAD SQL:" + sql.toString());
        }
    }

    public void inzertArchivInzerat(Inzerat inzerat) {
        try {
            int id = getArchivTopID();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO archiv (id,portal,nazov,text,meno,telefon,lokalita,aktualny_link,cena,sent,time_inserted,archivacia_time,pocet_zobrazeni,zaujimavy,surne,typ,kategoria,precitany,preposlany)"
                    + "VALUES('" + (id + 1) + "',"
                    + "'" + inzerat.getPortal() + "',"
                    + "'" + inzerat.getNazov() + "',"
                    + "'" + inzerat.getText() + "',"
                    + "'" + inzerat.getMeno() + "',"
                    + "'" + inzerat.getTelefon() + "',"
                    + "'" + inzerat.getLokalita() + "',"
                    + "'" + inzerat.getAktualny_link() + "',"
                    + "'" + inzerat.getCena() + "',"
                    + "'" + inzerat.isOdoslany() + "',"
                    + "'" + inzerat.getTimeInserted() + "',"
                    + "'" + getTimestamp() + "',"
                    + "'" + inzerat.getPocetZobrazeni() + "',"
                    + "'" + inzerat.isZaujimavy() + "',"
                    + "'" + inzerat.isSurne() + "',"
                    + "'" + inzerat.getTyp() + "',"
                    + "'" + inzerat.getKategoria() + "','" + inzerat.isPrecitany() + "','" + inzerat.isPreposlany() + "'); \n");

            jdbcTemplate.execute(sql.toString());
            // System.out.println("pridana transakcia " + nova + " s idckom: " + insertedBlocekId);
            changes.firePropertyChange("archivInzeratAdded", 0, 1);

            //jdbcTemplate.execute(sql.toString());
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            //System.out.println("BAD SQL:" + sql.toString());
        }
    }

    public int getTopRegisteredInzeratID() {
        //get najvyssie id
        RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
        StringBuilder sql = new StringBuilder("select top 1 * from inzeraty order by id desc");
        List<Inzerat> foundBlocky = jdbcTemplate.query(sql.toString(), rowMapper);
        if (foundBlocky.size() == 0) {
            return 0;
        }
        int topID = foundBlocky.get(0).getId();
        return topID;
    }

//    void updateInzeraty(Map<String, String> inzeraty) {
//        getvynimka();
//        StringBuilder sql = new StringBuilder();
//        for (String nazov : inzeraty.keySet()) {
//            sql.append("UPDATE inzeraty set aktualny_link='" + inzeraty.get(nazov) +"' WHERE nazov='" + nazov + "'\n");
//        }
//        if (sql.length() == 0) {
//            return;
//        }
//        //System.out.println("UPDATE SQL:" +sql.toString());
//        try {
//            jdbcTemplate.execute(sql.toString());
//        } catch (Exception exception) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
//            System.out.println("BAD SQL:" + sql.toString());
//        }
//
//    }
    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        return sdf.format(new Date(System.currentTimeMillis())).toString();
    }

    private void getvynimka() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    List<Inzerat> getInzeratyList() {
////        try {
////            RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
////            String sql = "SELECT * FROM inzeraty where sent=false";
////            List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
////            return inzeraty;
////        } catch (Exception exception) {
////            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
////        }
////        return null;
//    }
    public List<Inzerat> getInzeratyList(String portal, int pocet) {
        if (portal.equals("všetky")) {
            try {
                RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
                String sql = "SELECT * FROM inzeraty WHERE telefon not in "
                        + "(select telefon from(select telefon, count(*) as pocet from inzeraty group by telefon order by pocet desc)as T "
                        + "where pocet >" + pocet + ")";
                List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
                sql = "SELECT * FROM inzeraty WHERE telefon='nezverejnený' or telefon='Číslo neuvedené';";
                inzeraty.addAll(jdbcTemplate.query(sql, rowMapper));
                return inzeraty;
            } catch (Exception exception) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            }
            return null;
        } else {
            try {
                RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
                String sql = "SELECT * FROM inzeraty WHERE telefon not in "
                        + "(select telefon from(select telefon, count(*) as pocet from inzeraty group by telefon order by pocet desc)as T "
                        + "where pocet >" + pocet + ") and portal='" + portal + "'";
                List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
                return inzeraty;
            } catch (Exception exception) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            }
            return null;
        }
    }

//    List<Inzerat> getInzeratyListPovolenychMesto(int povolenych, String mestoAktualne) {
//        // ziskame podla miest
//        try {
//            RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
//            String sql = "SELECT * FROM inzeraty WHERE telefon not in "
//                    + "(select telefon from(select telefon, count(*) as pocet from inzeraty group by telefon order by pocet desc)as T "
//                    + "where pocet >" + povolenych + ") and lokalita like '%" + mestoAktualne + "%' and sent=false";
//            List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
//            return inzeraty;
//        } catch (Exception exception) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
//        }
//        return null;
//    }
    public void updateInzerat(Inzerat inzerat) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE inzeraty set portal='" + inzerat.getPortal() + "',"
                + "nazov='" + inzerat.getNazov() + "',"
                + "text='" + inzerat.getText() + "',"
                + "meno='" + inzerat.getMeno() + "',"
                + "telefon='" + inzerat.getTelefon() + "',"
                + "lokalita='" + inzerat.getLokalita() + "',"
                + "aktualny_link='" + inzerat.getAktualny_link() + "',"
                + "cena='" + inzerat.getCena() + "',"
                + "sent='" + inzerat.isOdoslany() + "',"
                + "time_inserted='" + inzerat.getTimeInserted() + "',"
                + "pocet_zobrazeni='" + inzerat.getPocetZobrazeni() + "',"
                + "zaujimavy=" + inzerat.isZaujimavy() + ", "
                + "surne=" + inzerat.isSurne() + ","
                + "typ='" + inzerat.getTyp() + "',"
                + "kategoria='" + inzerat.getKategoria() + "',"
                + "precitany=" + inzerat.isPrecitany() + ","
                + "preposlany=" + inzerat.isPreposlany() + ","
                + "cesta='" + inzerat.getArchivCesta() + "' WHERE id='" + inzerat.getId() + "'; \n");
        //System.out.println("update inzerat: "+sql.toString());
        //System.out.println("INSERT SQL:" +sql.toString());
        if (sql.length() == 0) {
            return;
        }
        try {
            jdbcTemplate.execute(sql.toString());
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println("BAD SQL:" + sql.toString());
        }
    }

    public void updateArchivInzeratArchivCesta(String link, String cesta) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE archiv set archivcesta='" + cesta + "' WHERE aktualny_link='" + link + "'; \n");
        if (sql.length() == 0) {
            return;
        }
        try {
            jdbcTemplate.execute(sql.toString());
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println("BAD SQL:" + sql.toString());
        }
    }

//    public List<Inzerat> getInzeratyListMesto(Integer integer) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    public List<String> getMestaList(Integer integer) {
//        try {
//            RowMapper<String> rowMapper = new RowMapper<String>() {
//
//                public String mapRow(ResultSet rs, int i) throws SQLException {
//                    return rs.getString("lokalita").substring(7).trim();
//                }
//            };
//            String sql = "SELECT * FROM inzeraty WHERE telefon not in "
//                    + "(select telefon from(select telefon, count(*) as pocet from inzeraty group by telefon order by pocet desc)as T "
//                    + "where pocet >" + integer + ")";
//            List<String> lokality = jdbcTemplate.query(sql, rowMapper);
//            return lokality;
//        } catch (Exception exception) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
//        }
//        return null;
//    }
//    public String getLastTimeInserted() {
//        //get najvyssie id
//        RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
//        StringBuilder sql = new StringBuilder("select top 1 * from inzeraty order by time_inserted desc");
//        List<Inzerat> foundBlocky = jdbcTemplate.query(sql.toString(), rowMapper);
//        if (foundBlocky.size() == 0) {
//            return "2013-10-08 13:24:00.000";
//        }
//        int topID = foundBlocky.get(0).getId();
//        return foundBlocky.get(0).getTimeInserted();
//    }
//    public void deleteInzeratyWithIDNotIn(List<Integer> aktualneIDs) {
//        if (aktualneIDs.size() == 0) {
//            return;
//        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("(");
//        for (int i = 0; i < aktualneIDs.size() - 1; i++) {
//            sb.append(aktualneIDs.get(i) + ",");
//        }
//        sb.append(aktualneIDs.get(aktualneIDs.size() - 1) + ")");
//        String prikaz = "DELETE FROM inzeraty WHERE id NOT IN" + sb.toString();
//        jdbcTemplate.execute(prikaz);
//    }
    public List<Integer> getInzeratyIDsFrom(List<Integer> noveInzeratyIDs) {
        if (noveInzeratyIDs.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < noveInzeratyIDs.size() - 1; i++) {
            sb.append(noveInzeratyIDs.get(i) + ",");
        }
        sb.append(noveInzeratyIDs.get(noveInzeratyIDs.size() - 1) + ")");

        try {
            RowMapper<Integer> rowMapper = new RowMapper<Integer>() {

                public Integer mapRow(ResultSet rs, int i) throws SQLException {
                    return Integer.parseInt(rs.getString("id"));
                }
            };
            String sql = "SELECT id FROM inzeraty WHERE id IN" + sb.toString();
            List<Integer> inzeraty = jdbcTemplate.query(sql, rowMapper);
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    public List<String> getPortalyNames() {
        try {
            RowMapper<String> rowMapper = new RowMapper<String>() {

                public String mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("portal");
                }
            };
            String sql = "select portal from inzeraty group by portal";
            List<String> inzeraty = jdbcTemplate.query(sql, rowMapper);
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    public void updateInzeraty(ArrayList<Inzerat> tableInzeraty) {
        StringBuilder sql = new StringBuilder();
        for (Inzerat inzerat : tableInzeraty) {
            sql.append("UPDATE inzeraty set portal='" + inzerat.getPortal() + "',"
                    + "nazov='" + inzerat.getNazov() + "',"
                    + "text='" + inzerat.getText() + "',"
                    + "meno='" + inzerat.getMeno() + "',"
                    + "telefon='" + inzerat.getTelefon() + "',"
                    + "lokalita='" + inzerat.getLokalita() + "',"
                    + "aktualny_link='" + inzerat.getAktualny_link() + "',"
                    + "cena='" + inzerat.getCena() + "',"
                    + "sent='" + inzerat.isOdoslany() + "',"
                    + "time_inserted='" + inzerat.getTimeInserted() + "',"
                    + "pocet_zobrazeni='" + inzerat.getPocetZobrazeni() + "',"
                    + "zaujimavy=" + inzerat.isZaujimavy() + ", "
                    + "surne=" + inzerat.isSurne() + ","
                    + "typ='" + inzerat.getTyp() + "',"
                    + "kategoria='" + inzerat.getKategoria() + "',"
                    + "precitany=" + inzerat.isPrecitany() + ","
                    + "preposlany=" + inzerat.isPreposlany() + ","
                    + "cesta='" + inzerat.getArchivCesta() + "'  WHERE id='" + inzerat.getId() + "'; \n");
        }
        //System.out.println("update inzerat: "+sql.toString());
        //System.out.println("INSERT SQL:" +sql.toString());
        if (sql.length() == 0) {
            return;
        }
        try {
            jdbcTemplate.execute(sql.toString());
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println("BAD SQL:" + sql.toString());
        }
    }

    public List<Inzerat> getSurneInzeraty() {
        try {
            RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
            String sql = "select * from inzeraty where surne=true";
            List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    public List<String> getVybraneMesta() {
        try {
            RowMapper<String> rowMapper = new RowMapper<String>() {
                public String mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("nazov");
                }
            };
            String sql = "select nazov from vybrane_mesta";
            List<String> inzeraty = new ArrayList<String>();
            inzeraty.addAll(jdbcTemplate.query(sql, rowMapper));
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }

    public void updateVybraneMesta(List<String> vybraneMesta) {
        try {
            StringBuilder sql = new StringBuilder("DELETE FROM vybrane_mesta;");
            for (String mesto : vybraneMesta) {
                sql.append("INSERT INTO vybrane_mesta(nazov) VALUES('" + mesto + "');\n");
            }
            jdbcTemplate.update(sql.toString());
            changes.firePropertyChange("vybrane_mestaUpdated", true, false);
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    public List<String> getArchivovaneInzeratyLinky() {
        try {
            RowMapper<String> rowMapper = new RowMapper<String>() {
                public String mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("aktualny_link");
                }
            };
            String sql = "select aktualny_link from archiv";
            List<String> inzeraty = new ArrayList<String>();
            inzeraty.addAll(jdbcTemplate.query(sql, rowMapper));
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    void deleteInzeratyWithLinkNotIn(List<String> aktualneLinky) {
        if (aktualneLinky.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("('");
        for (int i = 0; i < aktualneLinky.size() - 1; i++) {
            sb.append(aktualneLinky.get(i) + "','");
        }
        sb.append(aktualneLinky.get(aktualneLinky.size() - 1) + "')");
        String prikaz = "DELETE FROM inzeraty WHERE aktualny_link NOT IN" + sb.toString();
        jdbcTemplate.execute(prikaz);
    }

    private int getArchivTopID() {
        //get najvyssie id
        RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
        StringBuilder sql = new StringBuilder("select top 1 * from archiv order by id desc");
        List<Inzerat> foundBlocky = jdbcTemplate.query(sql.toString(), rowMapper);
        if (foundBlocky.size() == 0) {
            return 0;
        }
        int topID = foundBlocky.get(0).getId();
        return topID;
    }

    List<String> getInzeratyLinky() {
        try {
            RowMapper<String> rowMapper = new RowMapper<String>() {
                public String mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("aktualny_link");
                }
            };
            String sql = "select aktualny_link from inzeraty";
            List<String> inzeraty = new ArrayList<String>();
            inzeraty.addAll(jdbcTemplate.query(sql, rowMapper));
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    void inzertArchivInzeraty(List<Inzerat> inzeraty) {
        if (inzeraty == null) {
            System.out.println("inzeraty insert: inzeraty list is null");
            return;
        }
        int id = getArchivTopID();
        String time = getTimestamp();
        StringBuilder sql = new StringBuilder();
        for (Inzerat inzerat : inzeraty) {
            id++;
            sql.append("INSERT INTO archiv (id,portal,nazov,text,meno,telefon,lokalita,aktualny_link,cena,sent,time_inserted,archivacia_time,pocet_zobrazeni,zaujimavy,surne,typ,kategoria,precitany,preposlany)"
                    + "VALUES('" + id + "',"
                    + "'" + inzerat.getPortal() + "',"
                    + "'" + inzerat.getNazov() + "',"
                    + "'" + inzerat.getText() + "',"
                    + "'" + inzerat.getMeno() + "',"
                    + "'" + inzerat.getTelefon() + "',"
                    + "'" + inzerat.getLokalita() + "',"
                    + "'" + inzerat.getAktualny_link() + "',"
                    + "'" + inzerat.getCena() + "',"
                    + "'" + inzerat.isOdoslany() + "',"
                    + "'" + inzerat.getTimeInserted() + "',"
                    + "'" + time + "',"
                    + "'0',false," + inzerat.isSurne() + ","
                    + "'" + inzerat.getTyp() + "',"
                    + "'" + inzerat.getKategoria() + "',false," + inzerat.isPreposlany() + "); \n");
        }
        //System.out.println("INSERT SQL:" +sql.toString());
        if (sql.length() == 0) {
            return;
        }
        try {
            jdbcTemplate.execute(sql.toString());
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println("BAD SQL:" + sql.toString());
        }
    }

    String getSprievodnyText() {
        try {
            RowMapper<String> rowMapper = new RowMapper<String>() {
                public String mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("sprievodny_text");
                }
            };
            String sql = "select sprievodny_text from nastavenia";
            List<String> inzeraty = new ArrayList<String>();
            inzeraty.addAll(jdbcTemplate.query(sql, rowMapper));
            return inzeraty.get(0);
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    void updateSprievodnyText(String text) {
        try {
            StringBuilder sql = new StringBuilder("update nastavenia set sprievodny_text='" + text + "';");
            jdbcTemplate.update(sql.toString());
            changes.firePropertyChange("nastaveniaUpdated", true, false);
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    void nastavOdosielatela(String text) {
        try {
            StringBuilder sql = new StringBuilder("update nastavenia set odosielatel='" + text + "';");
            jdbcTemplate.update(sql.toString());
            changes.firePropertyChange("nastaveniaUpdated", true, false);
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    String getOdosielatel() {
        try {
            RowMapper<String> rowMapper = new RowMapper<String>() {
                public String mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("odosielatel");
                }
            };
            String sql = "select odosielatel from nastavenia";
            List<String> inzeraty = new ArrayList<String>();
            inzeraty.addAll(jdbcTemplate.query(sql, rowMapper));
            return inzeraty.get(0);
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    List<Inzerat> getInzeratyPreposlane() {
        try {
            RowMapper<Inzerat> rowMapper = new InzeratRowMapper();
            String sql = "SELECT * FROM inzeraty WHERE preposlany=true";
            List<Inzerat> inzeraty = jdbcTemplate.query(sql, rowMapper);
            return inzeraty;
        } catch (Exception exception) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    List<Integer> getNoveInzeratyIDs(Map<Integer, String> aktualneLinky) {
// HLAVNY PREDPOKLAD: vsetky inzeraty v DB maju ZAROVEN rovnake id aj aktualny link ako v mysql db        

// potrebujeme zistit ktore idcka este nemame v databaze
        // v tejto chvili su vsetky inzeraty v DB aktualne, iba treba zistit nove
        // zistime si vsetky idcka a tie ktore nemame budeme stahovat z mysql potom
        RowMapper<Integer> rowMapper = new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int i) throws SQLException {
                return Integer.parseInt(rs.getString("id"));
            }
        };
        String sql = "select id from inzeraty";
        List<Integer> inzeratyIDs = new ArrayList<Integer>();
        inzeratyIDs.addAll(jdbcTemplate.query(sql, rowMapper));

        List<Integer> noveIDcka = new ArrayList<Integer>();
        for (Integer idcko : aktualneLinky.keySet()) {
            if (!inzeratyIDs.contains(idcko)) {
                noveIDcka.add(idcko);
            }
        }
        return noveIDcka;
    }

//    public void opravDuplicity(Map<Integer, String> aktualneLinky) {
//        // najprv zistime duplicitne linky
//        RowMapper<String> rowMapper = new RowMapper<String>() {
//            public String mapRow(ResultSet rs, int i) throws SQLException {
//                return rs.getString("aktualny_link");
//            }
//        };
//        String sql = "select aktualny_link from (select aktualny_link, count(*)as pocet from inzeraty group by aktualny_link order by pocet desc) as T where pocet>1";
//        List<String> duplicitneLinky = new ArrayList<String>();
//        duplicitneLinky.addAll(jdbcTemplate.query(sql, rowMapper));
//
//        RowMapper<Integer> intRowMapper = new RowMapper<Integer>() {
//            public Integer mapRow(ResultSet rs, int i) throws SQLException {
//                return Integer.parseInt(rs.getString("id"));
//            }
//        };
//        List<Integer> duplicitneIDcka = new ArrayList<Integer>();
//        List<Integer> aktualneIDcka;
//        int count = 0;
//        for (String link : duplicitneLinky) {
//            count++;
//            System.out.println(count + ". opravujem link: " + link);
//            aktualneIDcka = jdbcTemplate.query("select id from inzeraty where aktualny_link='" + link + "';", intRowMapper);
//            for (int i = 0; i < aktualneIDcka.size(); i++) {
//                if (aktualneIDcka.contains(aktualneIDcka.get(i))) {
//                    // zachovame toto id
//                } else {
//                    duplicitneIDcka.add(aktualneIDcka.get(i));
//                }
//            }
//        }
//        // vymazeme vsetky duplicitne idcka
//        deleteDuplikatneInzeraty(duplicitneIDcka);
//        System.out.println("vymazanych " + duplicitneIDcka.size() + " duplicitnych inzeratov");
//    }

    public void deleteDuplikatneInzeraty(List<Integer> toDelete) {
        if (toDelete.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < toDelete.size() - 1; i++) {
            sb.append(toDelete.get(i) + ",");
        }
        sb.append(toDelete.get(toDelete.size() - 1) + ")");
        String prikaz = "DELETE FROM inzeraty WHERE id IN" + sb.toString();
        jdbcTemplate.execute(prikaz);
    }

    List<Inzerat> getInzeratyIdLink() {
        RowMapper<Inzerat> rowMapper = new RowMapper<Inzerat>() {
            public Inzerat mapRow(ResultSet rs, int i) throws SQLException {
                Inzerat novy = new Inzerat();
                novy.setId(Integer.parseInt(rs.getString("id")));
                novy.setAktualny_link(rs.getString("aktualny_link"));
                return novy;
            }
        };
        String sql = "select id,aktualny_link from inzeraty";
        List<Inzerat> inzeraty = new ArrayList<Inzerat>();
        inzeraty.addAll(jdbcTemplate.query(sql, rowMapper));
        return inzeraty;
    }
}
