public class Originator {
	
    private int boardState[][];
    private int diff;
    
   /* lots of memory consumptive private data that is not necessary to define the
    * state and should thus not be saved. Hence the small memento object. */

    public void setState(int boardState[][], int diff) {	// Imposto il campo da gioco
    
    	System.out.println("Originator: Board set Ok ");
        this.boardState = boardState;
        this.diff = diff;
    
    }

    public Memento save() {		// Salvo le coordinate della posizione a parte
    
    	System.out.println("State saved");
        return new Memento(boardState, diff);
    
    }
    
    public void restore(Memento m) {	// Riprendo il campo da gioco salvato
    
    	boardState = m.getStateXY();
        diff = m.getStateDiff();
        System.out.println("State restored\n");
    
    }
    
    public int[][] getRestXY(Memento m) {	// Ritorno la configurazione del campo da gioco salvato
	
    	boardState = m.getStateXY();
		return boardState;
    
    }
    
    public int getRestDiff(Memento m) {	// Ritorno la configurazione del campo da gioco salvato
	
    	diff = m.getStateDiff();
		return diff;
    
    }
    
}
