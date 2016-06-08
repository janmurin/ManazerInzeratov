/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deleted;

import home.managerinzeratov.Database;
import entity.Inzerat;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Janco1
 */
public class JLabelCellRenderer extends JLabel implements TableCellRenderer {

    private List<Inzerat> tableInzeraty;
    private Database database;
    private int aktRow;
    private boolean oznaceny=false;

    public JLabelCellRenderer(List<Inzerat> inzeraty, Database db, Color pozadie, int aktRow) {
        setOpaque(true);
        setBackground(pozadie);
        tableInzeraty = inzeraty;
        database = db;
        this.aktRow = aktRow;
        oznaceny=false;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //  setText((String) value);
        //System.out.println("JLabelCellRenderer row: " + row);
        if (hasFocus) {
            tableInzeraty.get(row).setZaujimavy(!tableInzeraty.get(row).isZaujimavy());
            database.updateInzerat(tableInzeraty.get(row));
        }
        if (aktRow == row && !oznaceny) {
            //c.setBackground(/*special background color*/);
            this.setBackground(new Color(199, 223, 252));
            oznaceny=true;
        }
        if (tableInzeraty.get(row).isZaujimavy()) {
            this.setIcon(new ImageIcon("pom/signedStar.png"));
        } else {
            this.setIcon(new ImageIcon("pom/unsignedStar.png"));
        }
        return this;
    }

}
