# Alpha Beta Pruning
Alpha Beta pruning is a minmix method that uses the idea of keeping count and control of a min value and max value to find the best move or set of possible moves over a certain number of steps. This algorithm only works well with 2 player games. This is because there can only be to states in numbers (positive and negative). For one player the more negative the move is the better it is said to be. Or the other player the more positive the move it the better its said to be. A good example of where something like this can be seen is the chess evaluation bar on chess.com (I am not saying this is what they use, I am saying it is possible that the alpha beta pruning can be shown in a similar manner). 

### How it works?
![alt text](image.png)

The image above is a graph that would generally be used to store the state of the board
The scores for a move would be stored on the last layer in the tree. in this case it is a depth of 2 which has 4 nodes. All other nodes dont have any values

We initially set the min and max value to -infinity and infinity respectivly.
We traverse the tree in a depth first search manner moving down the tree rather than horizontally. 
In the case for the Queens of Amazons Game there are 2 teams, black and white. 
For this breif demonstration I will consider white the starting piece at depth of 0 and with trying to maximise the number, positive values favoured.

When going through a depth first search you would check at every node if beta (min) is <= alpha(max)
In the case where it is you prune off a section reducing the nodes to be checked.
A further nte is that each node should have their own alpha and beta value.

The algorithm works best when sorted such that depending on whether you are minimizing or maximizing they are ordered such that the smallest or largest value is always on the right. This severly improves the algorthm and cuts the calculations done drasticly.

where b is the branching factor (number of possible moves per state)
d is the depth of tree
The worst case time complexity is O(b^d)
The best case time complexity is O(b^(d/2))
This is because it rdered correctly, approximately half the subtree is ignored for its depth

---


a good sorted tree shuld have a result like below
![](image-1.png) 
the above image is from the site https://pjdelta.wordpress.com/2015/06/20/computer-chess-and-alpha-beta-pruning/ 

---


A good video for understanding is https://www.youtube.com/watch?v=l-hh51ncgDI



