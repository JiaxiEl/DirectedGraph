/*
@author Jiangqi Su, cssc0845
        Jiaxi Chen cssc0870
 */

package edu.sdsu.cs.datastructures;

import java.io.*;
import java.util.*;
import java.lang.*;

public class App {
    public static void main(String[] args) throws IOException {
        String fileName = "layout.csv";
        if (args.length != 0)
            fileName = args[args.length - 1];
        File file = new File(fileName);
        IGraph<String> testGraph = new DirectedGraph();

        if (!file.exists()) {
            System.out.println("Error: Unable to open filename. Verify the file exists, is " +
                    "accessible, and meets the syntax requirements.");
            System.exit(1);
        }

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String readline = "";
        while ((readline = reader.readLine()) != null) {
            String temp[] = readline.split(",");
            if (!testGraph.contains(temp[0])) {
                testGraph.add(temp[0]);
            } else if (temp.length > 1) {
                testGraph.connect(temp[0], temp[1]);
            }
        }

        System.out.println(testGraph.toString());
        System.out.println("Enter the start:");
        Scanner str = new Scanner(System.in);
        StringBuilder startVertex = new StringBuilder();
        startVertex.append(str.nextLine());

        System.out.println("Enter the destination :");
        str = new Scanner(System.in);
        StringBuilder endVertex = new StringBuilder();
        endVertex.append(str.nextLine());
        List<String> graphPath = testGraph.shortestPath(startVertex.toString(), endVertex.toString());
        System.out.println("The best path from " + startVertex + " to " + endVertex + " is " + graphPath);
    }
}


