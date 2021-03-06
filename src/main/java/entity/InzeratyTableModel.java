/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Janco1
 */
public class InzeratyTableModel extends DefaultTableModel {

    private int COLUMN_COUNT;
    private int ROW_COUNT;
    private List<Inzerat> polozky = new ArrayList<Inzerat>();
    Color[] rowColours = null;

    public InzeratyTableModel(Object[] columnNames, List<Inzerat> plzks) {
        super(columnNames, plzks.size());
        polozky.addAll(plzks);
        ROW_COUNT = polozky.size();
        rowColours = new Color[ROW_COUNT];
        COLUMN_COUNT = columnNames.length;
        //System.out.println(columnNames[1]);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Icon.class;
            case 1:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
//            case 5:
//                return String.class;
//            case 6:
//                return Double.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Color[] getRowColours() {
        return rowColours;
    }

    public void setRowColour(int row, Color c) {
        rowColours[row] = c;
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
        return rowColours[row];
    }

    @Override
    public int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public boolean isPrecitany(int row) {
        return polozky.get(row).isPrecitany();
    }

//    public void setMessages(List<Blocek> blocky) {
//        this.polozky.clear();
//        this.polozky.addAll(blocky);
//    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Inzerat polozka = polozky.get(rowIndex);
        switch (columnIndex) {
            case 1:
                return polozka.getNazov();
            case 5:
                return formatujDatum(polozka.getTimeInserted().substring(0, 16));
            case 3:
                return polozka.getMeno();
            case 4:
                return polozka.getLokalita().trim();
//            case 0:
//                //return novy;
//            return new ImageIcon("pom/signedStar.png");
//            case 4:
//                return polozka.getNote();
//            case 5:
//                return polozka.getOwner();
//            case 6:
//                return polozka.getValue();
            default:
                return "invalid";
        }
    }

    private String formatujDatum(String timeInserted) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
        String den = sdf.format(new Date(System.currentTimeMillis()));
        String datum = "";
//        System.out.println("aktualny den: " + den);
//        System.out.println("vybrany den: " + timeInserted.split(" ")[0].split("-")[2] + "." + timeInserted.split(" ")[0].split("-")[1]);
        if (den.equals(timeInserted.split(" ")[0].split("-")[2] + "." + timeInserted.split(" ")[0].split("-")[1].trim())) {
            datum = datum += timeInserted.split(" ")[1];
        } else {
            datum = timeInserted.split(" ")[0].split("-")[2] + ". " + timeInserted.split(" ")[0].split("-")[1] + ". " + timeInserted.split(" ")[0].split("-")[0] + " ";
            datum += timeInserted.split(" ")[1];
        }
        return datum;
    }
}
