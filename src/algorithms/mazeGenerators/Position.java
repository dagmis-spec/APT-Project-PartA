package algorithms.mazeGenerators;

/**
        * Represents a position (cell) inside the maze.
        * Stores row and column indices.
 */
public class Position {

    private final int row;
    private final int column;

    /**
     * Constructs a Position with the given row and column.
     * @param row the row index
     * @param column the column index
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /** @return the row index of this position */
    public int getRowIndex() {
        return row;
    }

    /** @return the column index of this position */
    public int getColumnIndex() {
        return column;
    }

    /**
     * Returns a string representation in the format {row,column}.
     */
    @Override
    public String toString() {
        return "{" + row + "," + column + "}";
    }
}
