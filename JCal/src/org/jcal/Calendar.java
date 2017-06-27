/*
 * Calendar.java
 * -------------------------------------------------
 * Description: The Calendar object for JCal.
 * Author: Brandon Belna
 * Date Last Authored: 27 July 2016
 */

package org.jcal;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

public class Calendar {
    private static final long serialVersionUID = -141715871288459547L;
    JFrame theCal = new JFrame();
    MyTableModel tableModel = new MyTableModel();
    JTable calTable = new JTable(tableModel);
    Container defaultContainer;
    JScrollPane scrollPane = new JScrollPane(calTable);
    JLabel monthYearLabel = new JLabel("January 2000");
    GregorianCalendar cal = new GregorianCalendar();
    JMenuBar menuBar = new JMenuBar();
    JMenu optionsMenu = new JMenu("Options...");
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
                    "September", "October", "November", "December" };

    JMenuItem setYearOption = new JMenuItem("Set Year");
    JMenuItem importCalendar = new JMenuItem("Import default.jcal...");
    JMenuItem exportCalendar = new JMenuItem("Export default.jcal...");

    JButton forwardButton = new JButton(">>");
    JButton previousButton = new JButton("<<");

    static int currentMonth, currentYear;
    int realDay = cal.get(GregorianCalendar.DAY_OF_MONTH), realMonth = cal.get(GregorianCalendar.MONTH), realYear = cal.get(GregorianCalendar.YEAR);

    static ArrayList<String> events = new ArrayList<>();
    // Strings in the event array list are of the following format:
    // "YYYY-MM-DD:TEXT ASSOCIATED WITH DATE"

    Color eventColor = Color.red;

    /*
     *	The Click Listener for the JTable: this listener watches for clicks on
     *	dates on the JTable.
     */
    class tableClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JTable target = (JTable)e.getSource();
                int row = target.getSelectedRow();
                int column = target.getSelectedColumn();
                int date;
                try {
                    date = (int) target.getValueAt(row, column);
                } 
                catch (NullPointerException f) {
                    date = -1;
                }
                if (date != -1) {
                    String theDay = date < 10 ? "0" + date : "" + date;
                    int currentMonthInc = currentMonth+1;
                    String theMonth = currentMonthInc < 10 ? "0" + currentMonthInc : "" + currentMonthInc;
                    String fullDate = "" + currentYear + "-" + theMonth + "-" + theDay;
                    String totalEvents = "";
                    ArrayList<Integer> markForDeletion = new ArrayList<>();
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).substring(0, 10).equals(fullDate)) {
                            totalEvents += events.get(i).substring(11);
                            markForDeletion.add(i);
                        }
                    }
                    new EventEditor(totalEvents, currentYear, currentMonth, date);
                    // this is pretty stupid
                    for (int i = 0; i < markForDeletion.size(); i++) {
                        events.remove((int)markForDeletion.get(i) - i);
                    }
                }
            }
        }
    }

    // this ActionListener listens for our setYear button to be pressed
    class setYearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer newYear = currentYear;
            int error = 0;
            try {
                newYear = currentYear;
                String newYearStr = JOptionPane.showInputDialog("What year is it?");
                if (newYearStr == null) return; // the user exited or pressed cancel
                else newYear = Integer.parseInt(newYearStr);
            } catch (NumberFormatException f) {
                JOptionPane.showMessageDialog(null, "You must input a positive integer!", "Error", JOptionPane.ERROR_MESSAGE);
                error = 1;
            }
            if (newYear < 0 && error == 0) {
                JOptionPane.showMessageDialog(null, "Unfourtanetly, the BC times aren't supported.", "Eror", JOptionPane.ERROR_MESSAGE);
                error = 1;
            }
            if (error == 0) {
                currentYear = newYear;
                updateCalendar();
            }
        }
    }
    
    class importListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JFileChooser importChooser = new JFileChooser();
            int retVal = importChooser.showOpenDialog(theCal);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File theFile = importChooser.getSelectedFile();
                if (theFile.getName().equals("default.jcal")) {
                    // theFile.getAbsolutePath()
                    Object[] options = {"Yes, please",
                        "No way!"};
                    int n = JOptionPane.showOptionDialog(theCal, "Importing this default.jcal will OVERWRITE the current default.jcal.\nAre you sure you want to import?", "Warning!", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (n == 0) { // yes
                        // begin the import process by clearing the events ArrayList ...
                        for (int i = 0; i < events.size(); i++) {
                            events.remove(0); // this works bc items in the ArrayList shift left when deleted
                        }
                        // add in the new default.jcal
                        try(BufferedReader br = new BufferedReader(new FileReader(theFile.getAbsolutePath()))) {
                            for (String line; (line = br.readLine()) != null; ) {
                                events.add(line);
                            }
                        // line is not visible here.
                        } catch (FileNotFoundException e1) {
                            JOptionPane.showMessageDialog(null, "BufferedReader threw FileNotFoundException! Failed to write default.jcal!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null, "BufferedReader threw IOException! Failed to write default.jcal!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }

                        // and overwrite
                        try (PrintWriter writer = new PrintWriter("default.jcal")) {
                            for (int i = 0; i < events.size(); i++) {
                                writer.println(events.get(i));
                            }
                        } catch (FileNotFoundException g) {
                            JOptionPane.showMessageDialog(null, "Failed to write to default.jcal!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                        JOptionPane.showMessageDialog(null, "Import completed!", "Notice", JOptionPane.INFORMATION_MESSAGE);
                        updateCalendar();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "File selected is not a default.jcal!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    class exportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JFileChooser exportChooser = new JFileChooser();
            exportChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            exportChooser.setAcceptAllFileFilterUsed(false);
            int retVal = exportChooser.showOpenDialog(theCal);
            System.out.println(exportChooser.getSelectedFile() + "\\default.jcal");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                Object[] options = {"Yes, please",
                    "No way!"};
                int n = JOptionPane.showOptionDialog(theCal, "Exporting this default.jcal will OVERWRITE any existing default.jcal in this directory.\n"
                        + "Are you sure you want to continue?", "Warning!", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (n == 0) { // that's a yes
                    // theFile.getAbsolutePath()
                    // write the file
                    try (PrintWriter writer = new PrintWriter(exportChooser.getSelectedFile() + "\\default.jcal")) {
                        for (int i = 0; i < events.size(); i++) {
                            writer.println(events.get(i));
                        }
                    } catch (FileNotFoundException g) {
                        JOptionPane.showMessageDialog(null, "Failed to write to default.jcal!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    JOptionPane.showMessageDialog(null, "Export completed!", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    updateCalendar();
                }
            }
        }
    }
    
    class forwardButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 11) {
                currentMonth = 0;
                currentYear++;
            }
            else {
                currentMonth++;
            }
            updateCalendar();
        }
    }

    class previousButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 0) {
                currentMonth = 11;
                currentYear--;
            }
            else {
                currentMonth--;
            }
            updateCalendar();
        }
    }

    class calendarRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        @Override
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            boolean eventToday = false;
            if (column == 0 || column == 6) {
                setBackground(new Color(255, 220, 220));
            }
            else {
                setBackground(new Color(255, 255, 255));
            }
            if (value != null) {
                // the event fetcher and lister
                for (int i = 0; i < events.size(); i++) {
                    if (Integer.parseInt(events.get(i).substring(0, 4)) == currentYear) {
                        if (Integer.parseInt(events.get(i).substring(5, 7)) == currentMonth+1) {
                            if (Integer.parseInt(value.toString()) == Integer.parseInt(events.get(i).substring(8, 10))) {
                                setForeground(eventColor);
                                eventToday = true;
                            }
                        }
                    }
                }
                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear){ //Today	
                    setBackground(new Color(220, 220, 255));
                }
            }
            if (!eventToday) setForeground(Color.black);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));
            return this;
        }
    }

    public Calendar() {
        //JCal format
        try(BufferedReader br = new BufferedReader(new FileReader("default.jcal"))) {
            for (String line; (line = br.readLine()) != null; ) {
                events.add(line);
            }
            // line is not visible here.
        } catch (FileNotFoundException e1) {
            // do nothing
        } catch (IOException e1) {
            // do nothing
        }
        theCal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up the window stuff
        theCal.setSize(640, 500);
        theCal.setMinimumSize(new Dimension(200, 200));
        //theCal.setResizable(false);
        theCal.setTitle("JCal Beta 1");
        theCal.setLocationRelativeTo(null);

        // grid constraints
        GridBagLayout gridLay = new GridBagLayout();
        GridBagConstraints gridConstr = new GridBagConstraints();

        // menu bar
        menuBar.add(optionsMenu);
        theCal.setJMenuBar(menuBar);
        optionsMenu.add(setYearOption);
        optionsMenu.add(importCalendar);
        optionsMenu.add(exportCalendar);
        setYearOption.addActionListener(new setYearListener());
        importCalendar.addActionListener(new importListener());
        exportCalendar.addActionListener(new exportListener());

        monthYearLabel.setText(months[currentMonth] + " " + currentYear);

        // add our components
        defaultContainer = theCal.getContentPane();
        defaultContainer.setLayout(gridLay);
        
        // set up and layout our components
        gridConstr.weightx = .5;
        gridConstr.weighty = 0;
        gridConstr.gridx = 0;
        gridConstr.gridy = 0;
        gridConstr.gridwidth = 1;
        gridConstr.gridheight = 1;
        gridConstr.fill = GridBagConstraints.HORIZONTAL;
        defaultContainer.add(previousButton, gridConstr);
        
        gridConstr.gridx = 1;
        gridConstr.fill = GridBagConstraints.NONE;
        defaultContainer.add(monthYearLabel, gridConstr);
	
        gridConstr.gridx = 2;
        gridConstr.fill = GridBagConstraints.HORIZONTAL;
        defaultContainer.add(forwardButton, gridConstr);
        
        gridConstr.weightx = 1;
        gridConstr.weighty = 1;
        gridConstr.gridx = 0;
        gridConstr.gridy = GridBagConstraints.RELATIVE;
        gridConstr.gridwidth = 3;
        gridConstr.gridheight = 2;
        gridConstr.fill = GridBagConstraints.BOTH;
        defaultContainer.add(scrollPane, gridConstr);
        
        // set up our buttons
        forwardButton.addActionListener(new forwardButtonListener());
        previousButton.addActionListener(new previousButtonListener());
        
        // hide the scroll bar if it may appear
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize (new Dimension(0,0));
        scrollPane.getHorizontalScrollBar().setPreferredSize (new Dimension(0,0));
        scrollPane.getVerticalScrollBar().setMinimumSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setMinimumSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setMaximumSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setMaximumSize(new Dimension(0, 0));

        // populate headers
        String[] dayHeaders = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            tableModel.addColumn(dayHeaders[i]);
        }
        tableModel.setRowCount(6); // set our rows
        calTable.setRowHeight(64); // default row height

        // disallow moving of headers and resizing
        calTable.getTableHeader().setReorderingAllowed(false);
        calTable.getTableHeader().setResizingAllowed(false);

        // get the month and year
        currentMonth = cal.get(GregorianCalendar.MONTH);
        currentYear = cal.get(GregorianCalendar.YEAR);

        // add listener for jtable
        calTable.addMouseListener(new tableClickListener());

        // set visible
        theCal.setVisible(true);
        
        // update everything
        updateCalendar();

        // when we close, write stuff to file
        theCal.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try (PrintWriter writer = new PrintWriter("default.jcal")) {
                    for (int i = 0; i < events.size(); i++) {
                        writer.println(events.get(i));
                    }
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Failed to write to default.jcal!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        theCal.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateCalendar();     
            }
        });
    }

    public static void saveText(String text, int year, int month, int day) {
        if (text.equals("")) return;
        String theDay = day < 10 ? "0" + day : "" + day;
        int currentMonthInc = currentMonth+1;
        String theMonth = currentMonthInc < 10 ? "0" + currentMonthInc : "" + currentMonthInc;
        String fullDate = "" + currentYear + "-" + theMonth + "-" + theDay;
        events.add("" + fullDate + ":" + text);
    }

    /*
     *	updateCalendar: redraws the calendar
     */
    public final void updateCalendar() {
        monthYearLabel.setText(months[currentMonth] + " " + currentYear); // start by updating our label
        // BUG: the calendar will never exactly fit the specified space for it because of integer rounding.
        int setHeight = (int) Math.ceil((defaultContainer.getHeight() - calTable.getTableHeader().getHeight() - forwardButton.getHeight())/6);
        if (setHeight <= 1) {
            calTable.setRowHeight(1);
        }
        else {
            calTable.setRowHeight(setHeight);
        }

        if (currentYear == 0 && currentMonth == 0) {
            previousButton.setEnabled(false);
        }
        else {
            previousButton.setEnabled(true);
        }

        // clear out table	
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                tableModel.setValueAt(null, i, j);
            }
        }

        // fetch what day is the start of the month and how many days there are
        cal.set(currentYear, currentMonth, 1);
        int startOfMonth = cal.get(GregorianCalendar.DAY_OF_WEEK);
        int dayCount = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        // we will now iterate through our rows and columns and add the day numbers
        int row = 0;
        int column = startOfMonth - 2;
        for (int i = 1; i <= dayCount; i++) {
            column++;
            if (column >= 7) {
                column = 0;
                row++;
            }
            tableModel.setValueAt(i, row, column);
        }
        calTable.setDefaultRenderer(calTable.getColumnClass(0), new calendarRenderer());
    }
}
