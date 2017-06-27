/**
 * The PuzzleEngine class handles the algorithm for solving the cryptoarithmetic puzzle.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 * 
 */
package puzzlemodel;

import java.util.*;

public class PuzzleEngine
{
    private String myWord1, myWord2, myResultWord, myListOfLetters;
    private int[] myAddend1, myAddend2, mySum, myDigits, myAssignments;
    private boolean myIsPuzzleSolved;
    
    /**
     * Constructor for the PuzzleEngine.
     */
    public PuzzleEngine()
    {
        myIsPuzzleSolved = false;
        myListOfLetters = "";
    }
    
    /**
     * Assigns a digit to a letter.
     * @param digit Digit to assign.
     * @param letter Letter to assign digit to.
     * @return If the assignment was successful.
     */
    public boolean assignDigit(int digit, char letter)
    {
        if (myDigits[digit] == -1) return false;
        // find the letter in the list of letters
        int index = -1;
        for (int i = 0; i < myListOfLetters.length(); i++)
        {
            if (myListOfLetters.charAt(i) == letter)
            {
                index = i;
                break;
            }
        }
        // if the index is -1, the letter doesn't exist
        if (index == -1) return false;
        myAssignments[index] = digit;
        myDigits[digit] = -1;
        this.updateAddends();
        return true;
    }
    
    /**
     * Unassigns a digit to a letter.
     * @param digit Digit to unassign.
     * @param letter Letter to unassign digit to.
     * @return If the unassignment was successful.
     */
    public boolean unassignDigit(int digit, char letter)
    {
        //if (myDigits[digit] != -1) return false;
        // find the letter in the list of letters
        int index = -1;
        for (int i = 0; i < myListOfLetters.length(); i++)
        {
            if (myListOfLetters.charAt(i) == letter)
            {
                index = i;
                break;
            }
        }
        // if the index is -1, the letter doesn't exist
        if (index == -1) return false;
        myAssignments[index] = -1;
        myDigits[digit] = digit;
        this.updateAddends();
        return true;
    }
    
    /**
     * Checks to see if the Puzzle is solved.
     * @return If the puzzle is solved.
     */
    public boolean checkIfSolved()
    {
        if ((this.covertDigitArrayToInt(myAddend1) + this.covertDigitArrayToInt(myAddend2) 
            == this.covertDigitArrayToInt(mySum)) && myAddend1[0] != 0 
                && myAddend2[0] != 0 && mySum[0] !=0) myIsPuzzleSolved = true;
        return myIsPuzzleSolved;
    }
    
    /**
     * Check to see if all letters are assigned.
     * @return If all letters are assigned.
     */
    public boolean checkIfAllLettersAssigned()
    {
        for (int i = 0; i < myAssignments.length; i++)
        {
            //System.out.print(myAssignments[i]);
            if (myAssignments[i] == -1) return false;
        }
        return true;
    }
    
    /**
     * Update the addend arrays.
     */
    public void updateAddends()
    {
        for (int i = 0; i < myWord1.length(); i++)
        {
            myAddend1[i] = findLetterAssignment(myWord1.charAt(i));
        }
        for (int i = 0; i < myWord2.length(); i++)
        {
            myAddend2[i] = findLetterAssignment(myWord2.charAt(i));
        }
        for (int i = 0; i < myResultWord.length(); i++)
        {
            mySum[i] = findLetterAssignment(myResultWord.charAt(i));
        }
    }
    
    /**
     * Find the digit assignment of a letter.
     * @param letter The letter to search for.
     * @return The digit assigned to the letter (-1 if not set).
     */
    public int findLetterAssignment(char letter)
    {
        for (int i = 0; i < myAssignments.length; i++)
        {
            if (myListOfLetters.charAt(i) == letter) return myAssignments[i];
        }
        return -1; // letter wasn't in myListOfLetters
    }
    
    /**
     * Returns the list of letters generated in setPuzzle.
     * @return List of letters.
     */
    public String determineListOfLetters()
    {
        return myListOfLetters;
    }
    
    /**
     * Check to see if a digit is currently in use.
     * @param digit The digit to check.
     * @return If the digit is in use.
     */
    public boolean checkIfDigitUsed(int digit)
    {
        return myDigits[digit] == -1;
    }
    
    /**
     * Find the next free digit in the digit array.
     * @return The next free digit. -1 if no digits available.
     */
    public int findNextFreeDigit()
    {
        for (int i = 0; i < myDigits.length; i++)
        {
            if (myDigits[i] != -1) return myDigits[i];
        }
        return -1; // all digits are taken
    }
    
    /**
     * Finds the next free letter in the assignments array.
     * @return Next free letter. Returns ' ' if no free letters avaiable.
     */
    public char findNextFreeLetter()
    {
        for (int i = 0; i < myAssignments.length; i++)
        {
            if (myAssignments[i] == -1) return myListOfLetters.charAt(i);
        }
        return ' ';
    }
    
    /**
     * Sets the Puzzle.
     * @param word1 First word.
     * @param word2 Second word.
     * @param resultWord The word resulting from the sum of the first two words.
     */
    public void setPuzzle(String word1, String word2, String resultWord)
    {
        myDigits = new int[10];
        for (int i = 0; i < 10; i++)
        {
            myDigits[i] = i;
        }
        // set the variables
        myWord1 = word1;
        myWord2 = word2;
        myResultWord = resultWord;
        myAddend1 = new int[word1.length()];
        myAddend2 = new int[word2.length()];
        mySum = new int[resultWord.length()];
        String tempListOfLetters = word1 + word2 + resultWord;
        myListOfLetters = "";
        myIsPuzzleSolved = false;
        
        boolean isAlreadyInList = false;
        // loop through tempListOfLetters and omit duplicates
        for (int i = 0; i < tempListOfLetters.length(); i++)
        {
            isAlreadyInList = false;
            for (int j = 0; j < myListOfLetters.length(); j++)
            {
                if (myListOfLetters.charAt(j) == tempListOfLetters.charAt(i)) isAlreadyInList = true; // already in the list
            }
            
            // if it's not already in the list...
            if (!isAlreadyInList)
            {
                // add it
                myListOfLetters += "" + tempListOfLetters.charAt(i);
            }
        }
        
        // set up arrays
        myAssignments = new int[myListOfLetters.length()];
        for (int i = 0; i < myAssignments.length; i++)
        {
            myAssignments[i] = -1;
        }
        for (int i = 0; i < myWord1.length(); i++) 
        {
            myAddend1[i] = -1;
        }
        for (int i = 0; i < myWord2.length(); i++) 
        {
            myAddend2[i] = -1;
        }
        for (int i = 0; i < myResultWord.length(); i++) 
        {
            mySum[i] = -1;
        }
        System.gc();
    }
    
    /**
     * Convert an array of ints to one full int.
     * @param digitArray The array of digits.
     * @return An int composed of digitArray.
     */
    public int covertDigitArrayToInt(int[] digitArray)
    {
        int digit = 0;
        for (int i = 0; i < digitArray.length; i++)
        {
            digit += digitArray[digitArray.length - 1 - i]*(int)Math.pow(10, i);
        }
        //System.out.println(digit);
        return digit;
    }
    
    /**
     * Get the solution to the Puzzle.
     * @return The solution to the puzzle, in the form of {word1, word2, sum}.
     */
    public int[] getSolution()
    {
        int[] theSolution = new int[3];
        theSolution[0] = this.covertDigitArrayToInt(myAddend1);
        theSolution[1] = this.covertDigitArrayToInt(myAddend2);
        theSolution[2] = this.covertDigitArrayToInt(mySum);
        return theSolution;
    }
    
    /**
     * Solves the puzzle.
     * @return If the puzzle is solveable. If so, solution can be reached via getSolution().
     */
    public boolean solvePuzzle()
    {
        // check to make sure we don't have any empty entries
        if (myWord1.equals("") || myWord2.equals("") || myResultWord.equals("")) return false;
        // check to see if we've exhausted our list
        if (this.checkIfAllLettersAssigned())
        {
            if (checkIfSolved()) return true;
            else return false;
        }
        
        for (int i = 0; i < myDigits.length; i++)
        {
            if (myDigits[i] != -1) // if we hit an unassigned digit
            {
                char assignLetter = this.findNextFreeLetter();
                int currentDigit = myDigits[i];
                // assign it to the next character
                if (this.assignDigit(currentDigit, assignLetter))
                {
                    if (solvePuzzle()) return true;
                    else this.unassignDigit(currentDigit, assignLetter);
                }
            }
        }
        return false;
    }
}