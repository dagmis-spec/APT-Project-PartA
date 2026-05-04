package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

/**
 * Adapts a Maze object to the generic ISearchable interface.
 * Search algorithms can work with this class without knowing the Maze internals.
 */
public class SearchableMaze implements ISearchable {

    private Maze maze;

    /**
     * Creates a searchable wrapper around a maze.
     *
     * @param maze the maze that the search algorithm should solve.
     */
    public SearchableMaze(Maze maze){
        this.maze = maze; //TODO: maybe use a copy constructor
    }

    /**
     * @return the maze start position as a searchable state.
     */
    @Override
    public AState getStartState() {
        MazeState state = new MazeState(maze.getStartPosition());
        return state;
    }

    /**
     * @return the maze goal position as a searchable state.
     */
    @Override
    public AState getGoalState() {
        MazeState state = new MazeState(maze.getGoalPosition());
        return state;
    }

    /**
     * Finds every legal move from the given maze state.
     * Legal moves include four regular directions and four diagonal directions.
     *
     * @param state the current state being expanded.
     * @return a list of states that can be reached from the current state.
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState state) {
        MazeState mazeState = (MazeState) state;
        Position position = mazeState.getPosition();
        ArrayList<AState> allPossibleStates = new ArrayList<>();
        int row = position.getRowIndex();
        int column = position.getColumnIndex();

        // Check neighbors clockwise, starting from the top cell.
        addStateIfPossible(allPossibleStates, row - 1, column); //up
        addStateIfPossibleDiagonal(allPossibleStates, row - 1, column + 1, row - 1, column, row, column + 1); //up-right
        addStateIfPossible(allPossibleStates, row, column + 1); //right
        addStateIfPossibleDiagonal(allPossibleStates, row + 1, column + 1, row + 1, column, row, column + 1); //down-right
        addStateIfPossible(allPossibleStates, row + 1, column); //down
        addStateIfPossibleDiagonal(allPossibleStates, row + 1, column - 1, row + 1, column, row, column - 1); //down-left
        addStateIfPossible(allPossibleStates, row, column - 1); //left
        addStateIfPossibleDiagonal(allPossibleStates, row - 1, column - 1, row - 1, column, row, column - 1); //up-left

        return allPossibleStates;
    }

    /**
     * @return true if the row and column are inside the maze matrix.
     */
    private boolean isInBounds(int row, int column) {
        return row >= 0
                && row < maze.getRows()
                && column >= 0
                && column < maze.getColumns();
    }

    /**
     * @return true if the cell is inside the maze and is not a wall.
     */
    private boolean isOpenCell(int row, int column) {
        return isInBounds(row, column) && maze.getCellValue(row, column) == 0;
    }

    /**
     * Adds a regular move if the target cell is valid and open.
     */
    private void addStateIfPossible(ArrayList<AState> possibleStates, int row, int column) {
        if (isOpenCell(row, column)) {
            possibleStates.add(new MazeState(new Position(row, column)));
        }
    }

    /**
     * Adds a diagonal move if the target is open and at least one regular path
     * toward that diagonal target is also open.
     */
    private void addStateIfPossibleDiagonal(ArrayList<AState> possibleStates, int targetRow, int targetColumn,
                                            int firstPathRow, int firstPathColumn,
                                            int secondPathRow, int secondPathColumn) {
        if (isOpenCell(targetRow, targetColumn)
                && (isOpenCell(firstPathRow, firstPathColumn) || isOpenCell(secondPathRow, secondPathColumn))) {
            possibleStates.add(new MazeState(new Position(targetRow, targetColumn)));
        }
    }
}
