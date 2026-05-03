package test;

import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GeneralCheckingFunctions {

    public static boolean check3DMaze() {
    boolean weChoseToDoTheMaze3DAssignment = false;
    return weChoseToDoTheMaze3DAssignment;
    }

    public static String getGithubLink(){
        String githubLink = "https://github.com/dagmis-spec/ATP-Project.git";
        return githubLink;
    }

    /*
     * ============================================================
     *  Part A -- Sanity Check
     * ============================================================
     *  Java 17 | No external dependencies
     *
     *  HOW TO USE
     *  ----------
     *  1. Copy this file into your project's  src/  folder
     *     (the same root where your "algorithms" package folder lives).
     *  2. Compile and run:
     *       javac -cp src src/test/GeneralCheckingFunctions.java
     *       java  -cp src test.GeneralCheckingFunctions <output_file_path>
     *     Example:
     *       java  -cp src test.GeneralCheckingFunctions ./results/sanity_results.txt
     *  3. Every test prints [PASS] or [FAIL] to console AND to the output file.
     *     A summary line at the end shows how many passed.
     *
     *  This file lives in the test package.
     * ============================================================
     */

        // ---- counters ----
        private static int totalTests  = 0;
        private static int passedTests = 0;

        // ---- output collection ----
        private static final StringBuilder output = new StringBuilder();

        // ================================================================
        //  Main
        // ================================================================
        public static void main(String[] args) {
            log("========================================");
            log("  Part A  --  Sanity Check");
            log("========================================");
            log("");

            // --- 1. Maze generator instantiation ---
            testGeneratorInstantiation();

            // --- 2. Maze generation ---
            testMazeGeneration();

            // --- 3. Maze properties (positions & bounds) ---
            testMazeProperties();

            // --- 4. Time measurement ---
            testTimeMeasurement();

            // --- 5. SearchableMaze implements ISearchable ---
            testSearchableMaze();

            // --- 6. Search algorithm instantiation ---
            testSearchAlgorithmInstantiation();

            // --- 7. Solve maze with each algorithm ---
            testSolveMaze();

            // --- 8. Search algorithm API (getName, getNumberOfNodesEvaluated) ---
            testSearchAlgorithmAPI();

            // ---- summary ----
            log("");
            log("========================================");
            log(String.format("  Results: %d/%d passed", passedTests, totalTests));
            log("========================================");

            // ---- write to file ----
            if (args.length > 0) {
                writeToFile(args[0]);
            } else {
                System.out.println("\nTip: pass an output file path as argument to save results:");
                System.out.println("  java PartA_SanityCheck ./my_results.txt");
            }
        }

        // ================================================================
        //  1. Generator instantiation
        // ================================================================
        private static void testGeneratorInstantiation() {
            // EmptyMazeGenerator
            try {
                IMazeGenerator gen = new EmptyMazeGenerator();
                check(gen instanceof IMazeGenerator, "EmptyMazeGenerator is IMazeGenerator");
            } catch (Exception e) {
                fail("EmptyMazeGenerator is IMazeGenerator", e.getMessage());
            }

            // SimpleMazeGenerator
            try {
                IMazeGenerator gen = new SimpleMazeGenerator();
                check(gen instanceof IMazeGenerator, "SimpleMazeGenerator is IMazeGenerator");
            } catch (Exception e) {
                fail("SimpleMazeGenerator is IMazeGenerator", e.getMessage());
            }

            // MyMazeGenerator
            try {
                IMazeGenerator gen = new MyMazeGenerator();
                check(gen instanceof IMazeGenerator, "MyMazeGenerator is IMazeGenerator");
            } catch (Exception e) {
                fail("MyMazeGenerator is IMazeGenerator", e.getMessage());
            }
        }

        // ================================================================
        //  2. Maze generation (non-null return)
        // ================================================================
        private static void testMazeGeneration() {
            String[] names = {"EmptyMazeGenerator", "SimpleMazeGenerator", "MyMazeGenerator"};
            IMazeGenerator[] generators = null;
            try {
                generators = new IMazeGenerator[]{
                        new EmptyMazeGenerator(),
                        new SimpleMazeGenerator(),
                        new MyMazeGenerator()
                };
            } catch (Exception e) {
                fail("Instantiate generators for generation test", e.getMessage());
                return;
            }

            for (int i = 0; i < generators.length; i++) {
                try {
                    Maze maze = generators[i].generate(10, 10);
                    check(maze != null, names[i] + ".generate(10,10) returns non-null Maze");
                } catch (Exception e) {
                    fail(names[i] + ".generate(10,10) returns non-null Maze", e.getMessage());
                }
            }
        }

        // ================================================================
        //  3. Maze properties -- positions within bounds
        // ================================================================
        private static void testMazeProperties() {
            try {
                IMazeGenerator gen = new SimpleMazeGenerator();
                int rows = 10, cols = 10;
                Maze maze = gen.generate(rows, cols);

                Position start = maze.getStartPosition();
                Position goal  = maze.getGoalPosition();

                check(start != null, "getStartPosition() is non-null");
                check(goal  != null, "getGoalPosition() is non-null");

                boolean startInBounds = start.getRowIndex() >= 0 && start.getRowIndex() < rows
                        && start.getColumnIndex() >= 0 && start.getColumnIndex() < cols;
                check(startInBounds, "Start position within maze bounds");

                boolean goalInBounds = goal.getRowIndex() >= 0 && goal.getRowIndex() < rows
                        && goal.getColumnIndex() >= 0 && goal.getColumnIndex() < cols;
                check(goalInBounds, "Goal position within maze bounds");
            } catch (Exception e) {
                fail("Maze properties (positions & bounds)", e.getMessage());
            }
        }

        // ================================================================
        //  4. Time measurement
        // ================================================================
        private static void testTimeMeasurement() {
            try {
                IMazeGenerator gen = new SimpleMazeGenerator();
                long time = gen.measureAlgorithmTimeMillis(10, 10);
                check(time >= 0, "measureAlgorithmTimeMillis returns non-negative value (" + time + " ms)");
            } catch (Exception e) {
                fail("measureAlgorithmTimeMillis returns non-negative value", e.getMessage());
            }
        }

        // ================================================================
        //  5. SearchableMaze implements ISearchable
        // ================================================================
        private static void testSearchableMaze() {
            try {
                IMazeGenerator gen = new SimpleMazeGenerator();
                Maze maze = gen.generate(10, 10);
                SearchableMaze searchable = new SearchableMaze(maze);
                check(searchable instanceof ISearchable, "SearchableMaze is ISearchable");
            } catch (Exception e) {
                fail("SearchableMaze is ISearchable", e.getMessage());
            }
        }

        // ================================================================
        //  6. Search algorithm instantiation
        // ================================================================
        private static void testSearchAlgorithmInstantiation() {
            // BreadthFirstSearch
            try {
                ISearchingAlgorithm alg = new BreadthFirstSearch();
                check(alg instanceof ISearchingAlgorithm, "BreadthFirstSearch is ISearchingAlgorithm");
            } catch (Exception e) {
                fail("BreadthFirstSearch is ISearchingAlgorithm", e.getMessage());
            }

            // DepthFirstSearch
            try {
                ISearchingAlgorithm alg = new DepthFirstSearch();
                check(alg instanceof ISearchingAlgorithm, "DepthFirstSearch is ISearchingAlgorithm");
            } catch (Exception e) {
                fail("DepthFirstSearch is ISearchingAlgorithm", e.getMessage());
            }

            // BestFirstSearch
            try {
                ISearchingAlgorithm alg = new BestFirstSearch();
                check(alg instanceof ISearchingAlgorithm, "BestFirstSearch is ISearchingAlgorithm");
            } catch (Exception e) {
                fail("BestFirstSearch is ISearchingAlgorithm", e.getMessage());
            }
        }

        // ================================================================
        //  7. Solve maze with each algorithm
        // ================================================================
        private static void testSolveMaze() {
            IMazeGenerator gen;
            Maze maze;
            SearchableMaze searchable;
            try {
                gen = new MyMazeGenerator();
                maze = gen.generate(30, 30);
                searchable = new SearchableMaze(maze);
            } catch (Exception e) {
                fail("Prepare 30x30 maze for solving", e.getMessage());
                return;
            }

            String[] names = {"BreadthFirstSearch", "DepthFirstSearch", "BestFirstSearch"};
            ISearchingAlgorithm[] algorithms;
            try {
                algorithms = new ISearchingAlgorithm[]{
                        new BreadthFirstSearch(),
                        new DepthFirstSearch(),
                        new BestFirstSearch()
                };
            } catch (Exception e) {
                fail("Instantiate search algorithms for solving", e.getMessage());
                return;
            }

            for (int i = 0; i < algorithms.length; i++) {
                try {
                    // Each algorithm gets a fresh SearchableMaze to avoid state issues
                    SearchableMaze fresh = new SearchableMaze(maze);
                    Solution solution = algorithms[i].solve(fresh);
                    check(solution != null, names[i] + " returns non-null Solution");

                    if (solution != null) {
                        ArrayList<AState> path = solution.getSolutionPath();
                        check(path != null && !path.isEmpty(),
                                names[i] + " solution path is non-empty");
                    }
                } catch (Exception e) {
                    fail(names[i] + " solve", e.getMessage());
                }
            }
        }

        // ================================================================
        //  8. Search algorithm API -- getName, getNumberOfNodesEvaluated
        // ================================================================
        private static void testSearchAlgorithmAPI() {
            IMazeGenerator gen;
            Maze maze;
            try {
                gen = new MyMazeGenerator();
                maze = gen.generate(30, 30);
            } catch (Exception e) {
                fail("Prepare maze for algorithm API test", e.getMessage());
                return;
            }

            String[] names = {"BreadthFirstSearch", "DepthFirstSearch", "BestFirstSearch"};
            ISearchingAlgorithm[] algorithms;
            try {
                algorithms = new ISearchingAlgorithm[]{
                        new BreadthFirstSearch(),
                        new DepthFirstSearch(),
                        new BestFirstSearch()
                };
            } catch (Exception e) {
                fail("Instantiate search algorithms for API test", e.getMessage());
                return;
            }

            for (int i = 0; i < algorithms.length; i++) {
                try {
                    // getName() before solving
                    String name = algorithms[i].getName();
                    check(name != null, names[i] + ".getName() is non-null");

                    // Solve, then check nodes evaluated
                    SearchableMaze searchable = new SearchableMaze(maze);
                    algorithms[i].solve(searchable);

                    int nodesEvaluated = algorithms[i].getNumberOfNodesEvaluated();
                    check(nodesEvaluated > 0,
                            names[i] + ".getNumberOfNodesEvaluated() > 0 (got " + nodesEvaluated + ")");
                } catch (Exception e) {
                    fail(names[i] + " API check", e.getMessage());
                }
            }
        }

        // ================================================================
        //  Helpers
        // ================================================================

        /** Print to console and collect for file output. */
        private static void log(String message) {
            System.out.println(message);
            output.append(message).append(System.lineSeparator());
        }

        /** Run a boolean assertion. Counts as one test regardless of outcome. */
        private static void check(boolean condition, String testName) {
            if (condition) {
                pass(testName);
            } else {
                fail(testName, "condition was false");
            }
        }

        /** Record a passing test. */
        private static void pass(String testName) {
            totalTests++;
            passedTests++;
            log("  [PASS] " + testName);
        }

        /** Record a failing test. */
        private static void fail(String testName, String reason) {
            totalTests++;
            log("  [FAIL] " + testName + "  --  " + reason);
        }

        /** Write collected output to file. */
        private static void writeToFile(String filePath) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.print(output.toString());
                System.out.println("\nResults saved to: " + filePath);
            } catch (IOException e) {
                System.out.println("\nError writing results to file: " + e.getMessage());
            }
        }
    }

