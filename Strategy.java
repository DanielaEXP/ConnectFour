import java.awt.Graphics;

public interface Strategy {
	
   public void setDiff(int d);
   
   public ConnectFourModel model = new ConnectFourModel();
   public ConnectFourGUI ad = new ConnectFourGUI(model , 3, -1);
   public Context con = new Context(ad);
	
}