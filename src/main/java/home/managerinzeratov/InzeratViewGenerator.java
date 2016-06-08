/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.Inzerat;
import deleted.MainForm;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Janco1
 */
public class InzeratViewGenerator {

    private static int cislo;
    private static String galeria = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"5\" border=\"0\">\n"
            + "			<tbody><tr><td  valign=\"top\" style=\"min-width:535px\"><br /><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 1\" src=\"http://www.bazos.sk/img/1/253/45829253.jpg\" id=\"bobrazek\" />\n"
            + "			</td>\n"
            + "			<td  valign=\"top\">\n"
            + "			<br />\n"
            + "			<a onclick=\"return zobrazek('http://www.bazos.sk/img/1/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/1/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/1/253/45829253.jpg\">\n"
            + "			<img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 1\" src=\"http://www.bazos.sk/img/1t/253/45829253.jpg\" />\n"
            + "			</a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/2/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/2/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/2/253/45829253.jpg\"><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 2\" src=\"http://www.bazos.sk/img/2t/253/45829253.jpg\"/></a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/3/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/3/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/3/253/45829253.jpg\"><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 3\" src=\"http://www.bazos.sk/img/3t/253/45829253.jpg\" /></a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/4/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/4/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/4/253/45829253.jpg\"><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 4\" src=\"http://www.bazos.sk/img/4t/253/45829253.jpg\" /></a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/5/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/5/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/5/253/45829253.jpg\" /><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 5\" src=\"http://www.bazos.sk/img/5t/253/45829253.jpg\" /></a><br /></td><td  valign=\"top\"><span class=\"mobilskryt\"><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/6/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/6/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/6/253/45829253.jpg\" /><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 6\" src=\"http://www.bazos.sk/img/6t/253/45829253.jpg\" /></a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/7/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/7/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/7/253/45829253.jpg\" /><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 7\" src=\"http://www.bazos.sk/img/7t/253/45829253.jpg\" /></a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/8/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/8/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/8/253/45829253.jpg\" /><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 8\" src=\"http://www.bazos.sk/img/8t/253/45829253.jpg\" /></a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/9/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/9/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/9/253/45829253.jpg\" /><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 9\" src=\"http://www.bazos.sk/img/9t/253/45829253.jpg\" /></a><br /><a onclick=\"return zobrazek('http://www.bazos.sk/img/10/253/45829253.jpg');\" onmouseover=\"return zobrazek('http://www.bazos.sk/img/10/253/45829253.jpg');\" href=\"http://www.bazos.sk/img/10/253/45829253.jpg\" /><img class=\"obrazekv\" alt=\"3 IZB s LOGGIOU, JANIGOVA ul.,KVP, 3.posch. - TICHÁ LOKALITA - 10\" src=\"http://www.bazos.sk/img/10t/253/45829253.jpg\" /></a><br /></span></td><td ></td></tr>\n"
            + "			</tbody></table>";

//    static File getIzeratViewFile(Inzerat inzerat) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<!DOCTYPE html\n"
//                + "PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
//                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
//                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"sk\" lang=\"sk\">\n"
//                + "<head>\n"
//                + "  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n"
//                + "  <meta name=\"description\" content=\"Vyhľadávajte súkromné realitné inzeráty rýchlo a efektívne\" />\n"
//                + "  <meta name=\"author\" content=\"manazerInzeratov\" />\n"
//                + "  <link href=\"styly.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n"
//                + "  <link rel=\"stylesheet\" href=\"print.css\" type=\"text/css\" media=\"print\" />\n"
//                + "  <title>inzerat</title>\n"
//                + "  <script type=\"text/javascript\">\n"
//                + "	<!--\n"
//                + "	function zobrazek(img){\n"
//                + "		obr=document.getElementById('bobrazek');\n"
//                + "		obr.src=img;\n"
//                + "		return false;\n"
//                + "	}\n"
//                + "	// -->\n"
//                + "</script> \n"
//                + "</head>\n"
//                + "\n"
//                + "<body>\n"
//                + "<div id=\"container\">\n"
//                + "<div id=\"wrapper\" class=\"centerPage clr\">\n"
//                + "   <div id=\"header\">\n"
//                + "		<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"listainzerat\">\n"
//                + "			<tbody>\n"
//                + "				<tr >\n"
//                + "					<td><h1 class=\"nadpis\">" + inzerat.getNazov() + "</h1></td>\n"
//                + "				</tr>\n"
//                + "			</tbody>\n"
//                + "		</table>\n"
//                + "	</div> \n"
//                + "	<div id=\"content\"><div id=\"main\">\n"
//                + "		\n"
//                + "		<div id=\"info\">\n"
//                + "			<table width=\"*\" cellspacing=\"1\" cellpadding=\"0\" border=\"0\">\n"
//                + "				<tbody>\n"
//                + "					<tr>\n"
//                + "						<td   style=\"text-align:right;\">Meno:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px;\"><b>" + inzerat.getMeno() + "</b></td>\n"
//                + "					</tr>\n"
//                + "					<tr>\n"
//                + "						<td   style=\"text-align:right;\">Telefón:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px;\">" + inzerat.getTelefon() + "</td>\n"
//                + "					</tr>\n"
//                + "					<tr>\n"
//                + "						<td   style=\"text-align:right;\">Lokalita:</td>\n"
//                + "						<td style=\"text-align:left;padding-left: 10px;\">" + inzerat.getLokalita() + "</td>\n"
//                + "					</tr>\n"
//                + "					<tr>\n"
//                + "						<td  style=\"text-align:right;\">Cena:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px;\"><b>€   " + inzerat.getCena() + "</b></td>\n"
//                + "					</tr>\n"
//                + "                                     <tr>\n"
//                + "						<td  style=\"text-align:right;\">Odkaz:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px; width: 200px;word-wrap: break-word; line-height: 0.7; color:red;\"><a href=\"" + inzerat.getAktualny_link() + "\">" + inzerat.getAktualny_link() + "</a></td>\n"
//                + "					</tr>"
//                + "				</tbody>\n"
//                + "			</table>\n"
//                + "		</div>\n"
//                + "		<div id=\"text\">\n"
//                + "			" + inzerat.getText()
//                + "		</div>\n"
//                + "		<br />\n"
//                + "		<div id=\"galeria\">\n"
//                + "			" + galeria
//                + "		</div>\n"
//                + "\n"
//                + "		\n"
//                + "		<br />\n"
//                + "	</div>\n"
//                + "	</div>\n"
//                + "</div>\n"
//                + "</div>\n"
//                + "</body>\n"
//                + "</html>");
//        Writer out = null;
//        try {
//            cislo++;
//            cislo %= 5;
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("pom/inzerat" + cislo + ".html"), "UTF-8"));
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
//        File subor = null;
//        subor = new File("pom/inzerat" + cislo + ".html");
//        //subor = new File("pom/inzerat.html");
//
//        return subor;
//    }
//    static File getIzeratViewFileTlac(Inzerat inzerat) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<!DOCTYPE html\n"
//                + "PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
//                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
//                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"sk\" lang=\"sk\">\n"
//                + "<head>\n"
//                + "  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n"
//                + "  <meta name=\"description\" content=\"Vyhľadávajte súkromné realitné inzeráty rýchlo a efektívne\" />\n"
//                + "  <meta name=\"author\" content=\"manazerInzeratov\" />\n"
//                + "  <link href=\"stylyPrint.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n"
//                + "  <link rel=\"stylesheet\" href=\"print.css\" type=\"text/css\" media=\"print\" />\n"
//                + "  <title>inzerat</title>\n"
//                + "  <script type=\"text/javascript\">\n"
//                + "	<!--\n"
//                + "	function zobrazek(img){\n"
//                + "		obr=document.getElementById('bobrazek');\n"
//                + "		obr.src=img;\n"
//                + "		return false;\n"
//                + "	}\n"
//                + "	// -->\n"
//                + "</script> \n"
//                + "</head>\n"
//                + "\n"
//                + "<body>\n"
//                + "<div id=\"container\">\n"
//                + "<div id=\"wrapper\" class=\"centerPage clr\">\n"
//                + "   <div id=\"header\">\n"
//                + "		<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"listainzerat\">\n"
//                + "			<tbody>\n"
//                + "				<tr >\n"
//                + "					<td><h1 class=\"nadpis\">" + inzerat.getNazov() + "</h1></td>\n"
//                + "				</tr>\n"
//                + "			</tbody>\n"
//                + "		</table>\n"
//                + "	</div> \n"
//                + "	<div id=\"content\"><div id=\"main\">\n"
//                + "		\n"
//                + "		<div id=\"info\">\n"
//                + "			<table width=\"*\" cellspacing=\"1\" cellpadding=\"0\" border=\"0\">\n"
//                + "				<tbody>\n"
//                + "					<tr>\n"
//                + "						<td   style=\"text-align:right;\">Meno:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px;\"><b>" + inzerat.getMeno() + "</b></td>\n"
//                + "					</tr>\n"
//                + "					<tr>\n"
//                + "						<td   style=\"text-align:right;\">Telefón:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px;\">" + inzerat.getTelefon() + "</td>\n"
//                + "					</tr>\n"
//                + "					<tr>\n"
//                + "						<td   style=\"text-align:right;\">Lokalita:</td>\n"
//                + "						<td style=\"text-align:left;padding-left: 10px;\">" + inzerat.getLokalita() + "</td>\n"
//                + "					</tr>\n"
//                + "					<tr>\n"
//                + "						<td  style=\"text-align:right;\">Cena:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px;\"><b>€   " + inzerat.getCena() + "</b></td>\n"
//                + "					</tr>\n"
//                + "                                     <tr>\n"
//                + "						<td  style=\"text-align:right;\">Odkaz:</td>\n"
//                + "						<td colspan=\"2\" style=\"text-align:left;padding-left: 10px; width: 200px;word-wrap: break-word; line-height: 0.7; color:red;\"><a href=\"" + inzerat.getAktualny_link() + "\">" + inzerat.getAktualny_link() + "</a></td>\n"
//                + "					</tr>"
//                + "				</tbody>\n"
//                + "			</table>\n"
//                + "		</div>\n"
//                + "		<div id=\"text\">\n"
//                + "			" + inzerat.getText()
//                + "		</div>\n"
//                + "		<br />\n"
//                + "		<div id=\"galeria\">\n"
//                + "			" + galeria
//                + "		</div>\n"
//                + "\n"
//                + "		\n"
//                + "		<br />\n"
//                + "	</div>\n"
//                + "	</div>\n"
//                + "</div>\n"
//                + "</div>\n"
//                + "</body>\n"
//                + "</html>");
//        Writer out = null;
//        try {
//            cislo++;
//            cislo %= 5;
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("pom/inzerat" + cislo + ".html"), "UTF-8"));
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
//        File subor = null;
//        subor = new File("pom/inzerat" + cislo + ".html");
//        //subor = new File("pom/inzerat.html");
//
//        return subor;
//    }
    static File getArchivIzeratViewFile(Inzerat aktualnyInzerat) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html\n"
                + "PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"sk\" lang=\"sk\">\n"
                + "<head>\n"
                + "  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n"
                + "  <meta name=\"description\" content=\"Vyhľadávajte súkromné realitné inzeráty rýchlo a efektívne\" />\n"
                + "  <meta name=\"author\" content=\"manazerInzeratov\" />\n"
                + "  <link href=\"styly.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n"
                + "  <title>inzerat</title>\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "<div style=\" margin: 0 auto;\n"
                + "    min-width: 800px;\n"
                + "	max-width:1000px;\">\n"
                + "<img alt=\"Header\" src=\"" + aktualnyInzerat.getArchivCesta().substring(7) + "\" style=\"max-width:1000px; max-height:2000px; align:center;\"  />\n"
                + "</div>\n"
                + "</body>\n"
                + "</html>");
        Writer out = null;
        try {
            cislo++;
            cislo %= 5;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("archiv/inzerat" + cislo + ".html"), "UTF-8"));
            out.write(sb.toString());
            out.flush();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InzeratViewGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(InzeratViewGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        File subor = null;
        subor = new File("archiv/inzerat" + cislo + ".html");
        //subor = new File("pom/inzerat.html");

        return subor;
    }

    static File getEmail(ArrayList<Inzerat> preposielane, String sprievodnyText) {
        StringBuilder sb = new StringBuilder();
        sb.append(getEmailStringHtml(sprievodnyText, preposielane));

        Writer out = null;
        try {
            cislo++;
            cislo %= 5;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("pom/email" + cislo + ".html"), "UTF-8"));
            out.write(sb.toString());
            out.flush();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InzeratViewGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(InzeratViewGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        File subor = null;
        subor = new File("pom/email" + cislo + ".html");
        //subor = new File("pom/inzerat.html");

        return subor;
    }

    public static String getEmailStringHtml(String sprievodnyText, ArrayList<Inzerat> preposielane) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html\n"
                + "PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"sk\" lang=\"sk\">\n"
                + "  <head>\n"
                + "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n"
                + "	<style>\n"
                + "\n"
                + "body, td, div, .p, a {\n"
                + "    color: #000000;\n"
                + "    font-family: Verdana,Arial,Helvetica,sans-serif;\n"
                + "    font-size: 12px;\n"
                + "    line-height: 1.3;\n"
                + "}\n"
                + "A:hover {\n"
                + "    color: #cc3333;\n"
                + "    text-decoration: underline;\n"
                + "}\n"
                + "a {\n"
                + "    text-decoration: none;\n"
                + "}\n"
                + "h1 {\n"
                + "    font-size: 15px;\n"
                + "    font-weight: bold;\n"
                + "    margin: 3px 0 3px 3px;\n"
                + "}\n"
                + ".nadpiskategorie {\n"
                + "    font-size: 15px;\n"
                + "    font-weight: bold;\n"
                + "    margin: 0 0 3px 3px;\n"
                + "}\n"
                + ".nadpiskat {\n"
                + "    font-size: 12px;\n"
                + "    font-weight: normal;\n"
                + "    margin: 3px 0 3px 3px;\n"
                + "}\n"
                + ".listah {\n"
                + "    background-color: #ff6600;\n"
                + "    border: 1px solid #bf4c00;\n"
                + "    margin: 3px;\n"
                + "}\n"
                + ".sirka {\n"
                + "    max-width: 1500px;\n"
                + "}\n"
                + ".bila a, .bila a:hover {\n"
                + "    color: #ffffff;\n"
                + "}\n"
                + ".listal {\n"
                + "    border-right: 1px dashed #bbbbbb;\n"
                + "    margin: 10px;\n"
                + "}\n"
                + ".listahl {\n"
                + "    border-right: 1px dashed #dddddd;\n"
                + "    margin: 10px;\n"
                + "}\n"
                + ".velikost10, .velikost10 a, .velikost10 A:hover {\n"
                + "    font-size: 11px;\n"
                + "}\n"
                + ".thl A, .thl A:hover {\n"
                + "    color: #000000;\n"
                + "    font-size: 13px;\n"
                + "    line-height: 1.5;\n"
                + "}\n"
                + ".inzeraty {\n"
                + "    border-top: 1px solid #dddddd;\n"
                + "    padding: 5px;\n"
                + "}\n"
                + ".inzeraty:hover {\n"
                + "    background-color: #fbf3e1;\n"
                + "}\n"
                + ".listainzerat {\n"
                + "    background-color: #ffd9bf;\n"
                + "    border: 1px solid #c7a995;\n"
                + "    padding: 5px;\n"
                + "}\n"
                + ".inzerat {\n"
                + "    background-color: #ffffff;\n"
                + "}\n"
                + "a:link {\n"
                + "    color: #FF0000;\n"
                + "}\n"
                + "\n"
                + "/* visited link */\n"
                + "a:visited {\n"
                + "    color: #009933;\n"
                + "}\n"
                + "\n"
                + "/* mouse over link */\n"
                + "a:hover {\n"
                + "    color: #FF00FF;\n"
                + "}\n"
                + "\n"
                + "/* selected link */\n"
                + "a:active {\n"
                + "    color: #0000FF;\n"
                + "}\n"
                + ".barevne {\n"
                + "    background-color: #ffd9bf;\n"
                + "    border: 1px solid #e4c2ab;\n"
                + "}\n"
                + ".cisla {\n"
                + "    background-color: #ffd9bf;\n"
                + "    border: 1px solid #e4c2ab;\n"
                + "    margin: -2px;\n"
                + "    padding: 4px;\n"
                + "}\n"
                + ".nadpis {\n"
                + "    display: inline;\n"
                + "    font-size: 12px;\n"
                + "    font-weight: bold;\n"
                + "    word-wrap: break-word;\n"
                + "}\n"
                + ".nadpis a {\n"
                + "    text-decoration: underline;\n"
                + "}\n"
                + ".nadpis A:hover {\n"
                + "    text-decoration: none;\n"
                + "}\n"
                + ".obrazek {\n"
                + "    border: 1px solid Black;\n"
                + "    float: left;\n"
                + "    margin: 5px 6px 10px 0;\n"
                + "}\n"
                + ".obrazekv {\n"
                + "    border: 1px solid Black;\n"
                + "    margin: 4px;\n"
                + "}\n"
                + ".obrazekm {\n"
                + "    margin: 4px;\n"
                + "}\n"
                + ".ztop {\n"
                + "    color: #ff0000;\n"
                + "    font-weight: bold;\n"
                + "}\n"
                + ".ovse {\n"
                + "    color: #c00029;\n"
                + "}\n"
                + "#zvyraznenikat {\n"
                + "    color: #cc3333;\n"
                + "    font-weight: bold;\n"
                + "}\n"
                + ".cena {\n"
                + "    padding: 0 10px 0 5px;\n"
                + "}\n"
                + ".popis {\n"
                + "    padding-top: 5px;\n"
                + "    word-wrap: break-word;\n"
                + "}\n"
                + ".akce {\n"
                + "    color: #5d5d5d;\n"
                + "    font-size: 11px;\n"
                + "    margin-right: 7px;\n"
                + "    visibility: hidden;\n"
                + "}\n"
                + ".vypis:hover .akce {\n"
                + "    visibility: visible;\n"
                + "}\n"
                + "	</style>\n"
                + "  </head>\n"
                + "  <body>\n"
                + "   <div class=\"popis\">\n"
                + sprievodnyText.replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;")
                + "	</div>\n"
                + "	<table width=\"99%\" cellspacing=\"2\" cellpadding=\"10\" border=\"0\">\n"
                + "	<tbody>\n"
                + "		<tr>\n"
                + "		<td width=\"*\" valign=\"top\">\n"
                + "\n"
                //                + "		<table width=\"100%\" cellspacing=\"8\" cellpadding=\"0\" border=\"0\" align=\"center\">\n"
                //                + "			<tbody>\n"
                //                + "			<tr>\n"
                //                + "				<td><div align=\"center\"><h2 class=\"nadpiskategorie\">2 izbový byt  - predaj</h2><h2 class=\"nadpiskat\">2 izbový byt - inzercia, inzeráty</h2></div></td>\n"
                //                + "			</tr>\n"
                //                + "			</tbody>\n"
                //                + "		</table>\n"
                + "		<br>\n"
                + "\n");
        int count = 0;
        for (Inzerat inz : preposielane) {
            if (inz.isZaujimavy()) {
                count++;
                String text = inz.getText();
                text = text.replaceAll("\n", "<br>");
                //text=text.replaceAll(" ", "&nbsp;");
                sb.append("		<span class=\"vypis\">\n"
                        + "			<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"inzeraty\">\n"
                        + "				<tbody>\n"
                        + "				<tr>\n"
                        + "					<td width=\"83%\">\n"
                        + "					<span class=\"nadpis\">\n"
                        + "					<a target=\"_blank\" href=\"" + inz.getAktualny_link() + "\">" + count + ". " + inz.getNazov() + "</a>\n"
                        + "					</span><br>\n"
                        + "					<a target=\"_blank\" href=\"" + inz.getAktualny_link() + "\">" + inz.getAktualny_link()+ "</a>\n"
                        + "					</span><br>\n"
                        + "					<span class=\"velikost10\"> [" + formatujDatum(inz.getTimeInserted()) + "]</span>\n"
                        + "					<br>\n"
                        //+ "					<a href=\"/"+inz.getAktualny_link()+"\"><img border=\"0\" alt=\""+inz.getNazov()+"\" class=\"obrazek\" src=\"http://www.bazos.sk/img/1t/045/46713045.jpg\"></a>\n"
                        + "					<div class=\"popis\">" + text + "</div>\n"
                        + "					<br><br>\n"
                        + "					</td>\n"
                        + "					<td width=\"17%\" valign=\"top\">" + inz.getLokalita() + "\n"
                        + "					</td>\n"
                        + "				</tr>\n"
                        + "				</tbody>\n"
                        + "			</table>\n"
                        + "		</span>\n"
                        + "		<br>\n"
                        + "		\n");
            }
        }

        sb.append("		<br>\n"
                + "		</td>\n"
                + "		</tr>\n"
                + "	</tbody>\n"
                + "	</table>\n"
                + "  </body>\n"
                + "</html>");
        return sb.toString();
    }

    public static String formatujDatum(String timeInserted) {
        timeInserted = timeInserted.substring(0, 16);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
        String den = sdf.format(new Date(System.currentTimeMillis()));
        String datum = "";
//        System.out.println("aktualny den: " + den);
//        System.out.println("vybrany den: " + timeInserted.split(" ")[0].split("-")[2] + "." + timeInserted.split(" ")[0].split("-")[1]);
//        if (den.equals(timeInserted.split(" ")[0].split("-")[2] + "." + timeInserted.split(" ")[0].split("-")[1].trim())) {
//            datum = datum += timeInserted.split(" ")[1];
//        } else {
        datum = timeInserted.split(" ")[0].split("-")[2] + ". " + timeInserted.split(" ")[0].split("-")[1] + ". " + timeInserted.split(" ")[0].split("-")[0] + " ";
        datum += timeInserted.split(" ")[1];
//        }
        return datum;
    }
}
