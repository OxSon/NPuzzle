package eightPuzzle;

import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;

/**
 * Represents an 8-Puzzle board.
 * <p>
 * All methods take time proportional to N^2 or better, where N is the length of one side of the board.
 * Or: time proportional to M where M is total number of elements, i.e. N^2
 *
 * @author Alec Mills
 * @author Chau Pham
 */
public class Board {
    private final int[] boardFlat;
    private final int size;
    private final int[] goal;
    private int blankTileRow;
    private int blankTileCol;

    /**
     * Construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j).
     * Valid integers are between 0 and (N^2) - 1, where zero represents the empty space
     * <p>
     * Takes time proportional to N^2 where N = side-length of board,
     * i.e. time proportional to M where M = total number of elements.
     *
     * @param blocks initial layout of blocks.
     */
    public Board(int[][] blocks) {
        size = blocks.length;

        //calculate goal board for later reference
        goal = new int[size * size];
        //this step takes time proportional to M (N^2)
        for (int i = 0; i < goal.length - 1; i++) { //add numbers in range [1, size * size - 1]
            goal[i] = i + 1;
        }
        goal[goal.length - 1] = 0; //add the blank square in bottom right

        //transfer data to our internal representation
        boardFlat = new int[size * size];
        int k = 0;
        //this step takes time proportional to M (N^2)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardFlat[k++] = blocks[i][j];

                if (blocks[i][j] == 0) {
                    blankTileRow = i;
                    blankTileCol = j;
                }
            }
        }
    }

    /**
     * Board size N (i.e. a 3 by 3 board with 9 total spaces would be size 3).
     * <p>
     *
     * @return board size N.
     */
    public int size() {
        return size;
    }

    /**
     * Hamming priority heuristic; the number of blocks out of place.
     * <p>
     * Takes time proportional to N^2 where N = side-length of board,
     * i.e. time proportional to M where M = total number of elements.
     *
     * @return the number of blocks out of place.
     */
    public int hamming() {
        int count = 0;
        //we use boardFlat.length - 1 because we are not interested in the position of the blank tile
        for (int i = 0; i < boardFlat.length - 1; i++) {
            if (goal[i] != boardFlat[i])
                count++;
        }

        return count;
    }

    /**
     * Manhattan priority heuristic;
     * sum of vertical and horizontal distances between
     * blocks and their respective goal positions.
     * <p>
     * Takes time proportional to N^2 where N = side-length of board,
     * i.e. time proportional to M where M = total number of elements.
     *
     * @return sum of Manhattan distances between blocks and goal.
     */
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < boardFlat.length; i++) {
            sum += manhattanSingle(boardFlat[i], i);
        }

        return sum;
    }


    /**
     * Is this board the goal board?
     * <p>
     * Takes time proportional to N^2 where N = side-length of board,
     * i.e. time proportional to M where M = total number of elements,
     * in the worst case.
     *
     * @return true if this board is the goal board false otherwise.
     */
    public boolean isGoal() {
        //Should take time proportional to N^2 in the worst case as per java's Arrays.equals spec,
        //i.e. Arrays.equals takes linear time in the worst case,
        // i.e. time proportional to M, where M for Arrays.equals = N^2
        return Arrays.equals(boardFlat, goal);
    }

    /**
     * Is this board solvable?
     * <p>
     * Whether a board is solvable depends on two-to-three variables: the length of it's side-- N,
     * the number of inversions in the permutation, or more precisely, their parity,
     * and, if N is even, also the row-index of the blank tile, or again, its' parity.
     * <p>
     * Using this method, we are able to determine parity in
     * time proportional to M where M is the total number of elements, i.e. N^2
     *
     * @return true if board is solvable false otherwise.
     */
    public boolean isSolvable() {
        boolean evenParity = permutationParity();
        //odd board size, like classic 8 puzzle which is 3x3
        if (size % 2 != 0) {
            return evenParity; //in this case we need our parity to be even
        } else {
            //in the case of an even board size, we need parity to be odd.
            //the parity of a sum of numbers can only be odd in an
            //exclusive or situation, i.e.:
            //even + even = even, odd + odd = even
            //odd + even = odd
            boolean zeroParity = blankTileRow % 2 == 0;
            return zeroParity != evenParity;
        }
    }


    /**
     * All boards that can be reached in one legal move from this board
     * <p>
     * Takes time proportional to N
     *
     * @return all neighboring boards.
     */
    public Iterable<Board> neighbors() {
        //neighbors we've found so far
        var neighbors = new Queue<Board>();
        /*check the square to the left, right, above, and below, for a valid move and add the board that would
          result from that move if there is one */
        for (int i = blankTileRow - 1; i <= blankTileRow + 1; i++) {
            for (int j = blankTileCol - 1; j <= blankTileCol + 1; j++) {
                //dont go out of bounds
                boolean inBounds = (i >= 0 && i < size && j >= 0 && j < size);
                //we must still be on either blank tile row or blank tile column but not both
                if (inBounds && (i == blankTileRow || j == blankTileCol) && !(i == blankTileRow && j == blankTileCol)) {
                    int[][] newBoardState = new int[size][size];
                    //copy over our current board into a new array
                    for (int k = 0; k < size; k++) {
                        newBoardState[k] = Arrays.copyOfRange(boardFlat, (k * size), (k * size) + size);
                    }

                    //create the board corresponding to the move being made and add it to our queue
                    int tileToMove = (i * size) + j;
                    int temp = boardFlat[tileToMove];
                    newBoardState[i][j] = 0;
                    newBoardState[blankTileRow][blankTileCol] = temp;
                    neighbors.enqueue(new Board(newBoardState));
                }
            }
        }

        //Total time is apprx 9N, or O(N)
        return neighbors;
    }

    //Should take time proportional to N^2
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(size).append("\n");
        for (int i = 0; i < size; i++) { //outer loop runs N times
            for (int j = 0; j < size; j++) { //inner loop runs N times
                sb.append(String.format("%2d ", boardFlat[(i * size) + j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    //Should take time proportional to N^2 in the worst case as per java's Arrays.equals spec,
    //i.e. Arrays.equals takes linear time in the worst case,
    // i.e. time proportional to M, where M for Arrays.equals = N^2
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.equals(boardFlat, board.boardFlat);
    }

    //Should take time proportional to N^2 in the worst case as per java's Arrays.hashcode spec,
    //i.e. Arrays.hashcode takes linear time in the worst case,
    // i.e. time proportional to M, where M for Arrays.hashcode = N^2
    @Override
    public int hashCode() {
        return Arrays.hashCode(boardFlat);
    }

    private boolean permutationParity() {
        //choosing to copy the data into an aux array because it allows easy
        //marking of 'visited' elements, allows us to get rid of the zero in the beginning
        //instead of needing to do some clever calculation to figure out relative indices,
        //and conforms to expectation that board data type is immutable.
        //Another option would be to create an auxiliary bool array of length M,
        //mark zero as visited initially, and then do clever math on our indices
        //to translate to relative positions. This sounds more difficult to me, and
        //does not rid of us of the need to create another array. It does save time
        //as it gets rid of one N term for full eq of time complexity
        int[] p = new int[boardFlat.length - 1];
        int M = p.length;
        //time complexity: O(M), where M = N^2 - 1, where N = side-length of board, i.e. floor(sqrt(num elements))
        p = Arrays.stream(boardFlat).filter(x -> x != 0).toArray();

        //the last index we looked at as the potential beginning of a cycle
        int i = 0;
        int init = p[0]; //beginning of our current cycle
        int oddCycles = 0;
        int processed = 0; //number of elements we have processed
        int current = init; //current element we are processing
        int cycleSum = 1; //number of elements in our current cycle so far, starts at one to include init

        //if we've processed all but one element, we know the last element is fixed and thus
        //there are no more odd-cycles to find
        while (processed < M - 1) {

            //First, from our element, look to it's goal index and note that value
            // (note we add M to an element to mark it as visited, since no unvisited element will be > M)
            if (current <= M) {//if it's not visited
                int next = p[current - 1];
                p[current - 1] += M; //mark it as visited
                //If we haven't completed a cycle
                if (next != init) {
                    current = next; //move to process next element
                    cycleSum++; //note an additional element in our cycle
                } else {
                    //move to next element if there is one
                    init = p[++i];
                    current = init;
                    processed += cycleSum; //note how many elements were processed in this cycle
                    //An 'odd' cycle is defined as one with an odd number of transpositions,
                    //i.e. an even number of elements involved in the cycle
                    //e.g. a single transposition like: (13) is odd, a cycle consisting of
                    //two transpositions is even, like: (134)
                    oddCycles += (cycleSum % 2 == 0) ? 1 : 0;
                    cycleSum = 1; //reset cyclesum to 1 at the beginning of each new cycle
                }
            } else { //otherwise move to next element if there is one
                //additionally, this can only happen in between cycles, and thus cycleSum will never be even,
                //and thus oddCycles will never need to be iterated in this branch.
                init = p[++i];
                current = init;
            }
        }

        //total parity is the parity of the sum of each individual cycle's parity
        //put more simply, it is the product of each individual cycle's parity
        return oddCycles % 2 == 0;//index of next element to process (if not yet visited) in P, our permutation.
    }

    //calculates manhattan distance of a single tile using 1D index
    //Function is:
    //F(x, k) = manhattanSingle(x, i(k), j(k), where i(k) and j(k) return their respective 2D index for 1D index k
    //This is a constant time operation
    private int manhattanSingle(int x, int k) {
        //Function for going from k = 1D index to (i, j) = 2D indices:
        //F(k) = (k / N, k % N), where N = dimension of board
        return manhattanSingle(x, k / size, k % size);
    }

    //calculates manhattan distance of a single tile using 2D indices
    //Function is:
    //F(x, i, j) = |goalI - i| + |goalJ - j|, where (i, j) are current indices of x
    //
    //This is a constant time operation
    private int manhattanSingle(int x, int i, int j) {
        if (x == 0) //we are not interested in position of blank tile
            return 0;
        //int[] goal = [goalI, goalJ]
        int[] goal = goalIndices(x);

        //Recall our function: F(x, i, j) = |goalI - i| + |goalJ - j|
        return Math.abs(goal[0] - i) + Math.abs(goal[1] - j);
    }

    /*
     *  Helper function mapping a given number to it's goal (row, col) indices.
     *
     * Our function is;
     *  F(x) = [(x-1)/N, (x -1)%N], where N = dimensions of this board & '/' indicates integer division
     *  Domain: x | x is in range: [1, (N^2) - 1]
     *  Range: (i, j) -> ([0, N-1], [0, N-1])
     *  This is a constant time operation
     */
    private int[] goalIndices(int x) {
        //disallow inputs outside of our domain
        if (x < 1 || x > (size * size) - 1)
            throw new IllegalArgumentException("Input to goal indices is invalid");
        //Recall our function is: F(x) = [(x-1)/N, (x -1)%N]
        return new int[]{(x - 1) / size, (x - 1) % size};
    }
}
