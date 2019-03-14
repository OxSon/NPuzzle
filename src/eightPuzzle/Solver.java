package eightPuzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Solves a given 8-Puzzle board, if solvable.
 *
 * @author Alec Mills
 * @author Chau Pham
 */
public class Solver {
    //private Board initial;
    private final Stack<Board> solution = new Stack<>();

    /**
     * Finds a solution to the initial board given (using the A* algorithm).
     *
     * @param initial board state at game begin
     */
    public Solver(Board initial) {
        //corner cases
        if (initial == null)
            throw new NullPointerException("Cannot solve null board");
        if (!initial.isSolvable())
            throw new IllegalArgumentException("Board is not solvable");

        solve(initial);
    }

    //constructs our solution Iterable
    private void solve(Board initial) {
        var pq = new MinPQ<SearchNode>();
        var head = new SearchNode(initial);
        pq.insert(head);

        do {
            head = pq.delMin();
            for (Board neighbor : head.board.neighbors()) {
                //Don't insert boards that we've visited recently, and skip this check if we're still at the initial board
                if (head.previous == null || !neighbor.equals(head.previous.board))
                    pq.insert(new SearchNode(head, neighbor));
            }
        } while (!head.board.isGoal());

        while (head != null) {
            solution.push(head.board);
            head = head.previous;
        }
    }

    private static class SearchNode implements Comparable<SearchNode> {
        final SearchNode previous;
        final Board board;
        final int moves;
        final int priority;

        //constructor for first node only, i.e. no moves have been made
        SearchNode(Board initial) {
            previous = null;
            moves = 0;
            board = initial;
            priority = initial.manhattan();
        }

        //generic constructor
        SearchNode(SearchNode previous, Board current) {
            this.previous = previous;
            moves = previous.moves + 1;
            board = current;
            priority = current.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(priority, other.priority);
        }
    }

    /**
     * The minimum number of moves to solve initial board.
     *
     * @return the minimum number of moves.
     */
    public int moves() {
        return solution.size() - 1;
    }

    /**
     * Calculates a solution to the initial board state.
     *
     * @return a sequence of boards in a shortest solution.
     */
    public Iterable<Board> solution() {
        return solution;
    }

    /**
     * Program entry-point-- solves a slider puzzle as a demonstration.
     *
     * @param args unused.
     * @author Algs4 R. Sedgewick & K. Wayne
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In();
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
