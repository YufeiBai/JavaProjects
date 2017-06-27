/**
 * ConnectView is the front end for the Connect4 game. Manages all the drawing and takes input from Controller
 * to handle repainting.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package connectview;

import javax.swing.*;

import connectcontrol.ButtonListener;
import connectcontrol.Controller;
import connectmodel.PieceType;
import connectmodel.Player;

import java.awt.*;
import java.lang.reflect.Method;

public class ConnectView extends JFrame
{
    public final static int myRows = 6, myColumns = 7;
    private JPanel myConnectPanel;
    private JPanel myNamePanel;
    private JMenuBar myMenu;
    private JLabel myPlayerName;
    private JLabel myCpuName;
    private JLabel myPlayerWinCounter;
    private JLabel myCpuWinCounter;
    private JMenu myGame;
    private JMenuItem[] myOptions;
    private Controller myControl;
    private CanvasSquare[][] mySquares;
    private ButtonListener[] mySquareListener;
    private ButtonListener[] myMenuOptionListener;
    
    /**
     * The ConnectView constructor.
     * @param controller What Controller will be controlling this instance.
     */
    public ConnectView(Controller controller)
    {
        // variables
        mySquares = new CanvasSquare[myRows][myColumns];
        mySquareListener = new ButtonListener[myColumns];
        myControl = controller;
        
        // set up JFrame
        this.setSize(454, 824);
        this.setTitle("Connect 1(4)2");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("red.jpg"));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        
        // game panel
        myConnectPanel = new JPanel(new GridLayout(myRows, myColumns));
        myConnectPanel.setBounds(0, 0, 454, 424);
        myConnectPanel.setPreferredSize(new Dimension(454, 384));

        // names
        myPlayerName = new JLabel(myControl.getPlayer().getName());
        myPlayerName.setHorizontalAlignment(JLabel.CENTER);
        myCpuName = new JLabel("ComputerPlayer");
        myCpuName.setHorizontalAlignment(JLabel.CENTER);
        
        // scores
        myPlayerWinCounter = new JLabel("0");
        myPlayerWinCounter.setHorizontalAlignment(JLabel.CENTER);
        myCpuWinCounter = new JLabel("0");
        myCpuWinCounter.setHorizontalAlignment(JLabel.CENTER);
        
        // name panel
        myNamePanel = new JPanel(new GridLayout(0, 2));
        myNamePanel.add(myPlayerName);
        myNamePanel.add(myCpuName);
        myNamePanel.add(myPlayerWinCounter);
        myNamePanel.add(myCpuWinCounter);
        
        // drop down items
        myOptions = new JMenuItem[2];
        myOptions[0] = new JMenuItem("New Match");
        myOptions[1] = new JMenuItem("New Game");
        
        // drop downs
        myGame = new JMenu("Game");
        myGame.add(myOptions[0]);
        myGame.add(myOptions[1]);
        
        // menu bar
        myMenu = new JMenuBar();
        myMenu.add(myGame);
        this.setJMenuBar(myMenu);
        
        // instantiate game panel
        for (int i = 0; i < myRows; i++)
        {
            for (int j = 0; j < myColumns; j++)
            {
                mySquares[i][j] = new CanvasSquare(Toolkit.getDefaultToolkit().getImage("blank.jpg"), new Point(j, i));
                myConnectPanel.add(mySquares[i][j]);
            }
        }
        this.add(myConnectPanel);
        this.add(myNamePanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        JOptionPane.showMessageDialog(null, "Welcome to Connect 1(4)2! Click on Game and then New Match to start a match.");
        this.associateListeners();
    }
    
    /**
     * Associates the proper listeners using reflection.
     */
    private void associateListeners()
    {
        Class<? extends Controller> controlClass;
        Method[] pieceMethods, newGameMethod, rematchMethod;
        Class<?>[] classArgs;
        controlClass = myControl.getClass();
        
        pieceMethods = new Method[3];
        newGameMethod = new Method[1];
        rematchMethod = new Method[1];
        classArgs = new Class[1];
        
        try
        {
           classArgs[0] = Class.forName("java.lang.Integer");
        }
        catch(ClassNotFoundException e)
        {
           String error;
           error = e.toString();
           System.out.println(error);
        }
        
        try
        {
           pieceMethods[0] = controlClass.getMethod("placePiece",classArgs);  
           pieceMethods[1] = controlClass.getMethod("possiblePiece",classArgs);    
           pieceMethods[2] = controlClass.getMethod("repaintView",null);    
           newGameMethod[0] = controlClass.getMethod("startMatch", null);
           rematchMethod[0] = controlClass.getMethod("newGame", null);
        }
        catch(NoSuchMethodException exception)
        {
           String error;

           error = exception.toString();
           System.out.println(error);
        }
        catch(SecurityException exception)
        {
           String error;

           error = exception.toString();
           System.out.println(error);
        }
        
        // set up the menu options
        myMenuOptionListener = new ButtonListener[2];
        myMenuOptionListener[0] = new ButtonListener(myControl, newGameMethod, null);
        myMenuOptionListener[1] = new ButtonListener(myControl, rematchMethod, null);
        myOptions[0].addMouseListener(myMenuOptionListener[0]);
        myOptions[1].addMouseListener(myMenuOptionListener[1]);
        Integer[] args;
        for (int i = 0; i < myRows; i++)
        {
           for (int j = 0; j < myColumns; j++)
           {
               args = new Integer[1];
               args[0] = new Integer(j);
               mySquareListener[j] = 
                       new ButtonListener(myControl, pieceMethods, args);
               mySquares[i][j].addMouseListener(mySquareListener[j]);
           }

        }
    }
    
    /**
     * Redraws the board. Do NOT call if a possible piece is visible.
     */
    public void updateBoard()
    {
        for (int i = 0; i < myRows; i++)
        {
            for (int j = 0; j < myColumns; j++)
            {
                if (myControl.getGameBoard().getPieceOnBoard(new Point(j, i)) == null)
                {
                    if (mySquares[i][j].getPiece() != null || mySquares[i][j].getHoverStatus() == true)
                    {
                        mySquares[i][j].setImage(Toolkit.getDefaultToolkit().getImage("blank.jpg"));
                        mySquares[i][j].setPiece(null);
                        mySquares[i][j].repaint();
                        mySquares[i][j].setHoverStatus(false);
                    }
                }
                if (myControl.getGameBoard().getPieceOnBoard(new Point(j, i)) == PieceType.RED)
                {
                    if (mySquares[i][j].getPiece() != PieceType.RED)
                    {
                        mySquares[i][j].setImage(Toolkit.getDefaultToolkit().getImage("red.jpg"));
                        mySquares[i][j].setPiece(PieceType.RED);
                        mySquares[i][j].repaint();
                    }
                }
                if (myControl.getGameBoard().getPieceOnBoard(new Point(j, i)) == PieceType.YELLOW)
                {
                    if (mySquares[i][j].getPiece() != PieceType.YELLOW)
                    {
                        mySquares[i][j].setImage(Toolkit.getDefaultToolkit().getImage("yellow.jpg"));
                        mySquares[i][j].setPiece(PieceType.YELLOW);
                        mySquares[i][j].repaint();
                    }
                }
                if (myControl.getGameBoard().getPieceOnBoard(new Point(j, i)) == PieceType.GREEN)
                {
                    if (mySquares[i][j].getPiece() != PieceType.GREEN)
                    {
                        mySquares[i][j].setImage(Toolkit.getDefaultToolkit().getImage("green.jpg"));
                        mySquares[i][j].setPiece(PieceType.RED);
                        mySquares[i][j].repaint();
                    }
                }
                if (myControl.getGameBoard().getPieceOnBoard(new Point(j, i)) == PieceType.BLACK)
                {
                    if (mySquares[i][j].getPiece() != PieceType.BLACK)
                    {
                        mySquares[i][j].setImage(Toolkit.getDefaultToolkit().getImage("black.jpg"));
                        mySquares[i][j].setPiece(PieceType.BLACK);
                        mySquares[i][j].repaint();
                    }
                }
            }
        }
        myPlayerName.setText(myControl.getPlayer().getName());
        myCpuName.setText("ComputerPlayer");
        if (myControl.getGameEngine().getPlayerUp().equals(myControl.getPlayer())) myPlayerName.setText(myControl.getPlayer().getName() + " is thinking...");
        else myCpuName.setText("ComputerPlayer is thinking...");
    }
    
    /**
     * Shows where a piece could be placed in a given column.
     * @param col The column we are looking at.
     */
    public void showPossiblePlacement(int col)
    {
        int row = myControl.getGameBoard().getNextFreeRow(col);
        mySquares[row][col].setHoverStatus(true);
        if (!myControl.getWaitStatus())
        {
            this.updatePossiblePlacement();
        }
    }
    
    /**
     * Catalogs what square to show the possible piece of when our universal game wait unlocks.
     */
    public void updatePossiblePlacement()
    {
        for (int i = 0; i < myColumns; i++)
        {
            int row = myControl.getGameBoard().getNextFreeRow(i);
            if (row >= 0 && mySquares[row][i].getHoverStatus() == true)
            {
                mySquares[row][i].setImage(Toolkit.getDefaultToolkit().getImage(myControl.getPlayer().getPieceType() + "Possible.jpg"));
                mySquares[row][i].repaint();
            }
        }
    }
    
    /**
     * Returns the current board.
     * @return The current board composed of CanvasSquare's.
     */
    public CanvasSquare[][] getViewBoard()
    {
        return mySquares;
    }
    
    /**
     * Sets a new board.
     * @param squares The new board composed of CanvasSquare's.
     */
    public void setViewBoard(CanvasSquare[][] squares)
    {
        mySquares = squares;
    }
    
    /**
     * Draws names and scores.
     */
    public void drawNames()
    {
        if (myControl.getPlayer().getPieceType() == PieceType.RED)
        {
            myPlayerName.setForeground(Color.RED);
            myCpuName.setForeground(Color.BLACK);
        }
        if (myControl.getPlayer().getPieceType() == PieceType.BLACK)
        {
            myPlayerName.setForeground(Color.BLACK);
            myCpuName.setForeground(Color.RED);
        }
        if (myControl.getPlayer().getPieceType() == PieceType.GREEN)
        {
            myPlayerName.setForeground(new Color(0, 183, 45));
            myCpuName.setForeground(new Color(128, 140, 0));
        }
        if (myControl.getPlayer().getPieceType() == PieceType.YELLOW)
        {
            myPlayerName.setForeground(new Color(128, 140, 0));
            myCpuName.setForeground(new Color(0, 183, 45));
        }
        
        // reset scores
        myCpuWinCounter.setText("" + myControl.getComputerPlayer().getScore());
        myPlayerWinCounter.setText("" + myControl.getPlayer().getScore());
    }
    
    /**
     * Called when a victor occurs. Draws which player is the victor in our name panel.
     * @param victor The winner.
     */
    public void drawVictory(Player victor)
    {
        if (victor == myControl.getPlayer()) myPlayerName.setText(myControl.getPlayer().getName() + " WINS!");
        else myCpuName.setText("ComputerPlayer WINS!");
        myCpuWinCounter.setText("" + myControl.getComputerPlayer().getScore());
        myPlayerWinCounter.setText("" + myControl.getPlayer().getScore());
    }
}
