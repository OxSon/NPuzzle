package eightPuzzle;

/**
 * Test class for seeing if I have indeed found a method for determining parity of a permutation in linear time
 * <p>
 * first solving for specifically a 3x3, then will try to generalize
 */
public class PermutationParity {
    private final int[] permutation;
    private final int M;

    public PermutationParity(int[] p, int zeroIndex) {
        //choosing to copy the data into an aux array anyway, because it allows easy
        //marking of 'visited' elements, allows us to get rid of the zero in the beginning
        //instead of needing to do some clever calculation to figure out relative indices,
        //and conforms to expectation that board data type is immutable.
        //Another option would be to create an auxiliary bool array of length M,
        //mark zero as visited initially, and then do clever math on our indices
        //to translate to relative positions. This sounds more difficult to me, and
        //does not rid of us of the need to create another array. It does save time
        //as it gets rid of one N term for full eq of time complexity
        permutation = new int[p.length - 1];
        { //FIXME is this scoping necessary for i? this will all eventually be one method most likely, or maybe split into a couple privates before executing int he public method test this?
            int i = 0;
            while (i < p.length - 1) {
                if (p[i] != 0) {
                    permutation[i] = p[i];
                    i++;
                }
            }
        }
        M = permutation.length; //total number of elements in board excluding zero, i.e. N^2 - 1
    }

    public boolean evenParity() {
//        int i = 0; //index of next element to process (if not yet visited) in P, our permutation.
//        int init = permutation[0]; //beginning of our current cycle
//        //permutation[0] += M;
//        int oddCycles = 0;
//        int processed = 0; //number of elements we have processed
//        int current = init; //current element we are processing
//        int cycleSum = 1; //number of elements in our current cycle so far, starts at one to include init
//
//        while (processed < M) {
//            //First, from our initial element, look to it's goal index and note that value
//            //TODO in real implementation we will have read access to board always...
//            int next = permutation[current - 1]; //each numbered element's goal index is it's value - 1
//
//            //we add M to an element to mark it as visited, since no unvisited element will be > M
//            if (next <= M) {//if it's not visited
//                permutation[current - 1] += M;//mark it as visited
//                //If we haven't completed a cycle
//                if (next != init) {
//                    current = next; //move to process next element
//                    cycleSum++; //note an additional element in our cycle
//                    processed++;
//                } else {
//                    //move to next element if there is one
//                    if (i < M - 1) {
//                        init = permutation[++i];
//                        current = init;
//                    }
//                    processed += cycleSum; //note how many elements were processed in this cycle
//                    //An 'odd' cycle is defined as one with an odd number of transpositions,
//                    //i.e. an even number of elements involved in the cycle
//                    //e.g. a single transposition like: (13) is odd, a cycle consisting of
//                    //two transpositions is even, like: (134)
//                    oddCycles += (cycleSum % 2 == 0) ? 1 : 0;
//                    cycleSum = 1; //reset cyclesum to 1 at the beginning of each new cycle
//                }
//            } else if (i < M - 1) {//otherwise move to next element if there is one, don't iterate count.
//                //FIXME verify the below is true
//                //additionally, this can only happen in between cycles, and thus cycleSum will never be even,
//                //and thus oddCycles will never need to be iterated in this branch.
//                init = permutation[++i];
//                current = init;
//            }
//        }
//
//        //total parity is the parity of the sum of each individual cycle's parity
//        //put more simply, it is the product of each individual cycle's parity
//        return oddCycles % 2 == 0;
        return false;
    }

    public static void main(String[] args) {
        boolean succ;
        //these first two should work even with the zero issue because zero is at end of array and affects no other indices
        var parityChecker = new PermutationParity(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0}, 8);
        succ = parityChecker.evenParity(); //should succeed
        System.out.printf("Checking goal board: %s%n", succ ? "SUCCESS" : "FAILURE");

        parityChecker = new PermutationParity(new int[]{1, 2, 3, 4, 5, 6, 8, 7, 0}, 8);
        succ = !parityChecker.evenParity();//should fail
        System.out.printf("Checking trivial unsolvable board: %s%n", succ ? "SUCCESS" : "FAILURE");

        System.out.printf("Final result: %s", succ ? "SUCCESS" : "FAILURE");
    }
}
