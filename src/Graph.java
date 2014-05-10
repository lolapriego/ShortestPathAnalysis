
public class Graph {
	GraphNode [] nodes;
	int numberNodes;
	
	public Graph(){
		nodes = new GraphNode [5];
		numberNodes = 0;
	}
	
	public boolean addNode(GraphNode n){
		if(numberNodes == 5) return false;
		n.id = numberNodes;
		nodes[numberNodes++] = n;
		return true;
	}
}
