package com.company;

import java.util.Stack;

// TSP sub optimal method using DFS heuristic
class SubOptimalDfsTSP {
    private int n; // Representing the total vertices of the graph
    private int [][] graph; // Representing the adjacency matrix of the graph
    private boolean[] visitedVertices; // Representing which vertices have been currently visited
    private int shortestPathCost; // Representing the total cost of the path taken
    private Vertex lastVertex; // The last visited vertex before reaching the root again (Contains pointers with the path taken)

    SubOptimalDfsTSP(int[][] graph)
    {
        this.n = graph.length;
        this.graph = graph;
        this.visitedVertices = new boolean[n];
        this.shortestPathCost = Integer.MAX_VALUE;
        this.lastVertex = null;

        // Mark first vertex as visited since we use it as the root
        visitedVertices[0] = true;
    }

    // Returns an integer representing the shortest's path cost
    int getShortestPathCost()
    {
        dfs(new Vertex(0,null,0),  1);
        return shortestPathCost;
    }

    // Method to find the shortest path, searching in depth, adding the closest vertex each time
    // (assuming there is always a route from one vertex to another since we have to do with a complete graph)
    private void dfs(Vertex currentVertex, int verticesVisited)
    {
        // If the path contains all vertices return the with the path found
        if (verticesVisited == n && graph[currentVertex.getId()][0] > 0)
        {
            shortestPathCost = currentVertex.getCostToReach() + graph[currentVertex.getId()][0];
            lastVertex = currentVertex;
            return;
        }

        int closestVertexToReach = -1; // The id of the next closest vertex
        int costToReachClosestVertex = Integer.MAX_VALUE; // The cost to reach the closest vertex

        // Loop to traverse the adjacency list of the current vertex, moving to the next closest vertex and increasing the new vertex cost by the edge value
        // and increasing the visited vertices by 1
        for (int i = 0; i < n; i++)
        {
            if (!visitedVertices[i] && graph[currentVertex.getId()][i] > 0)
            {
                Vertex neighbourVertex = new Vertex(i, currentVertex, currentVertex.getCostToReach() + graph[currentVertex.getId()][i]);
                // If the neighbourVertex is closer than the previous closest found, mark it as the closest
                if (neighbourVertex.getCostToReach() < costToReachClosestVertex)
                {
                    closestVertexToReach = i;
                    costToReachClosestVertex = neighbourVertex.getCostToReach();
                }
            }
        }

        // Mark the closest to reach vertex as visited and recurse
        Vertex nextVertex = new Vertex(closestVertexToReach, currentVertex, currentVertex.getCostToReach() + graph[currentVertex.getId()][closestVertexToReach]);
        visitedVertices[closestVertexToReach] = true;
        dfs(nextVertex, verticesVisited + 1);
    }

    // Returns a Stack of integers representing the path of the best shortest route to take
    Stack<Integer> getShortestPath() {

        Stack<Integer> shortestPath = new Stack<>();
        shortestPath.push(0);

        Vertex currentVertex  = lastVertex;

        while(currentVertex != null)
        {
            shortestPath.push(currentVertex.getId());
            currentVertex = currentVertex.getPrev();
        }

        return shortestPath;
    }
}
