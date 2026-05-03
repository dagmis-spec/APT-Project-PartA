package algorithms.mazeGenerators;

/**
 * Generates a completely open maze no walls at all.
 * Start is placed at the top left corner and goal at the bottom right.
 */
public class EmptyMazeGenerator extends AMazeGenerator {

    /**
     * Generates a maze with no walls.
     * Every cell is set to 0.
     *
     * @param rows number of rows
     * @param columns number of columns
     * @return an empty  Maze
     */
    @Override
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows, columns);

        // open every cell
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++)
                maze.setCellValue(r, c, 0);

        maze.setStartPosition(new Position(0, 0));
        maze.setGoalPosition(new Position(rows - 1, columns - 1));
        return maze;
    }
}