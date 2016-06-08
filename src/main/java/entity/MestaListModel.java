/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Janco1
 */
public class MestaListModel extends DefaultListModel<String>{

    public List<String> mesta;

    public MestaListModel(List<String> noveMesta) {
        mesta=noveMesta;
    }

   
    @Override
    public int getSize() {
       return mesta.size();
    }
@Override
    public String getElementAt(int index) {
       return mesta.get(index);
    }

    
}
