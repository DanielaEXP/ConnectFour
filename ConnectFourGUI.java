/**
 /**
 * ConnectFourGUI.java
 *
 * Draws the Connect Four board using Connect Four Model
 *
 */

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ConnectFourGUI extends JComponent implements Strategy {
	
	private static final long serialVersionUID = 1L;
	private static final int cellPxl = 85; // cell size
	private static final int numCol = 7;
	private static final int numRow = 6;
	
	private int colChip;
	private int rowChip;
	private int diff;
	private int result = 0;
	
	final JOptionPane game = new JOptionPane();
	Object[] options = {"WON", "LOSE"};
	
	private ConnectFourModel model;


	public ConnectFourGUI(ConnectFourModel cfm, int col, int row) {
	
		setPreferredSize(new Dimension(cellPxl*7+10,cellPxl*7+10)); 
		setBackground(Color.white);
		model = cfm;
		colChip = col;
		rowChip = row;
	
	}

	
	public void paintComponent(Graphics g) {
	
		g.setColor(new Color(0,0,150));
		g.fillRect(5,85,cellPxl*numCol+10-8,cellPxl*(numRow+1)+30-109);
		drawBoard(g, g);
		drawChip(g, g, colChip,rowChip);
	
	}


	public void drawChip(Graphics g, Graphics g1, int col, int row) {
		
		int x = cellPxl;
		int y = cellPxl;

		//Check for player win.
		if (model.win()) {
			
			if(model.getPlayer()==2) {
				
				g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18));
				g.setColor(new Color(0,250,0));
				g.drawString("You Won! Click 'Start new game' to play again!",225,85);
				setResult(2);
			
			} else {
				
				g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18));
				g.setColor(new Color(250,0,0));
				g.drawString("You Lose! Click 'Start new game' to play again!",225,85);
			
			}
			//Draws the new chip in a not visible area.
			col = -5;

		}
		//Check if draw.
		if (model.full()) {
			
			g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18));
			g.setColor(new Color(50,50,50));
			g.drawString("Draw! Click 'Start new game' to play again!",225,85);
			col = -5;
		
		}

		//If player's turn, draws a chip to be dropped in.
		if (model.getPlayer()==1) {
			
			g1.setColor(new Color(200,0,0));
			g1.fillOval(5+x*col+10,y+y*row+10,cellPxl-20,cellPxl-20);
			g.setColor(new Color(155,0,0));
			g.fillOval(5+x*col+15,y+y*row+15,cellPxl-30,cellPxl-30);
		
		} else {
			
			System.out.println("DIFF: " + getDiff()); // Dev log
			
			Minimax comp = new Minimax(model.getGrid(),diff);
			int xEnemy = comp.calcValue();
			int yEnemy = model.drop(xEnemy);
			
			//Check for computer win.
			if (model.win()) {
				
				if(model.getPlayer()==1) {
					
					g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18));
					g.setColor(new Color(250,0,0));
					g.drawString("You Lose! Click 'Start new game' to play again!",225,85);
				
				} else if(model.getPlayer()==2) {
					
					g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18));
					g.setColor(new Color(0,250,0));
					g.drawString("You Won! Click 'Start new game' to play again!",225,85);
					setResult(2);
				
				}
				//Draws the new chip in a not visible area.
				col = -5;
	
			} else {
				
				g1.setColor(new Color(200,0,0));
				g1.fillOval(5+x*col+10,y+y*row+10,cellPxl-20,cellPxl-20);
				g.setColor(new Color(155,0,0));
				g.fillOval(5+x*col+15,y+y*row+15,cellPxl-30,cellPxl-30);
			
			}

		}
		
		//Draws the chips already filled in.
		for (int r =5; r>=0; r--) {
			
    		for (int c=0; c<7; c++) {
    			
    			if (model.getBoardSlot(c,r) == 1) {
    				
    				g1.setColor(new Color(200,0,0));
					g1.fillOval(5+x*c+10,y+y*r+10,cellPxl-20,cellPxl-20);
    				g.setColor(new Color(155,0,0));
					g.fillOval(5+x*c+15,y+y*r+15,cellPxl-30,cellPxl-30);
    			
    			} else if (model.getBoardSlot(c,r) == 2) {
    				
    				g1.setColor(new Color(200,200,0));
					g1.fillOval(5+x*c+10,y+y*r+10,cellPxl-20,cellPxl-20);
    				g.setColor(new Color(155,155,0));
					g.fillOval(5+x*c+15,y+y*r+15,cellPxl-30,cellPxl-30);
    			
    			}
    		}
    	}
	}
	
	// Set difficulty.
	@Override
	public void setDiff(int d) {
		
		this.diff = d;
	
	}
	
	public int getDiff() {
	
		return diff;
	
	}
	
	public void setResult(int res) {
	
		this.result = res;
	
	}
	
	public int getResult() {
	
		return result;
	
	}

	//Set the current column position of the chip.
	public void setCol(int col) {
		
		colChip = col;
	
	}
	
	//Returns the current column position of the chip.
	public int chipCol() {
		
		return colChip;
	
	}

	public void drawBoard(Graphics g, Graphics g1) {
		
		int x = cellPxl;
		int y = cellPxl;
		int r,c, a, b;
		
		//Draw a 7*6 grid
		g.setColor(Color.black);
		g.drawRect(5,y,x*numCol,y*numRow);

		for (r=0;r<numRow;r++) {
			
			g.drawLine(5,y+y*r,x*7+5,y+y*r);

		} 
		
		for (c=0;c<numCol;c++) {
		
			g.drawLine(x+5+x*c,y,x+5+x*c,y*7);
		
		}
		
		for (a=0;a<numCol;a++) {
		
			for (b=0;b<numRow;b++) {
			
				g1.setColor(new Color(255,255,255));
				g1.fillOval(5+x*a+10,y+y*b+10,cellPxl-20,cellPxl-20);
			
			}
		}
	}
}

