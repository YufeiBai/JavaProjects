import java.awt.*;
import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame {
	private Grid myTiles;
	private JPanel myControls;
	private JTextField emptyX, emptyY;
	private JComboBox sizeEntry;
	private JButton goButton;
	private JLabel sizeL, xL, yL;
	private Button goListener;
	private Controller myControl;
	public final static int VIEW_H = 512;
	public final static int VIEW_W = 512;
	
	public static class Grid extends JPanel {
		private List<Point> fillCells;
		private List<Color> fillColor;
		private int mySize;
		
		public Grid(int size) {
			fillCells = new ArrayList<>(size*size);
			fillColor = new ArrayList<>(size*size);
			mySize = size;
		}
	
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int i = 0; i < fillCells.size(); i++) {
				if (fillCells.get(i) != null) {
					g.setColor(fillColor.get(i));
					g.fillRect((VIEW_H/mySize)*fillCells.get(i).x, (VIEW_W/mySize)*fillCells.get(i).y, VIEW_H/mySize, VIEW_W/mySize);
				}
			}
			//g.setColor(Color.BLACK);
			//g.drawRect(0, 0, VIEW_H, VIEW_W);
		}
	
		public void fillCell(int x, int y, int col) {
			fillCells.add(new Point(x, y));
			if (col%4==0) fillColor.add(Color.BLUE);
			if (col%4==1) fillColor.add(Color.RED);
			if (col%4==2) fillColor.add(Color.YELLOW);
			if(col%4==3) fillColor.add(Color.GREEN);
			repaint();
		}
}
	
	public View(Controller control) {
		myControl = control;
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		myTiles = new Grid(1);
		myControls = new JPanel();
		goButton = new JButton("Go!");
		Integer[] sizeChoice = {2, 3, 4, 5, 6, 7, 8, 9};
		sizeEntry = new JComboBox(sizeChoice);
		emptyX = new JTextField();
		emptyY = new JTextField();
		sizeL = new JLabel("Size: ");
		xL = new JLabel("Empty X: ");
		yL = new JLabel("Empty Y: ");
		this.getContentPane().setLayout(null);
		myControls.setLayout(null);
		//myControls.setBorder(blackline);
		myTiles.setBorder(blackline);
		
		myTiles.setBounds(0, 0, VIEW_H, VIEW_W);
		myControls.setBounds(0,VIEW_W,VIEW_H,30);
		
		this.add(myTiles);
		this.add(myControls);
		
		goButton.setBounds(450,0,60,23);
		sizeEntry.setBounds(40, 0, 60, 23);
		emptyX.setBounds(180, 0, 60, 23);
		emptyY.setBounds(320, 0, 60, 23);
		sizeL.setBounds(5, 0, 60, 23);
		xL.setBounds(120, 0, 60, 23);
		yL.setBounds(260, 0, 60, 23);
		
		myControls.add(goButton);
		myControls.add(sizeEntry);
		myControls.add(emptyX);
		myControls.add(emptyY);
		myControls.add(sizeL);
		myControls.add(xL);
		myControls.add(yL);
		
		this.setSize(new Dimension(VIEW_H, VIEW_W+50));
	    this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.associateListeners();
		this.setVisible(true);
		this.setTitle("Tromino");
	}
	
	public void tile(int[][] grid) {
		myTiles.setVisible(false);
		myTiles = null;
		System.gc();
		myTiles = new Grid(grid.length);
		myTiles.setBounds(0, 0, VIEW_H, VIEW_W);
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j]>0) myTiles.fillCell(i, j, grid[i][j]);
			}
		}
		this.add(myTiles);
	}
	
	public void associateListeners()
	{
		Class<? extends Controller> controlClass;
		Method[] clickMethods;
		Class<?>[] classArgs;
		controlClass = myControl.getClass();
		
		clickMethods = new Method[1];
		
		try
		{
			clickMethods[0] = controlClass.getMethod("solve", null);  
		}
		catch(NoSuchMethodException exception)
		{
		}
		catch(SecurityException exception)
		{
		}
		goListener = new Button(myControl, clickMethods, null);
		goButton.addMouseListener(goListener);
	}
	
	public int getEmptyX() {
		return Integer.parseInt(emptyX.getText());
	}
	
	public int getEmptyY() {
		return Integer.parseInt(emptyY.getText());
	}
	
	public int getBoardSize() {
		return (int)sizeEntry.getSelectedItem();
	}
}
