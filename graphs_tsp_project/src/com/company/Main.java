package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.System.exit;

public class Main {
    private static int[][]  graph;
    public static void main(String[] args) {

        // If the user does not give a filepath for the graph
        if (args.length != 1){
            System.out.println(".txt file not specified");
            exit(1);
        }

        // Reads the adj matrix of the graph from the file given in main
        try {
            readGraphFromFile(args[0]);
        }
        catch (Exception e ){
            System.out.println("Error reading from " + args[0] + " file");
            System.out.println(e.getMessage());
            exit(1);
        }


        Stack<Integer> bestPath;
        long start;
        long end;
        float elapsedTimeInSec;

        System.out.println("Approximation algorithm (Using DFS): ");
        // finding the time before the operation is executed
        start = System.currentTimeMillis();
        // Finds the shortest path cost with the Approximation algorithm (Using DFS)
        SubOptimalDfsTSP subOptimalDfsTSP = new SubOptimalDfsTSP(graph);
        System.out.println("Path cost: " + subOptimalDfsTSP.getShortestPathCost());
        bestPath = subOptimalDfsTSP.getShortestPath();

        System.out.print("Path Taken: " + bestPath.pop());
        while(!bestPath.isEmpty())
            System.out.print("-> " + bestPath.pop() );

        System.out.println();

        // finding the time after the operation is executed
        end = System.currentTimeMillis();
        //finding the time difference and converting it into seconds
        elapsedTimeInSec = (end - start) / 1000F; System.out.println("Time taken: " + elapsedTimeInSec + " seconds");
        System.out.println();
        subOptimalDfsTSP = null;

        System.out.println("Branch and bound algorithm: ");
        // finding the time before the operation is executed
        start = System.currentTimeMillis();
        // Finds the shortest path cost with the Brunch and bound algorithm
        BranchBoundTSP branchBoundTSP = new BranchBoundTSP(graph);
        try {
            System.out.println("Path cost: " + branchBoundTSP.getShortestPathCost());
            bestPath = branchBoundTSP.getShortestPath();

            System.out.print("Path Taken: " + bestPath.pop());
            while (!bestPath.isEmpty())
                System.out.print("-> " + bestPath.pop());

            System.out.println();
        // finding the time after the operation is executed
        end = System.currentTimeMillis();
        //finding the time difference and converting it into seconds
        elapsedTimeInSec = (end - start) / 1000F; System.out.println("Time taken: " + elapsedTimeInSec + " seconds");
        System.out.println();
        }catch (OutOfMemoryError error){
            branchBoundTSP = null;
            System.out.println("Error: Max heap size reached");
        }

        System.out.println("Brute force: ");
        // finding the time before the operation is executed
        start = System.currentTimeMillis();
        // Finds the shortest path cost with the brute force
        BruteForceTSP bruteForceTSP = new BruteForceTSP(graph);
        try{
        System.out.println("Path cost: " + bruteForceTSP.getShortestPathCost());
        bestPath = bruteForceTSP.getShortestPath();

        System.out.print("Path Taken: " + bestPath.pop());
        while(!bestPath.isEmpty())
            System.out.print("-> " + bestPath.pop() );

        System.out.println();

        // finding the time after the operation is executed
        end = System.currentTimeMillis();
        //finding the time difference and converting it into seconds
        elapsedTimeInSec = (end - start) / 1000F; System.out.println(elapsedTimeInSec + " seconds");
        System.out.println();
        }catch (OutOfMemoryError error){
            bruteForceTSP = null;
            System.out.println("Error: Max heap size reached");
        }

    }

    // Reading the graph's adj matrix from the given .txt file
    private static void readGraphFromFile(String fileName) throws FileNotFoundException {
        // Used to get the size of the array
        Scanner scanner = new Scanner(new File(fileName));
        String firstLine = scanner.nextLine();
        String[] data = firstLine.split(" "); // *First number on first line of the file must not have a "space"*
        int n = data.length;
        scanner.close();

        // Copy the graph to the graph array
        scanner = new Scanner(new File(fileName));
        graph = new int[n][n];
        for(int r=0; r < n; r++)
        {
            for(int c=0; c < n; c++)
                graph[r][c] = scanner.nextInt();
        }
    }
}
