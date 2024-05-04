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


public class SolverPhase1{
    public static Graph<String, DefaultEdge> implicationGraph;

    // Initialization
    public static HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> visitedInverted = new HashMap<String, Boolean>();
    public static Stack<String> stack = new Stack<String>();
    public static HashMap<String, Integer> scc = new HashMap<String, Integer>();
    public static int sccNumber = 1;

    // Kosaraju's Algorithm for Strongly Connected Components
    // https://www.topcoder.com/thrive/articles/kosarajus-algorithm-for-strongly-connected-components
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

    // Kosaraju
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

        // a 2-CNF formula is satisfiable if and only if there is no variable that belongs to the same strongly connected component as its negation
        // https://en.wikipedia.org/wiki/2-satisfiability#Strongly_connected_components
        for (String v : vertices){
            String not_v = "~" + v;
            if (Objects.equals(scc.get(v), scc.get(not_v)) && scc.get(v) != 0){
                return false; // not satisfiable
            }
        }

        // No SCC containing both a variable and its negation, the 2 SAT problem is satisfiable
        return true;
    }

    public static void main(String args[]) throws IOException {
        implicationGraph = ImplicationGraph.initGraph("./src/format.txt");

        Boolean answer = solve();

        System.out.println(answer);
    }
}
