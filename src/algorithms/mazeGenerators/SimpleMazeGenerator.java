package algorithms.mazeGenerators;

import java.util.Random;

/**
 * Generates a maze by guaranteeing one clear path from start to goal.
 */
public class SimpleMazeGenerator extends AMazeGenerator {

    private static final Random RANDOM = new Random();

    /**
     * Generates a maze with a guaranteed path and random additional walls.
     *
     * Strategy:
     * 1. Fill the grid with random 0s and 1s.
     * 2. make a guaranteed path from (0,0) to (rows-1, columns-1)
     *    by walking right along the first row, then down the last column.
     * @param rows number of rows
     * @param columns number of columns
     * @return a Maze that is always solvable
     */
    @Override
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows, columns);

        // fill with random 0s and 1s (roughly 50/50)
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++)
                maze.setCellValue(r, c, RANDOM.nextInt(2));

        // carve a guaranteed path: across the top row, then down the last column
        for (int c = 0; c < columns; c++)
            maze.setCellValue(0, c, 0);
        for (int r = 0; r < rows; r++)
            maze.setCellValue(r, columns - 1, 0);

        maze.setStartPosition(new Position(0, 0));
        maze.setGoalPosition(new Position(rows - 1, columns - 1));

        return maze;
    }
}