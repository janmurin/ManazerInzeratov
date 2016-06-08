/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Janco1
 */
public class PortalyComboboxModel extends DefaultComboBoxModel<Object>{
 
    private List<String> portaly;

    public PortalyComboboxModel(List<String> portaly) {
        this.portaly = portaly;
    }
    
    
    
        @Override
    public int getSize() {
        return portaly.size();
    }

    @Override
    public Object getElementAt(int index) {
        return portaly.get(index);
    }
}
