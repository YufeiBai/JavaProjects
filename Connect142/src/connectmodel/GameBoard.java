/**
 * GameBoard is where all of game action happens. GameBoard checks for wins, handles piece placement,
 * and handles the board itself (which is constructed through myBoard[][]). GameBoard itself is accessed and controlled
 * through GameEngine.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectmodel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

public class GameBoard
{
    private int myNumRows;
    private int myNumColumns;
    private PieceType[][] myBoard;
    private int myNumTypes;
    private int myWinLength;
    private Point myLastPoint;
    private Vector<PieceType> myTypes;
    private Point myWinBegin;
    private Point myWinEnd;
    private boolean myIsAWin;
    
    /**
     * Constructor for the GameBoard.
     * @param rows Number of rows for our GameBoard.
     * @param cols Number of columns for our GameBoard.
     * @param winLength How big a run must be to win.
     * @param types An array containing the piece types.
     */
    public GameBoard(int rows, int cols, int winLength, PieceType[] types) 
    {
    	// initialize our member data
        myNumRows = rows;
        myNumColumns = cols;
        myWinLength = winLength;
        myTypes = new Vector<PieceType>(types.length);
        for (int i = 0; i < types.length; i++)
        {
            myTypes.add(types[i]);
        }
        myBoard = new PieceType[myNumRows][myNumColumns];
    }
    
    /**
     * Places a piece in the game board.
     * @param col The column to place our piece.
     * @param type The piece type.
     * @return If the placement was successful.
     */
    public boolean placePiece(int col, PieceType type)
    {
        if (col < 0 || col >= myNumColumns) return false; // invalid column
        if (isColumnFull(col)) return false; // if the column is full, do nothing
        // otherwise, we will proceed to placing the piece
        int placementRow = myNumRows - 1;
        while (placementRow != -1)
        {
        	if (myBoard[placementRow][col] == null) break; // we've found our row
        	else placementRow--; // otherwise, we go up and check the next one
        }
        if (placementRow == -1) // we couldn't find an empty row. if this occurs, there must be a bug in isColumnFull.
        {
        	assert placementRow != -1 : "isColumnFull() failed to list " + col + " as being full in placePiece()!";
        	return false;
        }
        myBoard[placementRow][col] = type; // place our piece
        return true; // we did it successfully
    }
    
    /**
     * Finds the next free row in the given column.
     * @param col The column to check.
     * @return The next free row in the given column. -1 if no such row exists.
     */
    public int getNextFreeRow(int col)
    {
        int placementRow = myNumRows - 1;
        while (placementRow != -1)
        {
            if (myBoard[placementRow][col] == null) break; // we've found our row
            else placementRow--; // otherwise, we go up and check the next one
        }
        return placementRow;
    }
    
    /**
     * Resets the board.
     */
    public void resetBoard() 
    {
    	for (int i = 0; i < myNumRows; i++) 
    	{
    	    for (int j = 0; j < myNumColumns; j++)
    	    {
    	        myBoard[i][j] = null;
    	    }
    	}
    }
    
    /**
     * Checks for a victory.
     * @return Whether if there is a victory or not.
     */
    public boolean checkIfWin()
    {
        return checkVerticalWin() || checkHorizontalWin() || checkDiagonalWin();
    }
    
    /**
     * Determines, according to Plante's algorithm, the 'next best move' for our ComputerPlayer.
     * @param type The PieceType to determine the next best move for.
     * @return The column for the next best move.
     */
    public int findBestMoveColumn(PieceType type)
    {
        // firstly, check to see if we can win. If we can, we will select there.
        for (int i = 0; i < myNumColumns; i++)
        {
            if (!this.isColumnFull(i))
            {
                if (this.countVerticalLengthIfPiecePlaced(i, type) == myWinLength || this.countHorizontalLengthIfPiecePlaced(i, type) == myWinLength
                        || this.countDiagonalLengthIfPiecePlaced(i, type) == myWinLength) return i;
            }
        }
        // then we check to block. we must figure out what piecetype is the opponent's piece type
        // this code assumes that the type is in the Vector and the size is of 2
        PieceType opponentType = null;
        for (int i = 0; i < myTypes.size(); i++)
        {
            if (type != myTypes.get(i)) opponentType = myTypes.get(i);
        }
        assert opponentType != null : "OpponentType was selected as null!";
        for (int i = 0; i < myNumColumns; i++)
        {
            if (!this.isColumnFull(i))
            {
                if (this.countVerticalLengthIfPiecePlaced(i, opponentType) == myWinLength || this.countHorizontalLengthIfPiecePlaced(i, opponentType) == myWinLength
                        || this.countDiagonalLengthIfPiecePlaced(i, opponentType) == myWinLength) 
                    {
                        return i;
                    }
            }
        }
        // otherwise, find the maximum of the countLengths and return that
        int currentMaxPieces = 0;
        int currentPlaceCol = -1;
        int tempSum = 0;
        for (int i = 0; i < myNumColumns; i++)
        {
            if (!this.isColumnFull(i))
            {
                tempSum = this.countDiagonalLengthIfPiecePlaced(i, type) + this.countHorizontalLengthIfPiecePlaced(i, type)
                    + this.countVerticalLengthIfPiecePlaced(i, type);
                if (tempSum > currentMaxPieces)
                {
                    currentMaxPieces = tempSum;
                    currentPlaceCol = i;
                }
            }
        }
        // fail safe just in case...
        if (currentPlaceCol == -1)
        {
            for (int i = 0; i < myNumColumns; i++)
            {
                if (!this.isColumnFull(i))
                {
                    currentPlaceCol = i;
                }
            }
        }
        return currentPlaceCol;
    }
    
    /**
     * Checks for a vertical win.
     * @return If a vertical win occurred. 
     */
    private boolean checkVerticalWin() 
    {
        // iterate through the columns, check for a run of myWinLength
        if (myNumRows < myWinLength - 1) return false; // there can't be a vertical win if there's not enough space!
        // we're going to check for runs of myWinLength
        int[] runArray = new int[myNumRows];
        PieceType currentType;
        // this array stores the "run" at the given position in a row
        for (int i = 0; i < myNumColumns; i++)
        {
            for (int j = 0; j < myNumRows; j++)
            {
                currentType = myBoard[j][i];
                for (int k = j; k < myNumRows; k++)
                {
                    if (myBoard[k][i] == currentType && currentType != null) runArray[j]++;
                    else break;
                }
            }
            // check for a run of myWinLength or longer
            for (int j = 0; j < myNumRows; j++)
            {
                if (runArray[j] >= myWinLength) // win location
                {
                    // store the win locations
                    myWinBegin = new Point(i, j);
                    myWinEnd = new Point(i, j + (myWinLength-1));
                    return true; // and return true
                }
                else runArray[j] = 0; // otherwise set it for zero for the next row loop
            }
        }
        return false;
    }
    
    /**
     * Checks for a horizontal win.
     * @return If a horizontal win occurred. 
     */
    private boolean checkHorizontalWin() 
    {
        // iterate through the rows, check for a run of myWinLength
    	if (myNumColumns < myWinLength - 1) return false; // there can't be a horizontal win if there's not enough space!
    	// we're going to check for runs of myWinLength
    	int[] runArray = new int[myNumColumns];
    	PieceType currentType;
    	// this array stores the "run" at the given position in a row
    	for (int i = 0; i < myNumRows; i++)
    	{
	    	for (int j = 0; j < myNumColumns; j++)
	    	{
	    	    currentType = myBoard[i][j];
	    	    for (int k = j; k < myNumColumns; k++)
	    	    {
	    	        if (myBoard[i][k] == currentType && currentType != null) runArray[j]++;
	    	        else break;
	    	    }
	    	}
	    	// check for a run of myWinLength or longer
	    	for (int j = 0; j < myNumColumns; j++)
            {
	    	    if (runArray[j] >= myWinLength) // win location
	    	    {
	    	        // store the win locations
	    	        myWinBegin = new Point(j, i);
	    	        myWinEnd = new Point(j + (myWinLength-1), i);
	    	        return true; // and return true
	    	    }
	    	    else runArray[j] = 0; // otherwise set it for zero for the next row loop
            }
    	}
    	return false;
    }
    
    /**
     * Checks for a diagonal win.
     * @return If a diagonal win occurred. 
     */
    private boolean checkDiagonalWin() 
    {
        // firstly, a diagonal win cannot occur if the rows or columns are less than (winLength - 1) .
        if (myNumColumns < myWinLength - 1 || myNumRows < myWinLength - 1) return false;
        // while there may be a better algorithm for this which is better in terms of space complexity,
        // I will be implementing this algorithm for now.
        // first, check diagonal right.
        int[][] checkedArray = new int[myNumRows][myNumColumns];
        ArrayList<PieceType> myPieces = new ArrayList<PieceType>();
        ArrayList<Point> myPoints = new ArrayList<Point>();
        for (int i = 0; i < myNumRows; i++)
        {
            for (int j = 0; j < myNumColumns; j++)
            {
                if (checkedArray[i][j] != 1) // if we have not already checked here...
                {
                    // check upright
                	for (int iCheck = i, jCheck = j; iCheck >= 0 && jCheck < myNumColumns; iCheck--, jCheck++)
                	{
                	    myPieces.add(myBoard[iCheck][jCheck]);
                	    myPoints.add(new Point(iCheck, jCheck));
                	    checkedArray[iCheck][jCheck] = 1;
                	}
                	
                	// and down left
                    for (int iCheck = i+1, jCheck = j-1; iCheck < myNumRows && jCheck >= 0; iCheck++, jCheck--)
                    {
                        myPieces.add(0, myBoard[iCheck][jCheck]);
                        myPoints.add(0, new Point(iCheck, jCheck));
                        checkedArray[iCheck][jCheck] = 1;
                    }
                	
                    // create a run length array
                    int[] myRunArray = new int[myPieces.size()];
                    PieceType currentType;
                    for (int k = 0; k < myPieces.size(); k++)
                    {
                        currentType = myPieces.get(k);
                        for (int l = k; l < myPieces.size(); l++)
                        {
                            if (myPieces.get(l) == currentType && currentType != null) myRunArray[k]++;
                            else break;
                        }
                    } 

                    // check for runs
                    for (int k = 0; k < myRunArray.length; k++)
                    {
                        if (myRunArray[k] >= myWinLength)
                        {
                            // get win points
                            myWinBegin = new Point(myPoints.get(k).y, myPoints.get(k).x);
                            myWinEnd = new Point(myPoints.get(k + myWinLength - 1).y, myPoints.get(k + myWinLength - 1).x);
                            return true;
                        }
                    }
                    myPieces.clear();
                	myPoints.clear();
                }
                
            }
        }
        for (int i = 0; i < myNumRows; i++)
        {
            for (int j = 0; j < myNumColumns; j++)
            {
                if (checkedArray[i][j] != 2) // if we have not already checked here...
                {
                    // check upleft
                    for (int iCheck = i, jCheck = j; iCheck >= 0 && jCheck >= 0; iCheck--, jCheck--)
                    {
                        myPieces.add(myBoard[iCheck][jCheck]);
                        myPoints.add(new Point(iCheck, jCheck));
                        checkedArray[iCheck][jCheck] = 2;
                    }
                    
                    // and down right
                    for (int iCheck = i+1, jCheck = j+1; iCheck < myNumRows && jCheck < myNumColumns; iCheck++, jCheck++)
                    {
                        myPieces.add(0, myBoard[iCheck][jCheck]);
                        myPoints.add(0, new Point(iCheck, jCheck));
                        checkedArray[iCheck][jCheck] = 2;
                    }
                    
                    // create a run length array
                    int[] myRunArray = new int[myPieces.size()];
                    PieceType currentType;
                    for (int k = 0; k < myPieces.size(); k++)
                    {
                        currentType = myPieces.get(k);
                        for (int l = k; l < myPieces.size(); l++)
                        {
                            if (myPieces.get(l) == currentType && currentType != null) myRunArray[k]++;
                            else break;
                        }
                    } 

                    // check for runs
                    for (int k = 0; k < myRunArray.length; k++)
                    {
                        if (myRunArray[k] >= myWinLength)
                        {
                            // get win points
                            myWinBegin = new Point(myPoints.get(k).y, myPoints.get(k).x);
                            myWinEnd = new Point(myPoints.get(k + myWinLength - 1).y, myPoints.get(k + myWinLength - 1).x);
                            return true;
                        }
                    }
                    myPieces.clear();
                    myPoints.clear();
                }
                
            }
        }
        return false;
    }
    
    /**
     * Figures out the horizontal length if a piece is placed in col.
     * @param col The column to place the piece.
     * @param type The piece type.
     * @return Horizontal length if placed in col.
     */
    private int countHorizontalLengthIfPiecePlaced(int col, PieceType type) 
    {
        // check to the left and right for pieces until we hit a piece that is not of the
    	// type, then we sum them together and return.
    	int lrSum = 1; // starts at 1 because of the piece that is about to be placed
    	int currentShiftIndex = 0;
    	// find where we are
    	int placementRow = myNumRows - 1;
        while (placementRow != -1)
        {
        	if (myBoard[placementRow][col] == null) break; // we've found our row
        	else placementRow--; // otherwise, we go up and check the next one
        }
        // we are guaranteed that our current position is null by the previous code
        // first check left
        while (true)
    	{
    		currentShiftIndex--;
    		if (col + currentShiftIndex >= 0 && col + currentShiftIndex < myNumColumns && 
    		        myBoard[placementRow][col + currentShiftIndex] != null && myBoard[placementRow][col + currentShiftIndex].equals(type)) lrSum++;
    		else break;
    	}
        // then right
        currentShiftIndex = 0;
        while (true)
    	{
    		currentShiftIndex++;
    		if (col + currentShiftIndex >= 0 && col + currentShiftIndex < myNumColumns &&
    		        myBoard[placementRow][col + currentShiftIndex] != null && myBoard[placementRow][col + currentShiftIndex].equals(type)) lrSum++;
    		else break;
    	}
        return lrSum;
    }
    
    /**
     * Figures out the vertical length if a piece is placed in col.
     * @param col The column to place the piece.
     * @param type The piece type.
     * @return Horizontal vertical if placed in col.
     */
    private int countVerticalLengthIfPiecePlaced(int col, PieceType type) 
    {
        // check up and down for pieces until we hit a piece that is not of the
        // type, then we sum them together and return.
        int udSum = 1; // starts at 1 because of the piece that is about to be placed
        int currentShiftIndex = 0;
        // find where we are
        int placementRow = myNumRows - 1;
        while (placementRow != -1)
        {
            if (myBoard[placementRow][col] == null) break; // we've found our row
            else placementRow--; // otherwise, we go up and check the next one
        }
        // we are guaranteed that our current position is null by the previous code
        // first check up
        while (true)
        {
            currentShiftIndex--;
            if (placementRow + currentShiftIndex >= 0 && placementRow + currentShiftIndex < myNumRows &&
                    myBoard[placementRow + currentShiftIndex][col] != null && myBoard[placementRow + currentShiftIndex][col].equals(type)) udSum++;
            else break;
        }
        // then down
        currentShiftIndex = 0;
        while (true)
        {
            currentShiftIndex++;
            if (placementRow + currentShiftIndex >= 0 && placementRow + currentShiftIndex < myNumRows && 
                    myBoard[placementRow + currentShiftIndex][col] != null && myBoard[placementRow + currentShiftIndex][col].equals(type)) udSum++;
            else break;
        }
        return udSum;
    }
    
    /**
     * Figures out the diagonal length if a piece is placed in col.
     * @param col The column to place the piece.
     * @param type The piece type.
     * @return Diagonal length if placed in col.
     */
    private int countDiagonalLengthIfPiecePlaced(int col, PieceType type) 
    {
        int diagSum1 = 1, diagSum2 = 1;
        // first, we locate the row we are at
        int placementRow = myNumRows - 1;
        while (placementRow != -1)
        {
            if (myBoard[placementRow][col] == null) break; // we've found our row
            else placementRow--; // otherwise, we go up and check the next one
        }
        // check upright
        for (int iCheck = placementRow, jCheck = col; iCheck >= 0 && jCheck < myNumColumns; iCheck--, jCheck++)
        {
            if (myBoard[iCheck][jCheck] == type) diagSum1++;
            else break;
        }
        
        // and down left
        for (int iCheck = placementRow+1, jCheck = col-1; iCheck < myNumRows && jCheck >= 0; iCheck++, jCheck--)
        {
            if (myBoard[iCheck][jCheck] == type) diagSum1++;
            else break;
        }
        for (int iCheck = placementRow, jCheck = col; iCheck >= 0 && jCheck >= 0; iCheck--, jCheck--)
        {
            if (myBoard[iCheck][jCheck] == type) diagSum2++;
            else break;
        }
        
        // and down right
        for (int iCheck = placementRow+1, jCheck = col+1; iCheck < myNumRows && jCheck < myNumColumns; iCheck++, jCheck++)
        {
            if (myBoard[iCheck][jCheck] == type) diagSum2++;
            else break;
        }
        return Math.max(diagSum1, diagSum2);
    }
    
    /**
     * Gets the current piece types.
     * @return The current piece types.
     */
    public Vector<PieceType> getTypes() 
    {
        return myTypes;
    }
    
    /**
     * Returns the beginning of the win.
     * @return The beginning of the win. Null if no win has yet to take place.
     */
    public Point getWinBegin() 
    {
        return myWinBegin;
    }
    
    /**
     * Returns the end of the win.
     * @return The end of the win. Null if no win has yet to take place.
     */
    public Point getWinEnd()
    {
        return myWinEnd;
    }
    
    /**
     * Returns what piece type is at a position.
     * @param point The point of the piece, in the form (x,y).
     * @return What piece type is located at (x,y).
     */
    public PieceType getPieceOnBoard(Point point) 
    {
        return myBoard[point.y][point.x];
    }
    
    /**
     * Gets the current board.
     * @return A PieceType[][] representing the current board.
     */
    public PieceType[][] getBoard() 
    {
        return myBoard;
    }
    
    /**
     * Checks to see if the board is full.
     * @return Whether or not the board is full.
     */
    public boolean isBoardFull() 
    {
    	for (int i = 0; i < myNumRows; i++) 
    	{
    	    for (int j = 0; j < myNumColumns; j++)
    	    {
    	        if (myBoard[i][j] == null) return false;
    	    }
    	}
    	return true;
    }
    
    /**
     * Checks to see if a column is full.
     * @param col The column to check.
     * @return If the column is full.
     */
    public boolean isColumnFull(int col) 
    {
        if (col < 0 || col >= myNumColumns) return false;
        for (int i = 0; i < myNumRows; i++)
        {
        	if (myBoard[i][col] == null) return false;
        }
        return true;
    }
    
    /**
     * A duplicate of the checkIfWin method.
     * @return If a win has occurred.
     */
    public boolean getIsAWin()
    {
        return this.checkIfWin();
    }
    
    /**
     * Checks to see if the board is empty.
     * @return Whether the board is empty or not.
     */
    public boolean checkAllNull()
    {
    	for (int i = 0; i < myNumRows; i++) 
    	{
    	    for (int j = 0; j < myNumColumns; j++)
    	    {
    	        if (myBoard[i][j] != null) return false;
    	    }
    	}
    	return true;
    }
}