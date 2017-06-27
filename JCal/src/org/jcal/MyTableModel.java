/*
 * MyTableModel.java
 * -------------------------------------------------
 * Description: An edit of the DefaultTableModel to disallow editing of cells.
 * Author: Brandon Belna
 * Date Last Authored: 22 July 2016
 */

package org.jcal;

import javax.swing.table.DefaultTableModel;

/*
 *	MyTableModel: a stupid extension of the DefaultTableModel to disable editing of entries.
 */
public class MyTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 5958289247186984401L;

    @Override
    public boolean isCellEditable(int row, int column){  
        return false;  
  }
}