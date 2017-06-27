/**
 * GameEngine is the glue of the Connect 4 project. However, it does a lot less work than GameBoard
 * does when it comes to actually managing the game. GameEngine simply controls players who need to play, as well
 * as handling piece placement for individual players.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectmodel;

import java.util.Vector;

public class GameEngine 
{
    private Vector<Player> myPlayers;
    private Player myPlayerUp;
    private Player myStartingPlayer;
    private GameBoard myGameBoard;
    private ComputerPlayer compPlayer;
    
    /**
     * GameEngine constructor.
     * @param player The human player.
     * @param gameBoard Which GameBoard to use.
     */
    public GameEngine(Player player, GameBoard gameBoard) 
    {
    	myPlayers = new Vector<Player>(2);
        myPlayers.add(player);
        myGameBoard = gameBoard;
        PieceType cpuType;
        cpuType = PieceType.BLACK;
        if (player.getPieceType() == PieceType.RED) cpuType = PieceType.BLACK;
        if (player.getPieceType() == PieceType.BLACK) cpuType = PieceType.RED;
        if (player.getPieceType() == PieceType.YELLOW) cpuType = PieceType.GREEN;
        if (player.getPieceType() == PieceType.GREEN) cpuType = PieceType.YELLOW;
        compPlayer = new ComputerPlayer("ComputerPlayer", cpuType, myGameBoard);
        myPlayers.add(compPlayer);
    }
    
    /**
     * Select which player goes first.
     * @param player The player to go first.
     * @return If the selection was successful.
     */
    public boolean selectStartingPlayer(Player player)
    {
        boolean isInVector = false;
        // we need to check to see if the player passed in is in the Player vector
        for (int i = 0; i < myPlayers.size(); i++)
        {
            if (player != null && myPlayers.get(i).equals(player)) isInVector = true;
        }
        if (isInVector)
        {
            myStartingPlayer = player;
            myPlayerUp = player;
        }
        return isInVector;
    }
    
    /**
     * Starts the game.
     * @return If the game start was successful.
     */
    public boolean startGame() 
    {
    	if (myPlayers.size() == 0) return false;
    	if (myGameBoard == null || myPlayers.get(0) == null) return false; // can't have null objects
    	if (myStartingPlayer == null) this.selectStartingPlayer(myPlayers.get(0));
    	else if (myStartingPlayer != null && myStartingPlayer == myPlayers.get(0)) this.selectStartingPlayer(myPlayers.get(1));
    	else this.selectStartingPlayer(myPlayers.get(0));
    	myGameBoard.resetBoard();
    	if (!myGameBoard.checkAllNull()) return false;
    	return true;
    }
    
    /**
     * Switches players.
     * @return The new player.
     */
    public Player switchPlayerUp() 
    {
        if (myPlayerUp.equals(myPlayers.get(0))) myPlayerUp = myPlayers.get(1);
        else if (myPlayerUp.equals(myPlayers.get(1))) myPlayerUp = myPlayers.get(0);
        return myPlayerUp;
    }
    
    /**
     * Places a piece in the specified column.
     * @param column The column to place the piece.
     * @return If the placement was successful.
     */
    public boolean placePiece(int column)
    {
        return myGameBoard.placePiece(column, myPlayerUp.getPieceType());
    }
    
    /**
     * Gets the current player up.
     * @return Current player up.
     */
    public Player getPlayerUp() 
    {
        return myPlayerUp;
    }
    
    /**
     * Returns the starting player.
     * @return The player who started.
     */
    public Player getStartingPlayer()
    {
        return myStartingPlayer;
    }
    
    /**
     * Get the list of players.
     * @return The players in a Vector.
     */
    public Vector<Player> getPlayers()
    {
        return myPlayers;
    }
    
    /**
     * Sets the GameBoard object.
     * @param gameboard The new GameBoard.
     */
    public void setGameBoard(GameBoard gameboard)
    {
        myGameBoard = gameboard;
    }
    
    /**
     * Get the current GameBoard.
     * @return The current GameBoard.
     */
    public GameBoard getGameBoard()
    {
        return myGameBoard;
    }

    /**
     * Sets a new human player.
     * @param player The new human player.
     */
    public void setPlayer(Player player)
    {
        boolean resetStartingPlayer = false;
        if (myStartingPlayer == myPlayers.get(0)) resetStartingPlayer = true;
        myPlayers.clear();
        myPlayers.add(player);
        myPlayers.add(compPlayer);
        if (resetStartingPlayer) myStartingPlayer = myPlayers.get(0);
        if (player.getPieceType() == PieceType.RED) compPlayer.setPieceType(PieceType.BLACK);
        if (player.getPieceType() == PieceType.BLACK) compPlayer.setPieceType(PieceType.RED);
        if (player.getPieceType() == PieceType.YELLOW) compPlayer.setPieceType(PieceType.GREEN);
        if (player.getPieceType() == PieceType.GREEN) compPlayer.setPieceType(PieceType.YELLOW);
    }
}