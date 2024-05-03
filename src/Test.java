import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.Iterator;
import java.util.function.Supplier;
import org.jgrapht.Graph;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.util.SupplierUtil;

import java.util.Iterator;
import java.util.Vector;
import java.util.function.Supplier;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Test {

    public static void main(String[] args)
    {
        // Create the VertexFactory so the generator can create vertices
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
        SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        graph.addVertex("a f");
        graph.addVertex("g f");
        graph.addVertex("c o");
        graph.addVertex("g o");

        graph.addVertex("c f");
        graph.addVertex("f o");
        graph.addVertex("f f");

        graph.addVertex("d o");
        graph.addVertex("d f");
        graph.addVertex("e o");
        graph.addVertex("e f");
        graph.addVertex("h o");
        graph.addVertex("h f");
        graph.addVertex("b o");
        graph.addVertex("b f");

        graph.addEdge("a f", "g f");
        graph.addEdge("c o", "g o");

        graph.addEdge("c f", "f o");
        graph.addEdge("c f", "f f");

        graph.addEdge("d f", "h o");
        graph.addEdge("d o", "e f");
        graph.addEdge("d o", "h f");
        graph.addEdge("h o", "b f");
        graph.addEdge("h f", "b o");
        graph.addEdge("b o", "e o");
        graph.addEdge("b f", "e o");

        // Print out the graph to be sure it's really complete
        Iterator<String> iter = new DepthFirstIterator<>(graph);
        while (iter.hasNext()) {
            String vertex = iter.next();
            System.out.println(
                    "Vertex " + vertex + " is connected to: "
                            + graph.edgesOf(vertex).toString());
        }
    }
}
