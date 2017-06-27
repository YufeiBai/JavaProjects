/**
 * The Player class contains all methods and data relevant to an individual player in our game.
 * Namely, the Player class keeps track of scores and the individual names of our players.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectmodel;

public class Player 
{
    public static final String DEFAULT_NAME = "JohnCena";
    protected String myName;
    protected int myNumWins;
    protected PieceType myPieceType;
    
    /**
     * Constructor for a Player.
     * @param name The name of the player.
     * @param type The PieceType to associate with the player.
     */
    public Player(String name, PieceType type)
    {
        if (this.validateName(name)) myName = name;
        else myName = DEFAULT_NAME;
        myPieceType = type;
    }
    
    /**
     * Validate if the name entered is valid.
     * @param name The name to check.
     * @return If the name is valid.
     */
    private boolean validateName(String name) 
    {
        String patternToCheck = "[a-zA-Z0-9]++";
        return name.matches(patternToCheck);
    }
    
    /**
     * Increment the win score.
     */
    public void incrementScore() 
    {
    	myNumWins++;
    }
    
    /**
     * Get the PieceType of this player.
     * @return The PieceType of the player.
     */
    public PieceType getPieceType()
    {
        return myPieceType;
    }
    
    /**
     * Get the Player name.
     * @return Player name.
     */
    public String getName() 
    {
        return myName;
    }
    
    /**
     * Get the current amount of wins.
     * @return The amount of wins.
     */
    public int getNumWins()
    {
        return myNumWins;
    }
    
    /**
     * Get the current amount of wins.
     * @return The amount of wins.
     */
    public int getScore()
    {
        return myNumWins;
    }
    
    /**
     * Set a new piece type for this player.
     * @param type The new piece type.
     */
    public void setPieceType(PieceType type)
    {
        myPieceType = type;
    }
}