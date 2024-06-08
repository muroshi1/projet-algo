Projet Algorithmique 2 : Fiat Lux
=================================

# Requirements

- JetBrains' IntellijIDEA IDE
- JDK version 21 or higher
- JGraphT 1.5.2 or higher
- JGraphT setup with IntellijIDEA
- JGraphT libraries to add to Project Structure:
  - jgrapht-core-1.5.2
  - jgrapht-core-1.5.21
  - jgrapht-ext-1.5.2
  - jgrapht-io-1.5.2

# How to execute code

- After setting up IntellijIDEA with JGraphT,
  - Place the .txt file containing the problem instance into src directory 
  - Rename that .txt file into "format.txt"

- For Phase 1, go into SolverPhase1.java and click run (Current File)

- For Phase 2, go into SolverPhase2.java and click run (Current File)

# Report

## Introduction

The solutions documented in this report are designed to solve the Fiat Lux light bulb problem (see FiatLuxProblem.pdf) . In this problem we are given an instance of inter-connected light bulbs and switches. Each switch can either be open or closed, and each bulb will only light up if and only if the constraints on the 2 switches are satisfied. Phase 1 consists in answering whether all the bulbs can be turned on simultaneously, while Phase 2 consists in finding the maximum number of light bulbs we can turn on for a given instance of the problem. We'll start with the main idea, the nature of the problem, and the design and implementation in Java of the proposed solutions (see sources and citations). All the algorithms used are implemented with the Java library JGraphT.

## General Idea

The first and second phase are analogous to a 2-satisfiability problem (2-SAT) and a maximum-2-satisfiability (MAX-2-SAT) problem, respectively. 2-SAT is a ”computational problem of assigning values to variables, each of which has two possible values, in order to satisfy a system of constraints on pairs of variables.” ”Instances of the 2-satisfiability problem are typically expressed as Boolean formulas of a special type, called conjunctive normal form (2-CNF).” (Wikipedia) Instances of our problem are provided as text files, with each line representing a light bulb’s constraints for being able to be turned on. The idea is to translate these constraints into implications to then build an implication graph upon which we can use our algorithms. Said implication graph expresses ”the variables of an instance and their negations as vertices in a graph, and constraints onpairs of variables as directed edges.” (Wikipedia) We will use this to our advantage to find if all light bulbs can be turned on, and if not, for the second phase, the maximum number of light bulbs that can be turned on.

## Translating constraints and implication graph

2-CNF formulas are ”a conjunction (a Boolean and operation) of clauses, where each clause is a disjunction (a Boolean or operation).” (Wikipedia) There are 16 different combinations for the 4 bits indicating the constraints on a light bulb (2^4= 16). Each light bulb is only affected by 2 switches and can thus be converted into clauses with 2 variables then implications.

![image](https://github.com/muroshi1/projet-algo/assets/119456902/79bd9fbf-e7bf-4086-9e10-e9fc85b7f86f)

The X and Y variables are respectively row switches and column switches. We can now create our implication graph, let’s call it G.

### Implementation

These are the files needed to create the implication graph :
• Constraint.java
• FileArrayProvider.java
• ImplicationGraph.java
• Lightbulb.java

The Constraint class is stores all different types of constraints (combination of bits) as String literals. FileArrayProvider is a class taken from this link that reads the .txt file into an array of Strings that is then parsed. Within the ImplicationGraph class, the parsed data is used to add vertices and directed edges to the graph. Each switch has 2 vertices representing its 2 possible states, open and closed as respectively true and false (for example, X and ∼X). For each line representing a light bulb, depending on the constraint type, directed edges will be added to the corresponding vertices in accordance with the previously mentioned implications. The Lightbulb class has 2 attributes representing the switches affecting it and an attribute indicating which constraint it has.

## Phase 1

### Idea

Now that we have our constraints expressed as an implication graph, we can use the existing work of Aspvall, Plass and Tarjan (1) to our advantage. Phase 1 is a 2-SAT problem of which the answer is simply a true or false to whether or not we can light all light bulbs at once. To find said answer, we are going to use the notion of strongly connected components (SCC) and Kosaraju’s algorithm. Condition : ”As Aspvall et al. showed, this is a necessary and sufficient condition: a 2-CNF formula is satisfiable if and only if there is no variable that belongs to the same strongly connected component as its negation.” (Wikipedia) Thus, by applying this condition, we only need to perform Kosaraju’s algorithm to find all SCCs within out implication graph, then check for each variable that it does not reside within the same SCC as its negation. If this is the case, we can stop searching and return false. The instance is not satisfiable, meaning all light bulbs cannot be turned on all at once. If not, the instance is satisfiable.
To do so, we are going to use various of the following data structures and methods :
• Implication graph
• HashMap
• Stack
• Depth-First Search (for Kosaraju)

### Implementation

#### Finding all SCCs

Kosaraju’s algorithm involves running 2 modified Depth-First Search algorithms back-to-back. The first DFS stores vertices of G into a Stack in inverse post-order. The second DFS runs through the G’s transpose, GT , to find all SCCs. This is done by simply using JGraphT’s predecessorListOf() method. 2 different hashmaps are used for both DFS to store the already visited vertices in normal and inverted traversals. A third hashmap stores which vertices belong to which SCC.

#### Searching in each SCC

The solve() method initializes the hashmaps, runs the first DFS, and, while the Stack filled by the first DFS is not empty, runs the second DFS on all elements of said Stack, while incrementing the number of the current SCC. Finally, for each vertice (variable) of G, see if it’s present in the same SCC as its negation and return the answer accordingly.

### Time Complexity

Let V be the number of vertices and E the number of edges in G. ”Kosaraju’s algorithm performs two complete traversals of the graph and so runs in (V+E) (linear) time.” (Wikipedia), since it has 2 DFS traversals (both in O(V + E)). Verifying each vertices’ SCC is in O(V) time. So the solver for Phase 1 has a time complexity of O(V + E).

## Phase 2

### Idea

Phase 2 is a MAX-2-SAT problem where the goal is to find the maximum number of light bulbs that can be turned on. This is analogous to the maximum number of clauses that can be satisfied in our 2-CFN Boolean formula. To do so, we resorted to a simple backtracking solution. Through brute force, our algorithm tests every possible configuration to find the one with the maximum satisfiable constraints. Speed is sacrificed for the absolute certainty that the solution found is the best one.

#### Implementation

For Phase 2, SolverPhase2.java and related files are used. The find() method performs backtracking on a hashmap that stores the state of each switch (true or false) for each possible configuration. (true or false) for each possible configuration. The countCurrentMaxLights() m'ethode counts the maximum number of bulbs in the current configuration. To do this, a loop iterates on each light bulb of the Lightbulb class mentioned above, checks whether its constraints are satisfied and incurs a count. if its constraints are satisfied, and increments a counter if this is the case. The max number is returned at the end of the backtrack.

### Time Complexity

The time complexity of backtracking is O(2^V) where V is the number of vertices in G. When backtracking has found a possible configuration, we must it'erate on each bulb to verify the configuration. backtracking has found a possible configuration, we need to it'erer over each light bulb to check which ones can ˆetre lit. which ones can ˆetre allum'ees. This is done in O(E). So our algorithm is in O(E ∗ 2^V ).

### Possible optimization ideas

A possible optimization of phase 2 would be to take advantage of the fact that each bulb is affected by only two switches and each switch only affects a limited number of bulbs. The idea would be to treat the bulbs in separate groups (bulbs whose constraints affect each other, which is manifested in the implication graph) and add up the maximum number of bulbs for each group of bulbs.

# Sources and Citations

2-SAT : https://en.wikipedia.org/wiki/2-satisfiability

File into Array : https://stackoverflow.com/questions/285712/java-reading-a-file-into-an-array

2-SAT : https://github.com/xiaoyuetang/AlgorithmTwoSAT/tree/master

Kosaraju’s algorithm : https://www.topcoder.com/thrive/articles/kosarajus-algorithm-for-strongly-connected-components
