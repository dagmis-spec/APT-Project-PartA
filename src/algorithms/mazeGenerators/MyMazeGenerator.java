package algorithms.mazeGenerators;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Generates maze using the Randomized depth-first search.
 *The algorithm treats every odd-indexed cell as a room and the
 * cell between two adjacent rooms as a wall that can be knocked down.
 * It visits every room exactly once, producing a maze with exactly one path
 * between any two rooms - complex, with many dead-ends and interesting structure.</p>
 * used An iterative stack so the algorithm can handle very large mazes
 */
public class MyMazeGenerator extends AMazeGenerator {

    private static final Random RANDOM = new Random();

    // The four cardinal directions as (dRow, dCol) steps of size 2 (room to room)
    private static final int[][] DIRECTIONS = {
            {-2,  0},   // up
            { 2,  0},   // down
            { 0, -2},   // left
            { 0,  2}    // right
    };

    /**
     * Generates a maze using iterative randomised DFS (Recursive Backtracker).
     *
     * Algorithm outline:
     * The algorithm starts from a random room (odd-indexed cell), pushes it onto a stack, and repeatedly
     * picks a random unvisited neighbor two cells away, knocks down the wall between them, and pushes the neighbor;
     * when no unvisited neighbors exist it backtracks by popping the stack, until all rooms have been visited.
     *
     * @param rows    desired number of rows  (minimum 2)
     * @param columns desired number of columns (minimum 2)
     * @return a freshly generated, perfect Maze
     */
    @Override
    public Maze generate(int rows, int columns) {
        // Ensure dimensions are at least 2×2
        rows    = Math.max(rows,    2);
        columns = Math.max(columns, 2);

        Maze maze = new Maze(rows, columns);   // constructor fills everything with 1s

        // ── DFS traversal over "room" cells (odd indices) ──────────────────────
        Deque<int[]> stack = new ArrayDeque<>();

        // Pick a random starting room; rooms live on odd indices
        int startRow = randomOdd(rows);
        int startCol = randomOdd(columns);

        maze.setCellValue(startRow, startCol, 0);   // open the starting room
        stack.push(new int[]{startRow, startCol});

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int cr = current[0];
            int cc = current[1];

            // Collect unvisited neighbours (2 steps away, still walls)
            List<int[]> neighbours = getUnvisitedNeighbours(maze, cr, cc, rows, columns);

            if (!neighbours.isEmpty()) {
                // Pick a random unvisited neighbour
                int[] chosen = neighbours.get(RANDOM.nextInt(neighbours.size()));
                int nr = chosen[0];
                int nc = chosen[1];

                // Knock down the wall between current room and chosen neighbour
                maze.setCellValue((cr + nr) / 2, (cc + nc) / 2, 0);
                // Open the chosen room
                maze.setCellValue(nr, nc, 0);

                stack.push(chosen);
            } else {
                // Dead end → backtrack
                stack.pop();
            }
        }

        // ── Place entrance and exit ────────────────────────────────────────────
        // Start: random open cell on the first row (or create one)
        int sCol = findOrOpenBorderCell(maze, 0, columns);
        maze.setStartPosition(new Position(0, sCol));

        // Goal: random open cell on the last row (or create one)
        int gCol = findOrOpenBorderCell(maze, rows - 1, columns);
        maze.setGoalPosition(new Position(rows - 1, gCol));

        return maze;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Returns a list of unvisited (wall) neighbors that are 2 cells away in
     * the four cardinal directions and within bounds.
     */
    private List<int[]> getUnvisitedNeighbours(Maze maze, int row, int col,
                                               int rows, int columns) {
        List<int[]> result = new ArrayList<>(4);
        for (int[] dir : DIRECTIONS) {
            int nr = row + dir[0];
            int nc = col + dir[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < columns
                    && maze.getCellValue(nr, nc) == 1) {   // still a wall, unvisited
                result.add(new int[]{nr, nc});
            }
        }
        Collections.shuffle(result, RANDOM);   // shuffle for extra randomness
        return result;
    }

    /**
     * Picks a random open cell in the given row.
     * If none exists (can happen on even-only rows), opens a random cell.
     * @param maze the maze being built
     * @param row the border row to search
     * @param columns total number of columns
     * @return the column index of the chosen cell
     */
    private int findOrOpenBorderCell(Maze maze, int row, int columns) {
        List<Integer> openCols = new ArrayList<>();
        for (int c = 0; c < columns; c++)
            if (maze.getCellValue(row, c) == 0)
                openCols.add(c);

        if (!openCols.isEmpty())
            return openCols.get(RANDOM.nextInt(openCols.size()));

        // No open cell on this border row - force-open one
        int c = RANDOM.nextInt(columns);
        maze.setCellValue(row, c, 0);
        return c;
    }

    /**
     * Returns a random odd number in the range [1, max-1].
     * If max is too small, returns 0.
     */
    private int randomOdd(int max) {
        // count of odd indices: 1, 3, 5, … up to max-1
        int count = (max - 1) / 2;   // e.g. max=5 → indices 1,3 → count=2
        if (count <= 0) return 0;
        return 1 + 2 * RANDOM.nextInt(count);
    }
}