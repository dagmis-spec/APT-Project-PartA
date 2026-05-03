package algorithms.mazeGenerators;

/**
 * Interface defining the contract for any maze-generation algorithm.
 */
public interface IMazeGenerator {

    /**
     * Generates a new maze with the given dimensions.
     * @param rows number of rows
     * @param columns number of columns
     * @return generated Maze
     */
    Maze generate(int rows, int columns);

    /**
     * Measures how long the generate method takes.
     * @param rows    number of rows
     * @param columns number of columns
     * @return time in milliseconds
     */
    long measureAlgorithmTimeMillis(int rows, int columns);
}