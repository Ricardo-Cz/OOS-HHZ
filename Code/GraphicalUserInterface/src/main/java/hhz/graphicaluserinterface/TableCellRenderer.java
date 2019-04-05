/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

/**
 *
 * @author Ricardo
 */
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import static java.awt.Color.*;
import static javafx.scene.paint.Color.GREY;

public class TableCellRenderer
        extends DefaultTableCellRenderer {

    int a_row = 0;
    int a_column = 0;
        
    String[][] status_table = new String [16][16];
    
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        Component c
                = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus,
                        row, column);

        // Only for specific cell
        
        if (status_table[row][column] == "Grün") {
            c.setBackground(GREEN);
            return c;
        } else if (status_table[row][column] == "Rot") {
            c.setBackground(RED);
            return c;
        } else if (status_table[row][column] == "Gelb") {
            c.setBackground(YELLOW);
            return c;
        } else if (status_table[row][column] == "Orange") {
            c.setBackground(ORANGE);
            return c;
        } else {
            c.setBackground(GRAY);
            return c;
        }
    }
}
