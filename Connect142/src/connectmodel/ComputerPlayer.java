/**
 * ComputerPlayer is a simple extension of the Player class to allow for computer calculated moves.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectmodel;

/*
 * This class may not be necessary...
 */

public class ComputerPlayer extends Player
{
    private GameBoard myGameBoard;
    /**
     * AI Constructor.
     * @param name Name of the AI.
     * @param type The PieceType for the AI to use.
     */
    public ComputerPlayer(String name, PieceType type, GameBoard board)
    {
        super(name, type);
        myGameBoard = board;
    }
    /**
     * This is supposed to figure out the next move the AI, however, this is done in GameBoard
     * under findNextBestMoveColumn, so...
     * @return The column for the AI to place its token.
     */
    public int nextMove()
    {
        return myGameBoard.findBestMoveColumn(myPieceType);
    }
}