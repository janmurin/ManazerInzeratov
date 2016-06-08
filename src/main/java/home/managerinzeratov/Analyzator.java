/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.Kategoria;
import entity.Inzerat;
import deleted.MainForm;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Janco1
 */
public class Analyzator {

    public static void main(String[] args) throws IOException {
//        Database db = new Database();
//        List<Inzerat> inzeraty = db.getInzeratyList("http://reality.bazos.sk");
//        int nevyuziteZnaky = 0;
//        Map<Integer, Integer> poctyZnakov = new TreeMap<Integer, Integer>();
//        poctyZnakov.put(250, 0);
//        poctyZnakov.put(500, 0);
//        poctyZnakov.put(750, 0);
//        poctyZnakov.put(1500, 0);
//        poctyZnakov.put(4000, 0);
//        poctyZnakov.put(4001, 0);
//        StringBuilder sb = new StringBuilder();
//        for (Inzerat inz : inzeraty) {
//            sb.append(getInsertScript(inz) + "\n");
//            int dlzka = inz.getText().length();
//            nevyuziteZnaky += 4000 - dlzka;
//            if (dlzka < 250) {
//                poctyZnakov.put(250, poctyZnakov.get(250) + 1);
//            } else {
//                if (dlzka < 500) {
//                    poctyZnakov.put(500, poctyZnakov.get(500) + 1);
//                } else {
//                    if (dlzka < 750) {
//                        poctyZnakov.put(750, poctyZnakov.get(750) + 1);
//                    } else {
//                        if (dlzka < 1500) {
//                            poctyZnakov.put(1500, poctyZnakov.get(1500) + 1);
//                        } else {
//                            if (dlzka < 4000) {
//                                poctyZnakov.put(4000, poctyZnakov.get(4000) + 1);
//                            } else {
//                                poctyZnakov.put(4001, poctyZnakov.get(4001) + 1);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        double vsetkychZnakov = (inzeraty.size() * 4000);
//        System.out.println("celkom potrebnych znakov : " + vsetkychZnakov);
//        double novyPocet = poctyZnakov.get(250) * 250 + poctyZnakov.get(500) * 500 + poctyZnakov.get(750) * 750 + poctyZnakov.get(1500) * 1500 + poctyZnakov.get(4000) * 4000;
//        double uspora = novyPocet / vsetkychZnakov;
//        System.out.println("novyPocet znakov: " + novyPocet);
//        System.out.println("kolko % novych zo vsetkych: " + uspora);
//        double nevyuzitychZnakov = nevyuziteZnaky / vsetkychZnakov;
//        System.out.println("pocty znakov: " + poctyZnakov);
//        System.out.println("nevyuzitych znakov:" + nevyuziteZnaky + " v % : " + nevyuzitychZnakov);

            for (int i=0; i<Kategoria.values.length; i++){
                if (Kategoria.values[i].length()>20){
                    System.out.println(Kategoria.values[i]+" je vecsie ako 20");
                }
            }
        
//        Writer out = null;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("inzeraty.txt"), "UTF-8"));
//            out.write(sb.toString());
//            out.flush();
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(InzeratViewGenerator.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//        for (int i=0; i<vsetkychZnakov-novyPocet; i+=10){
//            sb.append("1234567890");
//        }
//        out = null;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("inzeraty2.txt"), "UTF-8"));
//            out.write(sb.toString());
//            out.flush();
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(InzeratViewGenerator.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
    }

    private static String getInsertScript(Inzerat inzerat) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO inzeraty (id,portal,nazov,text,meno,telefon,lokalita,aktualny_link,cena,sent,time_inserted,pocet_zobrazeni,zaujimavy,surne,typ,kategoria,precitany)"
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
                + "'" + inzerat.getKategoria() + "',false); \n");
        return sql.toString();
    }

}
