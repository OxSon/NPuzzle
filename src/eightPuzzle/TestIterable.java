package eightPuzzle;

import edu.princeton.cs.algs4.*;

public class TestIterable {
    public static void main(final String[] args) {
        Solver solver = new Solver(new Board(new int[][] {
            {0, 1, 3},
            {4, 2, 5},
            {7, 8, 6}
        }));

        var solution = solver.solution();

        for(var b : solution) {
            System.out.println(b);
        }
    }
}
