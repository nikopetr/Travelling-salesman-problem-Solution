package com.company;

/*
 * An implementation of the traveling salesman problem in Java using dynamic 
 * programming to improve the time complexity from O(n!) to O(n^2 * 2^n).
 *
 * Time Complexity: O(n^2 * 2^n)
 * Space Complexity: O(n * 2^n), since we use an integer[n][2^n] array to store the data
 *
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class DynamicProgrammingTSP {

    private int n; // Representing the total vertices of the graph
    private int root; // Representing the starting vertex of the graph
    private int endState; // Representing the state of the graph which has
    private int[][] graph; // Representing the adjacency matrix of the graph
    private List<Integer> shortestPath; // List containing the ids of the vertices that forms the shortest path for the TSP problem
    private int shortestPathCost; // Representing the total cost of the path taken

    // Using a single 32-bit integer to represent the different visited vertices
    // for better caching in a memo array by saving 4 bytes of memory for each state.
    // For example in a graph with four vertices, a state with value 9 means that the currently visited vertices are
    // 0 and 3, since 9 = (1001)in binary.
    // That way, by using 32-bit integer represantation of state, we can easily get the combinations
     DynamicProgrammingTSP(int[][] graph) {
        this.n = graph.length;
        // Signed left shift in order to get the value of (2^n - 1), which represents the end state that includes all the vertices
        this.endState = (1 << n) - 1;
        this.root = 0; // Using vertex 0 as the root (starting point)
        this.graph = graph;

        this.shortestPathCost = Integer.MAX_VALUE;
        this.shortestPath = new ArrayList<>();
    }

    // Returns the optimal tour for the traveling salesman problem.
     List<Integer> getShortestPath() {
        return shortestPath;
    }

    // Solves the traveling salesman problem and caches solution.
    // Returns the minimal tour cost.
     double getShortestPathCost()
     {
         // Signed left shift in order to get the value of 2^n, array is same as Integer[n][2^n]
         Integer[][] memory = new Integer[n][1 << n];

         // Initializing memory array, adding all the outgoing edges from the starting vertex to memory array
         int leftShiftedRoot = 1 << root; // 2^0=1
         for (int i = 0; i < n; i++)
             memory[i][leftShiftedRoot | (1 << i)] = graph[root][i];

         for (int r = 3; r <= n; r++) {
             for (int subset : combinations(r, n))
             {
                 if (notIn(root, subset)) continue;
                 for (int next = 0; next < n; next++) {
                     if (next == root || notIn(next, subset)) continue;
                     int subsetWithoutNext = subset ^ (1 << next);
                     int minDist = Integer.MAX_VALUE;
                     for (int end = 0; end < n; end++) {
                         if (end == root || end == next || notIn(end, subset)) continue;
                         int newDistance = memory[end][subsetWithoutNext] + graph[end][next];
                         if (newDistance < minDist) {
                             minDist = newDistance;
                         }
                     }
                     memory[next][subset] = minDist;
                 }
             }
         }

         // Connect tour back to starting node and minimize cost.
         for (int i = 0; i < n; i++) {
             if (i == root) continue;
             int tourCost = memory[i][endState] + graph[i][root];
             if (tourCost < shortestPathCost) {
                 shortestPathCost = tourCost;
             }
         }

         int lastIndex = root;
         int state = endState;
         shortestPath.add(root);

         // Reconstruct TSP path from memory table.
         for (int i = 1; i < n; i++) {

             int index = -1;
             for (int j = 0; j < n; j++) {
                 if (j == root || notIn(j, state)) continue;
                 if (index == -1) index = j;
                 double prevDist = memory[index][state] + graph[index][lastIndex];
                 double newDist  = memory[j][state] + graph[j][lastIndex];
                 if (newDist < prevDist) {
                     index = j;
                 }
             }

             shortestPath.add(index);
             state = state ^ (1 << index);
             lastIndex = index;
         }

         shortestPath.add(root);
         Collections.reverse(shortestPath);
         return shortestPathCost;
    }

    private static boolean notIn(int elem, int subset) {
        return ((1 << elem) & subset) == 0;
    }

    // This method generates all bit sets of size n where r bits
    // are set to one. The result is returned as a list of integer masks.
    private static ArrayList<Integer> combinations(int r, int n) {
        ArrayList<Integer> subsets = new ArrayList<>();
        combinations(0, 0, r, n, subsets);
        return subsets;
    }

    // To find all the combinations of size r we need to recurse until we have
    // selected r elements (aka r = 0), otherwise if r != 0 then we still need to select
    // an element which is found after the position of our last selected element
    private static void combinations(int set, int at, int r, int n, ArrayList<Integer> subsets) {

        // Return early if there are more elements left to select than what is available.
        int elementsNotPicked = n - at;
        if (elementsNotPicked < r) return;

        // We selected 'r' elements so we found a valid subset!
        if (r == 0) {
            subsets.add(set);
        } else {
            for (int i = at; i < n; i++) {
                // Try including this element
                set |= 1 << i;

                combinations(set, i + 1, r - 1, n, subsets);

                // Backtrack and try the instance where we did not include this element
                set &= ~(1 << i);
            }
        }
    }
}