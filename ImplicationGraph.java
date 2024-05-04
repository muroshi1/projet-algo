import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.Iterator;
import java.util.function.Supplier;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.util.*;

import java.io.IOException;


// https://jgrapht.org/guide/UserOverview#development-setup
public class ImplicationGraph{

    // Read txt file into an array
    public static String[] initArray(String filename) throws IOException {
        FileArrayProvider x = new FileArrayProvider();
        return x.readLines(filename);
    }

    // Create implication graph
    public static Graph<String, DefaultEdge> initGraph(String filename) throws IOException {
        String[] array = initArray(filename);

        // https://jgrapht.org/guide/UserOverview#graph-generation
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
            if (!graph.vertexSet().contains(X)){
                graph.addVertex(X);
            }
            if (!graph.vertexSet().contains(notX)){
                graph.addVertex(notX);
            }
            if (!graph.vertexSet().contains(Y)){
                graph.addVertex(Y);
            }
            if (!graph.vertexSet().contains(notY)){
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
                // case Constraint.Equal
                // case Constraint.AlwaysOpen
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

        return graph;
    }
}
