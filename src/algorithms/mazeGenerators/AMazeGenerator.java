package algorithms.mazeGenerators;

/**
 * Abstract base class for maze generators.
 */
public abstract class AMazeGenerator implements IMazeGenerator {

    /**
     * Measures the time in milliseconds that generate(int, int) takes.
     * The measurement is the same for every algorithm, so it is implemented here
     * @param rows number of rows
     * @param columns number of columns
     * @return elapsed time in milliseconds
     */
    @Override
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long start = System.currentTimeMillis();
        generate(rows, columns);
        return System.currentTimeMillis() - start;
    }

    /**
     * Concrete subclasses must provide the actual generation logic.
     * @param rows number of rows
     * @param columns number of columns
     * @return a generate link Maze
     */
    @Override
    public abstract Maze generate(int rows, int columns);
}