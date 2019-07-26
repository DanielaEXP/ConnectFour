public class Memento {
	
    private int boardState[][];
    private int diff;

	public Memento() {}
	
    public Memento(int boardState[][], int diff) {	// Prende la configurazione del campo da gioco
    
    	this.boardState = boardState;
        this.diff = diff;
    
    }

    public int[][] getStateXY() {
    
    	return boardState;
		
    }
    
    public int getStateDiff() {
    
    	return diff;
		
    }

}