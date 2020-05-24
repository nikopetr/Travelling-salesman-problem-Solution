package com.company;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

// Finds  the shortest path for the TSP, using the branch and bound method, using the total sum of
// the two closest edges for each vertex and then dividing with 2
class BranchBoundTSP {
    private int n; // Representing the total vertices of the graph
    private int [][] graph; // Representing the adjacency matrix of the graph
    private double shortestPathCost; // Representing the total cost of the path taken
    private PathNode shortestPathNode; // The path with the last visited vertex before reaching the root again

    private PriorityQueue<PathNode> priorityQueue; // Priority queue used as a Min Heap for the path states

    BranchBoundTSP(int[][] graph)
    {
        this.priorityQueue = new PriorityQueue<>();
        this.n = graph.length;
        this.graph = graph;
        this.shortestPathCost = Integer.MAX_VALUE;
        this.shortestPathNode = null;
    }

    // Initializing the pairs of each vertex with the two closest edges
    private ArrayList<VerticesPair> initializeClosestPairs()
    {
        ArrayList<VerticesPair> closestPairs =  new ArrayList<>();
        for (int i = 0; i < n; i++) {
            VerticesPair closestPair = new VerticesPair();
            int closestVertexA = -1 ; int closestVertexB = -1;
            int costVertexA = Integer.MAX_VALUE;
            int costVertexB = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++)
            {
                if (j == i)
                    continue;

                if (graph[i][j] < costVertexA)
                {
                    closestVertexB = closestVertexA;
                    costVertexB = costVertexA;
                    closestVertexA = j;
                    costVertexA = graph[i][j];
                }
                else if (graph[i][j] < costVertexB)
                {
                    closestVertexB = j;
                    costVertexB = graph[i][j];
                }
            }
            closestPair.setVerticesIds(new int[]{closestVertexA, closestVertexB});
            closestPair.setCostsToReachVertices(new int[]{costVertexA, costVertexB});
            closestPairs.add(closestPair);
        }

        return closestPairs;
    }

    // Finds the shortest path using the branch and bound method and returns an integer representing the shortest's path cost
    double getShortestPathCost() {
        priorityQueue.add(new PathNode(0, initializeClosestPairs()));
        branchAndBound();
        return shortestPathCost;
    }

    // Method to find the shortest path using the branch and bound method, using the total sum of
    // the two closest edges for each vertex and then dividing with 2
    private void branchAndBound()
    {
        while(priorityQueue.size() > 0)
        {
            // Pick the path with the lowest lower bound
            PathNode currentPathNode = priorityQueue.poll();
            //  If no path with lower bound smaller than the current best exists, returning with the current path found
            if (currentPathNode.getLb() >= shortestPathCost)
                return;

            // If last vertex is reached and the path taken is shorter than the current one taken then
            // marking this path as the solution and save it's cost
            if (currentPathNode.getTotalVisitedVertices() == n && graph[0][currentPathNode.getCurrentVertexId()]> 0 && currentPathNode.getPathTotalCost() + graph[0][currentPathNode.getCurrentVertexId()] < shortestPathCost )
            {
                shortestPathCost = currentPathNode.getPathTotalCost() + graph[0][currentPathNode.getCurrentVertexId()];
                shortestPathNode = currentPathNode;
            }

            // Loop to traverse the adjacency list of the current vertex and increasing the visited vertices,
            // adding to the min heap the next path node if is not already visited
            for (int i = 0; i < n; i++)
            {
                if (!currentPathNode.getVisitedVertices()[i] && graph[i][currentPathNode.getCurrentVertexId()]> 0)
                {
                    PathNode pathNode = new PathNode(i, graph[i][currentPathNode.getCurrentVertexId()], currentPathNode);
                    if (pathNode.getPathTotalCost() < shortestPathCost)
                        priorityQueue.add(pathNode);
                }

            }
        }
    }

    // Returns a Stack of integers representing the path of the best shortest route to take
    Stack<Integer> getShortestPath() {

        Stack<Integer> shortestPath = new Stack<>();
        shortestPath.push(0);

        PathNode currentPathNode  = shortestPathNode;

        while(currentPathNode != null)
        {
            shortestPath.push(currentPathNode.getCurrentVertexId());
            currentPathNode = currentPathNode.getPreviousPathNode();
        }

        return shortestPath;
    }
}
