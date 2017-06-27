/**
 * A simple enumeration class for our piece types.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectmodel;

public enum PieceType 
{
    RED ("Red"),
    BLACK ("Black"),
    GREEN ("Green"),
    YELLOW ("Yellow");
    private final String myType;
    
    /**
     * Set the type for our specific piece.
     * @param type The type for our piece.
     */
    private PieceType(String type) 
    {
    	myType = type;
    }
    
    /**
     * Get the PieceType of this specific instance.
     * @return The PieceType.
     */
    public String getType()
    {
        return myType;
    }
}