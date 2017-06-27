
public class Controller {
	private View myView;
	private Tromino solver;
	
	public Controller() {
		myView = new View(this);
	}
	
	public static void main(String[] args) {
		new Controller();
	}
	
	public void solve() {
		solver = new Tromino((int)Math.pow(2, myView.getBoardSize()), myView.getEmptyX(), myView.getEmptyY());
		solver.tile();
		myView.tile(solver.getGrid());
	}
}
