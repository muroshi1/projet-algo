import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;


import java.io.IOException;


public class Main{
        private static final int SIZE = 10;

        public static


        public static String[] initArray() throws IOException {
                FileArrayProvider x = new FileArrayProvider();
                return x.readLines("./src/format.txt");
        }

        public static SimpleGraph<String, DefaultEdge> initGraph() throws IOException {
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
                SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

                // Add vertices and edges
                for(String s : array){
                        String[] light = s.split(" ");

                        // Set vertices names
                        String X_open = "X" + light[0] + "o";
                        String X_closed = "X" + light[0] + "f";
                        String Y_open = "Y" + light[1] + "o";
                        String Y_closed = "Y" + light[1] + "f";

                        // Add vertices
                        if (graph.vertexSet().contains(X_open) == false){
                                graph.addVertex(X_open);
                        }
                        if (graph.vertexSet().contains(X_closed) == false){
                                graph.addVertex(X_closed);
                        }
                        if (graph.vertexSet().contains(Y_open) == false){
                                graph.addVertex(Y_open);
                        }
                        if (graph.vertexSet().contains(Y_closed) == false){
                                graph.addVertex(Y_closed);
                        }

                        // Add constraint edges
                        if (Objects.equals(light[2], "1")){
                                graph.addEdge(X_open, Y_open);
                        }
                        if (Objects.equals(light[3], "1")){
                                graph.addEdge(X_open, Y_closed);
                        }
                        if (Objects.equals(light[4], "1")){
                                graph.addEdge(X_closed, Y_open);
                        }
                        if (Objects.equals(light[5], "1")){
                                graph.addEdge(X_closed, Y_closed);
                        }
                }
                return graph;
        }

        public static void findSolution() throws IOException {
                SimpleGraph<String, DefaultEdge> graph = initGraph();

                // Print out the graph to be sure it's really complete
                Iterator<String> iter = new DepthFirstIterator<>(graph);
                while (iter.hasNext()) {
                        String vertex = iter.next();
                        System.out.println(
                                "Vertex " + vertex + " is connected to: "
                                        + graph.edgesOf(vertex).toString());
                }
        }

        public static void main(String args[]) throws IOException {
                findSolution();
        }


        public static void hello(String[] args)
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

                graph.addVertex("hello");
                graph.addVertex("world");
                graph.addVertex("perception");

                graph.addEdge("hello", "world");
                graph.addEdge("world", "perception");

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
