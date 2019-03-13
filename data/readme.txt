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



Order of growth of running time:



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
   puzzle28.txt     28          //TODO          2.6
   puzzle30.txt     30          //TODO          5.63
   puzzle32.txt     Failed - Java.lang.OutOfMemoryError: Java heap space
   puzzle34.txt     Failed - Java.lang.OutOfMemoryError: Java heap space
   puzzle36.txt     Failed - Java.lang.OutOfMemoryError: Java heap space
   puzzle38.txt     Failed - Java.lang.OutOfMemoryError: Java heap space
   puzzle40.txt     Failed - Java.lang.OutOfMemoryError: Java heap space
   puzzle42.txt     Failed - Java.lang.OutOfMemoryError: Java heap space

   Was able to do:
   puzzle39.txt     39                          4.97
   puzzle41.txt     41                          0.94
   and
   puzzleXX.txt where XX < 30



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

We followed the protocol, due to our schedules not mtaching up,
we each wrote complete  working versions of the assignment, and then
merged them together into one, taking the best of both parts.






/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/

 Alec -most fun assignment so far.

 Chau- none.
