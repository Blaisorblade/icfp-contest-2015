package CDIO.pathFinder;

import java.awt.Point;
import java.util.ArrayList;

public class Node<Elem> implements Comparable<Node<Elem>> {
	/* Nodes that this is connected to */
	AreaMap<Elem> map;
	boolean visited;
	float distanceFromStart;
	float heuristicDistanceFromGoal;
	Node<Elem> previousNode;
	int x;
	int y;
	public boolean isStart;
	public boolean isGoal;
	public boolean isPath;

	Node(int x, int y, AreaMap<Elem> map) {
		this.x = x;
		this.y = y;
		this.map = map;
		this.visited = false;
		this.distanceFromStart = Integer.MAX_VALUE;
		this.isStart = false;
		this.isGoal = false;
	}

	Node (int x, int y, AreaMap<Elem> map, boolean visited, int distanceFromStart, boolean isStart, boolean isGoal) {
		this.x = x;
		this.y = y;
		this.map = map;
		this.visited = visited;
		this.distanceFromStart = distanceFromStart;
		this.isStart = isStart;
		this.isGoal = isGoal;
	}

	// commented out the neighbours that are not reachable from a
	// hexagon
	public ArrayList<Node<Elem>> getNeighborList() {
		ArrayList<Node<Elem>> neighborList = new ArrayList<Node<Elem>>();
		// if (!(y==0)) {
		// 	neighborList.add(map.getNode(x, (y-1)));
		// }
		// if (!(y==0) && !(x==(map.getMapWith()-1))) {
		// 	neighborList.add(map.getNode(x+1,y-1));
		// }
		if (!(x==(map.getMapWith()-1))) {
			neighborList.add(map.getNode(x+1,y));
		}
		if (!(x==(map.getMapWith()-1)) && !(y==(map.getMapHeight()-1))) {
			neighborList.add(map.getNode(x+1,y+1));
		}
		if (!(y==(map.getMapHeight()-1))) {
			neighborList.add(map.getNode(x,y+1));
		}
		// if (!(x==0) && !(y==(map.getMapHeight()-1))) {
		// 	neighborList.add(map.getNode(x-1,y+1));
		// }
		if (!(x==0)) {
			neighborList.add(map.getNode(x-1,y));
		}
		// if (!(x==0) && !(y==0)) {
		// 	neighborList.add(map.getNode(x-1,y-1));
		// }
		return neighborList;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public float getDistanceFromStart() {
		return distanceFromStart;
	}

	public void setDistanceFromStart(float f) {
		this.distanceFromStart = f;
	}

	public Node getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

	public float getHeuristicDistanceFromGoal() {
		return heuristicDistanceFromGoal;
	}

	public void setHeuristicDistanceFromGoal(float heuristicDistanceFromGoal) {
		this.heuristicDistanceFromGoal = heuristicDistanceFromGoal;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point getPoint() {
		return new Point(x,y);
	}

	public boolean canPlace(Elem el) {
		return map.game.canPlaceAt(el, x, y);
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public boolean isGoal() {
		return isGoal;
	}

	public void setGoal(boolean isGoal) {
		this.isGoal = isGoal;
	}

	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}

	public boolean equals(Node node) {
		return (node.x == x) && (node.y == y);
	}

	public int compareTo(Node<Elem> otherNode) {
		float thisTotalDistanceFromGoal = heuristicDistanceFromGoal + distanceFromStart;
		float otherTotalDistanceFromGoal = otherNode.getHeuristicDistanceFromGoal() + otherNode.getDistanceFromStart();

		if (thisTotalDistanceFromGoal < otherTotalDistanceFromGoal) {
			return -1;
		} else if (thisTotalDistanceFromGoal > otherTotalDistanceFromGoal) {
			return 1;
		} else {
			return 0;
		}
	}
}
