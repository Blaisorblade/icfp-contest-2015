package CDIO.pathFinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import CDIO.pathFinder.heuristics.AStarHeuristic;

public class AStar<Elem> {
	private AreaMap map;
	private AStarHeuristic heuristic;
	/**
	 * closedList The list of Nodes not searched yet, sorted by their distance to the goal as guessed by our heuristic.
	 */
	private ArrayList<Node> closedList;
	private SortedNodeList openList;
	private ArrayList<Point> shortestPath;

	private Elem elem;

	public AStar(AreaMap<Elem> map, Elem elem, AStarHeuristic heuristic) {
		this.elem = elem;
		this.map = map;
		this.heuristic = heuristic;
		closedList = new ArrayList<Node>();
		openList = new SortedNodeList();
	}

	public ArrayList<Point> calcShortestPath() {

		map.getStartNode().setDistanceFromStart(0);

		closedList.clear();
		openList.clear();
		openList.add(map.getStartNode());

		// while we haven't reached the goal yet
		while(openList.size() != 0) {

			//get the first Node from non-searched Node list, sorted by lowest distance from our goal as guessed by our heuristic
			Node<Elem> current = openList.getFirst();

			// check if our current Node location is the goal Node. If it is, we are done.
			if(current.getX() == map.getGoalLocationX() && current.getY() == map.getGoalLocationY()) {
				return reconstructPath(current);
			}

			//move current Node to the closed (already searched) list
			openList.remove(current);
			closedList.add(current);

			//go through all the current Nodes neighbors and calculate if one should be our next step
			for(Node<Elem> neighbor : current.getNeighborList()) {
				boolean neighborIsBetter;

				//if we have already searched this Node, don't bother and continue to the next one
				if (closedList.contains(neighbor))
					continue;

				//also just continue if the neighbor is not an obstacle
				if (neighbor.canPlace(elem)) {

					// calculate how long the path is if we choose this neighbor as the next step in the path
					float neighborDistanceFromStart = (current.getDistanceFromStart() + map.getDistanceBetween(current, neighbor));

					//add neighbor to the open list if it is not there
					if(!openList.contains(neighbor)) {
						openList.add(neighbor);
						neighborIsBetter = true;
						//if neighbor is closer to start it could also be better
					} else if(neighborDistanceFromStart < current.getDistanceFromStart()) {
						neighborIsBetter = true;
					} else {
						neighborIsBetter = false;
					}
					// set neighbors parameters if it is better
					if (neighborIsBetter) {
						neighbor.setPreviousNode(current);
						neighbor.setDistanceFromStart(neighborDistanceFromStart);
						neighbor.setHeuristicDistanceFromGoal(heuristic.getEstimatedDistanceToGoal(neighbor.getPoint(), map.getGoalPoint()));
					}
				}

			}
		}
		return null;
	}

	private ArrayList<Point> reconstructPath(Node node) {
		ArrayList<Point> path = new ArrayList<Point>();
		while(!(node.getPreviousNode() == null)) {
			path.add(0,node.getPoint());
			node = node.getPreviousNode();
		}
		this.shortestPath = path;
		return path;
	}

	private class SortedNodeList {

		private ArrayList<Node> list = new ArrayList<Node>();

		public Node<Elem> getFirst() {
			return list.get(0);
		}

		public void clear() {
			list.clear();
		}

		public void add(Node<Elem> node) {
			list.add(node);
			Collections.sort(list);
		}

		public void remove(Node<Elem> n) {
			list.remove(n);
		}

		public int size() {
			return list.size();
		}

		public boolean contains(Node<Elem> n) {
			return list.contains(n);
		}
	}

}
