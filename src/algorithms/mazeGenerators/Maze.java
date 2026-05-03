package algorithms.mazeGenerators;

/**
 * Represents a 2D maze as an integer array.
 * 0 = open cell, 1 = wall.
 * Holds the maze grid, the start position, and the goal position.
 */
public class Maze {

    private final int[][] map;
    private Position startPosition;
    private Position goalPosition;

    /**
     * Constructs a Maze of the given dimensions, filled with walls - 1.
     * @param rows number of rows
     * @param columns number of columns
     */
    public Maze(int rows, int columns) {
        map = new int[rows][columns];
        // fill everything with walls
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++)
                map[r][c] = 1;
    }

    /** @return number of rows in the maze */
    public int getRows() {
        return map.length;
    }

    /** @return number of columns in the maze */
    public int getColumns() {
        return map[0].length;
    }

    /**
     * Returns the value of a specific cell (0 = open, 1 = wall).
     * @param row    row index
     * @param column column index
     * @return cell value
     */
    public int getCellValue(int row, int column) {
        return map[row][column];
    }

    /**
     * Sets the value of a specific cell.
     * @param row row index
     * @param column column index
     * @param value  0 (open) or 1 (wall)
     */
    public void setCellValue(int row, int column, int value) {
        map[row][column] = value;
    }

    /** @return the start position of the maze */
    public Position getStartPosition() {
        return startPosition;
    }

    /** @param startPosition the start position to set */
    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    /** @return the goal position of the maze */
    public Position getGoalPosition() {
        return goalPosition;
    }

    /** @param goalPosition the goal position to set */
    public void setGoalPosition(Position goalPosition) {
        this.goalPosition = goalPosition;
    }

    /**
     * Prints the maze to stdout.
     * Start cell is shown as 'S', goal cell as 'E', open cells as '0', walls as '1'.
     */
    public void print() {
        for (int r = 0; r < getRows(); r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < getColumns(); c++) {
                if (startPosition != null
                        && startPosition.getRowIndex() == r
                        && startPosition.getColumnIndex() == c) {
                    sb.append('S');
                } else if (goalPosition != null
                        && goalPosition.getRowIndex() == r
                        && goalPosition.getColumnIndex() == c) {
                    sb.append('E');
                } else {
                    sb.append(map[r][c]);
                }
                sb.append(' ');
            }
            System.out.println(sb);
        }
    }
}