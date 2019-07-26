import java.util.ArrayList;

public class Caretaker {
	
    private ArrayList<Memento> mementos = new ArrayList<>();	// Creo un pool di salvataggi
    private int n;
    
    public void addMemento(Memento m) {		// Aggiungo un (altro) salvataggio
    
    	mementos.add(m);
    
    }

    public Memento getMemento() {	// Riprendo il salvataggio piu' recente (ultimo)
    
    	n = mementos.size() - 1;
    	return mementos.get(n);
   
    }
}