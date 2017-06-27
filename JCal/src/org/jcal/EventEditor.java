/*
 * EventEditor.java
 * -------------------------------------------------
 * Description: The EventEditor is a small notepad which allows text to be edited. It is used
 * 				to edit strings in JCal's event ArrayList.
 * Author: Brandon Belna
 * Date Last Authored: 27 July 2016
 */

package org.jcal;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class EventEditor {
    private static final long serialVersionUID = -5004823202713150163L;
    JFrame winEdit = new JFrame();
    JTextArea editor = new JTextArea(1, 1);
    JLabel editText = new JLabel("NULL");
    JScrollPane editorScroller = new JScrollPane(editor);
    // JPanel editorPanel = new JPanel();
    JButton saveButton = new JButton("Save");
    Container editorContainer;

    static ArrayList<String> openWindows = new ArrayList<>();

    public EventEditor() {
        // for whatever stupid reason this may happen ...
        JOptionPane.showMessageDialog(null, "Constructor failed to pass arguments for JCal Editor!", "JCal Editor Fatal Error", JOptionPane.ERROR_MESSAGE);
    }

    class saveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            killWindow();
        }
    }

    public EventEditor(String loadText, int year, int month, int day) {
        // does a window already exist with these parameters?
        for (int i = 0; i < openWindows.size(); i++) {
            if (openWindows.get(i).equals("" + year + month + day)) {
                JOptionPane.showMessageDialog(null, "A window with this date is already open!", "JCal Editor Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // nope, add it to the array list of open windows
        openWindows.add("" + year + month + day);
        int indexToRemove = openWindows.size()-1;

        // set our editor text..
        editor.setText(loadText);

        // usual JFrame stuff ...
        winEdit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // winEdit.setResizable(false);
        winEdit.setTitle("JCal Editor Beta 1");
        winEdit.setSize(400, 500);
        
        // set min size
        winEdit.setMinimumSize(new Dimension(200, 200));

        // close listener
        winEdit.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Calendar.saveText(editor.getText(), year, month, day);
                openWindows.remove(indexToRemove);
            }
        });

        // set our containers and panels
        editorContainer = winEdit.getContentPane();
        
        // grid layout stuff
        GridBagLayout gridLay = new GridBagLayout();
        GridBagConstraints gridConstr = new GridBagConstraints();
        editorContainer.setLayout(gridLay);

        // set up label and add it
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
                        "September", "October", "November", "December" };
        String[] dayEndings = {"st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th",
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", 
                        "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th",
                        "st"};
        
        
        editText.setText("Editing events for " + months[month] + " " + day + dayEndings[day - 1] + " " + year + "...");
        
        // add stuff
        gridConstr.weightx = 1;
        gridConstr.weighty = .01;
        gridConstr.gridx = 0;
        gridConstr.gridy = 0;
        gridConstr.gridwidth = 1;
        gridConstr.gridheight = 1;
        gridConstr.fill = GridBagConstraints.HORIZONTAL;
        editorContainer.add(editText, gridConstr);
        
        gridConstr.weightx = 1;
        gridConstr.weighty = 1;
        gridConstr.gridx = 0;
        gridConstr.gridy = GridBagConstraints.RELATIVE;
        gridConstr.gridwidth = 3;
        gridConstr.gridheight = 1;
        gridConstr.fill = GridBagConstraints.BOTH;
        editorContainer.add(editorScroller, gridConstr);

        // set up button
        gridConstr.weighty = 0;
        gridConstr.gridheight = 0;
        editorContainer.add(saveButton, gridConstr);
        saveButton.addActionListener(new saveButtonListener());

        winEdit.setLocationRelativeTo(null);
        winEdit.setVisible(true);
    }

    public void killWindow() {
        winEdit.dispatchEvent(new WindowEvent(winEdit, WindowEvent.WINDOW_CLOSING));
    }
}
