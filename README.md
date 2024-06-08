Projet Algorithmique 2 : Fiat Lux
=================================

# Requirements

- JetBrains' IntellijIDEA IDE
- JDK version required : 21 ou later
- JGraphT latest release : https://jgrapht.org/
- JGraphT setup with IntellijIDEA : https://github.com/jgrapht/jgrapht/wiki/Users:-How-to-use-JGraphT-as-a-dependency-in-your-projects#developing-using-intellij-idea
- JGraphT libraries to add to Project Structure:
  - jgrapht-core-1.5.2
  - jgrapht-core-1.5.21
  - jgrapht-ext-1.5.2
  - jgrapht-io-1.5.2

# How to execute code

- After setting up IntellijIDEA with JGraphT,
  - Place into src directory the .txt file containing the problem instance
  - Rename that .txt file into "format.txt" (***IMPORTANT***)

- For Phase 1, go into SolverPhase1.java and click run (Current File)

- For Phase 2, go into SolverPhase2.java and click run (Current File)

# Project

## Introduction

The solutions documented in this report are designed to solve the Fiat Lux light bulb problem (see FiatLux.pdf) . In this problem we are given an instance of inter-connected light bulbs and switches. Each switch can either be open or closed, and each bulb will only light up if and only if the constraints on the 2 switches are satisfied. Phase 1 consists in answering whether all the bulbs can be turned on simultaneously, while Phase 2 consists in finding the maximum number of light bulbs we can turn on for a given instance of the problem. We'll start with the main idea, the nature of the problem, and the design and implementation in Java of the proposed solutions (see sources and citations). All the algorithms used are implemented with the Java library JGraphT.



