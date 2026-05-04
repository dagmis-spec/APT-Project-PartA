package algorithms.search;

import algorithms.mazeGenerators.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BestFirstSearch.
 * These tests use a small fake searchable graph so they focus on the algorithm
 * instead of the maze-neighbor rules implemented by SearchableMaze.
 */
class BestFirstSearchTest {

    /**
     * Checks that a null searchable input fails clearly.
     */
    @Test
    void solveNullSearchableThrowsException() {
        BestFirstSearch searcher = new BestFirstSearch();

        assertThrows(NullPointerException.class, () -> searcher.solve(null));
    }

    /**
     * Checks that a null start state fails clearly.
     */
    @Test
    void solveNullStartStateThrowsException() {
        MazeState goal = createState(0, 0);
        BestFirstSearch searcher = new BestFirstSearch();

        assertThrows(NullPointerException.class, () -> searcher.solve(new StubSearchable(null, goal)));
    }

    /**
     * Checks that start equals goal returns a one-state solution.
     */
    @Test
    void solveStartIsGoalReturnsSingleStatePath() {
        MazeState start = createState(0, 0);
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(new StubSearchable(start, start));
        ArrayList<AState> path = solution.getSolutionPath();

        assertEquals(1, path.size());
        assertMazeStatePosition(path.get(0), 0, 0);
        assertEquals(1, searcher.getNumberOfNodesEvaluated());
    }

    /**
     * Checks that no route to the goal returns an empty solution.
     */
    @Test
    void solveUnreachableGoalReturnsEmptySolution() {
        MazeState start = createState(0, 0);
        MazeState goal = createState(0, 1);
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(new StubSearchable(start, goal));

        assertTrue(solution.getSolutionPath().isEmpty());
        assertEquals(1, searcher.getNumberOfNodesEvaluated());
    }

    /**
     * Checks that the priority queue prefers the lower-cost state first.
     */
    @Test
    void solveChoosesLowerCostGoalBeforeEarlierHigherCostNeighbor() {
        MazeState start = createState(0, 0);
        MazeState higherCostNeighbor = createState(1, 1);
        MazeState goal = createState(0, 1);
        StubSearchable searchable = new StubSearchable(start, goal);
        searchable.addNeighbors(start, higherCostNeighbor, goal);
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(searchable);
        ArrayList<AState> path = solution.getSolutionPath();

        assertEquals(2, path.size());
        assertMazeStatePosition(path.get(0), 0, 0);
        assertMazeStatePosition(path.get(1), 0, 1);
        assertEquals(10, path.get(1).getCost());
        assertEquals(2, searcher.getNumberOfNodesEvaluated());
    }

    /**
     * Checks that cameFrom links are used to rebuild the full solution path.
     */
    @Test
    void solveReconstructsPathFromStartToGoal() {
        MazeState start = createState(0, 0);
        MazeState middle = createState(0, 1);
        MazeState goal = createState(0, 2);
        StubSearchable searchable = new StubSearchable(start, goal);
        searchable.addNeighbors(start, middle);
        searchable.addNeighbors(middle, goal);
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(searchable);
        ArrayList<AState> path = solution.getSolutionPath();

        assertEquals(3, path.size());
        assertMazeStatePosition(path.get(0), 0, 0);
        assertMazeStatePosition(path.get(1), 0, 1);
        assertMazeStatePosition(path.get(2), 0, 2);
        assertEquals(20, path.get(2).getCost());
    }

    /**
     * Checks that diagonal moves receive the algorithm's diagonal cost.
     */
    @Test
    void solveAssignsDiagonalMoveCost() {
        MazeState start = createState(0, 0);
        MazeState directStep = createState(0, 1);
        MazeState goal = createState(1, 1);
        StubSearchable searchable = new StubSearchable(start, goal);
        searchable.addNeighbors(start, goal, directStep);
        searchable.addNeighbors(directStep, goal);
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(searchable);
        ArrayList<AState> path = solution.getSolutionPath();

        assertEquals(2, path.size());
        assertMazeStatePosition(path.get(0), 0, 0);
        assertMazeStatePosition(path.get(1), 1, 1);
        assertEquals(15, path.get(1).getCost());
    }

    /**
     * Checks that the evaluated counter is reset between solve calls.
     */
    @Test
    void solveResetsNodesEvaluatedBetweenRuns() {
        MazeState firstStart = createState(0, 0);
        MazeState firstGoal = createState(0, 1);
        StubSearchable firstSearchable = new StubSearchable(firstStart, firstGoal);
        firstSearchable.addNeighbors(firstStart, firstGoal);
        MazeState secondStart = createState(1, 0);
        BestFirstSearch searcher = new BestFirstSearch();

        searcher.solve(firstSearchable);
        Solution secondSolution = searcher.solve(new StubSearchable(secondStart, secondStart));

        assertEquals(1, secondSolution.getSolutionPath().size());
        assertEquals(1, searcher.getNumberOfNodesEvaluated());
    }

    /**
     * Checks that duplicate neighbor states are evaluated only once.
     */
    @Test
    void solveSkipsDuplicateStatesAfterTheyAreEvaluated() {
        MazeState start = createState(0, 0);
        MazeState duplicateA = createState(0, 1);
        MazeState duplicateB = createState(0, 1);
        MazeState goal = createState(0, 2);
        StubSearchable searchable = new StubSearchable(start, goal);
        searchable.addNeighbors(start, duplicateA, duplicateB);
        searchable.addNeighbors(duplicateA, goal);
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(searchable);

        assertEquals(3, solution.getSolutionPath().size());
        assertEquals(3, searcher.getNumberOfNodesEvaluated());
    }

    /**
     * Checks the public algorithm name used by runners.
     */
    @Test
    void getNameReturnsAlgorithmName() {
        BestFirstSearch searcher = new BestFirstSearch();

        assertEquals("Best First Search", searcher.getName());
    }

    /**
     * Creates a MazeState at the given row and column.
     */
    private MazeState createState(int row, int column) {
        return new MazeState(new Position(row, column));
    }

    /**
     * Checks that a returned state is a MazeState with the expected row and column.
     */
    private void assertMazeStatePosition(AState state, int expectedRow, int expectedColumn) {
        assertTrue(state instanceof MazeState);
        Position position = ((MazeState) state).getPosition();

        assertEquals(expectedRow, position.getRowIndex());
        assertEquals(expectedColumn, position.getColumnIndex());
    }

    /**
     * Minimal searchable graph used to isolate BestFirstSearch behavior.
     */
    private static class StubSearchable implements ISearchable {
        private final AState startState;
        private final AState goalState;
        private final HashMap<AState, ArrayList<AState>> neighbors;

        private StubSearchable(AState startState, AState goalState) {
            this.startState = startState;
            this.goalState = goalState;
            this.neighbors = new HashMap<>();
        }

        private void addNeighbors(AState state, AState... possibleStates) {
            ArrayList<AState> stateNeighbors = new ArrayList<>();

            for (AState possibleState : possibleStates) {
                stateNeighbors.add(possibleState);
            }

            neighbors.put(state, stateNeighbors);
        }

        @Override
        public AState getStartState() {
            return startState;
        }

        @Override
        public AState getGoalState() {
            return goalState;
        }

        @Override
        public ArrayList<AState> getAllPossibleStates(AState state) {
            if (!neighbors.containsKey(state)) {
                return new ArrayList<>();
            }

            return new ArrayList<>(neighbors.get(state));
        }
    }
}
