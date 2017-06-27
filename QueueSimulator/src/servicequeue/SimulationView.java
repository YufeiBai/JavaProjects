/**
 * SimulationView is the window where everything is drawn for the simulation.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

import java.awt.*;
import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class SimulationView extends JFrame
{
    private final int IMAGE_WIDTH = 64;
    private final int IMAGE_HEIGHT = 64;
    public static final String GO_TEXT = "Go!";
    public static final String PAUSE_TEXT = "Pause";
    
    private int myCurrentCashier;
    private JLayeredPane myMainPanel;
    private SimulationController myController;
    private JTextField[] myOverflows;
    private JTextField myGenerateEntry, myNumEntry, myServiceEntry;
    private JTextArea myTotalStats, myCashierStats;
    private JLabel myGenerateLabel, myNumLabel, myServiceLabel, myCashierLabel;
    private JComboBox<Integer> myCashierCombo;
    private JButton myGoButton;
    private JLabel[] myCashiers;
    private JLabel[][] myCustomers;
    private ButtonListener[] myCashierListener;
    
    private ButtonListener myGoPauseListener;
    
    /**
     * Constructor for the view.
     * @param controller Controller to handle this view.
     */
    public SimulationView(SimulationController controller)
    {
        
        try
        {
            //Set the required look and feel
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            //Update the component tree - associate the look and feel with the given frame.
            SwingUtilities.updateComponentTreeUI(this);
        }//end try
        catch(Exception ex)
        {
            ex.printStackTrace();
        }//end catch
        
        // variable init
        myMainPanel = new JLayeredPane();
        myController = controller;
        myGenerateLabel = new JLabel("Generation Time: ");
        myNumLabel = new JLabel("Number of Customers: ");
        myServiceLabel = new JLabel("Max Service Time: ");
        myCashierLabel = new JLabel("Number of Cashiers: ");
        myNumEntry = new JTextField();
        myGenerateEntry = new JTextField();
        myServiceEntry = new JTextField();
        myGoButton = new JButton("Go!");
        Integer[] comboSelection = {1, 2, 3, 4, 5};
        myCashierCombo = new JComboBox<>(comboSelection);
        myTotalStats = new JTextArea("Start a simulation to see complete statistics...");
        myCashierStats = new JTextArea("Click on a cashier to see their individual statistics...");
        myCashierListener = new ButtonListener[5];
        
        // set up preliminary window stuff
        this.setSize(448, 390);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(myMainPanel);
        this.setResizable(false);
        this.setTitle("Queue Simulation");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("images/cashier.png"));
        myMainPanel.setLayout(null);
        myMainPanel.setPreferredSize(new Dimension(448, 390));
        
        // set up graphics
        JLabel myBackground = new JLabel(new ImageIcon("images/background.png"));
        myBackground.setSize(IMAGE_WIDTH * 5, IMAGE_HEIGHT * 5);
        myBackground.setLocation(0, 0);
        myMainPanel.add(myBackground, 10);
        
        // add labels and text areas (space of 5, 30)
        myNumLabel.setLocation(5, 336);
        myNumLabel.setSize(135, 12);
        myMainPanel.add(myNumLabel, 0);
        myNumEntry.setLocation(141, 330);
        myNumEntry.setSize(80, 24);
        myMainPanel.add(myNumEntry, 0);
        
        myGenerateLabel.setLocation(5, 366);
        myGenerateLabel.setSize(104, 12);
        myMainPanel.add(myGenerateLabel, 0);
        myGenerateEntry.setLocation(109, 360);
        myGenerateEntry.setSize(72, 24);
        myMainPanel.add(myGenerateEntry, 0);
        
        myServiceLabel.setLocation(240, 336);
        myServiceLabel.setSize(112, 12);
        myMainPanel.add(myServiceLabel, 0);
        myServiceEntry.setLocation(352, 330);
        myServiceEntry.setSize(92, 24);
        myMainPanel.add(myServiceEntry, 0);
        
        myCashierLabel.setLocation(200, 366);
        myCashierLabel.setSize(124, 12);
        myMainPanel.add(myCashierLabel, 0);
        
        // add combo box
        myCashierCombo.setSize(45, 25);
        myCashierCombo.setLocation(321, 359);
        myMainPanel.add(myCashierCombo, 0);
        
        // and go/pause button
        myGoButton.setSize(72, 24);
        myGoButton.setLocation(372, 359);
        myMainPanel.add(myGoButton, 0);
        
        // add stat boxes
        DefaultCaret caret1 = (DefaultCaret) myTotalStats.getCaret();
        DefaultCaret caret2 = (DefaultCaret) myCashierStats.getCaret();
        myTotalStats.setLocation(0, 0);
        myTotalStats.setSize(129, 160);
        myTotalStats.setWrapStyleWord(true);
        myTotalStats.setEnabled(false);
        myTotalStats.setDisabledTextColor(Color.BLACK);
        JScrollPane statPane1 = new JScrollPane(myTotalStats);
        statPane1.setLocation(320, 0);
        statPane1.setSize(129, 160);
        myMainPanel.add(statPane1, 0);
        myTotalStats.setLineWrap(true);
        caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        caret2.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        myCashierStats.setLocation(0, 0);
        myCashierStats.setSize(129, 160);
        myCashierStats.setWrapStyleWord(true);
        myCashierStats.setEnabled(false);
        myCashierStats.setDisabledTextColor(Color.BLACK);
        JScrollPane statPane2 = new JScrollPane(myCashierStats);
        statPane2.setLocation(320, 158);
        statPane2.setSize(129, 162);
        myMainPanel.add(statPane2, 0);
        myCashierStats.setLineWrap(true);
        
        
        // add cashiers and overflows
        myOverflows = new JTextField[ServiceQueueManager.MAX_NUMBER_OF_QUEUES];
        myCashiers = new JLabel[ServiceQueueManager.MAX_NUMBER_OF_QUEUES];
        for (int i = 0; i < myCashiers.length; i++)
        {
            myOverflows[i] = new JTextField("+0");
            myOverflows[i].setSize(IMAGE_WIDTH, 24);
            myOverflows[i].setLocation(IMAGE_WIDTH * i, 0);
            myOverflows[i].setEnabled(false);
            myOverflows[i].setDisabledTextColor(Color.BLACK);
            myOverflows[i].setHorizontalAlignment(JTextField.CENTER);
            myMainPanel.add(myOverflows[i], 0);
            myOverflows[i].setVisible(false);
            
            myCashiers[i] = new JLabel(new ImageIcon("images/cashier.png"));
            myCashiers[i].setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            myCashiers[i].setLocation(IMAGE_WIDTH * i, 256);
            myMainPanel.add(myCashiers[i], 0);
            myCashiers[i].setVisible(false);
        }
        
        // add customers
        myCustomers = new JLabel[5][7];
        for (int j = 0; j < myCustomers[0].length; j++)
        {
            for (int i = 0; i < myCustomers.length; i++)
            {
                myCustomers[i][j] = new JLabel(new ImageIcon("images/face.png"));
                myCustomers[i][j].setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
                myCustomers[i][j].setLocation(IMAGE_WIDTH * i, (IMAGE_HEIGHT/2) * j + (IMAGE_HEIGHT/2));
                myMainPanel.add(myCustomers[i][j], 10);
                myCustomers[i][j].setVisible(false);
            }
        }
        
        // set current cashier
        myCurrentCashier = -1;
        
        // set visible
        this.pack();
        this.associateListeners();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    /**
     * Update the view.
     * @param reconstruct Whether or not to reconstruct simulation primitives.
     */
    public synchronized void updateView(boolean reconstruct)
    {
        if (reconstruct) // new simulation is being run, not routine updating
        {
            for (int i = 0; i < ServiceQueueManager.MAX_NUMBER_OF_QUEUES; i++)
            {
                for (int j = 0; j < 7; j++)
                {
                    myCustomers[i][j].setVisible(false);
                }
                myCashiers[i].setVisible(false);
                myOverflows[i].setText("+0");
                myOverflows[i].setVisible(false);
            }
            for (int i = 0; i < myController.getCashierCount(); i++)
            {
                myCashiers[i].setVisible(true);
                myOverflows[i].setVisible(true);
            }
            myCurrentCashier = -1;
        }
        // continue with routine updates
        for (int i = 0; i < myController.getCashierCount(); i++)
        {
            if (myController.getQueueSize(i) < 7)
            {
                myOverflows[i].setText("+0");
                for (int j = 0; j < myController.getQueueSize(i); j++)
                {
                    myCustomers[i][6 - j].setVisible(true);
                }
                for (int j = myController.getQueueSize(i); j < 7; j++)
                {
                    myCustomers[i][6 - j].setVisible(false);
                }
            }
            else
            {
                for (int j = 0; j < 7; j++)
                {
                    myCustomers[i][j].setVisible(true);
                }
                int myOverflowCount = myController.getQueueSize(i) - 7;
                myOverflows[i].setText("+" + myOverflowCount);
            }
        }
        myController.getModel().computeStatistics();
        myTotalStats.setText("Customers Served:\n" + myController.getModel().getCustomersServed() + "\n"
                + "Total Wait Time:\n" + myController.getModel().getTotalWaitTime() + "ms\n"
                + "Total Service Time:\n" + myController.getModel().getTotalServiceTime() + "ms\n"
                + "Total Idle Time:\n" + myController.getModel().getTotalIdleTime() + "ms\n"
                + "Average Wait Time:\n" + myController.getModel().getAverageWaitTime() + "ms\n"
                + "Average Service Time:\n" + myController.getModel().getAverageServiceTime() + "ms\n"
                + "Average Idle Time:\n" + myController.getModel().getAverageIdleTime() + "ms");
        if (myCurrentCashier != -1)
        {
            int fixedCashier = myCurrentCashier + 1;
            myCashierStats.setText("Cashier #" + fixedCashier + "\n" + 
                    "Customers Served:\n" + myController.getModel().getQueue(myCurrentCashier).getCustomersServed() + "\n" + 
                    "Total Wait Time:\n" + myController.getModel().getQueue(myCurrentCashier).getWaitTime() + "ms\n"
                    + "Total Service Time:\n" + myController.getModel().getQueue(myCurrentCashier).getServiceTime() + "ms\n"
                    + "Total Idle Time:\n" + myController.getModel().getQueue(myCurrentCashier).getIdleTime() + "ms\n"
                    + "Average Wait Time:\n" + myController.getModel().getQueue(myCurrentCashier).getWaitTime()/myController.getModel().getQueue(myCurrentCashier).getCustomersServed() + "ms\n"
                    + "Average Service Time:\n" + myController.getModel().getQueue(myCurrentCashier).getServiceTime()/myController.getModel().getQueue(myCurrentCashier).getCustomersServed() + "ms\n"
                    + "Average Idle Time:\n" + myController.getModel().getQueue(myCurrentCashier).getIdleTime()/myController.getModel().getQueue(myCurrentCashier).getCustomersServed() + "ms");
        }
        else if (myCurrentCashier == -1)
        {
            myCashierStats.setText("Click on a cashier to see their individual statistics...");
        }
        
        //myController.getModel().resetStatistics();
    }
    
    /**
     * Associate mouse listeners using reflection.
     */
    private void associateListeners()
    {
        Class<? extends SimulationController> controlClass;
        Method goAndPauseMethod;
        Method cashierMethod;
        Class<?>[] classArgs;
        
        controlClass = myController.getClass();
        goAndPauseMethod = null;
        cashierMethod = null;
        classArgs = new Class[1];
        
        try
        {
           classArgs[0] = Class.forName("java.lang.Integer");
        }
        catch(ClassNotFoundException e)
        {
           String error;
           error = e.toString();
           System.out.println(error);
        }
        
        try
        {
            goAndPauseMethod = controlClass.getMethod("goAndPause", (Class<?>[])null);
            cashierMethod = controlClass.getMethod("setCashierView", classArgs);
        } 
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        } 
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        
        myGoPauseListener = new ButtonListener(myController, goAndPauseMethod, null);
        myGoButton.addMouseListener(myGoPauseListener);
        Integer[] args;
        for (int i = 0; i < 5; i++)
        {
            args = new Integer[1];
            args[0] = new Integer(i);
            myCashierListener[i] = new ButtonListener(myController, cashierMethod, args);
            myCashiers[i].addMouseListener(myCashierListener[i]);
        }
    }
    
    /**
     * Get the cashiers entered by the user.
     * @return Cashier amount entered by the user.
     */
    public int getCashierCount()
    {
        return (int)myCashierCombo.getSelectedItem();
    }
    
    /**
     * Get the maximum generate time entered by the user.
     * @return Maximum generate time entered by the user.
     */
    public int getMaxGenerateTime()
    {
        if (!myGenerateEntry.getText().matches("[0-9]++")) return -1;
        int generateTime = Integer.parseInt(myGenerateEntry.getText());
        if (generateTime <= 0) return -1;
        return generateTime;
    }
    
    /**
     * Get the customer count entered by the user.
     * @return Customer count entered by the user.
     */
    public int getCustomerCount()
    {
        if (!myNumEntry.getText().matches("[0-9]++")) return -1;
        int customers = Integer.parseInt(myNumEntry.getText());
        if (customers <= 0) return -1;
        return customers;
    }
    
    /**
     * Get the max service time entered by the user.
     * @return Max service time entered by the user.
     */
    public int getMaxServiceTime()
    {
        if (!myServiceEntry.getText().matches("[0-9]++")) return -1;
        int service = Integer.parseInt(myServiceEntry.getText());
        if (service <= 0) return -1;
        return service;
    }
    
    /**
     * Get the text of the simulation Go!/Pause button.
     * @return Text of the simulation Go!/Pause button.
     */
    public String getGoPauseText()
    {
        return myGoButton.getText();
    }
    
    /**
     * Switch the Go!/Pause text.
     */
    public void switchGoPauseText()
    {
        if (this.getGoPauseText().equals(SimulationView.GO_TEXT))
        {
            myGoButton.setText(SimulationView.PAUSE_TEXT);
        }
        else
        {
            myGoButton.setText(SimulationView.GO_TEXT);
        }
    }
    
    /**
     * Set the current cashier on the stats box.
     * @param currentCashier New current cashier.
     */
    public void setCurrentCashier(int currentCashier)
    {
        myCurrentCashier = currentCashier;
    }
}