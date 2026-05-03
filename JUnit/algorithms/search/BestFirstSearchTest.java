package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BestFirstSearch.
 * The tests use small deterministic mazes so expected paths and edge cases are easy to verify.
 */
class BestFirstSearchTest {

    /**
     * Verifies that the algorithm fails clearly when the searchable problem is null.
     */
    @Test
    void solveNullSearchableThrowsException() {
        BestFirstSearch searcher = new BestFirstSearch();

        assertThrows(NullPointerException.class, () -> searcher.solve(null));
    }

    /**
     * If the start and goal are the same cell, the solution should contain only that cell.
     */
    @Test
    void solveStartIsGoalReturnsSingleStatePath() {
        Maze maze = new Maze(1, 1);
        maze.setCellValue(0, 0, 0);
        maze.setStartPosition(new Position(0, 0));
        maze.setGoalPosition(new Position(0, 0));
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(new SearchableMaze(maze));
        ArrayList<AState> path = solution.getSolutionPath();

        assertEquals(1, path.size());
        assertMazeStatePosition(path.get(0), 0, 0);
        assertEquals(1, searcher.getNumberOfNodesEvaluated());
    }

    /**
     * Checks that an unreachable goal returns an empty solution path.
     */
    @Test
    void solveNoPossiblePathReturnsEmptySolution() {
        Maze maze = new Maze(2, 2);
        maze.setCellValue(0, 0, 0);
        maze.setCellValue(1, 1, 0);
        maze.setStartPosition(new Position(0, 0));
        maze.setGoalPosition(new Position(1, 1));
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(new SearchableMaze(maze));

        assertTrue(solution.getSolutionPath().isEmpty());
    }

    /**
     * In an open maze, two diagonal steps are cheaper than four direct steps:
     * 15 + 15 = 30 instead of 10 + 10 + 10 + 10 = 40.
     */
    @Test
    void solveOpenMazePrefersLowerCostDiagonalPath() {
        Maze maze = createOpenMaze(3, 3);
        maze.setStartPosition(new Position(0, 0));
        maze.setGoalPosition(new Position(2, 2));
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(new SearchableMaze(maze));
        ArrayList<AState> path = solution.getSolutionPath();

        assertEquals(3, path.size());
        assertMazeStatePosition(path.get(0), 0, 0);
        assertMazeStatePosition(path.get(1), 1, 1);
        assertMazeStatePosition(path.get(2), 2, 2);
        assertEquals(30, calculatePathCost(path));
        assertValidPath(maze, path);
    }

    /**
     * Verifies that the returned path does not step onto blocked cells.
     */
    @Test
    void solvePathAroundWallDoesNotUseBlockedCells() {
        Maze maze = createOpenMaze(3, 3);
        maze.setCellValue(1, 1, 1);
        maze.setStartPosition(new Position(0, 0));
        maze.setGoalPosition(new Position(2, 2));
        BestFirstSearch searcher = new BestFirstSearch();

        Solution solution = searcher.solve(new SearchableMaze(maze));
        ArrayList<AState> path = solution.getSolutionPath();

        assertFalse(path.isEmpty());
        assertMazeStatePosition(path.get(0), 0, 0);
        assertMazeStatePosition(path.get(path.size() - 1), 2, 2);
        assertValidPath(maze, path);
        for (AState state : path) {
            Position position = ((MazeState) state).getPosition();
            assertFalse(position.getRowIndex() == 1 && position.getColumnIndex() == 1);
        }
    }

    /**
     * Checks the public algorithm name used by the test runner.
     */
    @Test
    void getNameReturnsAlgorithmName() {
        BestFirstSearch searcher = new BestFirstSearch();

        assertEquals("Best First Search", searcher.getName());
    }

    /**
     * Creates a maze where every cell is open.
     */
    private Maze createOpenMaze(int rows, int columns) {
        Maze maze = new Maze(rows, columns);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                maze.setCellValue(row, column, 0);
            }
        }

        return maze;
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
     * Calculates the total path cost using the project's movement costs.
     */
    private int calculatePathCost(ArrayList<AState> path) {
        int cost = 0;

        for (int i = 1; i < path.size(); i++) {
            Position previous = ((MazeState) path.get(i - 1)).getPosition();
            Position current = ((MazeState) path.get(i)).getPosition();
            int rowDifference = Math.abs(previous.getRowIndex() - current.getRowIndex());
            int columnDifference = Math.abs(previous.getColumnIndex() - current.getColumnIndex());

            cost += rowDifference == 1 && columnDifference == 1 ? 15 : 10;
        }

        return cost;
    }

    /**
     * Verifies every cell in the path is open and every transition is legal.
     */
    private void assertValidPath(Maze maze, ArrayList<AState> path) {
        for (int i = 0; i < path.size(); i++) {
            Position current = ((MazeState) path.get(i)).getPosition();

            assertTrue(isOpenCell(maze, current.getRowIndex(), current.getColumnIndex()));

            if (i > 0) {
                Position previous = ((MazeState) path.get(i - 1)).getPosition();
                assertLegalMove(maze, previous, current);
            }
        }
    }

    /**
     * Checks that a move goes to a neighboring cell and follows the diagonal rule.
     */
    private void assertLegalMove(Maze maze, Position from, Position to) {
        int rowDifference = Math.abs(from.getRowIndex() - to.getRowIndex());
        int columnDifference = Math.abs(from.getColumnIndex() - to.getColumnIndex());

        assertTrue(rowDifference <= 1 && columnDifference <= 1);
        assertTrue(rowDifference + columnDifference > 0);

        if (rowDifference == 1 && columnDifference == 1) {
            // A diagonal move is allowed only if at least one two-step regular route is open.
            boolean firstRegularStepOpen = isOpenCell(maze, to.getRowIndex(), from.getColumnIndex());
            boolean secondRegularStepOpen = isOpenCell(maze, from.getRowIndex(), to.getColumnIndex());

            assertTrue(firstRegularStepOpen || secondRegularStepOpen);
        }
    }

    /**
     * @return true if the cell is inside the maze and is not a wall.
     */
    private boolean isOpenCell(Maze maze, int row, int column) {
        return row >= 0
                && row < maze.getRows()
                && column >= 0
                && column < maze.getColumns()
                && maze.getCellValue(row, column) == 0;
    }
}
