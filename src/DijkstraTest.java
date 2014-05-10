
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class DijkstraTest {
	
	/*
	 * Test
	 * We create a weighted undirected Graph and test our shortest path algorithms with it
	 */
	public static void main(String [] args){
		long startTime = System.nanoTime();

		GraphNode a = new GraphNode("a");
		GraphNode b = new GraphNode("b");
		GraphNode c = new GraphNode("c");
		GraphNode d = new GraphNode("d");
		GraphNode e = new GraphNode("e");
		
		a.addNeighbor(b, 1, 20);
		a.addNeighbor(d, 1, 50);
		a.addNeighbor(c, 1, 60);
		
		b.addNeighbor(a, 1, 20);
		b.addNeighbor(e, 1, 10);
		b.addNeighbor(c, 1, 30);
		
		c.addNeighbor(b, 1, 30);
		c.addNeighbor(d, 1, 10);
		c.addNeighbor(a, 1, 60);
		c.addNeighbor(e, 1, 40);

		
		e.addNeighbor(b, 1, 10);
		e.addNeighbor(d, 1, 10);
		e.addNeighbor(c, 1, 40);
		
		d.addNeighbor(a, 1, 30);
		d.addNeighbor(e, 1, 10);
		d.addNeighbor(c, 1, 10);
		
		Graph g = new Graph();
		g.addNode(a);
		g.addNode(b);
		g.addNode(c);
		g.addNode(d);
		g.addNode(e);
		
		long creationTime = System.nanoTime();
		System.out.println("Creation graph time: " + (creationTime - startTime));
		
		System.out.println("" + minCostTPath(g, a, c));
		long dTime = System.nanoTime();
		System.out.println("Dijkstra Time: " + (dTime - creationTime));

		System.out.println("" + minCostTPathHeap(g, a, c));
		long dHeapTime = System.nanoTime();
		System.out.println("Dijkstra Heap Time: " + (dHeapTime - dTime));
		
		System.out.println("" + floydWarshall(g, a, c));
		long fTime = System.nanoTime();
		System.out.println("Floyd-Warshall Time: " + (fTime - dHeapTime));

	}
	
	/*
	 * Dijkstra Implementation
	 */
	public static List<GraphNode> minCostTPath (Graph g, GraphNode start, GraphNode end){
		Queue<GraphNode> queue = new LinkedList<GraphNode>();
		
		GraphNode u;
		for(GraphNode n: g.nodes){
			n.parent = null;
			n.distance = Integer.MAX_VALUE;
			n.visited = 0;
		}
		queue.add(start);
		start.visited = 1;
		start.distance = 0;
		
		while(!queue.isEmpty()){
			u = queue.poll();
			if(u!= null){
				for(int i = 0; i< u.number; i++){
					if(u.neighbors[i].visited != 2){
						if(u.neighbors[i].distance > u.distance + u.costTime[i]){
							u.neighbors[i].distance = u.distance + u.costTime[i];
							u.neighbors[i].parent = u;
						}
						//if(u.neighbors[i] == end) return u.neighbors[i].distancia;
						if(u.neighbors[i].visited == 0)
							queue.add(u.neighbors[i]);
						u.neighbors[i].visited = 1;
					}
				}
				u.visited = 2;
			}
		}
		return backtrackPath(end, start);
	}
	
	/*
	 * Dijkstra Implementation using a Heap.
	 */
	public static List<GraphNode> minCostTPathHeap (Graph g, GraphNode start, GraphNode end){
		Queue<GraphNode> pqueue = new PriorityQueue<GraphNode>(20, new Comparator<GraphNode>(){
	        @Override
	        public int compare(GraphNode a, GraphNode b) {
	            if (a.distance < b.distance)
	                return -1;
	            else if (a.distance > b.distance)
	                return 1;
	            return 0;
	        }
	      });		
		GraphNode u;
		for(GraphNode n: g.nodes){
			n.parent = null;
			n.distance = Integer.MAX_VALUE;
			n.visited = 0;
		}
		pqueue.add(start);
		start.visited = 1;
		start.distance = 0;
		
		while(!pqueue.isEmpty()){
			u = pqueue.poll();
			if(u!= null){
				for(int i = 0; i< u.number; i++){
					if(u.neighbors[i].visited != 2){
						if(u.neighbors[i].distance > u.distance + u.costTime[i]){
							u.neighbors[i].distance = u.distance + u.costTime[i];
							u.neighbors[i].parent = u;
						}
						//if(u.neighbors[i] == end) return u.neighbors[i].distancia;
						if(u.neighbors[i].visited == 0)
							pqueue.add(u.neighbors[i]);
						u.neighbors[i].visited = 1;
					}
				}
				u.visited = 2;
			}
		}
		return backtrackPath(end, start);
	}
	
	public static List<GraphNode> floydWarshall(Graph g, GraphNode start, GraphNode end){
		int distTo[][] = new int[g.numberNodes][g.numberNodes];
		GraphNode edgeTo[][] = new GraphNode[g.numberNodes][g.numberNodes];
		
		// dist to infinite
		for(int i = 0; i < g.numberNodes; i++){
			for(int j = 0; j < g.numberNodes; j++){
				distTo[i][j] = Integer.MAX_VALUE;
			}
		}
		
		// dist using edge-weighted 
		for(GraphNode node : g.nodes){
			for(GraphNode neighbor: node.neighbors){
				if(node!= null && neighbor != null){
				distTo[node.id][neighbor.id] = node.distanceTo(neighbor);
				edgeTo[node.id][neighbor.id] = node;
				}
			}			
		}
		
        // Floyd-Warshall updates
		for(int i = 0; i < g.numberNodes; i++){
			for(int j = 0; j < g.numberNodes; j++){
				if(edgeTo[j][i] != null){
				for(int k =0; k<g.numberNodes; k++){
					if(distTo[j][k] > distTo[j][i] + distTo[i][j]){
						distTo[j][k] = distTo[j][i] + distTo[i][k];
                        edgeTo[j][k] = edgeTo[i][k];
					}
				}
				}
			}
		}
		return backtrackPathF(end, start, edgeTo);
	}
	
	/*
	 * For Floyd-Warshall
	 */
	public static List<GraphNode> backtrackPathF(GraphNode end, GraphNode start, GraphNode [][] edgeTo){
		List<GraphNode> list = new LinkedList<GraphNode>();
		if(end.equals(start)) return list;
		
		list.add(end);
		list.addAll(backtrackPath(edgeTo[end.id][start.id], start));
		return list;
	}
	
	/*
	 * For Dijkstra methods
	 */
	public static List<GraphNode> backtrackPath(GraphNode end, GraphNode start){
		List<GraphNode> list = new LinkedList<GraphNode>();
		if(end.equals(start)) return list;
		
		list.add(end);
		list.addAll(backtrackPath(end.parent, start));
		return list;
	}
}
