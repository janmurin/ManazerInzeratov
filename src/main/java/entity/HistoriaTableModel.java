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
public class HistoriaTableModel extends DefaultTableModel {

    private int COLUMN_COUNT;
    private int ROW_COUNT;
    private List<PreposlanyEmail> polozky = new ArrayList<PreposlanyEmail>();
    Color[] rowColours = null;

    public HistoriaTableModel(Object[] columnNames, List<PreposlanyEmail> plzks) {
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
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
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


//    public void setMessages(List<Blocek> blocky) {
//        this.polozky.clear();
//        this.polozky.addAll(blocky);
//    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PreposlanyEmail polozka = polozky.get(rowIndex);
        switch (columnIndex) {
            case 0:
                //System.out.println("datum polozky je: "+polozka.getDatum());
                try {
                    return polozka.getDatum().substring(0, 5) + " " + polozka.getDatum().substring(11, 16).replaceAll("\\.", ":");
                } catch (Exception e) {
                    return polozka.getDatum();
                }
            case 1:
                return polozka.getPrijemca();
            case 2:
                return polozka.getSubject();
            default:
                return "invalid";
        }
    }

}
