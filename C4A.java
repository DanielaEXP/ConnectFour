import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class C4A extends JApplet { 
		//Initial position of chip
		private int initr = 3;
		private int initc = -1;
		private int rows = 6;
		private int cols = 7;
		private int board[][];
		
		//Uses the modules
		private ConnectFourModel cfm = new ConnectFourModel();
		private ConnectFourGUI cgui = new ConnectFourGUI(cfm , initr, initc);
		private Context context = new Context(cgui);
		private ConnectForGUIr ogui = new ConnectForGUIr();
		private Context cont = new Context(ogui);
		
		Caretaker caretaker = new Caretaker();
		Originator originator = new Originator();
		
		//New game difficulty dialog box
		final JOptionPane newGame = new JOptionPane();
		Object[] options = {"Defence", "Attack", "Careful"};
		
		//Difficulty (2 is easy, 4 is med)
		private int diff = 0;
		private String showDiff = "";
		
		//private JTextField nickname;
		private String userNick;
		private String scores;
		
		ArrayList<String> list = new ArrayList<String> ();
		ArrayList<String> last = new ArrayList<String> ();
		ArrayList<String> lastScore = new ArrayList<String> ();
		
		//private int indexPlayer;
		private int scoring;

        //Creates a new instance of ConnectFour.
		public C4A() {
     		init();
     	}
		
		public C4A(int res) {
			this.scoring = res;
		}
		
		public void init() {
   			this.setSize(new Dimension(600,600));
   			
   			//Panel components
   			JLabel nick = new JLabel("Nickname: ");  
		    nick.setBounds(50,50, 100,30);  
		    JLabel scoreList = new JLabel();

			JTextField nickname = new JTextField();
			
			nickname.setBounds(50,100,200,30); 
			nickname.setPreferredSize(new Dimension(100,30));
			
			JButton initDiff = new JButton("Choose difficulty");
			JButton nickConf = new JButton("Confirm");
			JButton showList = new JButton("Show score list");
			JPanel onlyPanel = new JPanel();
			JPanel scorePanel = new JPanel();
			JPanel listPanel = new JPanel();
			
			onlyPanel.setLayout(new FlowLayout());
			onlyPanel.add(initDiff);
			onlyPanel.add(nick);
			onlyPanel.add(nickname);
			onlyPanel.add(nickConf);
			
			scorePanel.setLayout(new FlowLayout());
			scorePanel.add(scoreList);
			
			listPanel.setLayout(new FlowLayout());
			listPanel.add(showList);
			
			scoreList.setVisible(false); 
			initDiff.addActionListener(new initDiffListener());
			
			nickConf.addActionListener(new ActionListener() {
				
				   public void actionPerformed(ActionEvent ae) {
					   
					   userNick = nickname.getText();  
					   
					   if(userNick.equals("")) {
						
						   newGame.showMessageDialog(null, "Insert a nickname", null,JOptionPane.WARNING_MESSAGE);
						   init();
						   nick.setForeground(Color.RED);
		
					   } else {
						   
						   writeOnScoreFile(userNick, 0);
						   writeOnFile(userNick);
					   }
				   }
			});
			
			showList.addActionListener(new ActionListener() {
				
				   public void actionPerformed(ActionEvent ae) {
		   			  
					   readFromFile();
					   readFromScoreFile();

					   scores = "<html>";
			
					   for(int j = 1; j < last.size(); j++) {
						   
						   if(last.get(j).equals(String.valueOf(0))) {
							
							   lastScore.add(last.get(j-1));
						   
						   }
						   
					   }
					   
					   lastScore.add(last.get(last.size()-1));
					   System.out.println(lastScore);
					   
					   
					   for(int i = 0; i < list.size(); i++) {
						
						   scores += list.get(i);
						   scores += " ";
				           scores += lastScore.get(i);
				           scores += "<br/>";
					   
					   }
					   
					   scores += "</html>";
					   
					   scoreList.setText(scores);
					   scoreList.setVisible(true);
				   }
			});
			
			//Content panel containing the button
   			Container cont = getContentPane();
   			cont.setLayout(new BorderLayout());
   			cont.add(onlyPanel, BorderLayout.NORTH);
   			cont.add(scorePanel, BorderLayout.CENTER);
   			cont.add(listPanel, BorderLayout.SOUTH);
			
			//Content keyListener
			initDiff.setFocusable(true);
			initDiff.addKeyListener(new keyPress());

   			validate();

    	}
		
   		public void intit() {
   			
   			this.getContentPane().removeAll();
   			System.out.println("diff: " + diff);
   			this.setSize(new Dimension(900,900)); // DIMENSIONE FINESTRA DI GIOCO
   			
   			JButton left = new JButton("Move Left");
   			JButton down = new JButton("Move Down");
   			JButton right = new JButton("Move Right");

   			JPanel topPanel = new JPanel();
   			topPanel.setLayout(new FlowLayout());
   			topPanel.add(left);
   			topPanel.add(down);
   			topPanel.add(right);

   			//Panel components at bottom
			JButton reset = new JButton("Start new game");
			JButton newDiff = new JButton("Choose difficulty");
			JButton save = new JButton("Save");
			JButton restore = new JButton("Restore");
		
   			JPanel botPanel = new JPanel();
   			botPanel.setLayout(new FlowLayout());
   			botPanel.add(save);
			botPanel.add(reset);
			botPanel.add(newDiff);
			botPanel.add(restore);

   			//Listeners for each button
   			left.addActionListener(new leftListener());
   			right.addActionListener(new rightListener());
			down.addActionListener(new downListener());
			reset.addActionListener(new resetListener());
			newDiff.addActionListener(new newDiffListener());
			save.addActionListener(new saveListener());
			restore.addActionListener(new restoreListener());
			
			//Content panel containing top panel and game board
   			Container content = getContentPane();
   			content.setLayout(new BorderLayout());
   			content.add(topPanel, BorderLayout.NORTH);
   			content.add(cgui, BorderLayout.CENTER);
   			content.add(botPanel, BorderLayout.SOUTH);
			topPanel.setFocusTraversalKeysEnabled(false);

			//Content keyListener
			topPanel.setFocusable(true);
			topPanel.addKeyListener(new keyPress());
			
			left.setFocusable(true);
			left.addKeyListener(new keyPress());
			
			down.setFocusable(true);
			down.addKeyListener(new keyPress());
			
			right.setFocusable(true);
			right.addKeyListener(new keyPress());
			
			reset.setFocusable(true);
			reset.addKeyListener(new keyPress());
			
			newDiff.setFocusable(true);
			newDiff.addKeyListener(new keyPress());

   			setContentPane(content);
   			validate();
   			
    	}
   		
   		public String getUserNick() {
   		
   			return userNick;
   		
   		}
   		
   		
   		public void writeOnFile(String userNickname) {
 
   			try {
   				
   				BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Daniela\\workspace\\C4A_COMPLETE\\users", true));
   				bw.append(userNickname);
   				bw.append('\n');
   				
   				System.out.println("WRITING: " + userNickname);
   				bw.close();

   			} catch (IOException e) {
   		
   				e.printStackTrace();
   			
   			} 
   			
		}
   		
   		public void readFromFile() {
   			   
   			try { 
   			
   				 BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Daniela\\workspace\\C4A_COMPLETE\\users"));
   			     String currLine;
   			     
   			     while ((currLine = br.readLine()) != null) {
   			    
   			    	 list.add(currLine);
   			     
   			     } 
   			     
   			     System.out.println("READING: " + list);
   			     br.close();
   			     
   			} catch (IOException e) {
				
   				e.printStackTrace();
			
   			}
   			     
   		}
   		
   		public void writeOnScoreFile(String userNickname, int sco) {
   			
   			String score = String.valueOf(sco);
   			
   			try {
   				
   				BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Daniela\\workspace\\C4A_COMPLETE\\scores", true));
   				bw.append(score);
   				bw.append('\n');
   				
   				System.out.println("WRITE: " + score);
   				bw.close();

   			} catch (IOException e) {
   			
   				e.printStackTrace();
   			
   			} 
   			
		}
   		
   		public void readFromScoreFile() {
			   
   			try { 
   			
   				 BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Daniela\\workspace\\C4A_COMPLETE\\scores"));
   			     String currLine;
   			     
   			     while ((currLine = br.readLine()) != null) {
   			    
   			    	 last.add(currLine);
   			     
   			     } 
   			     
   			     System.out.println("READ: " + last);
   			     br.close();
   			     
   			} catch (IOException e) {
				
   				e.printStackTrace();
			
   			}
   			     
   		}
   		
		private void reset() {
			
				cfm.reset();
				
    			if(cgui.getResult() != 0) {
    		
    				scoring++;
    				writeOnScoreFile(userNick, scoring); 
    				cgui.setResult(0);
    			
    			}
    			
    			if (diff==0) { //If no difficulty has been set, choose difficulty.
    			
    				chooseDiff();
    			
    			}
    			
    			cgui.setDiff(diff); //Sets difficulty to given one.
    			
    			if (diff == 2) {
    			
    				showDiff = ("Current difficulty: Defence");
    			
    			} else if (diff == 4) {
    			
    				showDiff = ("Current difficulty: Attack");
    			
    			} else if (diff == 6) {
    			
    				showDiff = ("Current difficulty: Careful");
    			
    			}
    			
    			cgui.repaint(); // Select the relative profile repaint
		}
		
		class initDiffListener implements ActionListener {
			
    		public void actionPerformed(ActionEvent ae) {

    			chooseInitDiff();
    		
    		}

    	}
		
		private void chooseInitDiff() {

			int i = newGame.showOptionDialog(null, showDiff, "Select difficulty", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
			
			if (i == 0) {    
			
				diff = 2;
				showDiff = ("Current difficulty: Defence");
				cgui.setDiff(diff);

			} else if (i == 1) {
		
				diff = 4;
				showDiff = ("Current difficulty: Attack");
				cgui.setDiff(diff);

			} else if (i == 2) {
				
				showDiff = ("Current difficulty: Careful");
		
				cont.executeStrategy(0);
				diff = ogui.getRes();
				System.out.println("res: " + diff); // Dev logs
				cgui.setDiff(diff);
			
			} else if (showDiff=="") { //Make sure a difficulty is chosen.
			
				newGame.showMessageDialog(null, "Please try again.", "Error",JOptionPane.WARNING_MESSAGE); 
				chooseDiff();
			
			}
	
			intit();
			
		}
		
    	class resetListener implements ActionListener {
    		
    		public void actionPerformed(ActionEvent ae) {
				
    			reset();
    		
    		}
    		
    	}

		class newDiffListener implements ActionListener {
			
    		public void actionPerformed(ActionEvent ae) {
    		
    			chooseDiff();
    			reset();
    		
    		}
    		
    	}

		//If Move Left button is clicked, the chip is moved left.
    	class leftListener implements ActionListener {
    		
    		public void actionPerformed(ActionEvent ae) {
    			
    			if (diff==0) { //If no difficulty has been set, choose difficulty.
    		
    				chooseDiff();		
    				context.executeStrategy(diff);
    			
    			}
    			
    			moveLeft();
    		}
    		
    	}

    	private void moveLeft() {
    		
    		if(cgui.getResult() == 1) {
   				
    			writeOnScoreFile(userNick, cgui.getResult());
   			
    		}
    		
    		int col = cgui.chipCol();
    		
    		if (col <= 6 && col >0) {
    		
    			cgui.setCol(col-1);
    			cgui.repaint();
    		
    		}
    		
    	}

		private void moveRight() {
			
			if(cgui.getResult() == 1) {
   			
				writeOnScoreFile(userNick, cgui.getResult());
   			
			}
			
    		int col = cgui.chipCol();
    			
    		if (col < 6 && col >=0) {
    		
    			cgui.setCol(col+1);
    			cgui.repaint();
    		
    		}
    		
    	}

    	private void moveDown() {
    		
    		if(cgui.getResult() == 1) {
   			
    			writeOnScoreFile(userNick, cgui.getResult());
   			
    		}
    		
    		int col = cgui.chipCol();
    		int dropRow = cfm.drop(col);
    		
    		if (dropRow != -1) {
    		
    			cgui.setCol(col);
    			cgui.repaint();
    		
    		}
    		
    	} 
    	
    	private void chooseDiff() { //Select difficulty method.

    		if (diff == 0) { //If diff = 0 (no games have been played), then do not show message dialogs.

    			int i = newGame.showOptionDialog(null, showDiff, "Select difficulty", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
   				
    			if (i == 0) {    
    		
    				diff = 2;
    				showDiff = ("Current difficulty: Defence");

    			} else if (i == 1) {
    				
    				diff = 4;
    				showDiff = ("Current difficulty: Attack");

    			} else if (i == 2) {
    				
    				showDiff = ("Current difficulty: Careful");

    				cont.executeStrategy(0);
    				diff = ogui.getRes();
    				System.out.println("res1: " + diff); // Dev logs
    				
    			} else if (showDiff=="") { //Make sure a difficulty is chosen.
    				
    				newGame.showMessageDialog(null, "Please try again.", "Error",JOptionPane.WARNING_MESSAGE); 
    				chooseDiff();
    			}
    			
    		} else { // If diff is already established, show message dialogs.

    			int i = newGame.showOptionDialog(null, showDiff, "Select difficulty", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    		
    			if (i == 0) {
    				
    				diff = 2;
    				newGame.showMessageDialog(null, "Difficulty changed to Defence", "Defence",JOptionPane.WARNING_MESSAGE);
    			
    			} else if (i == 1) {
    			
    				diff = 4;
    				newGame.showMessageDialog(null, "Difficulty changed to Attack", "Attack",JOptionPane.WARNING_MESSAGE);

    			} else if (i == 2) {
    			
    				cont.executeStrategy(0);
    				diff = ogui.getRes();
    				System.out.println("res1: " + diff); // Dev logs
    				newGame.showMessageDialog(null, "Difficulty changed to Careful", "Careful",JOptionPane.WARNING_MESSAGE);
    			
    			}
	
    		}

    	}

		//If Move Right button is clicked, the chip is moved right.
    	class rightListener implements ActionListener {
    	
    		public void actionPerformed(ActionEvent ae) {
    		
    			if (diff==0) { //If no difficulty has been set, choose difficulty.

    				chooseDiff();
    				context.executeStrategy(diff);
    				
    			}

    			moveRight();
    		}
    		
    	}

    	//If Move Down button is clicked, the chip is moved down to the lowest row.
    	//The computer acts as well.
    	class downListener implements ActionListener {
    	
    		public void actionPerformed(ActionEvent ae) {
    		
    			if (diff==0) { //If no difficulty has been set, choose difficulty.
    			
    				chooseDiff();	
    				context.executeStrategy(diff);
    				
    			}
    			
    			moveDown();
    		}
    		
    	}
    	
    	class saveListener implements ActionListener {
			
    		public void actionPerformed(ActionEvent ae) {
    			
    			originator.setState(cfm.getGrid(), cgui.getDiff());
                caretaker.addMemento(originator.save());
    		
    		}

    	}

		class restoreListener implements ActionListener {
	
			public void actionPerformed(ActionEvent ae) {
			
				intit();
				
				board = originator.getRestXY(caretaker.getMemento());
				diff = originator.getRestDiff(caretaker.getMemento());
		
				if (board != null) {
				
					originator.restore(caretaker.getMemento());
				
				}
				    
				//originator.restore(caretaker.getMemento());
				
				cfm.setGrid(board, cfm.getPlayer());
				cgui.setDiff(diff);
				
				cgui.repaint();
			}

		}

		class keyPress implements KeyListener {
    	
    		//If left, down or right is pressed, redraw board.
    		public void keyPressed(KeyEvent e) {
    		
    			int keyCode = e.getKeyCode();
    			System.out.println(keyCode);
    			
    			if (diff==0) { //Choose diff if no diff is set.
    			
    				chooseDiff();	
    				context.executeStrategy(diff);
    				
    			}
    			
    			if (keyCode == 37) { // if left is pressed:

    				moveLeft();

    			}
    		
    			if (keyCode == 40) { // if down is pressed:
    			
    				moveDown();
    			
    			}
    			
    			if (keyCode == 39) { // if right is pressed:
    			
    				moveRight();
    			
    			}
    			if (keyCode == 10) {
    				
    				reset();
    			
    			}
    			
    		}
    		
    		//Override methods
    		public void keyTyped(KeyEvent e) {}
    		public void keyReleased(KeyEvent e) {}
    	
		}
}