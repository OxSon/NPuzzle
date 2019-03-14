package a04;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public final class Board {
	
	private final int[] blocks;
	private final int size;
	
	/**
	 * Constructs a board from an N-by-N array of blocks
	 * (where blocks[i][j] = block in row i, column j)
	 * @param blocks
	 */
	public Board(int[][] blocks) {
		int n = blocks.length;
		this.blocks = new int[n * n];
		int row = 0;
		int col = 0;
		for (int i = 0; i < this.blocks.length; i++) {
			if (col == n) {
				row++;
				col = 0;
			}
			this.blocks[i] = blocks[row][col];
			col++;
		}
		size = n;
	}
	
	/**
	 * Constructs a board from an N array of blocks
	 * @param blocks
	 */
	private Board(int[] blocks) {
		int n = blocks.length;
		this.blocks = new int[n];
		for (int i = 0; i < blocks.length; i++) {
			this.blocks[i] = blocks[i];
		}
		size = (int) Math.sqrt(n);
	}
	
	/**
	 * Returns the size of the board
	 * @return
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns the number of blocks out of place
	 * @return
	 */
	public int hamming() {
		int hamming = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == 0) continue;
			if (blocks[i] - 1 != i) hamming++;
		}
		return hamming;
	}
	
	/**
	 * Returns the sum of Manhattan distances between blocks and goal
	 * @return
	 */
	public int manhattan() {
		int manhattan = 0;
		int temp = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == 0) continue;
			if (blocks[i] - 1 != i) {
				int rowStep = Math.abs((blocks[i] - 1) / size - i / size);
				int colStep = Math.abs((blocks[i] - 1) % size - i % size);
				temp = rowStep + colStep;			
				manhattan += temp;
			}
		}
		return manhattan;
	}
	
	/**
	 * Checks if this board is the goal board
	 * @return
	 */
	public boolean isGoal() {
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] - 1 != i && blocks[i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if this board is solvable.
	 * If the board size N is an odd integer, then each legal move changes 
	 * the number of inversions by an even number. Thus, if a board has an odd number of inversions,
	 * then it cannot lead to the goal board by a sequence of legal moves
	 * because the goal board has an even number of inversions. If the board size N is an even integer,
	 * then the parity of the number of inversions is not invariant.
	 * However, the parity of the number of inversions plus the row of the blank square is invariant:
	 * each legal move changes this sum by an even number. If this sum is even,
	 * then it cannot lead to the goal board by a sequence of legal moves.
	 * @return
	 */
	public boolean isSolvable() {
		int numberOfInversions = 0;
		for (int i = 1; i < blocks.length; i++) {
			for (int j = i - 1; j >= 0; j--) {
				if (blocks[i] < blocks[j] && blocks[i] != 0 && blocks[j] != 0) numberOfInversions++;
			}
		}
		if (size % 2 != 0 && numberOfInversions % 2 !=0) return false;
		if (size % 2 == 0) {
			int rowOfBlank = getIndexOfBlank() / size;
			int sum = rowOfBlank + numberOfInversions;
			if (sum % 2 == 0) return false;
		}
		return true;
	}
	
	/**
	 * Check if this board equals y
	 * @return
	 */
	public boolean equals(Object y) {
		if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        for (int i = 0; i < this.blocks.length; i++) {
        	if (this.blocks[i] != that.blocks[i]) {
        		return false;
        	}
        }
        return true;
	}
	
	/**
	 * Returns all neighboring boards
	 * @return
	 */
	public Iterable<Board> neighbors() {
		Queue<Board> neighbors = new Queue<>();
		int indexOfBlank = getIndexOfBlank();
		if (indexOfBlank < size) {
			if (indexOfBlank == 0) {
				swap(neighbors, indexOfBlank, indexOfBlank + 1);				
				swap(neighbors, indexOfBlank, indexOfBlank + size);
			} else if (indexOfBlank == size - 1) {
				swap(neighbors, indexOfBlank, indexOfBlank - 1);
				swap(neighbors, indexOfBlank, indexOfBlank + size);
			} else {
				swap(neighbors, indexOfBlank, indexOfBlank + 1);				
				swap(neighbors, indexOfBlank, indexOfBlank + size);
				swap(neighbors, indexOfBlank, indexOfBlank - 1);
			}
		} else if (indexOfBlank > blocks.length - size - 1) {
			if (indexOfBlank == blocks.length - 1) {
				swap(neighbors, indexOfBlank, indexOfBlank - 1);				
				swap(neighbors, indexOfBlank, indexOfBlank - size);
			} else if (indexOfBlank == blocks.length - size) {
				swap(neighbors, indexOfBlank, indexOfBlank + 1);				
				swap(neighbors, indexOfBlank, indexOfBlank - size);
			} else {
				swap(neighbors, indexOfBlank, indexOfBlank + 1);				
				swap(neighbors, indexOfBlank, indexOfBlank - size);
				swap(neighbors, indexOfBlank, indexOfBlank - 1);
			}
		} else if (indexOfBlank % size == 0) {
			swap(neighbors, indexOfBlank, indexOfBlank + 1);				
			swap(neighbors, indexOfBlank, indexOfBlank + size);
			swap(neighbors, indexOfBlank, indexOfBlank - size);
		} else if ((indexOfBlank + 1) % size == 0) {
			swap(neighbors, indexOfBlank, indexOfBlank - 1);				
			swap(neighbors, indexOfBlank, indexOfBlank + size);
			swap(neighbors, indexOfBlank, indexOfBlank - size);
		} else {
			swap(neighbors, indexOfBlank, indexOfBlank + 1);
			swap(neighbors, indexOfBlank, indexOfBlank - 1);
			swap(neighbors, indexOfBlank, indexOfBlank + size);
			swap(neighbors, indexOfBlank, indexOfBlank - size);
		}
		return neighbors;
	}
	
	/**
	 * Swaps elements' positions and enqueue to the queue
	 * @param queue
	 * @param current
	 * @param position
	 */
	private void swap(Queue<Board> queue, int current, int position) {
		int[] newBlocks = this.blocks.clone();
		int temp = newBlocks[current];
		newBlocks[current] = newBlocks[position];
		newBlocks[position] = temp;
		Board newBoard = new Board(newBlocks);
		queue.enqueue(newBoard);
	}
	
	/**
	 * String representation of this board (in the output format specified below)
	 */
	public String toString() {
		String show = size + "\n";
	    for (int i = 0; i < blocks.length; i++) {
	    	if (i % size == 0 && i > 0) {
	    		show += "\n";
	    	}
	    	show += String.format("%2d ", blocks[i]);
	    }
	    return show;
	}
	
	/**
	 * Returns the index of 0
	 * @return
	 */
	private int getIndexOfBlank() {
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == 0) {
				return i;
			}	
		}
		return -1;
	}
	
	/**
	 * Unit Tests
	 * @param args
	 */
	public static void main(String[] args) {
		int[][] blocks1 = { {8, 1, 3}, {4, 0, 2}, {7, 6, 5} };
		Board board1 = new Board(blocks1);
		int[][] blocks2 = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
		Board board2 = new Board(blocks2);
		
		StdOut.println(board1);
		StdOut.println("Hamming#: " + board1.hamming());
		StdOut.println("Manhattan#: " + board1.manhattan());
		StdOut.println("Is it the board goal? " + board1.isGoal());
		StdOut.println("Is it solvable? " + board1.isSolvable());
		StdOut.println();
		
		StdOut.println(board2);
		StdOut.println("Is it the board goal? " + board2.isGoal());
		StdOut.println("Hamming#: " + board2.hamming());
		StdOut.println("Manhattan#: " + board2.manhattan());
		StdOut.println();
		
		StdOut.println("Testing isSolvable()");
		Board[] boards1 = new Board[5];
		int[][] blocks6 = { {1, 2, 3}, {4, 5, 6}, {8, 7, 0} };
		int[][] blocks7 = { {1, 2, 3}, {4, 5, 6}, {8, 0, 7} };
		int[][] blocks8 = { {1, 2, 3}, {4, 0, 6}, {8, 5, 7} };
		int[][] blocks9 = { {1, 2, 3}, {0, 4, 6}, {8, 5, 7} };
		int[][] blocks10 = { {1, 2, 3}, {4, 6, 7}, {8, 5, 0} };
		boards1[0] = new Board(blocks6);
		boards1[1] = new Board(blocks7);
		boards1[2] = new Board(blocks8);
		boards1[3] = new Board(blocks9);
		boards1[4] = new Board(blocks10);
		StdOut.println(boards1[0]);
		StdOut.println("Is it solvable? " + boards1[0].isSolvable());
		StdOut.println("Hamming#: " + boards1[0].hamming());
		StdOut.println("Manhattan#: " + boards1[0].manhattan());
		StdOut.println(boards1[1]);
		StdOut.println("Is it solvable? " + boards1[1].isSolvable());
		StdOut.println("Hamming#: " + boards1[1].hamming());
		StdOut.println("Manhattan#: " + boards1[1].manhattan());
		StdOut.println(boards1[2]);
		StdOut.println("Is it solvable? " + boards1[2].isSolvable());
		StdOut.println("Hamming#: " + boards1[2].hamming());
		StdOut.println("Manhattan#: " + boards1[2].manhattan());
		StdOut.println(boards1[3]);
		StdOut.println("Is it solvable? " + boards1[3].isSolvable());
		StdOut.println("Hamming#: " + boards1[3].hamming());
		StdOut.println("Manhattan#: " + boards1[3].manhattan());
		StdOut.println(boards1[4]);
		StdOut.println("Is it solvable? " + boards1[4].isSolvable());
		StdOut.println("Hamming#: " + boards1[4].hamming());
		StdOut.println("Manhattan#: " + boards1[4].manhattan());
		
		int[][] blocks3 = { {0, 1, 3}, {4, 2, 5}, {7, 8, 6} };
		Board board3 = new Board(blocks3);
		StdOut.println(board3);
		StdOut.println("Is it solvable? " + board3.isSolvable());
		StdOut.println("Hamming#: " + board3.hamming());
		StdOut.println("Manhattan#: " + board3.manhattan());
		
		int[][] blocks4 = { {1, 2, 3, 4}, {5, 0, 6, 8}, {9, 10, 7, 11}, {13, 14, 15, 12} };
		Board board4 = new Board(blocks4);
		StdOut.println(board4);
		StdOut.println("Is it solvable? " + board4.isSolvable());
		StdOut.println("Hamming#: " + board4.hamming());
		StdOut.println("Manhattan#: " + board4.manhattan());
		
		int[][] blocks5 = { {1, 2, 3, 4}, {5, 0, 6, 8}, {9, 10, 7, 11}, {13, 14, 12, 15} };
		Board board5 = new Board(blocks5);
		StdOut.println(board5);
		StdOut.println("Is it solvable? " + board5.isSolvable());
		StdOut.println("Hamming#: " + board5.hamming());
		StdOut.println("Manhattan#: " + board5.manhattan());
		StdOut.println();
		
		StdOut.println("Testing equal()");
		StdOut.println(board1);
		StdOut.println(board2);
		StdOut.println("Are the above boards equal? " + board1.equals(board2));
		int[][] blocks11 = { {8, 1, 3}, {4, 0, 2}, {7, 6, 5} };
		Board board6 = new Board(blocks11);
		StdOut.println(board1);
		StdOut.println(board6);
		StdOut.println("Hamming#: " + board6.hamming());
		StdOut.println("Manhattan#: " + board6.manhattan());
		StdOut.println("Are the above boards equal? " + board1.equals(board6));
		StdOut.println();
		
		StdOut.println("Testing neighbors()");
		StdOut.println("Original:");
		StdOut.println(board1);
		StdOut.println("Neighbors:");
		for (Board b: board1.neighbors()) {
			StdOut.println(b);
		}
		StdOut.println("Original:");
		StdOut.println(board2);
		StdOut.println("Neighbors:");
		for (Board b: board2.neighbors()) {
			StdOut.println(b);
		}
		StdOut.println("Original:");
		StdOut.println(board3);
		StdOut.println("Neighbors:");
		for (Board b: board3.neighbors()) {
			StdOut.println(b);
		}
		int[][] blocks12 = { {1, 2, 4}, {0, 5, 3}, {7, 8, 6} };
		Board board7 = new Board(blocks12);
		StdOut.println("Original:");
		StdOut.println(board7);
		StdOut.println("Hamming#: " + board7.hamming());
		StdOut.println("Manhattan#: " + board7.manhattan());
		StdOut.println("Neighbors:");
		for (Board b: board7.neighbors()) {
			StdOut.println(b);
			StdOut.println("Hamming#: " + b.hamming());
			StdOut.println("Manhattan#: " + b.manhattan());
		}
		/*
		int[][] blocks1 = { {1, 2, 3}, {4, 6, 7}, {8, 5, 0} };
		Board board1 = new Board(blocks1);
		StdOut.println(board1);
		StdOut.println("Hamming#: " + board1.hamming());
		StdOut.println("Manhattan#: " + board1.manhattan());
		*/
	}
}
