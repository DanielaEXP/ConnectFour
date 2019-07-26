import java.awt.Graphics;

public class Context {
	
   private Strategy strategy;

   public Context(Strategy strategy) {
    
	   this.strategy = strategy;
   
   }

   public void executeStrategy(int diff) {
   
	   strategy.setDiff(diff);
   
   }
}