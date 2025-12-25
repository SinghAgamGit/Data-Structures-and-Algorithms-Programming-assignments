# Programming Assignment 2 – Graph Algorithms (MST + Paris Metro)

## Overview
This Java project implements weighted graph algorithms across two parts:

- **Part 1 (Main.java):** Builds a weighted undirected graph from input and computes the **Minimum Spanning Tree (MST)** total cost using **Kruskal’s algorithm**. If the graph is disconnected it prints `Impossible`.
- **Part 2 (ParisMetro.java):** Models the Paris Metro as a graph, detects **hub stations** by merging stations connected by “walking edges” (cost `-1`), constructs a reduced **hub graph** using only paid segments (positive costs), then computes the MST over hubs to minimize total purchase cost.

Focus: data structures, graph modeling, MST computation, and efficient implementation using priority queues and union find.

## Tech
- Java (standard library only)
- Uses `PriorityQueue`, `ArrayList`, and custom union find (`PartitionB`, `NodeB`)


## Part 1 – MST (Main.java)
### What it does
For each graph instance:
- Reads station names and weighted edges
- Runs Kruskal’s algorithm using a custom partition structure
- Prints the MST total cost
- Prints `Impossible` if an MST cannot be formed

### Output behavior
- `Impossible` if the graph is disconnected
- `0` for the single-vertex trivial case (when the origin matches the only station)
- Otherwise prints the integer total cost of the MST

## Part 2 – Paris Metro (ParisMetro.java)
### Input
`ParisMetro` reads from **standard input** (so you can redirect `metro.txt`):

- First line: `n m`
- Next `n` lines: `vertexId stationName`
- Separator line after vertices (discarded in code, commonly `$`)
- Next `m` lines: `v1 v2 weight`

Edge weights:
- `-1` means a walking connection, used to merge stations into the same hub cluster
- `> 0` means a paid segment cost

### How it works
1. **Build graph**
   - Stores all edges in adjacency lists (`metroGraph`)
2. **Find hub clusters**
   - All `-1` walking edges are unioned in a union find structure
   - Any cluster with size `> 1` is treated as a hub
   - Each vertex gets a `hubIndex` or `-1` if not part of a hub
3. **Build “segments” between hubs**
   - For every hub vertex, follow positive-cost edges outward
   - Walk along the line by continuing forward on positive edges until:
     - another hub is reached (segment recorded), or
     - the path cannot continue
   - Each recorded segment is an edge between hub IDs with total accumulated cost
4. **Compute MST on the hub graph**
   - Runs Kruskal on hub-to-hub segments
   - Prints total MST cost and lists the chosen segments sorted by cost

### Output
`ParisMetro.java` prints:
- Number of vertices and edges
- Hub station names and hub counts
- Number of possible segments found
- Either `Impossible` or:
  - `Total cost of the MST = $X`
  - A numbered list of `Segments to Buy` in ascending cost order


## Notes for Reviewers
- Uses Kruskal’s algorithm with a union find structure to ensure near-optimal performance
- Hub detection is done by collapsing walking edges (cost `-1`) into components
- Segment construction builds a reduced graph over hubs only, then computes an MST over that reduced graph
- No third-party libraries used

## Author
Agam Singh
