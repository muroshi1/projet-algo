import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.jgrapht.util.*;

import java.util.*;
import java.util.function.*;

import java.io.IOException;

import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

import java.util.HashMap;

import static org.jgrapht.Graphs.*;

// https://jgrapht.org/guide/UserOverview#development-setup


public class Solver{
    public static Graph<String, DefaultEdge> implicationGraph;

    //
    public static String[] initArray() throws IOException {
        FileArrayProvider x = new FileArrayProvider();
        return x.readLines("./src/format.txt");
    }

    // Create implication graph
    public static void initGraph() throws IOException {
        String[] array = initArray();

        Supplier<String> vSupplier = new Supplier<String>()
        {
            private int id = 0;

            @Override
            public String get()
            {
                return "v" + id++;
            }
        };

        // Create the graph object
        Graph<String, DefaultEdge> graph =
                new DirectedPseudograph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(), false);

        // Add vertices and edges for each line (light bulb)
        for(String s : array){
            String[] light = s.split(" ");
            String constraintType = light[2].concat(light[3]).concat(light[4]).concat(light[5]);

            String X = "X" + light[0];      // X open
            String notX = "~X" + light[0];  // X closed
            String Y = "Y" + light[1];      // Y open
            String notY = "~Y" + light[1];  // Y closed


            // Add vertices
            if (graph.vertexSet().contains(X) == false){
                graph.addVertex(X);
            }
            if (graph.vertexSet().contains(notX) == false){
                graph.addVertex(notX);
            }
            if (graph.vertexSet().contains(Y) == false){
                graph.addVertex(Y);
            }
            if (graph.vertexSet().contains(notY) == false){
                graph.addVertex(notY);
            }

            // Add edges
            switch(constraintType){
                case Constraint.XOpenAndYOpen: // 1000, ~X -> X, ~Y -> Y
                    graph.addEdge(notX, X);
                    graph.addEdge(notY, Y);
                    break;
                case Constraint.XOpenAndYClosed: // 0100, ~X -> X, Y -> ~Y
                    graph.addEdge(notX, X);
                    graph.addEdge(Y, notY);
                    break;
                case Constraint.XClosedAndYOpen: // 0010, X -> ~X, ~Y -> Y
                    graph.addEdge(X, notX);
                    graph.addEdge(notY, Y);
                    break;
                case Constraint.XClosedAndYClosed: // 0001, X -> ~X, Y -> ~Y
                    graph.addEdge(X, notX);
                    graph.addEdge(Y, notY);
                    break;

                case Constraint.XOpen: // 1100, X -> X
                    graph.addEdge(X, X);
                    break;
                case Constraint.XClosed: // 0011, ~X -> ~X
                    graph.addEdge(notX, notX);
                    break;
                case Constraint.YOpen: // 1010, Y -> Y
                    graph.addEdge(Y, Y);
                    break;
                case Constraint.YClosed: // 0101, ~Y -> ~Y
                    graph.addEdge(notY, notY);
                    break;

                case Constraint.XOpenOrYOpen: // 1110, ~X -> Y, ~Y -> X
                    graph.addEdge(notX, Y);
                    graph.addEdge(notY, X);
                    break;
                case Constraint.XOpenOrYClosed: // 1101, ~X ->~Y, Y -> X
                    graph.addEdge(notX, notY);
                    graph.addEdge(Y, X);
                    break;
                case Constraint.XClosedOrYOpen: // 1011, ~Y -> ~X, X -> Y
                    graph.addEdge(notY, notX);
                    graph.addEdge(X, Y);
                    break;
                case Constraint.XClosedOrYClosed: // 0111, X -> ~Y, Y -> ~X
                    graph.addEdge(X, notY);
                    graph.addEdge(Y, notX);
                    break;

                case Constraint.Different: // 0110, ~X -> Y, ~Y -> X
                    graph.addEdge(notX, Y);
                    graph.addEdge(notY, X);
                    break;
            }

        }

        // Print out the graph for tests, delete later
        Iterator<String> iter = new DepthFirstIterator<>(graph);
        while (iter.hasNext()) {
            String vertex = iter.next();
            System.out.println(
                    "Vertex " + vertex + " is connected to: "
                            + graph.edgesOf(vertex).toString());
        }

        implicationGraph = graph;
    }

    // a 2-CNF formula is satisfiable if and only if there is no variable that belongs to the same strongly connected component as its negation
    // https://en.wikipedia.org/wiki/2-satisfiability#Strongly_connected_components

    public static HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> visitedInverted = new HashMap<String, Boolean>();
    public static Stack<String> stack = new Stack<String>();
    public static HashMap<String, Integer> scc = new HashMap<String, Integer>();
    public static int sccNumber = 1;

    public static void firstDFS(String v) throws IOException{
        if ((visited.get(v))){
            return;
        }

        visited.put(v, true);

        List<String> accessibleNeighbors = successorListOf(implicationGraph, v);

        for (String u : accessibleNeighbors){
            firstDFS(u);
        }

        stack.push(v);
    }

    public static void secondDFS(String v) throws IOException{
        if ((visitedInverted.get(v))){
            return;
        }

        scc.put(v, sccNumber);

        visitedInverted.put(v, true);

        List<String> invertedAccessibleNeighbors = predecessorListOf(implicationGraph, v);

        for (String u : invertedAccessibleNeighbors){
            secondDFS(u);
        }
    }

    public static Boolean solve() throws IOException{
        Set<String> vertices = implicationGraph.vertexSet();

        //
        for (String v : vertices){
            visited.put(v, false);
            visitedInverted.put(v, false);
            scc.put(v, 0);
        }

        for (String v : vertices){
            if (!visited.get(v)){
                firstDFS(v);
            }
        }

        while (!stack.isEmpty()){
            String top = stack.pop();

            if (!visitedInverted.get(top)){
                secondDFS(top);
                sccNumber++;
            }
        }

        for (String v : vertices){
            String not_v = "~" + v;
            if (Objects.equals(scc.get(v), scc.get(not_v)) && scc.get(v) != 0){
                return false;
            }
        }

        return true;

    }


    public static void main(String args[]) throws IOException {
        initGraph();

        Boolean answer = solve();

        System.out.println(answer);
    }



}
