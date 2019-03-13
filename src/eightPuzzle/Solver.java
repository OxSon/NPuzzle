package eightPuzzle;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

/**
 * Solves a given 8-Puzzle board, if solvable.
 *
 * @author Alec Mills
 * @author Chau Pham
 */
public class Solver {
    private MinPQ<SearchNode> pq = new MinPQ<>();
    private Board initial;
    private Stack<Board> solution = new Stack<>();

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

        this.initial = initial;
        //TODO
        solve();
    }

    //constructs our solution Iterable
    private void solve() {
        var head = new SearchNode(initial);
        pq.insert(head);

        do {
            head = pq.delMin();
            for(var neighbor : head.board.neighbors()) {
                //critical optimization
                if (!neighbor.equals(head.previous))
                    pq.insert(new SearchNode(head, neighbor));
            }
        } while (!head.board.isGoal());

        while(head.previous != null) {
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

    //FIXME below represents an idea that was dropped partially through, may be
    // worked on again, involves using an enum to represent moves and thereby
    // saving on memory costs. Seems like it may require reimplementing Board
    // in its' entirety though. Could also maybe represent a board internally
    // to the solver class as a permutation, i.e. cycles?
//    private Stack<Board> solve(Board initial) {
//        //FIXME this is broken, we need search node class. board doesn't store previous.
//        // options are described below. In general, what we should do is:
//        // chain from end goal board back to beginning using the 'previous' field, or by reconstructing with move enum,
//        // and add them to a stack as we go, and then add all those boards to the solution queue (thereby correcting the order)
//        var head = new SearchNode();
//        do {
//            head = pq.delMin();
//
//            for (var neighbor : head.board.neighbors()) {
//                if (!neighbor.equals(head.board))
//                    pq.insert(new SearchNode(neighbor));
//            }
//        } while (!head.board.isGoal());
//
//        //can't return chain of previous right now.
//        //do we want to make searchnode class that stores reference to previous,
//        //or do we want to imitate my previous solution, i.e. make a private enum that tracks moves and
//        //reconstruct board from that? will memory savings be worth the confusion?
//        return null;
//    }
//
//
//    //Represents a move on the n-puzzle board, allows us to represent
//    //different boards with minimal memory storage rather than holding
//    //a reference to each explored possible board state
//    private enum Move {
//        Up, Down, Left, Right;
//    }
//
//    //represents one possible board state derived from an original board state
//    private static class SearchNode {
//        //Board layout associated with this search node
//        final Board board;
//        //moves it took to get here
//        final ArrayList<Move> moves;
//        final int priority;
//
//        public SearchNode(SearchNode previous, Move move) {
//            board = makeMove(previous.board, move);
//
//            moves = new ArrayList<>(previous.moves);
//            moves.add(move);
//
//            //manhattan function only
//            priority = board.manhattan() + moves.size();
//            //manhattan function plus inversions
////            priority = board.manhattan() + moves.size() + countInversions(board);
//        }
//
//        private Iterable<SearchNode> childNodes() {
//
//        }
//    }
//
//    private int countInversions(Board board) {
//        //TODO supposedly this is a better heuristic by an order of magnitude
//        //but would require us to count inversions, so make a copy of board's tiles
//        return 0;
//    }
//
//    private static Board makeMove(Board board, Move move) {
//        //TODO return the board that would result from making the move specified
//        // on the board specified. Maybe do some error checking and throw an exception
//        // if it's not a valid move.
//        return null;
//    }

    /**
     * The minimum number of moves to solve initial board.
     * @return the minimum number of moves.
     */
    public int moves() {
        return solution.size();
    }

    /**
     * Calculates a solution to the initial board state.
     * @return a sequence of boards in a shortest solution.
     */
    public Iterable<Board> solution() {
        return solution;
    }

    /**
     * Program entry-point-- solves a slider puzzle as a demonstration.
     *
     * @author Algs4 R. Sedgewick & K. Wayne
     * @param args unused.
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
