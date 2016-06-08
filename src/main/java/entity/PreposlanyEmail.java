/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.Inzerat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Janco1
 */
public class PreposlanyEmail implements Comparable<PreposlanyEmail> {

    private String prijemca = "";
    private String subject = "";
    private String cesta = "";
    private String datum = "";
    private Date sortDatum;

    public Date getSortDatum() {
        return sortDatum;
    }

    public void setSortDatum(Date sortDatum) {
        this.sortDatum = sortDatum;
    }
    private List<Inzerat> inzeraty = new ArrayList<Inzerat>();


    public String getPrijemca() {
        return prijemca;
    }

    public void setPrijemca(String prijemca) {
        this.prijemca = prijemca;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCesta() {
        return cesta;
    }

    public void setCesta(String cesta) {
        this.cesta = cesta;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public List<Inzerat> getInzeraty() {
        return inzeraty;
    }

//    public void setInzeraty(List<Inzerat> inzeraty) {
//        this.inzeraty = inzeraty;
//    }
    public int compareTo(PreposlanyEmail o) {
//        if(sortDatum.before(o.sortDatum)){
//            System.out.println(sortDatum+" JE PRED "+o.sortDatum);
//            return -1;
//        }else{
//            System.out.println(sortDatum+" JE ZA "+o.sortDatum);
//            return 1;
//        }
        if(sortDatum.getTime()-o.sortDatum.getTime()>0){
            return 1;
        }else{
            return -1;
        }
    }

}
