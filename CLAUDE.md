# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Maven-based Java project for algorithm design experiments (算法设计与分析实验). The project contains implementations of classic algorithms with detailed documentation and HTML-based visualizations.

## Build and Run Commands

```bash
# Compile the project
mvn compile

# Run a specific experiment (from project root)
mvn exec:java -Dexec.mainClass="com.rongx.exp1.EqualElements"
mvn exec:java -Dexec.mainClass="com.rongx.exp1.SetPartition"
mvn exec:java -Dexec.mainClass="com.rongx.exp2.GrayCode"
mvn exec:java -Dexec.mainClass="com.rongx.exp2.ChessMove"

# Package the project
mvn package
```

## Project Structure

- `src/main/java/com/rongx/exp1/` - Experiment 1: Simple algorithm design
  - `EqualElements.java` - Duplicate detection using self-implemented HashSet (链地址法/Separate Chaining)
  - `SetPartition.java` - Set partition using self-implemented QuickSort
- `src/main/java/com/rongx/exp2/` - Experiment 2: Recursive algorithms
  - `GrayCode.java` - Gray code generation using recursive construction
  - `ChessMove.java` - Chess piece movement puzzle solved with BFS
- `docs/` - Experiment reports (Markdown with Mermaid diagrams)
- `show/` - HTML/CSS/JS visualization pages for algorithm demonstrations
- `tech_documents/` - Technical documentation and experiment requirements

## Architecture Notes

### Experiment 1 Algorithms

**EqualElements (Duplicate Detection)**:
- Uses self-implemented HashSet with Separate Chaining (链地址法) for hash collision resolution
- Hash function: `hash = abs(value) % capacity`
- Time complexity: O(n) average, O(n²) worst case
- No Java standard library collections used - all data structures self-implemented

**SetPartition (Integer Set Partition)**:
- Uses self-implemented QuickSort for sorting
- Greedy strategy: smallest n/2 elements form one subset, largest n/2 form another
- Partition function selects rightmost element as pivot
- Time complexity: O(n log n) average

### Experiment 2 Algorithms

**GrayCode**:
- Recursive construction: G(n) = [0 + G(n-1)] + [1 + reverse(G(n-1))]
- Base case: G(1) = ["0", "1"]
- Key insight: reverse order ensures adjacent codes differ by exactly one bit
- Time/Space complexity: O(n·2^n)

**ChessMove**:
- BFS (广度优先搜索) for shortest path in state space
- State representation: string like "WWWWBBBB__" (n whites, n blacks, 2 empty slots)
- Initial state: n W's + n B's + "__" (right side)
- Target state: "__" + alternating WB pairs
- Move rule: must jump over at least one piece when moving adjacent pair to empty slots
- BFS guarantees shortest path (DFS may find longer solutions)
- Observed pattern: minimum steps = n+1

## Input Format

All programs use standard input with multiple test cases:
- First line: number of test cases (m)
- Each test case follows specific format (see individual file comments)

## Code Style

- Extensive inline documentation with algorithm explanations in Chinese
- ASCII art diagrams in comments illustrating data structures
- Detailed complexity analysis at end of each file
- Validation methods included for correctness checking
- Standard Java conventions with descriptive variable names

## Visualization

The `show/` directory contains HTML-based algorithm visualizations:
- `index.html` - Navigation page
- Individual pages for each algorithm with step-by-step animations
- Uses CSS animations and JavaScript for interactive demonstrations