import java.util.Random;

public class ConnectForGUIr implements Strategy {

	private int res; 
	
	@Override
	public void setDiff(int d) {
		
		Random random = new Random();
		int res = random.nextInt(2);

		if(res == 0) {
	
			setRes(2);
			System.out.println("Gui diff must be 2");
			System.out.println("EASY");
		
		} else {
		
			setRes(4);
			System.out.println("Gui diff must be 4");
			System.out.println("HARD");
		
		}
		
		System.out.println("RES: " + getRes());
	}
	
	public void setRes(int res) {
		
		this.res = res;
	
	}
	
	public int getRes() {
		
		return res;
	
	}
	
}
