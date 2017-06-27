/**
 * PuzzleView constructs the window where information is entered for PuzzleEngine.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 * 
 */
package puzzleview;

import java.awt.*;
import java.lang.reflect.Method;

import puzzlecontrol.*;

import javax.swing.*;

import puzzlecontrol.PuzzleControl;

public class PuzzleView extends JFrame
{
    private JTextField myWordEntry1, myWordEntry2, mySumEntry;
    private JTextField myWordOut1, myWordOut2, mySumOut;
    private JLabel myWordLabel1, myWordLabel2, mySumLabel;
    private JLabel mySolutionLabel1, mySolutionLabel2, mySolutionLabel3;
    private JPanel myContainer;
    private JButton myResetButton, mySolveButton;
    private PuzzleControl myController;
    private ButtonListener myResetListener, mySolveListener;
    
    /**
     * PuzzleView constructor.
     * @param control The control for the view.
     */
    public PuzzleView(PuzzleControl control)
    {
        // start the container and layout manager
        myContainer = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        myContainer.setLayout(layout);
        this.setContentPane(myContainer);
        
        // set up components
        myWordLabel1 = new JLabel("Word 1: ");
        myWordLabel2 = new JLabel("Word 2: ");
        mySumLabel = new JLabel("Result Word: ");
        mySolutionLabel1 = new JLabel("Addend 1: ");
        mySolutionLabel2 = new JLabel("Addend 2: ");
        mySolutionLabel3 = new JLabel("Sum: ");
        myWordEntry1 = new JTextField(7);
        myWordEntry2 = new JTextField(7);
        mySumEntry = new JTextField(7);
        myWordOut1 = new JTextField(7);
        myWordOut2 = new JTextField(7);
        mySumOut = new JTextField(7);
        myResetButton = new JButton("Reset");
        mySolveButton = new JButton("Solve!");
        myWordOut1.setEnabled(false);
        myWordOut2.setEnabled(false);
        mySumOut.setEnabled(false);
        
        // add components
        c.gridx = 0;
        c.gridy = 0;
        myContainer.add(myWordLabel1, c);
        
        c.gridx = 1;
        myContainer.add(myWordEntry1, c);
        
        c.gridy = 1;
        c.gridx = 0;
        myContainer.add(myWordLabel2, c);
        
        c.gridx = 1;
        myContainer.add(myWordEntry2, c);
        
        c.gridy = 2;
        c.gridx = 0;
        myContainer.add(mySumLabel, c);
        
        c.gridx = 1;
        myContainer.add(mySumEntry, c);
        
        Insets labelInsets = new Insets(0, 60, 0, 0);
        Insets fieldInsets = new Insets(0, 0, 0, 0);
        
        c.insets = labelInsets;
        c.gridy = 0;
        c.gridx = 2;
        myContainer.add(mySolutionLabel1, c);
        
        c.insets = fieldInsets;
        c.gridx = 3;
        myContainer.add(myWordOut1, c);
        
        c.insets = labelInsets;
        c.gridx = 2;
        c.gridy = 1;
        myContainer.add(mySolutionLabel2, c);
        
        c.insets = fieldInsets;
        c.gridx = 3;
        myContainer.add(myWordOut2, c);
        
        c.insets = labelInsets;
        c.gridx = 2;
        c.gridy = 2;
        myContainer.add(mySolutionLabel3, c);
        
        c.insets = fieldInsets;
        c.gridx = 3;
        myContainer.add(mySumOut, c);
        
        c.insets = new Insets(10, 60, 0, 0);
        c.gridy = 3;
        c.gridx = 2;
        c.gridwidth = 2;
        myContainer.add(myResetButton, c);
        
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 0;
        c.gridwidth = 2;
        myContainer.add(mySolveButton, c);
        
        // set up variables
        myController = control;
        
        // launch
        this.pack();
        this.associateListeners();
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("CryptoArithmetic");
        this.setVisible(true);
    }
    
    /**
     * Associate button listeners.
     */
    public void associateListeners()
    {
        Class<? extends PuzzleControl> controlClass;
        Method[] clickMethods;
        Class<?>[] classArgs;
        controlClass = myController.getClass();
        
        clickMethods = new Method[2];
        
        try
        {
           clickMethods[0] = controlClass.getMethod("solvePuzzle", null);  
           clickMethods[1] = controlClass.getMethod("resetPuzzle", null);    
        }
        catch(NoSuchMethodException exception)
        {
           String error;

           error = exception.toString();
           System.out.println(error);
        }
        catch(SecurityException exception)
        {
           String error;

           error = exception.toString();
           System.out.println(error);
        }
        
        // set up the menu options
        mySolveListener = new ButtonListener(myController, clickMethods[0], null);
        myResetListener = new ButtonListener(myController, clickMethods[1], null);
        mySolveButton.addMouseListener(mySolveListener);
        myResetButton.addMouseListener(myResetListener);
    }
    
    /**
     * Sets the solutions in the view.
     * @param solution The solutions to set.
     */
    public void setSolution(int[] solution)
    {
        assert solution.length >= 3;
        myWordOut1.setText("" + solution[0]);
        myWordOut2.setText("" + solution[1]);
        mySumOut.setText("" + solution[2]);
    }
    
    /**
     * Reset text entry fields.
     */
    public void reset()
    {
        myWordEntry1.setText("");
        myWordEntry2.setText("");
        mySumEntry.setText("");
        myWordOut1.setText("");
        myWordOut2.setText("");
        mySumOut.setText("");
    }
    
    /**
     * Get the entries entered by the user.
     * @return A String[] consititng of entries.
     */
    public String[] getEntries()
    {
        String[] entries = {myWordEntry1.getText(), myWordEntry2.getText(), mySumEntry.getText()};
        return entries;
    }
}
