package astar;

public interface GameInterface<Elem> {
	public int width();
	public int height();
	public boolean canPlaceAt(Elem el, int x, int y);
	public int startLocationX();
	public int startLocationY();
	public int targetLocationX();
	public int targetLocationY();
}
