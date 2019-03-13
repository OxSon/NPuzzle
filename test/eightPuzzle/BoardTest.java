package eightPuzzle;

import algs4.dequerandqueue.RandomizedQueue;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void hammingTest() {
        var board = new Board(new int[][]{
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}
        });
        //TODO test this more thoroughly
        assertEquals(board.hamming(), 5);

        board = new Board(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });
        assertEquals(board.hamming(), 0);
    }

    @Test
    void manhattanTest() {
        //TODO test this more thoroughly
        var board = new Board(new int[][]{
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}
        });
        assertEquals(board.manhattan(), 10);
    }

    @Test
    void isGoalTest() {
        //TODO are there more comparison cases that should be tested?
        // seems fairly simple but am i missing something?
        Board goal = new Board(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });
        Board notGoal = new Board(new int[][]{
                {1, 0, 3},
                {6, 8, 4},
                {2, 5, 7}
        });

        assertTrue(goal.isGoal());
        assertFalse(notGoal.isGoal());
    }

    @Test
    void isSolvableTest() {
        var board = new Board(new int[][] {
                {1,2,3},
                {4,5,6},
                {7,8,0}
        });
        assertTrue(board.isSolvable());
        board = new Board(new int[][] {
                {8,6,7},
                {2,5,4},
                {3,1,0}
        });
        assertTrue(board.isSolvable());

        //FIXME why does these next two data sets take so much time?
        board = new Board(new int[][]{
                {1, 2, 3, 4},
                {5, 0, 6, 8},
                {9, 10, 7, 11},
                {13, 14, 15, 12},
        });
        assertTrue(board.isSolvable());

        board = new Board(new int[][]{
                {1, 2, 3, 4},
                {5, 0, 6, 8},
                {9, 10, 7, 11},
                {13, 14, 12, 15},
        });
        assertFalse(board.isSolvable());

        board = new Board(new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {8, 7, 0}
        });
        assertFalse(board.isSolvable());

        board = new Board(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });
        assertTrue(board.isSolvable());

        board = new Board(new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 15, 14, 0},
        });
        assertFalse(board.isSolvable());
    }

    @Test
    void isSolvableRandomInputTest() {
        for(int i = 0; i < 100; i++) {
            //random dimension board
            int dimensions = StdRandom.uniform(100) + 2; //only do 2x2 or greater
            int range = dimensions * dimensions;
            int[][] tiles = new int[dimensions][dimensions];
            //use randomized queue so that board gets filled in random order
            var q = new RandomizedQueue<Integer>();
            for(int j = 0; j < range; j++) {
                q.enqueue(j);
            }
            //fill up tiles
            for(int j = 0; j < dimensions; j++) {
                for(int k = 0; k < dimensions; k++) {
                    tiles[j][k] = q.dequeue();
                }
            }

            var board = new Board(tiles);

            boolean linearMethod = board.isSolvable();
            boolean linearithmicMethod = board.isSolvableMergeSortMethod();
            assertEquals(linearithmicMethod, linearMethod);
        }
    }

    @Test
    void equalsTest() {
        //TODO are there more comparison cases that should be tested?
        // seems fairly simple but am i missing something?
        Board goal = new Board(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });
        Board notGoal = new Board(new int[][]{
                {1, 0, 3},
                {4, 8, 6},
                {7, 5, 2}
        });
        Board alsoGoal = new Board(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });

        assertEquals(goal, alsoGoal);
        assertNotEquals(goal, notGoal);
        assertNotEquals(alsoGoal, notGoal);
    }

    @Test
    void neightorsTestAll() {
        //fIXME note all tests are with size = 3 except middle,
        //and are with similar permutations of board
//        var neighborsTests = new Supplier<Void>[] {
//
//        };
    }

    @Test
    void neighborsTestRightEdgeNoCorner() {
        var board = new Board(new int[][]{
                {8, 1, 3},
                {4, 2, 0},
                {7, 6, 5},
        });

        var expectedNeighbors = new Board[]{
                new Board(new int[][]{
                        {8, 1, 3},
                        {4, 0, 2},
                        {7, 6, 5},
                }),
                new Board(new int[][]{
                        {8, 1, 0},
                        {4, 2, 3},
                        {7, 6, 5},
                }),
                new Board(new int[][]{
                        {8, 1, 3},
                        {4, 2, 5},
                        {7, 6, 0},
                }),
        };

        Iterable<Board> result = board.neighbors();
        for(var neighbor : result) {
            assertTrue( neighbor.equals(expectedNeighbors[0]) ||
                        neighbor.equals(expectedNeighbors[1]) ||
                    neighbor.equals(expectedNeighbors[2])
            );
        }
    }

    @Test
    void neighborsTestRightEdgeTopCorner() {
        var board = new Board(new int[][]{
                {8, 1, 0},
                {4, 2, 3},
                {7, 6, 5},
        });

        var expectedNeighbors = new Board[] {
                new Board(new int[][]{
                        {8, 1, 3},
                        {4, 2, 0},
                        {7, 6, 5}
                }),
                new Board(new int[][]{
                        {8, 0, 1},
                        {4, 2, 3},
                        {7, 6, 5}
                })
        };

        Iterable<Board> result = board.neighbors();
        for(var neighbor : result) {
            assertTrue( neighbor.equals(expectedNeighbors[0]) ||
                    neighbor.equals(expectedNeighbors[1])
            );
        }
    }

    @Test
    void neighborsTestRightEdgeBotCorner() {
        var board = new Board(new int[][]{
                {8, 1, 3},
                {4, 2, 5},
                {7, 6, 0},
        });

        fail();
    }

    @Test
    void neighborsTestTopEdgeNoCorner() {
        var board = new Board(new int[][]{
                {8, 0, 3},
                {4, 2, 1},
                {7, 6, 5},
        });

        fail();
    }

    @Test
    void neighborsTestTopEdgeRightCorner() {
        var board = new Board(new int[][]{
                {8, 3, 0},
                {4, 2, 1},
                {7, 6, 5},
        });

        fail();
    }

    @Test
    void neighborsTestTopEdgeLeftCorner() {
        var board = new Board(new int[][]{
                {0, 8, 3},
                {4, 2, 1},
                {7, 6, 5},
        });

        fail();
    }

    @Test
    void neighborsTestLeftEdgeNoCorner() {
        var board = new Board(new int[][]{
                {8, 4, 3},
                {0, 2, 1},
                {7, 6, 5},
        });

        fail();
    }

    @Test
    void neighborsTestLeftEdgeTopCorner() {
        var board = new Board(new int[][]{
                {0, 8, 3},
                {4, 2, 1},
                {7, 6, 5},
        });

        fail();
    }

    @Test
    void neighborsTestLeftEdgeBotCorner() {
        var board = new Board(new int[][]{
                {8, 7, 3},
                {4, 2, 1},
                {0, 6, 5},
        });

        fail();
    }

    @Test
    void neighborsTestBotEdgeNoCorner() {
        var board = new Board(new int[][]{
                {8, 6, 3},
                {4, 2, 1},
                {7, 0, 5},
        });

        fail();
    }

    @Test
    void neighborsTestBotEdgeRightCorner() {
        var board = new Board(new int[][]{
                {8, 5, 3},
                {4, 2, 1},
                {7, 6, 0},
        });

        fail();
    }

    @Test
    void neighborsTestBotEdgeLeftCorner() {
        var board = new Board(new int[][]{
                {8, 7, 3},
                {4, 2, 1},
                {0, 6, 5},
        });

        fail();
    }

    @Test
    void neighborsTestMiddle() {
        var board = new Board(new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 0, 10, 11},
                {12, 13, 14, 15},
        });

        var expectedNeighbors = new Board[] {
                new Board(new int[][] {
                        {1, 2, 3, 4},
                        {5, 0, 7, 8},
                        {9, 6, 10, 11},
                        {12, 13, 14, 15},
                }),
                new Board(new int[][] {
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 0, 11},
                        {12, 13, 14, 15},
                }),
                new Board(new int[][] {
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 13, 10, 11},
                        {12, 0, 14, 15},
                }),
                new Board(new int[][] {
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {0, 9, 10, 11},
                        {12, 13, 14, 15},
                })
        };

        Iterable<Board> result = board.neighbors();
        for(var neighbor : result) {
            assertTrue( neighbor.equals(expectedNeighbors[0]) ||
                    neighbor.equals(expectedNeighbors[1]) ||
                    neighbor.equals(expectedNeighbors[2]) ||
                    neighbor.equals(expectedNeighbors[3])
            );
        }
    }

    @Test
    void toStringTest() {
        Board test = new Board(new int[][]{
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}

        });

        String expected =
                "3\n 0  1  2 \n 3  4  5 \n 6  7  8 \n";
        assertEquals(expected, test.toString());
    }
}