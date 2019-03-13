package a04;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {
	
	private Stack<Board> solutions;
	
	/**
	 * Finds a solution to the initial board (using the A* algorithm)
	 * @param initial
	 */
	public Solver(Board initial) {
		if (!initial.isSolvable()) throw new IllegalArgumentException("The board is not solvable.");
		if (initial == null) throw new NullPointerException("Null value is not accepted.");
		
		MinPQ<SearchNode> pq = new MinPQ<>();
		solutions = new Stack<>();
		
		SearchNode firstNode = new SearchNode(initial, 0, null);
		pq.insert(firstNode);
		
		while (true) {
			firstNode = pq.delMin();
			if (firstNode.board.isGoal()) break;
			
			for (Board neighbor : firstNode.board.neighbors()) {
				if(firstNode.previous == null
					|| (firstNode.previous != null && !neighbor.equals(firstNode.previous.board))) {
					pq.insert(new SearchNode(neighbor, firstNode.moveNum + 1, firstNode));
				} 
			}
		}
		
		while(firstNode != null) {
            solutions.push(firstNode.board);
            firstNode = firstNode.previous;
        }
	}
	
	/**
	 * Returns the minimum number of moves to solve initial board
	 * @return
	 */
    public int moves() {
    	return solutions.size() - 1;
    }
    
    /**
     * Returns the sequence of boards in a shortest solution
     * @return
     */
    public Iterable<Board> solution() {
    	return solutions;
    }
    
    /**
     * A search node of the game includes a board, the number of moves made to reach the board,
     * and the previous search node. First, insert the initial search node
     * (the initial board, 0 moves, and a null previous search node) into a priority queue.
     * Then, delete from the priority queue the search node with the minimum priority,
     * and insert onto the priority queue all neighboring search nodes
     * (those that can be reached in one move from the dequeued search node).
     * Repeat this procedure until the search node dequeued corresponds to a goal board.
     * The success of this approach hinges on the choice of priority function for a search node.
     * @author Bao Chau Pham
     *
     */
    public class SearchNode implements Comparable<SearchNode> {
    	
    	Board board;
    	int moveNum;
    	int priority;
    	SearchNode previous;
    	
    	public SearchNode(Board board, int moveNum, SearchNode previous) {
    		this.board = board;
    		this.moveNum = moveNum;
    		this.priority = this.moveNum + this.board.manhattan();
    		this.previous = previous;
    	}

		@Override
		public int compareTo(SearchNode other) {
			return this.priority - other.priority;
		}
    }
    
    /**
     * Solves a slider puzzle
     * @param args
     */
    public static void main(String[] args) {
    	
    	// create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // check if puzzle is solvable; if so, solve it and output solution
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

        // if not, report unsolvable
        else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}