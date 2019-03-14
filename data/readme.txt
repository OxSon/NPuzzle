/******************************************************************************
 *  Name: Alec
 *  Partner Name: Chau
 *
 *  Hours to complete assignment (optional): not sure
 *
 ******************************************************************************/

Programming Assignment 4: Slider Puzzle


/******************************************************************************
 *  Explain briefly how you implemented the board data type.
 *****************************************************************************/

We used a 1d array to save memory, but conceptually still thought about the board
as a 2d array, and wrote private functions to convert from 1d to 2d indices, and back.

We also cached the size of the board, the goal board (calculated during construction)
and the current row & index of the blank tile. These cached values worsened our memory
requirements but eased calculations elsewhere and made our code more readable. We felt
this was a worthy trade-off. All fields were marked 'final'.


/******************************************************************************
 *  Explain briefly how you represented a search node
 *  (board + number of moves + previous search node).
 *****************************************************************************/

We used a private static class with the following final fields:
-the previous SearchNode
-the current board
-the priority (calculated on construction)
-the number of moves






/******************************************************************************
 *  Explain briefly how you detected unsolvable puzzles. What is the
 *  order of growth of the runtime of your isSolvable() method?
 *****************************************************************************/

Description:

Conceptually, we treated any given board configuration as a permutation on the total ordering
of the numbers [1, N^2-1]. We then modeled that permutation as a composition of cycles,
and took advantage of the fact that the parity of number of inversions in a permutation
is the same as the product of the parities of the cycles when written as such. We disregarded
the placement of the 'zero' or blank tile.

Pragmatically, we copied our board array into a new array, filtering out the zero. We then made
the following variables to keep track of:
 + the first number in the current cycle
 + the number we are currently processing in the current cycle
 + the total number of elements we have processed prior to this cycle (break when this is equal to N^2 - 1)
 + an index counter, started at zero and incremented when finishing a cycle, or when encountering
   an already visited node
 + the number of elements involved in the current cycle (an odd number -> an even cycle, and vice versa)
 + the number of odd-parity cycles we've encountered so far

From there, starting with the element at index 0 of our permutation, we looked to the index that element
occupies in our goal configuration (e.g. '3' should be at index '2'), if it is different than the index it
occupies in our permutation. If it is not, iterate 'i' and move on. If it is, then iterate our variable
corresponding to the total number of elements in our current cycle, and repeat the process on the element contained
in the goal index of our current element. In both cases we mark that index as visited by adding N^2 to it (because
before being visited, no element will be >= N^2).

When we reach the end of our current cycle, iterate 'i' and move on, changing our 'initial element
of current cycle' element as appropriate, and note the number of elements processed during this cycle.
In between cycles, we may encounter elements that have already been visited during a previous cycle.
If that is the case, we simply iterate 'i' and move on. We break once we have processed N^2 -1 elements.

An example of running through one cycle's worth of a permutation:

Beginning values of vars:
'initial element of cycle': [8]
'current element of cycle': [8]
'i':                        [0]
'processed'                 [0]
'current cycle sum'         [1]
'odd cycles'                [0]

Goal: [1, 2, 3, 4, 5, 6, 7, 8]
P:    [8, 3, 2, 4, 5, 7, 6, 1]

+ 8's goal index is not its' current index, it should be 7, which is currently occupied by 1. This is not our initial element.
  * vars changed in this step: 'current element' -> 1
+ Our 'current' is not equal to our 'init', add one to our current cycle sum and keep going.
 * 'current cycle sum' -> 2
+ 1's goal index is not its' current index, it should be 0, which is currently occupied by 8. This is our initial element.
  * 'current element' -> 8
+ Our 'current' is equal to our 'init', if 'current cycle sum' is an even number, add one to 'odd cycles'.
    Also, iterate i, add 'current cycle sum' to processed, set 'current cycle sum' back to 1.
+ 'i' -> 1, 'odd cycles' -> 1, 'processed' -> 2, ''current cycle sum' -> 1
+ element at index 'i' is 3, and has not been visited. Set this to our init.
  * 'init' -> 3
+ repeat above process


Order of growth of running time:
Because no element can be a member of more than one cycle, each element is touched either one or two times. Therefore
our runtime in tilde notation is somewhere between N and 2N. However, it is not possible to reach an upper bound of 2N.
We are unsure what the actual upper bound would be in tilde, but we can be certain that the order of growth is O(N).



/******************************************************************************
 *  For each of the following instances, give the minimal number of 
 *  moves to reach the goal state. Also give the amount of time
 *  it takes the A* heuristic with the Hamming and Manhattan
 *  priority functions to find the solution. If it can't find the
 *  solution in a reasonable amount of time (say, 5 minutes) or memory,
 *  indicate that instead. Note that you may be able to solve
 *  puzzlexx.txt even if you can't solve puzzleyy.txt even if xx > yy.
 *****************************************************************************/


                  number of          seconds
     instance       moves      Hamming     Manhattan
   ------------  ----------   ----------   ----------
   puzzle28.txt     28          0.64            0.03
   puzzle30.txt     30          1.26            0.07
   puzzle32.txt     32          FAIL            0.34
   puzzle34.txt     34          FAIL            0.20
   puzzle36.txt     36          FAIL            2.05
   puzzle38.txt     38          FAIL            1.40
   puzzle40.txt     40          FAIL            0.37
   puzzle42.txt     42          FAIL            1.27

   With manhattan function we were also able to do all puzzleXX.txt,
   up to puzzle48.txt besides puzzle47.txt (noted below)

   Longest successful runtime:
   puzzle4x4-48.txt 48                          4.44

/******************************************************************************
 *  If you wanted to solve random 4-by-4 or 5-by-5 puzzles, which
 *  would you prefer:  a faster computer (say, 2x as fast), more memory
 *  (say 2x as much), a better priority queue (say, 2x as fast),
 *  or a better priority function (say, one on the order of improvement
 *  from Hamming to Manhattan)? Why?
 *****************************************************************************/

A better priority function. A better priority function results in effectively
pruning many search paths, as those paths never get explored before the solution
is reached. Another way to say it, is that for every node that doesn't get enqueued,
everyone of it's potential children in that sub-tree also doesn't get enqueued,
so a better priority function should be an exponential improvement in both time
and space considerations.





/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/
Runs out of memory solving very hard puzzles.



/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/

Alec -I talked to Rich in the STEM center about group-theory, and the concept
of writing a permutation as a composition of cycles, and determining their parity,
and applied these concepts in the design of the isSolvable algorithm.

Chau - none.



/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/



/******************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/

We followed the protocol, due to our schedules not matching up,
we each wrote complete  working versions of the assignment, and then
merged them together into one, taking the best of both parts.






/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/

 Alec -most fun assignment so far.

 Chau- none.
