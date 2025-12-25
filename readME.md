# Island Survey – Disjoint Set / Union-Find Implementation

## Overview
This project implements an Island Survey system that determines connectivity between locations using a partition-based data structure. The program processes survey data to identify which locations belong to the same island based on direct and indirect connections.

The focus of this project is data structure design, algorithmic efficiency, and clean object-oriented implementation in Java.

## Problem Description
Given a set of locations and a series of survey connections, the program groups locations into islands. Two locations are part of the same island if there exists any path of connections between them.

The solution uses a Disjoint Set (Union-Find) structure to efficiently merge and query connected components.

## Key Concepts and Skills
- Disjoint Set / Union-Find
- Abstract Data Type (ADT) implementation
- Object-oriented programming in Java
- Efficient union and find operations
- Clean modular code structure

## Technologies Used
- Language: Java
- Runtime: Standard Java (JDK)
- External libraries: None

## File Descriptions
- **IslandSurvey.java**  
  Main program that processes island survey input and determines connectivity.

- **IslandLakesSurvey.java**  
  Extension of the survey logic applied to lake-based input data.

- **Partition.java**  
  Defines the Partition ADT used to manage disjoint sets.

- **Node.java**  
  Represents individual elements within the partition structure.

- **input_outputs/**  
  Contains sample input files and corresponding expected outputs used for testing.

## Design Overview
- Each location starts as its own partition
- Survey connections trigger union operations
- Find operations identify island representatives
- The structure efficiently tracks island membership
- Output reflects all direct and indirect connections

## Input and Output
**Input**
- Text files describing locations and connections
- Format follows assignment specifications

**Output**
- Text files showing island groupings
- Each grouping represents a connected component

## Performance
- Near-constant time union and find operations
- Efficient for large numbers of locations and connections

## Notes for Reviewers
- Completed as a university-level data structures assignment
- No third-party libraries used
- Emphasis on correctness, efficiency, and clear design
- Code is easy to read, test, and extend

## Author
Agam Singh

