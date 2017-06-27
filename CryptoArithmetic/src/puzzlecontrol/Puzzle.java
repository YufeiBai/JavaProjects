/**
 * The initial point for our Puzzle application.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 * 
 */
package puzzlecontrol;

public class Puzzle
{
    private PuzzleControl myController;
    
    /**
     * Main method.
     * @param args Arguments passed through command line.
     */
    public static void main(String[] args)
    {
        new Puzzle();
    }
    
    /**
     * Constructor. Creates a new controller.
     */
    public Puzzle()
    {
        setController(new PuzzleControl());
    }
    
    /**
     * Setter for the controller of our Puzzle.
     * @param controller The new controller.
     */
    public void setController(PuzzleControl controller)
    {
        myController = controller;
    }
}
