
public class GraphNode {
	GraphNode [] neighbors;
	int costUsd [];
	int costTime [];
	String region;
	int number;
	GraphNode parent;
	int distance; // for Dijkstra
	int id; // for Floyd-Warshall
	int visited;
	
	public GraphNode(String region){
		this.region = region;
		number = 0;
		neighbors = new GraphNode [5];
		costUsd = new int [5];
		costTime = new int[5];
	}
	
	public boolean addNeighbor(GraphNode n, int costUsd, int costTime){
		if(number == 5 || n == null) return false;
		neighbors[number] = n;
		this.costUsd[number] = costUsd;
		this.costTime[number++] = costTime;
		return true;
	}
	
	public String toString(){
		return this.region;
	}
	
	public boolean equals(GraphNode n){
		if(!(n instanceof GraphNode)) return false;
		return (n.distance == this.distance) && (n.visited == this.visited) && (n.number == this.number) && this.region.equals(n.region);
	}
	
	public int distanceTo(GraphNode n){
		for(int i = 0; i < number; i++){
			if(n.equals(neighbors[i])) return i;
		}
		return -1;
	}
}
