/**
 * The glue of the Connect4 GUI. Communicates input to the view and model.
 * Also responsible for synching the view and model together.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectcontrol;

import javax.swing.JOptionPane;
import connectmodel.ComputerPlayer;
import connectmodel.GameBoard;
import connectmodel.GameEngine;
import connectmodel.PieceType;
import connectmodel.Player;
import connectview.ConnectView;

import java.util.Timer;
import java.util.TimerTask;

public class Controller
{
    public final static int myWinLength = 4;
    private ConnectView myView;
    private GameEngine myGameEngine;
    private GameBoard myGameBoard;
    private PieceType[] myPieces;
    private Player myPlayer;
    private ComputerPlayer myCPUPlayer;
    private int myMouseCurrentColumn = 0;
    private boolean myWait = true; // myWait is the universal game lock. When it is switched to true,
                                   // no actions on the board can be performed by the user.
    private Timer myCpuTimer = new Timer(); // this timer allows our cpu to 'think'.
    
    /**
     * Constructor for the controller.
     */
    public Controller()
    {
        myPieces = new PieceType[2];
        myPieces[0] = PieceType.RED;
        myPieces[1] = PieceType.BLACK;
        myGameBoard = new GameBoard(ConnectView.myRows, ConnectView.myColumns, myWinLength, myPieces);
        myPlayer = new Player("NA", myPieces[0]);
        myView = new ConnectView(this);
        myCpuTimer = new Timer();
    }
    
    /**
     * Place a piece in the specified column, and update the corresponding view and model.
     * @param col The column to place piece.
     */
    public void placePiece(Integer col)
    {
        if (!myGameBoard.isColumnFull(col) && !myWait)
        {
            myGameBoard.placePiece(col, myPlayer.getPieceType());
            myView.updateBoard();
            if (myGameBoard.checkIfWin())
            {
                myPlayer.incrementScore();
                myView.drawVictory(myPlayer);
                myWait = true; // lock the game
                return;
            }
            myGameEngine.switchPlayerUp();
            myView.updateBoard();
            cpuTurn();
        }
        if (myGameBoard.isBoardFull())
        {
            JOptionPane.showMessageDialog(null, "Stalemate!");
            myGameBoard.resetBoard();
            myView.updateBoard();
        }
    }
    
    /**
     * Start a match. Initializes the view and model.
     */
    public void startMatch()
    {
        if (myGameEngine != null)
        {
            int dialogResult = JOptionPane.showConfirmDialog (null, "This will erase your scores! Are you sure you want to start a new match?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if(dialogResult != JOptionPane.YES_OPTION)
            {
                return;
            }
        }
        // reset cpu stack
        myCpuTimer.cancel();
        
        // get our name
        String name = (String)JOptionPane.showInputDialog(null, "Enter your name. Only letters and numbers allowed!", "Start Match!",
                JOptionPane.PLAIN_MESSAGE, null, null, "JohnCena");
        if (name == null) return;
        // get our color
        Object[] choices = {"Red", "Black", "Green", "Yellow"};
        String colorChoice = (String)JOptionPane.showInputDialog(null, "Pick a piece color!", "Start Match!", JOptionPane.PLAIN_MESSAGE,
                null, choices, "Red");
        if (colorChoice == null) return;
        PieceType realChoice;
        realChoice = PieceType.RED;
        if (colorChoice.equals("Red")) realChoice = PieceType.RED;
        if (colorChoice.equals("Black")) realChoice = PieceType.BLACK;
        if (colorChoice.equals("Green")) realChoice = PieceType.GREEN;
        if (colorChoice.equals("Yellow")) realChoice = PieceType.YELLOW;
        
        // instantiate the player
        myPieces[0] = realChoice;
        myPlayer = new Player(name, realChoice);
        
        // start the GameEngine
        myGameEngine = new GameEngine(myPlayer, myGameBoard);
        myGameEngine.startGame();
        myCPUPlayer = (ComputerPlayer) myGameEngine.getPlayers().get(1);
        myPieces[1] = myGameEngine.getPlayers().get(1).getPieceType();
        
        // refresh board
        myView.updateBoard();
        myView.drawNames();
        
        // release the game lock
        myWait = false;
    }
    
    /**
     * Start a new game. Resets the view.
     */
    public void newGame()
    {
        // reset cpu tasks
        myCpuTimer.cancel();
        
        // use game engine to start model
        if (myGameEngine == null) 
        {
            JOptionPane.showMessageDialog(null, "You need to start a match! Click New Match under Game.");
            return;
        }
        myGameEngine.startGame();
        
        // clear board and redraw
        myGameBoard.resetBoard();
        myView.updateBoard();
        
        // release game lock
        myWait = false;
        
        // check if cpu turn
        if (myGameEngine.getStartingPlayer().equals(myCPUPlayer))
        {
            this.cpuTurn();
        }
    }
    
    /**
     * The CPU turn. Creates a TimerTask which runs after a small delay.
     */
    public void cpuTurn()
    {
        int cpuCol = myCPUPlayer.nextMove();
        // we will wait a bit before having the cpu place its piece
        myWait = true;
        myCpuTimer = new Timer();
        myCpuTimer.schedule(new TimerTask() {
            public void run()
            {
                myGameBoard.placePiece(cpuCol, myPieces[1]); // place the piece
                myView.updateBoard(); // update our board to draw the new piece
                if (myGameBoard.checkIfWin())
                {
                    myCPUPlayer.incrementScore();
                    myView.drawVictory(myCPUPlayer);
                    myWait = true; // lock the game
                    return;
                }
                myGameEngine.switchPlayerUp();
                myView.updateBoard();
                myWait = false;
                if (myMouseCurrentColumn != -1) possiblePiece(myMouseCurrentColumn); // -1 would mean the mouse has left the frame
            }  
        }, 2000);
    }
    
    /**
     * Shows the possible piece placement for a given column.
     * @param col The column to light up for a possible piece.
     */
    public void possiblePiece(Integer col)
    {
        if (!myGameBoard.isColumnFull(col)) myView.showPossiblePlacement(col);
        myMouseCurrentColumn = col;
    }
    
    /**
     * Repaint the view.
     */
    public void repaintView()
    {
        if (!myWait)
        {
            myView.updateBoard();
        }
        myMouseCurrentColumn = -1;
    }
    
    /**
     * Get GameBoard associated with this controller.
     */
    public GameBoard getGameBoard()
    {
        return myGameBoard;
    }
    
    /**
     * Get GameEngine associated with this listener.
     * @return The GameEngine associated with the listener.
     */
    public GameEngine getGameEngine()
    {
        return myGameEngine;
    }
    
    /**
     * Returns the status of the universal game lock.
     * @return The current status of the lock.
     */
    public boolean getWaitStatus()
    {
        return myWait;
    }
    
    /**
     * Get the human player.
     * @return Current human player.
     */
    public Player getPlayer()
    {
        return myPlayer;
    }
    
    /**
     * Get the computer player.
     * @return Current computer player.
     */
    public ComputerPlayer getComputerPlayer()
    {
        return myCPUPlayer;
    }
}
