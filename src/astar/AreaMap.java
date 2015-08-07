package CDIO.pathFinder;

import java.awt.Point;
import java.util.ArrayList;

/**
 * The AreaMap holds information about the With, Height,
 * Start position, Goal position and Obstacles on the map.
 * A place on the map is referred to by it's (x,y) coordinates,
 * where (0,0) is the upper left corner, and x is horizontal and y is vertical.
 */
public class AreaMap<Elem> {
	private ArrayList<ArrayList<Node>> map;

	public GameInterface<Elem> game;

	public AreaMap(GameInterface<Elem> game) {
		this.game = game;
		createMap();
	}

	/**
	 * Sets up the Nodes of the map with the With and Height specified in the constructor
	 * or set methods.
	 */
	private void createMap() {
		Node node;
		map = new ArrayList<ArrayList<Node>>();
		for (int x = 0; x < game.width(); x++) {
			map.add(new ArrayList<Node>());
			for (int y = 0; y < game.height(); y++) {
				node = new Node(x, y, this);
				if (x == game.startLocationX() && y == game.startLocationY()) {
					node.setStart(true);
				}
				if (x == game.targetLocationX() && y == game.targetLocationY()) {
					node.setGoal(true);
				}
				map.get(x).add(node);
			}
		}
	}

	public Node getNode(int x, int y) {
		return map.get(x).get(y);
	}

	public int getStartLocationX() {
		return game.startLocationX();
	}

	public int getStartLocationY() {
		return game.startLocationY();
	}

	public Node getStartNode() {
		return getNode(getStartLocationX(), getStartLocationY());
	}

	public int getGoalLocationX() {
		return game.targetLocationX();
	}

	public int getGoalLocationY() {
		return game.targetLocationY();
	}

	public Point getGoalPoint() {
		return new Point(getGoalLocationX(), getGoalLocationY());
	}

	/**
	 * @return Node	The Goal Node
	 * @see Node
	 */
	public Node getGoalNode() {
		return getNode(getGoalLocationX(), getGoalLocationY());
	}

	/**
	 * Determine the distance between two neighbor Nodes
	 * as used by the AStar algorithm.
	 *
	 * @param node1 any Node
	 * @param node2 any of Node1's neighbors
	 * @return Float - the distance between the two neighbors
	 */
	public float getDistanceBetween(Node node1, Node node2) {
		//if the nodes are on top or next to each other, return 1
		if (node1.getX() == node2.getX() || node1.getY() == node2.getY()){
			return 1;//*(mapHeight+mapWith);
		} else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
			return (float) 1.9;//*(mapHeight+mapWith);
		}
	}

	public int getMapWith() {
		return game.width();
	}
	public int getMapHeight() {
		return game.height();
	}

	/**
	 * Removes all the map information about start location, goal location and obstacles.
	 * Then remakes the map with the original With and Height.
	 */
	public void clear() {
		createMap();
	}
}
