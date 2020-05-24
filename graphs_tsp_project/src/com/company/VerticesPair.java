package com.company;

// Used to save the vertices pairs for a vertex
class VerticesPair {

    private int[] verticesIds; // Contains the ids of the vertices
    private int[] costsToReachVertices; // Contains the costs to reach the vertices

    VerticesPair(){}

    // Adds the given vertex to the pair set and removes the current farthest
    void replaceVertex(int vertexId, int costToReachVertex, int prev){

        if(containsVertex(vertexId))
            return;

        int worstVertexId = 0;
        int costToReachWorstVertex = -1 * Integer.MAX_VALUE;
        for(int i = 0; i < verticesIds.length; i++)
        {
            if (prev != verticesIds[i] && costToReachWorstVertex <= costsToReachVertices[i])
            {
                worstVertexId = i;
                costToReachWorstVertex = costsToReachVertices[i];
            }
        }
        verticesIds[worstVertexId] = vertexId;
        costsToReachVertices[worstVertexId] = costToReachVertex;
    }

    private boolean containsVertex(int vertexId) {
        for (int verticesId : verticesIds) {
            if (vertexId == verticesId)
                return true;
        }
        return false;
    }

    void setVerticesIds(int[] verticesIds) {
        this.verticesIds = new int[verticesIds.length];
        System.arraycopy(verticesIds, 0, this.verticesIds, 0, this.verticesIds.length);
    }

    void setCostsToReachVertices(int[] costsToReachVertices) {
        this.costsToReachVertices = new int[costsToReachVertices.length];
        System.arraycopy(costsToReachVertices, 0, this.costsToReachVertices, 0, this.costsToReachVertices.length);
    }

    int[] getVerticesIds() {
        return verticesIds;
    }

    int[] getCostsToReachVertices() {
        return costsToReachVertices;
    }
}
