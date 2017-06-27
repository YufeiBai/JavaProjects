/**
 * A simple Canvas extension for drawing our tokens.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectview;

import java.awt.*;

import connectmodel.PieceType;

public class CanvasSquare extends Canvas
{
    private Image myImage;
    private Point myPosition;
    private PieceType myPiece;
    private boolean isHover;
    
    /**
     * CanvasSquare constructor.
     * @param image Default image.
     * @param pos The position of the CanvasSquare. Should relate to the position of the GameBoard.
     */
    public CanvasSquare(Image image, Point pos)
    {
        myImage = image;
        myPosition = pos;
        myPiece = null;
        isHover = false;
    }
    
    /**
     * Paint the CanvasSquare.
     */
    public void paint(Graphics g)
    {
        g.drawImage(myImage, 0, 0, this);
    }
    
    /**
     * Set a new image for our CanvasSquare.
     * @param image The new image for the CanvasSquare.
     */
    public void setImage(Image image)
    {
        myImage = image;
    }
    
    /**
     * Get the PieceType associated with the CanvasSquare.
     * @return The current PieceType of this CanvasSquare.
     */
    public PieceType getPiece()
    {
        return myPiece;
    }
    
    /**
     * Set the PieceType associated with the CanvasSquare.
     * @param The new PieceType of this CanvasSquare.
     */
    public void setPiece(PieceType piece)
    {
        myPiece = piece;
    }
    
    /**
     * Get whether we are currently being hovered over.
     * @return The hover status.
     */
    public boolean getHoverStatus()
    {
        return isHover;
    }
    
    /**
     * Set whether we are currently being hovered over.
     * @param The new hover status.
     */
    public void setHoverStatus(boolean status)
    {
        isHover = status;
    }
}
