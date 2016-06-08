/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.Inzerat;
import java.util.List;

/**
 *
 * @author Janco1
 */
public class Opravy {
    private static MySQLDatabase mysql;

    public static void main(String[] args) {
        Database database = new Database();

        String DB_URL = "jdbc:mysql://mysql51.websupport.sk:3309/crawlerDB2?characterEncoding=UTF-8";
        String PASS = "dbuser2";
        String USER = "Abkit8ol\"";
        mysql = new MySQLDatabase(DB_URL, USER, PASS);

        List<String> linky = database.getInzeratyLinky();
        List<Inzerat> inzeraty = mysql.getInzeratyListLinkNOTIn(linky);
        database.inzertArchivInzeraty(inzeraty);
        
        //List<Inzerat> inzeraty=database.getArchivInzeratyList();
        database.inzertInzeraty(inzeraty);
    }
}
