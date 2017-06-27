/**
 * The controller for the Puzzle.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package puzzlecontrol;

import javax.swing.JOptionPane;

import puzzlemodel.*;
import puzzleview.*;

public class PuzzleControl
{
    private PuzzleView myView;
    private PuzzleEngine myModel;
    
    /**
     * Constructor for the PuzzleControl.
     */
    public PuzzleControl()
    {
        myView = new PuzzleView(this);
        myModel = new PuzzleEngine();
    }
    
    /**
     * Called when the Solve! button is pressed.
     */
    public void solvePuzzle()
    {
        myModel.setPuzzle(myView.getEntries()[0], myView.getEntries()[1], myView.getEntries()[2]);
        boolean solveable = myModel.solvePuzzle();
        if (!solveable)
        {
            JOptionPane.showMessageDialog(null, "Puzzle is not solveable!");
            return;
        }
        myView.setSolution(myModel.getSolution());
    }
    
    /**
     * Reset the Puzzle view.
     */
    public void resetPuzzle()
    {
        myModel.setPuzzle("", "", "");
        myView.reset();
    }
}
