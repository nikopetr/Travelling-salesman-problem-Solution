package com.company;

import java.util.ArrayList;

// Represents a node (state), which contains information of the path taken for this state
class PathNode implements Comparable<PathNode>{
    private int currentVertexId; // The if of the vertex this path has last visited
    private double lb; // Representing the lower bound of this state
    private boolean[] visitedVertices; // Representing which vertices have been visited so far
    private int totalVisitedVertices; // The number of the visited vertices
    private int pathTotalCost; // The total cost of the path taken so far
    private ArrayList<VerticesPair> verticesPairs; // Used to calculate the lower bound, contains the pairs with the edges for each vertex
    private PathNode previousPathNode; // Pointer to the previous path node

    // Overridden compareTo method, in order to compare two paths first by their lower bound in the priority queue used
    @Override
    public int compareTo(PathNode p2) {
        int lbComparison = Double.compare(this.getLb(),p2.getLb());
        if (lbComparison != 0)
            return lbComparison;
        else
        return -1;
    }

    // Constructor for the initial path node
    PathNode(int currentVertexId, ArrayList<VerticesPair> closestPairs)
    {
        verticesPairs = closestPairs;
        visitedVertices = new boolean[closestPairs.size()];
        visitedVertices[currentVertexId] = true;
        totalVisitedVertices = 1;
        previousPathNode = null;
        pathTotalCost = 0;
        lb = calculateLowerBound();
    }

    // Constructor used for each path  node
    PathNode(int vertexToVisit, int costToReachVertex, PathNode parentPathNode) {

        this.previousPathNode = parentPathNode;

        // Copying the previous path taken so far
        this.verticesPairs = new ArrayList<>();
        for (VerticesPair verticesPair : parentPathNode.getVerticesPairs())
        {
            VerticesPair copyVerticesPair = new VerticesPair();
            copyVerticesPair.setVerticesIds(verticesPair.getVerticesIds());
            copyVerticesPair.setCostsToReachVertices(verticesPair.getCostsToReachVertices());
            verticesPairs.add(copyVerticesPair);
        }

        // Sets the vertex which is going to be visited
        this.currentVertexId = vertexToVisit;

        // Copying the previous visited vertices so far and sets the current as visited
        visitedVertices = new boolean[parentPathNode.getVisitedVertices().length];
        for (int i = 0; i < visitedVertices.length; i++)
            visitedVertices[i] = parentPathNode.getVisitedVertices()[i];
        visitedVertices[currentVertexId] = true;


        // Replacing the vertices pairs with the edges of the two vertices used
        if(parentPathNode.getPreviousPathNode() != null)
            verticesPairs.get(parentPathNode.getCurrentVertexId()).replaceVertex(currentVertexId,costToReachVertex,parentPathNode.getPreviousPathNode().getCurrentVertexId());
        else
            verticesPairs.get(parentPathNode.getCurrentVertexId()).replaceVertex(currentVertexId,costToReachVertex,-1);
        verticesPairs.get(currentVertexId).replaceVertex(parentPathNode.getCurrentVertexId(),costToReachVertex,parentPathNode.getCurrentVertexId());

        totalVisitedVertices = parentPathNode.getTotalVisitedVertices() + 1;
        pathTotalCost = parentPathNode.getPathTotalCost() + costToReachVertex;
        lb = calculateLowerBound();
    }

    // Calculates and returns the lower bound for this path
    private double calculateLowerBound(){
        double sum = 0 ;
        for (VerticesPair verticesPair : verticesPairs)
            sum += verticesPair.getCostsToReachVertices()[0] + verticesPair.getCostsToReachVertices()[1];
        return sum/2;
    }

    int getCurrentVertexId() {
        return currentVertexId;
    }

    double getLb() {
        return lb;
    }

    PathNode getPreviousPathNode() {
        return previousPathNode;
    }

    boolean[] getVisitedVertices() {
        return visitedVertices;
    }

    int getPathTotalCost() {
        return pathTotalCost;
    }

    private ArrayList<VerticesPair> getVerticesPairs() {
        return verticesPairs;
    }

    int getTotalVisitedVertices(){
        return totalVisitedVertices;
    }
}
