/**
 * The Tromino tiling algorithm. Based off of code written by Arup Guha (http://www.cs.ucf.edu/~dmarino/). Modified by
 * Brandon Belna (bbelna@stetson.edu) for use in Stetson's Discrete Structures course.
 */

import java.util.*;

public class Tromino {
	
	private int[][] grid;
	private int currentNum;

	public Tromino(int size, int x, int y) {
		int actualsize = size;
		grid = new int[actualsize][actualsize];
		currentNum = 1;
		for (int i=0; i<actualsize; i++) {
			for (int j=0; j<actualsize; j++) {
				grid[i][j] = 0;
			}
		}
		grid[x][y] = -1;
	}
	
	public int[][] getGrid() {
		return grid;
	}
	
	public void tile() {
		tileRec(grid.length, 0, 0);
	}
	
	private void tileRec(int size, int topx, int topy) {
		if (size == 2) {
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					if (grid[topx+i][topy+j] == 0) grid[topx+i][topy+j] = currentNum;
				}
			}
			currentNum++;
		}
		else {
			int savex=topx, savey=topy;
			for (int x=topx; x<topx+size; x++)  {
				for (int y=topy; y<topy+size; y++) {
					if (grid[x][y] != 0) {
						savex = x;
						savey = y;
					}
				}
			}
			if (savex < topx + size/2 && savey < topy + size/2) {
				tileRec(size/2, topx, topy);
				grid[topx+size/2][topy+size/2-1] = currentNum;
				grid[topx+size/2][topy+size/2] = currentNum;
				grid[topx+size/2-1][topy+size/2] = currentNum;
				currentNum++;
				tileRec(size/2, topx, topy+size/2);
				tileRec(size/2, topx+size/2, topy);
				tileRec(size/2, topx+size/2, topy+size/2);
				
			}
			else if (savex < topx + size/2 && savey >= topy + size/2) {
				tileRec(size/2, topx, topy+size/2);
				grid[topx+size/2][topy+size/2-1] = currentNum;
				grid[topx+size/2][topy+size/2] = currentNum;
				grid[topx+size/2-1][topy+size/2-1] = currentNum;
				currentNum++;
				tileRec(size/2, topx, topy);
				tileRec(size/2, topx+size/2, topy);
				tileRec(size/2, topx+size/2, topy+size/2);
			}
			else if (savex >= topx + size/2 && savey < topy + size/2) {
				tileRec(size/2, topx+size/2, topy);
				grid[topx+size/2-1][topy+size/2] = currentNum;
				grid[topx+size/2][topy+size/2] = currentNum;
				grid[topx+size/2-1][topy+size/2-1] = currentNum;
				currentNum++;
				tileRec(size/2, topx, topy);
				tileRec(size/2, topx, topy+size/2);
				tileRec(size/2, topx+size/2, topy+size/2);
			}
			else {
				tileRec(size/2, topx+size/2, topy+size/2);
				grid[topx+size/2-1][topy+size/2] = currentNum;
				grid[topx+size/2][topy+size/2-1] = currentNum;
				grid[topx+size/2-1][topy+size/2-1] = currentNum;
				currentNum++;
				tileRec(size/2, topx+size/2, topy);
				tileRec(size/2, topx, topy+size/2);
				tileRec(size/2, topx, topy);
			}
		}
	}
	
	public void print() {
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid[i].length; j++)
				System.out.print(grid[i][j] + "\t");
			System.out.println();
		}
	}
}