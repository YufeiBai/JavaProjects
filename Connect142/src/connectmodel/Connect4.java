/**
 * Entry point for our Connect 4 game.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectmodel;

import connectcontrol.Controller;

public class Connect4
{
    private Controller myController;
    
    /**
     * Entry point. Creates a new instance of this class which constructs a controller.
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        new Connect4();
    }
    
    /**
     * Constructor. Sets the controller for our game.
     */
    public Connect4()
    {
        setController(new Controller());
    }
    
    /**
     * Set a new game controller.
     * @param controller The new controller.
     */
    public void setController(Controller controller)
    {
        myController = controller;
    }
}
